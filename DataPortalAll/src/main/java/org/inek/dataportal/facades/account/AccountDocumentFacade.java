package org.inek.dataportal.facades.account;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.common.data.AbstractFacade;
import org.inek.dataportal.helper.structures.DocInfo;
import org.inek.dataportal.common.utils.DateUtils;

@Stateless
public class AccountDocumentFacade extends AbstractFacade<AccountDocument> {

    public AccountDocumentFacade() {
        super(AccountDocument.class);
    }

    public List<DocInfo> getDocInfos(int accountId) {
        String sql = "SELECT d._id, d._name, dd._name, d._created, d._validUntil, d._read, d._accountId, d._agentAccountId, d._senderIk, "
                + "    concat (a._company, ' ', a._town, ' (', a._firstName, ' ', a._lastName, ')'), d._sendToProcess "
                + "FROM AccountDocument d "
                + "join DocumentDomain dd "
                + "join Account a "
                + "WHERE d._domainId = dd._id and d._agentAccountId = a._id  and d._accountId = :accountId "
                + "ORDER BY d._id DESC";
        // does not work properly :(
//        TypedQuery<DocInfo> query = getEntityManager().createQuery(sql, DocInfo.class);
//        query.setParameter("accountId", accountId);
//        return query.getResultList();
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("accountId", accountId);
        @SuppressWarnings("unchecked") List<Object[]> objects = query.getResultList();
        List<DocInfo> docInfos = new ArrayList<>();
        for (Object[] obj : objects) {
            docInfos.add(new DocInfo((int) obj[0], (String) obj[1], (String) obj[2], (Date) obj[3], (Date) obj[4], 
                    (boolean) obj[5], (int) obj[6], (int) obj[7], (int) obj[8], "", (String) obj[9], (boolean) obj[10]));
        }
        return docInfos;
    }

    public List<DocInfo> getSupervisedDocInfos(List<Integer> accountIds, String filter, int maxAge) {
        String jpql = "SELECT d._id, d._name, dd._name, d._created, null, d._read, d._accountId, d._agentAccountId, d._senderIk, "
                + "    a._ik, concat (a._company, ' ', a._town, ' (', a._firstName, ' ', a._lastName, ')'), d._sendToProcess "
                + "FROM AccountDocument d "
                + "join DocumentDomain dd "
                + "join Account a "
                + "WHERE d._domainId = dd._id and d._accountId = a._id "
                + "  and d._agentAccountId in :accountIds "
                + "  and d._created > :refDate "
                + (filter.isEmpty() 
                ? "" 
                : " and (d._name like :filter or a._ik = :numFilter or a._company like :filter or a._town like :filter or dd._name like :filter)")
                + "ORDER BY d._read, d._created DESC";
        Query query = getEntityManager().createQuery(jpql); //.setMaxResults(100);
        query.setParameter("accountIds", accountIds);
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
            docInfos.add(new DocInfo((int) obj[0], (String) obj[1], (String) obj[2], (Date) obj[3], (Date) obj[4], 
                    (boolean) obj[5], (int) obj[6], (int) obj[7], (int) obj[8], "", 
                    ((int)obj[9] < 0 ? "" : obj[9] + " ") + obj[10], (boolean) obj[11]));
        }
        return docInfos;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSupervisingAccounts(int maxAge) {
        String sql = "select acId, acLastName + ', ' + acFirstName as AgentName \n"
                + "from account \n"
                + "where acMail like '%@inek-drg.de'\n"
                + "  and acId in (\n"
                + "    select adAgentAccountId \n"
                + "    from AccountDocument \n"
                + "    where adAgentAccountId > 0\n"
                + "        and DATEDIFF(DAY, adCreated, getDate()) <= " + maxAge + "\n"
                + "    ) \n"
                + "order by 2";
        Query query = getEntityManager().createNativeQuery(sql);
        List<Object[]> objects = query.getResultList();
        List<SelectItem> items = new ArrayList<>();
        for (Object[] obj : objects) {
            items.add(new SelectItem((int) obj[0], (String) obj[1]));
        }
        items.add(new SelectItem(-1, "<alle>"));
        return items;
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
        // the the user has opened multiple browsers or tabs, the document might be deleted somewhere else
        // thus we need to check for null
        if (doc != null && doc.isRead()) {
            return true;
        }
        return false;
    }

    @Schedule(hour = "2", minute = "15", info = "once a day")
    //@Schedule(hour = "*", minute = "*/1", info = "once a minute")
    private void scheduleSweepOldDocuments() {
        sweepOldDocuments();
    }

    @Asynchronous
    private void sweepOldDocuments() {
        LOGGER.log(Level.INFO, "Sweeping old documents");
        String sql = "DELETE FROM AccountDocument d WHERE d._validUntil < :date";
        Query query = getEntityManager().createQuery(sql, AccountDocument.class);
        query.setParameter("date", Calendar.getInstance().getTime());
        query.executeUpdate();
    }

    public AccountDocument save(AccountDocument accountDocument) {
        persist(accountDocument);
        clearCache();
        AccountDocument savedDoc = findFresh(accountDocument.getId());
        String delDate = new SimpleDateFormat("yyyy-MM-dd").format(savedDoc.getValidUntil());
        LOGGER.log(Level.INFO, "Document saved: {0}, valid until: {1}", new Object[]{savedDoc.getName(), delDate});
        return savedDoc;
    }

    public List<Integer> getAgentIds() {
        String sql = "select acId \n"
                + "from dbo.Account \n"
                + "join CallCenterDB.dbo.ccAgent on agEMail = acMail\n"
                + "where agActive = 1\n";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") List<Integer> result = query.getResultList();
        return result;
    }

}
