package org.inek.dataportal.facades;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import org.inek.dataportal.entities.Account;
import org.inek.dataportal.entities.CooperationRight;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;

@Stateless
public class CooperationRightFacade extends AbstractFacade<CooperationRight> {

    public CooperationRightFacade() {
        super(CooperationRight.class);
    }

    /**
     * returns a list of rights for a given feature, granted by account to
     * others
     *
     * @param accountId
     * @param feature
     * @return
     */
    public List<CooperationRight> getGrantedCooperationRights(Integer accountId, Feature feature) {
        String query = "SELECT cor FROM CooperationRight cor WHERE cor._ownerId = :accountId and cor._feature = :feature";
        return getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("accountId", accountId)
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
    public List<CooperationRight> getAchievedCooperationRights(int accountId, Feature feature) {
        String query = "SELECT cor FROM CooperationRight cor WHERE cor._ownerId > 0 and cor._partnerId = :accountId and cor._feature = :feature"; // and cor._cooperativeRight != CooperativeRight.None";
        return getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("accountId", accountId)
                .setParameter("feature", feature)
                .getResultList();
    }

    public CooperativeRight getAchievedCooperativeRight(int ownerId, int partnerId, Feature feature, Integer ik) {
        if (ik == null){return CooperativeRight.None;}
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
    public boolean hasSupervisor(Feature feature, Integer ik) {
        if (ik == null){return false;}
        String query = "SELECT cor FROM CooperationRight cor "
                + "WHERE cor._ownerId = -1 "
                + "and cor._ik = :ik "
                + "and cor._feature = :feature";
        List<CooperationRight> cooperationRights = getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("ik", ik)
                .setParameter("feature", feature)
                .getResultList();
        for (CooperationRight cooperationRight : cooperationRights) {
            CooperativeRight right = cooperationRight.getCooperativeRight();
            if (right.equals(CooperativeRight.ReadCompletedSealSupervisor)
                    || right.equals(CooperativeRight.ReadWriteCompletedSealSupervisor)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSupervisor(Feature feature, Integer ik, int accountId) {
        CooperativeRight right = getSupervisorRight(feature, ik, accountId);
        return right.equals(CooperativeRight.ReadCompletedSealSupervisor)
                || right.equals(CooperativeRight.ReadWriteCompletedSealSupervisor);

    }

    public CooperativeRight getSupervisorRight(Feature feature, Integer ik, int accountId) {
        if (ik == null){return CooperativeRight.None;}
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

    public Set<Integer> isSupervisorFor(Feature feature, Account account) {
        Set<Integer> iks = account.getFullIkList();
        if (iks.isEmpty()){return iks;}
        String inIk = "";
        for (int ik : iks) {
            inIk += (inIk.length() > 0 ? ", " : "") + ik;
        }
        String query = "select acId from dbo.account "
                + "join accountFeature on acId = afaccountId and afFeature = ?1 "
                + "where acIk in (" + inIk + ") " // did not work :( "where acIk in ?2 ";
                + "union "
                + "select aaiAccountId from dbo.AccountAdditionalIK "
                + "join accountFeature on aaiAccountId = afaccountId and afFeature = ?1 "
                + "where aaiAccountId is not null and aaiIk in (" + inIk + ")";

        return new HashSet<>(getEntityManager()
                .createNativeQuery(query)
                .setParameter(1, feature.name())
                //.setParameter(2, iks) did not work :(
                .getResultList());
    }

    public CooperationRight save(CooperationRight right) {
        if (right.getId() == null) {
            persist(right);
            return right;
        }
        return merge(right);
    }

}
