package org.inek.dataportal.base.feature.documents;

import org.inek.dataportal.base.facades.account.DocumentFacade;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRequestFacade;
import org.inek.dataportal.common.data.cooperation.facade.PortalMessageFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.helper.structures.DocInfo;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named
@ViewScoped
public class DocumentList implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private DocumentFacade _accountDocFacade;
    @Inject
    private SessionController _sessionController;
    @Inject
    private PortalMessageFacade _messageFacade;
    @Inject
    private CooperationRequestFacade _cooperationRequestFacade;

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

    private List<DocInfo> _documents = Collections.emptyList();

    private void ensureDocuments() {
        _documents = _accountDocFacade.getDocInfos(_sessionController.getAccountId());
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
            case "sender":
                docInfoStream = docInfoStream.sorted((n1, n2) -> direction * n1.getTag().compareTo(n2.getTag()));
                break;
            case "senderik":
                docInfoStream = docInfoStream.sorted((n1, n2) -> direction * (n1.getSenderIk() - n2.getSenderIk()));
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

    //<editor-fold desc="For PortalMenuTemplate">
    public String readDoc(int docId) {  // do not delete, used in PortalMenuTemplate
        if (_accountDocFacade.isDocRead(docId)) {
            return "tick.png";
        }
        return "error.png";
    }

    public boolean renderNumDocs(String topic) { // do not delete, used in PortalMenuTemplate
        if ("Dokumente".equals(topic)) {
            List<DocInfo> docs = getDocuments();
            if (getDocuments().size() > 0) {
                for (DocInfo doc : docs) {
                    if (!_accountDocFacade.isDocRead((int) doc.getAccountDocumentId())) {
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

    public String getNumberOfUnreadDocs(String topic) { // do not delete, used in PortalMenuTemplate
        int count = 0;
        if ("Dokumente".equals(topic)) {
            List<DocInfo> docs = new ArrayList<>();
            for (DocInfo doc : getDocuments()) {
                if (!_accountDocFacade.isDocRead((int) doc.getAccountDocumentId())) {
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
    //</editor-fold>


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
