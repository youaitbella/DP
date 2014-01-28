package org.inek.dataportal.feature.nub;

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
        return _accountDocFacade.readDoc(docId);
    }
    
    public boolean renderNumDocs(String topic) {
        if(!topic.equals("Dokumente")) {
            return false;
        }
        if(getDocuments().size() <= 0) {
            return false;
        }
        return true;
    }
    
    public String getNumberOfDocs() {
        return getDocuments().size() + "";
    }
}
