package org.inek.dataportal.facades.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.WaitingDocument;
import org.inek.dataportal.facades.AbstractFacade;
import org.inek.dataportal.helper.structures.DocInfo;
import org.inek.dataportal.utils.DateUtils;

@Stateless
public class WaitingDocumentFacade extends AbstractFacade<WaitingDocument> {

    public WaitingDocumentFacade() {
        super(WaitingDocument.class);
    }

    public List<DocInfo> getDocInfos(int agentAccountId) {
//        String sql = "SELECT d._id, d._name, dd._name, d._timestamp, null, false, d._accountId, d._agentAccountId, a._ik "
//                + "FROM WaitingDocument d "
//                + "join DocumentDomain dd "
//                + "join Account a "
//                + "WHERE d._accountId = a._id and d._domainId = dd._id and d._agentAccountId = :accountId ORDER BY d._id DESC";
        String sql = "SELECT d._id, d._name, dd._name, d._timestamp, null, false, d._accountId, d._agentAccountId, a._ik, a._company, a._town "
                + "FROM WaitingDocument d "
                + "join DocumentDomain dd "
                + "join Account a "
                + "WHERE d._domainId = dd._id and d._accountId = a._id and d._agentAccountId = :accountId ORDER BY d._id DESC";
        // does not work properly :(
//        TypedQuery<DocInfo> query = getEntityManager().createQuery(sql, DocInfo.class);
//        query.setParameter("accountId", accountId);
//        return query.getResultList();
        Query query = getEntityManager().createQuery(sql);
        dumpSql(query);
        query.setParameter("accountId", agentAccountId);
        List<Object[]> objects = query.getResultList();
        List<DocInfo> docInfos = new ArrayList<>();
        for (Object[] obj : objects) {
            docInfos.add(new DocInfo((int) obj[0], (String) obj[1], (String) obj[2], (Date) obj[3], (Date) obj[4], (boolean) obj[5], (int) obj[6], (int) obj[7], obj[8] + " " + obj[9] + " " + obj[10]));
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
