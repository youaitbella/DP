package org.inek.dataportal.base.feature.requestsystem.backingbean;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.base.feature.requestsystem.dao.RequestInfo;
import org.inek.dataportal.base.feature.requestsystem.entity.Request;
import org.inek.dataportal.base.feature.requestsystem.facade.RequestFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class RequestList {
    @Inject private RequestFacade _requestFacade;
    @Inject private SessionController _sessionController;

    public List<RequestInfo> getRequests() {
        return _requestFacade.getRequestInfos(_sessionController.getAccountId(), false);
    }
    
    public List<RequestInfo> getSealedRequests() {
        return _requestFacade.getRequestInfos(_sessionController.getAccountId(), true);
    }
    
    public String newRequest() {
        return Pages.RequestEdit.URL();
    }
    
    public String editRequest(int requestId) {
        Utils.getFlash().put("reqId", requestId);
        return Pages.RequestEdit.URL();
    }
    
    public String deleteRequest(int requestId) {
        Request request = _requestFacade.find(requestId);
        if(_sessionController.isMyAccount(request.getAccountId())) {
            _requestFacade.remove(request);
        }
        return null;
    }
    
    public String printRequest(int requestId) {
        Utils.getFlash().put("headLine", Utils.getMessage("nameREQUEST_SYSTEM"));
        Utils.getFlash().put("targetPage", Pages.RequestSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_requestFacade.find(requestId)));
        return Pages.PrintView.URL();
    }
    
    
}
