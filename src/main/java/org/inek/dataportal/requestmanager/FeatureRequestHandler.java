/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.requestmanager;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import org.inek.dataportal.entities.Account;
import org.inek.dataportal.entities.AccountFeatureRequest;
import org.inek.dataportal.entities.ContactRole;
import org.inek.dataportal.entities.Customer;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.AccountFeatureRequestFacade;
import org.inek.dataportal.facades.ContactRoleFacade;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.utils.DateUtils;
import org.inek.dataportal.utils.PropertyKey;
import org.inek.dataportal.utils.PropertyManager;

/**
 *
 * @author muellermi
 */
@Stateless
public class FeatureRequestHandler {

    private static final Logger _logger = Logger.getLogger("FeatureRequestHandler");
    @Inject private AccountFeatureRequestFacade _facade;
    @Inject private ContactRoleFacade _roleFacade;
    @Inject private CustomerFacade _customerFacade;

    
    //@Asynchronous
    public void handleFeatureRequest(Account account) {
        AccountFeatureRequest featureRequest = _facade.findByAccountId(account.getAccountId());
        if (featureRequest == null) {
            featureRequest = new AccountFeatureRequest();
            featureRequest.setAccountId(account.getAccountId());
        } else if (featureRequest.getCreationDate().after(DateUtils.getDateWithDayOffset(-3))) {
            // an approval process not older than 3 days is still in progress
            return;
        }
        featureRequest.setApprovalKey(UUID.randomUUID().toString());
        if (sendApprovalRequestMail(account, featureRequest)) {
            featureRequest.tagCreationDate();
            _facade.persist(featureRequest);
        }
    }

    private boolean sendApprovalRequestMail(Account account, AccountFeatureRequest featureRequest) {
        String link = PropertyManager.INSTANCE.getProperty(PropertyKey.ManagerURL) + Pages.AdminApproval.URL() + "?key=" + featureRequest.getApprovalKey();
        String body = Utils.getMessage("msgApprove") + "\r\n" + link + "\r\n\r\n";
        body += "Name:  " + account.getFirstName() + " " + account.getLastName() + "\r\n";
        body += "Email: " + account.getEmail() + "\r\n";
        ContactRole role = _roleFacade.find(account.getRoleId());
        body += "Funktion: " + role.getText() + "\r\n";
        body += "Telefon: " + account.getPhone() + "\r\n";
        body += "Firma: " + account.getCompany() + "\r\n";
        Customer cust = _customerFacade.getCustomerByIK(account.getIK());
        body += "IK:    " + account.getIK() + (cust.getIK() != null ? " (im ICMT bekannt)" : "")+ "\r\n";
        String subject = Utils.getMessage("headerFeatureApproval") + " " + account.getEmail();
        try {
            Mailer.sendMail(PropertyManager.INSTANCE.getProperty(PropertyKey.ManagerEmail), subject, body);
        } catch (MessagingException ex) {
            _logger.log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
