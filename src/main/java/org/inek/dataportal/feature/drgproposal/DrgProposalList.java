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

    @Inject DrgProposalFacade _drgProposalFacade;
    @Inject SessionController _sessionController;
    @Inject CooperationTools _cooperationTools;

    public List<Triple> getDrgProposals() {
        return _drgProposalFacade.getDrgProposalInfos(_sessionController.getAccountId(), DataSet.AllOpen);
    }

    public List<Triple> getSealedDrgProposals() {
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
    @Inject AccountFacade _accountFacade;
    @Inject CooperationFacade _cooperationFacade;
    @Inject CooperationRightFacade _cooperationRightFacade;
    // @Inject DrgSessionTools _drgSessionTools;
    private List<CooperationRight> _cooperationRights;
    private List<Account> _partners4Edit;
    private List<Account> _partners4List;

    public List<Account> getPartnersForEdit() {
        return _cooperationTools.getPartnersForEdit(Feature.DRG_PROPOSAL);
    }

    
    public List<Triple> getDrgProposalsForEditFromPartner(int partnerId) {
        CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.DRG_PROPOSAL, partnerId);
        
        DataSet dataSet = achievedRight.canReadAlways() ? DataSet.AllOpen 
                : achievedRight.canReadCompleted() ? DataSet.ApprovalRequested 
                : DataSet.None;
                return _drgProposalFacade.getDrgProposalInfos(partnerId, dataSet);
    }

//    public List<Account> getPartnersForDisplay() {
//        if (_partners4List == null) {
//            ensureAchievedCooperationRights();
//            Set<Integer> ids = new HashSet<>();
//            for (CooperationRight right : _cooperationRights) {
//                if (right.getCooperativeRight() != CooperativeRight.None) {
//                    if (right.getOwnerId() == -1) {
//                        ids.addAll(_drgProposalFacade.findAccountIdForIk(right.getIk()));
//                    } else {
//                        ids.add(right.getOwnerId());
//                    }
//                }
//            }
//            ids.remove(_sessionController.getAccountId());  // remove own id (if in set)
//            _partners4List = _accountFacade.getAccountsForIds(ids);
//        }
//        return _partners4List;
//    }
    public List<ProposalInfo> getDrgProposalsForDisplayFromPartner(int partnerId) {
        ensureAchievedCooperationRights();
        List<ProposalInfo> drgs = new ArrayList<>();
        Set<Integer> iks = new HashSet<>();

//        for (CooperationRight right : _cooperationRights) {
//            if (right.getOwnerId() == partnerId
//                    && right.getCooperativeRight() != CooperativeRight.None) {
//                iks.add(right.getIk());
//                nubs.addAll(_drgProposalFacade.findForAccountAndIk(partnerId, right.getIk(), 10, 999));
//            }
//        }
        // add managed iks
        for (int ik : _accountFacade.find(partnerId).getFullIkList()) {
            if (_cooperationRightFacade.isSupervisor(Feature.DRG_PROPOSAL, ik, _sessionController.getAccountId())) {
                iks.add(ik);
            }
        }

//        for (Integer ik : iks) {
//            nubs.addAll(_drgProposalFacade.findForAccountAndIk(partnerId, ik, 10, 999));
//        }
        return drgs;
    }

    private void ensureAchievedCooperationRights() {
        if (_cooperationRights == null) {
            _cooperationRights = _cooperationRightFacade.getAchievedCooperationRights(_sessionController.getAccountId(), Feature.DRG_PROPOSAL);
        }
    }
// </editor-fold>

}
