package org.inek.dataportal.feature.drgproposal;

//import org.inek.dataportal.feature.drg.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.cooperation.CooperationRight;
import org.inek.dataportal.entities.drg.DrgProposal;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.DrgProposalFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.cooperation.CooperationFacade;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.helper.structures.Triple;
import org.inek.dataportal.utils.DocumentationUtil;

@Named
@RequestScoped
public class DrgProposalList {

    @Inject
    DrgProposalFacade _drgProposalFacade;
    @Inject
    SessionController _sessionController;
    @Inject
    CooperationTools _cooperationTools;

    public List<ProposalInfo> getDrgProposals() {
        return _drgProposalFacade.getDrgProposalInfos(_sessionController.getAccountId(), DataSet.AllOpen);
    }

    public List<ProposalInfo> getSealedDrgProposals() {
        return _drgProposalFacade.getDrgProposalInfos(_sessionController.getAccountId(), DataSet.AllSealed);

    }

    /**
     * Nub may be created and send during Sept. and Oct.
     *
     * @return
     */
    public boolean getDrgEnabled() {
        int month = 1 + Calendar.getInstance().get(Calendar.MONTH); // jan=0, thus 1+month
        return (month >= 9 && month <= 10) || _sessionController.isInternalClient(); // allow local access allways
    }

    public boolean getOpenListEnabled() {
        return getDrgProposals().size() > 0;
    }

    public String newDrgProposal() {
        return Pages.DrgProposalEditAddress.RedirectURL();
    }

    public String editDrgProposal(int proposalId) {
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

    // <editor-fold defaultstate="collapsed" desc="Cooperation">
    
    public List<Account> getPartnersForEdit() {
        return _cooperationTools.getPartnersForEdit(Feature.DRG_PROPOSAL);
    }

    public List<ProposalInfo> getDrgProposalsForEditFromPartner(int partnerId) {
        CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.DRG_PROPOSAL, partnerId);

        DataSet dataSet = achievedRight.canReadAlways() ? DataSet.AllOpen
                : achievedRight.canReadCompleted() ? DataSet.ApprovalRequested
                        : DataSet.None;
        return _drgProposalFacade.getDrgProposalInfos(partnerId, dataSet);
    }

    public List<Account> getPartnersForDisplay() {
        return _cooperationTools.getPartnersForDisplay(Feature.DRG_PROPOSAL);
    }

    public List<ProposalInfo> getDrgProposalsForDisplayFromPartner(int partnerId) {
        CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.DRG_PROPOSAL, partnerId);
        DataSet dataSet = achievedRight.canReadSealed() ? DataSet.AllSealed : DataSet.None;
        return _drgProposalFacade.getDrgProposalInfos(partnerId, dataSet);
    }

// </editor-fold>

}
