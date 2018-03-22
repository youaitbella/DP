package org.inek.dataportal.cert.facade;

import javax.ejb.Stateless;
import org.inek.dataportal.cert.entities.GrouperAction;
import org.inek.dataportal.common.data.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class GrouperActionFacade extends AbstractFacade<GrouperAction> {

    public GrouperActionFacade() {
        super(GrouperAction.class);
    }

}
