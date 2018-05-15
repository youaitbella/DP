/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.common.requestmanager;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.inek.dataportal.common.data.icmt.entities.Customer;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountFeatureRequest;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.data.icmt.facade.ContactRoleFacade;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.data.account.facade.AccountFeatureRequestFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.utils.DateUtils;
import static org.inek.dataportal.common.helper.Const.*;

/**
 *
 * @author muellermi
 */
@Stateless
public class FeatureRequestHandler {

    private static final Logger LOGGER = Logger.getLogger("FeatureRequestHandler");
    @Inject
    private AccountFeatureRequestFacade _facade;
    @Inject
    private ContactRoleFacade _roleFacade;
    @Inject
    private CustomerFacade _customerFacade;
    @Inject
    private ConfigFacade _config;

    public boolean handleFeatureRequest(Account account, Feature feature) {
        AccountFeatureRequest featureRequest = _facade.findByAccountIdAndFeature(account.getId(), feature);
        if (featureRequest != null) {
            if (featureRequest.getCreationDate().after(DateUtils.getDateWithDayOffset(-4))) {
                // an approval process not older than 3 days is still in progress
                return true;
            }
            _facade.remove(featureRequest);
            if (_config.readConfigBool(ConfigKey.TestMode)) {
                return false;
            }
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

    @Inject
    private Mailer _mailer;

    private boolean sendApprovalRequestMail(Account account, AccountFeatureRequest featureRequest) {
        MailTemplate template = _mailer.getMailTemplate("ApprovalRequestMail");
        if (template == null) {
            return false;
        }
        String link = buildLink(featureRequest.getApprovalKey());
        String subject = template.getSubject().replace(PLACEHOLDER_FEATURE, featureRequest.getFeature().getDescription());
        String iks = "";
        boolean firstIK = true;
        for (int s : account.getFullIkSet()) {
            if (firstIK) {
                iks += s;
                firstIK = false;
            } else {
                iks += "," + s;
            }
        }

        String body = template.getBody()
                .replace(PLACEHOLDER_LINK, link)
                .replace(PLACEHOLDER_FEATURE, featureRequest.getFeature().getDescription())
                .replace(PLACEHOLDER_NAME, account.getFirstName() + " " + account.getLastName())
                .replace(PLACEHOLDER_EMAIL, account.getEmail())
                .replace(PLACEHOLDER_ROLE, _roleFacade.find(account.getRoleId()).getText())
                .replace(PLACEHOLDER_PHONE, account.getPhone())
                .replace(PLACEHOLDER_COMPANY, account.getCompany())
                .replace(PLACEHOLDER_IK, iks);
        String mailAddress = _config.readConfig(ConfigKey.ManagerEmail);
        return _mailer.sendMail(mailAddress, template.getBcc(), subject, body);

    }

    private String buildLink(String key) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String protocol = externalContext.getRequestScheme() + "://";
        int port = externalContext.getRequestServerPort();
        String server = externalContext.getRequestServerName();
        String link = protocol
                + server + (port == HTTP_PORT || port == HTTPS_PORT ? "" : ":" + port)
                + "/DataPortalAdmin"
                + Pages.FeatureApproval.URL()
                + "?key=" + key;
        return link;
    }

}
