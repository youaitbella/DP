/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<NubRequest> findAll(int accountId, DataSet dataSet, String filter) {
        if (dataSet == DataSet.All) {
            // todo: is this user allowed to get the whole list?
            return Collections.EMPTY_LIST;
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<NubRequest> cq = cb.createQuery(NubRequest.class);
        Root request = cq.from(NubRequest.class);
        Predicate status;
        Order order;
        if (dataSet == DataSet.AllOpen) {
            status = cb.lessThan(request.get("_status"), WorkflowStatus.Provided.getValue());
            order = cb.asc(request.get("_nubId"));
        }else if (dataSet == DataSet.ApprovalRequested) {
            status = cb.equal(request.get("_status"), WorkflowStatus.ApprovalRequested.getValue());
            order = cb.asc(request.get("_nubId"));
        } else {
            status = cb.greaterThanOrEqualTo(request.get("_status"), WorkflowStatus.Provided.getValue());
            order = cb.desc(request.get("_nubId"));
        }
        if (dataSet == DataSet.All) {
            cq.select(request).where(status).orderBy(order);
        } else {
            Predicate isAccount = cb.equal(request.get("_accountId"), accountId);
            if (!filter.isEmpty()) {
                Predicate isFiltered = cb.or(cb.like(request.get("_name"), filter), cb.like(request.get("_displayName"), filter));
                cq.select(request).where(cb.and(isAccount, status, isFiltered)).orderBy(order);
            } else {
                cq.select(request).where(cb.and(isAccount, status)).orderBy(order);
            }
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<NubRequest> findAll(int accountId) {
        String sql = "SELECT p FROM NubRequest p WHERE p._accountId = :accountId ORDER BY p._nubId DESC";
        Query query = getEntityManager().createQuery(sql, NubRequest.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public NubRequest saveNubRequest(NubRequest nubRequest) {
        if (nubRequest.getStatus() == WorkflowStatus.Unknown) {
            nubRequest.setStatus(WorkflowStatus.New);
        }

        if (nubRequest.getNubId() == -1) {
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
        List<NubRequest> requests = findAll(accountId, dataSet, filter);
        List<ProposalInfo> proposalInfos = new ArrayList<>();
        for (NubRequest request : requests) {
            String displayName = request.getDisplayName().trim().length() == 0
                    ? request.getName()
                    : request.getDisplayName();
            proposalInfos.add(new ProposalInfo(request.getNubId(), displayName, request.getTargetYear(), request.getStatus()));
        }
        return proposalInfos;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<ProposalInfo> findForAccountAndIk(int accountId, int ik, int minStatus, int maxStatus, String filter) {
        String jql = "SELECT p FROM NubRequest p "
                + "WHERE p._accountId = :accountId and p._ik = :ik and p._status >= :minStatus and p._status <= :maxStatus "
                + (filter.isEmpty() ? "" : "and (p._displayName like :filter1 or p._name like :filter2) ")
                + "ORDER BY p._nubId DESC";
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
            proposalInfos.add(new ProposalInfo(request.getNubId(), displayName, request.getTargetYear(), request.getStatus()));
        }
        return proposalInfos;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Integer> findAccountIdForIk(int ik) {
        String sql = "SELECT DISTINCT p._accountId FROM NubRequest p WHERE p._ik = :ik  ";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("ik", ik);
        List<Integer> accountIds = query.getResultList();
        return accountIds;
    }

    public Map<Integer, Integer> countOpenPerIk() {
        return NubRequestFacade.this.countOpenPerIk(1 + Calendar.getInstance().get(Calendar.YEAR));
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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


}
