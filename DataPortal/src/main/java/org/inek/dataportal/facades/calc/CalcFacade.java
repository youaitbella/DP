/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades.calc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.AbstractDataAccess;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.StringUtil;

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
        String sql = "select sopId as Id, 'SOP' as [Type], sopAccountId as AccountId, sopDataYear as DataYear, "
                + "  sopIk as IK, sopStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblStatementOfParticipance") + "' as Name, sopLastChanged as LastChanged\n"
                + "from calc.StatementOfParticipance\n"
                + "where sopStatusId" + statusCond + " and sopAccountId" + accountCond + " and sopDataYear = " + year + "\n"
                + "union\n"
                + "select biId as Id, 'CBD' as [Type], biAccountId as AccountId, biDataYear as DataYear, biIk as IK, biStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblCalculationBasicsDrg") + "' as Name, biLastChanged as LastChanged\n"
                + "from calc.KGLBaseInformation\n"
                + "where biStatusId" + statusCond + " and biAccountId" + accountCond + " and biDataYear = " + year + "\n"
                + "union\n"
                + "select biId as Id, 'CBP' as [Type], biAccountId as AccountId, biDataYear as DataYear, biIk as IK, biStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblCalculationBasicsPsy") + "' as Name, biLastChanged as LastChanged\n"
                + "from calc.KGPBaseInformation\n"
                + "where biStatusId" + statusCond + " and biAccountId" + accountCond + " and biDataYear = " + year + "\n"
                + "union\n"
                + "select cbaId as Id, 'CBA' as [Type], cbaAccountId as AccountId, cbaDataYear as DataYear, cbaIk as IK, cbaStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblCalculationBasicsObd") + "' as Name, cbaLastChanged as LastChanged\n"
                + "from calc.CalcBasicsAutopsy\n"
                + "where cbaStatusId" + statusCond + " and cbaAccountId" + accountCond + " and cbaDataYear = " + year + "\n"
                + "union\n"
                + "select dmmId as Id, 'CDM' as [Type], dmmAccountId as AccountId, dmmDataYear as DataYear, dmmIk as IK, dmmStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblClinicalDistributionModel") + " ' "
                + "+ case dmmType when 0 then 'DRG' when 1 then 'PEPP' else '???' end as Name, "
                + "dmmLastChanged as LastChanged\n"
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
                + "select cbaDataYear as DataYear\n"
                + "from calc.CalcBasicsAutopsy\n"
                + "where cbaAccountId" + accountCond + "\n"
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
                + "select cbaAccountId as AccountId\n"
                + "from calc.CalcBasicsAutopsy\n"
                + "where cbaStatusId" + statusCond + " and cbaAccountId" + accountCond + " and cbaDataYear = " + year + "\n"
                + "union\n"
                + "select dmmAccountId as AccountId\n"
                + "from calc.DistributionModelMaster\n"
                + "where dmmStatusId" + statusCond + " and dmmAccountId" + accountCond + " and dmmDataYear = " + year;
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") HashSet<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }
    // </editor-fold>

    public List<Account> getInekAccounts(String filter) {
        String sql = "select distinct account.*\n"
                + "from (select biIk, biDataYear from calc.KGLBaseInformation where biStatusID in (3, 10) \n"
                + "union select biIk, biDataYear from calc.KGPBaseInformation where biStatusID in (3, 10) \n"
                + "union select cbaIk, cbaDataYear from calc.CalcBasicsAutopsy where cbaStatusID in (3, 10) ) base\n"
                + "join CallCenterDB.dbo.ccCustomer on biIk = cuIK\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "join CallCenterDB.dbo.ccCalcInformation on caId = ciCalcAgreementId and biDataYear = ciDataYear \n"
                + "join CallCenterDB.dbo.mapCustomerReportAgent on ciId = mcraCalcInformationId\n"
                + "join CallCenterDB.dbo.ccAgent on mcraAgentId = agId\n"
                + "join dbo.Account on agEMail = acMail\n"
                + "where agActive = 1 and agDomainId in ('O', 'E')\n"
                + "     and mcraReportTypeId in (1, 3, 10) \n"
                + "     and biDataYear = " + Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
        String sqlFilter = StringUtil.getSqlFilter(filter);
        if (sqlFilter.length() > 0) {
            sql = sql + "\n"
                    + "    and (cast (cuIk as varchar) = " + sqlFilter
                    + "         or cuName like " + sqlFilter
                    + "         or cuCity like " + sqlFilter + ")";
        }
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        @SuppressWarnings("unchecked") List<Account> result = query.getResultList();
        return result;
    }

    public List<CalcHospitalInfo> getCalcBasicsForAccount(Account account, String filter) {
        String sql = "select distinct biId as Id, biType as [Type], biAccountId as AccountId, "
                + "    biDataYear as DataYear, biIk as IK, biStatusId as StatusId,\n"
                + "    Name, biLastChanged as LastChanged\n"
                + "from (select biId, biIk, 'CBD' as biType, biDataYear, biAccountID, biStatusId, biLastChanged, '"
                + Utils.getMessage("lblCalculationBasicsDrg") + "' as Name from calc.KGLBaseInformation where biStatusID in (3, 5, 10) \n"
                + "union select biId, biIk, 'CBP' as biType, biDataYear, biAccountID, biStatusId, biLastChanged, '"
                + Utils.getMessage("lblCalculationBasicsPsy") + "' as Name from calc.KGPBaseInformation where biStatusID in (3, 5, 10) \n"
                + "union select cbaId, cbaIk, 'CBA' as biType, cbaDataYear, cbaAccountID, cbaStatusId, cbaLastChanged, '"
                + Utils.getMessage("lblCalculationBasicsObd") + "' as Name from calc.CalcBasicsAutopsy where cbaStatusID in (3, 5, 10)) base \n"
                + "join CallCenterDB.dbo.ccCustomer on biIk = cuIK\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "join CallCenterDB.dbo.ccCalcInformation on caId = ciCalcAgreementId and biDataYear = ciDataYear \n"
                + "join CallCenterDB.dbo.mapCustomerReportAgent on ciId = mcraCalcInformationId\n"
                + "join CallCenterDB.dbo.ccAgent on mcraAgentId = agId\n"
                + "where agEMail = '" + account.getEmail() + "'\n"
                + "     and (biType = 'CBD' and mcraReportTypeId = 1 "
                + "          or biType = 'CBP' and mcraReportTypeId = 3 "
                + "          or biType = 'CBA' and mcraReportTypeId = 10) \n"
                + "     and biDataYear = " + Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
        String sqlFilter = StringUtil.getSqlFilter(filter);
        if (sqlFilter.length() > 0) {
            sql = sql + "\n"
                    + "    and (cast (cuIk as varchar) = " + sqlFilter
                    + "         or cuName like " + sqlFilter
                    + "         or cuCity like " + sqlFilter + ")";
        }
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        @SuppressWarnings("unchecked") List<CalcHospitalInfo> result = query.getResultList();
        return result;
    }

}
