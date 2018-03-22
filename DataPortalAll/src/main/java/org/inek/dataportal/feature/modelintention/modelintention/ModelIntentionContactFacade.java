package org.inek.dataportal.feature.modelintention.modelintention;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.modelintention.ModelIntentionContact;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class ModelIntentionContactFacade extends AbstractFacade<ModelIntentionContact> {

    public ModelIntentionContactFacade() {
        super(ModelIntentionContact.class);
    }
        
}
