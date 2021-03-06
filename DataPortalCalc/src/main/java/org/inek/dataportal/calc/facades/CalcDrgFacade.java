/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.facades;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.calc.entities.drg.*;
import org.inek.dataportal.calc.entities.sop.StatementOfParticipance;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;
import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author muellermi
 */
@RequestScoped
@Transactional
public class CalcDrgFacade extends AbstractDataAccessWithActionLog {


    public DrgCalcBasics findCalcBasicsDrg(int id) {
        return findFresh(DrgCalcBasics.class, id);
    }

    public int getCalcBasicsDrgVersion(int id) {
        String sql = "select biVersion from calc.KGLBaseInformation where biId = " + id;
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") List<Integer> result = query.getResultList();
        if (result.isEmpty()) {
            return -1;
        }
        return result.get(0);
    }

    public DrgCalcBasics saveCalcBasicsDrg(DrgCalcBasics calcBasics) {
        int retry = 0;
        while (true) {
            try {
                return trySaveCalcBasicsDrg(calcBasics);
            } catch (OptimisticLockException ex) {
                // let the caller handle multiuser access
                throw ex;
            } catch (Exception ex) {
                String msg = ex.getMessage() == null ? "" : ex.getMessage();
                if (msg.length() == 0 && ex.getCause() != null) {
                    msg = "" + ex.getCause().getMessage();
                }
                Logger.getLogger("saveCalcBasicsDrg").log(Level.WARNING, "Error during saveCalcBasicsDrg:" + msg);
                if (retry++ > 2 || !msg.contains("Rerun the transaction")) {
                    throw ex;
                }
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
            }
        }
    }

    private DrgCalcBasics trySaveCalcBasicsDrg(DrgCalcBasics calcBasics) {
        prepareServiceProvisionTypes(calcBasics.getServiceProvisions());

        if (calcBasics.getId() == -1) {
            KglOpAn opAn = calcBasics.getOpAn();
            calcBasics.setOpAn(null); // can not persist otherwise :(
            persist(calcBasics);
            opAn.setBaseInformationId(calcBasics.getId());
            persist(opAn);
            calcBasics.setOpAn(opAn);
            return calcBasics;
        }

        merge(calcBasics.getOpAn());
        // workarround for known problem (persist saves all, merge only one new entry): save lists first
        saveIdList(calcBasics.getLocations());
        saveIdList(calcBasics.getSpecialUnits());
        saveIdList(calcBasics.getDelimitationFacts());
        saveIdList(calcBasics.getServiceProvisions());
        saveIdList(calcBasics.getKstTop());
        saveIdList(calcBasics.getCostCenters());
        saveIdList(calcBasics.getCostCenterCosts());
        saveIdList(calcBasics.getObstetricsGynecologies());
        saveIdList(calcBasics.getEndoscopyAmbulant());
        saveIdList(calcBasics.getEndoscopyDifferentials());
        saveIdList(calcBasics.getRadiologyLaboratories());
        saveIdList(calcBasics.getRadiologyServices());
        saveIdList(calcBasics.getNormalStationServiceDocumentations());
        saveIdList(calcBasics.getNormalStationServiceDocumentationMinutes());
        saveIdList(calcBasics.getNormalFreelancers());
        saveIdList(calcBasics.getNormalFeeContracts());
        saveIdList(calcBasics.getMedInfras());
        saveIdList(calcBasics.getNeonateData());
        saveIdList(calcBasics.getDocuments());
        saveIdList(calcBasics.getCentralFocuses());
        saveIdList(calcBasics.getIntensivStrokes());
        saveIdList(calcBasics.getPkmsAlternatives());
        saveIdList(calcBasics.getOverviewPersonals());
        saveIdList(calcBasics.getCostCenterOpAn());
        saveIdList(calcBasics.getExternalNursingStaffs());
        saveIdList(calcBasics.getExternalTechFunctServices());
        saveIdList(calcBasics.getExternalMedStaffs());
        saveIdList(calcBasics.getLiabilityInsurances());
        return merge(calcBasics);
    }

    private void prepareServiceProvisionTypes(List<KGLListServiceProvision> serviceProvisions) {
        for (KGLListServiceProvision serviceProvision : serviceProvisions) {
            if (serviceProvision.getServiceProvisionTypeId() == -1 && !serviceProvision.getDomain().trim().isEmpty()) {
                // this is a provision type the user entered. Check, whether such an entry exists and create one if needed
                KGLListServiceProvisionType provisionType;
                try {
                    provisionType = findServiceProvisionType(serviceProvision.getDomain().trim());
                } catch (NoResultException ex) {
                    provisionType = new KGLListServiceProvisionType(-1, serviceProvision.getDomain().trim(), 1900, 2099);
                    persist(provisionType);
                }
                serviceProvision.setServiceProvisionType(provisionType);
            }
        }
    }

    KGLListServiceProvisionType findServiceProvisionType(String text) {
        String jpql = "select pt from KGLListServiceProvisionType pt where pt._text = :text";
        TypedQuery<KGLListServiceProvisionType> query = getEntityManager().createQuery(jpql, KGLListServiceProvisionType.class);
        query.setParameter("text", text);
        return query.getSingleResult();
    }

    public Set<Integer> obtainIks4NewBasicsDrg(int accountId, int year, boolean testMode) {
        String sql = "select distinct sopIk \n"
                + "from calc.StatementOfParticipance\n"
                + "join CallCenterDb.dbo.ccCustomer on sopIk = cuIK\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId and coIsActive = 1 \n" // (2)
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId and cdContactDetailTypeId = 'E'\n" // (2)
                + "join dbo.Account on (cdDetails = acMail"
                + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") and acId = " + accountId + "\n"
                + "join CallCenterDB.dbo.mapContactRole r1 on (r1.mcrContactId = coId) and (r1.mcrRoleId in (3, 12, 15, 16, 18, 19)"
                + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") \n"
                + "left join CallCenterDB.dbo.mapContactRole r2 on (r2.mcrContactId = coId) and r2.mcrRoleId = 14 "
                + (testMode ? " and acMail not like '%@inek-drg.de'" : "") + " \n"
                + "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId "
                + "where cciInfoTypeId in (1,2) and cciValidTo > " + year + " and cciCalcTypeId in (1, 3, 4, 6)"
                + "     and cuIk in (\n"
                + "             select aaiIK from dbo.AccountAdditionalIK where aaiAccountId = " + accountId + "\n"
                + "     ) \n"
                + "     and r2.mcrRoleId is null\n"
                + "     and sopStatusId = " + WorkflowStatus.Provided.getId() + "\n"
                + "     and sopIsDrg = 1\n"
                + "     and sopObligatoryCalcType != 1\n"
                + "     and sopDataYear = " + year + "\n"
                + "     and not exists (\n"
                + "             select 1\n"
                + "             from calc.KGLBaseInformation\n"
                + "             where biDataYear = " + year + "\n"
                + "                     and sopIk = biIk\n"
                + "     )";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") Set<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    public DrgCalcBasics retrievePriorCalcBasics(DrgCalcBasics calcBasics) {
        String jpql = "select c from DrgCalcBasics c where c._ik = :ik and (c._statusId = 10 or c._statusId = 3)  and c._dataYear = :year";
        TypedQuery<DrgCalcBasics> query = getEntityManager().createQuery(jpql, DrgCalcBasics.class);
        query.setParameter("ik", calcBasics.getIk());
        query.setParameter("year", calcBasics.getDataYear() - 1);
        try {

            List<DrgCalcBasics> priorCalcBasics = query.getResultList();
            getEntityManager().detach(priorCalcBasics.get(0));
            return priorCalcBasics.get(0);
        } catch (Exception ex) {
            return new DrgCalcBasics();
        }
    }

    public void delete(DrgCalcBasics calcBasics) {
        KglOpAn opAn = calcBasics.getOpAn();
        if (opAn != null) {
            remove(opAn);
            calcBasics.setOpAn(null);
        }
        remove(calcBasics);
    }

    public boolean existActiveCalcBasicsDrg(int ik) {
        String jpql = "select c from DrgCalcBasics c where c._ik = :ik and c._dataYear = :year and c._statusId < 10";
        TypedQuery<StatementOfParticipance> query = getEntityManager().createQuery(jpql, StatementOfParticipance.class);
        query.setParameter("ik", ik);
        query.setParameter("year", Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        return !query.getResultList().isEmpty();
    }

    private void saveIdList(List<? extends BaseIdValue> list) {
        for (BaseIdValue item : list) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Header and content texts">
    public DrgHeaderText findCalcHeaderText(int id) {
        return findFresh(DrgHeaderText.class, id);
    }

    public List<DrgHeaderText> findAllCalcHeaderTexts() {
        return findAll(DrgHeaderText.class);
    }

    public List<DrgHeaderText> retrieveHeaderTexts(int year, int sheetId, int type) {
        String jpql = "select ht from DrgHeaderText ht "
                + "where ht._firstYear <= :year and ht._lastYear >= :year and ht._sheetId = :sheetId "
                + (type >= 0 ? "and ht._type = :type " : "")
                + "order by ht._type, ht._sequence";
        TypedQuery<DrgHeaderText> query = getEntityManager().createQuery(jpql, DrgHeaderText.class);
        query.setParameter("year", year);
        query.setParameter("sheetId", sheetId);
        if (type >= 0) {
            query.setParameter("type", type);
        }
        return query.getResultList();
    }

    public List<KGLListServiceProvisionType> retrieveServiceProvisionTypes(int year, boolean mandatoryOnly) {
        String jpql = "select pt from KGLListServiceProvisionType pt "
                + "where pt._firstYear <= :year and pt._lastYear >= :year ";
        if (mandatoryOnly) {
            jpql += " and pt._firstYear > 1900 ";
        }
        jpql += " order by pt._id";
        TypedQuery<KGLListServiceProvisionType> query = getEntityManager().createQuery(jpql, KGLListServiceProvisionType.class);
        query.setParameter("year", year);
        return query.getResultList();
    }

    public List<KGLListOverviewPersonalType> retrieveOverviewPersonalTypes(int year) {
        String jpql = "select op from KGLListOverviewPersonalType op "
                + "where op._firstYear <= :year and op._lastYear >= :year order by op._sequence";
        TypedQuery<KGLListOverviewPersonalType> query = getEntityManager().createQuery(jpql, KGLListOverviewPersonalType.class);
        query.setParameter("year", year);
        return query.getResultList();
    }

    public DrgHeaderText saveCalcHeaderText(DrgHeaderText headerText) {
        if (headerText.getId() > 0) {
            return merge(headerText);
        }
        persist(headerText);
        return headerText;
    }

    public DrgContentText findCalcContentText(int id) {
        return findFresh(DrgContentText.class, id);
    }

    public List<DrgContentText> findAllCalcContentTexts() {
        return findAll(DrgContentText.class);
    }

    public DrgContentText saveCalcContentText(DrgContentText contentText) {
        if (contentText.getId() > 0) {
            return merge(contentText);
        }
        persist(contentText);
        return contentText;
    }

    public List<DrgContentText> retrieveContentTexts(int headerId, int year) {
        List<Integer> headerIds = new ArrayList<>();
        headerIds.add(headerId);
        return retrieveContentTexts(headerIds, year);
    }

    public List<DrgContentText> retrieveContentTexts(List<Integer> headerIds, int year) {
        String jpql = "select ct from DrgContentText ct "
                + "where ct._headerTextId in :headerIds and ct._firstYear <= :year and ct._lastYear >= :year order by ct._sequence";
        TypedQuery<DrgContentText> query = getEntityManager().createQuery(jpql, DrgContentText.class);
        query.setParameter("year", year);
        query.setParameter("headerIds", headerIds);
        return query.getResultList();
    }
    // </editor-fold>

    public List<KGLLlistRadioLaboService> retrieveKglLlistRadioLaboServices() {
        String jpql = "select rs from KGLLlistRadioLaboService rs ";
        TypedQuery<KGLLlistRadioLaboService> query = getEntityManager().createQuery(jpql, KGLLlistRadioLaboService.class);
        return query.getResultList();
    }

    public List<KGLListServiceArea> retrieveKglLlistServiceAreas() {
        String jpql = "select rs from KGLListServiceArea rs ";
        TypedQuery<KGLListServiceArea> query = getEntityManager().createQuery(jpql, KGLListServiceArea.class);
        return query.getResultList();
    }

}
