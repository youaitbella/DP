/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.facades.account;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.account.AccountFeatureRequest;
import org.inek.dataportal.entities.account.AccountFeatureRequest_;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class AccountFeatureRequestFacade extends AbstractFacade<AccountFeatureRequest> {

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

    public AccountFeatureRequest findByAccountIdAndFeature(int accountId, Feature feature) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AccountFeatureRequest> query = cb.createQuery(AccountFeatureRequest.class);
        Root<AccountFeatureRequest> root = query.from(AccountFeatureRequest.class);
        Predicate isAccount = cb.equal(root.get(AccountFeatureRequest_._accountId), accountId);
        Predicate isFeature = cb.equal(root.get(AccountFeatureRequest_._feature), feature);
        query.select(root).where(cb.and(isAccount, isFeature));
        TypedQuery<AccountFeatureRequest> q = getEntityManager().createQuery(query);
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
