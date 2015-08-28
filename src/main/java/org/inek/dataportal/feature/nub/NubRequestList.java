package org.inek.dataportal.feature.nub;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.NubRequest;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.NubRequestFacade;
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
    @Inject NubSessionTools _nubSessionTools;

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

    public String newNubRequest() {
        return Pages.NubEditAddress.URL();
    }

    public String newNubRequestFromTemplate() {
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
        _nubSessionTools.refreshNodes();
        return "";
    }

    public String printNubRequest(int requestId) {
        NubRequest nubRequest = _nubRequestFacade.find(requestId);

        Utils.getFlash().put("headLine", Utils.getMessage("nameNUB") + " " + nubRequest.getExternalId());
        Utils.getFlash().put("targetPage", Pages.NubSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(nubRequest));
        return Pages.PrintView.URL();
    }

    public String getExternalState(int requestId) {
        NubRequest nubRequest = _nubRequestFacade.find(requestId);
        return nubRequest.getExternalState();
    }

}
