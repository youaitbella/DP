package org.inek.dataportal.facades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.drg.DrgProposal;
import org.inek.dataportal.entities.drg.DrgProposalComment;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.helper.structures.ProposalInfo;
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

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<DrgProposal> findAll(int accountId, DataSet dataSet) {
        if (dataSet == DataSet.None) {return new ArrayList<>();}
        
        if (dataSet == DataSet.All) {
            // todo: is this user allowed to get the whole list?
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DrgProposal> cq = cb.createQuery(DrgProposal.class);
        Root request = cq.from(DrgProposal.class);
        Predicate sealed;
        Order order;
        if (dataSet == DataSet.AllOpen) {
            sealed = cb.lessThan(request.get("_status"), WorkflowStatus.Provided.getValue());
            order = cb.asc(request.get("_drgProposalId"));
        }else if (dataSet == DataSet.ApprovalRequested) {
            sealed = cb.equal(request.get("_status"), WorkflowStatus.ApprovalRequested.getValue());
            order = cb.asc(request.get("_drgProposalId"));
        } else {
            sealed = cb.greaterThanOrEqualTo(request.get("_status"), WorkflowStatus.Provided.getValue());
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

    public List<ProposalInfo> getDrgProposalInfos(int accountId, DataSet dataSet) {
        List<DrgProposal> drgProposals = findAll(accountId, dataSet);
        List<ProposalInfo> drgProposalInfos = new ArrayList<>();
        for (DrgProposal drgProposal : drgProposals) {
            int year = 2000 + Integer.parseInt(("" + drgProposal.getDrgProposalId()).substring(0, 2));  // todo: get year from better place
            ProposalInfo ppInfo = new ProposalInfo(drgProposal.getDrgProposalId(), drgProposal.getName(), year,  drgProposal.getStatus());
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

    
}
