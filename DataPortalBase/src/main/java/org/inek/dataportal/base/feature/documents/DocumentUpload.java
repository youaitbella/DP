package org.inek.dataportal.base.feature.documents;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.base.enums.DocumentTarget;
import org.inek.dataportal.base.facades.account.DocumentDomainFacade;
import org.inek.dataportal.base.facades.account.DocumentFacade;
import org.inek.dataportal.base.feature.agency.entities.Agency;
import org.inek.dataportal.base.feature.agency.facades.AgencyFacade;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.DocumentDomain;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.common.CommonDocument;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.MailTemplateFacade;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.scope.FeatureScoped;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

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
    @Inject
    private DocumentFacade _docFacade;
    @Inject
    private AgencyFacade _agencyFacade;

    private final List<CommonDocument> _documents = new ArrayList<>();

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

    public String getEmail() {
        return _account == null ? "" : _account.getEmail();
    }

    public void setEmail(String email) {
        // dummy to satisfy JSF
    }

    public void setAccountId(int accountId) {
        // dummy to satisfy JSF
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

    public String saveDocument() {
        if (_documents.isEmpty()) {
            return "";
        }
        if (documentsHasInvalidFilenames()) {
            return "";
        }
        Set<Account> accounts = new HashSet<>();
        for (CommonDocument commonDocument : _documents) {
            storeDocument(commonDocument);

            switch (_documentTarget) {
                case Account:
                    _docFacade.createAccountDocument(_account, commonDocument, _availability);
                    accounts.add(_account);
                    break;
                case Agency:
                    for (Account account : _agencyFacade.findAgencyAccounts(_agencyId)) {
                        _docFacade.createAccountDocument(account, commonDocument, _availability);
                        accounts.add(account);
                    }
                    break;
                case IK:
                    for (Account account : _accounts) {
                        if (account.isSelected()) {
                            _docFacade.createAccountDocument(account, commonDocument, _availability);
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
        for (CommonDocument doc : _documents) {
            for (String pattern : invalidPatterns) {
                if (doc.getName().matches(pattern)) {
                    DialogController.showErrorDialog("Unzulässiger Dateiname", "[" + doc.getName() + "] " +
                            "gehört gemäß Dateinamen zu den unzulässigen Dateien. " +
                            "Bitte überprüfen Sie, ob Sie dieses Dokument im Ausnahmefall tatsächlich hochladen müssen. " +
                            "Falls ja, ändern Sie bitte den Namen und laden Sie das Dokument erneut hoch.");
                    return true;
                }
            }
        }
        return false;
    }


    private void storeDocument(CommonDocument commonDocument) {
        commonDocument.setAccountId(_sessionController.getAccountId());
        commonDocument.setDomain(_domain);
        _docFacade.saveCommonDocument(commonDocument);
    }

    public List<CommonDocument> getDocuments() {
        return Collections.unmodifiableList(_documents);
    }

    public CommonDocument findOrCreateByName(String filename) {
        CommonDocument document = _documents
                .stream()
                .filter(d -> d.getName().equals(filename))
                .findFirst()
                .orElse(new CommonDocument(filename));
        if (!_documents.contains(document)) {
            _documents.add(document);
        }
        return document;
    }

    public String deleteDocument(CommonDocument doc) {
        _documents.remove(doc);
        return "";
    }

    public String downloadDocument(CommonDocument document) {
        return Utils.downloadDocument(document);
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

        _mailer.sendMailFrom("Anfragen@datenstelle.de", account.getEmail(), bcc, subject, body);
    }

}
