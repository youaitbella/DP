package org.inek.dataportal.feature.cooperation;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountFeature;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.enums.Genders;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.common.data.cooperation.facade.CooperationFacade;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRequestEmailFacade;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRequestFacade;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.mail.MailTemplateFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.helper.faceletvalidators.EmailValidator;
import org.inek.dataportal.common.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class CooperationManager {

    @Inject private CooperationRequestFacade _cooperationRequestFacade;
    @Inject private CooperationRequestEmailFacade _cooperationRequestEmailFacade;
    @Inject private CooperationFacade _cooperationFacade;
    @Inject private AccountFacade _accountFacade;
    @Inject private SessionController _sessionController;
    @Inject private PortalMessageFacade _messageFacade;
    @Inject private Mailer _mailer;
    @Inject private MailTemplateFacade _mtFacade;

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
            sendInvite(requested);
        } else {
            sendInviteRegister();
        }
        _sessionController.alertClient(Utils.getMessage("msgInvitation"));
        _userOrMail = "";
        return "";
    }

    private void sendInviteRegister() {
        if(EmailValidator.isValidEmail(_userOrMail)) {
            Account myAccount = _sessionController.getAccount();
            _cooperationRequestEmailFacade.createCooperation(myAccount.getId(), _userOrMail);
            MailTemplate mail = _mtFacade.findByName("CooperationRequestRegister");
            mail.setBody(mail.getBody().replace("{senderName}", myAccount.getFirstName() + " " + myAccount.getLastName()));
            mail.setBody(mail.getBody().replace("{senderMail}", myAccount.getEmail()));
            _mailer.sendMail(_userOrMail, mail.getSubject(), mail.getBody());
        }
    }

    private void sendInvite(Account requested) {
        Account myAccount = _sessionController.getAccount();
        if (!_cooperationFacade.existsCooperation(myAccount.getId(), requested.getId())) {
            _cooperationRequestFacade.createCooperationRequest(myAccount.getId(), requested.getId());
        }
        AccountFeature coop = new AccountFeature(Feature.COOPERATION);
        if (!requested.getFeatures().contains(coop)) {
            // Check if requested user has coop enabled. If not send an e-mail to the user.
            MailTemplate mail = _mtFacade.findByName("CooperationRequest");
            String salutation = "Sehr ";
            if (requested.getGender() == Genders.Male.id()) {
                salutation += "geehrter Herr ";
            } else {
                salutation += "geehrte Frau ";
            }
            salutation += requested.getLastName();
            mail.setBody(mail.getBody().replace("{salutation}", salutation));
            mail.setBody(mail.getBody().replace("{senderName}", myAccount.getFirstName() + " " + myAccount.getLastName()));
            mail.setBody(mail.getBody().replace("{senderMail}", myAccount.getEmail()));
            _mailer.sendMail(requested.getEmail(), mail.getSubject(), mail.getBody());
        }
    }

    public String managePartner(int partnerId) {
        if (partnerId == _sessionController.getAccountId()) {
            return "";
        }
        Utils.getFlash().put("partnerId", partnerId);
        return Pages.CooperationEditPartner.URL();
    }

    public String unreadMessages(int partnerId) {
        int count = _messageFacade.countUnreadMessages(_sessionController.getAccountId(), partnerId);
        if (count == 0) {
            return "";
        }

        return "(" + count + " " + Utils.getMessage("lblNewFemale") + " "
                + (count == 1 ? Utils.getMessage("lblMessage") : Utils.getMessage("lblMessages")) + ")";
    }

}
