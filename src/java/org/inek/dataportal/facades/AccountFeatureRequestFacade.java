/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.facades;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.AccountFeatureRequest;

/**
 *
 * @author muellermi
 */
@Stateless
public class AccountFeatureRequestFacade extends AbstractFacade<AccountFeatureRequest> {

    private static final Logger _logger = Logger.getLogger("AccountFeatureRequestFacade");

    public AccountFeatureRequestFacade() {
        super(AccountFeatureRequest.class);
    }

    public AccountFeatureRequest findByApprovalKey(String key) {
        return findByField("_approvalKey", key);
    }

    public AccountFeatureRequest findByAccountId(int accountId) {
        return findByField("_accountId", accountId);
    }

    private AccountFeatureRequest findByField(String name, Object value) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AccountFeatureRequest> query = cb.createQuery(AccountFeatureRequest.class);
        Root<AccountFeatureRequest> root = query.from(AccountFeatureRequest.class);
        query.select(root).where(cb.equal(root.get(name), value));
        TypedQuery<AccountFeatureRequest> q = getEntityManager().createQuery(query);
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
