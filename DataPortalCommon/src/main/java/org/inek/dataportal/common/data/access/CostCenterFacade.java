package org.inek.dataportal.common.data.access;

import javax.ejb.Stateless;
import org.inek.dataportal.common.data.common.CostCenter;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class CostCenterFacade extends AbstractFacade<CostCenter> {

    public CostCenterFacade (){
        super(CostCenter.class);
    }
    
}
