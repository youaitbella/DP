/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.calc.DrgCalcBasics;
import org.inek.dataportal.entities.calc.PeppCalcBasics;
import org.inek.dataportal.entities.calc.CalcContact;
import org.inek.dataportal.entities.calc.DrgContentText;
import org.inek.dataportal.entities.calc.DrgHeaderText;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.entities.calc.DrgNeonatData;
import org.inek.dataportal.entities.calc.StatementOfParticipance;
import org.inek.dataportal.enums.CalcHospitalFunction;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Stateless
public class CalcFacade extends AbstractDataAccess {

    // <editor-fold defaultstate="collapsed" desc="Statement of participance">
    public StatementOfParticipance findStatementOfParticipance(int id) {
        return findFresh(StatementOfParticipance.class, id);
    }

    public List<StatementOfParticipance> listStatementsOfParticipance(int accountId) {
        String sql = "SELECT sop FROM StatementOfParticipance sop WHERE sop._accountId = :accountId ORDER BY sop._id DESC";
        TypedQuery<StatementOfParticipance> query = getEntityManager().createQuery(sql, StatementOfParticipance.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
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
                + " '" + Utils.getMessage("lblStatementOfParticipance") + "' as Name\n"
                + "from calc.StatementOfParticipance\n"
                + "where sopStatusId" + statusCond + " and sopAccountId" + accountCond + " and sopDataYear = " + year + "\n"
                + "union\n"
                + "select biId as Id, 1 as [Type], biAccountId as AccountId, biDataYear as DataYear, biIk as IK, biStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblCalculationBasicsDrg") + "' as Name\n"
                + "from calc.KGLBaseInformation\n"
                + "where biStatusId" + statusCond + " and biAccountId" + accountCond + " and biDataYear = " + year + "\n"
                + "union\n"
                + "select biId as Id, 2 as [Type], biAccountId as AccountId, biDataYear as DataYear, biIk as IK, biStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblCalculationBasicsPepp") + "' as Name\n"
                + "from calc.KGPBaseInformation\n"
                + "where biStatusId" + statusCond + " and biAccountId" + accountCond + " and biDataYear = " + year + "\n"
                + "order by 2, 4, 5";
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        return query.getResultList();
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

    // todo: seems to be unused. keep or remove?
    public Map<Integer, Boolean> getAgreement(Set<Integer> iks) {
        String ikList = iks.stream().map(i -> i.toString()).collect(Collectors.joining(", "));
        String sql = "select cuIK, caHasAgreement\n"
                + "from CallCenterDb.dbo.ccCustomer\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "where cuIk in (" + ikList + ")";
        Query query = getEntityManager().createNativeQuery(sql);
        Map<Integer, Boolean> agreements = new HashMap<>();
        List<Object[]> objects = query.getResultList();
        objects.forEach((obj) -> {
            agreements.put((int) obj[0], (boolean) obj[1]);
        });
        return agreements;
    }

    public Set<Integer> obtainIks4NewStatementOfParticipance(int accountId, int year) {
        String sql = "select distinct cuIK\n"
                + "from CallCenterDb.dbo.ccCustomer\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "left join calc.StatementOfParticipance on cuIk = sopIk and sopDataYear = " + year + " and sopStatusId != " + WorkflowStatus.Retired.getValue() + "\n"
                + "where caHasAgreement = 1\n"
                + "     and cuIk in (\n"
                + "		select acIk from dbo.Account where acIk > 0 and acId = " + accountId + "\n"
                + "		union \n"
                + "		select aaiIK from dbo.AccountAdditionalIK where aaiAccountId = " + accountId + "\n"
                + "	) \n"
                + "	and sopId is null";
        Query query = getEntityManager().createNativeQuery(sql);
        return new HashSet<>(query.getResultList());
    }
    // </editor-fold>

    public DrgCalcBasics findCalcBasicsDrg(int id) {
        return findFresh(DrgCalcBasics.class, id);
    }

    public PeppCalcBasics findCalcBasicsPepp(int id) {
        return findFresh(PeppCalcBasics.class, id);
    }

    public DrgCalcBasics saveCalcBasicsDrg(DrgCalcBasics calcBasics) {
        if (calcBasics.getId() == -1) {
            persist(calcBasics);
            return calcBasics;
        }
        saveNeonatData(calcBasics);  // workarround for known problem (persist saves all, merge only one new entry)
        return merge(calcBasics);
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


    public Set<Integer> obtainIks4NewBasiscs(CalcHospitalFunction calcFunct, Set<Integer> accountIds, int year) {
        if (calcFunct == CalcHospitalFunction.CalculationBasicsDrg) {
            return obtainIks4NewBasiscsDrg(accountIds, year);
        }
        return obtainIks4NewBasiscsPepp(accountIds, year);
    }

    private Set<Integer> obtainIks4NewBasiscsDrg(Set<Integer> accountIds, int year) {
        String accountList = accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", "));
        String sql = "select sopIk \n"
                + "from calc.StatementOfParticipance\n"
                + "where sopAccountId in (" + accountList + ")\n"
                + "	and sopStatusId between " + WorkflowStatus.Provided.getValue() + " and " + (WorkflowStatus.Retired.getValue() - 1) + "\n"
                + "	and sopIsDrg=1\n"
                + "	and sopDataYear = " + year + "\n"
                + "	and not exists (\n"
                + "		select 1\n"
                + "		from calc.KGLBaseInformation\n"
                + "		where biAccountId in (" + accountList + ")\n"
                + "			and biDataYear = " + year + "\n"
                + "			and sopIk = biIk\n"
                + "	)";
        Query query = getEntityManager().createNativeQuery(sql);
        return new HashSet<>(query.getResultList());
    }

    private Set<Integer> obtainIks4NewBasiscsPepp(Set<Integer> accountIds, int year) {
        String accountList = accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", "));
        String sql = "select sopIk \n"
                + "from calc.StatementOfParticipance\n"
                + "where sopAccountId in (" + accountList + ")\n"
                + "	and sopStatusId between " + WorkflowStatus.Provided.getValue() + " and " + (WorkflowStatus.Retired.getValue() - 1) + "\n"
                + "	and sopIsPsy=1\n"
                + "	and sopDataYear = " + year + "\n"
                + "	and not exists (\n"
                + "		select 1\n"
                + "		from calc.KGPBaseInformation\n"
                + "		where biAccountId in (" + accountList + ")\n"
                + "			and biDataYear = " + year + "\n"
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
        query.setParameter("year", calcBasics.getDataYear() -1);
        List<DrgCalcBasics> resultList = query.getResultList();
        if (resultList.size() > 0){
            return resultList.get(0);
        }
        return new DrgCalcBasics();
    }

}
