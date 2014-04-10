package org.inek.dataportal.feature.cooperation;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.CooperationFacade;
import org.inek.dataportal.facades.CooperationRequestFacade;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class CooperationManager {

    @Inject CooperationRequestFacade _cooperationRequestFacade;
    @Inject CooperationFacade _cooperationFacade;
    @Inject AccountFacade _accountFacade;
    @Inject SessionController _sessionController;
    private List<Account> _requestors;
    private List<Account> _partners;
    private String _userOrMail;

    public List<Account> getRequestors() {
        if (_requestors == null) {
            _requestors = _cooperationRequestFacade.getCooperationRequestors(_sessionController.getAccountId());
        }
        return _requestors;
    }

    public List<Account> getPartners() {
        if (_partners == null) {
            _partners = _cooperationFacade.getCooperationPartners(_sessionController.getAccountId());
        }
        return _partners;
    }

    public String getUserOrMail() {
        return _userOrMail;
    }

    public void setUserOrMail(String userOrMail) {
        _userOrMail = userOrMail;
    }

    public String invite() {
        Account requested = _accountFacade.findByMailOrUser(_userOrMail);
        Account AccountNotExists = null;
        if (requested != AccountNotExists) {
            Account myAccount = _sessionController.getAccount();
            if (!_cooperationFacade.existsCooperation(myAccount.getAccountId(), requested.getAccountId())) {
                _cooperationRequestFacade.createCooperationRequest(myAccount.getAccountId(), requested.getAccountId());
            }
        }
        _sessionController.alertClient(Utils.getMessage("msgInvitation"));
        _userOrMail="";
        return "";
    }

    public String managePartner(int partnerId) {
        Utils.getFlash().put("partnerId", partnerId);
        return Pages.CooperationEditPartner.URL();
    }
}
