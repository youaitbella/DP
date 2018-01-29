package org.inek.dataportal.facades.modelintention;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.modelintention.ModelIntentionContact;
import org.inek.dataportal.facades.AbstractFacade;

@Stateless
public class ModelIntentionContactFacade extends AbstractFacade<ModelIntentionContact> {

    public ModelIntentionContactFacade() {
        super(ModelIntentionContact.class);
    }
        
}
