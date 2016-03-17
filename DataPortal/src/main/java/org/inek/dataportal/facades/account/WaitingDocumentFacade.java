package org.inek.dataportal.facades.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.inek.dataportal.entities.account.WaitingDocument;
import org.inek.dataportal.facades.AbstractFacade;
import org.inek.dataportal.helper.structures.DocInfo;
import org.inek.dataportal.utils.DateUtils;

@Stateless
public class WaitingDocumentFacade extends AbstractFacade<WaitingDocument> {

    public WaitingDocumentFacade() {
        super(WaitingDocument.class);
    }

    public List<DocInfo> getDocInfos(int accountId) {
        String sql = "SELECT d._id, d._name, dd._name, d._timestamp, null, false, d._accountId, d._agentAccountId FROM WaitingDocument d join DocumentDomain dd WHERE d._domainId = dd._id and  d._accountId = :accountId ORDER BY d._id DESC";
        // does not work properly :(
//        TypedQuery<DocInfo> query = getEntityManager().createQuery(sql, DocInfo.class);
//        query.setParameter("accountId", accountId);
//        return query.getResultList();
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("accountId", accountId);
        List<Object[]> objects = query.getResultList();
        List<DocInfo> docInfos = new ArrayList<>();
        for (Object[] obj : objects){
            docInfos.add(new DocInfo((int)obj[0], (String)obj[1], (String)obj[2], (Date)obj[3], (Date)obj[4], (boolean)obj[5], (int)obj[6], (int)obj[7]));
        }
        return docInfos;
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
