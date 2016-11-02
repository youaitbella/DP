/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.calc.CalculationHospitalInfo;
import org.inek.dataportal.entities.calc.StatementOfParticipance;
import org.inek.dataportal.entities.insurance.InsuranceNubMethodInfo;
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

    public List<CalculationHospitalInfo> getListCalcInfo(DataSet dataSet, List<Integer> accountIds) {
        String statusCond = dataSet == DataSet.AllOpen ? " < 10 " : " >= 10 ";
        String accountCond = " in (" + accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", ")) + ") ";
        String sql = "select 0 as [type], sopAccountId as AccountId, opDataYear as DataYear, sopIk as IK, sopStatusId as StatusId, "
                + "'" + Utils.getMessage("lblStatementOfParticipance") + "' as Name from calc.StatementOfParticipance where sopStatusId" + statusCond + " and sopAccountId" + accountCond
                + "union "
                + "select 1 as [type], bdAccountId as AccountId, bdDataYear as DataYear, bdIk as IK, bdStatusId as StatusId, "
                + "'" + Utils.getMessage("lblCalculationBasicsDrg") + "' as Name from calc.BasicsDrg where bdStatusId" + statusCond + " and bdAccountId" + accountCond
                + "union "
                + "select 2 as [type], bpAccountId as AccountId, bpDataYear as DataYear, bpIk as IK, bpStatusId as StatusId, "
                + "'" + Utils.getMessage("lblCalculationBasicsPepp") + "' as Name from calc.BasicsPepp where bpStatusId" + statusCond + " and bpAccountId" + accountCond
                + "order by 1, 3, 4";
        Query query = getEntityManager().createNativeQuery(sql, CalculationHospitalInfo.class);
        return query.getResultList();
        
    }
}
