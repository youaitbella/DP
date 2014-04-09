package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.modelintention.AgreedPatients;

@Stateless
public class AgreedPatientsFacade extends AbstractFacade<AgreedPatients> {

    public AgreedPatientsFacade() {
        super(AgreedPatients.class);
    }
        
}
