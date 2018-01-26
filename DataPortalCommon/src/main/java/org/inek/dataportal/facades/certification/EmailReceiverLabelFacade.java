package org.inek.dataportal.facades.certification;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.certification.MapEmailReceiverLabel;
import org.inek.dataportal.facades.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class EmailReceiverLabelFacade extends AbstractFacade<MapEmailReceiverLabel> {

    public EmailReceiverLabelFacade() {
        super(MapEmailReceiverLabel.class);
    }

    public MapEmailReceiverLabel save(MapEmailReceiverLabel map) {
        if (map.getEmailReceiverLabelId() == -1) {
            persist(map);
            return map;
        }
        return merge(map);
    }

    
    public int findEmailReceiverListByLabel(String label) {
        String jpql = "SELECT i FROM MapEmailReceiverLabel i WHERE i._label = :label";
        TypedQuery<MapEmailReceiverLabel> query = getEntityManager().createQuery(jpql, MapEmailReceiverLabel.class);
        query.setParameter("label", label);
        List<MapEmailReceiverLabel> list = query.getResultList();
        if (list.size() > 0) {
            return list.get(0).getEmailReceiverLabelId();
        }
        return -1;
    }

}
