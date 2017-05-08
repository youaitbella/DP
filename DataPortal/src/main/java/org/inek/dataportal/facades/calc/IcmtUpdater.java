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
        performInsertStatementOfParticipance(participance.getIk(), 6, "Obligatory");
        performInsertStatementOfParticipance(participance.getIk(), 7, "Obd");
        //Contacts
        performInsertUpdateContactRoleICMT(participance.getIk(), "Drg", "1", 3);
        performInsertUpdateContactRoleICMT(participance.getIk(), "Psy", "3", 12);
        performInsertUpdateContactRoleICMT(participance.getIk(), "Consultant", "1, 3, 14", 14);
    }

    private void performInsertStatementOfParticipance(int ik, int calcType, String column) {
        //insert CalcAgreement - Vereinbarung
        String sql = "insert into CallCenterDB.dbo.ccCalcAgreement (caCustomerId, caHasAgreement, caIsInactive, caCalcTypeId) \n"
                + "select distinct cuid, 1 agr, 0 inactive, 1 calctype \n"
                + "from calc.StatementOfParticipance \n"
                + "join CallCenterDB.dbo.ccCustomer on sopIk = cuik \n"
                + "left join CallCenterDB.dbo.ccCalcAgreement on cuid = caCustomerId and caCalcTypeId = " + calcType + " \n"
                + "where 1=1 \n"
                + "and sopIs" + column + " = 1 \n"
                + "and sopStatusId = 10 \n" //send to InEK
                + "and caHasAgreement is null \n"
                + "and sopIk = " + ik + "\n\n"
                //Pflicht KalkTyp setzen
                + "update x \n"
                + "set caTestCalculation = case when sopObligatoryCalcType = 2 then 1 else 0 end \n"
                + "from calc.StatementOfParticipance \n"
                + "join CallCenterDB.dbo.ccCustomer on sopik = cuIK \n"
                + "join CallCenterDB.dbo.ccCalcAgreement x on caCustomerId = cuid and caCalcTypeId = 6 \n"
                + "join CallCenterDB.dbo.ccCalcInformation on caid = ciCalcAgreementId and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                + "where sopDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                + "and sopStatusId = 10 \n"
                + "and sopIsObligatory = 1 \n"
                + "and sopIk = " + ik + "\n\n"
                + "update y \n"
                + "set caSimpleCalculation = case when sopObligatoryCalcType = 1 then 1 else 0 end \n"
                + "from calc.StatementOfParticipance \n"
                + "join CallCenterDB.dbo.ccCustomer on sopik = cuIK \n"
                + "join CallCenterDB.dbo.ccCalcAgreement y on caCustomerId = cuid and caCalcTypeId = 6 \n"
                + "join CallCenterDB.dbo.ccCalcInformation on caid = ciCalcAgreementId and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                + "where sopDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                + "and sopStatusId = 10 \n"
                + "and sopIsObligatory = 1 \n"
                + "and sopIk = " + ik + "\n\n"
                //insert CalcInformation - Teilnahme
                + "insert into CallCenterDB.dbo.ccCalcInformation (ciCalcAgreementId, ciDataYear, ciParticipation) \n"
                + "select caID, (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) datayear, 1 parti \n"
                + "from calc.StatementOfParticipance \n"
                + "join CallCenterDB.dbo.ccCustomer on sopIk = cuik \n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuid = caCustomerId and caCalcTypeId = " + calcType + " \n"
                + "left join CallCenterDB.dbo.ccCalcInformation on caID = ciCalcAgreementId \n"
                + "where 1=1 \n"
                + "and sopIs" + column + " = 1 \n"
                + "and caHasAgreement = 1 \n"
                + "and sopStatusId = 10 \n" //send to InEK
                + "and sopIk = " + ik + "\n"
                + "and ciCalcAgreementId is null \n\n"
                //update if participation is already set
                + "update a \n"
                + "set ciParticipation = sopIs" + column + " \n"
                + "from calc.StatementOfParticipance \n"
                + "join CallCenterDB.dbo.ccCustomer on sopIk = cuik \n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuid = caCustomerId and caCalcTypeId = " + calcType + " \n"
                + "join CallCenterDB.dbo.ccCalcInformation a on caId = ciCalcAgreementId and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                + "where 1=1 \n"
                + "and sopStatusId = 10 \n" //send to InEK
                + "and caHasAgreement = 1 \n"
                + "and sopIk = " + ik + "\n\n"
                //insert KVM
                + "insert into ccCustomerCalcTypeProperty (ctpCalcInformationId, ctpPropertyId, ctpValue) \n"
                + "select ciId, 3 kvm, case when sopCdmDrg = 1 then 'True' else 'False' end \n"
                + "from DataPortal.calc.StatementOfParticipance \n"
                + "join CallCenterDB.dbo.ivMapCustomerID on caCalcTypeId = " + calcType + " and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) and cuIK = sopIk \n"
                + "left join CallCenterDB.dbo.ccCustomerCalcTypeProperty on ciId = ctpCalcInformationId and ctpPropertyId = 3 \n"
                + "where 1=1 \n"
                + "and sopIs" + column + " = 1 \n"
                + "and sopStatusId = 10 \n" //send to InEK
                + "and sopIk = " + ik + "\n"
                + "and ctpCalcInformationId is null \n"
                + "and caCalcTypeId in (1, 3, 6) \n"
                + "\n \n"
                //insert Überlieger
                + "insert into ccCustomerCalcTypeProperty (ctpCalcInformationId, ctpPropertyId, ctpValue) \n"
                + "select ciId, 6 ueb, case when caCalcTypeId = 6    then '3 - nicht enthalten' \\n\""
                + "                         when sopMultiyearDrg = 1 then '1 - gesamter Zeitraum' \n"
                + "                         when sopMultiyearDrg = 2 then '2 - Zeitausschnitt' \n"
                + "                         when sopMultiyearDrg = 3 then '3 - nicht enthalten' \n"
                + "                         when sopMultiyearDrg = 4 then '4 - Alternatives Vorgehen' \n"
                + "                         else '3 - nicht enthalten' \n"
                + "end \n"
                + "from DataPortal.calc.StatementOfParticipance \n"
                + "join CallCenterDB.dbo.ivMapCustomerID on caCalcTypeId = " + calcType + " and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) and cuIK = sopIk \n"
                + "left join CallCenterDB.dbo.ccCustomerCalcTypeProperty on ciId = ctpCalcInformationId and ctpPropertyId = 6 \n"
                + "where 1=1 \n"
                + "and sopIs" + column + " = 1 \n"
                + "and sopStatusId = 10 \n" //send to InEK
                + "and sopIk = " + ik + "\n"
                + "and ctpCalcInformationId is null \n"
                + "and caCalcTypeId in (1, 3, 6) \n"
                + "\n \n"
                //update kvm/überlieger
                + "update a \n"
                + "set ctpValue = case when ctpPropertyId = 3 then case when sopCdmDrg = 1 then 'True' else 'False' end \n"
                + "                    when ctpPropertyId = 6 then case when sopMultiyearDrg = 1 then '1 - gesamter Zeitraum' \n"
                + "                                                     when sopMultiyearDrg = 2 then '2 - Zeitausschnitt' \n"
                + "                                                     when sopMultiyearDrg = 3 then '3 - nicht enthalten' \n"
                + "                                                     when sopMultiyearDrg = 4 then '4 - Alternatives Vorgehen' \n"
                + "                                                     else '3 - nicht enthalten' end \n"
                + "               end \n"
                + "from CallCenterDB.dbo.ccCustomerCalcTypeProperty a \n"
                + "join CallCenterDB.dbo.ivMapCustomerID on caCalcTypeId = " + calcType + " and ctpCalcInformationId = ciId and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                + "join DataPortal.calc.StatementOfParticipance on cuik = sopIk and sopDataYear = ciDataYear \n"
                + "where 1=1 \n"
                + "and sopIs" + column + " = 1 \n"
                + "and sopStatusId = 10 \n" //send zo InEK
                + "and sopIk = " + ik + "\n"
                + "and caCalcTypeId in (1, 3, 6) \n";

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
                + "select c.cuid, cr.coId, a.coGender gender, a.coTitle title, a.coFirstName firstName, a.coLastName lastName, a.coMail mail, a.coPhone phone, case when a.coIsConsultant = 1 then b.sopConsultantCompany else '' end consultantCompany \n"
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
                + "insert into CallCenterDB.dbo.ccContact (coCustomerId, coSexId, coTitle, coFirstName, coLastName, coIsMain, coIsActive, coDPReceiver, coInfo) \n"
                + "select cuid, case when gender = 1 then 'F' when gender = 2 then 'H' else 'U' end gender, isnull(title, ''), firstName, lastName, 0, 1, 1, isnull(consultantCompany, '') \n"
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
                + "\n\n"
                //Calc Report AP zuordnen
                + "insert into CallCenterDB.dbo.mapCustomerCalcContact (mcccCalcInformationId, mcccContactId) \n"
                + "select ciId, a.coid \n"
                + "from tmp.dpContacts a \n"
                + "join CallCenterDB.dbo.ivMapCustomerID b on a.cuId = b.cuId and caCalcTypeId in (" + calcTypes + ") and ciDataYear = (select max(ldyDataYear) from CallCenterDB.dbo.listDataYear) \n"
                + "left join CallCenterDB.dbo.mapCustomerCalcContact on a.coId = mcccContactId and ciId = mcccCalcInformationId \n"
                + "where mcccContactId is null and a.coid is not null\n"
                + "\n\n"
                //Prio setzen
                // todo: remove prio
                + "update a \n"
                + "set coPrio = case when b.coid is not null then rw else 99 end \n"
                + "from CallCenterDB.dbo.ccContact a \n"
                + "left join tmp.dpContacts b on a.coId = b.coId \n"
                + "where a.coCustomerId = (select distinct cuid from tmp.dpContacts) \n"
                + "and coIsMain = 0 \n"
                + "\n\n"
                //Kontakte deaktivieren die keine Rollen besitzen
                + "update a \n"
                + "set coIsActive = 0 \n"
                + "from CallCenterDB.dbo.ccContact a \n"
                + "join CallCenterDB.dbo.ccCustomer b on a.coCustomerId = b.cuid \n"
                + "left join CallCenterDB.dbo.mapContactRole on coId = mcrContactId \n"
                + "where 1=1 \n"
                + "and cuik = " + ik + "\n"
                + "and mcrContactId is null \n"
                + "and coIsMain = 0"
                + "\n\n"
                //Temp Tabelle löschen
                + "delete from tmp.dpContacts \n";

        Query query = getEntityManager().createNativeQuery(sql);
        query.executeUpdate();
    }
    
}
