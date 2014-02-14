package org.inek.dataportal.feature.requestsystem;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Request;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.RequestFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.helper.structures.Pair;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class RequestList {
    @Inject RequestFacade _requestFacade;
    @Inject SessionController _sessionController;

    public List<Pair> getRequests() {
        return _requestFacade.getRequestInfos(_sessionController.getAccountId(), false);
    }
    
    public List<Pair> getSealedRequests() {
        return _requestFacade.getRequestInfos(_sessionController.getAccountId(), true);
    }
    
    public String newRequest() {
        Utils.getFlash().put("conversationId", _sessionController.beginConversation());
        return Pages.RequestEdit.URL();
    }
    
    public String editRequest(int requestId) {
        Utils.getFlash().put("reqId", requestId);
        Utils.getFlash().put("conversationId", _sessionController.beginConversation());
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
