/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades.calc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.entities.calc.sop.CalcContact;
import org.inek.dataportal.entities.calc.sop.StatementOfParticipance;
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.AbstractDataAccess;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class CalcSopFacade extends AbstractDataAccess {

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
    
    public void saveStatementOfParticipanceForIcmt(StatementOfParticipance participance) {
        
        //Obsolet --> Klasse --> IcmtUpdater
        //Hospitals
        //performInsertStatementOfParticipance(participance.getIk(), 1, "Drg");
        //performInsertStatementOfParticipance(participance.getIk(), 3, "Psy");
        //performInsertStatementOfParticipance(participance.getIk(), 4, "Inv");
        //performInsertStatementOfParticipance(participance.getIk(), 5, "Tpg");
        //performInsertStatementOfParticipance(participance.getIk(), 6, "Obligatory");
        //performInsertStatementOfParticipance(participance.getIk(), 7, "Obd");
        //Contacts
        //performInsertUpdateContactRoleICMT(participance.getIk(), "Drg", "1", 3);
        //performInsertUpdateContactRoleICMT(participance.getIk(), "Psy", "3", 12);
    }

    private void performInsertStatementOfParticipance(int ik, int calcType, String column) {
        String sql = "--Pflicht KalkTyp setzen\n" +
                "insert into CallCenterDB.dbo.CustomerCalcInfo "
                + "(cciCustomerId, cciValidFrom, cciValidTo, cciCalcTypeId, cciSubCalcTypeId, cciInfoTypeId, cciDate) \n" +
                "select cuId, cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-01-01', "
                + "cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-12-31', " + calcType + ", "
                + "null, case when sopObligatoryCalcType = 2 then 8 else 7 end, null \n" +
                "from calc.StatementOfParticipance \n" +
                "join CallCenterDB.dbo.ccCustomer on sopik = cuIK \n" +
                "where sopDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n" +
                "and sopStatusId = 10 \n" +
                "and sopIsObligatory = 1 \n" +
                "and sopIk = " + ik + "\n" +
                "\n" +
                "--Insert CalcInformation - Teilnahme\n" +
                "insert into CallCenterDB.dbo.CustomerCalcInfo "
                + "(cciCustomerId, cciValidFrom, cciValidTo, cciCalcTypeId, cciSubCalcTypeId, cciInfoTypeId, cciDate) \n" +
                "select cuId, cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-01-01', "
                + "cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-12-31', " + calcType + ", "
                + "null, 3, null \n" +
                "from calc.StatementOfParticipance \n" +
                "join CallCenterDB.dbo.ccCustomer on sopIk = cuik \n" +
                "left join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId and cciCalcTypeId = " + calcType + " "
                + "and cciInfoTypeId = 3 and Year(cciValidTo) = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n" +
                "where 1=1 \n" +
                "and sopIs" + column + " = 1 \n" +
                "and sopStatusId = 10 \n" +
                "and sopIk = " + ik + "\n" +
                "and cciId is null \n" +
                "\n" +
                "--insert Überlieger \n" +
                "delete a from CallCenterDB.dbo.CustomerCalcInfo a \n" +
                "join CallCenterDB.dbo.ccCustomer on cuId = cciCustomerId \n" +
                "where cciInfoTypeId in (4,5,6,15) and cuIK = " + ik + "\n" +
                "and cciCalcTypeId = " + calcType + " \n" +
                "and Year(cciValidTo) = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n" +
                "\n" +
                "insert into CallCenterDB.dbo.CustomerCalcInfo "
                + "(cciCustomerId, cciValidFrom, cciValidTo, cciCalcTypeId, cciSubCalcTypeId, cciInfoTypeId, cciDate) \n" +
                        "select cuId, cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-01-01', "
                + "cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-12-31', " + calcType + ", null, \n" +
                "case when sopMultiyearDrg = 1 then 4 \n" +
                "when sopMultiyearDrg = 2 then 5 \n" +
                "when sopMultiyearDrg = 4 then 15 \n" +
                "else 6 end , null \n" +
                "from DataPortal.calc.StatementOfParticipance \n" +
                "join CallCenterDB.dbo.ccCustomer on sopIk = cuik  \n" +
                "left join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId and cciCalcTypeId = " + calcType + " \n" +
                "and cciInfoTypeId = case when sopMultiyearDrg = 1 then 4 \n" +
                "                         when sopMultiyearDrg = 2 then 5 \n" +
                "                         when sopMultiyearDrg = 4 then 15 \n" +
                "                         else 6 end \n" +
                "and Year(cciValidTo) = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n" +
                "where 1=1 \n" +
                "and sopIs" + column + " = 1 \n" +
                "and sopStatusId = 10 \n" +
                "and sopIk = " + ik + "\n" +
                "and cciId is null \n" +
                "and " + calcType + " in (1, 3, 6) ";

        Query query = getEntityManager().createNativeQuery(sql);
        //query.executeUpdate();
    }

    private void performInsertUpdateContactRoleICMT(int ik, String column, String calcTypes, int roleID) {
        String columnCons = column;
        if (roleID == 14) {
            columnCons = "WithConsultant";
        }
        String sql = ""
                //temp Tabelle mit Kontaktinfos anlegen
                // todo: remove rw when removing prio
                + "insert into tmp.dpContacts (cuid, coid, gender, title, firstName, lastName, mail, phone, consultantCompany, rw) \n"
                + "select *, ROW_NUMBER() OVER (order by a.coid) rw from ("
                + "select c.cuid, cr.coId, a.coGender gender, a.coTitle title, a.coFirstName firstName, "
                + "    a.coLastName lastName, a.coMail mail, a.coPhone phone, "
                + "    case when a.coIsConsultant = 1 then b.sopConsultantCompany else '' end consultantCompany \n"
                + "from DataPortal.calc.Contact a \n"
                + "join DataPortal.calc.StatementOfParticipance b on sopId = coStatementOfParticipanceId \n"
                + "join CallCenterDB.dbo.ccCustomer c on sopik = cuik \n"
                + "left join (select coid, coCustomerId customerID, coFirstName firstName, coLastName lastName \n"
                + "    from CallCenterDB.dbo.ccContact a \n"
                + "    join CallCenterDB.dbo.mapContactRole b on a.coId = b.mcrContactId \n"
                + "    join CallCenterDB.dbo.listRole c on b.mcrRoleId = c.roId \n"
                + "    where coIsMain = 0 \n"
                + "    and mcrRoleId = " + roleID + "\n"
                + ")cr on cuid = customerId and a.coFirstName = cr.firstName and a.coLastName = cr.lastName \n"
                + "where sopIs" + columnCons + " = 1 \n"
                + "and sopIk = " + ik + "\n"
                + "and sopStatusId = 10 \n"
                + "and a.coIs" + column + " = 1)a \n"
                + "\n\n"
                //Temp Kontakte aktualisieren
                + "update a \n"
                + "set a.coid = b.coid \n"
                + "from tmp.dpContacts a \n"
                + "join CallCenterDB.dbo.ccContact b on cuid = coCustomerId and coFirstName = FirstName and coLastName = LastName \n"
                + "where a.coid is null \n"
                + "\n\n"
                //neuen Kontakt aus DP in ICMT aufnehmen falls nicht vorhanden
                + "insert into CallCenterDB.dbo.ccContact (coCustomerId, coSexId, coTitle, coFirstName, coLastName, "
                + "    coIsMain, coIsActive, coDPReceiver, coInfo) \n"
                + "select cuid, case when gender = 1 then 'F' when gender = 2 then 'H' else 'U' end gender, "
                + "    isnull(title, ''), firstName, lastName, 0, 1, 1, isnull(consultantCompany, '') \n"
                + "from tmp.dpContacts \n"
                + "where coid is null \n"
                + "\n\n"
                //Kontaktdetails einfügen nachdem neuer Kontakt vorhanden
                + "insert into CallCenterDB.dbo.ccContactDetails (cdContactId, cdDetails, cdContactDetailTypeId) \n"
                + "select b.coId, phone, 'T' \n"
                + "from tmp.dpContacts  a \n"
                + "join CallCenterDB.dbo.ccContact b on cuId = coCustomerId and firstName = coFirstName and lastName = coLastName \n"
                + "where a.coid is null \n"
                + "and b.coid not in (select cdContactId from CallCenterDB.dbo.ccContactDetails where cdContactDetailTypeId = 'T') \n"
                + "union \n"
                + "select b.coId, mail, 'E' \n"
                + "from tmp.dpContacts  a \n"
                + "join CallCenterDB.dbo.ccContact b on cuId = coCustomerId and firstName = coFirstName and lastName = coLastName \n"
                + "where a.coid is null \n"
                + "and b.coid not in (select cdContactId from CallCenterDB.dbo.ccContactDetails where cdContactDetailTypeId = 'E') \n"
                + "\n\n"
                //Telefon aus DP übernehmen
                + "update b \n"
                + "set cdDetails = phone \n"
                + "from tmp.dpContacts a \n"
                + "join CallCenterDB.dbo.ccContactDetails b on coId = cdContactId and cdContactDetailTypeId = 'T' \n"
                + "\n\n"
                //Unterschiedliche Mail (DP - ICMT) als Atlernativmail speichern
                + "insert into CallCenterDB.dbo.ccContactDetails (cdContactId, cdDetails, cdContactDetailTypeId) \n"
                + "select coid, cdDetails, 'A' \n"
                + "from tmp.dpContacts a \n"
                + "join CallCenterDB.dbo.ccContactDetails b on coId = cdContactId and cdContactDetailTypeId = 'E' \n"
                + "where cdDetails != mail \n"
                + "\n\n"
                //Mail aus DP setzen
                + "update b \n"
                + "set cdDetails = mail \n"
                + "from tmp.dpContacts a \n"
                + "join CallCenterDB.dbo.ccContactDetails b on coId = cdContactId and cdContactDetailTypeId = 'E' \n"
                + "where cdDetails != mail \n"
                + "\n\n"
                //Rolle für alle Kontakte löschen
                + "delete a \n"
                + "from CallCenterDB.dbo.mapContactRole a \n"
                + "where mcrRoleId = " + roleID + "\n"
                + "and mcrContactId in (select coid from CallCenterDB.dbo.ccContact where coCustomerId in (select cuid from tmp.dpContacts))"
                + "\n\n"
                //Rolle anhand DP neu setzen
                + "insert into CallCenterDB.dbo.mapContactRole (mcrContactId, mcrRoleId) \n"
                + "select distinct coid, " + roleID + " \n"
                + "from tmp.dpContacts \n"
                + "--where coid is not null \n"
                + "\n\n" +
                //Calc Report AP zuordnen
                    "declare @y int \n" +
                    "set @y = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n" +
                    "declare @c varchar \n" +
                    "set @c = '" + column + "'                \n" +
                    "insert into CallCenterDB.dbo.mapCustomerCalcInfoContact (ccicCustomerCalcInfoId,ccicContactId,ccicValidFrom,ccicValidTo) \n" +
                    "select cciId, a.coid , cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-01-01',"
                    + " cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-12-31' \n" +
                    "--select * \n" +
                    "from tmp.dpContacts a \n" +
                    "join CallCenterDB.dbo.CustomerCalcInfo  on a.cuId = cciCustomerId and "
                    + " cciCalcTypeId = case when @c = 'DRG' then 1 when @c = 'PSY' then 3 "
                    + "when @c = 'INV' then 4 when @c = 'TPG' then 5 else 7 end  and cciInfoTypeId = 13 "
                    + "and Year(cciValidTo) = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n" +
                    "left join CallCenterDB.dbo.mapCustomerCalcInfoContact on a.coId = ccicContactId and Year(ccicValidTo) = @y \n" +
                    "where ccicContactId is null and a.coid is not null "
                //Prio setzen
                // todo: remove prio
                + "update a \n"
                + "set coPrio = case when b.coid is not null then rw else 99 end \n"
                + "from CallCenterDB.dbo.ccContact a \n"
                + "left join tmp.dpContacts b on a.coId = b.coId \n"
                + "where a.coCustomerId = (select distinct cuid from tmp.dpContacts) \n"
                + "and coIsMain = 0 \n"
                + "\n\n"
//                //Kontakte deaktivieren die keine Rollen besitzen
//                + "update a \n"
//                + "set coIsActive = 0 \n"
//                + "from CallCenterDB.dbo.ccContact a \n"
//                + "join CallCenterDB.dbo.ccCustomer b on a.coCustomerId = b.cuid \n"
//                + "left join CallCenterDB.dbo.mapContactRole on coId = mcrContactId \n"
//                + "where 1=1 \n"
//                + "and cuik = " + ik + "\n"
//                + "and mcrContactId is null \n"
//                + "and coIsMain = 0"
//                + "\n\n"
                //Temp Tabelle löschen
                + "delete from tmp.dpContacts \n";

        Query query = getEntityManager().createNativeQuery(sql);
        query.executeUpdate();
    }

    /**
     * Check, whether the customers assigned to the account iks have an agreement and the account is a well known
     * contact (2).An IK is only available, if no SoP exists for the given year.
     *
     * @param accountId
     * @param year
     * @param testMode
     * @return
     */
    public Set<Integer> obtainIks4NewStatementOfParticipance(int accountId, int year, boolean testMode) {
        String sql = "select distinct cuIK\n"
                + "from CallCenterDb.dbo.ccCustomer\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId and coIsActive = 1 \n" 
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId and cdContactDetailTypeId = 'E'\n" 
                + "join dbo.Account on (cdDetails = acMail" 
                + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") and acId = " + accountId + "\n" 
                + "join CallCenterDB.dbo.mapContactRole r1 on (r1.mcrContactId = coId) and (r1.mcrRoleId in (3, 12, 15, 16, 18, 19)" 
                + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") \n"
                + "left join CallCenterDB.dbo.mapContactRole r2 on (r2.mcrContactId = coId) and r2.mcrRoleId = 14 " 
                + (testMode ? " and acMail not like '%@inek-drg.de'" : "") + " \n"
                + "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId "
                + "left join calc.StatementOfParticipance on cuIk = sopIk and sopDataYear = " + year + "\n"
                + "where cciInfoTypeId in (1,2) and Year(cciValidTo) >= " + year + " and cciCalcTypeId in (1, 3, 4, 5, 5, 6, 7)"
                + "    and cuIk in (\n"
                + "        select acIk from dbo.Account where acIk > 0 and acId = " + accountId + "\n"
                + "        union \n"
                + "        select aaiIK from dbo.AccountAdditionalIK where aaiAccountId = " + accountId + "\n"
                + "    ) \n"
                + "    and sopId is null \n"
                + "    and r2.mcrRoleId is null";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") HashSet<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    public List<Object[]> retrieveCurrentStatementOfParticipanceData(int ik, int dataYear) {
        String sql1 = "select dbo.concatenate(case cciInfoTypeId \n" +
                        "        when 1 then 'obligatory' \n" +
                        "        end) as domain, \n" +
                        "    'F' as DrgKvm, \n" +
                        "    '0' as DrgMultiyear, \n" +
                        "    'F' as PsyKvm, \n" +
                        "    '0' as PsyMultiyear, \n" +
                        "cuDrgDelivery, cuPsyDelivery \n" +
                        "from CallCenterDB.dbo.ccCustomer \n" +
                        "join CallCenterDB.dbo.CustomerCalcInfo cci on cuId = cci.cciCustomerId \n" +
                        "and Year(cci.cciValidTo) >= " + dataYear + " and cci.cciInfoTypeId = 1 \n" +
                        "where cci.cciCalcTypeId in (1, 3, 4, 5, 6, 7) \n" +
                        "and cuIK = " + ik + " \n" +
                        "group by cuIk, cuDrgDelivery, cuPsyDelivery";        
        
        Query query1 = getEntityManager().createNativeQuery(sql1);
        @SuppressWarnings("unchecked") List<Object[]> result1 = query1.getResultList();
        if(result1.size() > 0) {
            return result1;
        }
        
        
        String sql = "select dbo.concatenate(case cci.cciCalcTypeId \n" +
                    "        when 1 then 'DRG' \n" +
                    "        when 3 then 'PSY' \n" +
                    "        when 4 then 'INV' \n" +
                    "        when 5 then 'TPG' \n" +
                    "        when 6 then 'obligatory' \n" +
                    "        when 7 then 'OBD' \n" +
                    "        end) as domain, \n" +
                    "    isnull((select distinct 'T'from calc.DistributionModelMaster \n" +
                    "where dmmIk = " + ik + " \n" +
                    "and dmmDataYear = " + dataYear + " \n" +
                    "and dmmType = 0), 'F') as DrgKvm,\n" +
                    "    isnull(dm.cciInfoTypeId, '0') as DrgMultiyear,\n" +
                    "    isnull((select distinct 'T'from calc.DistributionModelMaster \n" +
                    "where dmmIk = " + ik + " \n" +
                    "and dmmDataYear = " + dataYear + " \n" +
                    "and dmmType = 1), 'F') as PsyKvm,\n" +
                    "    isnull(pm.cciInfoTypeId, '0') as PsyMultiyear, \n" +
                    "cuDrgDelivery, cuPsyDelivery \n" +
                    "from CallCenterDB.dbo.ccCustomer \n" +
                    "join CallCenterDB.dbo.CustomerCalcInfo cci on cuId = cci.cciCustomerId "
                + "and Year(cci.cciValidTo) = " + dataYear + " and cci.cciInfoTypeId = 3 \n" +
                    "left join CallCenterDB.dbo.CustomerCalcInfo dm on cuId = dm.cciCustomerId "
                + "and Year(dm.cciValidTo) = " + dataYear + " and dm.cciInfoTypeId in (4,5,6,15) and dm.cciCalcTypeId = 1 \n" +
                    "left join CallCenterDB.dbo.CustomerCalcInfo pm on cuId = pm.cciCustomerId "
                + "and Year(pm.cciValidTo) = " + dataYear + " and pm.cciInfoTypeId in (4,5,6,15) and pm.cciCalcTypeId = 3 \n" +
                    "where cci.cciCalcTypeId in (1, 3, 4, 5, 6, 7) \n" +
                    "and exists (select 1 from CallCenterDB.dbo.CustomerCalcInfo ci "
                + "where ci.cciCustomerId = cuId and Year(ci.cciValidTo) = " + dataYear + " and ci.cciInfoTypeId = 3) \n" +
                    "and cuIk = " + ik + " \n" +
                    "group by cuIk, cuDrgDelivery, cuPsyDelivery, dm.cciInfoTypeId, pm.cciInfoTypeId";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") List<Object[]> result = query.getResultList();
        return result;
    }

    public List<CalcContact> retrieveCurrentContacts(int ik, int dataYear) {
        String sql = "select case coSexId when 'F' then 1 when 'H' then 2 else 0 end as gender, \n" +
                        "    coTitle, coFirstName, coLastName, p.cdDetails as phone, e.cdDetails as email, \n" +
                        "    dbo.concatenate(case cciCalcTypeId\n" +
                        "        when 1 then 'DRG' \n" +
                        "        when 3 then 'PSY' \n" +
                        "        when 4 then 'INV' \n" +
                        "        when 5 then 'TPG'\n" +
                        "        when 7 then 'OBD' end) as domain\n" +
                        "--select *\n" +
                        "from CallCenterDB.dbo.ccCustomer\n" +
                        "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId and Year(cciValidTo) = " + dataYear + "\n" +
                        "join CallCenterDB.dbo.mapCustomerCalcInfoContact on ccicCustomerCalcInfoId = cciId "
                + "and Year(ccicValidTo) = " + dataYear +"\n" +
                        "join CallCenterDB.dbo.ccContact on ccicContactId = coId\n" +
                        "left join CallCenterDB.dbo.ccContactDetails e on coId=e.cdContactId and e.cdContactDetailTypeId = 'E'\n" +
                        "left join CallCenterDB.dbo.ccContactDetails p on coId=p.cdContactId and p.cdContactDetailTypeId = 'T'\n" +
                        "where cciCalcTypeId in (1, 3, 4, 5, 7) \n" +
                        "and exists (select 1 from CallCenterDB.dbo.CustomerCalcInfo zv where cuId = zv.cciCustomerId "
                + "and zv.cciInfoTypeId in (1, 2) and Year(zv.cciValidTo) >= " + dataYear +") \n" +
                        "and not exists (select 1 from CallCenterDB.dbo.CustomerCalcInfo zv where cuId = zv.cciCustomerId "
                + "and zv.cciInfoTypeId in (12) and Year(zv.cciValidTo) = " + dataYear +") \n" +
                        "and coIsActive = 1 \n" +
                        "and cuIk = " + ik + "\n" +
                        "group by cuIk, coSexId, coTitle, coFirstName, coLastName, p.cdDetails, e.cdDetails";
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
            LOGGER.log(Level.WARNING, "isObligateCalculation - non unique ik {0}", ik);
            return false;
        }
    }
}
