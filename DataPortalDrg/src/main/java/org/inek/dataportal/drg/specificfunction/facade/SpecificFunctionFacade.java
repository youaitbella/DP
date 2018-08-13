/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.drg.specificfunction.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.specificfunction.entity.CenterName;
import org.inek.dataportal.common.specificfunction.entity.RelatedName;
import org.inek.dataportal.common.specificfunction.entity.SpecificFunction;
import org.inek.dataportal.common.specificfunction.entity.SpecificFunctionRequest;
import org.inek.dataportal.common.specificfunction.entity.TypeExtraCharge;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.StringUtil;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class SpecificFunctionFacade extends AbstractDataAccess {

    private static final String YEAR = "year";
    private static final String IK = "ik";
    private static final String STATUS_HIGH = "statusHigh";
    private static final String STATUS_LOW = "statusLow";
    private static final String MANAGED_IKS = "managedIks";
    private static final String ACCOUNT_IDS = "accountIds";
    private static final String CODE = "code";
    private static final String ACCOUNT_ID = "accountId";

    //<editor-fold defaultstate="collapsed" desc="Specific Function Request">
    public SpecificFunctionRequest findSpecificFunctionRequest(int id) {
        return findFresh(SpecificFunctionRequest.class, id);
    }

    public List<SpecificFunctionRequest> obtainSpecificFunctionRequests(
            int accountId,
            int ik,
            int year,
            WorkflowStatus statusLow,
            WorkflowStatus statusHigh) {
        String jpql = "SELECT s FROM SpecificFunctionRequest s "
                + "WHERE s._accountId = :accountId"
                + (ik > 0 ? " and s._ik = :ik" : "")
                + (year > 0 ? " and s._dataYear = :year" : "")
                + " and s._statusId between :statusLow and :statusHigh ORDER BY s._id DESC";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        query.setParameter(ACCOUNT_ID, accountId);
        if (year > 0) {
            query.setParameter(YEAR, year);
        }
        query.setParameter(IK, ik);
        query.setParameter(STATUS_LOW, statusLow.getId());
        query.setParameter(STATUS_HIGH, statusHigh.getId());
        return query.getResultList();
    }

    public List<SpecificFunctionRequest> obtainSpecificFunctionRequests(
            int ik,
            int year,
            WorkflowStatus statusLow,
            WorkflowStatus statusHigh) {
        String jpql = "SELECT s FROM SpecificFunctionRequest s "
                + "WHERE s._ik = :ik"
                + (year > 0 ? " and s._dataYear = :year" : "")
                + " and s._statusId between :statusLow and :statusHigh ORDER BY s._id DESC";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        query.setParameter(IK, ik);
        if (year > 0) {
            query.setParameter(YEAR, year);
        }
        query.setParameter(STATUS_LOW, statusLow.getId());
        query.setParameter(STATUS_HIGH, statusHigh.getId());
        return query.getResultList();
    }

    public boolean existActiveSpecificFunctionRequest(int ik) {
        String jpql = "select s from SpecificFunctionRequest s where s._ik = :ik and s._dataYear = :year and s._statusId < 10";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        query.setParameter(IK, ik);
        query.setParameter(YEAR, Utils.getTargetYear(Feature.SPECIFIC_FUNCTION));
        return query.getResultList().size() == 1;
    }

    public List<Account> loadRequestAccounts(
            Set<Integer> accountIds,
            WorkflowStatus statusLow,
            WorkflowStatus statusHigh,
            Set<Integer> managedIks) {
        return loadRequestAccountsForYear(accountIds, 0, statusLow, statusHigh, managedIks);
    }

    public List<Account> loadRequestAccountsForYear(
            Set<Integer> accountIds,
            int year,
            WorkflowStatus statusLow,
            WorkflowStatus statusHigh,
            Set<Integer> managedIks) {
        String jpql = "select distinct a "
                + "from Account a join SpecificFunctionRequest s "
                + "where a._id = s._accountId"
                + (year > 0 ? " and s._dataYear = :year " : "")
                + "    and s._statusId between :statusLow and :statusHigh and s._accountId in :accountIds"
                + (managedIks.size() > 0 ? "    and s._ik not in :managedIks" : "");
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        if (year > 0) {
            query.setParameter(YEAR, year);
        }
        query.setParameter(STATUS_LOW, statusLow.getId());
        query.setParameter(STATUS_HIGH, statusHigh.getId());
        query.setParameter(ACCOUNT_IDS, accountIds);
        if (managedIks.size() > 0) {
            query.setParameter(MANAGED_IKS, managedIks);
        }
        return query.getResultList();
    }

    public Set<Integer> getRequestCalcYears(Set<Integer> accountIds) {
        String jpql = "select s._dataYear from SpecificFunctionRequest s where s._accountId in :accountIds and s._statusId >= 10";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter(ACCOUNT_IDS, accountIds);
        @SuppressWarnings("unchecked")
        HashSet<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    public SpecificFunctionRequest saveSpecificFunctionRequest(SpecificFunctionRequest request) {
        if (request.getStatus() == WorkflowStatus.Unknown) {
            request.setStatus(WorkflowStatus.New);
        }

        if (request.getId() == -1) {
            persist(request);
            return request;
        }

//        for (RequestProjectedCenter item : request.getRequestProjectedCenters()) {
//            if (item.getId() == -1) {
//                persist(item);
//            } else {
//                merge(item);
//            }
//        }
//        for (RequestAgreedCenter item : request.getRequestAgreedCenters()) {
//            if (item.getId() == -1) {
//                persist(item);
//            } else {
//                merge(item);
//            }
//        }
        return merge(request);
    }

    public void deleteSpecificFunctionRequest(SpecificFunctionRequest statement) {
        remove(statement);
    }

    public List<Account> getInekAccounts() {
        String sql = "select distinct account.*\n"
                + "from spf.RequestMaster\n"
                + "join CallCenterDB.dbo.ccCustomer on rmIk = cuIK\n"
                + "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId and YEAR(cciValidTo) = (rmDataYear-1) \n"
                + "join CallCenterDB.dbo.mapCustomerCalcInfoAgent on cciId = cciaCustomerCalcInfoId"
                + "join CallCenterDB.dbo.ccAgent on cciaAgentId = agId\n"
                + "join dbo.Account on agEMail = acMail\n"
                + "where agActive = 1 and agDomainId in ('O', 'E')\n"
                + "     and rmStatusId = 10 \n"
                + "     and cciaReportTypeId in (1, 3) \n"
                + "     and rmDataYear = " + Utils.getTargetYear(Feature.SPECIFIC_FUNCTION);
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        @SuppressWarnings("unchecked")
        List<Account> result = query.getResultList();
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<SpecificFunctionRequest> getSpecificFunctionsForInek(int year, String filter) {
        String sqlFilter = StringUtil.getSqlFilter(filter);
        String sql = "select RequestMaster.* from spf.RequestMaster "
                + (sqlFilter.length() > 0 ? " join CallCenterDB.dbo.ccCustomer on rmik=cuIK " : "")
                + "where rmStatusId in (3, 10)"
                + (year > 2000 ? " and rmDataYear = " + year : "");
        if (sqlFilter.length() > 0) {
            sql = sql + "\n"
                    + "    and (cast (rmIk as varchar) = " + sqlFilter
                    + "         or rmCode like " + sqlFilter
                    + "         or cuName like " + sqlFilter
                    + "         or cuCity like " + sqlFilter + ")";
        }
        Query query = getEntityManager().createNativeQuery(sql, SpecificFunctionRequest.class);
        List<SpecificFunctionRequest> result = query.getResultList();
        return result;
    }

    public List<SpecificFunctionRequest> getSpecificFunctionsForInek() {
        String jpql = "select spf from SpecificFunctionRequest spf where spf._statusId in (3, 10)";
        TypedQuery<SpecificFunctionRequest> query = getEntityManager().createQuery(jpql, SpecificFunctionRequest.class);
        List<SpecificFunctionRequest> result = query.getResultList();
        return result;
    }

    public List<Integer> getExistingYears(int ik) {
        String jpql = "SELECT distinct spf._dataYear FROM SpecificFunctionRequest spf WHERE spf._ik = :ik  and spf._statusId != 200";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        query.setParameter(IK, ik);
        return query.getResultList();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Common">
    public List<CenterName> getCenterNames() {
        return findAll(CenterName.class)
                .stream()
                .filter(n -> n.getId() > 0)
                .sorted((n1, n2) -> (n1.getId() == -1 ? "ZZZ" : n1.getName()).compareTo((n2.getId() == -1 ? "ZZZ" : n2.
                getName())))
                .collect(Collectors.toList());
    }

    public List<RelatedName> getRelatedNames() {
        return findAll(RelatedName.class)
                .stream()
                .filter(n -> n.getId() > 0)
                .sorted((n1, n2) -> (n1.getId() == -1 ? "ZZZ" : n1.getName()).compareTo((n2.getId() == -1 ? "ZZZ" : n2.
                getName())))
                .collect(Collectors.toList());
    }

    public List<SpecificFunction> getSpecificFunctions(boolean includeOther) {
        return findAll(SpecificFunction.class)
                .stream()
                .filter(f -> includeOther || f.getId() > 0)
                .sorted((f1, f2) -> (f1.getId() == -1 ? 999 : f1.getId()) - (f2.getId() == -1 ? 999 : f2.getId()))
                .collect(Collectors.toList());
    }

    public List<TypeExtraCharge> getTypeChargeExtra() {
        return findAll(TypeExtraCharge.class)
                .stream()
                .filter(n -> n.getId() > 0)
                .sorted((f1, f2) -> (f1.getId() == -1 ? 999 : f1.getId()) - (f2.getId() == -1 ? 999 : f2.getId()))
                .collect(Collectors.toList());
    }
    //</editor-fold>

}
