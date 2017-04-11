package org.inek.dataportal.facades.account;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Asynchronous;
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
            String receipientInfo;
            int accountId = 0;
            if (info.getAccounts().isEmpty()) {
                receipientInfo = "Kein Empfänger gefunden";
            } else {
                Account account = info.getAccounts().get(0);
                accountId = account.getId();
                int ikDoc = info.getIk();
                int ikAccount = account.getIK() == null ? -1 : account.getIK();
                if (ikDoc < 0 || ikDoc == ikAccount) {
                    receipientInfo = ikAccount + " " + account.getCompany() + " " + account.getTown();
                } else {
                    receipientInfo = ikDoc + " " + _sessionController.getIkName(ikDoc);
                }
            }
            docInfos.add(new DocInfo((int) info.getId(), info.getName(), info.getDomain().getName(), info.getTimestamp(), null, false, accountId, info.getAgentAccountId(), "", receipientInfo));
        }
        return docInfos;
    }

    public List<Account> getAgents() {
        String sql = "SELECT distinct a FROM WaitingDocument d join Account a WHERE d._agentAccountId = a._id";
        TypedQuery<Account> query = getEntityManager().createQuery(sql, Account.class);
        return query.getResultList();
    }

    @Schedule(hour = "2", minute = "30", info = "once a day")
    // for test: @Schedule(hour = "*", minute = "*/1", info = "once a minute")
    private void startDeleteOldDocuments() {
        _logger.log(Level.INFO, "Start deleting old documents");
        deleteOldDocuments();
        _logger.log(Level.INFO, "Finished deleting old documents");
    }
    
    @Asynchronous
    private void deleteOldDocuments() {
        String sql = "SELECT p FROM WaitingDocument p WHERE p._timestamp < :referenceDate";
        TypedQuery<WaitingDocument> query = getEntityManager().createQuery(sql, WaitingDocument.class);
        query.setParameter("referenceDate", DateUtils.getDateWithDayOffset(-60));
        List<WaitingDocument> docs = query.getResultList();
        for (WaitingDocument doc : docs) {
            _logger.log(Level.INFO, "Delete old waiting document {0} of agent {1}", new Object[]{doc.getName(), doc.getAgentAccountId()});
            remove(doc);
        }
    }

    public void save(WaitingDocument waitingDocument) {
        clearCache();  // paranoid call, because sometimes documents have been stored twice, although the log contains only one creation
        persist(waitingDocument);
    }

}
