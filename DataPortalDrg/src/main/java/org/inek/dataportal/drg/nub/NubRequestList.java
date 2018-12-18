package org.inek.dataportal.drg.nub;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.ReportController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.drg.nub.entities.NubRequest;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.drg.nub.facades.NubRequestFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScopedContextHolder;

@Named
@RequestScoped
public class NubRequestList {

    private NubSessionTools _nubSessionTools;
    private NubRequestFacade _nubRequestFacade;
    private SessionController _sessionController;
    private ReportController _reportController;

    public NubRequestList() {
    }

    @Inject
    public NubRequestList(NubSessionTools nubSessionTools,
            NubRequestFacade nubRequestFacade,
            SessionController sessionController,
            ReportController reportController) {
        _nubSessionTools = nubSessionTools;
        _nubRequestFacade = nubRequestFacade;
        _sessionController = sessionController;
        _reportController = reportController;
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
        nubRequest.setLastChangedBy(_sessionController.getAccountId());
        if (nubRequest.getStatus().getId() < WorkflowStatus.Provided.getId()) {
            _nubRequestFacade.delete(nubRequest);
        } else if (nubRequest.getExternalState().trim().isEmpty()) {
            nubRequest.setStatus(WorkflowStatus.Retired);
            _nubRequestFacade.saveNubRequest(nubRequest);
            _nubSessionTools.sendNubConfirmationMail(nubRequest);

        }
        return "";
    }

    public String printNubRequest(int requestId) {
        _reportController.createSingleDocument("NUB.pdf", requestId, "NUB_N" + requestId + ".pdf");
        return "";
    }

    public String getExternalState(int requestId) {
        NubRequest nubRequest = _nubRequestFacade.find(requestId);
        return nubRequest.getExternalState();
    }

}
