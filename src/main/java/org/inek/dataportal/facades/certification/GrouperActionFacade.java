package org.inek.dataportal.facades.certification;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.certification.GrouperAction;
import org.inek.dataportal.facades.AbstractFacade;

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
