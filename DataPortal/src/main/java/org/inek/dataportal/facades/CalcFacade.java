/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.calc.CalcBasicsDrg;
import org.inek.dataportal.entities.calc.CalcBasicsPepp;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
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

        if (statementOfParticipance.getId() == -1) {
            persist(statementOfParticipance);
            return statementOfParticipance;
        }
        return merge(statementOfParticipance);
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
                + "select bdId as Id, 1 as [Type], bdAccountId as AccountId, bdDataYear as DataYear, bdIk as IK, bdStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblCalculationBasicsDrg") + "' as Name\n"
                + "from calc.BasicsDrg\n"
                + "where bdStatusId" + statusCond + " and bdAccountId" + accountCond + " and bdDataYear = " + year + "\n"
                + "union\n"
                + "select bpId as Id, 2 as [Type], bpAccountId as AccountId, bpDataYear as DataYear, bpIk as IK, bpStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblCalculationBasicsPepp") + "' as Name\n"
                + "from calc.BasicsPepp\n"
                + "where bpStatusId" + statusCond + " and bpAccountId" + accountCond + " and bpDataYear = " + year + "\n"
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
                + "select bdDataYear as DataYear\n"
                + "from calc.BasicsDrg\n"
                + "where bdAccountId" + accountCond + "\n"
                + "union\n"
                + "select bpDataYear as DataYear\n"
                + "from calc.BasicsPepp\n"
                + "where bpAccountId" + accountCond;
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
                + "select bdAccountId as AccountId\n"
                + "from calc.BasicsDrg\n"
                + "where bdStatusId" + statusCond + " and bdAccountId" + accountCond + " and bdDataYear = " + year + "\n"
                + "union\n"
                + "select bpAccountId as AccountId\n"
                + "from calc.BasicsPepp\n"
                + "where bpStatusId" + statusCond + " and bpAccountId" + accountCond + " and bpDataYear = " + year;
        Query query = getEntityManager().createNativeQuery(sql);
        return new HashSet<>(query.getResultList());
    }

    public CalcBasicsDrg findCalcBasicsDrg(int id) {
        return findFresh(CalcBasicsDrg.class, id);
    }

    public CalcBasicsPepp findCalcBasicsPepp(int id) {
        return findFresh(CalcBasicsPepp.class, id);
    }

    public CalcBasicsDrg saveCalcBasicsDrg(CalcBasicsDrg _calcBasics) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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
                + "		from calc.BasicsDrg\n"
                + "		where bdAccountId in (" + accountList + ")\n"
                + "			and bdStatusId < 200\n"
                + "			and bdDataYear = " + year + "\n"
                + "			and sopIk = bdIk\n"
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
                + "		from calc.BasicsPepp\n"
                + "		where bpAccountId in (" + accountList + ")\n"
                + "			and bpStatusId < 200\n"
                + "			and bpDataYear = " + year + "\n"
                + "			and sopIk = bpIk\n"
                + "	)";
        Query query = getEntityManager().createNativeQuery(sql);
        return new HashSet<>(query.getResultList());
    }
}
