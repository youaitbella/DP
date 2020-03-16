package org.inek.dataportal.base.utils;

import org.inek.dataportal.base.facades.account.DocumentFacade;
import org.inek.dataportal.base.feature.approval.ApprovalFacade;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRequestFacade;
import org.inek.dataportal.common.data.cooperation.facade.PortalMessageFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.helper.structures.DocInfo;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class MenuHighlighter {
    private DocumentFacade documentFacade;
    private SessionController sessionController;
    private PortalMessageFacade messageFacade;
    private CooperationRequestFacade cooperationRequestFacade;
    private ApprovalFacade approvalFacade;

    private List<DocInfo> docs;

    @Inject
    public MenuHighlighter(SessionController sessionController,
                           DocumentFacade documentFacade,
                           PortalMessageFacade messageFacade,
                           CooperationRequestFacade cooperationRequestFacade,
                           ApprovalFacade approvalFacade) {
        this.sessionController = sessionController;
        this.documentFacade = documentFacade;
        this.messageFacade = messageFacade;
        this.cooperationRequestFacade = cooperationRequestFacade;
        this.approvalFacade = approvalFacade;
    }

    @PostConstruct
    private void init() {
        docs = documentFacade.getDocInfos(sessionController.getAccountId());
    }

    public boolean renderHighlightMarker(String topic) { // do not delete, used in PortalMenuTemplate
        if ("Dokumente".equals(topic)) {
            return docs.stream().anyMatch(d -> !d.isRead());
        }
        if ("Rückmeldung".equals(topic)) {
            return approvalFacade.hasUnreadOrNonApprovedData(sessionController.getAccountId());
        }
        if (topic.equals(Utils.getMessage("lblCooperation"))) {
            return messageFacade.countUnreadMessages(sessionController.getAccountId()) > 0
                    || cooperationRequestFacade.getOpenCooperationRequestCount(sessionController.getAccountId()) > 0;
        }
        return false;
    }

    public String getCount(String topic) { // do not delete, used in PortalMenuTemplate
        long count = 0;
        if ("Dokumente".equals(topic)) {
            count = docs.stream().filter(d -> !d.isRead()).count();
        }
        if ("Rückmeldung".equals(topic)) {
            count = approvalFacade.countUnreadOrNonApprovedData(sessionController.getAccountId());
        }
        if (topic.equals(Utils.getMessage("lblCooperation"))) {
            count = messageFacade.countUnreadMessages(sessionController.getAccountId())
                    + (int) cooperationRequestFacade.getOpenCooperationRequestCount(sessionController.getAccountId());
        }
        return count == 0 ? "" : "" + count;
    }

}
