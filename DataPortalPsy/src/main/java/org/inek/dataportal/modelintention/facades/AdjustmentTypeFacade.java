package org.inek.dataportal.modelintention.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.modelintention.entities.AdjustmentType;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class AdjustmentTypeFacade extends AbstractFacade<AdjustmentType> {

    public AdjustmentTypeFacade (){
        super(AdjustmentType.class);
    }
    
}
