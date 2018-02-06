package org.inek.dataportal.facades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.drg.DrgProposal;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.utils.KeyValueLevel;

/**
 *
 * @author muellermi
 */
@Stateless
public class DrgProposalFacade extends AbstractFacade<DrgProposal> {

    public DrgProposalFacade() {
        super(DrgProposal.class);
    }

    
    public List<DrgProposal> findAll(int accountId, DataSet dataSet) {
        return findAll(accountId, -1, dataSet);
    }
    public List<DrgProposal> findAll(int accountId, int year, DataSet dataSet) {
        if (dataSet == DataSet.None) {
            return new ArrayList<>();
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DrgProposal> cq = cb.createQuery(DrgProposal.class);
        Root<DrgProposal> request = cq.from(DrgProposal.class);
        Predicate sealed;
        Order order;
        if (dataSet == DataSet.AllOpen) {
            sealed = cb.lessThan(request.get("_status"), WorkflowStatus.Provided.getId());
            order = cb.asc(request.get("_id"));
        } else if (dataSet == DataSet.ApprovalRequested) {
            sealed = cb.equal(request.get("_status"), WorkflowStatus.ApprovalRequested.getId());
            order = cb.asc(request.get("_id"));
        } else {
            sealed = cb.greaterThanOrEqualTo(request.get("_status"), WorkflowStatus.Provided.getId());
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

    public DrgProposal saveDrgProposal(DrgProposal drgProposal) {
        // logData(drgProposal); // un-comment if loggin is needed again
        if (drgProposal.getId() == null) {
            persist(drgProposal);
            return drgProposal;
        }
        return merge(drgProposal);
    }

    private void logData(Object data) {
        List<KeyValueLevel> doc = DocumentationUtil.getDocumentation(data);
        // ensure these messages to be logged
        Level oldLevel = LOGGER.getLevel();
        LOGGER.setLevel(Level.INFO);
        for (KeyValueLevel kv : doc) {
            LOGGER.log(Level.INFO, "{0} ^ Key: {1} ^ Length: {2} ^ Value: {3}", 
                    new Object[]{data.getClass().getSimpleName(), kv.getKey(), kv.getValue().toString().length(), kv.getValue()});
        }
        LOGGER.setLevel(oldLevel);
    }

    public List<ProposalInfo> getDrgProposalInfos(int accountId, DataSet dataSet) {
        return getDrgProposalInfos(accountId, -1, dataSet);
    }
    
    public List<ProposalInfo> getDrgProposalInfos(int accountId, int year, DataSet dataSet) {
        List<DrgProposal> drgProposals = findAll(accountId, year, dataSet);
        List<ProposalInfo> drgProposalInfos = new ArrayList<>();
        for (DrgProposal drgProposal : drgProposals) {
            ProposalInfo ppInfo = new ProposalInfo(drgProposal.getId(), drgProposal.getName(), 
                    drgProposal.getTargetYear(), drgProposal.getStatus());
            drgProposalInfos.add(ppInfo);
        }
        return drgProposalInfos;
    }

    /**
     * returns a list with the distinct account ids of all provided proposals
     * @return 
     */
    public List<Integer> getProposalAccounts(){
        int base = 16;  // todo: determine base
        int fromId = base * 10000;
        int toId = fromId + 9999;
        String statement = "select distinct prAccountId from DrgProposal where prId between ?1 and ?2 and prStatus = 10";
        Query query = getEntityManager().createNativeQuery(statement).setParameter(1, fromId).setParameter(2, toId);
        @SuppressWarnings("unchecked") List<Integer> result = query.getResultList();
        return result;
    }

    public Set<Integer> checkAccountsForProposalOfYear(Set<Integer> accountIds, int year, WorkflowStatus statusLow, WorkflowStatus statusHigh) {
        String jpql = "SELECT DISTINCT p._accountId FROM DrgProposal p "
                + "WHERE p._accountId in :accountIds and (p._targetYear = :year or -1 = :year) "
                + "    and p._status between :statusLow and :statusHigh";
        //TypedQuery<DrgProposal> query = getEntityManager().createQuery(jpql, DrgProposal.class);
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        query.setParameter("accountIds", accountIds);
        query.setParameter("year", year);
        query.setParameter("statusLow", statusLow.getId());
        query.setParameter("statusHigh", statusHigh.getId());
        return new HashSet<>(query.getResultList());
    }

    public List<Integer> getProposalYears(Set<Integer> accountIds) {
        String jpql = "SELECT DISTINCT p._targetYear FROM DrgProposal p "
                + "WHERE p._accountId in :accountIds and p._status >= 10 ORDER BY p._targetYear DESC";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        query.setParameter("accountIds", accountIds);
        return query.getResultList();
    }
    
    public List<DrgProposal> find(List<Integer> requestIds) {
        String jpql = "SELECT p FROM DrgProposal p WHERE p._id in :requestIds  ";
        TypedQuery<DrgProposal> query = getEntityManager().createQuery(jpql, DrgProposal.class);
        query.setParameter("requestIds", requestIds);
        return query.getResultList();
    }
    
    
}
