package org.inek.dataportal.facades;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.Account;
import org.inek.dataportal.entities.AccountAdditionalIK;
import org.inek.dataportal.entities.AccountChangeMail;
import org.inek.dataportal.entities.AccountFeature;
import org.inek.dataportal.entities.AccountPwd;
import org.inek.dataportal.entities.AccountRequest;
import org.inek.dataportal.entities.Customer;
import org.inek.dataportal.entities.PasswordRequest;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.FeatureState;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.requestmanager.FeatureRequestHandler;
import org.inek.dataportal.utils.Crypt;
import org.inek.dataportal.utils.ObjectUtil;
import org.inek.dataportal.utils.StringUtil;

/**
 *
 * @author muellermi
 */
@Stateless
public class AccountFacade extends AbstractFacade<Account> {

    @Inject private AccountPwdFacade _accountPwdFacade;
    @Inject private CustomerFacade _customerFacade;
    @Inject private FeatureRequestHandler _requestHandler;
    @Inject private AccountRequestFacade _accountRequestFacade;
    @Inject private AccountChangeMailFacade _accountChangeMailFacade;
    @Inject private PasswordRequestFacade _pwdRequestFacade;

    public AccountFacade() {
        super(Account.class);
    }

    public Account findByMailOrUser(String mailOrUser) {
        String query = "SELECT a FROM Account a WHERE a._email = :mailOrUser or a._user = :mailOrUser";
        List<Account> list = getEntityManager().createQuery(query, Account.class).setParameter("mailOrUser", mailOrUser).getResultList();
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    public Boolean existsMailOrUser(final String mailOrUser) {
        if (mailOrUser == null || mailOrUser.length() == 0) {
            return false;
        }
        return findByMailOrUser(mailOrUser) != null;
    }

    public List<Account> getAccountsWithRequestedFeatures() {
        String statement = "SELECT a FROM Account a, IN (a._features) f WHERE f._featureState = :state";
        TypedQuery<Account> query = getEntityManager().createQuery(statement, Account.class);
        return query.setParameter("state", FeatureState.REQUESTED).getResultList();
    }

    public List<Account> getAccountsForIds(Collection<Integer> ids) {
        String statement = "SELECT a FROM Account a WHERE a._accountId in :ids";
        TypedQuery<Account> query = getEntityManager().createQuery(statement, Account.class);
        return query.setParameter("ids", ids).getResultList();
    }

    public Account getAccount(final String mailOrUser, final String password) {
        clearCache();
        if (StringUtil.isNullOrEmpty(mailOrUser) || StringUtil.isNullOrEmpty(password)) {
            return null;
        }
        Account account = findByMailOrUser(mailOrUser);
        if (account == null || account.isDeactivated()) {
            return null;
        }
        if (!_accountPwdFacade.isCorrectPassword(account.getAccountId(), password)) {
            return null;
        }
        setIKNames(account);
        return account;
    }

    private void setIKNames(Account account) {
        for (AccountAdditionalIK ikInfo : account.getAdditionalIKs()) {
            Customer customer = _customerFacade.getCustomerByIK(ikInfo.getIK());
            ikInfo.setName(customer == null ? "" : customer.getName());
        }
    }

    public Account updateAccount(Account account) {
        if (account.getAccountId() == null) {
            _logger.log(Level.SEVERE, "attempt to update a non-existing account");
            return null;  // let the client crash
        }

        boolean requested = false;
        for (AccountFeature accFeature : account.getFeatures()) {
            if (accFeature.getFeatureState() == FeatureState.NEW) {
                FeatureState state = accFeature.getFeature() == Feature.DROPBOX ? FeatureState.REQUESTED : FeatureState.SIMPLE;
                accFeature.setFeatureState(state);
                requested |= state == FeatureState.REQUESTED;
            }
        }
        Account managedAccount = merge(account);
        setIKNames(managedAccount);
        if (requested) {
            _requestHandler.handleFeatureRequest(managedAccount);
        }
        return managedAccount;
    }

    public void deleteAccount(Account account) {
        if (account.getAccountId() != null) {
            Account emptyAccount = new Account();
            emptyAccount.setEmail("deleted"); // write non-emailaddress to indicate this is an deleted account
            emptyAccount.setUser("");
            emptyAccount.setAccountId(account.getAccountId());
            merge(emptyAccount);
            AccountPwd accountPwd = _accountPwdFacade.find(account.getAccountId());
            _accountPwdFacade.remove(accountPwd);
        }
    }

    public boolean activateAccount(String mailOrUser, String password, String activationKey) {
        if (StringUtil.isNullOrEmpty(mailOrUser) || StringUtil.isNullOrEmpty(password) || StringUtil.isNullOrEmpty(activationKey)) {
            return false;
        }
        AccountRequest accountRequest = _accountRequestFacade.findByMailOrUser(mailOrUser);
        if (accountRequest == null) {
            _logger.log(Level.WARNING, "No account request found for {0}", mailOrUser);
            return false;
        }
        if (!accountRequest.getPasswordHash().equals(Crypt.getHash("SHA", password)) || !accountRequest.getActivationKey().equals(activationKey)) {
            return false;
        }
        Account account = ObjectUtil.copyObject(Account.class, accountRequest);
        persist(account);
        AccountPwd accountPwd = new AccountPwd();
        accountPwd.setAccountId(account.getAccountId());
        accountPwd.setPasswordHash(Crypt.getPasswordHash(password, account.getAccountId()));
        _accountPwdFacade.persist(accountPwd);
        _accountRequestFacade.remove(accountRequest);
        return true;
    }

    public boolean activateMail(String mail, String password, String activationKey) {
        if (StringUtil.isNullOrEmpty(mail) || StringUtil.isNullOrEmpty(password) || StringUtil.isNullOrEmpty(activationKey)) {
            return false;
        }

        // is there such an activation entry?
        List<AccountChangeMail> changeMails = _accountChangeMailFacade.findByActivationKey(activationKey, mail);
        if (changeMails.size() != 1) {
            return false;
        }
        AccountChangeMail changeMail = changeMails.get(0);

        // is there an account
        Account account = find(changeMail.getAccountId());
        if (account == null || account.isDeactivated()) {
            return false;
        }

        // does the password match?
        if (!_accountPwdFacade.isCorrectPassword(account.getAccountId(), password)) {
            return false;
        }

        // everything is ok. Let's update email and remove change request
        account.setEmail(mail);
        merge(account);
        _accountChangeMailFacade.remove(changeMail);
        return true;
    }

    public boolean activatePassword(String mailOrUser, String password, String activationKey) {
        if (StringUtil.isNullOrEmpty(mailOrUser) || StringUtil.isNullOrEmpty(password) || StringUtil.isNullOrEmpty(activationKey)) {
            return false;
        }
        Account account = findByMailOrUser(mailOrUser);
        if (account == null || account.isDeactivated()) {
            return false;
        }
        PasswordRequest request = _pwdRequestFacade.find(account.getAccountId());
        if (request == null) {
            return false;
        }
        if (!request.getPasswordHash().equals(Crypt.getPasswordHash(password, account.getAccountId())) || !request.getActivationKey().equals(activationKey)) {
            return false;
        }
        AccountPwd accountPwd = _accountPwdFacade.find(account.getAccountId());
        if (accountPwd == null) {
            return false;
        }
        accountPwd.setPasswordHash(request.getPasswordHash());
        _accountPwdFacade.merge(accountPwd);
        _pwdRequestFacade.remove(request);
        return true;
    }

    public boolean requestPassword(final String mail, final String password) {
        if (StringUtil.isNullOrEmpty(mail) || StringUtil.isNullOrEmpty(password)) {
            return false;
        }
        Account account = findByMailOrUser(mail);
        if (account == null || account.isDeactivated()) {
            return false;
        }
        PasswordRequest request = _pwdRequestFacade.find(account.getAccountId());
        if (request == null) {
            request = new PasswordRequest();
            request.setAccountId(account.getAccountId());
            request.setActivationKey(UUID.randomUUID().toString());
            request.setPasswordHash(Crypt.getPasswordHash(password, account.getAccountId()));
            _pwdRequestFacade.persist(request);
        } else {
            request.setActivationKey(UUID.randomUUID().toString());
            request.setPasswordHash(Crypt.getPasswordHash(password, account.getAccountId()));
            _pwdRequestFacade.merge(request);
        }
        if (Mailer.sendPasswordActivationMail(request, mail)) {
            return true;
        }
        _logger.log(Level.WARNING, "Could not send password activation mail for {0}", account.getEmail());
        _pwdRequestFacade.remove(request);
        return false;
    }

}
