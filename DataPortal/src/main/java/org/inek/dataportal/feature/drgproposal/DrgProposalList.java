package org.inek.dataportal.feature.drgproposal;

//import org.inek.dataportal.feature.drg.*;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.drg.DrgProposal;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.DrgProposalFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.utils.DocumentationUtil;

@Named
@RequestScoped
public class DrgProposalList {

    @Inject DrgProposalFacade _drgProposalFacade;
    @Inject SessionController _sessionController;
    @Inject CooperationTools _cooperationTools;
    @Inject ApplicationTools _appTools;

    
    public String checkIfButtonEnabled() {
        if(_appTools.isEnabled(ConfigKey.IsDrgProposalCreateEnabled))
            return "";
        return "buttonDisabled";
    }

    public String newDrgProposal() {
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditDrgProposal");
        return Pages.DrgProposalEditAddress.RedirectURL();
    }

    public String editDrgProposal(int proposalId) {
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditDrgProposal");
        Utils.getFlash().put("drgId", proposalId);
        return Pages.DrgProposalEditAddress.RedirectURL();
    }

    public String deleteDrgProposal(int proposalId) {
        DrgProposal proposal = _drgProposalFacade.find(proposalId);
        if (proposal == null) {
            return "";
        }
        if (_sessionController.isMyAccount(proposal.getAccountId())) {
            _drgProposalFacade.remove(proposal);
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
