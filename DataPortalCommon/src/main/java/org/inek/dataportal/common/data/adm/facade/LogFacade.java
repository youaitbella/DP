package org.inek.dataportal.common.data.adm.facade;

import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.adm.ActionLog;
import org.inek.dataportal.common.data.adm.ChangeLog;
import org.inek.dataportal.common.data.adm.Log;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;

import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Stateless
public class LogFacade extends AbstractDataAccess implements Serializable {

    private SessionController _sessionController;

    public SessionController getSessionController() {
        return _sessionController;
    }

    @Inject
    public void setSessionController(SessionController sessionController) {
        _sessionController = sessionController;
    }

    @Schedule(hour = "2", minute = "30", info = "once a day")
    private void startRemoveOldEntries() {
        removeOldEntries();
    }

    @Asynchronous
    private void removeOldEntries() {
        removeOldLogs();
        removeOldActionLogs();
    }

    private void removeOldLogs() {
        Date logDate = DateUtils.getDateWithDayOffset(-90);
        String sql = "DELETE FROM Log l WHERE l._creationDate < :date";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("date", logDate).executeUpdate();
    }

    private void removeOldActionLogs() {
        Date logDate = DateUtils.getDateWithDayOffset(-400);
        String sql = "DELETE FROM ActionLog l WHERE l._timeStamp < :date";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("date", logDate).executeUpdate();
    }

    public void saveLog(Log log) {
        persist(log);
    }

    public void saveActionLog(String entity, int entryId, WorkflowStatus status) {
        ActionLog log = new ActionLog(_sessionController.getAccountId(), entity, entryId, status);
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
