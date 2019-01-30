/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.facades;

import org.inek.dataportal.calc.entities.CalcHospitalInfo;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.StringUtil;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author muellermi
 */
@RequestScoped
@Transactional
public class CalcFacade extends AbstractDataAccess {

    // <editor-fold defaultstate="collapsed" desc="CalcHospital commons">
    public List<CalcHospitalInfo> getListCalcInfo(int accountId, int year,
                                                  WorkflowStatus statusLow,
                                                  WorkflowStatus statusHigh,
                                                  int ik) {
        if (ik <= 0 && accountId <= 0) {
            return new ArrayList<>();
        }
        String statusCond = " between " + statusLow.getId() + " and " + statusHigh.getId();
        String sql = getListCalcInfoStatement(statusCond, year, accountId, ik);
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        @SuppressWarnings("unchecked")
        List<CalcHospitalInfo> infos = query.getResultList();
        return infos;
    }

    private String getListCalcInfoStatement(String statusCond, int year, int accountId, int ik) {
        String sql = "select sopId as Id, 'SOP' as [Type], sopAccountId as AccountId, sopDataYear as DataYear, "
                + "  sopIk as IK, sopStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblStatementOfParticipance") + "' as Name, sopLastChanged as LastChanged\n"
                + "from calc.StatementOfParticipance\n"
                + "where sopStatusId" + statusCond + " and sopDataYear = " + year + "\n"
                + (accountId > 0 ? " and sopAccountId = " + accountId + "\n" : "")
                + (ik > 0 ? " and sopIk = " + ik + "\n" : "")
                + "union\n"
                + "select biId as Id, 'CBD' as [Type], biAccountId as AccountId, biDataYear as DataYear, biIk as IK, biStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblCalculationBasicsDrg") + "' as Name, biLastChanged as LastChanged\n"
                + "from calc.KGLBaseInformation\n"
                + "where biStatusId" + statusCond + " and biDataYear = " + year + "\n"
                + (accountId > 0 ? " and biAccountId = " + accountId + "\n" : "")
                + (ik > 0 ? " and biIk = " + ik + "\n" : "")
                + "union\n"
                + "select biId as Id, 'CBP' as [Type], biAccountId as AccountId, biDataYear as DataYear, biIk as IK, biStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblCalculationBasicsPsy") + "' as Name, biLastChanged as LastChanged\n"
                + "from calc.KGPBaseInformation\n"
                + "where biStatusId" + statusCond + " and biDataYear = " + year + "\n"
                + (accountId > 0 ? " and biAccountId = " + accountId + "\n" : "")
                + (ik > 0 ? " and biIk = " + ik + "\n" : "")
                + "union\n"
                + "select cbaId as Id, 'CBA' as [Type], cbaAccountId as AccountId, cbaDataYear as DataYear, cbaIk as IK, cbaStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblCalculationBasicsObd") + "' as Name, cbaLastChanged as LastChanged\n"
                + "from calc.CalcBasicsAutopsy\n"
                + "where cbaStatusId" + statusCond + " and cbaDataYear = " + year + "\n"
                + (accountId > 0 ? " and cbaAccountId = " + accountId + "\n" : "")
                + (ik > 0 ? " and cbaIk = " + ik + "\n" : "")
                + "union\n"
                + "select dmmId as Id, 'CDM' as [Type], dmmAccountId as AccountId, dmmDataYear as DataYear, dmmIk as IK, dmmStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblClinicalDistributionModel") + " ' "
                + "+ case dmmType when 0 then 'DRG' when 1 then 'PEPP' else '???' end as Name, "
                + "dmmLastChanged as LastChanged\n"
                + "from calc.DistributionModelMaster\n"
                + "where dmmStatusId" + statusCond + " and dmmDataYear = " + year + "\n"
                + (accountId > 0 ? " and dmmAccountId = " + accountId + "\n" : "")
                + (ik > 0 ? " and dmmIk = " + ik + "\n" : "")
                + "order by 2, 4, 5, 8 desc";
        return sql;
    }

    @SuppressWarnings("unchecked")
    public List<Integer> getCalcYears(Set<Integer> accountIds) {
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
                + "where dmmAccountId" + accountCond + "\n"
                + "order by 1 desc";
        Query query = getEntityManager().createNativeQuery(sql);
        return query.getResultList();
    }

    public Set<Integer> checkAccountsForYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow,
                                             WorkflowStatus statusHigh, Set<Integer> managedIks) {
        String excludedIks = managedIks.stream().map(i -> "" + i).collect(Collectors.joining(","));
        String statusCond = " between " + statusLow.getId() + " and " + statusHigh.getId();
        String accountCond = " in (" + accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", ")) + ") ";
        String sql = "select sopAccountId as AccountId\n"
                + "from calc.StatementOfParticipance\n"
                + "where sopStatusId" + statusCond + " and sopAccountId" + accountCond + " and sopDataYear = " + year + "\n"
                + (managedIks.isEmpty() ? "" : " and sopIk not in (" + excludedIks + ")\n")
                + "union\n"
                + "select biAccountId as AccountId\n"
                + "from calc.KGLBaseInformation\n"
                + "where biStatusId" + statusCond + " and biAccountId" + accountCond + " and biDataYear = " + year + "\n"
                + (managedIks.isEmpty() ? "" : " and biIk not in (" + excludedIks + ")\n")
                + "union\n"
                + "select biAccountId as AccountId\n"
                + "from calc.KGPBaseInformation\n"
                + "where biStatusId" + statusCond + " and biAccountId" + accountCond + " and biDataYear = " + year + "\n"
                + (managedIks.isEmpty() ? "" : " and biIk not in (" + excludedIks + ")\n")
                + "union\n"
                + "select cbaAccountId as AccountId\n"
                + "from calc.CalcBasicsAutopsy\n"
                + "where cbaStatusId" + statusCond + " and cbaAccountId" + accountCond + " and cbaDataYear = " + year + "\n"
                + (managedIks.isEmpty() ? "" : " and cbaIk not in (" + excludedIks + ")\n")
                + "union\n"
                + "select dmmAccountId as AccountId\n"
                + "from calc.DistributionModelMaster\n"
                + "where dmmStatusId" + statusCond + " and dmmAccountId" + accountCond + " and dmmDataYear = " + year + "\n"
                + (managedIks.isEmpty() ? "" : " and dmmIk not in (" + excludedIks + ")\n");
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        HashSet<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }
    // </editor-fold>

    public List<Account> getInekAccounts(int year, String filter) {
        String sql = "select distinct account.*\n"
                + "from (select biIk, biDataYear from calc.KGLBaseInformation where biStatusID in (3, 10)\n"
                + "union select biIk, biDataYear from calc.KGPBaseInformation where biStatusID in (3, 10)\n"
                + "union select cbaIk, cbaDataYear from calc.CalcBasicsAutopsy where cbaStatusID in (3, 10) ) base\n"
                + "join CallCenterDB.dbo.ccCustomer on biIk = cuIK\n"
                + "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId "
                + "and biDataYear between year(cciValidFrom) and year(cciValidTo)\n"
                + "join CallCenterDB.dbo.mapCustomerCalcInfoAgent on cciId = cciaCustomerCalcInfoId\n"
                + "join CallCenterDB.dbo.ccAgent on cciaAgentId = agId\n"
                + "join dbo.Account on agEMail = acMail\n"
                + "where agActive = 1 and agDomainId in ('O', 'E')\n"
                + "and cciaReportTypeid in (1, 3, 10)\n"
                + "and biDataYear = " + year;

        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        @SuppressWarnings("unchecked")
        List<Account> result = query.getResultList();
        return result;
    }

    public List<CalcHospitalInfo> getCalcBasicsByEmail(String email, int year, String filter) {
        String sql = "select distinct biId as Id, biType as [Type], biAccountId as AccountId, biDataYear as DataYear, biIk as IK, "
                + "biStatusId as StatusId, Name, biLastChanged as LastChanged, cuName as customerName, agLastName + ', ' + agFirstName as AgentName, "
                + "cuCity as customerTown, acLastName + ', ' + acFirstName as AccountName, biSealed as SendAt\n"
                + "from (select biId, biIk, 'CBD' as biType, biDataYear, biAccountID, biStatusId, biLastChanged,"
                + " 'KGL' as Name, biSealed \n"
                + "from calc.KGLBaseInformation where biStatusID in (3, 5, 10) \n"
                + "union \n"
                + "select biId, biIk, 'CBP' as biType, biDataYear, biAccountID, biStatusId, biLastChanged,"
                + " 'KGP' as Name, biSealed \n"
                + "from calc.KGPBaseInformation where biStatusID in (3, 5, 10)\n"
                + "union \n"
                + "select cbaId, cbaIk, 'CBA' as biType, cbaDataYear, cbaAccountID, cbaStatusId,"
                + " cbaLastChanged, 'KGO' as Name, cbaSealed \n"
                + "from calc.CalcBasicsAutopsy \n"
                + "where cbaStatusID in (3, 5, 10)\n"
                + ") base\n"
                + "join CallCenterDB.dbo.ccCustomer on biIk = cuIK\n"
                + "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId"
                + " and biDataYear between year(cciValidFrom) and year(cciValidTo) and cciInfoTypeId = 14\n"
                + "join CallCenterDB.dbo.mapCustomerCalcInfoAgent on cciId = cciaCustomerCalcInfoId"
                + " and biDataYear between year(cciaValidFrom) and year(cciaValidTo)\n"
                + "join CallCenterDB.dbo.ccAgent on cciaAgentId = agId\n" +
                "join dbo.Account on acId = biAccountID\n"
                + "where agEMail = '" + email + "'\n"
                + "and (biType = 'CBD' and cciaReportTypeid = 1\n"
                + "or biType = 'CBP' and cciaReportTypeId = 3\n"
                + "or biType = 'CBA' and cciaReportTypeId = 10)\n"
                + "and biDataYear = " + year;
        String sqlFilter = StringUtil.getSqlFilter(filter);
        if (sqlFilter.length() > 0) {
            sql = sql + "\n"
                    + "    and (biIk = " + sqlFilter
                    + "         or cuName like " + sqlFilter
                    + "         or cuCity like " + sqlFilter + ")";
        }
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        @SuppressWarnings("unchecked")
        List<CalcHospitalInfo> result = query.getResultList();
        return result;
    }

    public Boolean agentHasHospitals(String email, int year) {
        String sql = "select 1 from CallCenterDB.dbo.ccAgent \n"
                + "join CallCenterDB.dbo.mapCustomerCalcInfoAgent on agId = cciaAgentId \n"
                + "where agEMail = '" + email + "'\n"
                + "and YEAR(cciaValidTo) = " + year;
        Query query = getEntityManager().createNativeQuery(sql);
        return !query.getResultList().isEmpty();
    }

    public List<CalcHospitalInfo> getAllCalcBasics(int dataYear) {
        String sql = "select distinct biId as Id, biType as [Type], biAccountId as AccountId, biDataYear as DataYear, biIk as IK, "
                + "biStatusId as StatusId, Name, biLastChanged as LastChanged, cuName as customerName, "
                + "agLastName + ', ' + agFirstName as AgentName, cuCity as customerTown, acLastName + ', ' + acFirstName as AccountName, " +
                "biSealed as SendAt \n"
                + "from ("
                + "select biId, biIk, 'CBD' as biType, biDataYear, biAccountID, biStatusId, biLastChanged,"
                + " 'KGL' as Name, biSealed \n"
                + "from calc.KGLBaseInformation where biStatusID in (3, 5, 10) \n"
                + "union \n"
                + "select biId, biIk, 'CBP' as biType, biDataYear, biAccountID, biStatusId, biLastChanged,"
                + " 'KGP' as Name, biSealed \n"
                + "from calc.KGPBaseInformation where biStatusID in (3, 5, 10)\n"
                + "union \n"
                + "select cbaId, cbaIk, 'CBA' as biType, cbaDataYear, cbaAccountID, cbaStatusId,"
                + " cbaLastChanged, 'KGO' as Name, cbaSealed \n"
                + "from calc.CalcBasicsAutopsy \n"
                + "where cbaStatusID in (3, 5, 10)\n"
                + ") base\n"
                + "join CallCenterDB.dbo.ccCustomer on biIk = cuIK\n"
                + "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId"
                + " and biDataYear between year(cciValidFrom) and year(cciValidTo) and cciInfoTypeId = 14\n"
                + "join CallCenterDB.dbo.mapCustomerCalcInfoAgent on cciId = cciaCustomerCalcInfoId"
                + " and biDataYear between year(cciaValidFrom) and year(cciaValidTo)\n"
                + "join CallCenterDB.dbo.ccAgent on cciaAgentId = agId and agDomainId = 'O'\n" +
                "join dbo.Account on acId = biAccountID\n"
                + "where (biType = 'CBD' and cciaReportTypeid = 1\n"
                + "or biType = 'CBP' and cciaReportTypeId = 3\n"
                + "or biType = 'CBA' and cciaReportTypeId = 10)\n"
                + "and biDataYear = " + dataYear + "\n"
                + "order by cuCity";
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        @SuppressWarnings("unchecked")
        List<CalcHospitalInfo> result = query.getResultList();
        return result;
    }

    public List<CalcHospitalInfo> getAllSop(int year) {
        String sql = "select distinct sopId as Id, 'SOP' as [Type], sopAccountId as AccountId, sopDataYear as DataYear, sopIk as IK, "
                + "sopStatusId as StatusId, cuName, sopLastChanged as LastChanged, cuName as customerName, cuCity as customerTown\n" +
                ", acLastName + ', ' + acFirstName as AccountName, sopSealed as SendAt\n"
                + "from calc.StatementOfParticipance \n"
                + "join CallCenterDB.dbo.ccCustomer on sopIk = cuIK\n" +
                "join dbo.Account on acId = sopAccountId\n"
                + "where sopStatusId = 10 \n"
                + "and sopDataYear = " + year + "\n"
                + "order by cuCity";
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        @SuppressWarnings("unchecked")
        List<CalcHospitalInfo> result = query.getResultList();
        return result;
    }

    public List<CalcHospitalInfo> getSopByEmail(String email, int year) {
        String sql = "select distinct sopId as Id, 'SOP' as [Type], sopAccountId as AccountId, sopDataYear as DataYear, sopIk as IK, "
                + "sopStatusId as StatusId, cuName, sopLastChanged as LastChanged, cuName as customerName, "
                + "agLastName + ', ' + agFirstName as AgentName, cuCity as customerTown, acLastName + ', ' + acFirstName as AccountName, " +
                "sopSealed as SendAt\n"
                + "from calc.StatementOfParticipance \n"
                + "join CallCenterDB.dbo.ccCustomer on sopIk = cuIK\n"
                + "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId"
                + " and sopDataYear between year(cciValidFrom) and year(cciValidTo) and cciInfoTypeId = 14\n"
                + "join CallCenterDB.dbo.mapCustomerCalcInfoAgent on cciId = cciaCustomerCalcInfoId"
                + " and sopDataYear between year(cciaValidFrom) and year(cciaValidTo)\n"
                + "join CallCenterDB.dbo.ccAgent on cciaAgentId = agId\n" +
                "join dbo.Account on acId = sopAccountId\n"
                + "where agEMail = '" + email + "'\n"
                + "and cciaReportTypeid in (1,3,10) \n"
                + "and sopStatusId = 10 \n"
                + "and sopDataYear = " + year + "\n"
                + "order by cuCity";
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        @SuppressWarnings("unchecked")
        List<CalcHospitalInfo> result = query.getResultList();
        return result;
    }

    public List<CalcHospitalInfo> getDistributionModelsByEmail(String email, int year) {
        String sql = "select distinct dmmId as Id, 'CDM' as [Type], dmmAccountId as AccountId, dmmDataYear as DataYear, dmmIk as IK, "
                + "dmmStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblClinicalDistributionModel") + " '\n"
                + "    + case dmmType when 0 then 'DRG' when 1 then 'PEPP' else '???' end as Name, "
                + "dmmLastChanged as LastChanged, cuName as customerName, "
                + "agLastName + ', ' + agFirstName as AgentName, cuCity as customerTown, acLastName + ', ' + acFirstName as AccountName, " +
                "dmmSealed as SendAt\n"
                + "from calc.DistributionModelMaster\n"
                + "join CallCenterDB.dbo.ccCustomer on dmmIk = cuIK\n"
                + "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId "
                + "and dmmDataYear between year(cciValidFrom) and year(cciValidTo)\n"
                + "join CallCenterDB.dbo.mapCustomerCalcInfoAgent on cciId = cciaCustomerCalcInfoId\n"
                + "join CallCenterDB.dbo.ccAgent on cciaAgentId = agId\n"
                + "join dbo.Account on acId = dmmAccountId\n"
                + "where dmmStatusId >= 10\n"
                + "and agEMail = '" + email + "'\n"
                + "and cciaReportTypeId in (1, 3)\n"
                + "and dmmDataYear = " + year + "\n"
                + "order by cuCity";
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        @SuppressWarnings("unchecked")
        List<CalcHospitalInfo> result = query.getResultList();
        return result;
    }

    public List<CalcHospitalInfo> getAllDistributionModels(int year) {
        String sql = "select distinct dmmId as Id, 'CDM' as [Type], dmmAccountId as AccountId, dmmDataYear as DataYear, dmmIk as IK, "
                + "dmmStatusId as StatusId,\n"
                + "case dmmType when 0 then 'DRG' when 1 then 'PEPP' else '???' end as Name, "
                + "dmmLastChanged as LastChanged, cuName as customerName, "
                + "agLastName + ', ' + agFirstName as AgentName, cuCity as customerTown, acLastName + ', ' + acFirstName as AccountName, " +
                "dmmSealed as SendAt\n"
                + "from calc.DistributionModelMaster\n"
                + "join CallCenterDB.dbo.ccCustomer on dmmIk = cuIK\n"
                + "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId "
                + "and dmmDataYear between year(cciValidFrom) and year(cciValidTo)\n"
                + "join CallCenterDB.dbo.mapCustomerCalcInfoAgent on cciId = cciaCustomerCalcInfoId\n"
                + "join CallCenterDB.dbo.ccAgent on cciaAgentId = agId and agDomainId = 'O'\n"
                + "join dbo.Account on acId = dmmAccountId\n"
                + "where dmmStatusId >= 10\n"
                + "and cciaReportTypeId in (1, 3)\n"
                + "and dmmDataYear = " + year + "\n"
                + "order by cuCity";
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        @SuppressWarnings("unchecked")
        List<CalcHospitalInfo> result = query.getResultList();
        return result;
    }
}
