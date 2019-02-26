/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.base.feature.documents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.base.feature.agency.entities.Agency;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.common.data.account.entities.DocumentDomain;
import org.inek.dataportal.base.enums.DocumentTarget;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.base.feature.agency.facades.AgencyFacade;
import org.inek.dataportal.base.facades.account.AccountDocumentFacade;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.base.facades.account.DocumentDomainFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.mail.MailTemplateFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "DocumentUpload")
public class DocumentUpload implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;
    @Inject private Mailer _mailer;
    private final List<AccountDocument> _documents = new ArrayList<>();

    @PostConstruct
    private void init() {
        _senderIk = getSenderIks().isEmpty() 
                ? -1 
                : getSenderIks().size() == 1 ? getSenderIks().stream().findFirst().get() : 0;
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

    // <editor-fold defaultstate="collapsed" desc="Property IK">
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        if (_ik == ik){
            return;
        }
        _ik = ik;
        _accounts = _accountFacade.getAccounts4IkInludeCustomer(_ik);
        _accountFacade.obtainRoleInfo(_ik, _accounts);
        loadLastDocuments();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Agencies">
    @Inject private AgencyFacade _agencyFacade;

    public List<Agency> getAgencies() {
        return _agencyFacade.findAllAgencies();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Accounts">
    private Collection<Account> _accounts = Collections.emptyList();

    public Collection<Account> getAccounts() {
        // need to return a list rather then a set to keep order
        return _accounts;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InekAccounts">
    private final List<Account> _inekAccounts = new ArrayList<>();

    public List<Account> getInekAccounts() {
        if (_inekAccounts.isEmpty()) {
            for (Account account : _accountFacade.getInekContacts(_sessionController.getAccount())) {
                _inekAccounts.add(account);
            }
            for (Account account : _accountFacade.getInekAccounts()) {
                if (!_inekAccounts.contains(account)) {
                    _inekAccounts.add(account);
                }
            }
        }
        for(int i = 0; i < _inekAccounts.size(); i++) {
            Account acc = _inekAccounts.get(i);
            if(acc.isDeactivated())
                _inekAccounts.remove(acc);
        }
        return _inekAccounts;
    }
    // </editor-fold>

    public void ikChanged() {
        // dummy method for composite componen
    }

    //<editor-fold defaultstate="collapsed" desc="Property SenderIk">
    private int _senderIk;
    
    public int getSenderIk() {
        return _senderIk;
    }
    
    public void setSenderIk(int senderIk) {
        this._senderIk = senderIk;
    }
    //</editor-fold>
    
    private Set<Integer> _senderIks;

    public Set<Integer> getSenderIks() {
        if (_senderIks == null) {
            _senderIks = _sessionController.getAccount().getFullIkSet();
        }
        return _senderIks;
    }

    // <editor-fold defaultstate="collapsed" desc="Property AvailableUntil">
    private int _availability = 400;

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

    //<editor-fold defaultstate="collapsed" desc="Getter/Setter">
    private String _mailTemplate;

    public String getMailTemplate() {
        return _mailTemplate;
    }

    public void setMailTemplate(String mailTemplate) {
        _mailTemplate = mailTemplate;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property MailTemplates">
    @Inject private MailTemplateFacade _mailTemplateFacade;

    public List<MailTemplate> getMailTemplates() {
        return _mailTemplateFacade.findTemplatesByFeature(Feature.DOCUMENTS);
    }
    // </editor-fold>    

    @Inject private DocumentDomainFacade _domainFacade;

    public List<DocumentDomain> getDomains() {
        return _domainFacade.findAll();
    } 
    public List<DocumentDomain> getPublicDomains() {
        return _domainFacade.findAll().stream().filter(d -> d.isPublicUsable()).collect(Collectors.toList());
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
            _account = _accountFacade.findAccount((int) value);
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
        switch (_documentTarget) {
            case Account:
                if (_account == null) {
                    return;
                }
                addDocuments(_docs, _account);
                break;
            case Agency:
                if (_agencyId == null) {
                    return;
                }
                for (Account account : _agencyFacade.findAgencyAccounts(_agencyId)) {
                    addDocuments(_docs, account);
                }
                break;
            case IK:
                if (_ik <= 0) {
                    return;
                }
                for (Account account : _accounts) {
                    addDocuments(_docs, account);
                }
                break;
            default:
                break;
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

    public boolean isSaveEnabled() {
        return _documents.size() > 0
                && _domain != null
                && _mailTemplate != null
                && (_documentTarget == DocumentTarget.Account && _account != null
                || _documentTarget == DocumentTarget.Agency && _agencyId != null
                || _documentTarget == DocumentTarget.IK && _ik > 0);
    }

    public boolean isSaveForInekEnabled() {
        return _documents.size() > 0
                && _domain != null 
                && (getSenderIks().size() <= 1 || _senderIk > 0);
    }

    public String saveDocument() {
        if (_documents.isEmpty()) {
            return "";
        }
        if (documentsHasInvalidFilenames()) {
            return "";
        }
        Set<Account> accounts = new HashSet<>();
        for (AccountDocument accountDocument : _documents) {
            accountDocument.setValidity(_availability);

            switch (_documentTarget) {
                case Account:
                    storeDocument(accountDocument, _account.getId());
                    accounts.add(_account);
                    break;
                case Agency:
                    for (Account account : _agencyFacade.findAgencyAccounts(_agencyId)) {
                        storeDocument(accountDocument, account.getId());
                        accounts.add(account);
                    }
                    break;
                case IK:
                    for (Account account : _accounts) {
                        if (account.isSelected()) {
                            storeDocument(accountDocument, account.getId());
                            accounts.add(account);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        for (Account account : accounts) {
            sendNotification(account);
        }
        _documents.clear();
        loadLastDocuments();
        return "";
    }

    private boolean documentsHasInvalidFilenames() {
        List<String> invalidPatterns = _docFacade.getInvalidFileNamePatterns();
        for (AccountDocument doc : _documents) {
            for (String pattern : invalidPatterns) {
                if (doc.getName().matches(pattern)) {
                    DialogController.showErrorDialog("Unzulässiger Dateiname", "[" + doc.getName() + "] " +
                            "entält einen verbotenen Namen. Bitte überprüfen Sie, ob Sie das korrekte Dokument hochladen wollen, " +
                            "und ändern Sie ggf. den Namen und laden Sie das Dokument erneut hoch");
                    return true;
                }
            }
        }
        return false;
    }

    public String saveDocumentForInek() {
        if (_documents.isEmpty()) {
            return "";
        }
        Set<Account> accounts = new HashSet<>();
        for (AccountDocument accountDocument : _documents) {
            accountDocument.setValidity(_availability);

            for (Account account : _inekAccounts) {
                if (account.isSelected()) {
                    storeDocument(accountDocument, account.getId());
                    accounts.add(account);
                }
            }
        }
        if(accounts.isEmpty()) {
            _sessionController.setScript("alert('Bitte wählen Sie mindestens einen Empfänger aus.');");
            return "";
        }
        _mailTemplate = "Neue Dokumente";  // fixed template
        for (Account account : accounts) {
            sendNotification(account);
        }
        _documents.clear();
        _sessionController.setScript("alert('Dokumente ans InEK gesendet.');");
        return "";
    }

    @Inject private AccountDocumentFacade _docFacade;

    private void storeDocument(AccountDocument accountDocument, int accountId) {
        accountDocument.setAccountId(accountId);
        accountDocument.setDomain(_domain);
        accountDocument.setAgentAccountId(_sessionController.getAccountId());
        accountDocument.setSenderIk(_senderIk);
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

    private void sendNotification(Account account) {
        MailTemplate template = _mailer.getMailTemplate(_mailTemplate);
        String salutation = _mailer.getFormalSalutation(account);
        String body = template.getBody().replace("{formalSalutation}", salutation);
        String bcc = template.getBcc();
        String subject = template.getSubject();

        _mailer.sendMailFrom("DSAnfragen@inek-drg.de", account.getEmail(), bcc, subject, body);
    }

}
