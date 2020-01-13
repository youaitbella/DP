package org.inek.dataportal.common.data.cooperation.facade;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.AbstractFacade;
import org.inek.dataportal.common.data.cooperation.entities.PortalMessage;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 *
 * @author muellermi
 */
@Stateless
public class PortalMessageFacade extends AbstractFacade<PortalMessage> {

    public static final String TO_ACCOUNT_ID = "_toAccountId";
    public static final String STATUS = "_status";
    public static final String FROM_ACCOUNT_ID = "_fromAccountId";
    public static final String FEATURE = "_feature";
    public static final String KEY_ID = "_keyId";
    public static final String CREATED = "_created";

    public PortalMessageFacade() {
        super(PortalMessage.class);
    }

    public int countUnreadMessages(int accountId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
        Root<PortalMessage> request = cq.from(PortalMessage.class);
        Predicate isReceiver = cb.equal(request.get(TO_ACCOUNT_ID), accountId);
        Predicate isUnread = cb.equal(request.get(STATUS), 0);
        cq.select(cb.count(request)).where(cb.and(isReceiver, isUnread));
        return (int) (long) getEntityManager().createQuery(cq).getSingleResult();
    }

    public int countUnreadMessages(int accountId, int fromAccountId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
        Root<PortalMessage> request = cq.from(PortalMessage.class);
        Predicate isReceiver = cb.equal(request.get(TO_ACCOUNT_ID), accountId);
        Predicate isSender = cb.equal(request.get(FROM_ACCOUNT_ID), fromAccountId);
        Predicate isUnread = cb.equal(request.get(STATUS), 0);
        cq.select(cb.count(request)).where(cb.and(isReceiver, isSender, isUnread));
        return (int) (long) getEntityManager().createQuery(cq).getSingleResult();
    }

    public List<PortalMessage> getMessagesByReceiver(int accountId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PortalMessage> cq = cb.createQuery(PortalMessage.class);
        Root<PortalMessage> request = cq.from(PortalMessage.class);
        Predicate isReceiver = cb.equal(request.get(TO_ACCOUNT_ID), accountId);
        cq.select(request).where(isReceiver);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<PortalMessage> getMessagesByReceiver(int accountId, Feature feature, int keyId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PortalMessage> cq = cb.createQuery(PortalMessage.class);
        Root<PortalMessage> request = cq.from(PortalMessage.class);
        Predicate isReceiver = cb.equal(request.get(TO_ACCOUNT_ID), accountId);
        Predicate isFeature = cb.equal(request.get(FEATURE), feature);
        Predicate isKeyId = cb.equal(request.get(KEY_ID), keyId);
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
        cq.orderBy(cb.desc(request.get(CREATED)));
        return getEntityManager().createQuery(cq).getResultList();
    }

    private Predicate isSender(CriteriaBuilder cb, Root request, int accountId) {
        return cb.equal(request.get(FROM_ACCOUNT_ID), accountId);
    }

    private Predicate isReceiver(CriteriaBuilder cb, Root request, int accountId) {
        return cb.equal(request.get(TO_ACCOUNT_ID), accountId);
    }

    public void save(PortalMessage portalMessage) {
        persist(portalMessage);
    }

}
