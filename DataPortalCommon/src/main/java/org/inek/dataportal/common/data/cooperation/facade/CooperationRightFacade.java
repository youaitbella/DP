package org.inek.dataportal.common.data.cooperation.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.cooperation.entities.CooperationRight;
import org.inek.dataportal.common.enums.CooperativeRight;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class CooperationRightFacade extends AbstractFacade<CooperationRight> {

    private static final Logger LOGGER = Logger.getLogger("CooperationRightFacade");

    public CooperationRightFacade() {
        super(CooperationRight.class);
    }

    /**
     * for a given feature returns a list of all cooperation rights given account is involved in
     *
     * @param feature
     * @param account
     *
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
     * returns a list of rights for a given feature, granted by account to others
     *
     * @param accountId
     * @param feature
     *
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
     * returns a list of rights for a given feature, achieved for account from others
     *
     * @param accountId
     * @param feature
     *
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

    public CooperationRight save(CooperationRight right) {
        if (right.getId() < 0) {
            persist(right);
            return right;
        }
        return merge(right);
    }

    public void deleteCooperationRight(int ownerId, int partnerId, Feature feature, int ik) {
        CooperationRight right = getCooperationRight(ownerId, partnerId, feature, ik);
        remove(right);
    }

    @Schedule(hour = "4")
    private void removeOrphantCooperationRights() {
        deleteCooperationRightsIfIkadminExists();
        deleteCooperationRightsIfPartnerIsDeleted();
        // do not delete if the owner is deleted (to keep rights for the still existent partner)
    }

    private void deleteCooperationRightsIfIkadminExists() {
        try {
            String sql = "delete from usr.CooperationRight where corId in (\n"
                    + "  select corid\n"
                    + "  from usr.CooperationRight \n"
                    + "  join ikadm.IkAdmin on corIk = iaik\n"
                    + "  join ikadm.IkAdminFeature on iaId = iafIkAdminId and corFeatureId = iafFeatureId\n"
                    + ")";
            Query query = getEntityManager().createNativeQuery(sql);
            query.executeUpdate();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Exception during deleteCooperationRightsIfIkadminExists: {0}", ex.getMessage());
        }
    }

    private void deleteCooperationRightsIfPartnerIsDeleted() {
        try {
            String sql = "delete from usr.CooperationRight where corId in (\n"
                    + "  select corId\n"
                    + "  from usr.CooperationRight \n"
                    + "  join Account o on corOwnerId = o.acId\n"
                    + "  join Account p on corPartnerId = p.acId\n"
                    + "  where p.acMail = 'deleted'\n"
                    + ")";
            Query query = getEntityManager().createNativeQuery(sql);
            query.executeUpdate();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Exception during deleteCooperationRightsIfPartnerIsDeleted: {0}", ex.getMessage());
        }
    }

}
