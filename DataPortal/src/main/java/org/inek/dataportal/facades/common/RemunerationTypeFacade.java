package org.inek.dataportal.facades.common;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.common.RemunerationType;
import org.inek.dataportal.facades.AbstractFacade;

@Stateless
public class RemunerationTypeFacade extends AbstractFacade<RemunerationType> {

    public RemunerationTypeFacade (){
        super(RemunerationType.class);
    }
    
}
