package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.additionalcost.AdditionalCost;

/**
 *
 * @author muellermi
 */
@Stateless
public class AdditionalCostFacade extends AbstractFacade<AdditionalCost> {

    public AdditionalCostFacade() {
        super(AdditionalCost.class);
    }
    
    
}
