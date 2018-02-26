package org.inek.dataportal.feature.admin.facade;

import java.util.Date;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.inek.dataportal.entities.nub.NubRequest;
import org.inek.dataportal.common.data.adm.Log;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.facades.AbstractFacade;

@Stateless
public class LogFacade extends AbstractFacade<Log> {

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
        Query query = getEntityManager().createQuery(sql, NubRequest.class);
        query.setParameter("date", logDate).executeUpdate();
    }

    public void save(Log log) {
        persist(log);
    }

}
