package org.inek.dataportal.feature.nub;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.feature.nub.entities.NubRequest;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.feature.nub.facades.NubRequestFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScopedContextHolder;
import org.inek.dataportal.common.utils.DocumentationUtil;

@Named
@RequestScoped
public class NubRequestList {

    @Inject private NubSessionTools _nubSessionTools;
    @Inject private NubRequestFacade _nubRequestFacade;
    @Inject private SessionController _sessionController;

    public String getRejectReason(int requestId) {
        String reason = WorkflowStatus.Rejected.getDescription();
        NubRequest proposal = _nubRequestFacade.find(requestId);
        if (proposal != null) {
            reason += " Grund: " + proposal.getErrorText();
        }
        return reason;
    }

    public String newNubRequest() {
        // if the user hit the browser's back-button, a request might be still active. 
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditNubRequest");
        return Pages.NubEditAddress.URL();
    }

    public String newNubRequestFromTemplate() {
        return Pages.NubFromTemplate.URL();
    }

    public String gotoNubInfo() {
        return Pages.NubMethodInfo.RedirectURL();
    }

    public String editNubRequest(int requestId) {
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditNubRequest");
        return Pages.NubEditAddress.URL();
    }

    public String getConfirmMessage(int requestId) {
        NubRequest proposal = _nubRequestFacade.find(requestId);
        if (proposal == null) {
            return "alert('NUB wurde bereits an anderer Stelle gelöscht oder kann derzeit nicht gelöscht werden.'); return false;";
        }
        String msg = proposal.getName() + "\n"
                + (proposal.getStatus().getId() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.
                getMessage("msgConfirmRetire"));
        msg = msg.replace("\r\n", "\n").replace("\n", "\\r\\n").replace("'", "\\'").replace("\"", "\\'");
        return "return confirm ('" + msg + "');";
    }

    public String deleteNubRequest(int requestId) {
        NubRequest nubRequest = _nubRequestFacade.find(requestId);
        if (nubRequest == null) {
            return "";
        }
        if (_sessionController.isMyAccount(nubRequest.getAccountId())) {
            if (nubRequest.getStatus().getId() < WorkflowStatus.Provided.getId()) {
                _nubRequestFacade.delete(nubRequest);
            } else if (nubRequest.getExternalState().trim().isEmpty()) {
                nubRequest.setStatus(WorkflowStatus.Retired);
                nubRequest.setLastChangedBy(_sessionController.getAccountId());
                _nubRequestFacade.saveNubRequest(nubRequest);
                _nubSessionTools.sendNubConfirmationMail(nubRequest);

            }
        }
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
