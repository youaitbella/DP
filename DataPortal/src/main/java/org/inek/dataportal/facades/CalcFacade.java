/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.inek.dataportal.entities.calc.DrgCalcBasics;
import org.inek.dataportal.entities.calc.PeppCalcBasics;
import org.inek.dataportal.entities.calc.CalcContact;
import org.inek.dataportal.entities.calc.DrgContentText;
import org.inek.dataportal.entities.calc.DrgHeaderText;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.entities.calc.DrgNeonatData;
import org.inek.dataportal.entities.calc.KGLListCentralFocus;
import org.inek.dataportal.entities.calc.KGLListEndoscopyDifferential;
import org.inek.dataportal.entities.calc.KGLListKstTop;
import org.inek.dataportal.entities.calc.KGLListLocation;
import org.inek.dataportal.entities.calc.KGLListRadiologyLaboratory;
import org.inek.dataportal.entities.calc.KGLListServiceProvision;
import org.inek.dataportal.entities.calc.KGLListServiceProvisionType;
import org.inek.dataportal.entities.calc.KGLListSpecialUnit;
import org.inek.dataportal.entities.calc.KGLOpAn;
import org.inek.dataportal.entities.calc.KGLPersonalAccounting;
import org.inek.dataportal.entities.calc.StatementOfParticipance;
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.enums.CalcHospitalFunction;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class CalcFacade extends AbstractDataAccess {

    // <editor-fold defaultstate="collapsed" desc="Statement of participance">
    public StatementOfParticipance findStatementOfParticipance(int id) {
        return findFresh(StatementOfParticipance.class, id);
    }

    public StatementOfParticipance retrievePriorStatementOfParticipance(int ik, int year) {
        String jpql = "select c from StatementOfParticipance c where c._ik = :ik and c._dataYear = :year";
        TypedQuery<StatementOfParticipance> query = getEntityManager().createQuery(jpql, StatementOfParticipance.class);
        query.setParameter("ik", ik);
        query.setParameter("year", year - 1);
        try {
            StatementOfParticipance statement = query.getSingleResult();
            getEntityManager().detach(statement);
            return statement;
        } catch (Exception ex) {
            return new StatementOfParticipance();
        }
    }

    public List<StatementOfParticipance> listStatementsOfParticipance(int accountId) {
        String sql = "SELECT sop FROM StatementOfParticipance sop WHERE sop._accountId = :accountId ORDER BY sop._id DESC";
        TypedQuery<StatementOfParticipance> query = getEntityManager().createQuery(sql, StatementOfParticipance.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public void delete(StatementOfParticipance statement) {
        remove(statement);
    }

    public boolean existActiveStatementOfParticipance(int ik) {
        String jpql = "select c from StatementOfParticipance c where c._ik = :ik and c._dataYear = :year and c._statusId < 10";
        TypedQuery<StatementOfParticipance> query = getEntityManager().createQuery(jpql, StatementOfParticipance.class);
        query.setParameter("ik", ik);
        query.setParameter("year", Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        return query.getResultList().size() == 1;
    }

    
    public StatementOfParticipance saveStatementOfParticipance(StatementOfParticipance statementOfParticipance) {
        if (statementOfParticipance.getStatus() == WorkflowStatus.Unknown) {
            statementOfParticipance.setStatus(WorkflowStatus.New);
        }

        removeEmptyContacts(statementOfParticipance);

        if (statementOfParticipance.getId() == -1) {
            persist(statementOfParticipance);
            return statementOfParticipance;
        }
        // whilst persist stores all contacts of the list,
        // merge only stores the first new contact (and replaces the other new by a copy of the first)
        // this seems to be a bug?
        // workarround: separately persist all new contacts before
        for (CalcContact contact : statementOfParticipance.getContacts()) {
            contact.setStatementOfParticipanceId(statementOfParticipance.getId());
            if (contact.getId() < 0) {
                persist(contact);
            }
        }
        StatementOfParticipance statement = merge(statementOfParticipance);
        return statement;
    }

    public void removeEmptyContacts(StatementOfParticipance statement) {
        List<CalcContact> contacts = statement.getContacts();
        for (int i = contacts.size() - 1; i >= 0; i--) {
            CalcContact contact = contacts.get(i);
            if (contact.isEmpty()) {
                contacts.remove(i);
            }
        }
    }

    public List<CalcHospitalInfo> getListCalcInfo(int accountId, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        Set<Integer> accountIds = new HashSet<>();
        accountIds.add(accountId);
        return getListCalcInfo(accountIds, year, statusLow, statusHigh);
    }

    public List<CalcHospitalInfo> getListCalcInfo(Set<Integer> accountIds, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String accountCond = " in (" + accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", ")) + ") ";
        String statusCond = " between " + statusLow.getValue() + " and " + statusHigh.getValue();
        String sql = "select sopId as Id, 0 as [Type], sopAccountId as AccountId, sopDataYear as DataYear, sopIk as IK, sopStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblStatementOfParticipance") + "' as Name, sopLastChanged as LastChanged\n"
                + "from calc.StatementOfParticipance\n"
                + "where sopStatusId" + statusCond + " and sopAccountId" + accountCond + " and sopDataYear = " + year + "\n"
                + "union\n"
                + "select biId as Id, 1 as [Type], biAccountId as AccountId, biDataYear as DataYear, biIk as IK, biStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblCalculationBasicsDrg") + "' as Name, biLastChanged as LastChanged\n"
                + "from calc.KGLBaseInformation\n"
                + "where biStatusId" + statusCond + " and biAccountId" + accountCond + " and biDataYear = " + year + "\n"
                + "union\n"
                + "select biId as Id, 2 as [Type], biAccountId as AccountId, biDataYear as DataYear, biIk as IK, biStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblCalculationBasicsPepp") + "' as Name, biLastChanged as LastChanged\n"
                + "from calc.KGPBaseInformation\n"
                + "where biStatusId" + statusCond + " and biAccountId" + accountCond + " and biDataYear = " + year + "\n"
                + "order by 2, 4, 5, 8 desc";
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        List<CalcHospitalInfo> infos = query.getResultList();
        return infos;
    }

    public Set<Integer> getCalcYears(Set<Integer> accountIds) {
        String accountCond = " in (" + accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", ")) + ") ";
        String sql = "select sopDataYear as DataYear\n"
                + "from calc.StatementOfParticipance\n"
                + "where sopAccountId" + accountCond + "\n"
                + "union\n"
                + "select biDataYear as DataYear\n"
                + "from calc.KGLBaseInformation\n"
                + "where biAccountId" + accountCond + "\n"
                + "union\n"
                + "select biDataYear as DataYear\n"
                + "from calc.KGPBaseInformation\n"
                + "where biAccountId" + accountCond;
        Query query = getEntityManager().createNativeQuery(sql);
        return new HashSet<>(query.getResultList());
    }

    public Set<Integer> checkAccountsForYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String statusCond = " between " + statusLow.getValue() + " and " + statusHigh.getValue();
        String accountCond = " in (" + accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", ")) + ") ";
        String sql = "select sopAccountId as AccountId\n"
                + "from calc.StatementOfParticipance\n"
                + "where sopStatusId" + statusCond + " and sopAccountId" + accountCond + " and sopDataYear = " + year + "\n"
                + "union\n"
                + "select biAccountId as AccountId\n"
                + "from calc.KGLBaseInformation\n"
                + "where biStatusId" + statusCond + " and biAccountId" + accountCond + " and biDataYear = " + year + "\n"
                + "union\n"
                + "select biAccountId as AccountId\n"
                + "from calc.KGPBaseInformation\n"
                + "where biStatusId" + statusCond + " and biAccountId" + accountCond + " and biDataYear = " + year;
        Query query = getEntityManager().createNativeQuery(sql);
        return new HashSet<>(query.getResultList());
    }

    /**
     * Check, whether the customers assigned to the account iks have an
     * agreement and the account is a well known contact (2) An IK is only
     * available, if no SoP exists for the given year
     *
     * @param accountId
     * @param year
     * @return
     */
    public Set<Integer> obtainIks4NewStatementOfParticipance(int accountId, int year) {
        String sql = "select distinct cuIK\n"
                + "from CallCenterDb.dbo.ccCustomer\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId\n" // (2)
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId and cdContactDetailTypeId = 'E'\n" // (2)
                + "join dbo.Account on (cdDetails = acMail or acMail like '%@inek-drg.de') and acId = " + accountId + "\n" // (2) - but let InEK staff perform without this restriction
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "left join calc.StatementOfParticipance on cuIk = sopIk and sopDataYear = " + year + "\n"
                + "where caHasAgreement = 1 and caIsInactive = 0\n"
                + "     and cuIk in (\n"
                + "		select acIk from dbo.Account where acIk > 0 and acId = " + accountId + "\n"
                + "		union \n"
                + "		select aaiIK from dbo.AccountAdditionalIK where aaiAccountId = " + accountId + "\n"
                + "	) \n"
                + "	and sopId is null";
        Query query = getEntityManager().createNativeQuery(sql);
        return new HashSet<>(query.getResultList());
    }

    public List<Object[]> retrieveCurrentStatementOfParticipanceData(int ik) {
        String sql = "select dbo.concatenate(case caCalcTypeId when 1 then 'DRG' when 3 then 'PSY' when 4 then 'INV' when 5 then 'TPG' when 6 then 'obligatory' when 7 then 'OBD' end) as domain, \n"
                + "max(isnull(left(dk.ctpValue, 1), 'F')) as DrgKvm,\n"
                + "max(isnull(left(dm.ctpValue, 1), '0')) as DrgMultiyear,\n"
                + "max(isnull(left(pk.ctpValue, 1), 'F')) as PsyKvm,\n"
                + "max(isnull(left(pm.ctpValue, 1), '0')) as PsyMultiyear\n"
                + "from CallCenterDB.dbo.ccCustomer\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "join CallCenterDB.dbo.ccCalcInformation on caId = ciCalcAgreementId\n"
                + "left join CallCenterDB.dbo.ccCustomerCalcTypeProperty dk on ciId = dk.ctpCalcInformationId and dk.ctpPropertyId = 3 and caCalcTypeId = 1 -- ctpPropertyId 3|6 : KVM| Multiyear; caCalcTypeId 1|3 : DRG|Psy\n"
                + "left join CallCenterDB.dbo.ccCustomerCalcTypeProperty dm on ciId = dm.ctpCalcInformationId and dm.ctpPropertyId = 6 and caCalcTypeId = 1\n"
                + "left join CallCenterDB.dbo.ccCustomerCalcTypeProperty pk on ciId = pk.ctpCalcInformationId and pk.ctpPropertyId = 3 and caCalcTypeId = 3\n"
                + "left join CallCenterDB.dbo.ccCustomerCalcTypeProperty pm on ciId = pm.ctpCalcInformationId and pm.ctpPropertyId = 6 and caCalcTypeId = 3\n"
                + "where caCalcTypeId in (1, 3, 4, 5, 6, 7) and caHasAgreement = 1 and caIsInactive = 0 and ciParticipation = 1 and ciParticipationRetreat = 0 and cuIk = " + ik + "\n"
                + "group by cuIk";
        Query query = getEntityManager().createNativeQuery(sql);
        return query.getResultList();
    }

    public List<CalcContact> retrieveCurrentContacts(int ik) {
        String sql = "select case coSexId when 'F' then 1 when 'H' then 2 else 0 end as gender, "
                + "coTitle, coFirstName, coLastName, p.cdDetails as phone, e.cdDetails as email, "
                + "dbo.concatenate(case caCalcTypeId when 1 then 'DRG' when 3 then 'PSY' when 4 then 'INV' when 5 then 'TPG' when 6 then 'obligatory' when 7 then 'OBD' end) as domain\n"
                + "from CallCenterDB.dbo.ccCustomer\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "join CallCenterDB.dbo.ccCalcInformation on caId = ciCalcAgreementId\n"
                + "join CallCenterDB.dbo.mapCustomerCalcContact on ciId = mcccCalcInformationId\n"
                + "join CallCenterDB.dbo.ccContact on mcccContactId = coId\n"
                + "left join CallCenterDB.dbo.ccContactDetails e on coId=e.cdContactId and e.cdContactDetailTypeId = 'E'\n"
                + "left join CallCenterDB.dbo.ccContactDetails p on coId=p.cdContactId and p.cdContactDetailTypeId = 'T'\n"
                + "where caCalcTypeId in (1, 3, 4, 5, 6, 7) and caHasAgreement = 1 and caIsInactive = 0 and ciParticipation = 1 and ciParticipationRetreat = 0 and coIsActive = 1 and cuIk = " + ik + "\n"
                + "and not exists (select 1 from CallCenterDB.dbo.mapContactRole where mcrRoleId = 14 and mcrContactId = coId)\n"
                + "group by cuIk, coSexId, coTitle, coFirstName, coLastName, p.cdDetails, e.cdDetails";
        Query query = getEntityManager().createNativeQuery(sql);
        List<CalcContact> contacts = new Vector<>();
        List<Object[]> objects = query.getResultList();
        for (Object[] obj : objects) {
            CalcContact contact = new CalcContact();
            contact.setGender((int) obj[0]);
            contact.setTitle((String) obj[1]);
            contact.setFirstName((String) obj[2]);
            contact.setLastName((String) obj[3]);
            contact.setPhone((String) obj[4]);
            contact.setMail((String) obj[5]);
            String domains = (String) obj[6];
            if (!domains.contains("obligatory")) {
                contact.setDrg(domains.contains("DRG"));
                contact.setPsy(domains.contains("PSY"));
                contact.setInv(domains.contains("INV"));
                contact.setTpg(domains.contains("TPG"));
                contact.setObd(domains.contains("OBD"));
            }
            contacts.add(contact);
        }
        return contacts;
    }

    public boolean isObligateCalculation(int ik, int year) {
        String jpql = "select c from Customer c where c._ik = :ik";
        TypedQuery<Customer> query = getEntityManager().createQuery(jpql, Customer.class);
        query.setParameter("ik", ik);
        try {
            Customer customer = query.getSingleResult();
            return customer.getObligateCalculationYear() > year - 5;
        } catch (Exception ex) {
            _logger.log(Level.WARNING, "isObligateCalculation - non unique ik {0}", ik);
            return false;
        }
    }
    // </editor-fold>

    public DrgCalcBasics findCalcBasicsDrg(int id) {
        return findFresh(DrgCalcBasics.class, id);
    }

    public PeppCalcBasics findCalcBasicsPepp(int id) {
        return findFresh(PeppCalcBasics.class, id);
    }

    public DrgCalcBasics saveCalcBasicsDrg(DrgCalcBasics calcBasics) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<DrgCalcBasics>> violations = validator.validate(calcBasics);
        for (ConstraintViolation<DrgCalcBasics> violation : violations) {
            System.out.println(violation.getMessage());
        }

        prepareServiceProvisionTypes(calcBasics.getServiceProvisions());

        if (calcBasics.getId() == -1) {
            KGLOpAn opAn = calcBasics.getOpAn();
            calcBasics.setOpAn(null); // can not persist otherwise :(
            persist(calcBasics);
            opAn.setBaseInformationID(calcBasics.getId());
            persist(opAn);
            calcBasics.setOpAn(opAn);
            return calcBasics;
        }

        merge(calcBasics.getOpAn());
        saveNeonatData(calcBasics);  // workarround for known problem (persist saves all, merge only one new entry)
        saveTopItems(calcBasics);
        saveServiceProvisions(calcBasics);
        saveEndoscopyDifferentials(calcBasics);
        saveLocations(calcBasics);
        saveSpecialUnits(calcBasics);
        saveCentralFocus(calcBasics);
        saveLaboratoryData(calcBasics);
        savePersonalAccounting(calcBasics);
        return merge(calcBasics);
    }
    
    private void saveLaboratoryData(DrgCalcBasics calcBasics) {
        for (KGLListRadiologyLaboratory item : calcBasics.getRadiologyLaboratories()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }
    }

    private void prepareServiceProvisionTypes(List<KGLListServiceProvision> serviceProvisions) {
        for (KGLListServiceProvision serviceProvision : serviceProvisions) {
            if (serviceProvision.getServiceProvisionTypeID() == -1 && !serviceProvision.getDomain().trim().isEmpty()) {
                // this is a provision type the user entered. Check, whether such an entry exists and create one if needed
                KGLListServiceProvisionType provisionType;
                try {
                    provisionType = findServiceProvisionType(serviceProvision.getDomain().trim());
                } catch (NoResultException ex) {
                    provisionType = new KGLListServiceProvisionType(-1, serviceProvision.getDomain().trim(), 1900, 2099);
                    persist(provisionType);
                }
                serviceProvision.setServiceProvisionType(provisionType);
                serviceProvision.setServiceProvisionTypeID(provisionType.getId());
            }
        }
    }

    KGLListServiceProvisionType findServiceProvisionType(String text) {
        String jpql = "select pt from KGLListServiceProvisionType pt where pt._text = :text";
        TypedQuery<KGLListServiceProvisionType> query = getEntityManager().createQuery(jpql, KGLListServiceProvisionType.class);
        query.setParameter("text", text);
        return query.getSingleResult();
    }

    private void saveNeonatData(DrgCalcBasics calcBasics) {
        for (DrgNeonatData item : calcBasics.getNeonateData()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }
    }

    private void saveTopItems(DrgCalcBasics calcBasics) {
        for (KGLListKstTop item : calcBasics.getKstTop()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }
    }

    private void saveLocations(DrgCalcBasics calcBasics) {
        for (KGLListLocation loc : calcBasics.getLocations()) {
            if (loc.getId() == -1) {
                persist(loc);
            } else {
                merge(loc);
            }
        }
    }

    private void saveSpecialUnits(DrgCalcBasics calcBasics) {
        for (KGLListSpecialUnit su : calcBasics.getSpecialUnits()) {
            if (su.getId() == -1) {
                persist(su);
            } else {
                merge(su);
            }
        }
    }

    private void saveCentralFocus(DrgCalcBasics calcBasics) {
        for (KGLListCentralFocus cf : calcBasics.getCentralFocuses()) {
            if (cf.getId() == -1) {
                persist(cf);
            } else {
                merge(cf);
            }
        }
    }

    private void saveServiceProvisions(DrgCalcBasics calcBasics) {
        for (KGLListServiceProvision item : calcBasics.getServiceProvisions()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }
    }

    private void saveEndoscopyDifferentials(DrgCalcBasics calcBasics) {
        for (KGLListEndoscopyDifferential item : calcBasics.getEndoscopyDifferentials()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }
    }
    
        private void savePersonalAccounting(DrgCalcBasics calcBasics) {
        for (KGLPersonalAccounting item : calcBasics.getPersonalAccountings()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }
    }

    public Set<Integer> obtainIks4NewBasiscs(CalcHospitalFunction calcFunct, Set<Integer> accountIds, int year) {
        if (calcFunct == CalcHospitalFunction.CalculationBasicsDrg) {
            return obtainIks4NewBasiscsDrg(accountIds, year);
        }
        return obtainIks4NewBasiscsPepp(accountIds, year);
    }

    private Set<Integer> obtainIks4NewBasiscsDrg(Set<Integer> accountIds, int year) {
        String accountList = accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", "));
        String sql = "select distinct sopIk \n"
                + "from calc.StatementOfParticipance\n"
                + "join CallCenterDb.dbo.ccCustomer on sopIk = cuIK\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId\n"
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId and cdContactDetailTypeId = 'E'\n"
                + "join dbo.Account on (cdDetails = acMail or acMail like '%@inek-drg.de') and acId in (" + accountList + ")\n" // but let InEK staff test without this restriction
                + "where sopAccountId in (" + accountList + ")\n"
                + "	and sopStatusId between " + WorkflowStatus.Provided.getValue() + " and " + (WorkflowStatus.Retired.getValue() - 1) + "\n"
                + "	and sopIsDrg=1\n"
                + "	and sopDataYear = " + year + "\n"
                + "	and not exists (\n"
                + "		select 1\n"
                + "		from calc.KGLBaseInformation\n"
                + "		where biDataYear = " + year + "\n"
                + "			and sopIk = biIk\n"
                + "	)";
        Query query = getEntityManager().createNativeQuery(sql);
        return new HashSet<>(query.getResultList());
    }

    private Set<Integer> obtainIks4NewBasiscsPepp(Set<Integer> accountIds, int year) {
        String accountList = accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", "));
        String sql = "select distinct sopIk \n"
                + "from calc.StatementOfParticipance\n"
                + "join CallCenterDb.dbo.ccCustomer on sopIk = cuIK\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId\n"
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId and cdContactDetailTypeId = 'E'\n"
                + "join dbo.Account on (cdDetails = acMail or acMail like '%@inek-drg.de') and acId in (" + accountList + ")\n" // but let InEK staff test without this restriction
                + "where sopAccountId in (" + accountList + ")\n"
                + "	and sopStatusId between " + WorkflowStatus.Provided.getValue() + " and " + (WorkflowStatus.Retired.getValue() - 1) + "\n"
                + "	and sopIsPsy=1\n"
                + "	and sopDataYear = " + year + "\n"
                + "	and not exists (\n"
                + "		select 1\n"
                + "		from calc.KGPBaseInformation\n"
                + "		where biDataYear = " + year + "\n"
                + "			and sopIk = biIk\n"
                + "	)";
        Query query = getEntityManager().createNativeQuery(sql);
        return new HashSet<>(query.getResultList());
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
            jpql += " and pt._firstYear > 1900";
        }
        TypedQuery<KGLListServiceProvisionType> query = getEntityManager().createQuery(jpql, KGLListServiceProvisionType.class);
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
        String jpql = "select ct from DrgContentText ct where ct._headerTextId in :headerIds and ct._firstYear <= :year and ct._lastYear >= :year order by ct._sequence";
        TypedQuery<DrgContentText> query = getEntityManager().createQuery(jpql, DrgContentText.class);
        query.setParameter("year", year);
        query.setParameter("headerIds", headerIds);
        return query.getResultList();
    }
    // </editor-fold>

    public DrgCalcBasics retrievePriorCalcBasics(DrgCalcBasics calcBasics) {
        String jpql = "select c from DrgCalcBasics c where c._ik = :ik and c._dataYear = :year";
        TypedQuery<DrgCalcBasics> query = getEntityManager().createQuery(jpql, DrgCalcBasics.class);
        query.setParameter("ik", calcBasics.getIk());
        query.setParameter("year", calcBasics.getDataYear() - 1);
        try {
            DrgCalcBasics priorCalcBasics = query.getSingleResult();
            getEntityManager().detach(priorCalcBasics);
            return priorCalcBasics;
        } catch (Exception ex) {
            return new DrgCalcBasics();
        }
    }

    public void saveCalcBasicsPepp(PeppCalcBasics calcBasics) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void delete(DrgCalcBasics calcBasics) {
        KGLOpAn opAn = calcBasics.getOpAn();
        if (opAn != null) {
            remove(opAn);
            calcBasics.setOpAn(null);
        }
        remove(calcBasics);
    }

    public void delete(PeppCalcBasics calcBasics) {
        remove(calcBasics);
    }

}
