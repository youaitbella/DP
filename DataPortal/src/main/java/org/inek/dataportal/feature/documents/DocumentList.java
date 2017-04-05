package org.inek.dataportal.feature.documents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
public class DocumentList{

    @Inject AccountDocumentFacade _accountDocFacade;
    @Inject SessionController _sessionController;
    @Inject PortalMessageFacade _messageFacade;
    @Inject CooperationRequestFacade _cooperationRequestFacade;
    
    private String _filter = "";

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

    List<DocInfo> _documents = Collections.emptyList();

    private void ensureDocuments() {
        if (_documents.isEmpty()) {
            _documents = _accountDocFacade.getDocInfos(_sessionController.getAccountId());
        }
    }
    
    public List<DocInfo> getDocuments() {
        ensureDocuments();
        return _documents.stream().filter(d -> d.getName().toLowerCase().contains(_filter) || d.getDomain().toLowerCase().contains(_filter)).collect(Collectors.toList());
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

    // <editor-fold defaultstate="collapsed" desc="Property SortCriteria + state">    
    private String _sortCriteria = "";
    private boolean _isDescending = false;

    public boolean isDescending() {
        return _isDescending;
    }

    public void setDescending(boolean isDescending) {
        _isDescending = isDescending;
    }

    public void setSortCriteria(String sortCriteria) {
        if (_sortCriteria.equals(sortCriteria)) {
            _isDescending = !_isDescending;
        } else {
            _isDescending = false;
        }
        _sortCriteria = sortCriteria == null ? "" : sortCriteria;
    }

    public String getSortCriteria() {
        return _sortCriteria;
    }
    // </editor-fold>    
    
}
