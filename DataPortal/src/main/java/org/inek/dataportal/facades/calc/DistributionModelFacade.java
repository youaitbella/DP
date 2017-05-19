/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades.calc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.entities.calc.cdm.DistributionModel;
import org.inek.dataportal.entities.calc.cdm.DistributionModelDetail;
import org.inek.dataportal.enums.CalcHospitalFunction;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.AbstractDataAccess;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class DistributionModelFacade extends AbstractDataAccess {

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
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "where caHasAgreement = 1 and caIsInactive = 0 and caCalcTypeId in (1, 3, 6)\n"
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
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "where caHasAgreement = 1 and caIsInactive = 0 and caCalcTypeId in (1, 3, 6)\n"
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
            return model;
        }

        for (DistributionModelDetail detail : model.getDetails()) {
            if (detail.getId() < 0) {
                persist(detail);
            } else {
                merge(detail);
            }
        }

        return merge(model);
    }

    public void delete(DistributionModel model) {
        remove(model);
    }

    public List<Account> getInekAccounts() {
        String sql = "select distinct account.*\n"
                + "from calc.DistributionModelMaster \n"
                + "join CallCenterDB.dbo.ccCustomer on dmmIk = cuIK\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "join CallCenterDB.dbo.ccCalcInformation on caId = ciCalcAgreementId and dmmDataYear = ciDataYear \n"
                + "join CallCenterDB.dbo.mapCustomerReportAgent on ciId = mcraCalcInformationId\n"
                + "join CallCenterDB.dbo.ccAgent on mcraAgentId = agId\n"
                + "left join dbo.Account on agEMail = acMail\n"
                + "where dmmStatusId >= 10 \n"
                + "     and agActive = 1 and agDomainId in ('O', 'E')\n"
                + "     and mcraReportTypeId in (1, 3)"; // 1=Drg, 3=Psy
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        @SuppressWarnings("unchecked") List<Account> result = query.getResultList();
        return result;
    }

    public List<CalcHospitalInfo> getDistributionModelsForAccount(Account account) {
        String sql = "select distinct dmmId as Id, 'CDM' as [Type], dmmAccountId as AccountId, dmmDataYear as DataYear, "
                + "    dmmIk as IK, dmmStatusId as StatusId,\n"
                + " '" + Utils.getMessage("lblClinicalDistributionModel") + " ' "
                + "    + case dmmType when 0 then 'DRG' when 1 then 'PEPP' else '???' end as Name, dmmLastChanged as LastChanged\n"
                + "from calc.DistributionModelMaster \n"
                + "join CallCenterDB.dbo.ccCustomer on dmmIk = cuIK\n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "join CallCenterDB.dbo.ccCalcInformation on caId = ciCalcAgreementId and dmmDataYear = ciDataYear \n"
                + "join CallCenterDB.dbo.mapCustomerReportAgent on ciId = mcraCalcInformationId\n"
                + "join CallCenterDB.dbo.ccAgent on mcraAgentId = agId\n"
                + "where dmmStatusId >= 10 \n"
                + "     and agEMail = '" + account.getEmail() + "'\n"
                + "     and mcraReportTypeId in (1, 3)\n"
                + "order by dmmIk, dmmId desc";
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
        if (resultList.isEmpty()){
            return null;
        }
        return resultList.get(0);
    }

}
