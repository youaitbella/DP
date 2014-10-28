package org.inek.dataportal.facades.account;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import org.inek.dataportal.entities.account.AccountActivation;
import org.inek.dataportal.facades.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class AccountActivationFacade extends AbstractFacade<AccountActivation> {

    public AccountActivationFacade() {
        super(AccountActivation.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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
