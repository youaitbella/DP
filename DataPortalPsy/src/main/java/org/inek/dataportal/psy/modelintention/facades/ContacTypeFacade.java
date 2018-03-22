package org.inek.dataportal.psy.modelintention.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.psy.modelintention.entities.ContactType;
import org.inek.dataportal.common.data.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class ContacTypeFacade extends AbstractFacade<ContactType> {

    public ContacTypeFacade() {
        super(ContactType.class);
    }
    
}
