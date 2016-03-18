package org.inek.dataportal.facades.account;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.inek.dataportal.entities.account.AccountDocument;
import org.inek.dataportal.facades.AbstractFacade;
import org.inek.dataportal.helper.structures.DocInfo;
import org.inek.dataportal.utils.DateUtils;

@Stateless
public class AccountDocumentFacade extends AbstractFacade<AccountDocument> {

    public AccountDocumentFacade() {
        super(AccountDocument.class);
    }

    public List<DocInfo> getDocInfos(int accountId) {
        String sql = "SELECT d._id, d._name, dd._name, d._timestamp, d._validUntil, d._read, d._accountId, d._uploadAccountId FROM AccountDocument d join DocumentDomain dd WHERE d._domainId = dd._id and  d._accountId = :accountId ORDER BY d._id DESC";
        // does not work properly :(
//        TypedQuery<DocInfo> query = getEntityManager().createQuery(sql, DocInfo.class);
//        query.setParameter("accountId", accountId);
//        return query.getResultList();
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("accountId", accountId);
        List<Object[]> objects = query.getResultList();
        List<DocInfo> docInfos = new ArrayList<>();
        for (Object[] obj : objects){
            docInfos.add(new DocInfo((int)obj[0], (String)obj[1], (String)obj[2], (Date)obj[3], (Date)obj[4], (boolean)obj[5], (int)obj[6], (int)obj[7], ""));
        }
        return docInfos;
    }

    public List<String> getNewDocs(int accountId) {
        String sql = "SELECT d._name FROM AccountDocument d WHERE d._accountId = :accountId and d._timestamp > :referenceDate ORDER BY d._id DESC";
        Query query = getEntityManager().createQuery(sql, String.class);
        query.setParameter("accountId", accountId);
        query.setParameter("referenceDate", DateUtils.getDateWithDayOffset(-60));
        return query.getResultList();
    }

    public List<AccountDocument> findAll(int accountId) {
        String sql = "SELECT d FROM AccountDocument d WHERE d._accountId = :accountId ORDER BY d._id DESC";
        Query query = getEntityManager().createQuery(sql, AccountDocument.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public long count(int accountId) {
        String sql = "SELECT count(d._id) FROM AccountDocument d WHERE d._accountId = :accountId";
        Query query = getEntityManager().createQuery(sql, Long.class);
        query.setParameter("accountId", accountId);
        return (long) query.getSingleResult();
    }
    
    public boolean isDocRead(int docId) {
        AccountDocument doc = find(docId);
        if (doc.isRead()) {
            return true;
        }
        return false;
    }

    @Schedule(hour = "2", minute = "15", info = "once a day")
    // for test: @Schedule(hour = "*", minute = "*/1", info = "once a minute")
    private void sweepOldDocuments() {
        String sql = "SELECT p FROM AccountDocument p WHERE p._validUntil < :date";
        Query query = getEntityManager().createQuery(sql, AccountDocument.class);
        query.setParameter("date", Calendar.getInstance().getTime());
        List<AccountDocument> docs = query.getResultList();
        for (AccountDocument doc : docs) {
            _logger.log(Level.WARNING, "Delete old document {0} of account {1}", new Object[]{doc.getName(), doc.getAccountId()});
            remove(doc);
        }
    }

    public AccountDocument save(AccountDocument accountDocument) {
        persist(accountDocument);
        clearCache();
        return findFresh(accountDocument.getId());
    }

}
