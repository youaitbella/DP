package org.inek.dataportal.feature.nub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Account;
import org.inek.dataportal.entities.CooperationRight;
import org.inek.dataportal.entities.NubProposal;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.NubStatus;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.AccountFacade;
import org.inek.dataportal.facades.CooperationFacade;
import org.inek.dataportal.facades.CooperationRightFacade;
import org.inek.dataportal.facades.NubProposalFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.helper.structures.Triple;

@Named
@RequestScoped
public class NubProposalList {

    @Inject
    NubProposalFacade _nubProposalFacade;
    @Inject
    SessionController _sessionController;

    public List<Triple> getNubProposals() {
        return _nubProposalFacade.getNubProposalInfos(_sessionController.getAccountId(), DataSet.OPEN);
    }

    public List<Triple> getSealedNubProposals() {
        return _nubProposalFacade.getNubProposalInfos(_sessionController.getAccountId(), DataSet.SEALED);
    }

    /**
     * Nub may be created and send during Sept. and Oct.
     *
     * @return
     */
    public boolean getNubEnabled() {
        int month = 1 + Calendar.getInstance().get(Calendar.MONTH); // jan=0, thus 1+month
        return (month >= 9 && month <= 10) || _sessionController.isInternalClient(); // allow local access allways  
    }

    public boolean getOpenListEnabled() {
        //return getNubEnabled() && getNubProposals().size() > 0;
        return getNubProposals().size() > 0;
    }

    public String getRejectReason(int proposalId) {
        String reason = NubStatus.Rejected.getDescription();
        NubProposal proposal = _nubProposalFacade.find(proposalId);
        if (proposal != null) {
            reason += " Grund: " + proposal.getErrorText();
        }
        return reason;
    }

    public String newNubProposal() {
        Utils.getFlash().put("conversationId", _sessionController.beginConversation());
        return Pages.NubEditAddress.URL();
    }

    public String newNubProposalFromTemplate() {
        //Utils.getFlash().put("conversationId", _sessionController.beginConversation());
        return Pages.NubFromTemplate.URL();
    }

    public String editNubProposal(int proposalId) {
        Utils.getFlash().put("nubId", proposalId);
        Utils.getFlash().put("conversationId", _sessionController.beginConversation());
        return Pages.NubEditAddress.URL();
    }

    public String requestDeleteNubProposal(int proposalId) {
        Utils.getFlash().put("nubId", proposalId);
        NubProposal proposal = _nubProposalFacade.find(proposalId);
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
                proposal.setStatus(NubStatus.Retired);
                _nubProposalFacade.saveNubProposal(proposal);
            }
        }
        return "";
    }

    public String printNubProposal(int proposalId) {
        NubProposal nubProposal = _nubProposalFacade.find(proposalId);

        String headLine = Utils.getMessage("nameNUB")
                + (nubProposal.getStatus().getValue() >= NubStatus.Provided.getValue() ? " N" + nubProposal.getNubId() : "");
        Utils.getFlash().put("headLine", headLine);
        Utils.getFlash().put("targetPage", Pages.NubSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(nubProposal));
        return Pages.PrintView.URL();
    }

    // <editor-fold defaultstate="collapsed" desc="Cooperation">
    @Inject
    AccountFacade _accountFacade;
    @Inject
    CooperationFacade _cooperationFacade;
    @Inject
    CooperationRightFacade _cooperationRightFacade;
    @Inject
    NubSessionTools _nubSessionTools;
    private List<CooperationRight> _cooperationRights;
    private List<Account> _partners4Edit;
    private List<Account> _partners4List;

    public List<Account> getPartnersForEdit() {
        if (_partners4Edit == null) {
            ensureAchievedCooperationRights();
            Set<Integer> ids = new HashSet<>();
            for (CooperationRight right : _cooperationRights) {
                if (right.getCooperativeRight() == CooperativeRight.ReadOnly
                        || right.getCooperativeRight() == CooperativeRight.ReadWrite
                        || right.getCooperativeRight() == CooperativeRight.ReadWriteSeal
                        || right.getCooperativeRight() == CooperativeRight.ReadCompletedSealSupervisor
                        || right.getCooperativeRight() == CooperativeRight.ReadWriteSealSupervisor
                        || right.getCooperativeRight() == CooperativeRight.ReadWriteCompletedSealSupervisor) {
                    ids.add(right.getOwnerId());
                }
            }
            ids.addAll(_nubSessionTools.getManagedAccounts());
            _partners4Edit = ids.isEmpty() ? new ArrayList<Account>() : _accountFacade.getAccountsForIds(ids);
        }
        return _partners4Edit;
    }

    public List<Triple> getNubProposalsForEditFromPartner(int partnerId) {
        ensureAchievedCooperationRights();
        List<Triple> nubs = new ArrayList<>();
        Map<Integer, Integer> iks = new HashMap<>();
        for (CooperationRight right : _cooperationRights) {
            if (right.getOwnerId() == partnerId
                    && right.getCooperativeRight() != CooperativeRight.None
                    && right.getCooperativeRight() != CooperativeRight.ReadSealed) {
                int minStatus = right.getCooperativeRight() == CooperativeRight.ReadCompletedSealSupervisor
                        || right.getCooperativeRight() == CooperativeRight.ReadWriteCompletedSealSupervisor
                        ? NubStatus.ApprovalRequested.getValue()
                        : 0;
                //        nubs.addAll(_nubProposalFacade.findForAccountAndIk(partnerId, right.getIk(), minStatus, 9));
                iks.put(right.getIk(), minStatus);
            }
        }

        // add managed iks
        for (int ik : _accountFacade.find(partnerId).getFullIkList()) {
            if (_cooperationRightFacade.isSupervisor(Feature.NUB, ik, _sessionController.getAccountId())) {
                if (!iks.containsKey(ik)) {
                    iks.put(ik, NubStatus.ApprovalRequested.getValue());
                }
            }
        }

        for (Entry<Integer, Integer> ikInfo : iks.entrySet()) {
            nubs.addAll(_nubProposalFacade.findForAccountAndIk(partnerId, ikInfo.getKey(), ikInfo.getValue(), 9));
        }

        return nubs;
    }

    public List<Account> getPartnersForDisplay() {
        if (_partners4List == null) {
            ensureAchievedCooperationRights();
            Set<Integer> ids = new HashSet<>();
            for (CooperationRight right : _cooperationRights) {
                if (right.getCooperativeRight() != CooperativeRight.None) {
                    ids.add(right.getOwnerId());
                }
            }
            ids.addAll(_nubSessionTools.getManagedAccounts());
            ids.remove(_sessionController.getAccountId());
            _partners4List = ids.isEmpty() ? new ArrayList<Account>() : _accountFacade.getAccountsForIds(ids);
        }
        return _partners4List;
    }

    public List<Triple> getNubProposalsForDisplayFromPartner(int partnerId) {
        ensureAchievedCooperationRights();
        List<Triple> nubs = new ArrayList<>();
        Set<Integer> iks = new HashSet<>();

        for (CooperationRight right : _cooperationRights) {
            if (right.getOwnerId() == partnerId
                    && right.getCooperativeRight() != CooperativeRight.None) {
                iks.add(right.getIk());
                nubs.addAll(_nubProposalFacade.findForAccountAndIk(partnerId, right.getIk(), 10, 999));
            }
        }

        // add managed iks
        for (int ik : _accountFacade.find(partnerId).getFullIkList()) {
            if (_cooperationRightFacade.isSupervisor(Feature.NUB, ik, _sessionController.getAccountId())) {
                iks.add(ik);
            }
        }

        for (Integer ik : iks) {
            nubs.addAll(_nubProposalFacade.findForAccountAndIk(partnerId, ik, 10, 999));
        }

        return nubs;
    }

    private void ensureAchievedCooperationRights() {
        if (_cooperationRights == null) {
            _cooperationRights = _cooperationRightFacade.getAchievedCooperationRights(_sessionController.getAccountId(), Feature.NUB);
        }
    }
// </editor-fold>

}
