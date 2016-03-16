/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.documents;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Agency;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountDocument;
import org.inek.dataportal.entities.account.DocumentDomain;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.enums.DocumentTarget;
import org.inek.dataportal.facades.AgencyFacade;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.account.DocumentDomainFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "DocumentUpload")
public class DocumentUpload {

    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;
    @Inject private Mailer _mailer;
    private List<AccountDocument> _documents = new ArrayList<>();

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
        return _domain == null ? null : _domain.getId();
    }

    public void setDomainId(Integer domainId) {
        _domain = domainId == null ? null : _domainFacade.find(domainId);
    }
    // </editor-fold>

    @Inject DocumentDomainFacade _domainFacade;

    public List<DocumentDomain> getDomains() {
        return _domainFacade.findAll();
    }

    public String getEmail() {
        return _account == null ? "" : _account.getEmail();
    }

    public void setEmail(String email) {
    }

    public void setAccountId(int accountId) {
    }

    public int getAccountId() {
        return _account == null ? 0 : _account.getId();
    }

    public void checkEmail(FacesContext context, UIComponent component, Object value) {
        String email = "" + value;
        _account = _accountFacade.findByMailOrUser(email);
        if (_account != null && _account.getId() <= 0) {
            // system accounts are not allowed here
            _account = null;
        }
        if (_account == null) {
            String msg = Utils.getMessage("errUnknownEmail");
            throw new ValidatorException(new FacesMessage(msg));
        }
        loadLastDocuments();
    }

    public void checkAccountId(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            _account = null;
        } else {
            _account = _accountFacade.find((int) value);
        }
        if (_account != null && _account.getId() <= 0) {
            _account = null;
        }
        if (_account == null) {
            String msg = Utils.getMessage("errUnknownAccount");
            throw new ValidatorException(new FacesMessage(msg));
        }
        loadLastDocuments();
    }

    private List<String> _docs = new ArrayList<>();

    public List<String> getLastDocuments() {
        return _docs;
    }

    private void loadLastDocuments() {
        _docs.clear();
        if (_account == null) {
            return;
        }
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
        if (_documents.isEmpty()) {
            return "";
        }
        for (AccountDocument accountDocument : _documents) {
            if (_documentTarget == DocumentTarget.Account) {
                storeDocument(accountDocument, _account.getId());
            } else {
                Agency agency = _agencyFacade.find(_agencyId);
                for (Account account : agency.getAccounts()) {
                    storeDocument(accountDocument, account.getId());
                }
            }
        }
        sendNotification();
        _documents.clear();
        loadLastDocuments();
        return "";
    }

    @Inject AccountDocumentFacade _docFacade;

    private void storeDocument(AccountDocument accountDocument, int accountId) {
        accountDocument.setAccountId(accountId);
        accountDocument.setDomain(_domain);
        accountDocument.setUploadAccountId(_sessionController.getAccountId());
        _docFacade.save(accountDocument);
    }

    public List<AccountDocument> getDocuments() {
        return _documents;
    }

    public String deleteDocument(AccountDocument doc) {
        _documents.remove(doc);
        return "";
    }

    public String downloadDocument(AccountDocument doc) {
        return Utils.downloadDocument(doc);
    }

    public String refresh() {
        return "";
    }

    private void sendNotification() {
        String subject;
        String body;
        String bcc;

        MailTemplate template = _mailer.getMailTemplate("Neue Dokumente");
        if (template == null) {
            // dump fallback
            bcc = "datenportal@inek.org";
            subject = "Neue Dokumente im InEK Datenportal";
            body = "Guten Tag,\n"
                    + "\n"
                    + "im InEK Datenportal sind neue Dokumente für Sie verfügbar.\n"
                    + "\n"
                    + "Freundliche Grüße\n"
                    + "InEK GmbH";

        } else {
            String salutation = _mailer.getFormalSalutation(_account);
            body = template.getBody().replace("{formalSalutation}", salutation);
            bcc = template.getBcc();
            subject = template.getSubject();
        }

        if (!subject.isEmpty() && !body.isEmpty()) {
            _mailer.sendMailFrom("datenportal@inek.org", _account.getEmail(), bcc, subject, body);
        }
    }

}
