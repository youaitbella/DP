package org.inek.dataportal.psy.nub.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.psy.nub.entities.PsyNubRequest;
import org.inek.dataportal.psy.nub.entities.PsyNubRequestHistory;
import org.inek.dataportal.psy.nub.helper.PsyNubRequestHistoryMergeHelper;
import org.inek.dataportal.psy.nub.helper.PsyNubRequestMergeHelper;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class PsyNubFacade extends AbstractDataAccess {

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

    //@Schedule(hour = "0", info = "once a day")
    private void check4NubOrphantCorrections() {
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
        String jpql = "SELECT p FROM PsyNubRequestHistory p WHERE p._psyNubRequestId = :nubId";
        TypedQuery<PsyNubRequestHistory> query = getEntityManager().createQuery(jpql, PsyNubRequestHistory.class);
        query.setParameter("nubId", request.getId());
        PsyNubRequestHistory nubRequestHistory = query.getResultList().get(0);
        PsyNubRequestHistoryMergeHelper.copyHistoryToRequest(request, nubRequestHistory);
        save(request);
        remove(nubRequestHistory);
    }
}
