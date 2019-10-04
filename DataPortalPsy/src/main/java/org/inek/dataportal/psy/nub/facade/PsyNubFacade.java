package org.inek.dataportal.psy.nub.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.psy.nub.entities.PsyNubRequest;
import org.inek.dataportal.psy.nub.entities.PsyNubRequestHistory;
import org.inek.dataportal.psy.nub.helper.PsyNubRequestHistoryMergeHelper;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class PsyNubFacade extends AbstractDataAccess {

    private static final Logger LOGGER = Logger.getLogger(PsyNubFacade.class.toString());

    @Transactional
    public PsyNubRequest save(PsyNubRequest request) {
        if (request.getId() == -1) {
            persist(request);
            return request;
        }
        return merge(request);
    }

    public PsyNubRequest findNubById(int nubId) {
        String jpql = "select nub from PsyNubRequest nub where nub._id = :id";
        TypedQuery<PsyNubRequest> query = getEntityManager().createQuery(jpql, PsyNubRequest.class);
        query.setParameter("id", nubId);
        return query.getSingleResult();
    }

    public List<PsyNubRequest> findAllByAccountIdAndNoIk(int accountId, WorkflowStatus status) {
        List<WorkflowStatus> statuse = new ArrayList<>();
        statuse.add(status);
        return findAllByAccountIdAndNoIk(accountId, statuse);
    }

    public List<PsyNubRequest> findAllByAccountIdAndNoIk(int accountId, List<WorkflowStatus> status) {
        String jpql = "select nub from PsyNubRequest nub where nub._ik = 0 and nub._createdByAccountId = :id and nub._status in :state";
        TypedQuery<PsyNubRequest> query = getEntityManager().createQuery(jpql, PsyNubRequest.class);
        query.setParameter("id", accountId);
        query.setParameter("state", status);
        return query.getResultList();
    }

    public List<PsyNubRequest> findAllByIkAndStatus(Integer ik, WorkflowStatus status) {
        List<WorkflowStatus> statuse = new ArrayList<>();
        statuse.add(status);
        return findAllByIkAndStatus(ik, statuse);
    }

    public List<PsyNubRequest> findAllByIkAndStatus(Integer ik, List<WorkflowStatus> status) {
        String jpql = "select nub from PsyNubRequest nub where nub._ik = :ik and nub._status in :status";
        TypedQuery<PsyNubRequest> query = getEntityManager().createQuery(jpql, PsyNubRequest.class);
        query.setParameter("status", status);
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    public List<PsyNubRequest> findAllByAccountIdAndIkAndStatus(int accountId, Integer ik, WorkflowStatus status) {
        List<WorkflowStatus> statuse = new ArrayList<>();
        statuse.add(status);
        return findAllByAccountIdAndIkAndStatus(accountId, ik, statuse);
    }

    public List<PsyNubRequest> findAllByAccountIdAndIkAndStatus(int accountId, Integer ik, List<WorkflowStatus> status) {
        String jpql = "select nub from PsyNubRequest nub where nub._ik = :ik and nub._createdByAccountId = :id and nub._status in :status";
        TypedQuery<PsyNubRequest> query = getEntityManager().createQuery(jpql, PsyNubRequest.class);
        query.setParameter("status", status);
        query.setParameter("ik", ik);
        query.setParameter("id", accountId);
        return query.getResultList();
    }

    public void delete(PsyNubRequest request) {
        remove(request);
    }

    public void check4NubOrphantCorrections() {
        LOGGER.log(Level.INFO, "Scanning for PSY-NUBs with correctionRequested older then 5 days");
        Date date = DateUtils.getDateWithDayOffset(-5);
        String jpql = "SELECT p FROM PsyNubRequest p WHERE p._dateCorrectionRequested < :date and p._status = :status ";
        TypedQuery<PsyNubRequest> query = getEntityManager().createQuery(jpql, PsyNubRequest.class);
        query.setParameter("date", date);
        query.setParameter("status", WorkflowStatus.CorrectionRequested.getId());
        for (PsyNubRequest request : query.getResultList()) {
            resetPsyNubRequestToHistoryEntry(request);
        }
    }

    private void resetPsyNubRequestToHistoryEntry(PsyNubRequest request) {
        LOGGER.log(Level.INFO, "reset nub [" + request.getExternalStatus() + "]");
        String jpql = "SELECT p FROM PsyNubRequestHistory p WHERE p._psyNubRequestId = :nubId";
        TypedQuery<PsyNubRequestHistory> query = getEntityManager().createQuery(jpql, PsyNubRequestHistory.class);
        query.setParameter("nubId", request.getId());
        PsyNubRequestHistory nubRequestHistory = query.getResultList().get(0);
        PsyNubRequestHistoryMergeHelper.copyHistoryToRequest(request, nubRequestHistory);
        save(request);
        remove(nubRequestHistory);
    }

    public Map<Integer, Integer> countOpenPerIk() {
        int targetYear = 1 + Calendar.getInstance().get(Calendar.YEAR);
        String jpql = "SELECT p._createdByAccountId, COUNT(p) "
                + "FROM PsyNubRequest p JOIN Account a "
                + "WHERE p._createdByAccountId = a._id and a._customerTypeId = 5 "
                + "    and p._status < 10 and p._targetYear = :targetYear GROUP BY p._createdByAccountId";
        Query query = getEntityManager().createQuery(jpql);
        query.setParameter("targetYear", targetYear);
        List data = query.getResultList();
        Map<Integer, Integer> result = new HashMap<>();
        for (Object x : data) {
            Object[] info = (Object[]) x;
            int accountId = (int) info[0];
            int count = (int) (long) info[1];
            result.put(accountId, count);
        }
        return result;
    }

}
