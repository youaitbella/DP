/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.ikadmin.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.data.ikadmin.entity.AccountResponsibility;
import org.inek.dataportal.common.data.ikadmin.entity.IkCorrelation;
import org.inek.dataportal.common.data.ikadmin.entity.User;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class IkAdminFacade extends AbstractDataAccess {

    public List<AccessRight> findAccessRights(int ik) {
        String jpql = "select ar from AccessRight ar where ar._ik = :ik";
        TypedQuery<AccessRight> query = getEntityManager().createQuery(jpql, AccessRight.class);
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    public AccessRight saveAccessRight(AccessRight accessRight) {
        if (accessRight.getId() > 0) {
            return merge(accessRight);
        } else {
            persist(accessRight);
            return accessRight;
        }
    }

    public List<User> findUsersByMailDomain(String mailDomain) {
        String jpql = "Select u from User u where u._email like :mailDomain";
        TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("mailDomain", "%" + mailDomain);
        return query.getResultList();
    }

    public Account findAccount(int userId) {
        return getEntityManager().find(Account.class, userId);
    }

    public Account saveAccount(Account account) {
        return getEntityManager().merge(account);
    }

    public List<AccessRight> findAccessRightsByAccountAndFeature(Account account, Feature feature) {
        String jpql = "select ar from AccessRight ar where ar._accountId = :accountId and ar._feature = :feature";
        TypedQuery<AccessRight> query = getEntityManager().createQuery(jpql, AccessRight.class);
        query.setParameter("accountId", account.getId());
        query.setParameter("feature", feature);
        return query.getResultList();
    }

    public List<AccessRight> findAccessRightsByAccountIkAndFeature(Account account, int ik, Feature feature) {
        String jpql = "select ar from AccessRight ar where ar._accountId = :accountId and ar._ik = :ik and ar._feature = :feature";
        TypedQuery<AccessRight> query = getEntityManager().createQuery(jpql, AccessRight.class);
        query.setParameter("accountId", account.getId());
        query.setParameter("ik", ik);
        query.setParameter("feature", feature);
        return query.getResultList();
    }

    /**
     * Checks for a list of iks, which of them are managed by an ik admin
     *
     * @param iks
     *
     * @return managedIks
     */
    public List<Integer> dertermineManagegIks(List<Integer> iks) {
        String jpql = "select distinct a._ik from AccountIkAdmin a where a._ik in :iks";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        query.setParameter("iks", iks);
        return query.getResultList();
    }

    public void removeRights(int accountId, int ik) {
        String jpql = "DELETE FROM AccessRight ar where ar._accountId = :accountId and ar._ik = :ik";
        Query query = getEntityManager().createQuery(jpql, AccessRight.class);
        query.setParameter("accountId", accountId);
        query.setParameter("ik", ik);
        query.executeUpdate();
    }

    public boolean hasIkAdmin(int ik) {
        String sql = "select cast(sign(count(0)) as bit) as hasAdmin from ikadm.mapAccountIkAdmin where aiaIk = " + ik;
        Query query = getEntityManager().createNativeQuery(sql);
        boolean hasIkAdmin = (boolean) query.getSingleResult();
        return hasIkAdmin;
    }

    @SuppressWarnings("unchecked")
    public List<Account> findIkAdmins(int ik) {
        String sql = "select Account.* from ikadm.mapAccountIkAdmin join Account on aiaAccountId = acId where aiaIk = " + ik;
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        return query.getResultList();
    }

    public List<Integer> loadAllManagegIks() {
        String jpql = "select distinct a._ik from AccountIkAdmin a";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        return query.getResultList();
    }

    public List<IkCorrelation> loadAllCorrelations() {
        return findAll(IkCorrelation.class);
    }

    public List<AccountResponsibility> loadAccountResponsibilities (Feature feature, Account account, Set<Integer> accountIks){
        String jpql = "select ar from AccountResponsibility ar "
                + "where ar._userIk in :accountIks and ar._accountId = :accountId and ar._feature = :feature";
        TypedQuery<AccountResponsibility> query = getEntityManager().createQuery(jpql, AccountResponsibility.class);
        query.setParameter("accountIks", accountIks);
        query.setParameter("accountId", account.getId());
        query.setParameter("feature", feature);
        return query.getResultList();
    }
}
