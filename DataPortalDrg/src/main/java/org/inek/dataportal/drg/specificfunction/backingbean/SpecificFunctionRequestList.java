package org.inek.dataportal.drg.specificfunction.backingbean;

import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.adm.facade.InekRoleFacade;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.specificfunction.entity.SpecificFunctionRequest;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.drg.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class SpecificFunctionRequestList {

    // <editor-fold defaultstate="collapsed" desc="fields">
    private static final Logger LOGGER = Logger.getLogger(SpecificFunctionRequestList.class.getName());

    @Inject
    private SpecificFunctionFacade _specificFunctionFacade;
    @Inject
    private ApplicationTools _appTools;
    @Inject
    private Mailer _mailer;
    @Inject
    private InekRoleFacade _inekRoleFacade;
    // </editor-fold>

    public boolean isNewAllowed() {
        return _appTools.isEnabled(ConfigKey.IsSpecificFunctionRequestCreateEnabled);
    }

    public String newRequest() {
        return Pages.SpecificFunctionEditRequest.URL();
    }

    public String print(SpecificFunctionRequest request) {
        Utils.getFlash().put("headLine", Utils.getMessage("nameSPECIFIC_FUNCTION"));
        Utils.getFlash().put("targetPage", Pages.SpecificFunctionSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(request));
        return Pages.PrintView.URL();
    }

    public void delete(SpecificFunctionRequest request) {
        if (request == null) {
            // might be deleted by somebody else
            return;
        }
        if (request.getStatus().getId() >= WorkflowStatus.Provided.getId()) {
            sendMailToInek(request);
            request.setStatus(WorkflowStatus.Retired);
            _specificFunctionFacade.saveSpecificFunctionRequest(request);
        } else {
            _specificFunctionFacade.deleteSpecificFunctionRequest(request);
        }
    }

    public String edit() {
        return Pages.SpecificFunctionEditRequest.URL();
    }

    private void sendMailToInek(SpecificFunctionRequest request) {
        List<Account> inekAccounts = _inekRoleFacade.findForFeature(Feature.SPECIFIC_FUNCTION);

        MailTemplate template = _mailer.getMailTemplate("SpecificFunctionCorrectionRequested");
        template.setBody(template.getBody().replace("{ik}", Integer.toString(request.getIk())));
        template.setBody(template.getBody().replace("{year}", Integer.toString(request.getDataYear())));

        String body = template.getBody();

        for (Account ac : inekAccounts) {
            template.setBody(body.replace("{salutation}", _mailer.getFormalSalutation(ac)));
            _mailer.sendMailTemplate(template, ac.getEmail());
        }
    }

}
