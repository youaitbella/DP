package org.inek.dataportal.feature.peppproposal;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.pepp.PeppProposal;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.PeppProposalFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.utils.DocumentationUtil;

@Named
@RequestScoped
public class PeppProposalList {

    @Inject PeppProposalFacade _peppProposalFacade;
    @Inject SessionController _sessionController;
    @Inject CooperationTools _cooperationTools;
    @Inject ApplicationTools _appTools;

    public List<ProposalInfo> getPeppProposals() {
        return _peppProposalFacade.getPeppProposalInfos(_sessionController.getAccountId(), DataSet.AllOpen);
    }

    public List<ProposalInfo> getSealedPeppProposals() {
        return _peppProposalFacade.getPeppProposalInfos(_sessionController.getAccountId(), DataSet.AllSealed);
    }

    public String newPeppProposal() {
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditPeppProposal");
        return Pages.PeppProposalEdit.URL();
    }

    public String editPeppProposal(int proposalId) {
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditPeppProposal");
        Utils.getFlash().put("ppId", proposalId);
        return Pages.PeppProposalEdit.URL();
    }

    public String deletePeppProposal(int proposalId) {
        PeppProposal proposal = _peppProposalFacade.find(proposalId);
        if (proposal == null) {
            return "";
        }
        if (_sessionController.isMyAccount(proposal.getAccountId())) {
            if (proposal.getStatus().getValue() < 9) {
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
    
    public String checkIfButtonEnabled() {
        if(_appTools.isEnabled(ConfigKey.IsPeppProposalCreateEnabled))
            return "";
        return "buttonDisabled";
    }

    // <editor-fold defaultstate="collapsed" desc="Cooperation">
    public List<Account> getPartnersForEdit() {
        return _cooperationTools.getPartnersForEdit(Feature.PEPP_PROPOSAL);
    }

    public List<ProposalInfo> getPeppProposalsForEditFromPartner(int partnerId) {
        CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.PEPP_PROPOSAL, partnerId);

        DataSet dataSet = achievedRight.canReadAlways() ? DataSet.AllOpen
                : achievedRight.canReadCompleted() ? DataSet.ApprovalRequested
                        : DataSet.None;
        return _peppProposalFacade.getPeppProposalInfos(partnerId, dataSet);
    }

    public List<Account> getPartnersForDisplay() {
        if (_sessionController.isInekUser(Feature.PEPP_PROPOSAL)){
            List<Integer> accountIds = _peppProposalFacade.getProposalAccounts();
            for (int accountId : accountIds){
                _cooperationTools.addReadRight(Feature.PEPP_PROPOSAL, accountId);
            }
        }
        return _cooperationTools.getPartnersForDisplay(Feature.PEPP_PROPOSAL);
    }

    public List<ProposalInfo> getPeppProposalsForDisplayFromPartner(int partnerId) {
        CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.PEPP_PROPOSAL, partnerId);
        DataSet dataSet = achievedRight.canReadSealed() ? DataSet.AllSealed : DataSet.None;
        return _peppProposalFacade.getPeppProposalInfos(partnerId, dataSet);
    }

// </editor-fold>
}
