/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.requestmanager;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.inek.dataportal.entities.Customer;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountFeatureRequest;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.ContactRoleFacade;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.account.AccountFeatureRequestFacade;
import org.inek.dataportal.facades.admin.ConfigFacade;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.utils.DateUtils;

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
    @Inject private ConfigFacade _config;

    public boolean handleFeatureRequest(Account account, Feature feature) {
        AccountFeatureRequest featureRequest = _facade.findByAccountIdAndFeature(account.getId(), feature);
        if (featureRequest != null) {
            if (featureRequest.getCreationDate().after(DateUtils.getDateWithDayOffset(-4))) {
                // an approval process not older than 3 days is still in progress
                return true;
            }
            _facade.remove(featureRequest);
        }
        featureRequest = new AccountFeatureRequest();
        featureRequest.setAccountId(account.getId());
        featureRequest.setFeature(feature);
        featureRequest.setApprovalKey(UUID.randomUUID().toString());
        if (sendApprovalRequestMail(account, featureRequest)) {
            featureRequest.tagCreationDate();
            _facade.save(featureRequest);
            return true;
        }
        return false;
    }

    @Inject Mailer _mailer;

    private boolean sendApprovalRequestMail(Account account, AccountFeatureRequest featureRequest) {
        MailTemplate template = _mailer.getMailTemplate("ApprovalRequestMail");
        if (template == null) {
            return false;
        }
        String link = _config.read(ConfigKey.LocalManagerURL) + Pages.AdminApproval.URL() + "?key=" + featureRequest.getApprovalKey();
        String subject = template.getSubject().replace("{feature}", featureRequest.getFeature().getDescription());
        Customer cust = _customerFacade.getCustomerByIK(account.getIK());
        String body = template.getBody()
                .replace("{link}", link)
                .replace("{feature}", featureRequest.getFeature().getDescription())
                .replace("{name}", account.getFirstName() + " " + account.getLastName())
                .replace("{email}", account.getEmail())
                .replace("{role}", _roleFacade.find(account.getRoleId()).getText())
                .replace("{phone}", account.getPhone())
                .replace("{company}", account.getCompany())
                .replace("{ik}", account.getIK() + (Objects.equals(cust.getIK(), account.getIK()) ? " (im ICMT bekannt)" : ""));
        String mailAddress = _config.read(ConfigKey.ManagerEmail);
        return _mailer.sendMail(mailAddress, template.getBcc(), subject, body);

    }

}
