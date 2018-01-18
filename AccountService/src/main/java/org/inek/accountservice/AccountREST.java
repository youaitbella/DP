package org.inek.accountservice;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.Schedule;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author muellermi
 */
@Path("account")
public class AccountREST {

    private static final long VALID_TIME = 2000;
    private static final Map<String, AccountInfo> ACCOUNT_INFOS = new ConcurrentHashMap<>();

    @GET
    @Path("id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getToken(@PathParam("id") String id) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        ACCOUNT_INFOS.put(uuid, new AccountInfo(id));
        return uuid;
    }

    @GET
    @Path("token/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAccountId(@PathParam("token") String token) {
        String id = "";
        AccountInfo accountInfo = ACCOUNT_INFOS.remove(token);
        long ts = new Date().getTime();
        if (accountInfo != null && ts - accountInfo.getTs() <= VALID_TIME) {
            id = accountInfo.getId();
        }
        return id;
    }

    @Schedule(hour = "*", minute = "*", second = "*/10")
    public void sweepOld() {
        long ts = new Date().getTime();
        Iterator<String> iterator = ACCOUNT_INFOS.keySet().iterator();
        while (iterator.hasNext()) {
            String token = iterator.next();
            AccountInfo accountInfo = ACCOUNT_INFOS.get(token);
            if (accountInfo != null && ts - accountInfo.getTs() > VALID_TIME) {
                iterator.remove();
            }
        }
    }

    int getSize() {
        return ACCOUNT_INFOS.size();
    }
    
    static void clear(){
        ACCOUNT_INFOS.clear();
    }
}
