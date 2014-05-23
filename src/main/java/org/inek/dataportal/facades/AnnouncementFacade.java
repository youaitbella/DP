package org.inek.dataportal.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.Announcement;

@Stateless
public class AnnouncementFacade extends AbstractFacade<Announcement> {

    public AnnouncementFacade (){
        super(Announcement.class);
    }
    
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
