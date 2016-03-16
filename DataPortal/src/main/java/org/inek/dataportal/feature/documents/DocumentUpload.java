/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.documents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Agency;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountDocument;
import org.inek.dataportal.entities.account.DocumentDomain;
import org.inek.dataportal.enums.DocumentTarget;
import org.inek.dataportal.facades.AgencyFacade;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.account.DocumentDomainFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.utils.StreamUtils;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "DocumentUpload")
public class DocumentUpload {

    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;

    public DocumentUpload() {
        System.out.println("ctor DocumentUpload");
    }
    // <editor-fold defaultstate="collapsed" desc="Property DocumentTarget">
    private DocumentTarget _documentTarget = DocumentTarget.Account;

    public DocumentTarget getDocumentTarget() {
        return _documentTarget;
    }

    public void setDocumentTarget(DocumentTarget documentTarget) {
        _documentTarget = documentTarget;
        loadLastDocuments();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Account">
    private Account _account;

    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        _account = account;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Agency">
    private Integer _agencyId;

    public Integer getAgency() {
        return _agencyId;
    }

    public void setAgency(Integer agency) {
        _agencyId = agency;
        loadLastDocuments();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Agencies">
    @Inject AgencyFacade _agencyFacade;

    public List<Agency> getAgencies() {
        return _agencyFacade.findAll();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property AvailableUntil">
    private int _availability = 60;

    public int getAvailability() {
        return _availability;
    }

    public void setAvailability(int availability) {
        _availability = availability;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Domain">
    private DocumentDomain _domain;

    public Integer getDomainId() {
        return _domain == null ? null :  _domain.getId();
    }

    public void setDomainId(Integer domainId) {
        _domain = domainId == null ? null : _domainFacade.find(domainId);
    }
    // </editor-fold>

    @Inject DocumentDomainFacade _domainFacade;

    public List<DocumentDomain> getDomains() {
        return _domainFacade.findAll();
    }

    // <editor-fold defaultstate="collapsed" desc="Property File">
    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }
    // </editor-fold>

    public String getEmail() {
        return _account == null ? "" : _account.getEmail();
    }

    public void setEmail(String email) {
        _account = _accountFacade.findByMailOrUser(email);
        loadLastDocuments();
    }

    public void setAccountId(int accountId) {
        _account = _accountFacade.find(accountId);
        loadLastDocuments();
    }

    public int getAccountId() {
        return _account == null ? 0 : _account.getId();
    }

    public void checkEmail(FacesContext context, UIComponent component, Object value) {
        String email = (String) value;
        Account account = _accountFacade.findByMailOrUser(email);
        if (account == null) {
            String msg = Utils.getMessage("errUnknownEmail");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkAccountId(FacesContext context, UIComponent component, Object value) {
        Account account = _accountFacade.find((int) value);
        if (account == null) {
            String msg = Utils.getMessage("errUnknownAccount");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    private List<String> _docs = new ArrayList<>();

    public List<String> getLastDocuments() {
        return _docs;
    }

    public void loadLastDocuments() {
        _docs.clear();
        if (_documentTarget == DocumentTarget.Account) {
            addDocuments(_docs, _account);
        } else {
            if (_agencyId == null) {
                return;
            }
            Agency agency = _agencyFacade.find(_agencyId);
            if (agency == null) {
                return;
            }
            for (Account account : agency.getAccounts()) {
                addDocuments(_docs, account);
            }
        }
    }

    private void addDocuments(List<String> docs, Account account) {
        if (account == null) {
            return;
        }
        for (String name : _docFacade.getNewDocs(account.getId())) {
            if (!docs.contains(name)) {
                docs.add(name);
            }
        }
    }

    public void uploadDoc() {
    }

    public String saveDocument() {
        try {
            if (_file != null) {
                byte[] document = StreamUtils.stream2blob(_file.getInputStream());
                String name = _file.getSubmittedFileName();
                if (_documentTarget == DocumentTarget.Account) {
                    storeDocument(name, document, _account.getId());
                } else {
                    Agency agency = _agencyFacade.find(_agencyId);
                    for (Account account : agency.getAccounts()) {
                        storeDocument(name, document, account.getId());
                    }
                }
                _sessionController.alertClient("Gespeichert: " + _file.getName());
                _file = null;
            }
        } catch (IOException | NoSuchElementException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Upload failed!"));
        }
        return "";
    }

    @Inject AccountDocumentFacade _docFacade;

    private void storeDocument(String name, byte[] document, int accountId) {
        AccountDocument accountDocument = new AccountDocument();
        accountDocument.setAccountId(accountId);
        accountDocument.setContent(document);
        accountDocument.setName(name);
        accountDocument.setDomain(_domain);
        accountDocument.setUploadAccountId(_sessionController.getAccountId());
        _docFacade.save(accountDocument);
    }

}
