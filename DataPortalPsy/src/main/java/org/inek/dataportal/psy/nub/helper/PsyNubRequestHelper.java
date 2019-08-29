package org.inek.dataportal.psy.nub.helper;

import org.inek.dataportal.common.data.access.ProcedureFacade;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.MailTemplateHelper;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.psy.nub.entities.PsyNubRequest;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class PsyNubRequestHelper implements Serializable {

    private static String NUB_CONFIRM_TEMPLATE = "PSY-NUB Confirm";
    private static String NUB_RETIRED_TEMPLATE = "PSY-NUB Retired";

    @Inject
    private CustomerFacade _customerFacade;

    @Inject
    private ProcedureFacade _procedureFacade;

    @Inject
    private Mailer _mailer;

    public String checkProcedureCodes(String value, int targetYear) {
        return _procedureFacade.checkProcedures(value, targetYear - 1, targetYear);
    }

    public String checkProxyIKs(String value) {
        String[] iks = value.split("\\s|,|\r|\n");
        StringBuilder invalidIKs = new StringBuilder();
        for (String ik : iks) {
            if (ik.isEmpty()) {
                continue;
            }
            if (!_customerFacade.isValidIK(ik)) {
                if (invalidIKs.length() > 0) {
                    invalidIKs.append(", ");
                }
                invalidIKs.append(ik);
            }
        }
        if (invalidIKs.length() > 0) {
            if (invalidIKs.indexOf(",") < 0) {
                invalidIKs.insert(0, "Ungültige IK: ");
            } else {
                invalidIKs.insert(0, "Ungültige IKs: ");
            }
        }
        return invalidIKs.toString();
    }

    public String formatProxyIks(String value) {
        String[] iks = value.split("\\s|,|\r|\n");
        String formatted = "";
        for (String ik : iks) {
            if (formatted.length() > 0) {
                formatted += ", ";
            }
            formatted += ik;
        }
        return formatted;
    }

    public void sendPsyNubConformationMail(PsyNubRequest request, Account account) {
        if (account.isNubInformationMail()) {
            MailTemplate template;

            if (request.getStatus().equals(WorkflowStatus.Retired)) {
                template = _mailer.getMailTemplate(NUB_RETIRED_TEMPLATE);
            } else {
                template = _mailer.getMailTemplate(NUB_CONFIRM_TEMPLATE);
            }

            String proxy = request.getProposalData().getProxyIks().trim();
            if (!proxy.isEmpty()) {
                proxy = "\r\nSie haben diese Anfrage auch stellvertretend für die folgenden IKs gestellt:\r\n" + proxy + "\r\n";
            }

            MailTemplateHelper.setPlaceholderInTemplate(template, "{id}", request.getNubIdExtern());
            MailTemplateHelper.setPlaceholderInTemplate(template, "{ik}", String.valueOf(request.getIk()));
            MailTemplateHelper.setPlaceholderInTemplate(template, "{formalSalutation}", _mailer.getFormalSalutation(account));
            MailTemplateHelper.setPlaceholderInTemplate(template, "{name}", request.getName());
            MailTemplateHelper.setPlaceholderInTemplate(template, "{proxyIks}", proxy);
            MailTemplateHelper.setPlaceholderInTemplate(template, "{targetYear}", String.valueOf(request.getTargetYear()));

            _mailer.sendMailTemplate(template, account.getEmail());
        }
    }
}
