/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades.calc;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.inek.dataportal.entities.calc.sop.StatementOfParticipance;
import org.inek.dataportal.facades.AbstractDataAccess;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class IcmtUpdater extends AbstractDataAccess {

    public void saveStatementOfParticipanceForIcmt(StatementOfParticipance participance) {
        //Hospitals
        performInsertStatementOfParticipance(participance.getIk(), 1, "Drg");
        performInsertStatementOfParticipance(participance.getIk(), 3, "Psy");
        performInsertStatementOfParticipance(participance.getIk(), 4, "Inv");
        performInsertStatementOfParticipance(participance.getIk(), 5, "Tpg");
//        performInsertStatementOfParticipance(participance.getIk(), 6, "Obligatory");
        performInsertStatementOfParticipance(participance.getIk(), 7, "Obd");
        //Contacts
        performInsertUpdateContactRoleICMT(participance.getIk(), "Drg", "1", 3);
        performInsertUpdateContactRoleICMT(participance.getIk(), "Psy", "3", 12);
        performInsertUpdateContactRoleICMTConsultant(participance.getIk(), "Consultant", "1", 14);
        performInsertUpdateContactRoleICMTConsultant(participance.getIk(), "Consultant", "3", 14);
//        performInsertUpdateContactRoleICMT(participance.getIk(), "Consultant", "1, 3, 14", 14);
    }

    private void performInsertStatementOfParticipance(int ik, int calcType, String column) {
        String sql = "--Pflicht KalkTyp setzen\n"
                + "insert into CallCenterDB.dbo.CustomerCalcInfo "
                + "(cciCustomerId, cciValidFrom, cciValidTo, cciCalcTypeId, cciSubCalcTypeId, cciInfoTypeId, cciDate) \n"
                + "select cuId, cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-01-01', "
                + "cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-12-31', " + calcType + ", "
                + "null, case when sopObligatoryCalcType = 2 then 8 else 7 end, null \n"
                + "from calc.StatementOfParticipance \n"
                + "join CallCenterDB.dbo.ccCustomer on sopik = cuIK \n"
                + "where sopDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                + "and sopStatusId = 10 \n"
                + "and sopIsObligatory = 1 \n"
                + "and sopIk = " + ik + "\n"
                + "\n"
                + "--Insert CalcInformation - Teilnahme\n"
                + "insert into CallCenterDB.dbo.CustomerCalcInfo "
                + "(cciCustomerId, cciValidFrom, cciValidTo, cciCalcTypeId, cciSubCalcTypeId, cciInfoTypeId, cciDate) \n"
                + "select cuId, cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-01-01', "
                + "cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-12-31', " + calcType + ", "
                + "null, 3, null \n"
                + "from calc.StatementOfParticipance \n"
                + "join CallCenterDB.dbo.ccCustomer on sopIk = cuik \n"
                + "left join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId and cciCalcTypeId = " + calcType + " "
                + "and cciInfoTypeId = 3 and Year(cciValidTo) = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                + "where 1=1 \n"
                + "and sopIs" + column + " = 1 \n"
                + "and sopStatusId = 10 \n"
                + "and sopIk = " + ik + "\n"
                + "and cciId is null \n"
                + "\n";
        if (calcType == 1 || calcType == 3) {
            sql += "--insert Überlieger \n"
                    + "delete a from CallCenterDB.dbo.CustomerCalcInfo a \n"
                    + "join CallCenterDB.dbo.ccCustomer on cuId = cciCustomerId \n"
                    + "where cciInfoTypeId in (4,5,6,15,20) and cuIK = " + ik + "\n"
                    + "and cciCalcTypeId = " + calcType + " \n"
                    + "and Year(cciValidTo) = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                    + "\n"
                    + "insert into CallCenterDB.dbo.CustomerCalcInfo "
                    + "(cciCustomerId, cciValidFrom, cciValidTo, cciCalcTypeId, cciSubCalcTypeId, cciInfoTypeId, cciDate, cciComment) \n"
                    + "select cuId, cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-01-01', "
                    + "cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-12-31', " + calcType + ", null, \n"
                    + "sopMultiyear" + column + ", null, case when sopMultiyear" + column + " = 15 then "
                    + "sopMultiyear" + column + "Text else null end \n"
                    + "from calc.StatementOfParticipance \n"
                    + "join CallCenterDB.dbo.ccCustomer on sopIk = cuik  \n"
                    + "left join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId and cciCalcTypeId = " + calcType + " \n"
                    + "and cciInfoTypeId = sopMultiyear" + column + " \n"
                    + "and Year(cciValidTo) = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                    + "where 1=1 \n"
                    + "and sopIs" + column + " = 1 \n"
                    + "and sopMultiyear" + column + " != 0 \n"
                    + "and sopStatusId = 10 \n"
                    + "and sopIk = " + ik + "\n"
                    + "and cciId is null \n"
                    + "and " + calcType + " in (1, 3) ";
        }

        Query query = getEntityManager().createNativeQuery(sql);
        query.executeUpdate();
    }

    private void performInsertUpdateContactRoleICMT(int ik, String column, String calcTypes, int roleID) {
        String columnCons = column;
        String sql = ""
                //temp Tabelle mit Kontaktinfos anlegen
                // todo: remove rw when removing prio
                + "insert into tmp.dpContacts (cuid, coid, gender, title, firstName, lastName, mail, phone, consultantCompany, rw) \n"
                + "select *, ROW_NUMBER() OVER (order by a.coid) rw from ("
                + "select c.cuid, cr.coId, a.coGender gender, a.coTitle title, a.coFirstName firstName, "
                + "    a.coLastName lastName, a.coMail mail, a.coPhone phone, "
                + "    case when a.coIsConsultant = 1 then b.sopConsultantCompany else '' end consultantCompany \n"
                + "from calc.Contact a \n"
                + "join calc.StatementOfParticipance b on sopId = coStatementOfParticipanceId \n"
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
                //Temp Kontakte aktualisieren
                + "update a \n"
                + "set a.coid = b.coid \n"
                + "from tmp.dpContacts a \n"
                + "join CallCenterDB.dbo.ccContact b on cuid = coCustomerId and coFirstName = FirstName and coLastName = LastName \n"
                + "where a.coid is null \n"
                + "\n\n"
                //Rolle anhand DP neu setzen
                + "insert into CallCenterDB.dbo.mapContactRole (mcrContactId, mcrRoleId) \n"
                + "select distinct coid, " + roleID + " \n"
                + "from tmp.dpContacts \n"
                + "--where coid is not null \n"
                + "\n\n" +
                //Calc Report AP zuordnen
                "--Calc Report AP zuordnen\n" +
                "--Eintrag in der CustomerCalcInfo für Ansprechpartner vornehmen \n" +
                "insert into CallCenterDB.dbo.CustomerCalcInfo(cciCustomerId,cciCalcTypeId,cciValidFrom,cciValidTo,cciInfoTypeId) \n" +
                "select cuId, " + calcTypes + " , cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-01-01', \n" +
                " cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-12-31',13 \n" +
                " from CallCenterDB.dbo.ccCustomer \n" +
                " left join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId and cciInfoTypeId = 13 "
                + "and Year(cciValidTo) = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) "
                + "and cciCalcTypeId = " + calcTypes + " \n" +
                " where cuIK = " + ik + " \n" +
                " and cciId is null \n" +
                "\n" +
                "declare @y int \n" +
                "set @y = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n" +
                "\n" +
                "insert into CallCenterDB.dbo.mapCustomerCalcInfoContact (ccicCustomerCalcInfoId,ccicContactId,ccicValidFrom,ccicValidTo) \n" +
                "select cciId, a.coid , cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-01-01', \n" +
                " cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-12-31' \n" +
                "--select * \n" +
                "from tmp.dpContacts a \n" +
                "join CallCenterDB.dbo.CustomerCalcInfo  on a.cuId = cciCustomerId and \n" +
                " cciCalcTypeId = " + calcTypes + "  and cciInfoTypeId = 13 \n" +
                "and Year(cciValidTo) = @y \n" +
                "left join CallCenterDB.dbo.mapCustomerCalcInfoContact on cciId = ccicCustomerCalcInfoId and Year(ccicValidTo) = @y \n" +
                "where (ccicContactId is null or ccicCustomerCalcInfoId != cciId) and a.coid is not null "
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
    
    private void performInsertUpdateContactRoleICMTConsultant(int ik, String column, String calcTypes, int roleID) {
        String columnCons = "WithConsultant";
        
        String sql = ""
                //temp Tabelle mit Kontaktinfos anlegen
                // todo: remove rw when removing prio
                + "insert into tmp.dpContacts (cuid, coid, gender, title, firstName, lastName, mail, phone, consultantCompany, rw) \n"
                + "select *, ROW_NUMBER() OVER (order by a.coid) rw from ("
                + "select c.cuid, cr.coId, a.coGender gender, a.coTitle title, a.coFirstName firstName, "
                + "    a.coLastName lastName, a.coMail mail, a.coPhone phone, "
                + "    case when a.coIsConsultant = 1 then b.sopConsultantCompany else '' end consultantCompany \n"
                + "from calc.Contact a \n"
                + "join calc.StatementOfParticipance b on sopId = coStatementOfParticipanceId \n"
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
                //Temp Kontakte aktualisieren
                + "update a \n"
                + "set a.coid = b.coid \n"
                + "from tmp.dpContacts a \n"
                + "join CallCenterDB.dbo.ccContact b on cuid = coCustomerId and coFirstName = FirstName and coLastName = LastName \n"
                + "where a.coid is null \n"
                + "\n\n"
                //Rolle anhand DP neu setzen
                + "insert into CallCenterDB.dbo.mapContactRole (mcrContactId, mcrRoleId) \n"
                + "select distinct coid, " + roleID + " \n"
                + "from tmp.dpContacts \n"
                + "--where coid is not null \n"
                + "\n\n" +
                //Calc Report AP zuordnen
                "--Calc Report AP zuordnen\n" +
                "--Eintrag in der CustomerCalcInfo für Ansprechpartner vornehmen \n" +
                "insert into CallCenterDB.dbo.CustomerCalcInfo(cciCustomerId,cciCalcTypeId,cciValidFrom,cciValidTo,cciInfoTypeId) \n" +
                "select cuId, " + calcTypes + " , cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-01-01', \n" +
                " cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-12-31',13 \n" +
                " from CallCenterDB.dbo.ccCustomer \n" +
                " left join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId and cciInfoTypeId = 13 "
                + "and Year(cciValidTo) = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) "
                + "and cciCalcTypeId = " + calcTypes + " \n" +
                " where cuIK = " + ik + " \n" +
                " and cciId is null \n" +
                "\n" +
                "declare @y int \n" +
                "set @y = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n" +
                "\n" +
                "insert into CallCenterDB.dbo.mapCustomerCalcInfoContact (ccicCustomerCalcInfoId,ccicContactId,ccicValidFrom,ccicValidTo) \n" +
                "select cciId, a.coid , cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-01-01', \n" +
                " cast((select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) as varchar) + '-12-31' \n" +
                "--select * \n" +
                "from tmp.dpContacts a \n" +
                "join CallCenterDB.dbo.CustomerCalcInfo  on a.cuId = cciCustomerId and \n" +
                " cciCalcTypeId = " + calcTypes + "  and cciInfoTypeId = 13 \n" +
                "and Year(cciValidTo) = @y \n" +
                "left join CallCenterDB.dbo.mapCustomerCalcInfoContact on a.coId = ccicContactId and Year(ccicValidTo) = @y \n" +
                "where (ccicContactId is null or ccicCustomerCalcInfoId != cciId) and a.coid is not null "
                //Prio setzen
                // todo: remove prio
                + "update a \n"
                + "set coPrio = case when b.coid is not null then rw else 99 end \n"
                + "from CallCenterDB.dbo.ccContact a \n"
                + "left join tmp.dpContacts b on a.coId = b.coId \n"
                + "where a.coCustomerId = (select distinct cuid from tmp.dpContacts) \n"
                + "and coIsMain = 0 \n"
                + "\n\n"
                //Temp Tabelle löschen
                + "delete from tmp.dpContacts \n";

        Query query = getEntityManager().createNativeQuery(sql);
        query.executeUpdate();
    }

}
