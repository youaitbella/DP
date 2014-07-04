package org.inek.dataportal.facades.admin;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.admin.InekRole;
import org.inek.dataportal.facades.AbstractFacade;

@Stateless
public class InekRoleFacade extends AbstractFacade<InekRole> {

    public InekRoleFacade (){
        super(InekRole.class);
    }
    
}
