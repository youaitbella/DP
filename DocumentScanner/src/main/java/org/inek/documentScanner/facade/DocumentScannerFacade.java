package org.inek.documentScanner.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;
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
        String sql = "DELETE FROM AccountDocument d WHERE d._validUntil < :date";
        Query query = getEntityManager().createQuery(sql, AccountDocument.class);
        query.setParameter("date", Calendar.getInstance().getTime());
        return query.executeUpdate();
    }

    public int removeOldCommonDocuments() {
        String sql = "delete from CommonDocument cd "
                + "WHERE not exists(select 1 from AccountDocument ad where ad._documentId = cd._id) "
                + "      and cd._created < :date";
        Query query = getEntityManager().createQuery(sql, CommonDocument.class);
        query.setParameter("date", DateUtils.getDateWithDayOffset(-3));
        return query.executeUpdate();
    }

    public CommonDocument saveCommonDocument(CommonDocument document) {
        persist(document);
        clearCache();
        return document;
    }

    public AccountDocument saveAccountDocument(AccountDocument document) {
        persist(document);
        clearCache();
        return document;
    }

    @Asynchronous
    public void deleteOldWaitingDocuments() {
        String sql = "SELECT p FROM WaitingDocument p WHERE p._timestamp < :referenceDate";
        TypedQuery<WaitingDocument> query = getEntityManager().createQuery(sql, WaitingDocument.class);
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
    
}
