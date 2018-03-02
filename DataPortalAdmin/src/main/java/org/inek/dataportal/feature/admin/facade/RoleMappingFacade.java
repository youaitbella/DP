package org.inek.dataportal.feature.admin.facade;

import javax.ejb.Stateless;
import org.inek.dataportal.common.data.adm.RoleMapping;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class RoleMappingFacade extends AbstractFacade<RoleMapping> {

    public RoleMappingFacade (){
        super(RoleMapping.class);
    }

    public void save(RoleMapping mapping) {
        persist(mapping);
    }
    
}
