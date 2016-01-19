package org.inek.dataportal.facades.modelintention;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.modelintention.ContactType;
import org.inek.dataportal.facades.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class ContacTypeFacade extends AbstractFacade{

    public ContacTypeFacade() {
        super(ContactType.class);
    }
    
}
