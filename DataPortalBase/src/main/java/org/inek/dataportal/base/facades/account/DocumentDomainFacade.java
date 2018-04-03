/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.base.facades.account;

import org.inek.dataportal.common.data.AbstractFacade;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.inek.dataportal.common.data.account.entities.DocumentDomain;

/**
 *
 * @author muellermi
 */
@Stateless
public class DocumentDomainFacade extends AbstractFacade<DocumentDomain> {

    public DocumentDomainFacade() {
        super(DocumentDomain.class);
    }

    public Optional<DocumentDomain> findByName(String name) {
        String jql = "select d from DocumentDomain d where d._name = :name";
        TypedQuery<DocumentDomain> query = getEntityManager().createQuery(jql, DocumentDomain.class);
        query.setParameter("name", name);
        List<DocumentDomain> resList = query.getResultList();
        switch (resList.size()) {
            case 0:
                return Optional.empty();
            case 1:
                return Optional.of(resList.get(0));
            default:
                throw new IllegalArgumentException("multiple entries in DocumentDomain: " + name);
        }
    }

    public DocumentDomain findOrCreateForName(String name) {
        String jql = "select d from DocumentDomain d where d._name = :name";
        TypedQuery<DocumentDomain> query = getEntityManager().createQuery(jql, DocumentDomain.class);
        query.setParameter("name", name);
        try{
            return query.getSingleResult();
        }catch (NoResultException ex){
            return createForName (name);
        }
    }

    private synchronized DocumentDomain createForName(String name) {
        DocumentDomain d = new DocumentDomain();
        d.setName(name);
        persist(d);
        return d;
    }

}
