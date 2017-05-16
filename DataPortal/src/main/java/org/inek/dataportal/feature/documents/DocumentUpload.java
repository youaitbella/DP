/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.documents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.enums.DocumentTarget;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.AgencyFacade;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.account.DocumentDomainFacade;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "DocumentUpload")
public class DocumentUpload implements Serializable {

    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;
    @Inject private Mailer _mailer;
    private final List<AccountDocument> _documents = new ArrayList<>();

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
    @Inject private CustomerFacade _customerFacade;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
        Customer customer = _customerFacade.getCustomerByIK(_ik);
        Set<String> emails = customer.getContacts()
                .stream().filter(c -> c.isActive())
                .flatMap(c -> c.getContactDetails().stream().filter(d -> d.getContactDetailTypeId().equals("E")).map(d -> d.getDetails().toLowerCase()))
                .collect(Collectors.toSet());
        List<Account> accounts = _accountFacade.getAccounts4Ik(_ik, emails);
        _accountRoles = _accountFacade.obtainRoleInfo(_ik, accounts);
        loadLastDocuments();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Agencies">
    @Inject private AgencyFacade _agencyFacade;

    public List<Agency> getAgencies() {
        return _agencyFacade.findAll();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Accounts">
    private Map<Account, String> _accountRoles = Collections.emptyMap();

    public List<Account> getAccounts() {
        // need to return a list rather then a set to keep order
        return _accountRoles.keySet().stream().sorted((a1, a2) -> Boolean.compare(a2.isReportViaPortal(), a1.isReportViaPortal())).collect(Collectors.toList());
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property InekAccounts">
    private List<Account> _inekAccounts = new ArrayList<>();

    public List<Account> getInekAccounts() {
        if (_inekAccounts.isEmpty()){
            _inekAccounts = _accountFacade.getInekAccounts();
        }
        return _inekAccounts;
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

    public String getRoles(Account account) {
        return _accountRoles.get(account);
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
                Agency agency = _agencyFacade.find(_agencyId);
                if (agency == null) {
                    return;
                }
                for (Account account : agency.getAccounts()) {
                    addDocuments(_docs, account);
                }
                break;
            case IK:
                if (_ik <= 0) {
                    return;
                }
                for (Account account : _accountRoles.keySet()) {
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

    public String saveDocument() {
        if (_documents.isEmpty()) {
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
                    Agency agency = _agencyFacade.find(_agencyId);
                    for (Account account : agency.getAccounts()) {
                        storeDocument(accountDocument, account.getId());
                        accounts.add(account);
                    }
                    break;
                case IK:
                    for (Account account : _accountRoles.keySet()) {
                        if (account.isReportViaPortal()){
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

    @Inject private AccountDocumentFacade _docFacade;

    private void storeDocument(AccountDocument accountDocument, int accountId) {
        accountDocument.setAccountId(accountId);
        accountDocument.setDomain(_domain);
        accountDocument.setAgentAccountId(_sessionController.getAccountId());
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

        _mailer.sendMailFrom("datenportal@inek.org", account.getEmail(), bcc, subject, body);
    }

}
