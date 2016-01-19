package org.inek.dataportal.facades;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.PasswordRequest;
import org.inek.dataportal.utils.DateUtils;

/**
 *
 * @author muellermi
 */
@Stateless
public class PasswordRequestFacade extends AbstractFacade<PasswordRequest> {

    public PasswordRequestFacade() {
        super(PasswordRequest.class);
    }

    
    public List<PasswordRequest> findRequestsOlderThan(Date date) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PasswordRequest> cq = cb.createQuery(PasswordRequest.class);
        Root request = cq.from(PasswordRequest.class);
        cq.select(request).where(cb.lessThan(request.get("_creationDate"), date));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Schedule(hour = "*", minute = "*/15", info = "every 15 minutes")
    private void cleanPasswordRequests() {
        List<PasswordRequest> requests = findRequestsOlderThan(DateUtils.getDateWithMinuteOffset(-60));
        for (PasswordRequest request : requests) {
            _logger.log(Level.WARNING, "Clean password request {0}", request.getAccountId());
            remove(request);
        }
    }

}
