package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import javax.persistence.Query;
import org.inek.dataportal.entities.account.AccountActivation;

/**
 *
 * @author muellermi
 */
@Stateless
public class AccountActivationFacade extends AbstractFacade<AccountActivation> {

    public AccountActivationFacade() {
        super(AccountActivation.class);
    }

    public AccountActivation findAccountByGUID(String guid) {
        String sql = "SELECT a FROM AccountActivation a WHERE a._guid = :guid";
        Query query = getEntityManager().createQuery(sql, AccountActivation.class);
        query.setParameter("guid", guid);
        try {
            return (AccountActivation) query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
}
