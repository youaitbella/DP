package org.inek.dataportal.webservices;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * This service may be used to check, whether the application is still alive.
 * @author muellermi
 */
@WebService(serviceName = "PingService")
public class PingService {

    @WebMethod(operationName = "ping")
    public String ping() {
        return "ok";
    }

}
