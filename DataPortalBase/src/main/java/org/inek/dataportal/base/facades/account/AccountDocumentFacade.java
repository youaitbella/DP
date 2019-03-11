package org.inek.dataportal.base.facades.account;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.common.helper.structures.DocInfo;
import org.inek.dataportal.common.utils.DateUtils;

import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

@Stateless
public class AccountDocumentFacade extends AbstractDataAccess {

    public AccountDocument find(int id) {
        return find(AccountDocument.class, id);
    }

    public AccountDocument findFresh(int id) {
        return findFresh(AccountDocument.class, id);
    }

    public void remove(AccountDocument accountDocument) {
        super.remove(accountDocument);
    }

    public AccountDocument merge(AccountDocument accountDocument) {
        return super.merge(accountDocument);
    }

    public void persist(AccountDocument accountDocument) {
        super.persist(accountDocument);
    }

    public List<DocInfo> getDocInfos(int accountId) {
        String jpql = "SELECT new org.inek.dataportal.common.helper.structures.DocInfo(" +
                "      ad._id, COALESCE(cd._name, ad._name), COALESCE(cd._domain._name, ad._domain._name), ad._created, ad._validUntil, ad._read, ad._accountId, ad._agentAccountId, ad._senderIk, "
                + "    '', concat (a._company, ' ', a._town, ' (', a._firstName, ' ', a._lastName, ')'), ad._sendToProcess) "
                + "FROM AccountDocument ad "
                + "join CommonDocument cd "
                + "join Account a on ad._documentId = cd._id "
                + "WHERE ad._agentAccountId = a._id  and ad._accountId = :accountId "
                + "ORDER BY ad._id DESC";

        TypedQuery<DocInfo> query = getEntityManager().createQuery(jpql, DocInfo.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public List<DocInfo> getSupervisedDocInfos(List<Integer> accountIds, String filter, int maxAge) {
        String jpql = "SELECT ad._id, ad._name, ad._domain._name, ad._created, null, ad._read, ad._accountId, ad._agentAccountId, ad._senderIk, "
                + "    min(ai._ik), max(ai._ik), count(ai._ik), "
                + "    concat (a._company, ' ', a._town, ' (', a._firstName, ' ', a._lastName, ')'), ad._sendToProcess "
                + "FROM AccountDocument ad "
                + "join Account a "
                + "join AccountIk ai "
                + "WHERE ad._accountId = a._id "
                + "  and a._id = ai._accountId"
                + "  and ad._agentAccountId in :accountIds "
                + "  and ad._created > :refDate "
                + (filter.isEmpty()
                ? ""
                : " and (ad._name like :filter or ai._ik = :numFilter or a._company like :filter or a._town like :filter or ad._domain._name like :filter) ")
                + "GROUP BY ad._id, ad._name, ad._domain._name, ad._created, ad._read, ad._accountId, ad._agentAccountId, ad._senderIk, "
                + "    a._company, a._town, a._firstName, a._lastName, ad._sendToProcess "
                + "ORDER BY ad._read, ad._created DESC";
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
            long ikCount = (long) obj[11];
            String ikInfo = (ikCount > 0 ? obj[9] : "")
                    + (ikCount > 2 ? ", (" + (ikCount - 2) + " weitere)" : "")
                    + (ikCount > 1 ? ", " + obj[10] : "")
                    + (ikCount > 0 ? " " : "");
            docInfos.add(new DocInfo((int) obj[0], (String) obj[1], (String) obj[2], (Date) obj[3], (Date) obj[4],
                    (boolean) obj[5], (int) obj[6], (int) obj[7], (int) obj[8], "",
                    ikInfo + obj[12], (boolean) obj[13]));
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
        String jpql = "SELECT ad._name FROM AccountDocument ad " +
                "WHERE ad._accountId = :accountId and ad._created > :referenceDate ORDER BY ad._id DESC";
        TypedQuery<String> query = getEntityManager().createQuery(jpql, String.class);
        query.setParameter("accountId", accountId);
        query.setParameter("referenceDate", DateUtils.getDateWithDayOffset(-30));
        return query.setMaxResults(5).getResultList();
    }

    public List<AccountDocument> findAll(int accountId) {
        String jpql = "SELECT ad FROM AccountDocument ad WHERE ad._accountId = :accountId ORDER BY ad._id DESC";
        TypedQuery<AccountDocument> query = getEntityManager().createQuery(jpql, AccountDocument.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public long count(int accountId) {
        String jpql = "SELECT count(ad._id) FROM AccountDocument ad WHERE ad._accountId = :accountId";
        Query query = getEntityManager().createQuery(jpql, Long.class);
        query.setParameter("accountId", accountId);
        return (long) query.getSingleResult();
    }

    public boolean isDocRead(int docId) {
        String jpql = "SELECT ad._read FROM AccountDocument ad WHERE ad._id = :docId";
        Query query = getEntityManager().createQuery(jpql, Boolean.class);
        query.setParameter("docId", docId);
        try {
            return (boolean) query.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        }
    }

    @Schedule(hour = "2", minute = "15", info = "once a day")
    //@Schedule(hour = "*", minute = "*/1", info = "once a minute")
    private void scheduleSweepOldDocuments() {
        sweepOldDocuments();
    }

    @Asynchronous
    private void sweepOldDocuments() {
        LOGGER.log(Level.INFO, "Sweeping old documents");
        String sql = "DELETE FROM AccountDocument ad WHERE ad._validUntil < :date";
        Query query = getEntityManager().createQuery(sql, AccountDocument.class);
        query.setParameter("date", Calendar.getInstance().getTime());
        query.executeUpdate();
    }

    public AccountDocument save(AccountDocument accountDocument) {
        persist(accountDocument);
        clearCache();
        AccountDocument savedDoc = findFresh(AccountDocument.class, accountDocument.getId());
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

    public List<String> getInvalidFileNamePatterns() {
        String sql = "select rfpPattern\n" +
                "from adm.listRejectedFilePatterns";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked") List<String> result = query.getResultList();
        return result;
    }
}
