/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.documentScanner.facade;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Asynchronous;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.account.entities.DocumentDomain;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.common.data.account.entities.WaitingDocument;
import org.inek.dataportal.common.utils.DateUtils;

/**
 * @author muellermi
 */
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

    public int removeOldDocuments() {
        String sql = "DELETE FROM AccountDocument d WHERE d._validUntil < :date";
        Query query = getEntityManager().createQuery(sql, AccountDocument.class);
        query.setParameter("date", Calendar.getInstance().getTime());
        return query.executeUpdate();
    }

    public AccountDocument saveAccountDocument(AccountDocument accountDocument) {
        persist(accountDocument);
        return accountDocument;
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
