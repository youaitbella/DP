package org.inek.dataportal.facades.account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.entities.PasswordRequest;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountAdditionalIK;
import org.inek.dataportal.entities.account.AccountChangeMail;
import org.inek.dataportal.entities.account.AccountFeature;
import org.inek.dataportal.entities.account.AccountPwd;
import org.inek.dataportal.entities.account.AccountRequest;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.FeatureState;
import org.inek.dataportal.facades.AbstractFacade;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.PasswordRequestFacade;
import org.inek.dataportal.facades.admin.ConfigFacade;
import org.inek.dataportal.helper.TransferFileCreator;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.requestmanager.FeatureRequestHandler;
import org.inek.dataportal.utils.Crypt;
import org.inek.dataportal.utils.ObjectUtil;
import org.inek.dataportal.utils.StringUtil;

/**
 *
 * @author vohldo
 */
@Stateless
public class AccountFacade extends AbstractFacade<Account> {

    @Inject private AccountPwdFacade _accountPwdFacade;
    @Inject private CustomerFacade _customerFacade;
    @Inject private FeatureRequestHandler _requestHandler;
    @Inject private AccountRequestFacade _accountRequestFacade;
    @Inject private AccountChangeMailFacade _accountChangeMailFacade;
    @Inject private PasswordRequestFacade _pwdRequestFacade;
    @Inject private ConfigFacade _configFacade;

    public AccountFacade() {
        super(Account.class);
    }

//    public String getDatabaseName() {
//        String ret = "Properties: ";
//        for(Object key : getEntityManager().getEntityManagerFactory().) {
//            ret += key + " ";
//        }
//        return ret;
//    }
    public Account findByMailOrUser(String mailOrUser) {
        String query = "SELECT a FROM Account a WHERE a._email = :mailOrUser or a._user = :mailOrUser";
        List<Account> list = getEntityManager().createQuery(query, Account.class).setParameter("mailOrUser", mailOrUser).getResultList();
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    public Account findByMail(String mail) {
        String query = "SELECT a FROM Account a WHERE a._email = :mail";
        List<Account> list = getEntityManager().createQuery(query, Account.class).setParameter("mail", mail).getResultList();
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    public Boolean existsMail(String mail) {
        return findByMail(mail) != null;
    }

    public Boolean existsMailOrUser(final String mailOrUser) {
        if (mailOrUser == null || mailOrUser.length() == 0) {
            return false;
        }
        return findByMailOrUser(mailOrUser) != null;
    }

    public Set<Account> getAccountsWithRequestedFeatures() {
        String statement = "SELECT a FROM Account a, IN (a._features) f WHERE f._featureState = :state";
        TypedQuery<Account> query = getEntityManager().createQuery(statement, Account.class);
        List<Account> acc = query.setParameter("state", FeatureState.REQUESTED).getResultList();
        return new HashSet<>(acc);
    }

    public List<Account> getAccountsForIds(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        String statement = "SELECT a FROM Account a WHERE a._id in :ids order by a._lastName, a._firstName";
        TypedQuery<Account> query = getEntityManager().createQuery(statement, Account.class);
        List<Account> accounts = query.setParameter("ids", ids).getResultList();
        return accounts;
    }

    public List<Account> getInekAcounts() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        Root<Account> request = cq.from(Account.class);
        cq.select(request).where(cb.like(request.get("_email"), "%@inek-drg.de"));
        TypedQuery<Account> query = getEntityManager().createQuery(cq);
        return query.getResultList();
    }

    public List<SelectItem> getInekAgents() {
//        CriteriaBuilder cBuilder = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<SelectItem> cQuery = cBuilder.createQuery(SelectItem.class);
//        Root<Account> accountRoot = cQuery.from(Account.class);
//        cQuery.select(cBuilder.construct(
//                SelectItem.class,
//                accountRoot.get(Account_._id),
//                accountRoot.get(Account_._lastName)
//        ));
        CriteriaBuilder cBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Object[]> cQuery = cBuilder.createQuery(Object[].class);
        Root<Account> accountRoot = cQuery.from(Account.class);
        Path<Integer> idPath = accountRoot.get("_id");
        Path<String> lastNamePath = accountRoot.get("_lastName");
        Path<String> firstNamePath = accountRoot.get("_firstName");
        cQuery.select(cBuilder.array(idPath, lastNamePath, firstNamePath));
        //cQuery.where(cBuilder.like(accountRoot.get(Account_._email), "%@inek-drg.de"));
        cQuery.where(cBuilder.like(accountRoot.get("_email").as(String.class), "%@inek-drg.de"));
        cQuery.orderBy(cBuilder.asc(accountRoot.get("_lastName")));
        List<Object[]> valueArray = getEntityManager().createQuery(cQuery).getResultList();
        List<SelectItem> agents = new ArrayList<>();
        for (Object[] values : valueArray) {
            agents.add(new SelectItem(values[0], values[1] + ", " + values[2]));
        }
        return agents;
    }

    public List<Account> getAccounts4Feature(Feature feature) {
        String statement = "SELECT a FROM Account a, IN (a._features) f WHERE f._feature = :feature and (f._featureState = :approved or f._featureState = :simple) ORDER BY a._company";
        TypedQuery<Account> query = getEntityManager().createQuery(statement, Account.class);
//        String sql = query.unwrap(JpaQuery.class).getDatabaseQuery().getSQLString();
//        System.out.println(sql);
        return query.setParameter("feature", feature).setParameter("approved", FeatureState.APPROVED).setParameter("simple", FeatureState.SIMPLE).getResultList();
    }

    public Account getAccount(final String mailOrUser, final String password) {
        //clearCache();
        if (StringUtil.isNullOrEmpty(mailOrUser) || StringUtil.isNullOrEmpty(password)) {
            return null;
        }
        Account account = findByMailOrUser(mailOrUser);
        if (account == null || account.isDeactivated()) {
            return null;
        }
        if (!_accountPwdFacade.isCorrectPassword(account.getId(), password)) {
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
        if (account.getId() == null) {
            getLogger().log(Level.SEVERE, "attempt to update a non-existing account");
            return null;  // let the client crash
        }

        for (AccountFeature accFeature : account.getFeatures()) {
            if (accFeature.getFeatureState() == FeatureState.NEW) {
                Feature feature = accFeature.getFeature();
                if (feature.needsApproval()) {
                    if (_requestHandler.handleFeatureRequest(account, feature)) {
                        accFeature.setFeatureState(FeatureState.REQUESTED);
                    }
                } else {
                    accFeature.setFeatureState(FeatureState.SIMPLE);
                }
            }
        }
        Account managedAccount = merge(account);
        setIKNames(managedAccount);
        return managedAccount;
    }

    public void deleteAccount(Account account) {
        if (account.getId() != null) {
            Account emptyAccount = new Account();
            emptyAccount.setEmail("deleted"); // write non-emailaddress to indicate this is an deleted account
            emptyAccount.setUser("");
            emptyAccount.setId(account.getId());
            merge(emptyAccount);
            AccountPwd accountPwd = _accountPwdFacade.find(account.getId());
            _accountPwdFacade.delete(accountPwd);
        }
    }

    public boolean activateAccount(String mailOrUser, String password, String activationKey) {
        if (StringUtil.isNullOrEmpty(mailOrUser) || StringUtil.isNullOrEmpty(password) || StringUtil.isNullOrEmpty(activationKey)) {
            return false;
        }
        AccountRequest accountRequest = _accountRequestFacade.findByMailOrUser(mailOrUser);
        if (accountRequest == null) {
            getLogger().log(Level.WARNING, "No account request found for {0}", mailOrUser);
            return false;
        }
        if (!accountRequest.getPasswordHash().equals(Crypt.hashPassword(password, accountRequest.getSalt())) || !accountRequest.getActivationKey().equals(activationKey)) {
            getLogger().log(Level.WARNING, "Password or activation key does not match {0}", mailOrUser);
            return false;
        }
        Account account = ObjectUtil.copyObject(Account.class, accountRequest);
        persist(account);
        AccountPwd accountPwd = new AccountPwd();
        accountPwd.setAccountId(account.getId());
        String salt = Crypt.getSalt();
        accountPwd.setSalt(salt);
        accountPwd.setPasswordHash(Crypt.hashPassword(password, salt));
        _accountPwdFacade.save(accountPwd);
        _accountRequestFacade.remove(accountRequest);
        TransferFileCreator.createEmailTransferFile(_configFacade, account.getEmail());
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
        if (!_accountPwdFacade.isCorrectPassword(account.getId(), password)) {
            return false;
        }

        // everything is ok. Let's update email and remove change request
        account.setEmail(mail);
        merge(account);
        _accountChangeMailFacade.remove(changeMail);

        TransferFileCreator.createEmailTransferFile(_configFacade, account.getEmail());

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
        PasswordRequest request = _pwdRequestFacade.find(account.getId());
        if (request == null) {
            return false;
        }
        if (!request.getPasswordHash().equals(Crypt.hashPassword(password, request.getSalt())) || !request.getActivationKey().equals(activationKey)) {
            return false;
        }
        AccountPwd accountPwd = _accountPwdFacade.find(account.getId());
        if (accountPwd == null) {
            return false;
        }
        String salt = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        accountPwd.setSalt(salt);
        accountPwd.setPasswordHash(Crypt.hashPassword(password, salt));
        _accountPwdFacade.merge(accountPwd);
        _pwdRequestFacade.remove(request);
        return true;
    }

    @Inject Mailer _mailer;

    public boolean requestPassword(final String mail, final String password) {
        if (StringUtil.isNullOrEmpty(mail) || StringUtil.isNullOrEmpty(password)) {
            return false;
        }
        Account account = findByMailOrUser(mail);
        if (account == null) {
            _logger.log(Level.INFO, "Password request for unknown account {0}", mail);
            return false;
        }
        if (account.isDeactivated()) {
            _logger.log(Level.INFO, "Password request for deactivated account {0}", mail);
            return false;
        }
        PasswordRequest request = _pwdRequestFacade.find(account.getId());
        if (request == null) {
            request = new PasswordRequest();
            request.setAccountId(account.getId());
            request.setActivationKey(UUID.randomUUID().toString());
            String salt = Crypt.getSalt();
            request.setSalt(salt);
            request.setPasswordHash(Crypt.hashPassword(password, salt));
            _pwdRequestFacade.save(request);
        } else {
            request.setActivationKey(UUID.randomUUID().toString());
            String salt = Crypt.getSalt();
            request.setSalt(salt);
            request.setPasswordHash(Crypt.hashPassword(password, salt));
            _pwdRequestFacade.merge(request);
        }
        if (_mailer.sendPasswordActivationMail(request, account)) {
            return true;
        }
        getLogger().log(Level.WARNING, "Could not send password activation mail for {0}", account.getEmail());
        _pwdRequestFacade.remove(request);
        return false;
    }

    public boolean isReRegister(String email) {
        try {
            Account account = findByMail(email);
            if (account == null) {
                return false;
            }
            _mailer.sendReRegisterMail(account);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<Account> getAccounts4Ik(Integer ik) {
        String jpql = "SELECT DISTINCT a FROM Account a left join AccountAdditionalIK i WHERE  a._ik = :ik  or a._id = i._accountId and i._ik = :ik order by a._lastName";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        dumpSql(query);
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    public Set<Integer> obtainIks4Accounts(Set<Integer> accountIds) {
        String accountIdList = accountIds.stream().map(i -> i.toString()).collect(Collectors.joining(", "));
        String sql = "select acIk"
                + "\r\n from dbo.Account"
                + "\r\n where acId in (" + accountIdList + ")"
                + "\r\n union"
                + "\r\n select aaiIK"
                + "\r\n from dbo.Account"
                + "\r\n join dbo.AccountAdditionalIK on acId = aaiAccountId"
                + "\r\n where acId in (" + accountIdList + ")";
        Query query = getEntityManager().createNativeQuery(sql, Integer.class);
        @SuppressWarnings("unchecked") HashSet<Integer> result = new HashSet<Integer>(query.getResultList());
        return result;
    }

    public void obtainRoleInfo(int ik, List<String> emailAddreses) {
        String addresses = emailAddreses.stream().map(m -> "'" + m + "'").collect(Collectors.joining(", "));
        String sql = "select cdDetails, dbo.concatenate(roText)\n"
                + "from CallCenterDB.dbo.ccCustomer\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId\n"
                + "join CallCenterDB.dbo.mapContactRole r1 on coId = r1.mcrContactId and r1.mcrRoleId in (3, 12, 14, 15, 16, 18, 19)\n"
                + "join CallCenterDB.dbo.listRole on r1.mcrRoleId = roId\n"
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId\n"
                + "where cuIk = "+ ik + "\n"
                + "	and coIsActive = 1\n"
                + "	and cdContactDetailTypeId = 'E'\n"
                + "     and cdDetails in("+ addresses + ")\n"
                + "group by cdDetails";
    }
}
