package org.inek.dataportal.feature.peppproposal;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.pepp.PeppProposal;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.facades.PeppProposalFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.utils.DocumentationUtil;

@Named
@RequestScoped
public class PeppProposalList {

    @Inject private PeppProposalFacade _peppProposalFacade;
    @Inject private SessionController _sessionController;

    public String newPeppProposal() {
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditPeppProposal");
        return Pages.PeppProposalEdit.URL();
    }

    public String editPeppProposal(int proposalId) {
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditPeppProposal");
        return Pages.PeppProposalEdit.URL();
    }

    public String deletePeppProposal(int proposalId) {
        PeppProposal proposal = _peppProposalFacade.find(proposalId);
        if (proposal == null) {
            return "";
        }
        if (_sessionController.isMyAccount(proposal.getAccountId())) {
            if (proposal.getStatus().getId() < 9) {
                _peppProposalFacade.remove(proposal);
            } else {
                proposal.setStatus(WorkflowStatus.Retired);
                _peppProposalFacade.savePeppProposal(proposal);
            }
        }
        return "";
    }

    public String printPeppProposal(int proposalId) {
        PeppProposal peppProposal = _peppProposalFacade.find(proposalId);
        String headLine = Utils.getMessage("namePEPP_PROPOSAL") + " " + peppProposal.getExternalId();
        Utils.getFlash().put("headLine", headLine);
        Utils.getFlash().put("targetPage", Pages.PeppProposalSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(peppProposal));
        return Pages.PrintView.URL();
    }

}
