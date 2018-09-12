package org.inek.idservice;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class IdManager {
    public static final long VALID_TIME = 5000;
    private final Map<String, IdInfo> _accountInfos = new ConcurrentHashMap<>();
 
    
    public int getSize() {
        return _accountInfos.size();
    }
    
    public void clear(){
        _accountInfos.clear();
    }

    public String getToken(String id) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        _accountInfos.put(uuid, new IdInfo(id));
        return uuid;
    }

    public String getAccountId(String token) {
        String id = "";
        IdInfo accountInfo = _accountInfos.remove(token);
        long ts = new Date().getTime();
        if (accountInfo != null && ts - accountInfo.getTs() <= VALID_TIME) {
            id = accountInfo.getId();
        }
        return id;
    }
    
    public void sweepOld() {
        long ts = new Date().getTime();
        Iterator<String> iterator = _accountInfos.keySet().iterator();
        while (iterator.hasNext()) {
            String token = iterator.next();
            IdInfo accountInfo = _accountInfos.get(token);
            if (accountInfo != null && ts - accountInfo.getTs() > VALID_TIME) {
                iterator.remove();
            }
        }
    }

}
