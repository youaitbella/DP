package org.inek.dataportal.feature.documents;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.facades.AccountDocumentFacade;
import org.inek.dataportal.helper.structures.Triple;

@Named
@RequestScoped
public class DocumentList {

    @Inject
    AccountDocumentFacade _accountDocFacade;
    @Inject
    SessionController _sessionController;

    public List<Triple> getDocuments() {
        return _accountDocFacade.getDocInfos(_sessionController.getAccountId());
    }
    
    public boolean renderDocList() {
        return getDocuments().size() > 0;
    }
    
    public String readDoc(int docId) {
        if(_accountDocFacade.readDoc(docId)) {
            return "tick.png";
        } 
        return "error.png";
    }
    
    public boolean renderNumDocs(String topic) {
        if(!topic.equals("Dokumente")) {
            return false;
        }
        List<Triple> docs = getDocuments();
        if(getDocuments().size() > 0) {
            for(Triple doc : docs) {
                if(!_accountDocFacade.readDoc((int)doc.getValue1()))
                    return true;
            }
        }
        return false;
    }
    
    public String getNumberOfUnreadDocs() {
        List<Triple> docs = new ArrayList<>();
        for(Triple doc : getDocuments()) {
            if(!_accountDocFacade.readDoc((int)doc.getValue1())) {
                docs.add(doc);
            }
        }
        return docs.size() + "";
    }
}
