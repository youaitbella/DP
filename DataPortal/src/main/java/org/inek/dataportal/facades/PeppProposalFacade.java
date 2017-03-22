package org.inek.dataportal.facades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.pepp.PeppProposal;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.utils.KeyValueLevel;

/**
 *
 * @author muellermi
 */
@Stateless
public class PeppProposalFacade extends AbstractFacade<PeppProposal> {

    @Inject private PeppProposalCommentFacade _commentFacade;
    @Inject private AccountFacade _accountFacade;

    public PeppProposalFacade() {
        super(PeppProposal.class);
    }

    
    public List<PeppProposal> findAll(int accountId, DataSet dataSet) {
        return findAll(accountId, -1, dataSet);
    }
    
    public List<PeppProposal> findAll(int accountId, int year, DataSet dataSet) {
        if (dataSet == DataSet.None) {
            return new ArrayList<>();
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PeppProposal> cq = cb.createQuery(PeppProposal.class);
        Root<PeppProposal> request = cq.from(PeppProposal.class);
        Predicate sealed;
        Order order;
        if (dataSet == DataSet.AllOpen) {
            sealed = cb.le(request.get("_status"), 0);
            order = cb.asc(request.get("_id"));
        } else {
            sealed = cb.greaterThan(request.get("_status"), 0);
            order = cb.desc(request.get("_id"));
        }
        Predicate checkYear;
        if (year > 0){
          checkYear = cb.equal(request.get("_targetYear"), year);  
        }else{
            checkYear = cb.notEqual(request.get("_targetYear"), year);  
        }
        if (dataSet == DataSet.All) {
            cq.select(request).where(cb.and(sealed, checkYear)).orderBy(order);
        } else {
            Predicate isAccount = cb.equal(request.get("_accountId"), accountId);
            cq.select(request).where(cb.and(isAccount, sealed, checkYear)).orderBy(order);
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    public PeppProposal savePeppProposal(PeppProposal peppProposal) {
        logData(peppProposal);
        if (peppProposal.getId() == null) {
            persist(peppProposal);
            return peppProposal;
        }
        return merge(peppProposal);
    }

    private void logData(Object data) {
        List<KeyValueLevel> doc = DocumentationUtil.getDocumentation(data);
        // ensure these messages to be logged
        Level oldLevel = _logger.getLevel();
        _logger.setLevel(Level.INFO);
        for (KeyValueLevel kv : doc) {
            _logger.log(Level.INFO, "{0} ^ Key: {1} ^ Length: {2} ^ Value: {3}", new Object[]{data.getClass().getSimpleName(), kv.getKey(), kv.getValue().toString().length(), kv.getValue()});
        }
        _logger.setLevel(oldLevel);
    }

    public List<ProposalInfo> getPeppProposalInfos(int accountId, DataSet dataSet) {
        return getPeppProposalInfos(accountId, -1, dataSet);
    }
    public List<ProposalInfo> getPeppProposalInfos(int accountId, int year, DataSet dataSet) {
        List<PeppProposal> peppProposals = findAll(accountId, year, dataSet);
        List<ProposalInfo> peppProposalInfos = new ArrayList<>();
        for (PeppProposal peppProposal : peppProposals) {
            ProposalInfo ppInfo = new ProposalInfo(peppProposal.getId(), peppProposal.getName(), peppProposal.getTargetYear(), peppProposal.getStatus());
            peppProposalInfos.add(ppInfo);
        }
        return peppProposalInfos;
    }


    /**
     * returns a list with the distinct account ids of all provided proposals
     * @return 
     */
    public List<Integer> getProposalAccounts(){
        int base = 16;  // todo: determine base
        int fromId = base * 100000;
        int toId = fromId + 99999;
        String statement = "select distinct ppAccountId from PeppProposal where ppId between ?1 and ?2 and ppStatus = 10";
        Query query = getEntityManager().createNativeQuery(statement).setParameter(1, fromId).setParameter(2, toId);
        @SuppressWarnings("unchecked") List<Integer> result = query.getResultList();
        return result;
    }

    public Set<Integer> checkAccountsForProposalOfYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jpql = "SELECT DISTINCT p._accountId FROM PeppProposal p WHERE p._accountId in :accountIds and (p._targetYear = :year or -1 = :year) and p._status between :statusLow and :statusHigh";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        query.setParameter("accountIds", accountIds);
        query.setParameter("year", year);
        query.setParameter("statusLow", statusLow.getId());
        query.setParameter("statusHigh", statusHigh.getId());
        return new HashSet<>(query.getResultList());
    }

    public List<Integer> getProposalYears(Set<Integer> accountIds) {
        String jpql = "SELECT DISTINCT p._targetYear FROM PeppProposal p WHERE p._accountId in :accountIds and p._status >= 10 ORDER BY p._targetYear DESC";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        query.setParameter("accountIds", accountIds);
        return query.getResultList();
    }
    
    public List<PeppProposal> find(List<Integer> requestIds) {
        String jpql = "SELECT p FROM PeppProposal p WHERE p._id in :requestIds  ";
        TypedQuery<PeppProposal> query = getEntityManager().createQuery(jpql, PeppProposal.class);
        query.setParameter("requestIds", requestIds);
        return query.getResultList();
    }
    
}
