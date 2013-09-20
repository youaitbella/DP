package org.inek.dataportal.feature.nub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

    @Inject NubProposalFacade _nubProposalFacade;
    @Inject SessionController _sessionController;

    public List<Triple> getNubProposals() {
        return _nubProposalFacade.getNubProposalInfos(_sessionController.getAccount().getAccountId(), DataSet.OPEN);
    }

    public List<Triple> getSealedNubProposals() {
        return _nubProposalFacade.getNubProposalInfos(_sessionController.getAccount().getAccountId(), DataSet.SEALED);
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
        return getNubEnabled() && getNubProposals().size() > 0;
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
            String script = "if (confirm ('" + proposal.getName().replaceAll("(\\r|\\n)", "") + "\\r\\n" + msg + "')) {document.getElementById('form:deleteNubProposal').click();}";
            _sessionController.setScript(script);
        }
        return null;
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
        Utils.getFlash().put("headLine", Utils.getMessage("nameNUB"));
        Utils.getFlash().put("targetPage", Pages.NubSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_nubProposalFacade.find(proposalId)));
        return Pages.PrintView.URL();
    }

    @Inject AccountFacade _accountFacade;
    @Inject CooperationFacade _cooperationFacade;
    @Inject CooperationRightFacade _cooperationRightFacade;
    private List<CooperationRight> _cooperationRights;
    private List<Account> _partners;

    public List<Account> getPartners() {
        if (_partners == null) {
            ensureAchievedCooperationRights();
            Set<Integer> ids = new HashSet<>();
            for (CooperationRight right : _cooperationRights) {
                if (right.getCooperativeRight() == CooperativeRight.ReadOnly
                        || right.getCooperativeRight() == CooperativeRight.ReadWrite
                        || right.getCooperativeRight() == CooperativeRight.ReadWriteSeal) {
                    ids.add(right.getOwnerId());
                }
            }
            _partners = ids.isEmpty() ? new ArrayList<Account>() :_accountFacade.getAccountsForIds(ids);
        }
        return _partners;
    }

    public List<Triple> getNubProposalsFromPartner(int partnerId) {
        ensureAchievedCooperationRights();
        List<Integer> iks = new ArrayList<>();
        for (CooperationRight right : _cooperationRights) {
            if (right.getOwnerId() == partnerId
                    && (right.getCooperativeRight() == CooperativeRight.ReadOnly
                    || right.getCooperativeRight() == CooperativeRight.ReadWrite
                    || right.getCooperativeRight() == CooperativeRight.ReadWriteSeal)) {
                iks.add(right.getIk());
            }
        }
        return _nubProposalFacade.findForAccountAndIks(partnerId, iks);
    }

    private void ensureAchievedCooperationRights() {
        if (_cooperationRights == null) {
            _cooperationRights = _cooperationRightFacade.getAchievedCooperationRights(_sessionController.getAccount().getAccountId(), Feature.NUB);
        }
    }
}
