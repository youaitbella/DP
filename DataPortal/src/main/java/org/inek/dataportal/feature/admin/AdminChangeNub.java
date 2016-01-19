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
import org.inek.dataportal.entities.nub.NubRequest;
import org.inek.dataportal.enums.DataSet;
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

    public void setIk(Integer ik) {
        if (ik == null){
            ik = 0;
        }
        if (_ik != ik){
            updateAccounts(ik);
        }
        _ik = ik;
    }

    public Integer getIk() {
        return _ik > 0 ? _ik : null ;
    }

    private void updateAccounts(int ik) {
        _accountInfos = _nubFacade.getAccountInfos(ik);
    }

    public String changeOwner(){
        _accountInfos.stream().filter(ai -> ai.isSelected()).forEach(ai -> changeOwnerForAccount(ai.getAccount()));
        updateAccounts(_ik);
        return "";
    }
    
    private void changeOwnerForAccount(Account account){
        List<NubRequest> nubRequests = _nubFacade.findAll(account.getId(), _ik, -1, DataSet.All, "");
        for (NubRequest nubRequest : nubRequests){
            nubRequest.setAccountId(_account.getId());
            _nubFacade.saveNubRequest(nubRequest);
            _sessionController.logMessage("NubOwner changed: nub=" + nubRequest.getId() + ", oldOwner=" + account.getId() + ", newOwner=" + _account.getId());
        }
    }
}
