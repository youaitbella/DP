package org.inek.dataportal.feature.documents;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.cooperation.CooperationRequestFacade;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.helper.structures.DocInfo;

@Named
@ViewScoped
public class DocumentViewer implements Serializable {

    @Inject AccountDocumentFacade _accountDocFacade;
    @Inject SessionController _sessionController;
    @Inject PortalMessageFacade _messageFacade;
    @Inject CooperationRequestFacade _cooperationRequestFacade;

    private String _filter = "";
    private int _maxAge = 30;

    public String getFilter() {
        return _filter;
    }

    public void setFilter(String filter) {
        _filter = filter;
        _documents.clear();
    }

    public String reload() {
        return "";
    }

    public int getMaxAge() {
        return _maxAge;
    }

    public void setMaxAge(int maxAge) {
        _maxAge = maxAge;
        _documents.clear();
    }

    List<DocInfo> _documents = Collections.EMPTY_LIST;
    public List<DocInfo> getDocuments() {
        ensureDocuments();
        return _documents;
    }

    private void ensureDocuments() {
        if (!_documents.isEmpty()){return;}
        if (_sessionController.isInekUser(Feature.ADMIN)) {
            _documents = _accountDocFacade.getSupervisedDocInfos(-1, _filter, _maxAge);
        } else {
            _documents = _accountDocFacade.getSupervisedDocInfos(_sessionController.getAccountId(), _filter, _maxAge);
        }
    }

    public boolean renderDocList() {
        return getDocuments().size() > 0;
    }

}
