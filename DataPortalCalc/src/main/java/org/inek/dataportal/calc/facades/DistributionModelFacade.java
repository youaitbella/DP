/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.facades;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.calc.entities.CalcHospitalInfo;
import org.inek.dataportal.calc.entities.cdm.DistributionModel;
import org.inek.dataportal.calc.entities.cdm.DistributionModelDetail;
import org.inek.dataportal.calc.enums.CalcHospitalFunction;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.adm.facade.LogFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.StringUtil;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class DistributionModelFacade extends AbstractDataAccess {

    // <editor-fold defaultstate="collapsed" desc="Property LogFacade">
    private LogFacade _logFacade;

    @Inject
    public void setLogFacade(LogFacade logFacade) {
        _logFacade = logFacade;
    }
    // </editor-fold>

    private void logAction(DistributionModel entity) {
        _logFacade.saveActionLog(Feature.CALCULATION_HOSPITAL,
                entity.getClass().getSimpleName(),
                entity.getId(),
                entity.getStatus());
    }
    
    public DistributionModel findDistributionModel(int id) {
        return findFresh(DistributionModel.class, id);
    }

    public Set<Integer> obtainIks4NewDistributionModel(CalcHospitalFunction calcFunct, int accountId, int year, boolean testMode) {
        if (calcFunct == CalcHospitalFunction.ClinicalDistributionModelDrg) {
            return obtainIks4Drg(accountId, year, testMode);
        }
        if (calcFunct == CalcHospitalFunction.ClinicalDistributionModelPepp) {
            return obtainIks4Pepp(accountId, year, testMode);
        }
        throw new RuntimeException("obtainIks4NewDistributionModel is not allowed to be called with parameter " + calcFunct);
    }

    private Set<Integer> obtainIks4Drg(int accountId, int year, boolean testMode) {
        String sql = "select distinct sopIk \n"
                + "from calc.StatementOfParticipance\n"
                + "join CallCenterDb.dbo.ccCustomer on sopIk = cuIK\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId and coIsActive = 1 \n" // (2)
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId and cdContactDetailTypeId = 'E'\n" // (2)
                + "join dbo.Account on (cdDetails = acMail"
                + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") and acId = " + accountId + "\n"
                + "join CallCenterDB.dbo.mapContactRole r1 on (r1.mcrContactId = coId) and (r1.mcrRoleId in (3, 12, 15, 16, 18, 19)"
                + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") \n"
                + "left join CallCenterDB.dbo.mapContactRole r2 on (r2.mcrContactId = coId) and r2.mcrRoleId = 14 "
                + (testMode ? " and acMail not like '%@inek-drg.de'" : "") + " \n"
                + "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId "
                + "where cciInfoTypeId in (1, 2) and cciValidTo > " + year + " and cciCalcTypeId in (1, 3, 4, 6)"
                + "     and cuIk in (\n"
                + "             select acIk from dbo.Account where acIk > 0 and acId = " + accountId + "\n"
                + "             union \n"
                + "             select aaiIK from dbo.AccountAdditionalIK where aaiAccountId = " + accountId + "\n"
                + "     ) \n"
                + "     and r2.mcrRoleId is null\n"
                + "     and sopStatusId = " + WorkflowStatus.Provided.getId() + "\n" //+ " and " + (WorkflowStatus.Retired.getId() - 1) + "\n"
                + "     and sopIsDrg = 1\n"
                + "     and sopCdmDrg = 1\n"
                + "     and sopObligatoryCalcType != 1\n"
                + "     and sopDataYear = " + year + "\n"
                + "     and not exists (\n"
                + "             select 1\n"
                + "             from calc.DistributionModelMaster\n"
                + "             where dmmDataYear = " + year + "\n"
                + "                     and dmmType = 0\n"
                + "                     and sopIk = dmmIk\n"
                + "     )";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") Set<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    private Set<Integer> obtainIks4Pepp(int accountId, int year, boolean testMode) {
        String sql = "select distinct sopIk \n"
                + "from calc.StatementOfParticipance\n"
                + "join CallCenterDb.dbo.ccCustomer on sopIk = cuIK\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId and coIsActive = 1 \n" // (2)
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId and cdContactDetailTypeId = 'E'\n" // (2)
                + "join dbo.Account on (cdDetails = acMail"
                + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") and acId = " + accountId + "\n"
                + "join CallCenterDB.dbo.mapContactRole r1 on (r1.mcrContactId = coId) and (r1.mcrRoleId in (3, 12, 15, 16, 18, 19)"
                + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") \n"
                + "left join CallCenterDB.dbo.mapContactRole r2 on (r2.mcrContactId = coId) and r2.mcrRoleId = 14 "
                + (testMode ? " and acMail not like '%@inek-drg.de'" : "") + " \n"
                + "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId "
                + "where cciInfoTypeId in (1, 2) and cciValidTo > " + year + " and cciCalcTypeId in (1, 3, 4, 6)"
                + "     and cuIk in (\n"
                + "             select acIk from dbo.Account where acIk > 0 and acId = " + accountId + "\n"
                + "             union \n"
                + "             select aaiIK from dbo.AccountAdditionalIK where aaiAccountId = " + accountId + "\n"
                + "     ) \n"
                + "     and r2.mcrRoleId is null\n"
                + "     and sopStatusId = " + WorkflowStatus.Provided.getId() + "\n"
                + "     and sopIsPsy = 1\n"
                + "     and sopCdmPsy = 1\n"
                + "     and sopObligatoryCalcType != 1\n"
                + "     and sopDataYear = " + year + "\n"
                + "     and not exists (\n"
                + "             select 1\n"
                + "             from calc.DistributionModelMaster\n"
                + "             where dmmDataYear = " + year + "\n"
                + "                     and dmmType = 1\n"
                + "                     and sopIk = dmmIk\n"
                + "     )";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") Set<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    public DistributionModel saveDistributionModel(DistributionModel model) {
        if (model.getStatus() == WorkflowStatus.Unknown) {
            model.setStatus(WorkflowStatus.New);
        }

        if (model.getId() == -1) {
            persist(model);
            logAction(model);
            return model;
        }

        for (DistributionModelDetail detail : model.getDetails()) {
            if (detail.getId() < 0) {
                persist(detail);
            } else {
                merge(detail);
            }
        }
        logAction(model);
        return merge(model);
    }

    public void delete(DistributionModel model) {
        remove(model);
        model.setStatus(WorkflowStatus.Deleted);
        logAction(model);
    }

    public List<Account> getInekAccounts(int year, String filter) {
        String sql = "select distinct account.*\n" +
                    "from calc.DistributionModelMaster\n" +
                    "join CallCenterDB.dbo.ccCustomer on dmmIk = cuIK\n" +
                    "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId "
                    + "and dmmDataYear between year(cciValidFrom) and year(cciValidTo)\n" +
                    "join CallCenterDB.dbo.mapCustomerCalcInfoAgent on cciId = cciaCustomerCalcInfoId\n" +
                    "join CallCenterDB.dbo.ccAgent on cciaAgentId = agId\n" +
                    "join dbo.Account on agEMail = acMail\n" +
                    "where dmmStatusId >= 10 \n" +
                    "and agActive = 1 and agDomainId in ('O', 'E')\n" +
                    "and cciaReportTypeid in (1, 3)\n" + // 1=Drg, 3=Psy
                    "and dmmDataYear = " + year;
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

    public List<CalcHospitalInfo> getDistributionModelsByEmail(String email, int year, String filter) {
      
        String sql = "select distinct dmmId as Id, 'CDM' as [Type], dmmAccountId as AccountId, dmmDataYear as DataYear, dmmIk as IK, "
                + "dmmStatusId as StatusId,\n" +
                    " '" + Utils.getMessage("lblClinicalDistributionModel") + " '\n" +
                    "    + case dmmType when 0 then 'DRG' when 1 then 'PEPP' else '???' end as Name, dmmLastChanged as LastChanged\n" +
                    "from calc.DistributionModelMaster\n" +
                    "join CallCenterDB.dbo.ccCustomer on dmmIk = cuIK\n" +
                    "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId "
                + "and dmmDataYear between year(cciValidFrom) and year(cciValidTo)\n" +
                    "join CallCenterDB.dbo.mapCustomerCalcInfoAgent on cciId = cciaCustomerCalcInfoId\n" +
                    "join CallCenterDB.dbo.ccAgent on cciaAgentId = agId\n" +
                    "join dbo.Account on agEMail = acMail\n" +
                    "where dmmStatusId >= 10\n" +
                    "and agEMail = '" + email + "'\n" +
                    "and cciaReportTypeId in (1, 3)\n"+
                    "and dmmDataYear = " + year;
        String sqlFilter = StringUtil.getSqlFilter(filter);
        if (sqlFilter.length() > 0) {
            sql = sql + "    and (cast (cuIk as varchar) = " + sqlFilter
                    + "         or cuName like " + sqlFilter
                    + "         or cuCity like " + sqlFilter + ")";
        }
        sql = sql + "order by dmmIk, dmmId desc";
        Query query = getEntityManager().createNativeQuery(sql, CalcHospitalInfo.class);
        @SuppressWarnings("unchecked") List<CalcHospitalInfo> result = query.getResultList();
        return result;
    }

    public DistributionModel findPriorDistributionModel(DistributionModel model) {
        String jpql = "select d from DistributionModel d "
                + "where d._dataYear = :dataYear and d._ik = :ik and d._statusId = :statusId order by d._id desc";
        TypedQuery<DistributionModel> query = getEntityManager().createQuery(jpql, DistributionModel.class);
        query.setParameter("dataYear", model.getDataYear());
        query.setParameter("ik", model.getIk());
        query.setParameter("statusId", 200);
        List<DistributionModel> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

}
