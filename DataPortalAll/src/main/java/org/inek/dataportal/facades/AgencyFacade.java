/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.feature.account.entities.Account;
import org.inek.dataportal.entities.Agency;

/**
 *
 * @author muellermi
 */
@Stateless
public class AgencyFacade extends AbstractDataAccess {

    public List<Agency> findAllAgencies() {
        return findAll(Agency.class);
    }

    public Agency findAgency(int id) {
        return find(Agency.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Account> findAgencyAccounts(int agencyId){
        String sql = "select a.* from account a join mapAccountAgency on acId = maaAccountId where maaAgencyId = " + agencyId;
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        return query.getResultList();
    }
}
