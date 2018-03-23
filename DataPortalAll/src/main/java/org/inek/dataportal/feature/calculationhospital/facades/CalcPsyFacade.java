/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital.facades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.feature.calculationhospital.entities.psy.PeppCalcBasics;
import org.inek.dataportal.feature.calculationhospital.entities.psy.KGPListContentText;
import org.inek.dataportal.feature.calculationhospital.entities.psy.KGPListServiceProvisionType;
import org.inek.dataportal.feature.calculationhospital.entities.sop.StatementOfParticipance;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.data.iface.BaseIdValue;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class CalcPsyFacade extends AbstractDataAccess {


    public Set<Integer> obtainIks4NewBasicsPepp(int accountId, int year, boolean testMode) {
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
                + "where cciInfoTypeId in (1, 2) and cciValidTo > " + year + " and cciCalcTypeId in (1, 3, 4, 6)"
                + "     and cuIk in (\n"
                + "             select acIk from dbo.Account where acIk > 0 and acId = " + accountId + "\n"
                + "             union \n"
                + "             select aaiIK from dbo.AccountAdditionalIK where aaiAccountId = " + accountId + "\n"
                + "     ) \n"
                + "     and r2.mcrRoleId is null\n"
                + "     and sopStatusId = " + WorkflowStatus.Provided.getId() + "\n" 
                + "     and sopIsPsy = 1\n"
                + "     and sopObligatoryCalcType != 1\n"
                + "     and sopDataYear = " + year + "\n"
                + "     and not exists (\n"
                + "             select 1\n"
                + "             from calc.KGPBaseInformation\n"
                + "             where biDataYear = " + year + "\n"
                + "                     and biStatusId < 200 \n"
                + "                     and sopIk = biIk\n"
                + "     )";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") Set<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }
    
    public PeppCalcBasics retrievePriorCalcBasics(PeppCalcBasics calcBasics) {
        String jpql = "select c from PeppCalcBasics c where c._ik = :ik and (c._statusId = 10 or c._statusId = 3) and c._dataYear = :year";
        TypedQuery<PeppCalcBasics> query = getEntityManager().createQuery(jpql, PeppCalcBasics.class);
        query.setParameter("ik", calcBasics.getIk());
        query.setParameter("year", calcBasics.getDataYear() - 1);
        try {
            PeppCalcBasics priorCalcBasics = query.getResultList().get(0);//query.getSingleResult();
            getEntityManager().detach(priorCalcBasics);
            return priorCalcBasics;
        } catch (Exception ex) {
            return new PeppCalcBasics();
        }
    }

    public PeppCalcBasics findCalcBasicsPepp(int id) {
        return findFresh(PeppCalcBasics.class, id);
    }

    public int getCalcBasicsPsyVersion(int id) {
        String sql = "select biVersion from calc.KGPBaseInformation where biId = " + id;
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") List<Integer> result = query.getResultList();
        if (result.isEmpty()) {
            return -1;
        }
        return result.get(0);
    }

    public PeppCalcBasics saveCalcBasicsPepp(PeppCalcBasics calcBasics) {
        if (calcBasics.getId() == -1) {
            persist(calcBasics);
            return calcBasics;
        }

        saveIdList(calcBasics.getKgpDocumentsList());
        saveIdList(calcBasics.getLocations());
        saveIdList(calcBasics.getDelimitationFacts());
        saveIdList(calcBasics.getServiceProvisions());
        saveIdList(calcBasics.getTherapies());
        saveIdList(calcBasics.getCostCenters());
        saveIdList(calcBasics.getKgpStationDepartmentList());
        saveIdList(calcBasics.getPersonalAccountings());
        saveIdList(calcBasics.getStationServiceCosts());
        saveIdList(calcBasics.getKgpMedInfraList());
        saveIdList(calcBasics.getRadiologyLaboratories());

        PeppCalcBasics merged = merge(calcBasics);
        return merged;
    }

    public void delete(PeppCalcBasics calcBasics) {
        remove(calcBasics);
    }

    public boolean existActiveCalcBasicsPsy(int ik) {
        String jpql = "select c from PeppCalcBasics c where c._ik = :ik and c._dataYear = :year and c._statusId < 10";
        TypedQuery<StatementOfParticipance> query = getEntityManager().createQuery(jpql, StatementOfParticipance.class);
        query.setParameter("ik", ik);
        query.setParameter("year", Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        return !query.getResultList().isEmpty();
    }

    public List<KGPListServiceProvisionType> retrieveServiceProvisionTypesPepp(int year, boolean mandatoryOnly) {
        String jpql = "select pt from KGPListServiceProvisionType pt "
                + "where pt._firstYear <= :year and pt._lastYear >= :year ";
        if (mandatoryOnly) {
            jpql += " and pt._firstYear > 1900";
        }
        
        jpql += " order by pt._sequence";
        TypedQuery<KGPListServiceProvisionType> query = getEntityManager().createQuery(jpql, KGPListServiceProvisionType.class);
        query.setParameter("year", year);
        return query.getResultList();
    }

    public List<KGPListContentText> retrieveContentTextsPepp(int headerId, int year) {
        List<Integer> headerIds = new ArrayList<>();
        headerIds.add(headerId);
        return retrieveContentTextsPepp(headerIds, year);
    }

    public List<KGPListContentText> retrieveContentTextsPepp(List<Integer> headerIds, int year) {
        String jpql = "select ct from KGPListContentText ct "
                + "where ct._headerTextID in :headerIds and ct._firstYear <= :year and ct._lastYear >= :year order by ct._seq";
        TypedQuery<KGPListContentText> query = getEntityManager().createQuery(jpql, KGPListContentText.class);
        query.setParameter("year", year);
        query.setParameter("headerIds", headerIds);
        return query.getResultList();
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

}
