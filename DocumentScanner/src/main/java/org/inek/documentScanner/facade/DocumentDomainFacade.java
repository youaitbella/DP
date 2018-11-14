/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.documentScanner.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.account.entities.DocumentDomain;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * @author muellermi
 */
@Stateless
public class DocumentDomainFacade extends AbstractDataAccess {

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

}
