package org.inek.dataportal.feature.modelintention.modelintention;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.modelintention.ContactType;
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
