package org.inek.dataportal.feature.documents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.cooperation.CooperationRequestFacade;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.DocInfo;

@Named
@ViewScoped
public class DocumentList implements Serializable{

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
        Stream<DocInfo> docInfoStream = _documents.stream();
        if (!_filter.isEmpty()) {
            docInfoStream = docInfoStream.filter(d -> d.getName().toLowerCase().contains(_filter) || d.getDomain().toLowerCase().contains(_filter));
        }
        int direction = _isDescending ? -1 : 1;
        switch (_sortCriteria.toLowerCase()) {
            case "domain":
                docInfoStream = docInfoStream.sorted((n1, n2) -> direction * n1.getDomain().compareTo(n2.getDomain()));
                break;
            case "document":
                docInfoStream = docInfoStream.sorted((n1, n2) -> direction * n1.getName().compareTo(n2.getName()));
                break;
            case "validuntil":
                docInfoStream = docInfoStream.sorted((n1, n2) -> direction * n1.getValidUntil().compareTo(n2.getValidUntil()));
                break;
            case "date":
                docInfoStream = docInfoStream.sorted((n1, n2) -> direction * n1.getCreated().compareTo(n2.getCreated()));
                break;
            case "read":
                docInfoStream = docInfoStream.sorted((n1, n2) -> direction * Boolean.compare(n1.isRead(), n2.isRead()));
                break;
            default:
        }
        return docInfoStream.collect(Collectors.toList());

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
