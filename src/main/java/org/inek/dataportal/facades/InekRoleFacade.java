package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.InekRole;

@Stateless
public class InekRoleFacade extends AbstractFacade<InekRole> {

    public InekRoleFacade (){
        super(InekRole.class);
    }
    
}
