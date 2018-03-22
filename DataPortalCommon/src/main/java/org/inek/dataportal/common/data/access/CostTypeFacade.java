package org.inek.dataportal.common.data.access;

import javax.ejb.Stateless;
import org.inek.dataportal.common.data.common.CostType;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class CostTypeFacade extends AbstractFacade<CostType> {

    public CostTypeFacade (){
        super(CostType.class);
    }
    
}
