package org.inek.dataportal.facades.common;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.common.CostCenter;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class CostCenterFacade extends AbstractFacade<CostCenter> {

    public CostCenterFacade (){
        super(CostCenter.class);
    }
    
}
