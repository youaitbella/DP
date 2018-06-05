package org.inek.dataportal.common.data.cooperation.facade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.cooperation.entities.CooperationRight;
import org.inek.dataportal.common.enums.CooperativeRight;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.AbstractFacade;
import org.inek.dataportal.common.data.IkSupervisorInfo;

@Stateless
public class CooperationRightFacade extends AbstractFacade<CooperationRight> {

    public CooperationRightFacade() {
        super(CooperationRight.class);
    }

    /**
     * for a given feature returns a list of all cooperation rights given
     * account is involved in
     *
     * @param feature
     * @param account
     * @return
     */
    public List<CooperationRight> getCooperationRights(Feature feature, Account account) {
        if (account == null) {
            return new ArrayList<>();
        }
        Set<Integer> iks = account.getFullIkSet();
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
    public List<CooperationRight> getGrantedCooperationRights(Integer accountId, Feature feature) {
        String query = "SELECT cor FROM CooperationRight cor WHERE cor._ownerId = :accountId and cor._feature = :feature";
        return getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("accountId", accountId)
                .setParameter("feature", feature)
                .getResultList();
    }

    public List<CooperationRight> getGrantedCooperationRights(Integer accountId, int partnerId) {
        String query = "SELECT cor FROM CooperationRight cor WHERE cor._ownerId = :accountId and cor._partnerId = :partnerId";
        return getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("accountId", accountId)
                .setParameter("partnerId", partnerId)
                .getResultList();
    }

    public List<CooperationRight> getGrantedCooperationRights(Integer accountId, int partnerId, Feature feature) {
        String query = "SELECT cor FROM CooperationRight cor "
                + "WHERE cor._ownerId = :accountId and cor._partnerId = :partnerId and cor._feature = :feature";
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
    public List<CooperationRight> getAchievedCooperationRights(int accountId, Feature feature) {
        String query = "SELECT cor FROM CooperationRight cor WHERE cor._partnerId = :accountId and cor._feature = :feature";
        return getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("accountId", accountId)
                .setParameter("feature", feature)
                .getResultList();
    }

    public CooperationRight getCooperationRight(int ownerId, int partnerId, Feature feature, int ik) {
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
                    .getSingleResult();
        } catch (Exception e) {
            return new CooperationRight(ownerId, partnerId, ik, feature);
        }
    }
    public CooperativeRight getCooperativeRight(int ownerId, int partnerId, Feature feature, Integer ik) {
        return getCooperationRight(ownerId, partnerId, feature, ik == null ? -1 : ik).getCooperativeRight();
    }

    public CooperativeRight getIkSupervisorRight(Feature feature, Integer ik, int accountId) {
        return getCooperativeRight(-1, accountId, feature, ik);
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
        List<CooperationRight> cooperationRights = getIkSupervisorRights(ik, feature);
        return cooperationRights.stream().anyMatch((cooperationRight) -> (cooperationRight.getCooperativeRight().isSupervisor()));
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
        return getIkSupervisorRight(feature, ik, accountId).isSupervisor();
    }

    public Set<Integer> getAccountIdsByFeatureAndIk(Feature feature, int ik) {
        String jpql = "select acId from dbo.account "
                + "join accountFeature on acId = afaccountId and afFeatureId = ?1 "
                + "where acIk = ?2 "
                + "union "
                + "select aaiAccountId from dbo.AccountAdditionalIK "
                + "join accountFeature on aaiAccountId = afaccountId and afFeatureId = ?1 "
                + "where aaiAccountId is not null and aaiIk = ?2";
        @SuppressWarnings("unchecked") HashSet<Integer> result = new HashSet<>(getEntityManager()
                .createNativeQuery(jpql)
                .setParameter(1, feature.getId())
                .setParameter(2, ik)
                .getResultList());
        return result;
    }

    public CooperationRight save(CooperationRight right) {
        if (right.getId() < 0) {
            persist(right);
            return right;
        }
        return merge(right);
    }

    public List<IkSupervisorInfo> getIkSupervisorInfos() {
        String jpql = "SELECT r._feature, r._ik, a, r._cooperativeRight FROM CooperationRight r JOIN Account a "
                + "WHERE r._partnerId = a._id and r._ownerId = -1 order by r._feature, r._ik, a._lastName";
        // sadly this is not a list of the expected type, but of object[]
        //List<IkSupervisorInfo> infos = getEntityManager().createQuery(jpql, IkSupervisorInfo.class).getResultList();
        //return infos;
        
        // although the compiler tells us something else, this is whalt we get
        List<IkSupervisorInfo> infos = new ArrayList<>();
        @SuppressWarnings("unchecked") List<Object[]> objects = getEntityManager().createQuery(jpql).getResultList();
        for (Object[] obj : objects){
            IkSupervisorInfo info = new IkSupervisorInfo((Feature)obj[0], (int)obj[1], (Account)obj[2], (CooperativeRight)obj[3]);
            infos.add(info);
        }
        return infos;
    }

    public void createIkSupervisor(Feature feature, int ik, Integer accountId, CooperativeRight _right) {
        CooperationRight existingRight = getCooperationRight(-1, accountId, feature, ik);
        if (existingRight.getId() > 0){return;}  // to prevent multiple save
        CooperationRight right = new CooperationRight(-1, accountId, ik, feature);
        right.setCooperativeRight(_right);
        save(right);
    }

    public void deleteCooperationRight(int ownerId, int partnerId, Feature feature, int ik) {
        CooperationRight right = getCooperationRight(ownerId, partnerId, feature, ik);
        remove(right);
    }

}
