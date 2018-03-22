package org.inek.dataportal.psy.modelintention.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.psy.modelintention.entities.AdjustmentType;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class AdjustmentTypeFacade extends AbstractFacade<AdjustmentType> {

    public AdjustmentTypeFacade (){
        super(AdjustmentType.class);
    }
    
}
