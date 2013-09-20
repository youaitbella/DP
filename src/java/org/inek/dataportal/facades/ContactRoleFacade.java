package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.ContactRole;

@Stateless
public class ContactRoleFacade extends AbstractFacade<ContactRole> {

    public ContactRoleFacade (){
        super(ContactRole.class);
    }
    
}
