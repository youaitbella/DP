package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.ModelIntentionStructureInvolved;

@Stateless
public class ModelIntentionStructureInvolvedFacade extends AbstractFacade<ModelIntentionStructureInvolved> {

    public ModelIntentionStructureInvolvedFacade() {
        super(ModelIntentionStructureInvolved.class);
    }
        
}
