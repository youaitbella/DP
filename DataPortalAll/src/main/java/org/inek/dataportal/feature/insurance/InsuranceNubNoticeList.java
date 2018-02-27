package org.inek.dataportal.feature.insurance;

import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.insurance.InsuranceNubNotice;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.facades.InsuranceFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.common.utils.DocumentationUtil;

@Named
@RequestScoped
public class InsuranceNubNoticeList {

    private static final Logger LOGGER = Logger.getLogger("InsuranceNubNoticeList");

    @Inject private InsuranceFacade _insuranceFacade;
    @Inject private SessionController _sessionController;

    public List<InsuranceNubNotice> getOpenNotices() {
        return _insuranceFacade.getAccountNotices(_sessionController.getAccountId(), DataSet.AllOpen);
    }

    public List<InsuranceNubNotice> getProvidedNotices() {
        return _insuranceFacade.getAccountNotices(_sessionController.getAccountId(), DataSet.AllSealed);
    }

    public String editNotice() {
        // if the user hit the browser's back-button, a request might be still active. 
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("Insurance");
        return Pages.InsuranceNubNoticeEditAddress.URL();
    }

    public String getConfirmMessage(int noticeId) {
        InsuranceNubNotice notice = _insuranceFacade.findFreshNubNotice(noticeId);
        String msg = "Meldung für " + notice.getHospitalIk() + "\n"
                + (notice.getWorkflowStatus().getId() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.getMessage("msgConfirmRetire"));
        msg = msg.replace("\r\n", "\n").replace("\n", "\\r\\n").replace("'", "\\'").replace("\"", "\\'");
        return "return confirm ('" + msg + "');";
    }
    public String deleteNotice(int noticeId) {
        InsuranceNubNotice notice = _insuranceFacade.findNubNotice(noticeId);
        if (notice == null) {
            return "";
        }
        if (_sessionController.isMyAccount(notice.getAccountId())) {
            if (notice.getWorkflowStatusId() < WorkflowStatus.Provided.getId()) {
                _insuranceFacade.delete(notice);
            } else {
                notice.setWorkflowStatus(WorkflowStatus.Retired);
                _insuranceFacade.saveNubNotice(notice);
            }
        }
        return "";
    }
    
    public String printNotice(int noticeId) {
        Utils.getFlash().put("headLine", Utils.getMessage("nameINSURANCE"));
        Utils.getFlash().put("targetPage", Pages.InsuranceSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_insuranceFacade.findNubNotice(noticeId)));
        return Pages.PrintView.URL();
    }

}
