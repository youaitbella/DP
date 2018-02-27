package org.inek.dataportal.facades.common;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.common.CostType;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class CostTypeFacade extends AbstractFacade<CostType> {

    public CostTypeFacade (){
        super(CostType.class);
    }
    
}
