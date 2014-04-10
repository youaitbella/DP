package org.inek.dataportal.facades.modelintention;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.modelintention.ModelIntentionModelLife;
import org.inek.dataportal.facades.AbstractFacade;

@Stateless
public class ModelIntentionModelLifeFacade extends AbstractFacade<ModelIntentionModelLife> {

    public ModelIntentionModelLifeFacade() {
        super(ModelIntentionModelLife.class);
    }
        
}
