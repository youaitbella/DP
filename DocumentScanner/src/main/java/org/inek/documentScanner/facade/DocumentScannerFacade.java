package org.inek.documentScanner.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.common.data.account.entities.DocumentDomain;
import org.inek.dataportal.common.data.account.entities.WaitingDocument;
import org.inek.dataportal.common.data.common.CommonDocument;
import org.inek.dataportal.common.utils.DateUtils;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

@Stateless
public class DocumentScannerFacade extends AbstractDataAccess {

    public DocumentDomain findOrCreateForName(String name) {
        String jql = "select d from DocumentDomain d where d._name = :name";
        TypedQuery<DocumentDomain> query = getEntityManager().createQuery(jql, DocumentDomain.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return createForName(name);
        }
    }

    private synchronized DocumentDomain createForName(String name) {
        DocumentDomain d = new DocumentDomain();
        d.setName(name);
        persist(d);
        return d;
    }

    public int removeOldAccountDocuments() {
        String jpql = "DELETE FROM AccountDocument d WHERE d._validUntil < :date";
        Query query = getEntityManager().createQuery(jpql, AccountDocument.class);
        query.setParameter("date", Calendar.getInstance().getTime());
        return query.executeUpdate();
    }

    public int removeOldCommonDocuments() {
        String jpql = "delete from CommonDocument cd "
                + "WHERE not exists(select 1 from AccountDocument ad where ad._documentId = cd._id) "
                + "      and cd._created < :date";
        Query query = getEntityManager().createQuery(jpql, CommonDocument.class);
        query.setParameter("date", DateUtils.getDateWithDayOffset(-3));
        return query.executeUpdate();
    }

    public CommonDocument saveCommonDocument(CommonDocument document) {
        persist(document);
        clearCache();
        return document;
    }

    public AccountDocument createAccountDocument(Account account, CommonDocument commonDocument, int validity) {
        AccountDocument accountDocument = new AccountDocument(commonDocument.getId());
        accountDocument.setAccountId(account.getId());
        accountDocument.setValidity(validity);
        accountDocument.setDomain(commonDocument.getDomain());
        accountDocument.setName(commonDocument.getName());
        saveAccountDocument(accountDocument);
        return accountDocument;
    }

    public AccountDocument saveAccountDocument(AccountDocument document) {
        if (document.getId() <= 0) {
            persist(document);
            clearCache();
            return document;
        }
        return merge(document);
    }

    @Asynchronous
    public void deleteOldWaitingDocuments() {
        String jpql = "SELECT p FROM WaitingDocument p WHERE p._timestamp < :referenceDate";
        TypedQuery<WaitingDocument> query = getEntityManager().createQuery(jpql, WaitingDocument.class);
        query.setParameter("referenceDate", DateUtils.getDateWithDayOffset(-60));
        List<WaitingDocument> docs = query.getResultList();
        for (WaitingDocument doc : docs) {
            LOGGER.log(Level.INFO, "Delete old waiting document {0} of agent {1}", new Object[]{doc.getName(),
                    doc.getAgentAccountId()});
            remove(doc);
        }
    }

    public void saveWaitingDocument(WaitingDocument waitingDocument) {
        persist(waitingDocument);
    }

    public Optional<AccountDocument> findAccountDocumentWithContent() {
        String jpql = "Select a from AccountDocument a where a._documentId = 0";
        TypedQuery<AccountDocument> query = getEntityManager().createQuery(jpql, AccountDocument.class);
        query.setMaxResults(1);
        List<AccountDocument> documents = query.getResultList();
        if (documents.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(documents.get(0));
    }
}
