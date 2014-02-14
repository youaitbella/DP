package org.inek.dataportal.facades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.criteria.*;
import org.inek.dataportal.entities.PeppProposal;
import org.inek.dataportal.entities.PeppProposalComment;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.utils.KeyValue;
import org.inek.dataportal.helper.structures.Triple;

/**
 *
 * @author muellermi
 */
@Stateless
public class PeppProposalFacade extends AbstractFacade<PeppProposal> {

    @EJB private PeppProposalCommentFacade _commentFacade;
    @EJB private AccountFacade _accountFacade;

    public PeppProposalFacade() {
        super(PeppProposal.class);
    }

    public List<PeppProposal> findAll(int accountId, DataSet dataSet) {
        if (dataSet == DataSet.ALLSEALED) {
            // todo: is this user allowed to get the whole list?
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PeppProposal> cq = cb.createQuery(PeppProposal.class);
        Root request = cq.from(PeppProposal.class);
        Predicate sealed;
        Order order;
        if (dataSet == DataSet.OPEN) {
            sealed = cb.le(request.get("_status"), 0);
            order = cb.asc(request.get("_peppProposalId"));
        } else {
            sealed = cb.greaterThan(request.get("_status"), 0);
            order = cb.desc(request.get("_peppProposalId"));
        }
        if (dataSet == DataSet.ALLSEALED) {
            cq.select(request).where(sealed).orderBy(order);
        } else {
            Predicate isAccount = cb.equal(request.get("_accountId"), accountId);
            cq.select(request).where(cb.and(isAccount, sealed)).orderBy(order);
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    public PeppProposal savePeppProposal(PeppProposal peppProposal) {
        logData(peppProposal);
        if (peppProposal.getPeppProposalId() == null) {
            persist(peppProposal);
            return peppProposal;
        }
        return merge(peppProposal);
    }

    private void logData(Object data) {
        List<KeyValue> doc = DocumentationUtil.getDocumentation(data);
        // ensure these messages to be logged
        Level oldLevel = _logger.getLevel();
        _logger.setLevel(Level.INFO);
        for (KeyValue kv : doc) {
            _logger.log(Level.INFO, "{0} ^ Key: {1} ^ Length: {2} ^ Value: {3}", new Object[]{data.getClass().getSimpleName(), kv.getKey(), kv.getValue().toString().length(), kv.getValue()});
        }
        _logger.setLevel(oldLevel);
    }

    public List<Triple> getPeppProposalInfos(int accountId, DataSet dataSet) {
        List<PeppProposal> peppProposals = findAll(accountId, dataSet);
        List<Triple> peppProposalInfos = new ArrayList<>();
        for (PeppProposal peppProposal : peppProposals) {
            Triple ppInfo = new Triple(peppProposal.getPeppProposalId(), peppProposal.getName(), peppProposal.getDateSealed());
            peppProposalInfos.add(ppInfo);
        }
        return peppProposalInfos;
    }

    public PeppProposal getPeppProposal(int id) {
        clearCache(PeppProposal.class);
        PeppProposal proposal = find(id);
        for (PeppProposalComment comment : proposal.getComments()) {
            comment.setInitials(getInitials(comment.getAccountId()));
        }
        return proposal;
    }

    private Map<Integer, String> _accountInitials = new HashMap<>();

    private String getInitials(Integer accountId) {
        if (!_accountInitials.containsKey(accountId)) {
            _accountInitials.put(accountId, _accountFacade.find(accountId).getInitials());
        }
        return _accountInitials.get(accountId);
    }

    @Schedule(hour = "*/6", minute = "*", info = "every 6 hours")
    private void invalidateCache() {
        _accountInitials = new HashMap<>();
        _commentFacade.clearCache();
    }

    public Boolean addComment(int accountId, int proposalId, String comment) {
        PeppProposalComment ppComment = new PeppProposalComment();
        ppComment.setAccountId(accountId);
        ppComment.setPeppProposalId(proposalId);
        ppComment.setComment(comment);
        _commentFacade.persist(ppComment);
        //_commentFacade.clearCache(PeppProposal.class);
        return true;
    }

}
