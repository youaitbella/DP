package org.inek.dataportal.feature.admin.facade;

import javax.ejb.Stateless;
import org.inek.dataportal.feature.admin.entity.RoleMapping;
import org.inek.dataportal.facades.AbstractFacade;

@Stateless
public class RoleMappingFacade extends AbstractFacade<RoleMapping> {

    public RoleMappingFacade (){
        super(RoleMapping.class);
    }

    public void save(RoleMapping mapping) {
        persist(mapping);
    }
    
}
