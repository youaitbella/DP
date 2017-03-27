package org.inek.dataportal.facades.admin;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.admin.InekRole;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.AbstractFacade;

@Stateless
public class InekRoleFacade extends AbstractFacade<InekRole> {

    public InekRoleFacade() {
        super(InekRole.class);
    }

    public List<Account> findForFeature(Feature feature) {
        String sql = "select Account.* \n"
                + "from adm.listInekRole \n"
                + "join adm.mapAccountInekRole on irId = aiInekRoleId\n"
                + "join Account on aiAccountId = acId\n"
                + "where irFeature = 'SPECIFIC_FUNCTION'";
        //String jpql = "select r from InekRole r where r._feature = :feature";
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
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
