package org.inek.dataportal.common.data.ikadmin.facade;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.ikadmin.entity.*;
import org.inek.dataportal.common.enums.Right;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;

import static org.inek.dataportal.api.helper.PortalConstants.*;

@RequestScoped
@Transactional
public class IkAdminFacade extends AbstractDataAccess {

    public List<AccessRight> findAccessRights(int ik) {
        String name = "AccessRight.findByIk";
        TypedQuery<AccessRight> query = getEntityManager().createNamedQuery(name, AccessRight.class);
        query.setParameter(IK, ik);
        return query.getResultList();
    }

    public List<AccessRight> findAccessRights(int ik, List<Feature> features) {
        String name = "AccessRight.findByIk+Feature";
        TypedQuery<AccessRight> query = getEntityManager().createNamedQuery(name, AccessRight.class);
        query.setParameter(IK, ik);
        query.setParameter("features", features);
        return query.getResultList();
    }

    public AccessRight readAccessRight(AccessRight accessRight) {
        String name = "AccessRight.findByRight";
        TypedQuery<AccessRight> query = getEntityManager().createNamedQuery(name, AccessRight.class);
        query.setParameter(IK, accessRight.getIk());
        query.setParameter(FEATURE, accessRight.getFeature());
        query.setParameter(ACCOUNT_ID, accessRight.getAccountId());
        return query.getSingleResult();
    }

    public AccessRight saveAccessRight(AccessRight accessRight) {
        try {
            if (accessRight.getId() > 0) {
                if (accessRight.getRight().equals(Right.Deny)) {
                    deleteAccountResponsibilities(accessRight.getAccountId(), accessRight.getFeature(), accessRight.getIk());
                }
                return merge(accessRight);
            } else {
                persist(accessRight);
                return accessRight;
            }
        } catch (DatabaseException ex) {
            // in one case (concurrent access?), we've got a duplicate key error
            // thus, we try to read an existing right
            return readAccessRight(accessRight);
        }

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
        query.setParameter(ACCOUNT_ID, account.getId());
        query.setParameter(FEATURE, feature);
        return query.getResultList();
    }

    public List<AccessRight> findAccessRightsByAccountIkAndFeature(Account account, int ik, Feature feature) {
        String jpql = "select ar from AccessRight ar where ar._accountId = :accountId and ar._ik = :ik and ar._feature = :feature";
        TypedQuery<AccessRight> query = getEntityManager().createQuery(jpql, AccessRight.class);
        query.setParameter(ACCOUNT_ID, account.getId());
        query.setParameter(IK, ik);
        query.setParameter(FEATURE, feature);
        return query.getResultList();
    }

    /**
     * Checks for a list of iks, which of them are managed by an ik admin
     *
     * @param iks
     * @return managedIks
     */
    public List<Integer> dertermineManagegIks(List<Integer> iks) {
        String jpql = "select distinct a._ik from IkAdmin a where a._ik in :iks";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        query.setParameter("iks", iks);
        return query.getResultList();
    }

    public void removeRights(int accountId, int ik) {
        String jpql = "DELETE FROM AccessRight ar where ar._accountId = :accountId and ar._ik = :ik";
        Query query = getEntityManager().createQuery(jpql, AccessRight.class);
        query.setParameter(ACCOUNT_ID, accountId);
        query.setParameter(IK, ik);
        query.executeUpdate();
    }

    public boolean hasIkAdmin(int ik) {
        String sql = "select cast(sign(count(0)) as bit) as hasAdmin from ikadm.IkAdmin where iaIk = " + ik;
        Query query = getEntityManager().createNativeQuery(sql);
        boolean hasIkAdmin = (boolean) query.getSingleResult();
        return hasIkAdmin;
    }

    public boolean hasIkAdmin(int ik, int featureId) {
        String sql = "select cast(sign(count(0)) as bit) as hasAdmin from ikadm.IkAdmin "
                + "join ikadm.IkAdminFeature on iafIkAdminId = iaId "
                + "where iaIk = " + ik
                + " and iafFeatureId = " + featureId;
        Query query = getEntityManager().createNativeQuery(sql);
        boolean hasIkAdmin = (boolean) query.getSingleResult();
        return hasIkAdmin;
    }

    public List<IkAdmin> findIkAdminsForIk(int ik) {
        String jpql = "select ia from IkAdmin ia where ia._ik = :ik";
        TypedQuery<IkAdmin> query = getEntityManager().createQuery(jpql, IkAdmin.class);
        query.setParameter(IK, ik);
        return query.getResultList();
    }

    public List<Integer> loadAllManagedIks() {
        String jpql = "select distinct a._ik from IkAdmin a";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        return query.getResultList();
    }

    public Map<Integer, Set<Feature>> loadAllManagedIkWithFeatures() {
        List<IkAdmin> ikAdmins = findAllFresh(IkAdmin.class);
        Map<Integer, Set<Feature>> managedIkWithFeatures = new HashMap<>();
        for (IkAdmin ikAdmin : ikAdmins) {
            Integer ik = ikAdmin.getIk();
            if (!managedIkWithFeatures.containsKey(ik)) {
                managedIkWithFeatures.put(ik, new HashSet<>());
            }
            Set<Feature> features = managedIkWithFeatures.get(ik);
            for (IkAdminFeature ikAdminFeature : ikAdmin.getIkAdminFeatures()) {
                features.add(ikAdminFeature.getFeature());
            }
        }
        return managedIkWithFeatures;
    }

    public List<IkCorrelation> loadAllCorrelations() {
        return findAll(IkCorrelation.class);
    }

    public List<AccountResponsibility> obtainAccountResponsibilities(int accountId, Feature feature, int userIk) {
        String name = "AccountResponsibility.findByAccountId+Feature+UserIk";
        TypedQuery<AccountResponsibility> query = getEntityManager().createNamedQuery(name, AccountResponsibility.class);
        query.setParameter(ACCOUNT_ID, accountId);
        query.setParameter(FEATURE, feature);
        query.setParameter("userIk", userIk);
        return query.getResultList();
    }

    public void saveResponsibilities(Map<String, List<AccountResponsibility>> _responsibleForIks) {
        for (String key : _responsibleForIks.keySet()) {
            String[] parts = key.split("\\|");
            int accountId = Integer.parseInt(parts[0]);
            Feature feature = Feature.fromName(parts[1]);
            int userIk = Integer.parseInt(parts[2]);
            deleteAccountResponsibilities(accountId, feature, userIk);
            saveResponsibilities(_responsibleForIks, key);
        }
    }

    private void deleteAccountResponsibilities(int accountId, Feature feature, int userIk) {
        String jpql = "delete from AccountResponsibility ar "
                + "where ar._accountId = :accountId and ar._feature = :feature and ar._userIk = :userIk";
        Query query = getEntityManager().createQuery(jpql, AccountResponsibility.class);
        query.setParameter(ACCOUNT_ID, accountId);
        query.setParameter(FEATURE, feature);
        query.setParameter("userIk", userIk);
        query.executeUpdate();
    }

    private void saveResponsibilities(Map<String, List<AccountResponsibility>> _responsibleForIks, String key) {
        for (AccountResponsibility accountResponsibility : _responsibleForIks.get(key)) {
            if (accountResponsibility.getDataIk() <= 0) {
                continue;
            }
            accountResponsibility.setId(-1);
            persist(accountResponsibility);
        }
    }

}
