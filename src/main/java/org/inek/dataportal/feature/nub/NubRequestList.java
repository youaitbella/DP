package org.inek.dataportal.feature.nub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import org.inek.dataportal.entities.NubRequest;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.cooperation.CooperationFacade;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.utils.DocumentationUtil;

@Named
@RequestScoped
public class NubRequestList {

    private static final Logger _logger = Logger.getLogger("NubRequestList");
    @Inject NubRequestFacade _nubRequestFacade;
    @Inject SessionController _sessionController;
    @Inject CooperationTools _cooperationTools;

    private List<ProposalInfo> _openNubs;
    private List<ProposalInfo> _sealedNubs;

    public List<ProposalInfo> getNubRequests() {
        if (_openNubs == null) {
            _openNubs = _nubRequestFacade.getNubRequestInfos(_sessionController.getAccountId(), DataSet.AllOpen, getFilter());
        }
        return _openNubs;
    }

    public List<ProposalInfo> getSealedNubRequests() {
        if (_sealedNubs == null) {
            _sealedNubs = _nubRequestFacade.getNubRequestInfos(_sessionController.getAccountId(), DataSet.AllSealed, getFilter());
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
        return getNubRequests().size() > 0;
    }

    public String getRejectReason(int requestId) {
        String reason = WorkflowStatus.Rejected.getDescription();
        NubRequest proposal = _nubRequestFacade.find(requestId);
        if (proposal != null) {
            reason += " Grund: " + proposal.getErrorText();
        }
        return reason;
    }

    public String refresh() {
        return "";
    }

    public String newNubRequest() {
        return Pages.NubEditAddress.URL();
    }

    public String newNubRequestlFromTemplate() {
        return Pages.NubFromTemplate.URL();
    }

    public String editNubRequest(int requestId) {
        Utils.getFlash().put("nubId", requestId);
        return Pages.NubEditAddress.URL();
    }

    public String requestDeleteNubRequest(int requestId) {
        Utils.getFlash().put("nubId", requestId);
        NubRequest proposal = _nubRequestFacade.find(requestId);
        if (proposal == null) {
            _logger.log(Level.INFO, "Could not find nubRequest with id {0}.", requestId);
            return "";
        }
        if (_sessionController.isMyAccount(proposal.getAccountId())) {
            String msg = proposal.getStatus().getValue() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.getMessage("msgConfirmRetire");
            String script = "if (confirm ('" + proposal.getName().replaceAll("(\\r|\\n)", "") + "\\r\\n" + msg + "')) {document.getElementById('deleteNubRequest').click();}";
            _sessionController.setScript(script);
        }
        return "";
    }

    public String getConfirmMessage(int requestId) {
        NubRequest proposal = _nubRequestFacade.find(requestId);
        String msg = proposal.getName() + "\n"
                + (proposal.getStatus().getValue() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.getMessage("msgConfirmRetire"));
        msg = msg.replace("\r\n", "\n").replace("\n", "\\r\\n").replace("'", "\\'").replace("\"", "\\'");
        return "return confirm ('" + msg + "');";
    }

    public String deleteNubRequest(int requestId) {
        NubRequest proposal = _nubRequestFacade.find(requestId);
        if (proposal == null) {
            return "";
        }
        if (_sessionController.isMyAccount(proposal.getAccountId())) {
            if (proposal.getStatus().getValue() < WorkflowStatus.Provided.getValue()) {
                _nubRequestFacade.remove(proposal);
            } else if (proposal.getExternalState().trim().isEmpty()) {
                proposal.setStatus(WorkflowStatus.Retired);
                _nubRequestFacade.saveNubRequest(proposal);
            }
        }
        return "";
    }

    public String printNubRequest(int requestId) {
        NubRequest nubRequest = _nubRequestFacade.find(requestId);

        String headLine = Utils.getMessage("nameNUB")
                + (nubRequest.getStatus().getValue() >= WorkflowStatus.Provided.getValue() ? " N" + nubRequest.getId() : "");
        Utils.getFlash().put("headLine", headLine);
        Utils.getFlash().put("targetPage", Pages.NubSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(nubRequest));
        return Pages.PrintView.URL();
    }

    public String getExternalState(int requestId) {
        NubRequest nubRequest = _nubRequestFacade.find(requestId);
        return nubRequest.getExternalState();
    }

    // <editor-fold defaultstate="collapsed" desc="Cooperation">
    @Inject AccountFacade _accountFacade;
    @Inject CooperationFacade _cooperationFacade;
    @Inject CooperationRightFacade _cooperationRightFacade;
    @Inject NubSessionTools _nubSessionTools;

    public List<Account> getPartnersForEdit() {
        return _cooperationTools.getPartnersForEdit(Feature.NUB);
    }

    Map<Integer, List<ProposalInfo>> _partnerNubsForEdit = new HashMap<>();

    public List<ProposalInfo> getNubRequestsForEditFromPartner(int partnerId) {
        if (!_partnerNubsForEdit.containsKey(partnerId)) {
            //System.out.println("getNubRequestsForEditFromPartner" + partnerId);
            List<ProposalInfo> infos = new ArrayList<>();
            Set<Integer> iks = _cooperationTools.getPartnerIks(Feature.NUB, partnerId);
            for (int ik : iks) {
                CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.NUB, partnerId, ik);
                DataSet dataSet = achievedRight.canReadAlways() ? DataSet.AllOpen
                        : achievedRight.canReadCompleted() ? DataSet.ApprovalRequested
                                : DataSet.None;
                List<ProposalInfo> infosForIk = _nubRequestFacade.getNubRequestInfos(partnerId, ik, dataSet, getFilter());
                infos.addAll(infosForIk);

            }
            _partnerNubsForEdit.put(partnerId, infos);
        }
        return _partnerNubsForEdit.get(partnerId);
    }

    public List<Account> getPartnersForDisplay() {
        return _cooperationTools.getPartnersForDisplay(Feature.NUB);
    }

    Map<Integer, List<ProposalInfo>> _partnerNubsForDisplay = new HashMap<>();

    public List<ProposalInfo> getNubRequestsForDisplayFromPartner(int partnerId, int year) {
        if (!_partnerNubsForDisplay.containsKey(partnerId)) {
            if (partnerId == _sessionController.getAccountId()) {
                List<ProposalInfo> infos = _nubRequestFacade.getNubRequestInfos(_sessionController.getAccountId(), -1, year, DataSet.AllSealed, getFilter());
                _partnerNubsForDisplay.put(partnerId, infos);
            } else {
                List<ProposalInfo> infos = new ArrayList<>();
                Set<Integer> iks = _cooperationTools.getPartnerIks(Feature.NUB, partnerId);
                for (int ik : iks) {
                    CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.NUB, partnerId, ik);
                    DataSet dataSet = achievedRight.canReadSealed() ? DataSet.AllSealed : DataSet.None;
                    List<ProposalInfo> infosForIk = _nubRequestFacade.getNubRequestInfos(partnerId, ik, year, dataSet, getFilter());
                    infos.addAll(infosForIk);
                }
                _partnerNubsForDisplay.put(partnerId, infos);
            }
        }
        return _partnerNubsForDisplay.get(partnerId);
    }

// </editor-fold>
}
