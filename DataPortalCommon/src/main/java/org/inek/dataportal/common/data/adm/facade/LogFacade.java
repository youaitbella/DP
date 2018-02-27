package org.inek.dataportal.common.data.adm.facade;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.inek.dataportal.common.data.adm.Log;
import org.inek.dataportal.common.data.AbstractFacade;
import org.inek.dataportal.common.utils.DateUtils;

@Stateless
public class LogFacade extends AbstractFacade<Log> implements Serializable{

    public LogFacade() {
        super(Log.class);
    }

    @Schedule(info = "every day")
    private void startRemoveOldEntries() {
        removeOldEntries();
    }
    
    @Asynchronous
    private void removeOldEntries() {
        Date logDate = DateUtils.getDateWithDayOffset(-90);
        String sql = "DELETE FROM Log l WHERE l._creationDate < :date";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("date", logDate).executeUpdate();
    }

    public void save(Log log) {
        persist(log);
    }

}
