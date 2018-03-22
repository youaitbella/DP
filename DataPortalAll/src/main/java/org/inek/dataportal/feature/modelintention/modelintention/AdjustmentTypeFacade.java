package org.inek.dataportal.feature.modelintention.modelintention;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.modelintention.AdjustmentType;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class AdjustmentTypeFacade extends AbstractFacade<AdjustmentType> {

    public AdjustmentTypeFacade (){
        super(AdjustmentType.class);
    }
    
}
