package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.Log;

@Stateless
public class LogFacade extends AbstractFacade<Log> {

    public LogFacade (){
        super(Log.class);
    }
    
}
