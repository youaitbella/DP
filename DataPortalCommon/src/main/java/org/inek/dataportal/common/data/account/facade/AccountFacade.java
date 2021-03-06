package org.inek.dataportal.common.data.account.facade;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.FeatureState;
import org.inek.dataportal.api.enums.IkReference;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.account.entities.*;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.common.User;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.data.ikadmin.facade.IkAdminFacade;
import org.inek.dataportal.common.enums.EnvironmentType;
import org.inek.dataportal.common.enums.Right;
import org.inek.dataportal.common.helper.TransferFileCreator;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.MailTemplateFacade;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.requestmanager.FeatureRequestHandler;
import org.inek.dataportal.common.utils.Crypt;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.common.utils.ObjectUtil;
import org.inek.dataportal.common.utils.StringUtil;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 *
 * @author vohldo
 */
@Stateless
public class AccountFacade extends AbstractDataAccess {

    @Inject
    private AccountPwdFacade _accountPwdFacade;
    @Inject
    private FeatureRequestHandler _requestHandler;
    @Inject
    private AccountRequestFacade _accountRequestFacade;
    @Inject
    private AccountChangeMailFacade _accountChangeMailFacade;
    @Inject
    private PasswordRequestFacade _pwdRequestFacade;
    @Inject
    private ConfigFacade _configFacade;
    @Inject
    private MailTemplateFacade _mailTemplateFacde;

    public AccountFacade() {
    }

    public AccountFacade(EntityManager em) {
        super(em);
    }

    private void deleteOrphanRights(int accountId) {
        String sql = "delete ikadm.AccessRight where arid in (\n"
                + "    select arid \n"
                + "    from ikadm.AccessRight\n"
                + "    left join AccountAdditionalIK on arAccountId = aaiAccountId and arik = aaiik\n"
                + "    where arAccountId = ? and aaiId is null\n"
                + ")";
        deleteOrphanRights(accountId, sql);


        sql = "delete ikadm.AccessRight where arid in (\n"
                + "    select arid \n"
                + "    from ikadm.AccessRight\n"
                + "    left join AccountFeature on arAccountId = afAccountId and arFeatureId = afFeatureId\n"
                + "    where arAccountId = ? and afId is null\n"
                + ")";
        deleteOrphanRights(accountId, sql);
    }

    private void deleteOrphanRights(int accountId, String sql) {
        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter(1, accountId);
        query.executeUpdate();
    }

    public Account findByMailOrUser(String mailOrUser) {
        String query = "SELECT a FROM Account a WHERE a._email = :mailOrUser or a._user = :mailOrUser";
        List<Account> list = getEntityManager().createQuery(query, Account.class).setParameter("mailOrUser", mailOrUser).
                getResultList();
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    public Account findByMail(String mail) {
        String query = "SELECT a FROM Account a WHERE a._email = :mail";
        List<Account> list = getEntityManager().createQuery(query, Account.class).setParameter("mail", mail).
                getResultList();
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
        return account;
    }

    @Inject
    private IkAdminFacade _ikAdminFacade;

    // todo: define access rights for new features instead of approving features.
    // see checkIKAdminRights and create one unified method
    public Account updateAccount(Account account) {
        for (AccountFeature accFeature : account.getFeatures()) {
            if (accFeature.getFeatureState() == FeatureState.NEW) {
                if (accFeature.getFeature().getIkReference() != IkReference.None
                        && updateIfManaged(account, accFeature)) {
                    continue;
                }
                Feature feature = accFeature.getFeature();
                if (feature.getNeedsApproval()) {
                    _requestHandler.handleFeatureRequest(account, feature);
                    accFeature.setFeatureState(FeatureState.REQUESTED);
                } else {
                    accFeature.setFeatureState(FeatureState.SIMPLE);
                }
            }
        }
        Account managedAccount = merge(account);
        deleteOrphanRights(account.getId());
        return managedAccount;
    }

    private boolean updateIfManaged(Account account, AccountFeature accFeature) {
        Feature feature = accFeature.getFeature();
        boolean handleClassicalWay;
        // this feature might be affected by an ik admin
        Set<Integer> fullIkSet = account.getFullIkSet();
        handleClassicalWay = fullIkSet.isEmpty();
        for (int ik : fullIkSet) {
            if (_ikAdminFacade.hasIkAdmin(ik, accFeature.getFeature().getId())) {
                List<AccessRight> formerRights = _ikAdminFacade.
                        findAccessRightsByAccountIkAndFeature(account, ik, feature);
                if (formerRights.isEmpty()) {
                    AccessRight accessRight = new AccessRight(account.getId(), ik, feature, Right.Deny);
                    _ikAdminFacade.saveAccessRight(accessRight);
                }
                accFeature.
                        setFeatureState(feature.getNeedsApproval() ? FeatureState.APPROVED : FeatureState.SIMPLE);
            } else {
                handleClassicalWay = true;
            }
        }
        return !handleClassicalWay;
    }

    public void deleteAccount(Account account) {
        if (account.getId() > 0) {
            Account emptyAccount = new Account();
            emptyAccount.setEmail("deleted"); // write non-emailaddress to indicate this is an deleted account
            emptyAccount.setUser("");
            emptyAccount.setDeactivated(true);
            emptyAccount.setId(account.getId());
            merge(emptyAccount);
            AccountPwd accountPwd = _accountPwdFacade.find(emptyAccount.getId());
            if (accountPwd != null) {
                _accountPwdFacade.delete(accountPwd);
            }
        }
    }

    public boolean activateAccount(String mailOrUser, String password, String activationKey) {
        if (StringUtil.isNullOrEmpty(mailOrUser) || StringUtil.isNullOrEmpty(password) || StringUtil.
                isNullOrEmpty(activationKey)) {
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
        Account account = createAccount(accountRequest);
        merge(account);
        AccountPwd accountPwd = new AccountPwd();
        accountPwd.setAccountId(account.getId());
        String salt = Crypt.getSalt();
        accountPwd.setSalt(salt);
        accountPwd.setPasswordHash(Crypt.hashPassword(password, salt));
        _accountPwdFacade.save(accountPwd);
        _accountRequestFacade.remove(accountRequest);
        TransferFileCreator.createEmailTransferFile(_configFacade, account.getEmail());
        setIkAdminIfOnInekList(account);
        return true;
    }

    private void setIkAdminIfOnInekList(Account acc) {
        try {
            if (accountHasIkAdminRequest(acc)) {

                LOGGER.log(Level.INFO, "Found IK Admin Request: {0}", acc.getEmail());

                List<Feature> features = getFeaturesFromAdminRequest(acc);
                List<AccessRight> rights = new ArrayList<>();

                for (Feature fe : features) {
                    List<Integer> iks = getIksForAdminRequestFeature(acc, fe);
                    for (int ik : iks) {
                        if (!acc.getFullIkSet().contains(ik)) {
                            acc.addIk(ik);
                        }
                        acc.addIkAdmin(ik, "", fe);
                        rights.add(new AccessRight(acc.getId(), ik, fe, Right.All));
                    }
                    acc.addFeature(fe, true);
                }

                for (AccessRight right : rights) {
                    _ikAdminFacade.saveAccessRight(right);
                }

                merge(acc);
            } else {
                LOGGER.log(Level.INFO, "No Admin Request found: {0}", acc.getEmail());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "IK Admin konnte nicht eingetragen werden: {0}", ex.getMessage());
        }
    }

    private List<Integer> getIksForAdminRequestFeature(Account acc, Feature fe) {
        String sql = "select distinct arIk from ikadm.AdminRequest\n"
                + "where arMail = '" + acc.getEmail() + "'\n"
                + "and arFeatureId = '" + fe.getId() + "';";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> results = query.getResultList();

        return results;
    }

    private Boolean accountHasIkAdminRequest(Account acc) {
        String sql = "select * from ikadm.AdminRequest\n"
                + "where arMail = '" + acc.getEmail() + "';";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Object> result = query.getResultList();
        return result.size() > 0;
    }

    private List<Feature> getFeaturesFromAdminRequest(Account acc) {
        String sql = "select distinct arFeatureId from ikadm.AdminRequest\n"
                + "where arMail = '" + acc.getEmail() + "';";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> results = query.getResultList();

        List<Feature> features = new ArrayList<>();

        for (Integer featureId : results) {
            features.add(Feature.getFeatureFromId(featureId));
        }

        return features;
    }

    private Account createAccount(AccountRequest accountRequest) {
        Account account = ObjectUtil.copyObject(Account.class, accountRequest);
        int ik = accountRequest.getIK();
        if (ik > 100000000 && ik < 999999999) {
            account.addIk(ik);
        }
        persist(account);
        return account;
    }

    public Account merge(Account account) {
        return super.merge(account);
    }

    public boolean activateMail(String mail, String password, String activationKey) {
        if (StringUtil.isNullOrEmpty(mail) || StringUtil.isNullOrEmpty(password) || StringUtil.
                isNullOrEmpty(activationKey)) {
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
        if (StringUtil.isNullOrEmpty(mailOrUser) || StringUtil.isNullOrEmpty(password) || StringUtil.
                isNullOrEmpty(activationKey)) {
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
            accountPwd = new AccountPwd();
            accountPwd.setAccountId(account.getId());
        }
        String salt = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        accountPwd.setSalt(salt);
        accountPwd.setPasswordHash(Crypt.hashPassword(password, salt));
        _accountPwdFacade.merge(accountPwd);
        _pwdRequestFacade.remove(request);
        return true;
    }

    @Inject
    private Mailer _mailer;

    public boolean requestPassword(final String mail, final String password) {
        if (StringUtil.isNullOrEmpty(mail) || StringUtil.isNullOrEmpty(password)) {
            return false;
        }
        Account account = findByMailOrUser(mail);
        if (account == null) {
            LOGGER.log(Level.INFO, "Password request for unknown account {0}", mail);
            MailTemplate mailTemplate = _mailTemplateFacde.findByName("PasswordActivationMailError");
            _mailer.sendMailTemplate(mailTemplate, mail);
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

    public List<Account> getAccounts4Ik(int ik) {
        String jpql = "SELECT DISTINCT a FROM Account a join AccountIk i "
                + "WHERE  a._id = i._accountId and i._ik = :ik "
                + "order by a._lastName";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    public List<Account> getAccounts4IkInludeCustomer(int ik) {
        String sql = "select distinct a.* \n"
                + "from dbo.Account a\n"
                + "left join dbo.AccountAdditionalIK on acId = aaiAccountId\n"
                + "where aaiIK = " + ik + "\n"
                + "  or acMail in (\n"
                + "     select cdDetails\n"
                + "     from CallCenterDb.dbo.ccCustomer\n"
                + "     join CallCenterDB.dbo.ccContact on cuId = coCustomerId and coIsActive = 1\n"
                + "     join CallCenterDB.dbo.ccContactDetails on coId = cdContactId and cdContactDetailTypeId = 'E'\n"
                + "     where cuIk = " + ik + "\n"
                + " )\n"
                + "order by acLastName";
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        @SuppressWarnings("unchecked")
        List<Account> result = query.getResultList();
        return result;
    }

    public List<Account> findAccountsByMailDomain(String mailDomain) {
        String jpql = "Select a from Account a where a._email like :mailDomain";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        query.setParameter("mailDomain", "%" + mailDomain);
        return query.getResultList();
    }

    public void obtainRoleInfo(int ik, Collection<Account> accounts) {
        Map<Account, String> accountRoles = new HashMap<>();
        String emails = accounts
                .stream()
                .map(a -> "'" + a.getEmail().toLowerCase() + "'")
                .collect(Collectors.joining(", "));
        if (emails.isEmpty()) {
            return;
        }
        int year = DateUtils.currentMonth() == 1 ? DateUtils.currentYear() - 2 : DateUtils.currentYear() - 1;
        String sql = "select lower(coMail), "
                + "IIF(coIsDrg = 1, 'DRG ', '') + IIF(coIsPsy = 1, 'PSY ', '') + IIF(coIsInv = 1, 'INV ', '') "
                + "+ IIF(coIsTpg = 1, 'TPG ', '') + IIF(coIsObd = 1, 'OBD ', '') + IIF(coIsConsultant = 1, 'Berater', '')\n"
                + "from calc.StatementOfParticipance\n"
                + "join calc.Contact on sopId = coStatementOfParticipanceId\n"
                + "where sopStatusId < 200\n"
                + "    and sopik = " + ik + " \n"
                + "    and sopDataYear = " + year + " \n"
                + "    and coMail in (" + emails + ")";
        Query query = getEntityManager().createNativeQuery(sql);
        Map<String, String> mailRole = new HashMap<>();
        @SuppressWarnings("unchecked")
        List<Object[]> objects = query.getResultList();
        for (Object[] obj : objects) {
            String email = (String) obj[0];
            String roles = (String) obj[1];
            mailRole.put(email, roles);
        }
        for (Account account : accounts) {
            String email = account.getEmail().toLowerCase();
            String roles = mailRole.containsKey(email) ? mailRole.get(email) : "";
            account.setSelected(!roles.isEmpty());
            account.setCalcRoles(roles);
        }
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
        @SuppressWarnings("unchecked")
        List<Account> result = query.getResultList();
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Account> getInekContacts(Account account) {
        Set<Integer> iks = account.getFullIkSet();
        if (iks.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        String sql = "select account.* \n"
                + "from dbo.Account \n"
                + "join CallCenterDB.dbo.ccAgent on agEMail = acMail \n"
                + "join (\n"
                + "select distinct cciaAgentId \n"
                + "from CallCenterDB.dbo.ccCustomer \n"
                + "join CallCenterDB.dbo.CustomerCalcInfo on cuId = cciCustomerId \n"
                + "join CallCenterDB.dbo.mapCustomerCalcInfoAgent on cciaCustomerCalcInfoId = cciId \n"
                + "and Year(cciaValidTo) = " + Utils.getTargetYear(Feature.CALCULATION_HOSPITAL) + " \n"
                + "and cuIK in (" + iks.stream().map(i -> "" + i).collect(Collectors.joining(", ")) + ") \n"
                + ") d on agId = cciaAgentId \n"
                + "where agActive = 1 \n"
                + "order by acLastName";
        Query query = getEntityManager().createNativeQuery(sql, Account.class);
        return query.getResultList();
    }

    public List<Account> getIkAdminAccounts() {
        String jpql = "select a from Account a where a._adminIks is not empty";
        TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
        List<Account> accounts = query.getResultList();
        return accounts;
    }

    public List<User> findIkAdmins(int ik, Feature feature) {
        TypedQuery<User> query = getEntityManager().createNamedQuery("User.findIkAdmins", User.class);
        query.setParameter("ik", ik);
        query.setParameter("feature", feature);
        dumpSql(query);
        return query.getResultList();
    }

    public List<User> findUsersWithRights(int ik, Feature feature, List<Right> rights) {
        TypedQuery<User> query = getEntityManager().createNamedQuery("User.findUsersWithRights", User.class);
        query.setParameter("ik", ik);
        query.setParameter("feature", feature);
        query.setParameter("rights", rights);
        return query.getResultList();
    }

    public Account findFreshAccount(int id) {
        return findFresh(Account.class, id);
    }

    public Account findAccount(int id) {
        return find(Account.class, id);
    }

    public boolean deleteIkAllowed(int ik, Account acc) {
        String sql = "select distinct 1 from calc.KGLBaseInformation\n"
                + "where biAccountID = " + acc.getId() + "\n"
                + "and biIK = " + ik + "\n"
                + "union\n"
                + "select distinct 1 from calc.KGPBaseInformation\n"
                + "where biAccountID = " + acc.getId() + "\n"
                + "and biIK = " + ik + "\n"
                + "union\n"
                + "select distinct 1 from calc.StatementOfParticipance\n"
                + "where sopAccountId = " + acc.getId() + "\n"
                + "and sopIk = " + ik + "\n"
                + "union\n"
                + "select 1 from calc.DistributionModelMaster\n"
                + "where dmmAccountId = " + acc.getId() + "\n"
                + "and dmmIk = " + ik + "\n"
                + "union\n"
                + "select 1 from vr.ValuationRatio\n"
                + "where vrAccountId = " + acc.getId() + "\n"
                + "and vrIK = " + ik + "\n"
                + "union\n"
                + "select 1 from spf.RequestMaster\n"
                + "where rmAccountId = " + acc.getId() + "\n"
                + "and rmIK = " + ik + "\n"
                + "union\n"
                + "select 1 from spf.AgreementMaster\n"
                + "where amAccountId = " + acc.getId() + "\n"
                + "and amIK = " + ik + "\n"
                + "union\n"
                + "select 1 from psy.StaffProofMaster\n"
                + "where spmAccountId = " + acc.getId() + "\n"
                + "and spmIK = " + ik + "\n"
                + "union\n"
                + "select 1 from dbo.NubProposal\n"
                + "where nubAccountId = " + acc.getId() + "\n"
                + "and nubIk = " + ik + "";

        Query query = getEntityManager().createNativeQuery(sql);
        return !(query.getResultList().size() > 0);
    }

    public boolean isAllowedForTest(int accountId, int featureId) {
        String sql = "select distinct 1\n"
                + "from adm.AccountsAllowedForTest\n"
                + "where aaftAccountId = " + accountId + "\n"
                + "and aaftFeatureId = " + featureId + "";

        Query query = getEntityManager().createNativeQuery(sql);
        return query.getResultList().size() > 0;
    }

    public void countUserEnvironment(EnvironmentType type, String content) {
        try {
            String sql = "merge adm.UserEnvironment e\n"
                    + "using (select left(convert(nvarchar, getDate(), 112), 6) as datestring, ? as typ, ? as content) as s\n"
                    + "on e.ueDate = datestring and ueType = typ and ueContent = content\n"
                    + "when matched then \n"
                    + "   update set ueCount = ueCount+1\n"
                    + "when not matched then \n"
                    + "  insert values(datestring, typ, content, 1);";
            Query query = getEntityManager().createNativeQuery(sql);
            query.setParameter(1, type.name());
            query.setParameter(2, content);
            query.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            // ignore
        }
    }
}
