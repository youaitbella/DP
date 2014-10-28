package org.inek.dataportal.facades.admin;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.admin.Announcement;
import org.inek.dataportal.facades.AbstractFacade;

@Stateless
public class AnnouncementFacade extends AbstractFacade<Announcement> {

    public AnnouncementFacade() {
        super(Announcement.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Announcement> findActiveWarnings() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Announcement> cq = cb.createQuery(Announcement.class);
        Root request = cq.from(Announcement.class);
        cq.select(request)
                .where(cb.isTrue(request.get("_isActive")))
                .orderBy(cb.asc(request.get("_id")));
        return getEntityManager().createQuery(cq).getResultList();
    }

}
