/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.admin.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.IkReference;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.admin.dao.IkAccount;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.ManagedBy;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.enums.Right;
import org.inek.dataportal.common.data.account.entities.AccountFeature;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.data.ikadmin.entity.IkAdminFeature;
import org.inek.dataportal.common.data.ikadmin.facade.IkAdminFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScoped;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class IkAdmin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private SessionController _sessionController;
    @Inject
    private AccountFacade _accountFacade;
    @Inject
    private IkAdminFacade _ikAdminFacade;

    private int _ik;
    private Account _account;
    private String _mailDomain;

    private List<IkAccount> _adminAccounts = new ArrayList<>();
    private List<Feature> _validFeatures = new ArrayList<>();
    private List<Feature> _selectedFeatures = new ArrayList<>();

    public List<Feature> getValidFeatures() {
        return _validFeatures;
    }

    public void setValidFeatures(List<Feature> validFeatures) {
        this._validFeatures = validFeatures;
    }

    public List<Feature> getSelectedFeatures() {
        return _selectedFeatures;
    }

    public void setSelectedFeatures(List<Feature> selectedFeatures) {
        this._selectedFeatures = selectedFeatures;
    }

    public List<IkAccount> getAdminAccounts() {
        return _adminAccounts;
    }

    public void setAdminAccounts(List<IkAccount> adminAccounts) {
        this._adminAccounts = adminAccounts;
    }

    @PostConstruct
    private void init() {
        createAdminAccountList();
        createValidFeaturesList();
    }

    public String deleteIkAdmin(IkAccount ikAccount) {
        Account account = ikAccount.getAccount();
        int ik = ikAccount.getIk();
        _sessionController.logMessage("Delete IK Admin: account=" + account.getId() + ", ik=" + ik);
        account.removeIkAdmin(ik);
        _accountFacade.merge(account);
        createAdminAccountList();
        return "";
    }

    public void setInput(IkAccount ikAccount) {
        _account = ikAccount.getAccount();
        _ik = ikAccount.getIk();
        _mailDomain = ikAccount.getMailDomain();
        _selectedFeatures.clear();
        for (IkAdminFeature ikAdminFeature : ikAccount.getIkAdminFeatures()) {
            _selectedFeatures.add(ikAdminFeature.getFeature());
        }
    }

    public String getEmail() {
        return _account == null ? "" : _account.getEmail();
    }

    public void setEmail(String email) {
        _account = _accountFacade.findByMailOrUser(email);
    }

    public void checkAccountId(FacesContext context, UIComponent component, Object value) {
        Account account = _accountFacade.findAccount((int) value);
        if (account == null) {
            String msg = Utils.getMessage("errUnknownAccount");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void setAccountId(int accountId) {
        _account = _accountFacade.findAccount(accountId);
    }

    public void setAccount(Account account) {
        _account = account;
    }

    public Account getAccount() {
        return _account;
    }

    public int getAccountId() {
        return _account == null ? 0 : _account.getId();
    }

    public String getMailDomain() {
        return _mailDomain;
    }

    public void setMailDomain(String mailDomain) {
        _mailDomain = mailDomain;
    }

    public String saveIkAdmin() {
        if (_account.updateIkAdmin(_ik, _mailDomain, _selectedFeatures)) {
            _sessionController.logMessage("Added IK Admin: account=" + _account.getId() + ", ik=" + _ik);
            collectExistingAccess(_ik);
        }

        if (!_account.getFullIkSet().contains(_ik)) {
            _account.addIk(_ik);
        }

        for (Feature fe : _selectedFeatures.stream()
                .filter(c -> c.getManagedBy() == ManagedBy.IkAdminOnly)
                .collect(Collectors.toList())) {
            if (_account.getFeatures().stream().noneMatch(f -> f.getFeature() == fe)) {
                _account.addFeature(fe, true);
                AccessRight accessRight = new AccessRight(_account.getId(), _ik, fe, Right.All);
                _ikAdminFacade.saveAccessRight(accessRight);
            }
        }

        _accountFacade.merge(_account);
        DialogController.showSaveDialog();
        createAdminAccountList();
        return "";
    }
    // </editor-fold>

    /**
     * If for an ik an ik admin is created (and none existed before) then we need to collect existing accesses and store them in the access rights
     */
    private void collectExistingAccess(int ik) {
        List<AccessRight> accessRights = _ikAdminFacade.findAccessRights(ik);
        List<Account> accounts = _accountFacade.getAccounts4Ik(ik);
        for (Account account : accounts) {
            for (AccountFeature feature : account.getFeatures()) {
                if (feature.getFeature().getIkReference() == IkReference.None
                        || accessRights
                                .stream()
                                .anyMatch(ar -> ar.getAccountId() == account.getId() && ar.getFeature() == feature.getFeature())) {
                    continue;
                }
                AccessRight accessRight = new AccessRight(account.getId(), ik, feature.getFeature(), Right.All);
                _ikAdminFacade.saveAccessRight(accessRight);
            }
        }
    }

    public void checkEmail(FacesContext context, UIComponent component, Object value) {
        String email = (String) value;
        Account account = _accountFacade.findByMailOrUser(email);
        if (account == null) {
            String msg = Utils.getMessage("errUnknownEmail");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void setIk(Integer ik) {
        if (ik == null) {
            ik = 0;
        }
        _ik = ik;
    }

    public Integer getIk() {
        return _ik > 0 ? _ik : null;
    }

    private void createAdminAccountList() {
        List<Account> ikAdminAccounts = _accountFacade.getIkAdminAccounts();
        setAdminAccounts(IkAccount.createFromAccounts(ikAdminAccounts));
    }

    private void createValidFeaturesList() {
        _validFeatures.clear();
        _validFeatures.addAll(Arrays.asList(Feature.values()).stream()
                .filter(c -> c.getManagedBy() != ManagedBy.None)
                .collect(Collectors.toList()));
    }
}
