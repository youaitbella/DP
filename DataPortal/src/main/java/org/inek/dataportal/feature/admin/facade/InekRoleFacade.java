package org.inek.dataportal.feature.admin.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.AbstractFacade;
import org.inek.dataportal.feature.admin.entity.InekRole;

@Stateless
public class InekRoleFacade extends AbstractFacade<InekRole> {

    public InekRoleFacade() {
        super(InekRole.class);
    }

    @SuppressWarnings("unchecked")
    public List<Account> findForFeature(Feature feature) {
        String sql = "select Account.* \n"
                + "from adm.listInekRole \n"
                + "join adm.mapAccountInekRole on irId = aiInekRoleId\n"
                + "join Account on aiAccountId = acId\n"
                + "where irFeatureId = ?1";
        //String jpql = "select r from InekRole r where r._feature = :feature";
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        query.setParameter(1, Feature.SPECIFIC_FUNCTION.getId());

        List<Account> data = query.getResultList();
        return data;
    }

    public InekRole save(InekRole role) {
        if (role.getId() == -1) {
            persist(role);
            return role;
        }
        return merge(role);
    }

}
