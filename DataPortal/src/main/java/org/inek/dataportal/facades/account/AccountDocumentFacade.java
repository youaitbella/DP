package org.inek.dataportal.facades.account;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
        String sql = "SELECT d._id, d._name, dd._name, d._created, d._validUntil, d._read, d._accountId, d._agentAccountId FROM AccountDocument d join DocumentDomain dd WHERE d._domainId = dd._id and  d._accountId = :accountId ORDER BY d._id DESC";
        // does not work properly :(
//        TypedQuery<DocInfo> query = getEntityManager().createQuery(sql, DocInfo.class);
//        query.setParameter("accountId", accountId);
//        return query.getResultList();
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("accountId", accountId);
        @SuppressWarnings("unchecked") List<Object[]> objects = query.getResultList();
        List<DocInfo> docInfos = new ArrayList<>();
        for (Object[] obj : objects) {
            docInfos.add(new DocInfo((int) obj[0], (String) obj[1], (String) obj[2], (Date) obj[3], (Date) obj[4], (boolean) obj[5], (int) obj[6], (int) obj[7], ""));
        }
        return docInfos;
    }

    public List<DocInfo> getSupervisedDocInfos(int accountId, String filter, int maxAge) {
        String jpql = "SELECT d._id, d._name, dd._name, d._created, null, d._read, d._accountId, d._agentAccountId, a._ik, a._company, a._town, a._firstName, a._lastName "
                + "FROM AccountDocument d "
                + "join DocumentDomain dd "
                + "join Account a "
                + "WHERE d._domainId = dd._id and d._accountId = a._id "
                + (accountId < 0 ? "" : "and d._agentAccountId = :accountId ")
                + "and d._created > :refDate "
                + (filter.isEmpty() ? "" : " and (d._name like :filter or a._ik = :numFilter or a._company like :filter or a._town like :filter or dd._name like :filter)")
                + "ORDER BY d._read, d._created DESC";
        Query query = getEntityManager().createQuery(jpql); //.setMaxResults(100);
        if (accountId >= 0) {
            query.setParameter("accountId", accountId);
        }
        if (!filter.isEmpty()) {
            int numFilter;
            try {
                numFilter = Integer.parseInt(filter);
            } catch (Exception ex) {
                numFilter = -999;
            }
            query.setParameter("numFilter", numFilter);
            query.setParameter("filter", "%" + filter + "%");
        }
        query.setParameter("refDate", DateUtils.getDateWithDayOffset(-maxAge));
        //dumpSql(query);
        @SuppressWarnings("unchecked") List<Object[]> objects = query.getResultList();
        List<DocInfo> docInfos = new ArrayList<>();
        for (Object[] obj : objects) {
            docInfos.add(new DocInfo((int) obj[0], (String) obj[1], (String) obj[2], (Date) obj[3], (Date) obj[4], (boolean) obj[5], (int) obj[6], (int) obj[7], obj[8] + " " + obj[9] + " " + obj[10] + " (" + obj[11] + " " + obj[12] + ")"));
        }
        return docInfos;
    }

    public List<String> getNewDocs(int accountId) {
        String sql = "SELECT d._name FROM AccountDocument d WHERE d._accountId = :accountId and d._created > :referenceDate ORDER BY d._id DESC";
        TypedQuery<String> query = getEntityManager().createQuery(sql, String.class);
        query.setParameter("accountId", accountId);
        query.setParameter("referenceDate", DateUtils.getDateWithDayOffset(-30));
        return query.setMaxResults(5).getResultList();
    }

    public List<AccountDocument> findAll(int accountId) {
        String sql = "SELECT d FROM AccountDocument d WHERE d._accountId = :accountId ORDER BY d._id DESC";
        TypedQuery<AccountDocument> query = getEntityManager().createQuery(sql, AccountDocument.class);
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
        _logger.log(Level.INFO, "Start sweeping old documents");
        String sql = "SELECT p FROM AccountDocument p WHERE p._validUntil < :date";
        Query query = getEntityManager().createQuery(sql, AccountDocument.class);
        query.setParameter("date", Calendar.getInstance().getTime());
        @SuppressWarnings("unchecked") List<AccountDocument> docs = query.getResultList();
        for (AccountDocument doc : docs) {
            _logger.log(Level.INFO, "Delete old document {0} of account {1}", new Object[]{doc.getName(), doc.getAccountId()});
            remove(doc);
        }
        _logger.log(Level.INFO, "Finished sweeping old documents");
    }

    public AccountDocument save(AccountDocument accountDocument) {
        persist(accountDocument);
        clearCache();
        return findFresh(accountDocument.getId());
    }

}
