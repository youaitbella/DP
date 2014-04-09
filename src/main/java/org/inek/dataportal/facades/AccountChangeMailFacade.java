package org.inek.dataportal.facades;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.account.AccountChangeMail;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.utils.StringUtil;

@Stateless
public class AccountChangeMailFacade extends AbstractFacade<AccountChangeMail> {

    public AccountChangeMailFacade (){
        super(AccountChangeMail.class);
    }
    
    /**
     * Delivers all change mail requests older than the given date
     * @param date
     * @return 
     */
    public List<AccountChangeMail> findRequestsOlderThan(Date date) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AccountChangeMail> cq = cb.createQuery(AccountChangeMail.class);
        Root request = cq.from(AccountChangeMail.class);
        cq.select(request).where(cb.lessThan(request.get("_creationDate"), date));
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    public List<AccountChangeMail> findByActivationKey(String key, String mail) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AccountChangeMail> cq = cb.createQuery(AccountChangeMail.class);
        Root request = cq.from(AccountChangeMail.class);
        Predicate isKey = cb.equal(request.get("_activationKey"), key);
        Predicate isMail = cb.equal(request.get("_mail"), mail);
        cq.select(request).where(cb.and(isKey, isMail));
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    public boolean changeMail(int accountId, final String newMail) {
        if (StringUtil.isNullOrEmpty(newMail)) {
            return false;
        }
        AccountChangeMail changeMail = find(accountId);
        if (changeMail == null) {
            changeMail = new AccountChangeMail();
            changeMail.setAccountId(accountId);
            changeMail.setActivationKey(UUID.randomUUID().toString());
            changeMail.setMail(newMail);
            persist(changeMail);
        } else {
            changeMail.setActivationKey(UUID.randomUUID().toString());
            changeMail.setMail(newMail);
            merge(changeMail);
        }
        if (Mailer.sendMailActivationMail(changeMail)) {
            return true;
        }
        _logger.log(Level.WARNING, "Could not send mail activation mail for {0}", newMail);
        remove(changeMail);
        return false;
    }
    
}
