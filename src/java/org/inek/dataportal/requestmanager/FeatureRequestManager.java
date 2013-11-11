package org.inek.dataportal.requestmanager;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.entities.*;
import org.inek.dataportal.enums.FeatureState;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.AccountFacade;
import org.inek.dataportal.facades.AccountFeatureRequestFacade;
import org.inek.dataportal.facades.ContactRoleFacade;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class FeatureRequestManager implements Serializable {

    private static final Logger _logger = Logger.getLogger("FeatureRequestManager");
    private static final long serialVersionUID = 1L;
    @EJB
    private AccountFacade _accountFacade;
    @EJB
    private AccountFeatureRequestFacade _featureRequestFacade;
    @EJB
    private CustomerFacade _customerFacade;
    @EJB
    private ContactRoleFacade _roleFacade;
    private AccountFeatureRequest _request;
    private Account _account;

    @PostConstruct
    private void init() {
        System.out.println("init");
        HttpServletRequest r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String key = r.getParameter("key");
        if(Utils.getFlash().containsKey("key")) {
           key = Utils.getFlash().get("key").toString();
        }
        Utils.getFlash().put("key", key);
        _request = _featureRequestFacade.findByApprovalKey(key);
        if (_request == null) {
            _account = null;
        } else {
            _account = _accountFacade.find(_request.getAccountId());
            if (_account.getUser().isEmpty()) {
                _account = null;
            }
        }
    }

    public Account getAccount() {
        return _account;
    }

    public String getRole() {
        if (_account == null) {
            return "";
        }
        ContactRole role = _roleFacade.find(_account.getRoleId());
        return role.getText();
    }

    public String approve() {
        if (_account != null) {
            setNewState(FeatureState.APPROVED);
            sendApprovalMail(_account, _request);
            _accountFacade.clearCache(Account.class);
            return Pages.AdminApproved.URL();
        }
        return Pages.AdminError.URL();
    }

    public String reject() {
        if (_account != null) {
            setNewState(FeatureState.REJECTED);
            sendRejectMail(_account, _request);
            _accountFacade.clearCache(Account.class);
            return Pages.AdminApproved.URL();
        }
        return Pages.AdminError.URL();
    }

    private void setNewState(FeatureState newState) {
        for (AccountFeature feature : _account.getFeatures()) {
            if (feature.getFeatureState() == FeatureState.REQUESTED) {
                feature.setFeatureState(newState);
            }
        }
        _accountFacade.merge(_account);
        _featureRequestFacade.remove(_request);
    }

    public String getIkKnown() {
        if (_account == null) {
            return "";
        }
        Integer ik = _account.getIK();
        if (ik == null) {
            return "---";
        }
        Customer cust = _customerFacade.getCustomerByIK(ik);
        if (cust == null) {
            return " (New IK)";
        }
        return " (IK well known in ICMT)";
    }

    private void sendApprovalMail(Account account, AccountFeatureRequest featureRequest) {
        // todo: replace hardecoded info by configuration
        String body = "Sehr geehrte" + (account.getGender() == 1 ? " Frau " : "r Herr ") + account.getLastName() + ",\r\n\r\n";
        body += "entsprechend Ihrer Anfrage haben wir Ihre Daten geprüft und das DropBox-Verfahren für Sie freigeschaltet.\r\n";
        body += "Ab sofort können Sie Daten via DropBox, wahlweise mittels DatenDienst oder über das DatenPortal übermitteln.\r\n";
        body += "\r\nMit freundlichen Grüßen\r\n";
        body += "Ihre InEK GmbH\r\n";
        String subject = Utils.getMessage("headerFeatureApproval");
        try {
            Mailer.sendMail(account.getEmail(), "PortalAnmeldung@datenstelle.de", subject, body);
        } catch (MessagingException ex) {
            _logger.log(Level.SEVERE, null, ex);
        }
    }

    private void sendRejectMail(Account account, AccountFeatureRequest featureRequest) {
        String body = "Sehr geehrte" + (account.getGender() == 1 ? " Frau " : "r Herr ") + account.getLastName() + ",\r\n\r\n";
        body += "entsprechend Ihrer Anfrage haben wir Ihre Daten geprüft. Entsprechend derzeitigem Sachstand war es jedoch nicht möglich, das DropBox-Verfahren für Sie frei zu schalten.\r\n";
        body += "Sollten Sie dennoch das DropBox-Verfahren benötigen, so begründen Sie uns dieses bitte per Email an anfragen@datenstelle.de.\r\n";
        body += "\r\nMit freundlichen Grüßen\r\n";
        body += "Ihre InEK GmbH\r\n";
        String subject = Utils.getMessage("headerFeatureApproval");
        try {
            Mailer.sendMail(account.getEmail(), "PortalAnmeldung@datenstelle.de", subject, body);
        } catch (MessagingException ex) {
            _logger.log(Level.SEVERE, null, ex);
        }
    }
}
