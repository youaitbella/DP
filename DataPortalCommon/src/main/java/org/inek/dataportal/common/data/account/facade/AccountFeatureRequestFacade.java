/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.common.data.account.facade;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.common.data.account.entities.AccountFeatureRequest;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.data.AbstractFacade;

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
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AccountFeatureRequest> query = cb.createQuery(AccountFeatureRequest.class);
        Root<AccountFeatureRequest> root = query.from(AccountFeatureRequest.class);
        query.select(root).where(cb.equal(root.get("_approvalKey"), key));
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
        Predicate isAccount = cb.equal(root.get("_accountId"), accountId);
        Predicate isFeature = cb.equal(root.get("_feature"), feature);
        query.select(root).where(cb.and(isAccount, isFeature));
        TypedQuery<AccountFeatureRequest> q = getEntityManager().createQuery(query);
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void save(AccountFeatureRequest featureRequest) {
        persist(featureRequest);
    }

}
