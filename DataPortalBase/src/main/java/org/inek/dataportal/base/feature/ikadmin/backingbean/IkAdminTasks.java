package org.inek.dataportal.base.feature.ikadmin.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.IkReference;
import org.inek.dataportal.api.enums.IkUsage;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.common.User;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.data.ikadmin.entity.AccountResponsibility;
import org.inek.dataportal.common.data.ikadmin.facade.IkAdminFacade;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.Right;
import org.inek.dataportal.common.helper.AccessRightHelper;
import org.inek.dataportal.common.helper.Utils;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class IkAdminTasks implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger("IkAdminTasks");
    private final Map<String, List<AccountResponsibility>> _responsibleForIks = new HashMap<>();
    @Inject
    private SessionController _sessionController;
    @Inject
    private IkAdminFacade _ikAdminFacade;
    @Inject
    private AccountFacade _accountFacade;
    private List<AccessRight> _accessRights;
    private int _ik;
    private int _accountId;
    private Account _account;
    private List<Account> _accounts = new ArrayList<>();
    private int _featureId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
        setAccount(_accountFacade.findAccount(accountId));
    }

    public List<Account> getAccounts() {
        return _accounts
                .stream()
                .sorted((p1, p2) -> p1.getLastName().compareTo(p2.getLastName()))
                .collect(Collectors.toList());
    }

    public void setAccounts(List<Account> accounts) {
        this._accounts = accounts;
    }

    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        this._account = account;
    }

    public int getIk() {
        return _ik;
    }

    public List<Right> getRights() {
        return Arrays.stream(Right.values()).filter(r -> !r.name().equals("Take")).collect(Collectors.toList());
    }

    public List<AccessRight> getAccessRights() {
        return _accessRights;
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String ikParam = "" + params.get("ik");
        try {
            int ik = Integer.parseInt(ikParam);
            if (_sessionController.getAccount().getAdminIks().stream().anyMatch(a -> a.getIk() == ik)) {
                _ik = ik;
                List<Feature> features = obtainManageableFeatures(ik);
                _accessRights = _ikAdminFacade.findAccessRights(_ik, features);
                buildAccountList();
                return;
            }
        } catch (NumberFormatException ex) {
            // ignore here and handle after catch
        }
        Utils.navigate(Pages.NotAllowed.RedirectURL());
    }

    private List<Feature> obtainManageableFeatures(int ik) {
        List<Feature> features = _sessionController
                .getAccount()
                .getAdminIks()
                .stream()
                .filter(a -> a.getIk() == ik)
                .findAny()
                .get()
                .getIkAdminFeatures()
                .stream()
                .map(af -> af.getFeature())
                .filter(f -> f.getIkReference() != IkReference.None)
                .collect(Collectors.toList());
        return features;
    }

    public Boolean getContainsResponsibility() {
        return _accessRights.stream().
                anyMatch(r -> r.getFeature().getIkUsage() == IkUsage.ByResponsibilityAndCorrelation);
    }

    public List<AccessRight> getResponsibilities() {
        return _accessRights
                .stream()
                .filter(r -> r.getRight() != Right.Deny)
                .filter(r -> r.getFeature().getIkUsage() == IkUsage.ByResponsibilityAndCorrelation)
                .collect(Collectors.toList());
    }

    private String buildKey(int accountId, Feature feature, int ik) {
        return accountId + "|" + feature.name() + "|" + ik;
    }

    public List<AccountResponsibility> obtainIkList(int accountId, Feature feature) {
        String key = buildKey(accountId, feature, _ik);
        if (!_responsibleForIks.containsKey(key)) {
            List<AccountResponsibility> responsibleForIks = _ikAdminFacade.
                    obtainAccountResponsibilities(accountId, feature, _ik);
            _responsibleForIks.put(key, responsibleForIks);
        }
        return _responsibleForIks.get(key);
    }

    public void deleteIk(int accountId, Feature feature, AccountResponsibility responsibility) {
        String key = buildKey(accountId, feature, _ik);
        _responsibleForIks.get(key).remove(responsibility);
    }

    public void addIk(int accountId, Feature feature) {
        String key = buildKey(accountId, feature, _ik);
        _responsibleForIks.get(key).add(new AccountResponsibility(accountId, feature, _ik, 0));
    }

    public String saveResponsibilities() {

        StringBuilder errorMessages = new StringBuilder();

        if (AccessRightHelper.responsibilitiesHasNotToMuchUsers(_responsibleForIks, errorMessages)) {
            _ikAdminFacade.saveResponsibilities(_responsibleForIks);
            DialogController.showSaveDialog();
            return "";
        }
        else {
            DialogController.showWarningDialog("Fehler beim speichern", errorMessages.toString());
            return "";
        }
    }

    public String saveAccessRights() {
        try {
            StringBuilder errorMessages = new StringBuilder();
            if (!AccessRightHelper.accessWriteHasNotToMuchUsers(_accessRights, errorMessages)
                    || !AccessRightHelper.accessWriteHasMinOneWithAccesRigth(_accessRights, errorMessages)) {
                DialogController.showWarningDialog("Fehler beim speichern", errorMessages.toString());
            } else {
                for (AccessRight accessRight : _accessRights) {
                    _ikAdminFacade.saveAccessRight(accessRight);
                }
                DialogController.showSaveDialog();
            }
            return null;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            return Pages.Error.URL();
        }
    }

    public int getFeatureId() {
        return _featureId;
    }

    public void setFeatureId(int featureId) {
        _featureId = featureId;
    }

    public Set<Feature> getMissingFeatures() {
        Set<Feature> features = new HashSet<>();
        if (_account == null) {
            return features;
        }
        for (Feature feature : obtainManageableFeatures(_ik)) {
            if (_accessRights.stream().anyMatch(r -> r.getAccountId() == _account.getId() && r.getFeature() == feature)) {
                continue;
            }
            features.add(feature);
        }
        return features;
    }

    public void addAccessRight() {
        if (_account == null || _featureId == 0) {
            return;
        }

        Feature feature = Feature.getFeatureFromId(_featureId);
        User user = new User(_account);

        AccessRight accessRight;

        if (AccessRightHelper.userCanGetAllRight(_accessRights, feature, _ik)) {
            accessRight = new AccessRight(user, _ik, feature, Right.All);
        } else {
            accessRight = new AccessRight(user, _ik, feature, Right.Deny);
        }

        accessRight = _ikAdminFacade.saveAccessRight(accessRight);
        _accessRights.add(accessRight);
        if (!_account.getFullIkSet().contains(_ik)) {
            _account.addIk(_ik);
        }
        if (_account.getFeatures().stream().noneMatch(f -> f.getFeature() == feature)) {
            _account.addFeature(feature, true);
        }
        _accountFacade.updateAccount(_account);
        _featureId = 0;

        DialogController.showSaveDialog();
    }
    // </editor-fold>

    private void buildAccountList() {
        _accounts.clear();

        _accounts.addAll(_accountFacade.getAccounts4Ik(_ik));

        String[] mailDomains = _sessionController
                .getAccount()
                .getAdminIks()
                .stream()
                .filter(a -> a.getIk() == _ik)
                .map(a -> a.getMailDomain())
                .findAny()
                .orElse("")
                .split(";");

        for (String mailDomain : mailDomains) {
            String domain = mailDomain.trim();
            _accounts.addAll(_accountFacade.findAccountsByMailDomain((domain.startsWith("@") ? "" : "@") + domain)
                    .stream()
                    .filter(c -> !c.getFullIkSet().contains(_ik))
                    .collect(Collectors.toList()));
        }
    }

}
