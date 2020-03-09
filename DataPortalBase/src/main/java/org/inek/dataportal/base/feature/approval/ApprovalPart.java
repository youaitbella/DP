package org.inek.dataportal.base.feature.approval;

import org.inek.dataportal.common.controller.SessionController;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class ApprovalPart {
    private SessionController sessionController;
    private ApprovalFacade approvalFacade;

    public ApprovalPart() {
    }

    @Inject
    public ApprovalPart(SessionController sessionController, ApprovalFacade approvalFacade) {
        this.sessionController = sessionController;
        this.approvalFacade = approvalFacade;
    }

    public boolean getContainsOpenApproval() {
        return approvalFacade.hasUnreadData(sessionController.getAccountId());
    }

}
