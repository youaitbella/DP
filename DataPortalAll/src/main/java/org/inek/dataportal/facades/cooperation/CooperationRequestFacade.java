package org.inek.dataportal.facades.cooperation;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.entities.cooperation.CooperationRequest;
import org.inek.dataportal.common.data.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class CooperationRequestFacade extends AbstractFacade<CooperationRequest> {

    public CooperationRequestFacade() {
        super(CooperationRequest.class);
    }

    /**
     * returns a list with info of all accounts who requested a cooperation with
     * the given accountId
     *
     * @param accountId Id of requested account
     * @return
     */
    public List<Account> getCooperationRequestors(int accountId) {
        String jpql = "select a from CooperationRequest r join Account a where r._requestorId = a._id and  r._requestedId = :accountId";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        return query.setParameter("accountId", accountId).getResultList();
    }

    public long getOpenCooperationRequestCount(int accountId){
        String jpql = "SELECT count(c._requestedId) FROM CooperationRequest c WHERE c._requestedId = :accountId";
        TypedQuery<Long> query = getEntityManager().createQuery(jpql, Long.class);
        return query.setParameter("accountId", accountId)
                .getSingleResult();
    } 
    
    public void createCooperationRequest(int requestorId, int requestedId) {
        if (findCooperationRequest(requestorId, requestedId) != null) {
            return;
        }
        CooperationRequest request = CooperationRequest.create(requestorId, requestedId);
        persist(request);
    }

    /**
     * finds
     *
     * @param requestorId
     * @param requestedId
     * @return
     */
    
    public CooperationRequest findCooperationRequest(int requestorId, int requestedId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CooperationRequest> cq = cb.createQuery(CooperationRequest.class);
        Root<CooperationRequest> request = cq.from(CooperationRequest.class);
        Predicate criteria = cb.conjunction();
        criteria = cb.and(criteria, cb.equal(request.get("_requestorId"), requestorId));
        criteria = cb.and(criteria, cb.equal(request.get("_requestedId"), requestedId));
        cq.select(request).where(criteria);
        List<CooperationRequest> requests = getEntityManager().createQuery(cq).getResultList();
        return requests.isEmpty() ? null : requests.get(0);
    }

    /**
     * Checks whether cooperation request (in any direction) exists
     *
     * @param partner1Id
     * @param partner2Id
     * @return
     */
    public boolean existsAnyCooperationRequest(int partner1Id, int partner2Id) {
        return findCooperationRequest(partner1Id, partner2Id) != null
                || findCooperationRequest(partner2Id, partner1Id) != null;
    }

    /**
     * removes cooperation requests, in both directions
     *
     * @param partner1Id
     * @param partner2Id
     */
    public void removeAnyCooperationRequest(int partner1Id, int partner2Id) {
        CooperationRequest cooperationRequest = findCooperationRequest(partner1Id, partner2Id);
        if (cooperationRequest != null) {
            remove(cooperationRequest);
        }
        cooperationRequest = findCooperationRequest(partner2Id, partner1Id);
        if (cooperationRequest != null) {
            remove(cooperationRequest);
        }
    }

    
    public List<CooperationRequest> findRequestsOlderThan(Date date) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CooperationRequest> cq = cb.createQuery(CooperationRequest.class);
        Root<CooperationRequest> request = cq.from(CooperationRequest.class);
        cq.select(request).where(cb.lessThan(request.<Date>get("_creationDate"), date));
        return getEntityManager().createQuery(cq).getResultList();
    }

}
