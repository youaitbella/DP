/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.eclipse.persistence.jpa.JpaQuery;
import org.inek.dataportal.entities.NubRequest;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.helper.structures.ProposalInfo;

/**
 *
 * @author muellermi
 */
@Stateless
public class NubRequestFacade extends AbstractFacade<NubRequest> {

    public NubRequestFacade() {
        super(NubRequest.class);
    }

    public List<NubRequest> findAll(int accountId, DataSet dataSet, String filter) {
        return findAll(accountId, -1, -1, dataSet, filter);
    }

    public List<NubRequest> findAll(int accountId, int ik, int year, DataSet dataSet, String filter) {
        if (dataSet == DataSet.All) {
            // todo: is this user allowed to get the whole list?
            return Collections.EMPTY_LIST;
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<NubRequest> cq = cb.createQuery(NubRequest.class);
        Root request = cq.from(NubRequest.class);
        Predicate condition = null;
        Order order = null;
        if (null != dataSet) {
            switch (dataSet) {
                case All:
                    condition = cb.ge(request.get("_status"), WorkflowStatus.New.getValue());
                    order = cb.asc(request.get("_id"));
                    break;
                case AllOpen:
                    condition = cb.lessThan(request.get("_status"), WorkflowStatus.Provided.getValue());
                    order = cb.asc(request.get("_id"));
                    break;
                case ApprovalRequested:
                    condition = cb.or(cb.equal(request.get("_status"), WorkflowStatus.ApprovalRequested.getValue()),
                            cb.equal(request.get("_status"), WorkflowStatus.CorrectionRequested.getValue()));
                    order = cb.asc(request.get("_id"));
                    break;
                default:
                    // provided (sealed)
                    condition = cb.greaterThanOrEqualTo(request.get("_status"), WorkflowStatus.Provided.getValue());
                    order = cb.desc(request.get("_id"));
                    break;
            }
        }
        condition = cb.and(condition, cb.equal(request.get("_accountId"), accountId));
        if (!filter.isEmpty()) {
            Predicate isFiltered = cb.or(cb.like(request.get("_name"), filter), cb.like(request.get("_displayName"), filter));
            condition = cb.and(condition, isFiltered);
        }
        if (ik > 0) {
            condition = cb.and(condition, cb.equal(request.get("_ik"), ik));
        }
        if (year > 0) {
            condition = cb.and(condition, cb.equal(request.get("_targetYear"), year));
        }
        cq.select(request).where(condition).orderBy(order);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<NubRequest> findAll(int accountId) {
        String sql = "SELECT p FROM NubRequest p WHERE p._accountId = :accountId ORDER BY p._id DESC";
        Query query = getEntityManager().createQuery(sql, NubRequest.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public NubRequest saveNubRequest(NubRequest nubRequest) {
        if (nubRequest.getStatus() == WorkflowStatus.Unknown) {
            nubRequest.setStatus(WorkflowStatus.New);
        }

        if (nubRequest.getId() == -1) {
            persist(nubRequest);
            return nubRequest;
        }
        return merge(nubRequest);
    }

    /**
     * A list of NUB infos for display usage, e.g. lists
     *
     * @param accountId
     * @param dataSet
     * @param filter
     * @return
     */
    public List<ProposalInfo> getNubRequestInfos(int accountId, DataSet dataSet, String filter) {
        return getNubRequestInfos(accountId, -1, dataSet, filter);
    }

    public List<ProposalInfo> getNubRequestInfos(int accountId, int ik, DataSet dataSet, String filter) {
        return getNubRequestInfos(accountId, ik, -1, dataSet, filter);
    }

    public List<ProposalInfo> getNubRequestInfos(int accountId, int ik, int year, DataSet dataSet, String filter) {
        List<NubRequest> requests = findAll(accountId, ik, year, dataSet, filter);
        List<ProposalInfo> proposalInfos = new ArrayList<>();
        for (NubRequest request : requests) {
            String displayName = request.getDisplayName().trim().length() == 0
                    ? request.getName()
                    : request.getDisplayName();
            proposalInfos.add(new ProposalInfo(request.getId(), displayName, request.getTargetYear(), request.getStatus()));
        }
        return proposalInfos;
    }

    public List<ProposalInfo> findForAccountAndIk(int accountId, int ik, int minStatus, int maxStatus, String filter) {
        String jql = "SELECT p FROM NubRequest p "
                + "WHERE p._accountId = :accountId and p._ik = :ik and p._status >= :minStatus and p._status <= :maxStatus "
                + (filter.isEmpty() ? "" : "and (p._displayName like :filter1 or p._name like :filter2) ")
                + "ORDER BY p._id DESC";
        Query query = getEntityManager().createQuery(jql, NubRequest.class);
        query.setParameter("accountId", accountId);
        query.setParameter("ik", ik);
        query.setParameter("minStatus", minStatus);
        query.setParameter("maxStatus", maxStatus);
        if (!filter.isEmpty()) {
            query.setParameter("filter1", filter);
            query.setParameter("filter2", filter);
        }
        List<NubRequest> requests = query.getResultList();
        List<ProposalInfo> proposalInfos = new ArrayList<>();
        for (NubRequest request : requests) {
            String displayName = request.getDisplayName().trim().length() == 0
                    ? request.getName()
                    : request.getDisplayName();
            proposalInfos.add(new ProposalInfo(request.getId(), displayName, request.getTargetYear(), request.getStatus()));
        }
        return proposalInfos;
    }

    public List<Integer> getNubYears(int accountId) {
        Set<Integer> accountIds = new HashSet<>();
        accountIds.add(accountId);
        return getNubYears(accountIds);
    }

    public List<Integer> getNubYears(Set<Integer> accountIds) {
        String jql = "SELECT DISTINCT p._targetYear FROM NubRequest p WHERE p._accountId in :accountIds and p._status >= 10 ORDER BY p._targetYear DESC";
        Query query = getEntityManager().createQuery(jql, NubRequest.class);
        query.setParameter("accountIds", accountIds);
//        String sql = query.unwrap(JpaQuery.class).getDatabaseQuery().getSQLString();
//        System.out.println(sql);
        return query.getResultList();
    }

    public Set<Integer> checkAccountsForNubOfYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jql = "SELECT DISTINCT p._accountId FROM NubRequest p WHERE p._accountId in :accountIds and (p._targetYear = :year or -1 = :year) and p._status between :statusLow and :statusHigh";
        Query query = getEntityManager().createQuery(jql, NubRequest.class);
        query.setParameter("accountIds", accountIds);
        query.setParameter("year", year);
        query.setParameter("statusLow", statusLow.getValue());
        query.setParameter("statusHigh", statusHigh.getValue());
        return new HashSet<>(query.getResultList());
    }

    public List<Integer> findAccountIdForIk(int ik) {
        String sql = "SELECT DISTINCT p._accountId FROM NubRequest p WHERE p._ik = :ik  ";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    public Map<Integer, Integer> countOpenPerIk() {
        return NubRequestFacade.this.countOpenPerIk(1 + Calendar.getInstance().get(Calendar.YEAR));
    }

    public Map<Integer, Integer> countOpenPerIk(int targetYear) {
        String jql = "SELECT p._accountId, COUNT(p) FROM NubRequest p JOIN Account a WHERE p._accountId = a._id and a._customerTypeId = 5 and p._status < 10 and p._targetYear = :targetYear GROUP BY p._accountId";
        Query query = getEntityManager().createQuery(jql);
        query.setParameter("targetYear", targetYear);
        //String sql = query.unwrap(JpaQuery.class).getDatabaseQuery().getSQLString();
        //System.out.println(sql);
        List data = query.getResultList();
        Map<Integer, Integer> result = new HashMap<>();
        for (Object x : data) {
            Object[] info = (Object[]) x;
            int accountId = (int) info[0];
            int count = (int) (long) info[1];
            result.put(accountId, count);
        }
        return result;
    }

    public List<NubRequest> find(List<Integer> requestIds) {
        String sql = "SELECT p FROM NubRequest p WHERE p._id in :requestIds  ";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("requestIds", requestIds);
        return query.getResultList();
    }
}
