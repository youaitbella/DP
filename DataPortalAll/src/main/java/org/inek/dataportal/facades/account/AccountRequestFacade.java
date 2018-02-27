package org.inek.dataportal.facades.account;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.common.data.account.entities.AccountRequest;
import org.inek.dataportal.common.data.AbstractFacade;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.common.utils.DateUtils;

/**
 *
 * @author muellermi
 */
@Stateless
public class AccountRequestFacade extends AbstractFacade<AccountRequest> {

    public AccountRequestFacade() {
        super(AccountRequest.class);
    }

    
    public AccountRequest findByMailOrUser(String mailOrUser) {
        String jpql = "SELECT a FROM AccountRequest a WHERE a._email = :mailOrUser or a._user = :mailOrUser";
        TypedQuery<AccountRequest> query = getEntityManager().createQuery(jpql, AccountRequest.class);
        query.setParameter("mailOrUser", mailOrUser);
        try{
            return query.getSingleResult();
        }catch (Exception ex){
            return null;
        }
    }

    
    public List<AccountRequest> findRequestsOlderThan(Date date) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AccountRequest> cq = cb.createQuery(AccountRequest.class);
        Root<AccountRequest> request = cq.from(AccountRequest.class);
        cq.select(request).where(cb.lessThan(request.<Date>get("_creationDate"), date));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Inject private Mailer _mailer;
    public boolean createAccountRequest(AccountRequest accountRequest) {
        getLogger().info("Try to create a new account request");
        accountRequest.setActivationKey(UUID.randomUUID().toString());
        persist(accountRequest);
        if (accountRequest.getId() <= 0) {
            getLogger().warning("Failed to store accout request.");
            return false;
        }
        if (_mailer.sendActivationMail(accountRequest)) {
            return true;
        }
        getLogger().log(Level.WARNING, "Could not send activation mail for {0}", accountRequest.getEmail());
        remove(accountRequest);
        return false;

    }

    @Schedule(hour = "2")
    private void startcleanAccountRequests() {
        cleanAccountRequests();
    }
    
    @Asynchronous
    private void cleanAccountRequests() {
        List<AccountRequest> requests = findRequestsOlderThan(DateUtils.getDateWithDayOffset(-3));
        for (AccountRequest request : requests) {
            LOGGER.log(Level.INFO, "Clean request {0}, {1}", new Object[]{request.getId(), request.getUser()});
            remove(request);
        }
    }

}
