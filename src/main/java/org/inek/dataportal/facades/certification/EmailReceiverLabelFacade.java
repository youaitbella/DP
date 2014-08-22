package org.inek.dataportal.facades.certification;

import javax.ejb.Stateless;
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
}
