package org.inek.dataportal.facades.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.WaitingDocument;
import org.inek.dataportal.entities.account.WaitingDocumentInfo;
import org.inek.dataportal.facades.AbstractFacade;
import org.inek.dataportal.helper.structures.DocInfo;
import org.inek.dataportal.utils.DateUtils;

@Stateless
public class WaitingDocumentFacade extends AbstractFacade<WaitingDocument> {

    @Inject SessionController _sessionController;

    public WaitingDocumentFacade() {
        super(WaitingDocument.class);
    }

    public List<DocInfo> getDocInfos(int agentAccountId) {
        String sql = "Select d from WaitingDocumentInfo d where d._agentAccountId = :accountId ORDER BY d._id DESC";
        TypedQuery<WaitingDocumentInfo> query = getEntityManager().createQuery(sql, WaitingDocumentInfo.class);
        query.setParameter("accountId", agentAccountId);
        List<WaitingDocumentInfo> resultList = query.getResultList();
        
        List<DocInfo> docInfos = new ArrayList<>();
        for (WaitingDocumentInfo info : resultList) {
            Account account = info.getAccounts().get(0);
            int ikDoc = info.getIk();
            int ikAccount = account.getIK();
            String receipientInfo;
            if (ikDoc < 0 || ikDoc == ikAccount) {
                receipientInfo = ikAccount + " " + account.getCompany() + " " + account.getTown();
            } else {
                receipientInfo = ikDoc + " " + _sessionController.getIkName(ikDoc);
            }
            docInfos.add(new DocInfo((int) info.getId(), info.getName(), info.getDomain().getName(), info.getTimestamp(), null, false, account.getId(), info.getAgentAccountId(), receipientInfo));
        }
        return docInfos;
    }

    public List<Account> getAgents() {
        String sql = "SELECT distinct a FROM WaitingDocument d join Account a WHERE d._agentAccountId = a._id";
        TypedQuery<Account> query = getEntityManager().createQuery(sql, Account.class);
        return query.getResultList();
    }

    @Schedule(hour = "2", minute = "15", info = "once a day")
    // for test: @Schedule(hour = "*", minute = "*/1", info = "once a minute")
    private void sweepOldDocuments() {
        String sql = "SELECT p FROM WaitingDocument p WHERE p._timestamp < :referenceDate";
        Query query = getEntityManager().createQuery(sql, WaitingDocument.class);
        query.setParameter("referenceDate", DateUtils.getDateWithDayOffset(-60));
        List<WaitingDocument> docs = query.getResultList();
        for (WaitingDocument doc : docs) {
            _logger.log(Level.WARNING, "Delete old waiting document {0} of agent {1}", new Object[]{doc.getName(), doc.getAgentAccountId()});
            remove(doc);
        }
    }

    public WaitingDocument save(WaitingDocument waitingDocument) {
        persist(waitingDocument);
        clearCache();
        return findFresh(waitingDocument.getId());
    }

}
