package org.inek.dataportal.facades.admin;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.admin.InekRole;
import org.inek.dataportal.facades.AbstractFacade;

@Stateless
public class InekRoleFacade extends AbstractFacade<InekRole> {

    public InekRoleFacade (){
        super(InekRole.class);
    }

    public InekRole save(InekRole role) {
        if (role.getId() == -1) {
            persist(role);
            return role;
        }
        return merge(role);
    }
    
}
