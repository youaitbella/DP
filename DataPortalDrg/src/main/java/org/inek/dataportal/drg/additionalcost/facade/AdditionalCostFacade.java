package org.inek.dataportal.drg.additionalcost.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.drg.additionalcost.entity.AdditionalCost;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;

/**
 *
 * @author aitbellayo
 */
@Stateless
public class AdditionalCostFacade extends AbstractDataAccessWithActionLog {

    public AdditionalCost findAdditionalCost(int id) {
        return find(AdditionalCost.class, id);
    }

    public List<AdditionalCost> obtainAdditionalCosts() {
        return findAll(AdditionalCost.class);
    }

    public List<AdditionalCost> getAdditionalCosts(Set<Integer> accountIds, Set<Integer> allowedManagedIks,
            Set<Integer> deniedManagedIks, DataSet dataSet) {
        String jpql = "SELECT n FROM AdditionalCost n "
                + "WHERE n._statusId BETWEEN :minStatus AND :maxStatus "
                + "and ("
                + (allowedManagedIks.size() > 0 ? "     n._ik in :allowedManagedIks or " : "")
                + "n._accountId in :accountIds"
                + (deniedManagedIks.size() > 0 ? " and n._ik not in :deniedManagedIks" : "")
                + ")"
                + "ORDER BY n._id";
        TypedQuery<AdditionalCost> query = getEntityManager().createQuery(jpql, AdditionalCost.class);
        query.setParameter("accountIds", accountIds);
        if (allowedManagedIks.size() > 0) {
            query.setParameter("allowedManagedIks", allowedManagedIks);
        }
        if (deniedManagedIks.size() > 0) {
            query.setParameter("deniedManagedIks", deniedManagedIks);
        }
        query.setParameter("minStatus", dataSet.getMinStatus().getId());
        query.setParameter("maxStatus", dataSet.getMaxStatus().getId());
        return query.getResultList();
    }

    public AdditionalCost saveAdditionalCost(AdditionalCost _additionalCost) {
        if (_additionalCost.getStatus() == WorkflowStatus.Unknown) {
            _additionalCost.setStatus(WorkflowStatus.New);
        }
        if (_additionalCost.getId() == -1) {
            persist(_additionalCost);
            return _additionalCost;
        }
        return merge(_additionalCost);
    }

    public AdditionalCost merge(AdditionalCost entity) {
        return super.merge(entity);
    }

    public void deleteAdditionalCost(AdditionalCost additionalCost) {
        remove(additionalCost);

    }

    public List<Account> loadRequestAccountsForYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow,
            WorkflowStatus statusHigh) {
        String jpql = "select distinct a "
                + "from Account a join AdditionalCost s "
                + "where a._id = s._accountId and s._calenderYear = :year "
                + "    and s._statusId between :statusLow and :statusHigh and s._accountId in :accountIds";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        query.setParameter("year", year);
        query.setParameter("statusLow", statusLow.getId());
        query.setParameter("statusHigh", statusHigh.getId());
        query.setParameter("accountIds", accountIds);
        return query.getResultList();
    }

    public Set<Integer> getRequestCalcYears(Set<Integer> accountIds) {
        String jpql = "select a._calenderYear from AdditionalCost a where a._accountId in :accountIds and a._statusId >= 10";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("accountIds", accountIds);
        @SuppressWarnings("unchecked") HashSet<Integer> result = new HashSet<>(query.getResultList());
        return result;
    }

    public List<AdditionalCost> obtainAdditionalCosts(int accountId, int year,
            WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jpql = "SELECT s FROM AdditionalCost s "
                + "WHERE s._accountId = :accountId "
                + "  and s._calenderYear = :year "
                + "  and s._statusId between :statusLow "
                + "  and :statusHigh "
                + "ORDER BY s._id DESC";
        TypedQuery<AdditionalCost> query = getEntityManager().createQuery(jpql, AdditionalCost.class);
        query.setParameter("accountId", accountId);
        query.setParameter("year", year);
        query.setParameter("statusLow", statusLow.getId());
        query.setParameter("statusHigh", statusHigh.getId());
        return query.getResultList();
    }

    public List<AdditionalCost> getAdditionalCostsForInek(int dataYear) {
        String jpql = "select ac from AdditionalCost ac where ac._statusId in (3, 10) and ac._calenderYear = :dataYear";
        TypedQuery<AdditionalCost> query = getEntityManager().createQuery(jpql, AdditionalCost.class);
        query.setParameter("dataYear", dataYear);
        List<AdditionalCost> result = query.getResultList();
        return result;
    }
}
