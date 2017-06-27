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
import org.inek.dataportal.entities.account.AccountDocument;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.ConfigFacade;
import org.inek.dataportal.facades.cooperation.CooperationRequestFacade;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.helper.TransferFileCreator;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.DocInfo;

@Named
@ViewScoped
public class DocumentList implements Serializable{
    
    @Inject private AccountFacade _accFacade;
    @Inject private AccountDocumentFacade _accountDocFacade;
    @Inject private SessionController _sessionController;
    @Inject private PortalMessageFacade _messageFacade;
    @Inject private CooperationRequestFacade _cooperationRequestFacade;
    @Inject private ConfigFacade _configFacade;

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

    public String readDoc(int docId) {
        if (_accountDocFacade.isDocRead(docId)) {
            return "tick.png";
        }
        return "error.png";
    }

    public boolean renderNumDocs(String topic) {
        if ("Dokumente".equals(topic)) {
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
        if ("Dokumente".equals(topic)) {
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
    
    public boolean hasSelected() {
        return _documents.stream().anyMatch((_doc) -> (_doc.isSelected()));
    }
    
    public void sendDocumentsToProcess() {
        List<AccountIk> senders = new ArrayList<>();
        List<DocInfo> _processDocs = new ArrayList<>();
        createSenderList(_processDocs, senders);
        createTransferFiles(senders, _processDocs);
        _documents = _accountDocFacade.getDocInfos(_sessionController.getAccountId());
    }

    private void createSenderList(List<DocInfo> _processDocs, List<AccountIk> senders) {
        for(DocInfo info : _documents) {
            if(!info.isSelected()) {
                continue;
            }
            _processDocs.add(info);
            AccountIk accIk = new AccountIk(info.getAgentId(), info.getSenderIk());
            boolean contains = false;
            for(AccountIk ai : senders) {
                if(ai.getIk() == info.getSenderIk()&& info.getAgentId() == ai.getAccountId()) {
                    contains = true;
                    break;
                }
            }
            if(contains)
                continue;
            senders.add(accIk);
        }
    }

    private void createTransferFiles(List<AccountIk> senders, List<DocInfo> _processDocs) {
        for(AccountIk accIk : senders) {
            List<AccountDocument> docs = new ArrayList<>();
            String email = _accFacade.find(accIk.getAccountId()).getEmail();
            String subject = "InEK Datenportal - Dokument(e) für IK " + accIk.getIk() + " bearbeitet";
            if(accIk.getIk() < 1) {
                subject = "InEK Datenportal - Dokument(e) bearbeitet";
            }
            for(DocInfo info : _processDocs) {
                if(info.getAgentId() != accIk.getAccountId() || info.getSenderIk() != accIk.getIk()) {
                    continue;
                }
                AccountDocument doc = _accountDocFacade.find(info.getId());
                docs.add(doc);
            }
            try {
                TransferFileCreator.createInekDocumentFiles(_configFacade, docs, email, subject);
                markDocsAsProcessed(docs);
            }
            catch(Exception ex) { }
        }
    }
    
    private void markDocsAsProcessed(List<AccountDocument> docs) {
        for(AccountDocument doc : docs) {
            doc.setSendToProcess(true);
            _accountDocFacade.merge(doc);
        }
    }
    
    private class AccountIk {
        private Integer _accountId;
        private Integer _ik;
        
        public AccountIk(Integer accountId, Integer ik) {
            _accountId = accountId;
            _ik = ik;
        }

        public Integer getAccountId() {
            return _accountId;
        }

        public void setAccountId(Integer accountId) {
            this._accountId = accountId;
        }

        public Integer getIk() {
            return _ik;
        }

        public void setIk(Integer ik) {
            this._ik = ik;
        }
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
