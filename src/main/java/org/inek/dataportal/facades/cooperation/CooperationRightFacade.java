package org.inek.dataportal.facades.cooperation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.cooperation.CooperationRight;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.AbstractFacade;

@Stateless
public class CooperationRightFacade extends AbstractFacade<CooperationRight> {

    public CooperationRightFacade() {
        super(CooperationRight.class);
    }

    /**
     * for a given feature
     * returns a list of all cooperation rights given account is involved in
     * @param feature
     * @param account
     * @return 
     */
    public List<CooperationRight> getCooperationRights(Feature feature, Account account) {
        if (account == null) {
            return new ArrayList<>();
        }
        Set<Integer> iks = account.getFullIkList();
        iks.add(-1);
        String query = "SELECT cor FROM CooperationRight cor WHERE cor._feature = :feature "
                + "and (cor._ownerId = :accountId or cor._partnerId = :accountId or cor._ownerId = -1 and cor._ik in :iks)";
        return getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("accountId", account.getId())
                .setParameter("feature", feature)
                .setParameter("iks", iks)
                .getResultList();
    }


    /**
     * returns a list of rights for a given feature, granted by account to
     * others
     *
     * @param accountId
     * @param feature
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<CooperationRight> getGrantedCooperationRights(Integer accountId, Feature feature) {
        String query = "SELECT cor FROM CooperationRight cor WHERE cor._ownerId = :accountId and cor._feature = :feature";
        return getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("accountId", accountId)
                .setParameter("feature", feature)
                .getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<CooperationRight> getGrantedCooperationRights(Integer accountId, int partnerId) {
        String query = "SELECT cor FROM CooperationRight cor WHERE cor._ownerId = :accountId and cor._partnerId = :partnerId";
        return getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("accountId", accountId)
                .setParameter("partnerId", partnerId)
                .getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<CooperationRight> getGrantedCooperationRights(Integer accountId, int partnerId, Feature feature) {
        String query = "SELECT cor FROM CooperationRight cor WHERE cor._ownerId = :accountId and cor._partnerId = :partnerId and cor._feature = :feature";
        return getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("accountId", accountId)
                .setParameter("partnerId", partnerId)
                .setParameter("feature", feature)
                .getResultList();
    }

    /**
     * returns a list of rights for a given feature, achieved for account from
     * others
     *
     * @param accountId
     * @param feature
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<CooperationRight> getAchievedCooperationRights(int accountId, Feature feature) {
//        String query = "SELECT cor FROM CooperationRight cor WHERE cor._ownerId > 0 and cor._partnerId = :accountId and cor._feature = :feature"; // and cor._cooperativeRight != CooperativeRight.None";
        String query = "SELECT cor FROM CooperationRight cor WHERE cor._partnerId = :accountId and cor._feature = :feature"; // and cor._cooperativeRight != CooperativeRight.None";
        return getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("accountId", accountId)
                .setParameter("feature", feature)
                .getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public CooperativeRight getCooperativeRight(int ownerId, int partnerId, Feature feature, Integer ik) {
        if (ik == null) {
            return CooperativeRight.None;
        }
        try {
            String query = "SELECT cor FROM CooperationRight cor "
                    + "WHERE cor._ownerId = :ownerId "
                    + "and cor._partnerId = :partnerId "
                    + "and cor._ik = :ik "
                    + "and cor._feature = :feature";
            return getEntityManager()
                    .createQuery(query, CooperationRight.class)
                    .setParameter("ownerId", ownerId)
                    .setParameter("partnerId", partnerId)
                    .setParameter("ik", ik)
                    .setParameter("feature", feature)
                    .getSingleResult().getCooperativeRight();
        } catch (Exception e) {
            return CooperativeRight.None;
        }
    }

    /**
     * Checks, whether a declared supervisor exists for given feature and ik A
     * declared supervisor cannot be establish herself by cooperation feature A
     * declared supervisor will be created by system admin: ownerId = -1,
     * partnerId = supervisor thus supervisor will be forced to be partner of
     * all others
     *
     * @param feature
     * @param ik
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean hasSupervisor(Feature feature, Integer ik) {
        List<CooperationRight> cooperationRights = getIkSupervisorRights(ik, feature);
        return cooperationRights.stream().anyMatch((cooperationRight) -> (cooperationRight.getCooperativeRight().canSeal()));
    }

    public List<CooperationRight> getIkSupervisorRights(Integer ik, Feature feature) {
        if (ik == null || ik < 0) {
            return new ArrayList<>();
        }
        String query = "SELECT cor FROM CooperationRight cor "
                + "WHERE cor._ownerId = -1 "
                + "and cor._ik = :ik "
                + "and cor._feature = :feature";
        List<CooperationRight> cooperationRights = getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("ik", ik)
                .setParameter("feature", feature)
                .getResultList();
        return cooperationRights;
    }

    public boolean isIkSupervisor(Feature feature, Integer ik, int accountId) {
        return getIkSupervisorRight(feature, ik, accountId).canSeal();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public CooperativeRight getIkSupervisorRight(Feature feature, Integer ik, int accountId) {
        if (ik == null) {
            return CooperativeRight.None;
        }
        try {
            String query = "SELECT cor FROM CooperationRight cor "
                    + "WHERE cor._ownerId = -1 "
                    + "and cor._partnerId = :accountId "
                    + "and cor._ik = :ik "
                    + "and cor._feature = :feature";
            CooperativeRight right = getEntityManager()
                    .createQuery(query, CooperationRight.class)
                    .setParameter("accountId", accountId)
                    .setParameter("ik", ik)
                    .setParameter("feature", feature)
                    .getSingleResult().getCooperativeRight();
            return right;
        } catch (Exception e) {
            // there might be no entry
            return CooperativeRight.None;
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Set<Integer> getAccountIdsByFeatureandIk(Feature feature, int ik) {
        String query = "select acId from dbo.account "
                + "join accountFeature on acId = afaccountId and afFeature = :feature "
                + "where acIk = :ik "
                + "union "
                + "select aaiAccountId from dbo.AccountAdditionalIK "
                + "join accountFeature on aaiAccountId = afaccountId and afFeature = :feature "
                + "where aaiAccountId is not null and aaiIk = :ik";

        return new HashSet<>(getEntityManager()
                .createNativeQuery(query)
                .setParameter("feature", feature.name())
                .setParameter("ik", ik)
                .getResultList());
    }


    public CooperationRight save(CooperationRight right) {
        if (right.getId() < 0) {
            persist(right);
            return right;
        }
        return merge(right);
    }

    
}
