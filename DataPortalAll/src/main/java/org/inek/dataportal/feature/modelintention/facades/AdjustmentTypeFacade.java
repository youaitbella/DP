package org.inek.dataportal.feature.modelintention.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.feature.modelintention.entities.AdjustmentType;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class AdjustmentTypeFacade extends AbstractFacade<AdjustmentType> {

    public AdjustmentTypeFacade (){
        super(AdjustmentType.class);
    }
    
}
