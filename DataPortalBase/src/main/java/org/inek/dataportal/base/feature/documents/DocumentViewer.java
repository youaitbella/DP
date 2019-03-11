package org.inek.dataportal.base.feature.documents;

import org.inek.dataportal.base.facades.account.DocumentFacade;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRequestFacade;
import org.inek.dataportal.common.data.cooperation.facade.PortalMessageFacade;
import org.inek.dataportal.common.helper.structures.DocInfo;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class DocumentViewer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private DocumentFacade _accountDocFacade;
    @Inject private SessionController _sessionController;
    @Inject private PortalMessageFacade _messageFacade;
    @Inject private CooperationRequestFacade _cooperationRequestFacade;

    private String _filter = "";
    private int _maxAge = 30;
    private int _agentId = -1;

    @PostConstruct
    private void init() {
        _agentId = _sessionController.getAccountId();
    }

    public int getAgentId() {
        return _agentId;
    }

    public void setAgentId(int agentId) {
        this._agentId = agentId;
    }

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

    private List<DocInfo> _documents = Collections.emptyList();

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
                return _documents.stream().sorted((n1, n2) -> direction * n1.getCreated().compareTo(n2.getCreated())).collect(Collectors.toList());
            case "read":
                return _documents.stream().sorted((n1, n2) -> direction * Boolean.compare(n1.isRead(), n2.isRead())).collect(Collectors.toList());
            default:
                return _documents;
        }
    }

    public List<SelectItem> getSupervisingAgents() {
        List<SelectItem> agents = _accountDocFacade.getSupervisingAccounts(_maxAge);
        agents.add(new SelectItem(0, "Prozess"));
        return agents;
    }

    private void ensureDocuments() {
        if (!_documents.isEmpty()) {
            return;
        }
        List<Integer> agentIds = new ArrayList<>();
        if (_agentId >= 0) {
            agentIds.add(_agentId);
        } else {
            agentIds = _accountDocFacade.getAgentIds();
        }
        _documents = _accountDocFacade.getSupervisedDocInfos(agentIds, _filter, _maxAge);
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
