package org.inek.dataportal.facades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.DrgProposal;
import org.inek.dataportal.entities.DrgProposalComment;
//import org.inek.dataportal.entities.PeppProposal;
//import org.inek.dataportal.entities.PeppProposalComment;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.helper.structures.Triple;
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
        if (dataSet == DataSet.All) {
            // todo: is this user allowed to get the whole list?
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DrgProposal> cq = cb.createQuery(DrgProposal.class);
        Root request = cq.from(DrgProposal.class);
        Predicate sealed;
        Order order;
        if (dataSet == DataSet.OpenOnly) {
            sealed = cb.le(request.get("_status"), 0);
            order = cb.asc(request.get("_drgProposalId"));
        } else {
            sealed = cb.greaterThan(request.get("_status"), 0);
            order = cb.desc(request.get("_drgProposalId"));
        }
        if (dataSet == DataSet.All) {
            cq.select(request).where(sealed).orderBy(order);
        } else {
            Predicate isAccount = cb.equal(request.get("_accountId"), accountId);
            cq.select(request).where(cb.and(isAccount, sealed)).orderBy(order);
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    public DrgProposal saveDrgProposal(DrgProposal drgProposal) {
        logData(drgProposal);
        if (drgProposal.getDrgProposalId() == null) {
            persist(drgProposal);
            return drgProposal;
        }
        return merge(drgProposal);
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

    public List<Triple> getDrgProposalInfos(int accountId, DataSet dataSet) {
        List<DrgProposal> drgProposals = findAll(accountId, dataSet);
        List<Triple> drgProposalInfos = new ArrayList<>();
        for (DrgProposal drgProposal : drgProposals) {
            Triple ppInfo = new Triple(drgProposal.getDrgProposalId(), drgProposal.getName(), drgProposal.getDateSealed());
            drgProposalInfos.add(ppInfo);
        }
        return drgProposalInfos;
    }

    public DrgProposal getDrgProposal(int id) {
        clearCache(DrgProposal.class);
        DrgProposal proposal = find(id);
        for (DrgProposalComment comment : proposal.getComments()) {
            comment.setInitials(getInitials(comment.getAccountId()));
        }
        return proposal;
                       
    }

    private Map<Integer, String> _accountInitials = new HashMap<>();

    private String getInitials(Integer accountId) {
//        if (!_accountInitials.containsKey(accountId)) {
//            _accountInitials.put(accountId, _accountFacade.find(accountId).getInitials());
//        }
        return _accountInitials.get(accountId);
    }

//    @Schedule(hour = "*/6", minute = "*", info = "every 6 hours")
//    private void invalidateCache() {
//        _accountInitials = new HashMap<>();
//        _commentFacade.clearCache();
//    }
//
//    public Boolean addComment(int accountId, int proposalId, String comment) {
//        PeppProposalComment ppComment = new PeppProposalComment();
//        ppComment.setAccountId(accountId);
//        ppComment.setPeppProposalId(proposalId);
//        ppComment.setComment(comment);
//        _commentFacade.persist(ppComment);
//        //_commentFacade.clearCache(PeppProposal.class);
//        return true;
//    }

}
