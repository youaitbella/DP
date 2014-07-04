package org.inek.dataportal.facades.admin;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.admin.Log;
import org.inek.dataportal.facades.AbstractFacade;

@Stateless
public class LogFacade extends AbstractFacade<Log> {

    public LogFacade (){
        super(Log.class);
    }
    
}
