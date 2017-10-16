/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.admin.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.feature.admin.dao.IkAccount;
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
        Account account = _accountFacade.find((int) value);
        if (account == null) {
            String msg = Utils.getMessage("errUnknownAccount");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void setAccountId(int accountId) {
        _account = _accountFacade.find(accountId);
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
            collectExistingAccess();
        }
        _accountFacade.merge(_account);
        
        _accounts.clear();  // force reload
        return "";
    }
    // </editor-fold>

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

    private void collectExistingAccess() {
    }

}
