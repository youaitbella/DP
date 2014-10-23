package org.inek.dataportal.facades.admin;

import java.util.Date;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.inek.dataportal.entities.NubProposal;
import org.inek.dataportal.entities.admin.Log;
import org.inek.dataportal.facades.AbstractFacade;
import org.inek.dataportal.utils.DateUtils;

@Stateless
public class LogFacade extends AbstractFacade<Log> {

    public LogFacade() {
        super(Log.class);
    }

    @Schedule(info = "every day")
    private void removeOldEntries() {
        Date logDate = DateUtils.getDateWithDayOffset(-30);
        String sql = "DELETE FROM Log l WHERE l._creationDate < :date";
        Query query = getEntityManager().createQuery(sql, NubProposal.class);
        query.setParameter("date", logDate).executeUpdate();
    }

}
