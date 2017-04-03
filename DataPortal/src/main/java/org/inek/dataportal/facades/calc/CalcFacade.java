/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades.calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.DrgCalcBasics;
import org.inek.dataportal.entities.calc.PeppCalcBasics;
import org.inek.dataportal.entities.calc.CalcContact;
import org.inek.dataportal.entities.calc.DrgContentText;
import org.inek.dataportal.entities.calc.DrgHeaderText;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.entities.calc.DrgDelimitationFact;
import org.inek.dataportal.entities.calc.DrgNeonatData;
import org.inek.dataportal.entities.calc.KGLListCentralFocus;
import org.inek.dataportal.entities.calc.KGLListContentTextOps;
import org.inek.dataportal.entities.calc.KGLListCostCenter;
import org.inek.dataportal.entities.calc.KGLListCostCenterCost;
import org.inek.dataportal.entities.calc.KGLListEndoscopyAmbulant;
import org.inek.dataportal.entities.calc.KGLListEndoscopyDifferential;
import org.inek.dataportal.entities.calc.KGLListIntensivStroke;
import org.inek.dataportal.entities.calc.KGLListKstTop;
import org.inek.dataportal.entities.calc.KGLListLocation;
import org.inek.dataportal.entities.calc.KGLListMedInfra;
import org.inek.dataportal.entities.calc.KGLListObstetricsGynecology;
import org.inek.dataportal.entities.calc.KGLListRadiologyLaboratory;
import org.inek.dataportal.entities.calc.KGLListServiceProvision;
import org.inek.dataportal.entities.calc.KGLListServiceProvisionType;
import org.inek.dataportal.entities.calc.KGLListSpecialUnit;
import org.inek.dataportal.entities.calc.KGLNormalFreelancer;
import org.inek.dataportal.entities.calc.KGLNormalStationServiceDocumentation;
import org.inek.dataportal.entities.calc.KGLOpAn;
import org.inek.dataportal.entities.calc.KGLPersonalAccounting;
import org.inek.dataportal.entities.calc.KGLRadiologyService;
import org.inek.dataportal.entities.calc.KGPListContentText;
import org.inek.dataportal.entities.calc.KGPListServiceProvisionType;
import org.inek.dataportal.entities.calc.KGPPersonalAccounting;
import org.inek.dataportal.entities.calc.StatementOfParticipance;
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.enums.CalcHospitalFunction;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.AbstractDataAccess;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.entities.calc.iface.BaseIdValue;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class CalcFacade extends AbstractDataAccess {

    // <editor-fold defaultstate="collapsed" desc="CalcHospital commons">
    public List<CalcHospitalInfo> getListCalcInfo(int accountId, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        Set<Integer> accountIds = new HashSet<>();
        accountIds.add(accountId);
        return getListCalcInfo(accountIds, year, statusLow, statusHigh);
    }

    public List<CalcHospitalInfo> getListCalcInfo(Set<Integer> accountIds, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String accountCond = " in (" + accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", ")) + ") ";
        String statusCond = " between " + statusLow.getId() + " and " + statusHigh.getId();
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
                + " '" + Utils.getMessage("lblCalculationBasicsPsy") + "' as Name, biLastChanged as LastChanged\n"
                + "from calc.KGPBaseInformation\n"
                + "where biStatusId" + statusCond + " and biAccountId" + accountCond + " and biDataYear = " + year + "\n"
                + "union\n"
                + "select dmmId as Id, 3 + dmmType as [Type], dmmAccountId as AccountId, dmmDataYear as DataYear, dmmIk as IK, dmmStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblClinicalDistributionModel") + " ' + case dmmType when 0 then 'DRG' when 1 then 'PEPP' else '???' end as Name, dmmLastChanged as LastChanged\n"
                + "from calc.DistributionModelMaster\n"
                + "where dmmStatusId" + statusCond + " and dmmAccountId" + accountCond + " and dmmDataYear = " + year + "\n"
                + "order by 2, 4, 5, 8 desc";
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        @SuppressWarnings("unchecked") List<CalcHospitalInfo> infos = query.getResultList();
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
                + "where biAccountId" + accountCond + "\n"
                + "union\n"
                + "select dmmDataYear as DataYear\n"
                + "from calc.DistributionModelMaster\n"
                + "where dmmAccountId" + accountCond;
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") HashSet<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    public Set<Integer> checkAccountsForYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String statusCond = " between " + statusLow.getId() + " and " + statusHigh.getId();
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
                + "where biStatusId" + statusCond + " and biAccountId" + accountCond + " and biDataYear = " + year + "\n"
                + "union\n"
                + "select dmmAccountId as AccountId\n"
                + "from calc.DistributionModelMaster\n"
                + "where dmmStatusId" + statusCond + " and dmmAccountId" + accountCond + " and dmmDataYear = " + year;
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") HashSet<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    // </editor-fold>
    
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
    
    public List<StatementOfParticipance> listStatementOfParticipanceByIk(int ik) {
        String sql = "SELECT sop FROM StatementOfParticipance sop WHERE sop._ik = :ik ORDER BY sop._id DESC";
        TypedQuery<StatementOfParticipance> query = getEntityManager().createQuery(sql, StatementOfParticipance.class);
        query.setParameter("ik", ik);
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
        return !query.getResultList().isEmpty();
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

    public void saveStatementOfParticipanceForIcmt(StatementOfParticipance participance){
        //Hospitals
        performInsertStatementOfParticipance(participance.getIk(), 1, "Drg");
        performInsertStatementOfParticipance(participance.getIk(), 3, "Psy");
        performInsertStatementOfParticipance(participance.getIk(), 4, "Inv");
        performInsertStatementOfParticipance(participance.getIk(), 5, "Tpg");
        performInsertStatementOfParticipance(participance.getIk(), 6, "Obligatory");
        performInsertStatementOfParticipance(participance.getIk(), 7, "Obd");
        //Contacts
        performInsertUpdateContactRoleICMT(participance.getIk(), "Drg", "1", 3);
        performInsertUpdateContactRoleICMT(participance.getIk(), "Psy", "3", 12);
        performInsertUpdateContactRoleICMT(participance.getIk(), "Consultant", "1, 3, 14", 14);
    }
    
    private void performInsertStatementOfParticipance(int ik, int calcType, String column){
                //insert CalcAgreement - Vereinbarung
        String sql = "insert into CallCenterDB.dbo.ccCalcAgreement (caCustomerId, caHasAgreement, caIsInactive, caCalcTypeId) \n"
                +     "select distinct cuid, 1 agr, 0 inactive, 1 calctype \n"
                +     "from calc.StatementOfParticipance \n"
                +     "join CallCenterDB.dbo.ccCustomer on sopIk = cuik \n"
                +     "left join CallCenterDB.dbo.ccCalcAgreement on cuid = caCustomerId and caCalcTypeId = " + calcType + " \n"
                +     "where 1=1 \n"
                +     "and sopIs" + column + " = 1 \n"
                +     "and sopStatusId = 10 \n" //send to InEK
                +     "and caHasAgreement is null \n"
                +     "and sopIk = " + ik + "\n\n"
                //Pflicht KalkTyp setzen
                +     "update x \n"
                +     "set caTestCalculation = case when sopObligatoryCalcType = 2 then 1 else 0 end \n"
                +     "from calc.StatementOfParticipance \n"
                +     "join CallCenterDB.dbo.ccCustomer on sopik = cuIK \n"
                +     "join CallCenterDB.dbo.ccCalcAgreement x on caCustomerId = cuid and caCalcTypeId = 6 \n"
                +     "join CallCenterDB.dbo.ccCalcInformation on caid = ciCalcAgreementId and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                +     "where sopDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                +     "and sopStatusId = 10 \n"
                +     "and sopIsObligatory = 1 \n"
                +     "and sopIk = " + ik + "\n\n"
                +     "update y \n"
                +     "set caSimpleCalculation = case when sopObligatoryCalcType = 1 then 1 else 0 end \n"
                +     "from calc.StatementOfParticipance \n"
                +     "join CallCenterDB.dbo.ccCustomer on sopik = cuIK \n"
                +     "join CallCenterDB.dbo.ccCalcAgreement y on caCustomerId = cuid and caCalcTypeId = 6 \n"
                +     "join CallCenterDB.dbo.ccCalcInformation on caid = ciCalcAgreementId and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                +     "where sopDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                +     "and sopStatusId = 10 \n"
                +     "and sopIsObligatory = 1 \n"
                +     "and sopIk = " + ik + "\n\n"
                //insert CalcInformation - Teilnahme
                +    "insert into CallCenterDB.dbo.ccCalcInformation (ciCalcAgreementId, ciDataYear, ciParticipation) \n"
                +    "select caID, (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) datayear, 1 parti \n"
                +    "from calc.StatementOfParticipance \n"
                +    "join CallCenterDB.dbo.ccCustomer on sopIk = cuik \n"
                +    "join CallCenterDB.dbo.ccCalcAgreement on cuid = caCustomerId and caCalcTypeId = " + calcType + " \n"
                +    "left join CallCenterDB.dbo.ccCalcInformation on caID = ciCalcAgreementId \n"
                +    "where 1=1 \n"
                +    "and sopIs" + column + " = 1 \n"
                +    "and caHasAgreement = 1 \n"
                +    "and sopStatusId = 10 \n" //send to InEK
                +    "and sopIk = " + ik + "\n"
                +    "and ciCalcAgreementId is null \n\n"
                //update if participation is already set
                +    "update a \n"
                +    "set ciParticipation = sopIs" + column + " \n"
                +    "from calc.StatementOfParticipance \n"
                +    "join CallCenterDB.dbo.ccCustomer on sopIk = cuik \n"
                +    "join CallCenterDB.dbo.ccCalcAgreement on cuid = caCustomerId and caCalcTypeId = " + calcType + " \n"
                +    "join CallCenterDB.dbo.ccCalcInformation a on caId = ciCalcAgreementId and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                +    "where 1=1 \n"
                +    "and sopStatusId = 10 \n" //send to InEK
                +    "and caHasAgreement = 1 \n"
                +    "and sopIk = " + ik + "\n\n"
                //insert KVM
                +    "insert into ccCustomerCalcTypeProperty (ctpCalcInformationId, ctpPropertyId, ctpValue) \n"
                +    "select ciId, 3 kvm, case when sopCdmDrg = 1 then 'True' else 'False' end \n"
                +    "from DataPortal.calc.StatementOfParticipance \n"
                +    "join CallCenterDB.dbo.ivMapCustomerID on caCalcTypeId = " + calcType + " and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) and cuIK = sopIk \n"
                +    "left join CallCenterDB.dbo.ccCustomerCalcTypeProperty on ciId = ctpCalcInformationId and ctpPropertyId = 3 \n"
                +    "where 1=1 \n"
                +    "and sopIs" + column + " = 1 \n"
                +    "and sopStatusId = 10 \n" //send to InEK
                +    "and sopIk = " + ik + "\n"
                +    "and ctpCalcInformationId is null \n"
                +    "and caCalcTypeId in (1, 3, 6) \n"
                +    "\n \n"
                //insert Überlieger
                +    "insert into ccCustomerCalcTypeProperty (ctpCalcInformationId, ctpPropertyId, ctpValue) \n"
                +    "select ciId, 6 ueb, case when caCalcTypeId = 6    then '3 - nicht enthalten' \\n\""
                +    "                         when sopMultiyearDrg = 1 then '1 - gesamter Zeitraum' \n"
                +    "                         when sopMultiyearDrg = 2 then '2 - Zeitausschnitt' \n"
                +    "                         when sopMultiyearDrg = 3 then '3 - nicht enthalten' \n"
                +    "                         when sopMultiyearDrg = 4 then '4 - Alternatives Vorgehen' \n"
                +    "                         else '3 - nicht enthalten' \n"
                +    "end \n"
                +    "from DataPortal.calc.StatementOfParticipance \n"
                +    "join CallCenterDB.dbo.ivMapCustomerID on caCalcTypeId = " + calcType + " and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) and cuIK = sopIk \n"
                +    "left join CallCenterDB.dbo.ccCustomerCalcTypeProperty on ciId = ctpCalcInformationId and ctpPropertyId = 6 \n"
                +    "where 1=1 \n"
                +    "and sopIs" + column + " = 1 \n"
                +    "and sopStatusId = 10 \n" //send to InEK
                +    "and sopIk = " + ik + "\n"
                +    "and ctpCalcInformationId is null \n"
                +    "and caCalcTypeId in (1, 3, 6) \n"
                +    "\n \n"        
                //update kvm/überlieger
                +    "update a \n"
                +    "set ctpValue = case when ctpPropertyId = 3 then case when sopCdmDrg = 1 then 'True' else 'False' end \n"
                +    "                    when ctpPropertyId = 6 then case when sopMultiyearDrg = 1 then '1 - gesamter Zeitraum' \n"
                +    "                                                     when sopMultiyearDrg = 2 then '2 - Zeitausschnitt' \n"
                +    "                                                     when sopMultiyearDrg = 3 then '3 - nicht enthalten' \n"
                +    "                                                     when sopMultiyearDrg = 4 then '4 - Alternatives Vorgehen' \n"
                +    "                                                     else '3 - nicht enthalten' end \n"
                +    "               end \n"
                +    "from CallCenterDB.dbo.ccCustomerCalcTypeProperty a \n"
                +    "join CallCenterDB.dbo.ivMapCustomerID on caCalcTypeId = " + calcType + " and ctpCalcInformationId = ciId and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                +    "join DataPortal.calc.StatementOfParticipance on cuik = sopIk and sopDataYear = ciDataYear \n"
                +    "where 1=1 \n"
                +    "and sopIs" + column + " = 1 \n"
                +    "and sopStatusId = 10 \n"  //send zo InEK
                +    "and sopIk = " + ik + "\n"
                +    "and caCalcTypeId in (1, 3, 6) \n";
        
        Query query = getEntityManager().createNativeQuery(sql);
        //query.executeUpdate();
    }
  
    private void performInsertUpdateContactRoleICMT(int ik, String column, String calcTypes, int roleID){
        String columnCons = column;
        if(roleID == 14){
            columnCons = "WithConsultant";
        }
        String sql = ""
            //temp Tabelle mit Kontaktinfos anlegen
                +  "insert into tmp.dpContacts (cuid, coid, gender, title, firstName, lastName, mail, phone, consultantCompany, rw) \n"
                +  "select *, ROW_NUMBER() OVER (order by a.coid) rw from ("
                +  "select c.cuid, cr.coId, a.coGender gender, a.coTitle title, a.coFirstName firstName, a.coLastName lastName, a.coMail mail, a.coPhone phone, case when a.coIsConsultant = 1 then b.sopConsultantCompany else '' end consultantCompany \n"
                +  "from DataPortal.calc.Contact a \n"
                +  "join DataPortal.calc.StatementOfParticipance b on sopId = coStatementOfParticipanceId \n"
                +  "join CallCenterDB.dbo.ccCustomer c on sopik = cuik \n"
                +  "left join (select coid, coCustomerId customerID, coFirstName firstName, coLastName lastName \n"
                +  "			from CallCenterDB.dbo.ccContact a \n"
                +  "			join CallCenterDB.dbo.mapContactRole b on a.coId = b.mcrContactId \n"
                +  "			join CallCenterDB.dbo.listRole c on b.mcrRoleId = c.roId \n"
                +  "			where coIsMain = 0 \n"
                +  "			and mcrRoleId = " + roleID + "\n"
                +  ")cr on cuid = customerId and a.coFirstName = cr.firstName and a.coLastName = cr.lastName \n"
                +  "where sopIs"+ columnCons + " = 1 \n"
                +  "and sopIk = " + ik + "\n"
                +  "and sopStatusId = 10 \n"
                +  "and a.coIs"+ column + " = 1)a \n"
                +  "\n\n"
            //Temp Kontakte aktualisieren
                +  "update a \n"
                +  "set a.coid = b.coid \n"
                +  "from tmp.dpContacts a \n"
                +  "join CallCenterDB.dbo.ccContact b on cuid = coCustomerId and coFirstName = FirstName and coLastName = LastName \n"
                +  "where a.coid is null \n"
                +  "\n\n"            
            //neuen Kontakt aus DP in ICMT aufnehmen falls nicht vorhanden
                + "insert into CallCenterDB.dbo.ccContact (coCustomerId, coSexId, coTitle, coFirstName, coLastName, coIsMain, coIsActive, coDPReceiver, coInfo) \n"
                +  "select cuid, case when gender = 1 then 'F' when gender = 2 then 'H' else 'U' end gender, isnull(title, ''), firstName, lastName, 0, 1, 1, isnull(consultantCompany, '') \n"
                +  "from tmp.dpContacts \n"
                +  "where coid is null \n"
                +  "\n\n"
            //Kontaktdetails einfügen nachdem neuer Kontakt vorhanden
                +  "insert into CallCenterDB.dbo.ccContactDetails (cdContactId, cdDetails, cdContactDetailTypeId) \n"
                +  "select b.coId, phone, 'T' \n"
                +  "from tmp.dpContacts  a \n"
                +  "join CallCenterDB.dbo.ccContact b on cuId = coCustomerId and firstName = coFirstName and lastName = coLastName \n"
                +  "where a.coid is null \n"
                +  "and b.coid not in (select cdContactId from CallCenterDB.dbo.ccContactDetails where cdContactDetailTypeId = 'T') \n"
                +  "union \n"
                +  "select b.coId, mail, 'E' \n"
                +  "from tmp.dpContacts  a \n"
                +  "join CallCenterDB.dbo.ccContact b on cuId = coCustomerId and firstName = coFirstName and lastName = coLastName \n"
                +  "where a.coid is null \n"
                +  "and b.coid not in (select cdContactId from CallCenterDB.dbo.ccContactDetails where cdContactDetailTypeId = 'E') \n"
                +  "\n\n" 

            //Telefon aus DP übernehmen
                +  "update b \n"
                +  "set cdDetails = phone \n"
                +  "from tmp.dpContacts a \n"
                +  "join CallCenterDB.dbo.ccContactDetails b on coId = cdContactId and cdContactDetailTypeId = 'T' \n"
                +  "\n\n"
            //Unterschiedliche Mail (DP - ICMT) als Atlernativmail speichern
                +  "insert into CallCenterDB.dbo.ccContactDetails (cdContactId, cdDetails, cdContactDetailTypeId) \n"
                +  "select coid, cdDetails, 'A' \n"
                +  "from tmp.dpContacts a \n"
                +  "join CallCenterDB.dbo.ccContactDetails b on coId = cdContactId and cdContactDetailTypeId = 'E' \n"
                +  "where cdDetails != mail \n"
                +  "\n\n"
            //Mail aus DP setzen
                +  "update b \n"
                +  "set cdDetails = mail \n"
                +  "from tmp.dpContacts a \n"
                +  "join CallCenterDB.dbo.ccContactDetails b on coId = cdContactId and cdContactDetailTypeId = 'E' \n"
                +  "where cdDetails != mail \n"
                +  "\n\n"
            //Rolle für alle Kontakte löschen
                +  "delete a \n"
                +  "from CallCenterDB.dbo.mapContactRole a \n"
                +  "where mcrRoleId = "  + roleID + "\n"
                +  "and mcrContactId in (select coid from CallCenterDB.dbo.ccContact where coCustomerId in (select cuid from tmp.dpContacts))"
                +  "\n\n"
            //Rolle anhand DP neu setzen
                +  "insert into CallCenterDB.dbo.mapContactRole (mcrContactId, mcrRoleId) \n"
                +  "select distinct coid, " + roleID +" \n"
                +  "from tmp.dpContacts \n"
                +  "--where coid is not null \n"
                +  "\n\n"
            //Calc Report AP zuordnen
                +  "insert into CallCenterDB.dbo.mapCustomerCalcContact (mcccCalcInformationId, mcccContactId) \n"
                +  "select ciId, a.coid \n"
                +  "from tmp.dpContacts a \n"
                +  "join CallCenterDB.dbo.ivMapCustomerID b on a.cuId = b.cuId and caCalcTypeId in (" + calcTypes + ") and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                +  "left join CallCenterDB.dbo.mapCustomerCalcContact on a.coId = mcccContactId and ciId = mcccCalcInformationId \n"
                +  "where mcccContactId is null and a.coid is not null\n"
                +  "\n\n"
            //Prio setzen
                +  "update a \n"
                +  "set coPrio = case when b.coid is not null then rw else 99 end \n"
                +  "from CallCenterDB.dbo.ccContact a \n"
                +  "left join tmp.dpContacts b on a.coId = b.coId \n"
                +  "where a.coCustomerId = (select distinct cuid from tmp.dpContacts) \n"
                +  "and coIsMain = 0 \n"
                +  "\n\n"
            //Kontakte deaktivieren die keine Rollen besitzen
                +  "update a \n"
                +  "set coIsActive = 0 \n"
                +  "from CallCenterDB.dbo.ccContact a \n"
                +  "join CallCenterDB.dbo.ccCustomer b on a.coCustomerId = b.cuid \n"
                +  "left join CallCenterDB.dbo.mapContactRole on coId = mcrContactId \n"
                +  "where 1=1 \n"
                +  "and cuik = " + ik + "\n"
                +  "and mcrContactId is null \n"
                +  "and coIsMain = 0"
                +  "\n\n"
            //Temp Tabelle löschen
                +  "delete from tmp.dpContacts \n";
                        
                        
            Query query = getEntityManager().createNativeQuery(sql);
            query.executeUpdate();
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
    public Set<Integer> obtainIks4NewStatementOfParticipance(int accountId, int year, boolean testMode) {
        String sql = "select distinct cuIK\n"
                + "from CallCenterDb.dbo.ccCustomer\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId and coIsActive = 1 \n" // (2)
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId and cdContactDetailTypeId = 'E'\n" // (2)
                + "join dbo.Account on (cdDetails = acMail" + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") and acId = " + accountId + "\n" // (2) - but let InEK staff perform without this restriction
                + "join CallCenterDB.dbo.mapContactRole r1 on (r1.mcrContactId = coId) and (r1.mcrRoleId in (3, 12, 15, 16, 18, 19)" + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") \n"
                + "left join CallCenterDB.dbo.mapContactRole r2 on (r2.mcrContactId = coId) and r2.mcrRoleId = 14 " + (testMode ? " and acMail not like '%@inek-drg.de'" : "") + " \n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "left join calc.StatementOfParticipance on cuIk = sopIk and sopDataYear = " + year + "\n"
                + "where caHasAgreement = 1 and caIsInactive = 0 and caCalcTypeId in (1, 3, 6)\n"
                + "     and cuIk in (\n"
                + "		select acIk from dbo.Account where acIk > 0 and acId = " + accountId + "\n"
                + "		union \n"
                + "		select aaiIK from dbo.AccountAdditionalIK where aaiAccountId = " + accountId + "\n"
                + "	) \n"
                + "	and sopId is null \n"
                + "     and r2.mcrRoleId is null";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") HashSet<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    public List<Object[]> retrieveCurrentStatementOfParticipanceData(int ik, int dataYear) {
        String sql = "select dbo.concatenate(case caCalcTypeId when 1 then 'DRG' when 3 then 'PSY' when 4 then 'INV' when 5 then 'TPG' when 6 then 'obligatory' when 7 then 'OBD' end) as domain, \n"
                + "    max(isnull(left(dk.ctpValue, 1), 'F')) as DrgKvm,\n"
                + "    max(isnull(left(dm.ctpValue, 1), '0')) as DrgMultiyear,\n"
                + "    max(isnull(left(pk.ctpValue, 1), 'F')) as PsyKvm,\n"
                + "    max(isnull(left(pm.ctpValue, 1), '0')) as PsyMultiyear, "
                + "cuDrgDelivery, cuPsyDelivery\n"
                + "from CallCenterDB.dbo.ccCustomer\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "join CallCenterDB.dbo.ccCalcInformation on caId = ciCalcAgreementId\n"
                + "left join CallCenterDB.dbo.ccCustomerCalcTypeProperty dk on ciId = dk.ctpCalcInformationId and dk.ctpPropertyId = 3 and caCalcTypeId = 1 -- ctpPropertyId 3|6 : KVM| Multiyear; caCalcTypeId 1|3 : DRG|Psy\n"
                + "left join CallCenterDB.dbo.ccCustomerCalcTypeProperty dm on ciId = dm.ctpCalcInformationId and dm.ctpPropertyId = 6 and caCalcTypeId = 1\n"
                + "left join CallCenterDB.dbo.ccCustomerCalcTypeProperty pk on ciId = pk.ctpCalcInformationId and pk.ctpPropertyId = 3 and caCalcTypeId = 3\n"
                + "left join CallCenterDB.dbo.ccCustomerCalcTypeProperty pm on ciId = pm.ctpCalcInformationId and pm.ctpPropertyId = 6 and caCalcTypeId = 3\n"
                + "where caCalcTypeId in (1, 3, 4, 5, 6, 7) and caHasAgreement = 1 and caIsInactive = 0 and ciParticipation = 1 and ciParticipationRetreat = 0 and cuIk = " + ik + "\n"
                + "      and ciDataYear = " + dataYear + "\n"
                + "group by cuIk, cuDrgDelivery, cuPsyDelivery";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") List<Object[]> result = query.getResultList();
        return result;
    }

    public List<CalcContact> retrieveCurrentContacts(int ik, int dataYear) {
        String sql = "select case coSexId when 'F' then 1 when 'H' then 2 else 0 end as gender, "
                + "    coTitle, coFirstName, coLastName, p.cdDetails as phone, e.cdDetails as email, "
                + "    dbo.concatenate(case caCalcTypeId when 1 then 'DRG' when 3 then 'PSY' when 4 then 'INV' when 5 then 'TPG' when 6 then 'obligatory' when 7 then 'OBD' end) as domain\n"
                + "from CallCenterDB.dbo.ccCustomer\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "join CallCenterDB.dbo.ccCalcInformation on caId = ciCalcAgreementId\n"
                + "join CallCenterDB.dbo.mapCustomerCalcContact on ciId = mcccCalcInformationId\n"
                + "join CallCenterDB.dbo.ccContact on mcccContactId = coId\n"
                + "left join CallCenterDB.dbo.ccContactDetails e on coId=e.cdContactId and e.cdContactDetailTypeId = 'E'\n"
                + "left join CallCenterDB.dbo.ccContactDetails p on coId=p.cdContactId and p.cdContactDetailTypeId = 'T'\n"
                + "where caCalcTypeId in (1, 3, 4, 5, 6, 7) and caHasAgreement = 1 and caIsInactive = 0 and ciParticipation = 1 and ciParticipationRetreat = 0 and coIsActive = 1 and cuIk = " + ik + "\n"
                + "and not exists (select 1 from CallCenterDB.dbo.mapContactRole where mcrRoleId = 14 and mcrContactId = coId)\n"
                + "      and ciDataYear = " + dataYear + "\n"
                + "group by cuIk, coSexId, coTitle, coFirstName, coLastName, p.cdDetails, e.cdDetails";
        Query query = getEntityManager().createNativeQuery(sql);
        List<CalcContact> contacts = new Vector<>();
        @SuppressWarnings("unchecked") List<Object[]> objects = query.getResultList();
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
    
    // <editor-fold defaultstate="collapsed" desc="CalcBasics DRG">
    public DrgCalcBasics findCalcBasicsDrg(int id) {
        return findFresh(DrgCalcBasics.class, id);
    }
    
    public int getCalcBasicsDrgVersion(int id){
        String sql = "select biVersion from calc.KGLBaseInformation where biId = " + id;
                Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") List<Integer> result = query.getResultList();
        if (result.isEmpty()){
            return -1;
        }
        return result.get(0);
    }

    public DrgCalcBasics saveCalcBasicsDrg(DrgCalcBasics calcBasics) {
        prepareServiceProvisionTypes(calcBasics.getServiceProvisions());

        if (calcBasics.getId() == -1) {
            KGLOpAn opAn = calcBasics.getOpAn();
            calcBasics.setOpAn(null); // can not persist otherwise :(
            persist(calcBasics);
            opAn.setBaseInformationId(calcBasics.getId());
            persist(opAn);
            calcBasics.setOpAn(opAn);
            return calcBasics;
        }

        merge(calcBasics.getOpAn());
        saveNeonatData(calcBasics);  // workarround for known problem (persist saves all, merge only one new entry)
        saveDelimitationFacts(calcBasics);
        saveTopItems(calcBasics);
        saveServiceProvisions(calcBasics);
        saveEndoscopyDifferentials(calcBasics);
        saveLocations(calcBasics);
        saveSpecialUnits(calcBasics);
        saveCentralFocus(calcBasics);
        saveLaboratoryData(calcBasics);
        saveCostCenterData(calcBasics);
        savePersonalAccounting(calcBasics);
        saveObstetricsGynecologies(calcBasics);
        saveCostCenterCosts(calcBasics);
        saveRadioServices(calcBasics);
        saveEndoscopyAmbulant(calcBasics);
        saveNormalWardDocMinutes(calcBasics);
        saveIdList(calcBasics.getNormalFreelancers());
        saveIdList(calcBasics.getNormalFeeContracts());
        saveMedInfra(calcBasics);
        saveIntensiveStroke(calcBasics);
        return merge(calcBasics);
    }
    
    private void saveNormalWardDocMinutes(DrgCalcBasics calcBasics) {
        for(KGLNormalFreelancer item : calcBasics.getNormalFreelancers()) {
            if(item.getId() == -1)
                persist(item);
            else
                merge(item);
        }
    }
    
    private void saveDelimitationFacts(DrgCalcBasics calcBasics) {
        for(DrgDelimitationFact item : calcBasics.getDelimitationFacts()) {
            if(item.getId() == -1)
                persist(item);
            else
                merge(item);
        }
    }
    
    private void saveMedInfra(DrgCalcBasics calcBasics) {
        for(KGLListMedInfra medInfra : calcBasics.getMedInfras()) {
            if(medInfra.getId() == -1)
                persist(medInfra);
            else {
                merge(medInfra);
            }
        }
    }
    
    private void saveIntensiveStroke(DrgCalcBasics calcBasics) {
        for(KGLListIntensivStroke item : calcBasics.getIntensivStrokes()) {
            if(item.getId() == -1)
                persist(item);
            else {
                merge(item);
            }
        }
    }
    
    private void saveRadioServices(DrgCalcBasics calcBasics) {
        for(KGLRadiologyService rs : calcBasics.getRadiologyServices()) {
            if(rs.getId() == -1)
                persist(rs);
            else
                merge(rs);
        }
    }
    
    private void saveCostCenterCosts(DrgCalcBasics calcBasic) {
        for(KGLListCostCenterCost ccc : calcBasic.getCostCenterCosts()) {
            if(ccc.getId() == -1)
                persist(ccc);
            else
                merge(ccc);
        }
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
    
    private void saveEndoscopyAmbulant(DrgCalcBasics calcBasics) {
        for(KGLListEndoscopyAmbulant item : calcBasics.getEndoscopyAmbulant()) {
            if(item.getId() == -1)
                persist(item);
            else
                merge(item);
        }
    }

    private void saveCostCenterData(DrgCalcBasics calcBasics) {
        for (KGLListCostCenter item : calcBasics.getCostCenters()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }
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
                serviceProvision.setServiceProvisionTypeId(provisionType.getId());
            }
        }
    }

    KGLListServiceProvisionType findServiceProvisionType(String text) {
        String jpql = "select pt from KGLListServiceProvisionType pt where pt._text = :text";
        TypedQuery<KGLListServiceProvisionType> query = getEntityManager().createQuery(jpql, KGLListServiceProvisionType.class);
        query.setParameter("text", text);
        return query.getSingleResult();
    }
    
    public KGLListContentTextOps findOpsCodeByContentTextId(int contextTextId) {
        String jpql = "select cto from KGLListContentTextOps cto where cto._contentTextId = :id";
        TypedQuery<KGLListContentTextOps> query = getEntityManager().createQuery(jpql, KGLListContentTextOps.class);
        query.setParameter("id", contextTextId);
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

    private void saveObstetricsGynecologies(DrgCalcBasics calcBasics) {
        for (KGLListObstetricsGynecology item : calcBasics.getObstetricsGynecologies()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }
    }

    public Set<Integer> obtainIks4NewBasics(CalcHospitalFunction calcFunct, int accountId, int year, boolean testMode) {
        if (calcFunct == CalcHospitalFunction.CalculationBasicsDrg) {
            return obtainIks4NewBasiscsDrg(accountId, year, testMode);
        }
        return obtainIks4NewBasiscsPepp(accountId, year, testMode);
    }

    private Set<Integer> obtainIks4NewBasiscsDrg(int accountId, int year, boolean testMode) {
        String sql = "select distinct sopIk \n"
                + "from calc.StatementOfParticipance\n"
                + "join CallCenterDb.dbo.ccCustomer on sopIk = cuIK\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId and coIsActive = 1 \n" // (2)
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId and cdContactDetailTypeId = 'E'\n" // (2)
                + "join dbo.Account on (cdDetails = acMail" + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") and acId = " + accountId + "\n" // (2) - but let InEK staff perform without this restriction
                + "join CallCenterDB.dbo.mapContactRole r1 on (r1.mcrContactId = coId) and (r1.mcrRoleId in (3, 12, 15, 16, 18, 19)" + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") \n"
                + "left join CallCenterDB.dbo.mapContactRole r2 on (r2.mcrContactId = coId) and r2.mcrRoleId = 14 " + (testMode ? " and acMail not like '%@inek-drg.de'" : "") + " \n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "where caHasAgreement = 1 and caIsInactive = 0 and caCalcTypeId in (1, 3, 6)\n"
                + "     and cuIk in (\n"
                + "		select acIk from dbo.Account where acIk > 0 and acId = " + accountId + "\n"
                + "		union \n"
                + "		select aaiIK from dbo.AccountAdditionalIK where aaiAccountId = " + accountId + "\n"
                + "	) \n"
                + "     and r2.mcrRoleId is null\n"
                + "	and sopStatusId = " + WorkflowStatus.Provided.getId() + "\n" //+ " and " + (WorkflowStatus.Retired.getId() - 1) + "\n"
                + "	and sopIsDrg = 1\n"
                + "	and sopObligatoryCalcType != 1\n"
                + "	and sopDataYear = " + year + "\n"
                + "	and not exists (\n"
                + "		select 1\n"
                + "		from calc.KGLBaseInformation\n"
                + "		where biDataYear = " + year + "\n"
                + "			and sopIk = biIk\n"
                + "	)";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") Set<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    private Set<Integer> obtainIks4NewBasiscsPepp(int accountId, int year, boolean testMode) {
        String sql = "select distinct sopIk \n"
                + "from calc.StatementOfParticipance\n"
                + "join CallCenterDb.dbo.ccCustomer on sopIk = cuIK\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId and coIsActive = 1 \n" // (2)
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId and cdContactDetailTypeId = 'E'\n" // (2)
                + "join dbo.Account on (cdDetails = acMail" + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") and acId = " + accountId + "\n" // (2) - but let InEK staff perform without this restriction
                + "join CallCenterDB.dbo.mapContactRole r1 on (r1.mcrContactId = coId) and (r1.mcrRoleId in (3, 12, 15, 16, 18, 19)" + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") \n"
                + "left join CallCenterDB.dbo.mapContactRole r2 on (r2.mcrContactId = coId) and r2.mcrRoleId = 14 " + (testMode ? " and acMail not like '%@inek-drg.de'" : "") + " \n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "where caHasAgreement = 1 and caIsInactive = 0 and caCalcTypeId in (1, 3, 6)\n"
                + "     and cuIk in (\n"
                + "		select acIk from dbo.Account where acIk > 0 and acId = " + accountId + "\n"
                + "		union \n"
                + "		select aaiIK from dbo.AccountAdditionalIK where aaiAccountId = " + accountId + "\n"
                + "	) \n"
                + "     and r2.mcrRoleId is null\n"
                + "	and sopStatusId = " + WorkflowStatus.Provided.getId() + "\n" //+ " and " + (WorkflowStatus.Retired.getId() - 1) + "\n"
                + "	and sopIsPsy = 1\n"
                + "	and sopObligatoryCalcType != 1\n"
                + "	and sopDataYear = " + year + "\n"
                + "	and not exists (\n"
                + "		select 1\n"
                + "		from calc.KGPBaseInformation\n"
                + "		where biDataYear = " + year + "\n"
                + "			and sopIk = biIk\n"
                + "	)";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") Set<Integer> result = new HashSet<>(query.getResultList());
        return result;
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

    public List<KGPListServiceProvisionType> retrieveServiceProvisionTypesPepp(int year, boolean mandatoryOnly) {
        String jpql = "select pt from KGPListServiceProvisionType pt "
                + "where pt._firstYear <= :year and pt._lastYear >= :year ";
        if (mandatoryOnly) {
            jpql += " and pt._firstYear > 1900";
        }
        TypedQuery<KGPListServiceProvisionType> query = getEntityManager().createQuery(jpql, KGPListServiceProvisionType.class);
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
    
    public List<KGPListContentText> retrieveContentTextsPepp(int headerId, int year) {
        List<Integer> headerIds = new ArrayList<>();
        headerIds.add(headerId);
        return retrieveContentTextsPepp(headerIds, year);
    }

    public List<KGPListContentText> retrieveContentTextsPepp(List<Integer> headerIds, int year) {
        String jpql = "select ct from KGPListContentText ct where ct._headerTextID in :headerIds and ct._firstYear <= :year and ct._lastYear >= :year order by ct._seq";
        TypedQuery<KGPListContentText> query = getEntityManager().createQuery(jpql, KGPListContentText.class);
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

    public void delete(DrgCalcBasics calcBasics) {
        KGLOpAn opAn = calcBasics.getOpAn();
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CalcBasics PSY">
    public PeppCalcBasics retrievePriorCalcBasics(PeppCalcBasics calcBasics) {
        String jpql = "select c from PeppCalcBasics c where c._ik = :ik and c._dataYear = :year";
        TypedQuery<PeppCalcBasics> query = getEntityManager().createQuery(jpql, PeppCalcBasics.class);
        query.setParameter("ik", calcBasics.getIk());
        query.setParameter("year", calcBasics.getDataYear() - 1);
        try {
            PeppCalcBasics priorCalcBasics = query.getSingleResult();
            getEntityManager().detach(priorCalcBasics);
            return priorCalcBasics;
        } catch (Exception ex) {
            return new PeppCalcBasics();
        }
    }

    public PeppCalcBasics findCalcBasicsPepp(int id) {
        return findFresh(PeppCalcBasics.class, id);
    }
    
    public PeppCalcBasics saveCalcBasicsPepp(PeppCalcBasics calcBasics) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<PeppCalcBasics>> violations = validator.validate(calcBasics);
        for (ConstraintViolation<PeppCalcBasics> violation : violations) {
            System.out.println(violation.getMessage());
        }

        if (calcBasics.getId() == -1) {
            persist(calcBasics);
            return calcBasics;
        }
        
        saveIdList(calcBasics.getLocations());
        saveIdList(calcBasics.getDelimitationFacts());
        saveIdList(calcBasics.getServiceProvisions());
        saveIdList(calcBasics.getTherapies());
        saveIdList(calcBasics.getCostCenters());
        saveIdList(calcBasics.getPersonalAccountings());
        saveIdList(calcBasics.getStationServiceCosts());
        saveIdList(calcBasics.getKgpMedInfraList());
        saveIdList(calcBasics.getRadiologyLaboratories());

        Map<Integer, Integer> priorPersonalAccountingsAmount = new HashMap<>();
        for (KGPPersonalAccounting ppa :  retrievePriorCalcBasics(calcBasics).getPersonalAccountings()) {
            priorPersonalAccountingsAmount.put(ppa.getCostTypeId(), ppa.getAmount());
        }
        
        PeppCalcBasics merged = merge(calcBasics);
        for (KGPPersonalAccounting pa : merged.getPersonalAccountings()) {
            if (priorPersonalAccountingsAmount.containsKey(pa.getCostTypeId())) {
                pa.setPriorCostAmount(priorPersonalAccountingsAmount.get(pa.getCostTypeId()));
            }
        }
        return merged;
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
    // </editor-fold>

    public List<Account> getInekAccounts() {
        String sql = "select distinct account.*\n"
                //        String sql = "select distinct acId, acCreated, acLastModified, acIsDeactivated, acMail, acMailUnverified, acUser, acGender, acTitle, acFirstName, acLastName, acInitials, acPhone, acRoleId, acCompany, acCustomerTypeId, acIK, acStreet, acPostalCode, acTown, acCustomerPhone, acCustomerFax, acNubConfirmation, acMessageCopy, acNubInformationMail, acReportViaPortal, acDropBoxHoldTime\n"
                + "from (select biIk, biDataYear from calc.KGLBaseInformation where biStatusID between 3 and 10 \n"
                + "union select biIk, biDataYear from calc.KGPBaseInformation where biStatusID between 3 and 10 ) base\n"
                + "join CallCenterDB.dbo.ccCustomer on biIk = cuIK\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "join CallCenterDB.dbo.ccCalcInformation on caId = ciCalcAgreementId and biDataYear = ciDataYear \n"
                + "join CallCenterDB.dbo.mapCustomerReportAgent on ciId = mcraCalcInformationId\n"
                + "join CallCenterDB.dbo.ccAgent on mcraAgentId = agId\n"
                + "left join dbo.Account on agEMail = acMail\n"
                + "where agActive = 1 and agDomainId in ('O', 'E')\n"
                + "	and mcraReportTypeId in (1, 3) \n"
                + "     and biDataYear = " + Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        @SuppressWarnings("unchecked") List<Account> result = query.getResultList();
        return result;
    }

    public List<CalcHospitalInfo> getCalcBasicsForAccount(Account account) {
        String sql = "select distinct biId as Id, biType as [Type], biAccountId as AccountId, biDataYear as DataYear, biIk as IK, biStatusId as StatusId,\n"
                + "Name, biLastChanged as LastChanged\n"
                + "from (select biId, biIk, 1 as biType, biDataYear, biAccountID, biStatusId, biLastChanged, '" + Utils.getMessage("lblCalculationBasicsDrg") + "' as Name from calc.KGLBaseInformation where biStatusID between 3 and 10 "
                + "union select biId, biIk, 2 as biType, biDataYear, biAccountID, biStatusId, biLastChanged, '" + Utils.getMessage("lblCalculationBasicsPsy") + "' as Name from calc.KGPBaseInformation where biStatusID between 3 and 10) base \n"
                + "join CallCenterDB.dbo.ccCustomer on biIk = cuIK\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "join CallCenterDB.dbo.ccCalcInformation on caId = ciCalcAgreementId and biDataYear = ciDataYear \n"
                + "join CallCenterDB.dbo.mapCustomerReportAgent on ciId = mcraCalcInformationId\n"
                + "join CallCenterDB.dbo.ccAgent on mcraAgentId = agId\n"
                + "where agEMail = '" + account.getEmail() + "'\n"
                + "	and mcraReportTypeId in (1, 3) \n"
                + "     and biDataYear = " + Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        @SuppressWarnings("unchecked") List<CalcHospitalInfo> result = query.getResultList();
        return result;
    }
    
}
