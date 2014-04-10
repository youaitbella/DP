package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.modelintention.ModelIntentionContact;

@Stateless
public class ModelIntentionStructureInvolvedFacade extends AbstractFacade<ModelIntentionContact> {

    public ModelIntentionStructureInvolvedFacade() {
        super(ModelIntentionContact.class);
    }
        
}
