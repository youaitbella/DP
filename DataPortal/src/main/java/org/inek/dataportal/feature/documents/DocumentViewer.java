package org.inek.dataportal.feature.documents;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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

    List<DocInfo> _documents = Collections.emptyList();

    public List<DocInfo> getDocuments() {
        ensureDocuments();
        int direction = _isDescending ? -1 : 1;
        switch (_sortCriteria) {
            case "receipient":
                return _documents.stream().sorted((n1, n2) -> direction * n1.getTag().compareTo(n2.getTag())).collect(Collectors.toList());
            case "domain":
                return _documents.stream().sorted((n1, n2) -> direction * n1.getDomain().compareTo(n2.getDomain())).collect(Collectors.toList());
            case "document":
                return _documents.stream().sorted((n1, n2) -> direction * n1.getName().compareTo(n2.getName())).collect(Collectors.toList());
            case "date":
                return _documents.stream().sorted((n1, n2) -> direction * n1.getLongCreatedString().compareTo(n2.getLongCreatedString())).collect(Collectors.toList());
            case "read":
                return _documents.stream().sorted((n1, n2) -> direction * Boolean.compare(n1.isRead(), n2.isRead())).collect(Collectors.toList());
            default:
                return _documents;
        }
    }

    private void ensureDocuments() {
        if (!_documents.isEmpty()) {
            return;
        }
        if (_sessionController.isInekUser(Feature.ADMIN)) {
            _documents = _accountDocFacade.getSupervisedDocInfos(-1, _filter, _maxAge);
        } else {
            _documents = _accountDocFacade.getSupervisedDocInfos(_sessionController.getAccountId(), _filter, _maxAge);
        }
    }

    public boolean renderDocList() {
        return getDocuments().size() > 0;
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
