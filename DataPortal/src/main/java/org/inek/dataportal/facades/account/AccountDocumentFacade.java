package org.inek.dataportal.facades.account;

import java.util.ArrayList;
import java.util.Calendar;
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
        List<AccountDocument> docs = findAll(accountId);
        List<DocInfo> docInfos = new ArrayList<>();
        for (AccountDocument doc : docs) {
            docInfos.add(new DocInfo(doc.getId(), doc.getName(), doc.getDomain().getName(), doc.getTimestamp(), doc.getValidUntil(), doc.isRead()));
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
        String sql = "SELECT p FROM AccountDocument p WHERE p._accountId = :accountId ORDER BY p._id DESC";
        Query query = getEntityManager().createQuery(sql, AccountDocument.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
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
