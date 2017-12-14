package org.inek.dataportal.feature.drgproposal;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.AccessManager;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.drg.DrgProposal;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.DrgProposalFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.utils.DocumentationUtil;

@Named
@RequestScoped
public class DrgProposalList {

    @Inject private DrgProposalFacade _drgProposalFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;
    @Inject private ApplicationTools _appTools;

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
