package org.inek.dataportal.facades.modelintention;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.modelintention.AgreedPatients;
import org.inek.dataportal.facades.AbstractFacade;

@Stateless
public class AgreedPatientsFacade extends AbstractFacade<AgreedPatients> {

    public AgreedPatientsFacade() {
        super(AgreedPatients.class);
    }
        
}
