package org.inek.dataportal.common.data.adm.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.adm.ActionLog;
import org.inek.dataportal.common.data.adm.ChangeLog;
import org.inek.dataportal.common.data.adm.Log;
import org.inek.dataportal.common.utils.DateUtils;

@Stateless
public class LogFacade extends AbstractDataAccess{

    @Schedule(hour = "2", minute = "30", info = "once a day")
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

    public void saveLog(Log log) {
        persist(log);
    }

    public void saveActionLog(ActionLog log) {
        persist(log);
    }

    @Transactional
    public void saveChangeLogs(List<ChangeLog> actions) {
        for (ChangeLog ac : actions) {
            if (ac.getId() == 0) {
                persist(ac);
                continue;
            }
            merge(ac);
        }
        actions.clear();
    }
    
}
