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
import javax.transaction.Transactional;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.DistributionModel;
import org.inek.dataportal.entities.calc.DistributionModelDetail;
import org.inek.dataportal.enums.CalcHospitalFunction;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.AbstractDataAccess;

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
                + "join dbo.Account on (cdDetails = acMail" + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") and acId = " + accountId + "\n" // (2) - but let InEK staff perform without this restriction
                + "join CallCenterDB.dbo.mapContactRole r1 on (r1.mcrContactId = coId) and (r1.mcrRoleId in (3, 12, 15, 16, 18, 19)" + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") \n"
                + "left join CallCenterDB.dbo.mapContactRole r2 on (r2.mcrContactId = coId) and r2.mcrRoleId = 14 " + (testMode ? " and acMail not like '%@inek-drg.de'" : "") + " \n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "where caHasAgreement = 1 and caIsInactive = 0 and caCalcTypeId in (1, 3, 6)\n"
                + "     and cuIk in (\n"
                + "		select acIk from dbo.Account where acIk > 0 and acId = " + accountId + "\n"
                + "		union \n"
                + "		select aaiIK from dbo.AccountAdditionalIK where aaiAccountId = " + accountId + "\n"
                + "	) \n"
                + "     and r2.mcrRoleId is null\n"
                + "	and sopStatusId = " + WorkflowStatus.Provided.getValue() + "\n" //+ " and " + (WorkflowStatus.Retired.getValue() - 1) + "\n"
                + "	and sopIsDrg = 1\n"
                + "	and sopCdmDrg = 1\n"
                + "	and sopObligatoryCalcType != 1\n"
                + "	and sopDataYear = " + year + "\n"
                + "	and not exists (\n"
                + "		select 1\n"
                + "		from calc.DistributionModelMaster\n"
                + "		where dmmDataYear = " + year + "\n"
                + "			and dmmType = 0\n"
                + "			and sopIk = dmmIk\n"
                + "	)";

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
                + "join dbo.Account on (cdDetails = acMail" + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") and acId = " + accountId + "\n" // (2) - but let InEK staff perform without this restriction
                + "join CallCenterDB.dbo.mapContactRole r1 on (r1.mcrContactId = coId) and (r1.mcrRoleId in (3, 12, 15, 16, 18, 19)" + (testMode ? " or acMail like '%@inek-drg.de'" : "") + ") \n"
                + "left join CallCenterDB.dbo.mapContactRole r2 on (r2.mcrContactId = coId) and r2.mcrRoleId = 14 " + (testMode ? " and acMail not like '%@inek-drg.de'" : "") + " \n"
                + "join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId\n"
                + "where caHasAgreement = 1 and caIsInactive = 0 and caCalcTypeId in (1, 3, 6)\n"
                + "     and cuIk in (\n"
                + "		select acIk from dbo.Account where acIk > 0 and acId = " + accountId + "\n"
                + "		union \n"
                + "		select aaiIK from dbo.AccountAdditionalIK where aaiAccountId = " + accountId + "\n"
                + "	) \n"
                + "     and r2.mcrRoleId is null\n"
                + "	and sopStatusId = " + WorkflowStatus.Provided.getValue() + "\n" //+ " and " + (WorkflowStatus.Retired.getValue() - 1) + "\n"
                + "	and sopIsPsy = 1\n"
                + "	and sopCdmPsy = 1\n"
                + "	and sopObligatoryCalcType != 1\n"
                + "	and sopDataYear = " + year + "\n"
                + "	and not exists (\n"
                + "		select 1\n"
                + "		from calc.DistributionModelMaster\n"
                + "		where dmmDataYear = " + year + "\n"
                + "			and dmmType = 1\n"
                + "			and sopIk = dmmIk\n"
                + "	)";

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

    public void deleteDistributionModel(DistributionModel model) {
        remove(model);
    }

    public List<Account> getInekAgentsForDrg() {// todo: change to agent
        /*
select agId, agFirstName, agLastName, agEMail
from calc.DistributionModelMaster 
join CallCenterDB.dbo.ccCustomer on dmmIk = cuIK
join CallCenterDB.dbo.ccCalcAgreement on cuId = caCustomerId
join CallCenterDB.dbo.ccCalcInformation on caId = ciCalcAgreementId
join CallCenterDB.dbo.mapCustomerReportAgent on ciId = mcraCalcInformationId
join CallCenterDB.dbo.ccAgent on mcraAgentId = agId
where dmmStatusId = 10 
	and agActive = 1 and agDomainId in ('O', 'E')
	and mcraReportTypeId in (1, 3) --1=DRG, 30PSY
        
        */
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
