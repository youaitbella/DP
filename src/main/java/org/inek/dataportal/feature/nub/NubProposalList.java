package org.inek.dataportal.feature.nub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.cooperation.CooperationRight;
import org.inek.dataportal.entities.NubProposal;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.cooperation.CooperationFacade;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;
import org.inek.dataportal.facades.NubProposalFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.utils.DocumentationUtil;

@Named
@RequestScoped
public class NubProposalList {

    private static final Logger _logger = Logger.getLogger("NubProposalList");
//    public NubProposalList() {
//        System.out.println("ctor NubProposalList");
//    }
    @Inject NubProposalFacade _nubProposalFacade;
    @Inject SessionController _sessionController;
    @Inject CooperationTools _cooperationTools;

    private List<ProposalInfo> _openNubs;
    private List<ProposalInfo> _sealedNubs;

    public List<ProposalInfo> getNubProposals() {
        if (_openNubs == null) {
            _openNubs = _nubProposalFacade.getNubProposalInfos(_sessionController.getAccountId(), DataSet.AllOpen, getFilter());
        }
        return _openNubs;
    }

    public List<ProposalInfo> getSealedNubProposals() {
        if (_sealedNubs == null) {
            _sealedNubs = _nubProposalFacade.getNubProposalInfos(_sessionController.getAccountId(), DataSet.AllSealed, getFilter());
        }
        return _sealedNubs;
    }

    private String getFilter() {
        String filter = _nubSessionTools.getNubFilter();
        if (!filter.isEmpty() && !filter.contains("%")) {
            filter = "%" + filter + "%";
        }
        return filter;
    }

    /**
     * Nub may be created and send during Sept. and Oct.
     *
     * @return
     */
    public boolean getNubEnabled() {
        int month = 1 + Calendar.getInstance().get(Calendar.MONTH); // jan=0, thus 1+month
        if (month == 11 && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 4) {
            //in 2014 allow till next monday
            // todo: other years
            return true;
        }
        return (month >= 9 && month <= 10) || _sessionController.isInternalClient(); // allow local access allways
    }

    public boolean getOpenListEnabled() {
        //return getNubEnabled() && getNubProposals().size() > 0;
        return getNubProposals().size() > 0;
    }

    public String getRejectReason(int proposalId) {
        String reason = WorkflowStatus.Rejected.getDescription();
        NubProposal proposal = _nubProposalFacade.find(proposalId);
        if (proposal != null) {
            reason += " Grund: " + proposal.getErrorText();
        }
        return reason;
    }

    public String refresh() {
        return "";
    }

    public String newNubProposal() {
        return Pages.NubEditAddress.URL();
    }

    public String newNubProposalFromTemplate() {
        return Pages.NubFromTemplate.URL();
    }

    public String editNubProposal(int proposalId) {
        Utils.getFlash().put("nubId", proposalId);
        return Pages.NubEditAddress.URL();
    }

    public String requestDeleteNubProposal(int proposalId) {
        Utils.getFlash().put("nubId", proposalId);
        NubProposal proposal = _nubProposalFacade.find(proposalId);
        if (proposal == null) {
            _logger.log(Level.INFO, "Could not find nubRequest with id {0}.", proposalId);
            return "";
        }
        if (_sessionController.isMyAccount(proposal.getAccountId())) {
            String msg = proposal.getStatus().getValue() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.getMessage("msgConfirmRetire");
            String script = "if (confirm ('" + proposal.getName().replaceAll("(\\r|\\n)", "") + "\\r\\n" + msg + "')) {document.getElementById('deleteNubProposal').click();}";
            _sessionController.setScript(script);
        }
        return "";
    }

    public String deleteNubProposal(int proposalId) {
        NubProposal proposal = _nubProposalFacade.find(proposalId);
        if (proposal == null) {
            return "";
        }
        if (_sessionController.isMyAccount(proposal.getAccountId())) {
            if (proposal.getStatus().getValue() <= 9) {
                _nubProposalFacade.remove(proposal);
            } else {
                proposal.setStatus(WorkflowStatus.Retired);
                _nubProposalFacade.saveNubProposal(proposal);
            }
        }
        return "";
    }

    public String printNubProposal(int proposalId) {
        NubProposal nubProposal = _nubProposalFacade.find(proposalId);

        String headLine = Utils.getMessage("nameNUB")
                + (nubProposal.getStatus().getValue() >= WorkflowStatus.Provided.getValue() ? " N" + nubProposal.getNubId() : "");
        Utils.getFlash().put("headLine", headLine);
        Utils.getFlash().put("targetPage", Pages.NubSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(nubProposal));
        return Pages.PrintView.URL();
    }

    public String getExternalState(int proposalId) {
        NubProposal nubProposal = _nubProposalFacade.find(proposalId);
        return nubProposal.getExternalState();
    }

    // <editor-fold defaultstate="collapsed" desc="Cooperation">
    @Inject AccountFacade _accountFacade;
    @Inject CooperationFacade _cooperationFacade;
    @Inject CooperationRightFacade _cooperationRightFacade;
    @Inject NubSessionTools _nubSessionTools;
    private List<CooperationRight> _cooperationRights;
    private List<Account> _partners4Edit;
    private List<Account> _partners4List;

    public List<Account> getPartnersForEdit() {
        return _cooperationTools.getPartnersForEdit(Feature.NUB);
    }

    Map<Integer, List<ProposalInfo>> _partnerNubsForEdit = new HashMap<>();
    public List<ProposalInfo> getNubProposalsForEditFromPartner(int partnerId) {
        if (!_partnerNubsForEdit.containsKey(partnerId)) {
            ensureAchievedCooperationRights();
            Set<Integer> partnerIKs = _accountFacade.find(partnerId).getFullIkList();
            List<ProposalInfo> nubs = new ArrayList<>();
            for (CooperationRight right : _cooperationRights) {
                if ((right.getOwnerId() == partnerId || right.getOwnerId() == -1)
                        && partnerIKs.contains(right.getIk())
                        && right.getCooperativeRight() != CooperativeRight.None
                        && right.getCooperativeRight() != CooperativeRight.ReadSealed) {
                    int minStatus = right.getCooperativeRight() == CooperativeRight.ReadCompletedSealSupervisor
                            || right.getCooperativeRight() == CooperativeRight.ReadWriteCompletedSealSupervisor
                                    ? WorkflowStatus.ApprovalRequested.getValue()
                                    : 0;
                    nubs.addAll(_nubProposalFacade.findForAccountAndIk(partnerId, right.getIk(), minStatus, 9, getFilter()));
                }
            }
            _partnerNubsForEdit.put(partnerId, nubs);
        }
        return _partnerNubsForEdit.get(partnerId);
    }

    public List<Account> getPartnersForDisplay() {
        if (_partners4List == null) {
            ensureAchievedCooperationRights();
            Set<Integer> ids = new HashSet<>();
            for (CooperationRight right : _cooperationRights) {
                if (right.getCooperativeRight() != CooperativeRight.None) {
                    if (right.getOwnerId() == -1) {
                        ids.addAll(_nubProposalFacade.findAccountIdForIk(right.getIk()));
                    } else {
                        ids.add(right.getOwnerId());
                    }
                }
            }
            ids.remove(_sessionController.getAccountId());  // remove own id (if in set)
            _partners4List = _accountFacade.getAccountsForIds(ids);
        }
        return _partners4List;
    }

    Map<Integer, List<ProposalInfo>> _partnerNubsForDisplay = new HashMap<>();
    public List<ProposalInfo> getNubProposalsForDisplayFromPartner(int partnerId) {
        if (!_partnerNubsForDisplay.containsKey(partnerId)) {
            ensureAchievedCooperationRights();
            List<ProposalInfo> nubs = new ArrayList<>();
            Set<Integer> iks = new HashSet<>();

            for (CooperationRight right : _cooperationRights) {
                if (right.getOwnerId() == partnerId
                        && right.getCooperativeRight() != CooperativeRight.None) {
                    iks.add(right.getIk());
                    //nubs.addAll(_nubProposalFacade.findForAccountAndIk(partnerId, right.getIk(), 10, 999, getFilter()));
                }
            }

            // add managed iks
            for (int ik : _accountFacade.find(partnerId).getFullIkList()) {
                if (_cooperationRightFacade.isIkSupervisor(Feature.NUB, ik, _sessionController.getAccountId())) {
                    iks.add(ik);
                }
            }

            for (Integer ik : iks) {
                nubs.addAll(_nubProposalFacade.findForAccountAndIk(partnerId, ik, 10, 999, getFilter()));
            }
            _partnerNubsForDisplay.put(partnerId, nubs);
        }
        return _partnerNubsForDisplay.get(partnerId);
    }

    private void ensureAchievedCooperationRights() {
        if (_cooperationRights == null) {
            _cooperationRights = _cooperationRightFacade.getAchievedCooperationRights(_sessionController.getAccountId(), Feature.NUB);
        }
    }
// </editor-fold>

}
