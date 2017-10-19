/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.admin.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountFeature;
import org.inek.dataportal.enums.IkReference;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.feature.admin.dao.IkAccount;
import org.inek.dataportal.feature.ikadmin.entity.AccessRight;
import org.inek.dataportal.feature.ikadmin.enums.Right;
import org.inek.dataportal.feature.ikadmin.facade.IkAdminFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "AdminTask")
public class IkAdmin implements Serializable {

    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;
    @Inject private IkAdminFacade _ikAdminFacade;
    private int _ik;
    private Account _account;
    private String _mailDomain;

    private List<Account> _accounts = new ArrayList<>();

    public List<IkAccount> getIkAccounts() {
        if (_accounts.isEmpty()) {
            _accounts = _accountFacade.getIkAdminAccounts();
        }
        return IkAccount.createFromAccounts(_accounts);
    }

    public String deleteIkAdmin(IkAccount ikAccount) {
        Account account = ikAccount.getAccount();
        int ik = ikAccount.getIk();
        _sessionController.logMessage("Delete IK Admin: account=" + account.getId() + ", ik=" + ik);
        account.removeIkAdmin(ik);
        _accountFacade.merge(account);
        _accounts.clear();  // force reload
        return "";
    }

    public void setInput(IkAccount ikAccount) {
        _account = ikAccount.getAccount();
        _ik = ikAccount.getIk();
        _mailDomain = ikAccount.getMailDomain();
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
        if (_account.addIkAdmin(_ik, _mailDomain)) {
            _sessionController.logMessage("Added IK Admin: account=" + _account.getId() + ", ik=" + _ik);
            collectExistingAccess(_ik);
        }
        _accountFacade.merge(_account);

        _accounts.clear();  // force reload
        return "";
    }
    // </editor-fold>

    /**
     * If for an ik an ik admin is created (and none existed before) then we need to collect existing accesses and store
     * them in the access rights
     */
    private void collectExistingAccess(int ik) {
        List<AccessRight> accessRights = _ikAdminFacade.findAccessRights(ik);
        Set<String> emails = new HashSet<>();
        List<Account> accounts = _accountFacade.getAccounts4Ik(ik, emails);
        for (Account account : accounts) {
            for (AccountFeature feature : account.getFeatures()) {
                if (feature.getFeature().getIkReference() != IkReference.Hospital 
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

}
