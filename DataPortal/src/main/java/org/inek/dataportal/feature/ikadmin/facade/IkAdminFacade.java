/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.ikadmin.facade;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.facades.AbstractDataAccess;
import org.inek.dataportal.feature.ikadmin.entity.AccessRight;
import org.inek.dataportal.feature.ikadmin.entity.User;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class IkAdminFacade extends AbstractDataAccess{

    public List<AccessRight> findAccessRights(int ik) {
        String jpql = "select ar from AccessRight ar where ar._ik = :ik";
        TypedQuery<AccessRight> query = getEntityManager().createQuery(jpql, AccessRight.class);
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    public AccessRight saveAccessRight(AccessRight accessRight) {
        if (accessRight.getId() > 0){
            return merge(accessRight);
        }else{
            persist(accessRight);
            return accessRight;
        }
    }

    public List<User> findUsersByMailDomain(String mailDomain) {
        String jpql = "Select u from User u where u._email like :mailDomain";
        TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
        query.setParameter("mailDomain", "%" + mailDomain);
        return query.getResultList();
    }

    public Account findAccount(int userId) {
        return getEntityManager().find(Account.class, userId);
    }

    public Account saveAccount(Account account) {
        return getEntityManager().merge(account);
    }
    
    
}
