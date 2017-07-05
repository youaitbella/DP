/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.admin.backingbean;

import java.io.Serializable;
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

    public List<IkAccount> getIkAccounts() {
        List<Account> accounts = _accountFacade.getIkAdminAccounts();
        return IkAccount.createFromAccounts(accounts);
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

    public String saveIkSupervisor() {
//        _sessionController.logMessage("Create IK supervisor: account=" + _account.getId()
//                + ", feature=" + _feature + ", ik=" + _ik + ", right=" + _cooperativeRight.name());
//        _cooperationRightFacade.createIkSupervisor(_feature, _ik, _account.getId(), _cooperativeRight);
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

    public String deleteIkAdmin(Account account, int ik) {
        _sessionController.logMessage("Delete IK Admin: account=" + account.getId() + ", ik=" + ik);
        //todo: _account.removeAdminIk(ik);
        return "";
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
