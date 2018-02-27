package org.inek.dataportal.facades.modelintention;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.modelintention.ModelLife;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class ModelIntentionModelLifeFacade extends AbstractFacade<ModelLife> {

    public ModelIntentionModelLifeFacade() {
        super(ModelLife.class);
    }
        
}
