package org.inek.dataportal.feature.admin.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.common.data.adm.Announcement;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class AnnouncementFacade extends AbstractFacade<Announcement> {

    public AnnouncementFacade() {
        super(Announcement.class);
    }

    public List<Announcement> findActiveWarnings() {
        clearCache(Announcement.class);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Announcement> cq = cb.createQuery(Announcement.class);
        Root<Announcement> request = cq.from(Announcement.class);
        cq.select(request)
                .where(cb.isTrue(request.get("_isActive")))
                .orderBy(cb.asc(request.get("_id")));
        return getEntityManager().createQuery(cq).getResultList();
    }

}
