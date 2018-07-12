package org.inek.dataportal.drg.drgproposal;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.drg.drgproposal.entities.DrgProposal;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.drg.drgproposal.facades.DrgProposalFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.MailTemplateFacade;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.scope.FeatureScopedContextHolder;
import org.inek.dataportal.common.utils.DocumentationUtil;

@Named
@RequestScoped
public class DrgProposalList {

    @Inject
    private DrgProposalFacade _drgProposalFacade;
    @Inject
    private SessionController _sessionController;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private ApplicationTools _appTools;
    @Inject
    private Mailer _mailer;
    @Inject
    private MailTemplateFacade _mailTemplateFacde;

    public String editDrgProposal() {
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditDrgProposal");
        return Pages.DrgProposalEditAddress.URL();
    }

    public String deleteDrgProposal(int proposalId) {
        DrgProposal proposal = _drgProposalFacade.find(proposalId);
        if (proposal == null) {
            return "";
        }
        if (_sessionController.isMyAccount(proposal.getAccountId())) {
            if (proposal.getStatus().getId() < 9) {
                _drgProposalFacade.remove(proposal);
            } else {
                proposal.setStatus(WorkflowStatus.Retired);
                _drgProposalFacade.saveDrgProposal(proposal);
                if (!_drgProposalFacade.updateDrgProposalDb(proposal.getExternalId())) {
                    MailTemplate template = _mailTemplateFacde.findByName("Vorschlag zurückgezogen");
                    template.setBody(template.getBody().replace("{id}", proposal.getExternalId()));
                    _mailer.sendMailTemplate(template, "medizin@inek-drg.de");
                }
            }
        }
        return "";
    }

    public String printDrgProposal(int proposalId) {
        DrgProposal drgProposal = _drgProposalFacade.find(proposalId);
        String headLine = Utils.getMessage("nameDRG_PROPOSAL") + " " + drgProposal.getExternalId();
        Utils.getFlash().put("headLine", headLine);
        Utils.getFlash().put("targetPage", Pages.DrgProposalSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(drgProposal));
        return Pages.PrintView.URL();
    }

}
