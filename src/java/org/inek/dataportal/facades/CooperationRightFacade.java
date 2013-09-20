package org.inek.dataportal.facades;

import java.util.List;
import javax.ejb.Stateless;
import org.inek.dataportal.entities.CooperationRight;
import org.inek.dataportal.enums.Feature;

@Stateless
public class CooperationRightFacade extends AbstractFacade<CooperationRight> {

    public CooperationRightFacade() {
        super(CooperationRight.class);
    }

    /**
     * returns a list of rights for a given feature, granted by account to others
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
     * returns a list of rights for a given feature, achieved for account from others
     * @param accountId
     * @param feature
     * @return 
     */
    public List<CooperationRight> getAchievedCooperationRights(Integer accountId, Feature feature) {
        String query = "SELECT cor FROM CooperationRight cor WHERE cor._partnerId = :accountId and cor._feature = :feature"; // and cor._cooperativeRight != CooperativeRight.None";
        return getEntityManager()
                .createQuery(query, CooperationRight.class)
                .setParameter("accountId", accountId)
                .setParameter("feature", feature)
                .getResultList();
    }

    public CooperationRight save(CooperationRight right) {
        if (right.getId()== null) {
            persist(right);
            return right;
        }
        return merge(right);
    }

}
