package org.inek.dataportal.insurance.nub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.insurance.nub.entities.InsuranceNubNotice;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.insurance.facade.InsuranceFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScopedContextHolder;
import org.inek.dataportal.common.utils.DocumentationUtil;
import org.inek.dataportal.insurance.nub.dao.InsuranceNubNoticeInfo;

@Named
@RequestScoped
public class InsuranceNubNoticeList {

    private static final Logger LOGGER = Logger.getLogger("InsuranceNubNoticeList");
    private Map<Integer, List<InsuranceNubNoticeInfo>> _cache = new HashMap<>();
    @Inject private InsuranceFacade _insuranceFacade;
    @Inject private SessionController _sessionController;

    public List<InsuranceNubNoticeInfo> getOpenNotices() {
        if (!_cache.containsKey(1)) {
            _cache.put(1, _insuranceFacade.getAccountNoticeInfos(_sessionController.getAccountId(), DataSet.AllOpen));
        }
        return _cache.get(1);
    }

    public List<InsuranceNubNoticeInfo> getProvidedNotices() {
        if (!_cache.containsKey(2)) {
            _cache.put(2, _insuranceFacade.getAccountNoticeInfos(_sessionController.getAccountId(), DataSet.AllSealed));
        }
        return _cache.get(2);
    }

    public String editNotice() {
        // if the user hit the browser's back-button, a request might be still active. 
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("Insurance");
        return Pages.InsuranceNubNoticeEdit.URL();
    }

    public String getConfirmMessage(int noticeId) {
        InsuranceNubNoticeInfo notice = retrieveInfoFromCache(noticeId);
        String msg = "Meldung für " + notice.getHospitalIk() + "\n"
                + (notice.getStatus().getId() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.
                getMessage("msgConfirmRetire"));
        msg = msg.replace("\r\n", "\n").replace("\n", "\\r\\n").replace("'", "\\'").replace("\"", "\\'");
        return "return confirm ('" + msg + "');";
    }

    private InsuranceNubNoticeInfo retrieveInfoFromCache(int noticeId) {
        List<InsuranceNubNoticeInfo> infos = _cache.get(1).stream().filter(i -> i.getId() == noticeId).
                collect(Collectors.toList());
        if (!infos.isEmpty()) {
            return infos.get(0);
        }
        infos = _cache.get(2).stream().filter(i -> i.getId() == noticeId).collect(Collectors.toList());
        return infos.get(0);
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
                notice.setStatus(WorkflowStatus.Retired);
                _insuranceFacade.saveNubNotice(notice);
            }
        }
        return "";
    }

    public String printNotice(int noticeId) {
        Utils.getFlash().put("headLine", Utils.getMessage("nameNUB_NOTICE"));
        Utils.getFlash().put("targetPage", Pages.InsuranceNubNoticeSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.
                getDocumentation(_insuranceFacade.findNubNotice(noticeId)));
        return Pages.PrintView.URL();
    }

}
