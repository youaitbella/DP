/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.NubProposal;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.helper.structures.Triple;

/**
 *
 * @author muellermi
 */
@Stateless
public class NubProposalFacade extends AbstractFacade<NubProposal> {

    public NubProposalFacade() {
        super(NubProposal.class);
    }

    public List<NubProposal> findAll(int accountId, DataSet dataSet) {
        if (dataSet == DataSet.ALLSEALED) {
            // todo: is this user allowed to get the whole list?
            return Collections.EMPTY_LIST;
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<NubProposal> cq = cb.createQuery(NubProposal.class);
        Root request = cq.from(NubProposal.class);
        Predicate status;
        Order order;
        if (dataSet == DataSet.OPEN) {
            status = cb.lessThan(request.get("_status"), 10);
            order = cb.asc(request.get("_nubId"));
        } else {
            status = cb.greaterThanOrEqualTo(request.get("_status"), 10);
            order = cb.desc(request.get("_nubId"));
        }
        if (dataSet == DataSet.ALLSEALED) {
            cq.select(request).where(status).orderBy(order);
        } else {
            Predicate isAccount = cb.equal(request.get("_accountId"), accountId);
            cq.select(request).where(cb.and(isAccount, status)).orderBy(order);
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<NubProposal> findAll(int accountId) {
        String sql = "SELECT p FROM NubProposal p WHERE p._accountId = :accountId ORDER BY p._nubId DESC";
        Query query = getEntityManager().createQuery(sql, NubProposal.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public int count(int accountId, boolean isComplete) {
        String sql = "SELECT COUNT(p) FROM NubProposal p WHERE p._accountId = :accountId and p._isComplete = :isComplete";
        Query query = getEntityManager().createQuery(sql, NubProposal.class);
        query.setParameter("accountId", accountId);
        query.setParameter("isComplete", isComplete);
        return ((Long)query.getSingleResult()).intValue();
    }

    public NubProposal saveNubProposal(NubProposal nubProposal) {
        if (nubProposal.getNubId() == null) {
            persist(nubProposal);
            return nubProposal;
        }
        return merge(nubProposal);
    }

    /**
     * A list of NUB infos for display usage, e.g. lists
     * @param accountId
     * @param dataSet
     * @return 
     */
    public List<Triple> getNubProposalInfos(int accountId, DataSet dataSet) {
        List<NubProposal> proposals = findAll(accountId, dataSet);
        List<Triple> proposalInfos = new ArrayList<>(); 
        for (NubProposal proposal : proposals){
            String displayName = proposal.getDisplayName().trim().length() == 0 
                    ? proposal.getName() 
                    : proposal.getDisplayName();
            proposalInfos.add(new Triple(proposal.getNubId(), displayName, proposal.getStatus()));
        }
        return proposalInfos;
    }
   
    public List<Triple> findForAccountAndIk(int accountId, int ik, int minStatus, int maxStatus) {
        String sql = "SELECT p FROM NubProposal p "
                + "WHERE p._accountId = :accountId and p._ik = :ik and p._status >= :minStatus and p._status <= :maxStatus "
                + "ORDER BY p._nubId DESC";
        Query query = getEntityManager().createQuery(sql, NubProposal.class);
        query.setParameter("accountId", accountId);
        query.setParameter("ik", ik);
        query.setParameter("minStatus", minStatus);
        query.setParameter("maxStatus", maxStatus);
        List<NubProposal> proposals = query.getResultList();
        List<Triple> proposalInfos = new ArrayList<>(); 
        for (NubProposal proposal : proposals){
            String displayName = proposal.getDisplayName().trim().length() == 0 
                    ? proposal.getName() 
                    : proposal.getDisplayName();
            proposalInfos.add(new Triple(proposal.getNubId(), displayName, proposal.getStatus()));
        }
        return proposalInfos;
    }

    
}
