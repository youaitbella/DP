package org.inek.dataportal.base.facades.account;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.common.data.common.CommonDocument;
import org.inek.dataportal.common.helper.structures.DocInfo;
import org.inek.dataportal.common.utils.DateUtils;

import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

@Stateless
public class DocumentFacade extends AbstractDataAccess {

    public CommonDocument findCommonDocument(int documentId) {
        return find(CommonDocument.class, documentId);
    }

    public CommonDocument saveCommonDocument(CommonDocument document) {
        if (document.getId() <= 0) {
            super.persist(document);
            return document;
        }
        return merge(document);
    }

    public AccountDocument findAccountDocument(int id) {
        return find(AccountDocument.class, id);
    }

    public void remove(AccountDocument accountDocument) {
        super.remove(accountDocument);
    }

    public AccountDocument saveAccountDocument(AccountDocument document) {
        if (document.getId() <= 0) {
            super.persist(document);
            return document;
        }
        return merge(document);
    }

    public List<DocInfo> getDocInfos(int accountId) {
        /*
        sadly within this code

        String jpql = "SELECT new org.inek.dataportal.common.helper.structures.DocInfo("
                + "      ad._id, COALESCE(cd._name, ad._name), COALESCE(cd._domain._name, ad._domain._name), "
                + "      ad._created, ad._validUntil, ad._read, ad._accountId, ad._agentAccountId, ad._senderIk, "
                + "    '', concat (a._company, ' ', a._town, ' (', a._firstName, ' ', a._lastName, ')')) "
                + "FROM AccountDocument ad "
                + "left join CommonDocument cd  on ad._documentId = cd._id "
                + "join Account a on ad._accountId = a._id "
                + "WHERE ad._accountId = :accountId "
                + "ORDER BY ad._id DESC";

         the left join fails on cd._domain._name (the two dot element) resulting in an inner join :(

         Thus, we use a join and add a second list without a join.
         The second list contins the old-style documents stored within accountDocument
         Once this table does not contain a blob anymore, wa are able to remove getDocInfosFromAccountDocument
         */
        String jpql = "SELECT new org.inek.dataportal.common.helper.structures.DocInfo("
                + "      ad._id, cd._name, cd._domain._name, "
                + "      ad._created, ad._validUntil, ad._read, ad._accountId, ad._agentAccountId, ad._senderIk, "
                + "    '', concat (a._company, ' ', a._town, ' (', a._firstName, ' ', a._lastName, ')')) "
                + "FROM AccountDocument ad "
                + "join CommonDocument cd  on ad._documentId = cd._id "
                + "join Account a on ad._accountId = a._id "
                + "WHERE ad._accountId = :accountId "
                + "ORDER BY ad._id DESC";

        TypedQuery<DocInfo> query = getEntityManager().createQuery(jpql, DocInfo.class);
        query.setParameter("accountId", accountId);
        List<DocInfo> docInfos = query.getResultList();
        docInfos.addAll(getDocInfosFromAccountDocument(accountId));
        return docInfos;
    }

    private List<DocInfo> getDocInfosFromAccountDocument(int accountId) {
        String jpql = "SELECT new org.inek.dataportal.common.helper.structures.DocInfo("
                + "      ad._id, ad._name, ad._domain._name, "
                + "      ad._created, ad._validUntil, ad._read, ad._accountId, ad._agentAccountId, ad._senderIk, "
                + "    '', concat (a._company, ' ', a._town, ' (', a._firstName, ' ', a._lastName, ')')) "
                + "FROM AccountDocument ad "
                + "join Account a on ad._accountId = a._id "
                + "WHERE ad._documentId = 0 "
                + "      and ad._accountId = :accountId "
                + "ORDER BY ad._id DESC";

        TypedQuery<DocInfo> query = getEntityManager().createQuery(jpql, DocInfo.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public List<DocInfo> getSupervisedDocInfos(List<Integer> accountIds, String filter, int maxAge) {
        // see comment above
        String jpql = "SELECT new org.inek.dataportal.common.helper.structures.DocInfo("
                + "           ad._id, cd._name, cd._domain._name, "
                + "           ad._created, ad._created, ad._read, ad._accountId, ad._agentAccountId, ad._senderIk, '', "
                + "           concat (cast(min(ai._ik) as varchar), "
                + "               case count(ai._ik) "
                + "                   when 1 then '' "
                + "                   when 2 then concat (', ', cast(max(ai._ik) as varchar)) "
                + "                   else concat (', ..., ', cast(max(ai._ik) as varchar)) end, "
                + "               ' ', a._company, ' ', a._town, ' (', a._firstName, ' ', a._lastName, ')')) "
                + "FROM AccountDocument ad "
                + "join CommonDocument cd  on ad._documentId = cd._id "
                + "join Account a on ad._accountId = a._id "
                + "join AccountIk ai on a._id = ai._accountId "
                + "WHERE ad._agentAccountId in :accountIds "
                + "  and ad._created > :refDate "
                + (filter.isEmpty()
                ? ""
                : " and (ad._name like :filter or ai._ik = :numFilter or a._company like :filter "
                + " or a._town like :filter or ad._domain._name like :filter) ")
                + "GROUP BY ad._id, cd._name, cd._domain._name, "
                + "    ad._created, ad._read, ad._accountId, ad._agentAccountId, ad._senderIk, "
                + "    a._company, a._town, a._firstName, a._lastName "
                + "ORDER BY ad._read, ad._created DESC";
        TypedQuery<DocInfo> query = getEntityManager().createQuery(jpql, DocInfo.class);
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
        List<DocInfo> docInfos = query.getResultList();
        docInfos.addAll(getSupervisedDocInfosFromAccountDocument(accountIds, filter, maxAge));
        return docInfos;
    }

    public List<DocInfo> getSupervisedDocInfosFromAccountDocument(List<Integer> accountIds, String filter, int maxAge) {
        String jpql = "SELECT new org.inek.dataportal.common.helper.structures.DocInfo("
                + "           ad._id, ad._name, ad._domain._name, "
                + "           ad._created, ad._created, ad._read, ad._accountId, ad._agentAccountId, ad._senderIk, '', "
                + "           concat (cast(min(ai._ik) as varchar), "
                + "               case count(ai._ik) "
                + "                   when 1 then '' "
                + "                   when 2 then concat (', ', cast(max(ai._ik) as varchar)) "
                + "                   else concat (', ..., ', cast(max(ai._ik) as varchar)) end, "
                + "               ' ', a._company, ' ', a._town, ' (', a._firstName, ' ', a._lastName, ')')) "
                + "FROM AccountDocument ad "
                + "join Account a on ad._accountId = a._id "
                + "join AccountIk ai on a._id = ai._accountId "
                + "WHERE  ad._documentId = 0 "
                + "  and ad._agentAccountId in :accountIds "
                + "  and ad._created > :refDate "
                + (filter.isEmpty()
                ? ""
                : " and (ad._name like :filter or ai._ik = :numFilter or a._company like :filter "
                + " or a._town like :filter or ad._domain._name like :filter) ")
                + "GROUP BY ad._id, ad._name, ad._domain._name, "
                + "    ad._created, ad._read, ad._accountId, ad._agentAccountId, ad._senderIk, "
                + "    a._company, a._town, a._firstName, a._lastName "
                + "ORDER BY ad._read, ad._created DESC";
        TypedQuery<DocInfo> query = getEntityManager().createQuery(jpql, DocInfo.class);
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
        return query.getResultList();
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
        sweepOldAccountDocuments();
        sweepOldCommonDocuments();
    }

    @Asynchronous
    private void sweepOldAccountDocuments() {
        LOGGER.log(Level.INFO, "Sweeping old AccountDocuments");
        String sql = "DELETE FROM AccountDocument ad WHERE ad._validUntil < :date";
        Query query = getEntityManager().createQuery(sql, AccountDocument.class);
        query.setParameter("date", Calendar.getInstance().getTime());
        query.executeUpdate();
    }

    @Asynchronous
    private void sweepOldCommonDocuments() {
        LOGGER.log(Level.INFO, "Sweeping old CommonDocuments");
        String sql = "delete from CommonDocument cd "
                + "WHERE not exists(select 1 from AccountDocument ad where ad._documentId = cd._id) "
                + "      and cd._created < :date";
        Query query = getEntityManager().createQuery(sql, CommonDocument.class);
        query.setParameter("date", DateUtils.getDateWithDayOffset(-3));
        query.executeUpdate();
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
