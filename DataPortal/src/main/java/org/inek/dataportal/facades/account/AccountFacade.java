package org.inek.dataportal.facades.account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
import org.inek.dataportal.enums.IkReference;
import org.inek.dataportal.facades.AbstractDataAccess;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.PasswordRequestFacade;
import org.inek.dataportal.feature.admin.facade.ConfigFacade;
import org.inek.dataportal.feature.ikadmin.entity.AccessRight;
import org.inek.dataportal.feature.ikadmin.enums.Right;
import org.inek.dataportal.feature.ikadmin.facade.IkAdminFacade;
import org.inek.dataportal.helper.TransferFileCreator;
import org.inek.dataportal.helper.Utils;
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
public class AccountFacade extends AbstractDataAccess {

    @Inject private AccountPwdFacade _accountPwdFacade;
    @Inject private CustomerFacade _customerFacade;
    @Inject private FeatureRequestHandler _requestHandler;
    @Inject private AccountRequestFacade _accountRequestFacade;
    @Inject private AccountChangeMailFacade _accountChangeMailFacade;
    @Inject private PasswordRequestFacade _pwdRequestFacade;
    @Inject private ConfigFacade _configFacade;

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

    public List<Account> getAccounts4Feature(Feature feature) {
        String statement = "SELECT a FROM Account a, IN (a._features) f "
                + "WHERE f._feature = :feature and (f._featureState = :approved or f._featureState = :simple) ORDER BY a._company";
        TypedQuery<Account> query = getEntityManager().createQuery(statement, Account.class);
//        String sql = query.unwrap(JpaQuery.class).getDatabaseQuery().getSQLString();
//        System.out.println(sql);
        return query.setParameter("feature", feature)
                .setParameter("approved", FeatureState.APPROVED)
                .setParameter("simple", FeatureState.SIMPLE)
                .getResultList();
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

    @Inject private IkAdminFacade _ikAdminFacade;

    public Account updateAccount(Account account) {
        if (account.getId() <= 0) {
            getLogger().log(Level.SEVERE, "attempt to update a non-existing account");
            return null;  // let the client crash
        }

        for (AccountFeature accFeature : account.getFeatures()) {
            if (accFeature.getFeatureState() == FeatureState.NEW) {
                Feature feature = accFeature.getFeature();
                boolean handleClassicalWay = false;
                if (accFeature.getFeature().getIkReference() == IkReference.Hospital) {
                    // this feature might be affected by an ik admin
                    Set<Integer> fullIkSet = account.getFullIkSet();
                    handleClassicalWay = fullIkSet.isEmpty();
                    for (int ik : fullIkSet) {
                        if (hasIkAdmin(ik)) {
                            AccessRight accessRight = new AccessRight(account.getId(), ik, feature, Right.Deny);
                            _ikAdminFacade.saveAccessRight(accessRight);
                        } else {
                            handleClassicalWay = true;
                        }
                    }
                }else{
                    handleClassicalWay = true;
                }
                if (handleClassicalWay) {
                    if (feature.needsApproval()) {
                        if (_requestHandler.handleFeatureRequest(account, feature)) {
                            accFeature.setFeatureState(FeatureState.REQUESTED);
                        }
                    } else {
                        accFeature.setFeatureState(FeatureState.SIMPLE);
                    }
                }
            }
        }
        Account managedAccount = merge(account);
        setIKNames(managedAccount);
        return managedAccount;
    }

    public boolean hasIkAdmin(int ik) {
        String sql = "select cast(sign(count(0)) as bit) as hasAdmin from ikadm.mapAccountIkAdmin where aiaIk = " + ik;
        Query query = getEntityManager().createNativeQuery(sql);
        boolean hasIkAdmin = (boolean) query.getSingleResult();
        return hasIkAdmin;
    }

    public void deleteAccount(Account account) {
        if (account.getId() > 0) {
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
        if (!accountRequest.getPasswordHash().equals(Crypt.hashPassword(password, accountRequest.getSalt()))
                || !accountRequest.getActivationKey().equals(activationKey)) {
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
        Account account = find(Account.class, changeMail.getAccountId());
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
        if (!request.getPasswordHash().equals(Crypt.hashPassword(password, request.getSalt()))
                || !request.getActivationKey().equals(activationKey)) {
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

    @Inject private Mailer _mailer;

    public boolean requestPassword(final String mail, final String password) {
        if (StringUtil.isNullOrEmpty(mail) || StringUtil.isNullOrEmpty(password)) {
            return false;
        }
        Account account = findByMailOrUser(mail);
        if (account == null) {
            LOGGER.log(Level.INFO, "Password request for unknown account {0}", mail);
            return false;
        }
        if (account.isDeactivated()) {
            LOGGER.log(Level.INFO, "Password request for deactivated account {0}", mail);
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
        LOGGER.log(Level.WARNING, "Could not send password activation mail for {0}", account.getEmail());
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

    public List<Account> getAccounts4Ik(Integer ik, Set<String> emails) {
        String orEmailCond = emails.isEmpty() ? "" : " or a._email in (" + emails.stream().map(e -> "'" + e + "'")
                .collect(Collectors.joining(", ")) + ") ";
        String jpql = "SELECT DISTINCT a FROM Account a left join AccountAdditionalIK i "
                + "WHERE  a._ik = :ik  or a._id = i._accountId and i._ik = :ik " + orEmailCond
                + "order by a._lastName";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    public List<Account> findAccountsByMailDomain(String mailDomain) {
        String jpql = "Select a from Account a where a._email like :mailDomain";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        query.setParameter("mailDomain", "%" + mailDomain);
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

    public Map<Account, String> obtainRoleInfo(int ik, List<Account> accounts) {
        Map<Account, String> accountRoles = new HashMap<>();
        String addresses = accounts.stream().map(a -> "'" + a.getEmail().toLowerCase() + "'").collect(Collectors.joining(", "));
        if (addresses.isEmpty()) {
            return accountRoles;
        }
        String sql = "select cdDetails, dbo.concatenate(roText)\n"
                + "from CallCenterDB.dbo.ccCustomer\n"
                + "join CallCenterDB.dbo.ccContact on cuId = coCustomerId\n"
                + "join CallCenterDB.dbo.mapContactRole r1 on coId = r1.mcrContactId and r1.mcrRoleId in (3, 12, 14, 15, 16, 18, 19)\n"
                + "join CallCenterDB.dbo.listRole on r1.mcrRoleId = roId\n"
                + "join CallCenterDB.dbo.ccContactDetails on coId = cdContactId\n"
                + "where cuIk = " + ik + "\n"
                + "    and coIsActive = 1\n"
                + "    and cdContactDetailTypeId = 'E'\n"
                + "    and cdDetails in(" + addresses + ")\n"
                + "group by cdDetails";
        Query query = getEntityManager().createNativeQuery(sql);
        Map<String, String> mailRole = new HashMap<>();
        @SuppressWarnings("unchecked") List<Object[]> objects = query.getResultList();
        for (Object[] obj : objects) {
            String email = (String) obj[0];
            String roles = (String) obj[1];
            mailRole.put(email.toLowerCase(), roles);
        }
        for (Account account : accounts) {
            String email = account.getEmail().toLowerCase();
            String roles = mailRole.containsKey(email) ? mailRole.get(email) : "";
            account.setSelected(!roles.isEmpty());
            accountRoles.put(account, roles);
        }
        return accountRoles;
    }

    public List<SelectItem> getInekAgents() {
        List<SelectItem> agents = new ArrayList<>();
        for (Account account : getInekAccounts()) {
            agents.add(new SelectItem(account.getId(), account.getLastName() + ", " + account.getFirstName()));
        }
        return agents;
    }

    public List<Account> getInekAccounts() {
        String sql = "select account.* \n"
                + "from dbo.Account \n"
                + "join CallCenterDB.dbo.ccAgent on agEMail = acMail\n"
                + "where agActive = 1\n" //  and agDomainId in ('O', 'E', 'M')
                + "order by acLastName";
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        @SuppressWarnings("unchecked") List<Account> result = query.getResultList();
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Account> getInekContacts(Account account) {
        Set<Integer> iks = account.getFullIkSet();
        if (iks.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        String sql = "select account.* \n" +
                    "from dbo.Account \n" +
                    "join CallCenterDB.dbo.ccAgent on agEMail = acMail \n" +
                    "join (\n" +
                    "select distinct cciaAgentId \n" +
                    "from CallCenterDB.dbo.ccCustomer \n" +
                    "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId \n" +
                    "join CallCenterDB.dbo.mapCustomerCalcInfoAgent on cciaCustomerCalcInfoId = cciId \n" +
                    "and Year(cciaValidTo) = " + Utils.getTargetYear(Feature.CALCULATION_HOSPITAL) + " \n" +
                    "and cuIK in (" + iks.stream().map(i -> "" + i).collect(Collectors.joining(", ")) + ") \n" +
                    ") d on agId = cciaAgentId \n" +
                    "where agActive = 1 \n" +
                    "order by acLastName";
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        return query.getResultList();
    }

    public List<Account> getIkAdminAccounts() {
        String jpql = "select a from Account a where a._adminIks is not empty";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        List<Account> accounts = query.getResultList();
        return accounts;
    }

    public Account findFreshAccount(int id) {
        return findFresh(Account.class, id);
    }

    public Account findAccount(int id) {
        return find(Account.class, id);
    }

}
