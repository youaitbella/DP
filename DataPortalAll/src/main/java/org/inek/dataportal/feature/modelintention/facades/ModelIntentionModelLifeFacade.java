package org.inek.dataportal.feature.modelintention.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.feature.modelintention.entities.ModelLife;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class ModelIntentionModelLifeFacade extends AbstractFacade<ModelLife> {

    public ModelIntentionModelLifeFacade() {
        super(ModelLife.class);
    }
        
}
