/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.admin;

import java.util.Collections;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "AdminTask")
public class AdminChangeNub {

    @Inject private SessionController _sessionController;
    @Inject AccountFacade _accountFacade;
    @Inject NubRequestFacade _nubFacade;
    private int _ik;
    private Account _account;
    private List<AccountInfo> _accountInfos = Collections.EMPTY_LIST;

    public List<AccountInfo> getAccountInfos() {
        return _accountInfos;
    }

    public void setAccountInfos(List<AccountInfo> accountInfos) {
        _accountInfos = accountInfos;
    }

    public void setAccount(Account account) {
        _account = account;
    }

    public Account getAccount() {
        return _account;
    }

    public String getEmail() {
        return _account == null ? "" : _account.getEmail();
    }

    public void setEmail(String email) {
        _account = _accountFacade.findByMailOrUser(email);
    }

    public void setAccountId(int accountId) {
        _account = _accountFacade.find(accountId);
    }


    public int getAccountId() {
        return _account == null ? 0 : _account.getId();
    }

    public void checkEmail(FacesContext context, UIComponent component, Object value) {
        String email = (String) value;
        Account account = _accountFacade.findByMailOrUser(email);
        if (account == null) {
            String msg = Utils.getMessage("errUnknownEmail");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkAccountId(FacesContext context, UIComponent component, Object value) {
        Account account = _accountFacade.find((int) value);
        if (account == null) {
            String msg = Utils.getMessage("errUnknownAccount");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void setIk(int ik) {
        if (_ik != ik){
            updateAccounts(ik);
        }
        _ik = ik;
        
    }

    public int getIk() {
        return _ik;
    }

    private void updateAccounts(int ik) {
        _accountInfos = _nubFacade.getAccountInfos(ik);
    }

    public String changeOwner(){
        return "";
    }
}
