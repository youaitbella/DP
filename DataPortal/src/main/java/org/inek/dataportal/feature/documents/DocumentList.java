package org.inek.dataportal.feature.documents;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.cooperation.CooperationRequestFacade;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.DocInfo;

@Named
@RequestScoped
public class DocumentList {

    @Inject
    AccountDocumentFacade _accountDocFacade;
    @Inject
    SessionController _sessionController;
    @Inject PortalMessageFacade _messageFacade;
    @Inject CooperationRequestFacade _cooperationRequestFacade;
    
    public List<DocInfo> getDocuments() {
        return _accountDocFacade.getDocInfos(_sessionController.getAccountId());
    }

    public boolean renderDocList() {
        return getDocuments().size() > 0;
    }

    public String readDoc(int docId) {
        if (_accountDocFacade.isDocRead(docId)) {
            return "tick.png";
        }
        return "error.png";
    }

    public boolean renderNumDocs(String topic) {
        if (topic.equals("Dokumente")) {
            List<DocInfo> docs = getDocuments();
            if (getDocuments().size() > 0) {
                for (DocInfo doc : docs) {
                    if (!_accountDocFacade.isDocRead((int) doc.getId())) {
                        return true;
                    }
                }
            }
        }
        if (topic.equals(Utils.getMessage("lblCooperation"))) {
            return _messageFacade.countUnreadMessages(_sessionController.getAccountId()) > 0
                    || _cooperationRequestFacade.getOpenCooperationRequestCount(_sessionController.getAccountId()) > 0;
        }
        return false;
    }

    public String getNumberOfUnreadDocs(String topic) {
        int count = 0;
        if (topic.equals("Dokumente")) {
            List<DocInfo> docs = new ArrayList<>();
            for (DocInfo doc : getDocuments()) {
                if (!_accountDocFacade.isDocRead((int) doc.getId())) {
                    docs.add(doc);
                }
            }
            count = docs.size();
        }
        if (topic.equals(Utils.getMessage("lblCooperation"))) {
            count = _messageFacade.countUnreadMessages(_sessionController.getAccountId())
                    + (int) _cooperationRequestFacade.getOpenCooperationRequestCount(_sessionController.getAccountId());
        }
        return "" + count;
    }

}
