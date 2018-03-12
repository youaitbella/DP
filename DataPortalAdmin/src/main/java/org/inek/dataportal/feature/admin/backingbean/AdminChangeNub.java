package org.inek.dataportal.feature.admin.backingbean;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.data.AccountInfo;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.feature.admin.facade.AdminFacade;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "AdminTask")
public class AdminChangeNub implements Serializable {

    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;
    @Inject private AdminFacade _adminFacade;
    private int _ik;
    private Account _account;
    private List<AccountInfo> _accountInfos = Collections.emptyList();

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
        _account = _accountFacade.findAccount(accountId);
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
        Account account = _accountFacade.findAccount((int) value);
        if (account == null) {
            String msg = Utils.getMessage("errUnknownAccount");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void setIk(Integer ik) {
        if (ik == null) {
            ik = 0;
        }
        if (_ik != ik) {
            updateAccounts(ik);
        }
        _ik = ik;
    }

    public Integer getIk() {
        return _ik > 0 ? _ik : null;
    }

    private void updateAccounts(int ik) {
        _accountInfos = _adminFacade.retrieveNubAccountInfos(ik);
    }

    public String changeOwner() {
        _accountInfos.stream().filter(ai -> ai.isSelected()).forEach(ai -> changeOwnerForAccount(ai.getAccount()));
        updateAccounts(_ik);
        return "";
    }

    private void changeOwnerForAccount(Account oldAccount) {
        _adminFacade.changeNubOwner(_ik, oldAccount.getId(), _account.getId(), _sessionController.getAccountId());
    }
}
