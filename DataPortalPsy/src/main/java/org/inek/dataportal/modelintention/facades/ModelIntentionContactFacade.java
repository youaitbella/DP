package org.inek.dataportal.modelintention.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.modelintention.entities.ModelIntentionContact;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class ModelIntentionContactFacade extends AbstractFacade<ModelIntentionContact> {

    public ModelIntentionContactFacade() {
        super(ModelIntentionContact.class);
    }
        
}
