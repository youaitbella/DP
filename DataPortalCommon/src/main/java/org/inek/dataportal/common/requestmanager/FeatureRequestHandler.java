/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.common.requestmanager;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.helper.PortalConstants;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountFeatureRequest;
import org.inek.dataportal.common.data.account.facade.AccountFeatureRequestFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.icmt.facade.ContactRoleFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.utils.DateUtils;

import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.UUID;
import java.util.logging.Logger;

import static org.inek.dataportal.api.helper.PortalConstants.*;

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
        sendApprovalRequestMail(account, featureRequest);
        featureRequest.tagCreationDate();
        _facade.save(featureRequest);

        return true;
    }

    @Inject
    private Mailer _mailer;

    private boolean sendApprovalRequestMail(Account account, AccountFeatureRequest featureRequest) {
        MailTemplate template = _mailer.getMailTemplate("ApprovalRequestMail");
        if (template == null) {
            return false;
        }
        String link = buildLink(featureRequest.getApprovalKey());
        String subject = template.getSubject().replace(VAR_FEATURE, featureRequest.getFeature().getDescription());
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
                .replace(VAR_LINK, link)
                .replace(VAR_FEATURE, featureRequest.getFeature().getDescription())
                .replace(VAR_NAME, account.getFirstName() + " " + account.getLastName())
                .replace(VAR_EMAIL, account.getEmail())
                .replace(VAR_ROLE, _roleFacade.find(account.getRoleId()).getText())
                .replace(VAR_PHONE, account.getPhone())
                .replace(VAR_COMPANY, account.getCompany())
                .replace(VAR_IK, iks);
        String mailAddress = _config.readConfig(ConfigKey.ManagerEmail);
        return _mailer.sendMail(mailAddress, template.getBcc(), subject, body);

    }

    private String buildLink(String key) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String protocol = externalContext.getRequestScheme() + "://";
        int port = externalContext.getRequestServerPort();
        String server = externalContext.getRequestServerName();
        String link = protocol
                + server + (port == PortalConstants.HTTP_PORT || port == PortalConstants.HTTPS_PORT ? "" : ":" + port)
                + "/DataPortalAdmin"
                + Pages.FeatureApproval.URL()
                + "?key=" + key;
        return link;
    }

}
