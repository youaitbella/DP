/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.entities.calc.StatementOfParticipance;
import org.inek.dataportal.enums.DataSet;
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

    public List<CalcHospitalInfo> getListCalcInfo(int accountId, int year, DataSet dataSet) {
        int statusLow = dataSet == DataSet.AllOpen ? 0 : dataSet == DataSet.AllSealed ? 10 : -1;
        int statusHigh = dataSet == DataSet.AllOpen ? 9 : dataSet == DataSet.AllSealed ? 200 : -1;
        String statusCond = " between " + statusLow + " and " + statusHigh;
        String accountCond = " = " + accountId;
        String sql = "select sopId * 10 as Id, 0 as [Type], sopAccountId as AccountId, sopDataYear as DataYear, sopIk as IK, sopStatusId as StatusId, "
                + "'" + Utils.getMessage("lblStatementOfParticipance") + "' as Name from calc.StatementOfParticipance where sopStatusId" + statusCond + " and sopAccountId" + accountCond + " and sopDataYear = " + year
                + " union "
                + "select bdId * 10 + 1 as Id, 1 as [Type], bdAccountId as AccountId, bdDataYear as DataYear, bdIk as IK, bdStatusId as StatusId, "
                + "'" + Utils.getMessage("lblCalculationBasicsDrg") + "' as Name from calc.BasicsDrg where bdStatusId" + statusCond + " and bdAccountId" + accountCond + " and bdDataYear = " + year
                + " union "
                + "select bpId * 10 + 2 as Id, 2 as [Type], bpAccountId as AccountId, bpDataYear as DataYear, bpIk as IK, bpStatusId as StatusId, "
                + "'" + Utils.getMessage("lblCalculationBasicsPepp") + "' as Name from calc.BasicsPepp where bpStatusId" + statusCond + " and bpAccountId" + accountCond + " and bpDataYear = " + year
                + "order by 2, 4, 5";
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        return query.getResultList();
    }
    
    public Set<Integer> getCalcYears(Set<Integer> accountIds) {
        String accountCond = " in (" + accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", ")) + ") ";
        String sql = "select sopDataYear as DataYear from calc.StatementOfParticipance where sopAccountId" + accountCond 
                + " union "
                + "select bdDataYear as DataYear from calc.BasicsDrg where bdAccountId" + accountCond 
                + " union "
                + "select bpDataYear as DataYear from calc.BasicsPepp where and bpAccountId" + accountCond;
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        return new HashSet<>(query.getResultList());
    }

    public Set<Integer> checkAccountsForYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String statusCond = " between " + statusLow.getValue() + " and " + statusHigh.getValue();
        String accountCond = " in (" + accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", ")) + ") ";
        String sql = "select sopAccountId as AccountId from calc.StatementOfParticipance where sopStatusId" + statusCond + " and sopAccountId" + accountCond + " and sopDataYear = " + year
                + " union "
                + "select bdAccountId as AccountId from calc.BasicsDrg where bdStatusId" + statusCond + " and bdAccountId" + accountCond + " and bdDataYear = " + year
                + " union "
                + "select bpAccountId as AccountId from calc.BasicsPepp where bpStatusId" + statusCond + " and bpAccountId" + accountCond + " and bpDataYear = " + year;
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        return new HashSet<>(query.getResultList());
    }

    
}
