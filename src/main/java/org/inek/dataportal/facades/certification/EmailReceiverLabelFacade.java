package org.inek.dataportal.facades.certification;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public int findEmailReceiverListByLabel(String label) {
        String query = "SELECT i FROM MapEmailReceiverLabel i WHERE i._label = :label";
        List<MapEmailReceiverLabel> list = getEntityManager().createQuery(query, MapEmailReceiverLabel.class).setParameter("label", label).getResultList();
        if (list.size() > 0) {
            return list.get(0).getEmailReceiverLabelId();
        }
        return -1;
    }

}
