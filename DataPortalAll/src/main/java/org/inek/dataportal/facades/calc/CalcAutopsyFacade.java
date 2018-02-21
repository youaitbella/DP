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
import org.inek.dataportal.entities.calc.autopsy.CalcBasicsAutopsy;
import org.inek.dataportal.entities.calc.autopsy.AutopsyItem;
import org.inek.dataportal.entities.calc.autopsy.AutopsyServiceText;
import org.inek.dataportal.entities.calc.sop.StatementOfParticipance;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class CalcAutopsyFacade extends AbstractDataAccess {

    public Set<Integer> obtainIks4NewBasicsAutopsy(int accountId, int year, boolean testMode) {
        String sql = "select distinct sopIk \n"
                + "from calc.StatementOfParticipance\n"
                + "join CallCenterDb.dbo.ccCustomer on sopIk = cuIK\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId and coIsActive = 1 \n" 
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId and cdContactDetailTypeId = 'E'\n" 
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
                + "     and sopIsObd = 1\n"
                + "     and sopDataYear = " + year + "\n"
                + "     and not exists (\n"
                + "             select 1\n"
                + "             from calc.CalcBasicsAutopsy\n"
                + "             where cbaDataYear = " + year + "\n"
                + "                     and cbaStatusId < 200 \n"
                + "                     and sopIk = cbaIk\n"
                + "     )";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") Set<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    public CalcBasicsAutopsy findCalcBasicsAutopsy(int id) {
        return findFresh(CalcBasicsAutopsy.class, id);
    }

    public CalcBasicsAutopsy saveCalcBasicsAutopsy(CalcBasicsAutopsy calcBasics) {
        if (calcBasics.getId() == -1) {
            persist(calcBasics);
            return calcBasics;
        }

        for (AutopsyItem item : calcBasics.getAutopsyItems()) {
            if (item.getId() == -1) {
                persist(item);
            } else {
                merge(item);
            }
        }

        CalcBasicsAutopsy merged = merge(calcBasics);
        return merged;
    }

    public void delete(CalcBasicsAutopsy calcBasics) {
        remove(calcBasics);
    }

    public boolean existActiveCalcBasicsAutopsy(int ik) {
        String jpql = "select c from CalcBasicsAutopsy c where c._ik = :ik and c._dataYear = :year and c._statusId < 10";
        TypedQuery<StatementOfParticipance> query = getEntityManager().createQuery(jpql, StatementOfParticipance.class);
        query.setParameter("ik", ik);
        query.setParameter("year", Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        return !query.getResultList().isEmpty();
    }

    public List<AutopsyServiceText> findAllServiceTexts() {
        return findAll(AutopsyServiceText.class);
    }

}
