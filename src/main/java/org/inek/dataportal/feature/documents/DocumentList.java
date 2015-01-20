package org.inek.dataportal.feature.documents;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.Triple;

@Named
@RequestScoped
public class DocumentList {

    @Inject
    AccountDocumentFacade _accountDocFacade;
    @Inject
    SessionController _sessionController;
    @Inject PortalMessageFacade _messageFacade;

    public List<Triple> getDocuments() {
        return _accountDocFacade.getDocInfos(_sessionController.getAccountId());
    }

    public boolean renderDocList() {
        return getDocuments().size() > 0;
    }

    public String readDoc(int docId) {
        if (_accountDocFacade.readDoc(docId)) {
            return "tick.png";
        }
        return "error.png";
    }

    public boolean renderNumDocs(String topic) {
        if (topic.equals("Dokumente")) {
            List<Triple> docs = getDocuments();
            if (getDocuments().size() > 0) {
                for (Triple doc : docs) {
                    if (!_accountDocFacade.readDoc((int) doc.getValue1())) {
                        return true;
                    }
                }
            }
        }
        if (topic.equals(Utils.getMessage("lblCooperation"))) {
            int count = _messageFacade.countUnreadMessages(_sessionController.getAccountId());
            return count != 0;
        }
        return false;
    }

    public String getNumberOfUnreadDocs(String topic) {
        int count = 0;
        if (topic.equals("Dokumente")) {
            List<Triple> docs = new ArrayList<>();
            for (Triple doc : getDocuments()) {
                if (!_accountDocFacade.readDoc((int) doc.getValue1())) {
                    docs.add(doc);
                }
            }
            count = docs.size();
        }
        if (topic.equals(Utils.getMessage("lblCooperation"))) {
            count = _messageFacade.countUnreadMessages(_sessionController.getAccountId());
        }
        return "" + count;
    }

}
