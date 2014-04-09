package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.modelintention.ModelIntentionModelLife;

@Stateless
public class ModelIntentionModelLifeFacade extends AbstractFacade<ModelIntentionModelLife> {

    public ModelIntentionModelLifeFacade() {
        super(ModelIntentionModelLife.class);
    }
        
}
