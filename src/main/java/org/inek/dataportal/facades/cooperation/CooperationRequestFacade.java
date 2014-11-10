package org.inek.dataportal.facades.cooperation;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.cooperation.CooperationRequest;
import org.inek.dataportal.facades.AbstractFacade;

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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Account> getCooperationRequestors(int accountId) {
        String query = "SELECT acId, acLastModified, acIsDeactivated, "
                + "acUser, acMail, acGender, acTitle, acFirstName, "
                + "acLastName, acInitials, acPhone, acRoleId, acCompany, acCustomerTypeId, "
                + "acIK, acStreet, acPostalCode, acTown, acCustomerPhone, acCustomerFax "
                + "from dbo.account "
                + "join usr.CooperationRequest on acId = crRequestorAccountId "
                + "where crRequestedAccountId = ?1";
        return getEntityManager().createNativeQuery(query, Account.class).setParameter(1, accountId).getResultList();
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public CooperationRequest findCooperationRequest(int requestorId, int requestedId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CooperationRequest> cq = cb.createQuery(CooperationRequest.class);
        Root request = cq.from(CooperationRequest.class);
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

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<CooperationRequest> findRequestsOlderThan(Date date) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CooperationRequest> cq = cb.createQuery(CooperationRequest.class);
        Root request = cq.from(CooperationRequest.class);
        cq.select(request).where(cb.lessThan(request.get("_creationDate"), date));
        return getEntityManager().createQuery(cq).getResultList();
    }

}
