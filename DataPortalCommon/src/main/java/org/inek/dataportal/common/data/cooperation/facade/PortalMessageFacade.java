package org.inek.dataportal.common.data.cooperation.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.common.data.cooperation.entities.PortalMessage;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class PortalMessageFacade extends AbstractFacade<PortalMessage> {

    public PortalMessageFacade() {
        super(PortalMessage.class);
    }

    public int countUnreadMessages(int accountId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
        Root<PortalMessage> request = cq.from(PortalMessage.class);
        Predicate isReceiver = cb.equal(request.get("_toAccountId"), accountId);
        Predicate isUnread = cb.equal(request.get("_status"), 0);
        cq.select(cb.count(request)).where(cb.and(isReceiver, isUnread));
        return (int) (long) getEntityManager().createQuery(cq).getSingleResult();
    }

    public int countUnreadMessages(int accountId, int fromAccountId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
        Root<PortalMessage> request = cq.from(PortalMessage.class);
        Predicate isReceiver = cb.equal(request.get("_toAccountId"), accountId);
        Predicate isSender = cb.equal(request.get("_fromAccountId"), fromAccountId);
        Predicate isUnread = cb.equal(request.get("_status"), 0);
        cq.select(cb.count(request)).where(cb.and(isReceiver, isSender, isUnread));
        return (int) (long) getEntityManager().createQuery(cq).getSingleResult();
    }

    public List<PortalMessage> getMessagesByReceiver(int accountId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PortalMessage> cq = cb.createQuery(PortalMessage.class);
        Root<PortalMessage> request = cq.from(PortalMessage.class);
        Predicate isReceiver = cb.equal(request.get("_toAccountId"), accountId);
        cq.select(request).where(isReceiver);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<PortalMessage> getMessagesByReceiver(int accountId, Feature feature, int keyId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PortalMessage> cq = cb.createQuery(PortalMessage.class);
        Root<PortalMessage> request = cq.from(PortalMessage.class);
        Predicate isReceiver = cb.equal(request.get("_toAccountId"), accountId);
        Predicate isFeature = cb.equal(request.get("_feature"), feature);
        Predicate isKeyId = cb.equal(request.get("_keyId"), keyId);
        cq.select(request).where(cb.and(isReceiver, isFeature, isKeyId));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<PortalMessage> getMessagesByParticipants(int accountId, int partnerId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PortalMessage> cq = cb.createQuery(PortalMessage.class);
        Root<PortalMessage> request = cq.from(PortalMessage.class);
        Predicate isReceiver = cb.or(isReceiver(cb, request, accountId), isReceiver(cb, request, partnerId));
        Predicate isSender = cb.or(isSender(cb, request, accountId), isSender(cb, request, partnerId));
        cq.where(cb.and(isReceiver, isSender));
        cq.select(request);
        cq.orderBy(cb.desc(request.get("_created")));
        return getEntityManager().createQuery(cq).getResultList();
    }

    private Predicate isSender(CriteriaBuilder cb, Root request, int accountId) {
        return cb.equal(request.get("_fromAccountId"), accountId);
    }

    private Predicate isReceiver(CriteriaBuilder cb, Root request, int accountId) {
        return cb.equal(request.get("_toAccountId"), accountId);
    }

    public void save(PortalMessage portalMessage) {
        persist(portalMessage);
    }

}
