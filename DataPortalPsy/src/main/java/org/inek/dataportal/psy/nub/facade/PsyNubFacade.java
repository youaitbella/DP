package org.inek.dataportal.psy.nub.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.psy.nub.entities.PsyNubProposal;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PsyNubFacade extends AbstractDataAccess {

    @Transactional
    public PsyNubProposal save(PsyNubProposal proposal) {
        if (proposal.getId() == -1) {
            persist(proposal);
            return proposal;
        }
        return merge(proposal);
    }

    public PsyNubProposal findNubById(int nubId) {
        String jpql = "select nub from PsyNubProposal nub where nub._id = :id";
        TypedQuery<PsyNubProposal> query = getEntityManager().createQuery(jpql, PsyNubProposal.class);
        query.setParameter("id", nubId);
        return query.getSingleResult();
    }

    public List<PsyNubProposal> findAllByAccountIdAndNoIk(int accountId, WorkflowStatus status) {
        List<WorkflowStatus> statuse = new ArrayList<>();
        statuse.add(status);
        return findAllByAccountIdAndNoIk(accountId, statuse);
    }

    public List<PsyNubProposal> findAllByAccountIdAndNoIk(int accountId, List<WorkflowStatus> status) {
        String jpql = "select nub from PsyNubProposal nub where nub._ik = 0 and nub._createdByAccountId = :id and nub._status in :state";
        TypedQuery<PsyNubProposal> query = getEntityManager().createQuery(jpql, PsyNubProposal.class);
        query.setParameter("id", accountId);
        query.setParameter("state", status);
        return query.getResultList();
    }

    public List<PsyNubProposal> findAllByIkAndStatus(Integer ik, WorkflowStatus status) {
        List<WorkflowStatus> statuse = new ArrayList<>();
        statuse.add(status);
        return findAllByIkAndStatus(ik, statuse);
    }

    public List<PsyNubProposal> findAllByIkAndStatus(Integer ik, List<WorkflowStatus> status) {
        String jpql = "select nub from PsyNubProposal nub where nub._ik = :ik and nub._status in :status";
        TypedQuery<PsyNubProposal> query = getEntityManager().createQuery(jpql, PsyNubProposal.class);
        query.setParameter("status", status);
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    public List<PsyNubProposal> findAllByAccountIdAndIkAndStatus(int accountId, Integer ik, WorkflowStatus status) {
        List<WorkflowStatus> statuse = new ArrayList<>();
        statuse.add(status);
        return findAllByAccountIdAndIkAndStatus(accountId, ik, statuse);
    }

    public List<PsyNubProposal> findAllByAccountIdAndIkAndStatus(int accountId, Integer ik, List<WorkflowStatus> status) {
        String jpql = "select nub from PsyNubProposal nub where nub._ik = :ik and nub._createdByAccountId = :id and nub._status in :status";
        TypedQuery<PsyNubProposal> query = getEntityManager().createQuery(jpql, PsyNubProposal.class);
        query.setParameter("status", status);
        query.setParameter("ik", ik);
        query.setParameter("id", accountId);
        return query.getResultList();
    }

    public void delete(PsyNubProposal proposal) {
        remove(proposal);
    }
}
