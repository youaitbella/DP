package org.inek.dataportal.feature.certification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.entities.certification.EmailLog;
import org.inek.dataportal.entities.certification.EmailReceiver;
import org.inek.dataportal.entities.certification.Grouper;
import org.inek.dataportal.entities.certification.MapEmailReceiverLabel;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.CertMailType;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Genders;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.facades.certification.EmailLogFacade;
import org.inek.dataportal.facades.certification.EmailReceiverFacade;
import org.inek.dataportal.facades.certification.EmailReceiverLabelFacade;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class CertMail implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("CertMail");

    //<editor-fold defaultstate="collapsed" desc="Email creation/preview fields.">
    private boolean _previewEnabled = false;
    private final List<String> _emailList = new ArrayList<>();
    private String _selectedEmailAddressPreview = "";
    private String _previewSubject = "";
    private String _previewBody = "";
    private String _attachement = "";
    private UIComponent _previewButton;
    private final List<EmailSentInfo> _emailSentInfoDataTable = new ArrayList<>();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Email receiver lists - fields.">
    private String _selectedTemplate = "";
    private String _systemReceiverList = "";
    private String _singleReceiver = "";
    private String _selectedReceiverNewList = "";
    private String _selectedListEditName = "";
    private String _receiverListsName = "";
    private int _systemForEmail = -1;
    private List<EmailReceiver> _emailReceivers;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Injections">
    @Inject
    private SystemFacade _systemFacade;

    @Inject
    private AccountFacade _accFacade;

    @Inject
    private EmailReceiverFacade _emailReceiverFacade;

    @Inject
    private EmailReceiverLabelFacade _emailReceiverLabelFacade;

    @Inject
    private MailTemplateFacade _emailTemplateFacade;

    @Inject
    private Mailer _mailer;

    @Inject
    private EmailLogFacade _emailLogFacade;

    @Inject
    private SessionController _sessionController;
    //</editor-fold>

    public CertMail() {
        _emailReceivers = new ArrayList<>();
    }

    //<editor-fold defaultstate="collapsed" desc="SelectItems">
    public SelectItem[] getEmailTemplates() {
        List<SelectItem> emailTemplates = new ArrayList<>();
        emailTemplates.add(new SelectItem(""));
        List<MailTemplate> mts = _emailTemplateFacade.findTemplatesByFeature(Feature.CERT);
        mts.stream().forEach((t) -> {
            if(t.getType() == CertMailType.Information.getId() || t.getType() == CertMailType.Opening.getId())
                emailTemplates.add(new SelectItem(t.getName()));
        });
        return emailTemplates.toArray(new SelectItem[emailTemplates.size()]);
    }

    public SelectItem[] getSystemReceiverLists() {
        List<SelectItem> receiverList = new ArrayList<>();
        receiverList.add(new SelectItem(""));
        List<RemunerationSystem> systems = _systemFacade.findAll();
        systems.stream().forEach((s) -> {
            receiverList.add(new SelectItem(s.getDisplayName()));
        });
        return receiverList.toArray(new SelectItem[receiverList.size()]);
    }

    public SelectItem[] getSingleReceivers() {
        List<SelectItem> singleReceivers = new ArrayList<>();
        singleReceivers.add(new SelectItem(""));
        List<Account> accountsWithCert = _accFacade.getAccounts4Feature(Feature.CERT);
        accountsWithCert.stream().forEach((acc) -> {
            singleReceivers.add(new SelectItem(acc.getCompany() + " (" + acc.getEmail() + ")"));
        });
        return singleReceivers.toArray(new SelectItem[singleReceivers.size()]);
    }

    public SelectItem[] getEmailReceiverLists() {
        List<SelectItem> emailReceivers = new ArrayList<>();
        emailReceivers.add(new SelectItem(""));
        List<MapEmailReceiverLabel> labels = _emailReceiverLabelFacade.findAll();
        labels.stream().forEach((rl) -> {
            emailReceivers.add(new SelectItem(rl.getLabel()));
        });
        return emailReceivers.toArray(new SelectItem[emailReceivers.size()]);
    }

    public SelectItem[] getEmailAddressForPreview() {
        List<SelectItem> list = new ArrayList<>();
        _emailList.stream().forEach((s) -> {
            list.add(new SelectItem(s));
        });
        return list.toArray(new SelectItem[list.size()]);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="AjaxBehavior events">
    public void receiverChanged(AjaxBehaviorEvent event) {
        switch (event.getComponent().getId()) {
            case "selectedSystemReceiverList":
                _selectedListEditName = "";
                _singleReceiver = "";
                _receiverListsName = "";
                _emailReceivers.clear();
                break;
            case "availableLists":
                _systemReceiverList = "";
                _singleReceiver = "";
                break;
            case "selectedReceiver":
                _systemReceiverList = "";
                _selectedListEditName = "";
                _receiverListsName = "";
                _emailReceivers.clear();
                break;
            default:
                throw new IllegalArgumentException("Unknown component id " + event.getComponent().getId());
        }
        _previewEnabled = false;
        _emailSentInfoDataTable.clear();
    }

    public void editReceiverListChanged(AjaxBehaviorEvent event) {
        setReceiverListsName(_selectedListEditName);
        receiverChanged(event);
        initEmailReceiversTemplateList();
    }

    public void changedPreviewReceiver(AjaxBehaviorEvent event) {
        buildPreviewEmail();
    }
    //</editor-fold>

    private void initEmailReceiversTemplateList() {
        _emailReceivers.clear();
        if (_selectedListEditName != null) {
            int receiverListId = _emailReceiverLabelFacade.findEmailReceiverListByLabel(_selectedListEditName);
            _emailReceivers = _emailReceiverFacade.findAllEmailReceiverByListId(receiverListId);
        }
    }

    public String addReceiverToList() {
        if(_selectedReceiverNewList == null || _selectedReceiverNewList.isEmpty())
            return "";
        String userEmail = _selectedReceiverNewList.substring(_selectedReceiverNewList.indexOf('(') + 1, _selectedReceiverNewList.length() - 1);
        EmailReceiver er = new EmailReceiver();
        er.setAccountId(_accFacade.findByMailOrUser(userEmail).getId());
        if (!"".equals(_selectedReceiverNewList)) {
            if (_emailReceivers.size() > 0) {
                er.setReceiverList(_emailReceivers.get(0).getReceiverList());
            } else if (_emailReceivers.isEmpty()) {
                er.setReceiverList(_emailReceiverFacade.getHighestEmailReceiverListId() + 1);
            }
        } else {
            er.setReceiverList(_emailReceiverFacade.getHighestEmailReceiverListId() + 1);
        }
        if (_emailReceivers.contains(er)) {
            return "";
        }
        _emailReceivers.add(er);
        return "";
    }

    public String saveReceiverList() {
        if (_emailReceivers.size() <= 0) {
            return ""; // throw exception here, to print specific message to user.
        }
        if (_selectedListEditName != null && !_selectedListEditName.isEmpty()) {
            // Receiverliste existiert schon in der DB
            int receiverListId = _emailReceiverLabelFacade.findEmailReceiverListByLabel(_selectedListEditName);
            List<EmailReceiver> existingEmailReceivers = _emailReceiverFacade.findAllEmailReceiverByListId(receiverListId);
            existingEmailReceivers.stream().filter((er) -> (_emailReceivers.contains(er))).forEach((er) -> {
                _emailReceivers.remove(er);
            });
            _emailReceivers.stream().forEach((er) -> {
                _emailReceiverFacade.save(er);
            });
        } else {
            if (_receiverListsName.isEmpty()) {
                return ""; // throw exception
            }
            if (_emailReceiverLabelFacade.findEmailReceiverListByLabel(_receiverListsName) > -1) {
                return ""; // throw exception - listname already in DB
            }
            MapEmailReceiverLabel label = new MapEmailReceiverLabel();
            label.setEmailReceiverLabelId(_emailReceiverFacade.getHighestEmailReceiverListId() + 1);
            label.setLabel(_receiverListsName);
            _emailReceiverLabelFacade.save(label);
            _emailReceivers.stream().forEach((er) -> {
                _emailReceiverFacade.save(er);
            });
        }
        initEmailReceiversTemplateList();
        return ""; // successfully saved
    }

    public String deleteReceiverList() {
        if ("".equals(_selectedListEditName)) {
            return ""; // throw exception here.
        }
        MapEmailReceiverLabel erl = _emailReceiverLabelFacade.find(_emailReceiverLabelFacade.findEmailReceiverListByLabel(_selectedListEditName));
        int erId = erl.getEmailReceiverLabelId();
        if (!_emailReceiverFacade.deleteAllEmailReceiverByListId(erId)) {
            return ""; // throw exception here.
        }
        _emailReceiverLabelFacade.remove(erl);
        _emailReceivers.clear();
        return "";
    }

    public String getCompanyNameByAccId(int id) {
        return _accFacade.find(id).getCompany();
    }

    public boolean renderEmailReceiverTable() {
        return _emailReceivers.size() > 0;
    }

    public boolean renderEmailSentSuccessTable() {
        return _emailSentInfoDataTable.size() > 0;
    }
    
    public boolean renderAttachementText() {
        return !_attachement.isEmpty();
    }

    public String deleteReceiverFromTemplate(int erId, int accId) {
        EmailReceiver er = _emailReceiverFacade.find(erId);
        if (er != null) {
            _emailReceiverFacade.remove(er);
            initEmailReceiversTemplateList();
        } else if (accId != -1) {
            for (EmailReceiver element : _emailReceivers) {
                if (element.getAccountId() == accId) {
                    _emailReceivers.remove(element);
                    break;
                }
            }
        }
        return "";
    }

    public boolean previewEnabled() {
        return _previewEnabled;
    }

    public String showPreview() {
        if (inputVerificationOfEmailReceivers()) {
            _previewEnabled = false;
            return "";
        }
        _emailList.clear();
        if (_systemReceiverList != null) {
            buildEmailListBySystemName();
        } else if (_selectedListEditName != null) {
            buildEmailListByReceiverList();
        } else if (_singleReceiver != null) {
            _emailList.add(_singleReceiver.substring(_singleReceiver.indexOf('(') + 1, _singleReceiver.lastIndexOf(')')));
        }
        if (!checkForAvailableEmailReceivers()) {
            return "";
        }
        _selectedEmailAddressPreview = _emailList.get(0);
        _previewEnabled = true;
        _emailSentInfoDataTable.clear();
        buildPreviewEmail();
        return "";
    }

    private boolean checkForAvailableEmailReceivers() throws ValidatorException {
        if (_emailList.size() <= 0) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(_previewButton.getClientId(ctx), new FacesMessage("Es gibt keine Emailempfänger für die ausgewählte Liste!"));
            _previewEnabled = false;
            return false;
        }
        return true;
    }

    private void buildEmailListByReceiverList() {
        List<EmailReceiver> list = _emailReceiverFacade.findAllEmailReceiverByListId(
                _emailReceiverLabelFacade.findEmailReceiverListByLabel(_selectedListEditName));
        list.stream().map((er) -> _accFacade.find(er.getAccountId()).getEmail()).forEach((email) -> {
            _emailList.add(email);
        });
    }

    private void buildEmailListBySystemName() {
        RemunerationSystem rs = _systemFacade.findRemunerationSystemByName(_systemReceiverList);
        List<Grouper> grouperList = rs.getGrouperList();
        grouperList.stream().map((gr) -> _accFacade.find(gr.getAccountId()).getEmail() + ";" + gr.getEmailCopy()).forEach((email) -> {
            _emailList.add(email);
        });
    }

    private boolean inputVerificationOfEmailReceivers() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (_selectedTemplate == null) {
            ctx.addMessage(_previewButton.getClientId(ctx), new FacesMessage("Bitte wählen Sie ein Template aus!"));
            return true;
        }
        if (_systemReceiverList == null && _selectedListEditName == null && _singleReceiver == null) {
            ctx.addMessage(_previewButton.getClientId(ctx), new FacesMessage("Bitte wählen Sie eine Empfängerliste aus!"));
            return true;
        }
        return false;
    }

    public void buildPreviewEmail() {
        String receipient = getReceipient(_selectedEmailAddressPreview);
        String salutation = buildEmailSalutation(receipient);
        String version = "";
        String company = _accFacade.findByMailOrUser(receipient).getCompany();
        if (_systemReceiverList != null) {
            version = _systemReceiverList;
        }
        MailTemplate mt = _emailTemplateFacade.findByName(_selectedTemplate);
        _previewSubject = mt.getSubject().replace("{version}", version).replace("{company}", company);
        _previewBody = mt.getBody().replace("{version}", version).replace("{salutation}", salutation)
                .replace("{company}", company)
                .replace("{sender}", _sessionController.getAccount().getFirstName() + " " + _sessionController.getAccount().getLastName());
    }

    private String getReceipient(String adressInfo) {
        String[] receipients = adressInfo.split(";");
        String receipient = receipients[0];
        return receipient;
    }

    private String getCC(String adressInfo) {
        int pos = adressInfo.indexOf(";");
        if (pos < 0) {
            return "";
        }
        return adressInfo.substring(pos + 1);
    }

    private String buildEmailSalutation(String receiverEmail) {
        String receiver = receiverEmail;
        Account receiverAccount = _accFacade.findByMailOrUser(receiver);
        String title = "".equals(receiverAccount.getTitle()) ? "" : " " + receiverAccount.getTitle();
        boolean isFemale = true;
        if (receiverAccount.getGender() == Genders.Male.id()) {
            isFemale = false;
        }
        String salutation = "Sehr " + (isFemale ? "geehrte Frau" : "geehrter Herr") + title + " " + receiverAccount.getLastName() + ",";
        return salutation;
    }
    
    public String getAccountName(int accId) {
        return _accFacade.find(accId).getFirstName() + " " + _accFacade.find(accId).getLastName();
    }

    public String sendMailsToAllReceivers() {
        _emailSentInfoDataTable.clear();
        MailTemplate mt = _emailTemplateFacade.findByName(_selectedTemplate);
        String version = _systemReceiverList == null ? "" : _systemReceiverList;
        for (String emailAddressInfo : _emailList) {
            String emailAddress = getReceipient(emailAddressInfo);
            String salutation = buildEmailSalutation(emailAddress);
            String company = _accFacade.findByMailOrUser(emailAddress).getCompany();
            String subject = mt.getSubject().replace("{version}", version).replace("{company}", company);
            String body = mt.getBody().replace("{version}", version).replace("{salutation}", salutation)
                    .replace("{company}", company)
                    .replace("{sender}", _sessionController.getAccount().getFirstName() + " " + _sessionController.getAccount().getLastName());
            try {
                if(!_mailer.sendMailFrom(mt.getFrom(), emailAddress, getCC(emailAddressInfo), mt.getBcc(), subject, body, _attachement))
                    throw new Exception("Fehler bei Mailversand!");
                createEmailLogEntry(version, mt, emailAddress);
                _emailSentInfoDataTable.add(new EmailSentInfo(emailAddressInfo, mt.getBcc(), "Erfolgreich"));
            } catch (Exception ex) {
                _emailSentInfoDataTable.add(new EmailSentInfo(emailAddressInfo, mt.getBcc(), "Fehler!\n" + ex.getMessage()));
            }
        }
        return "";
    }

    private void createEmailLogEntry(String version, MailTemplate mt, String emailAddress) {
        EmailLog log = new EmailLog();
        if (!"".equals(version)) {
            log.setSystemId(_systemFacade.findRemunerationSystemByName(version).getId());
        }
        if(version.isEmpty()) {
            if(_systemForEmail != -1) {
                log.setSystemId(_systemForEmail);
            }
        }
        log.setTemplateId(mt.getId());
        log.setType(mt.getType());
        log.setReceiverAccountId(_accFacade.findByMailOrUser(emailAddress).getId());
        log.setSenderAccountId(_sessionController.getAccountId());
        _emailLogFacade.save(log);
    }

    //<editor-fold defaultstate="collapsed" desc="Getter/Setter">
    public String getSelectedTemplate() {
        return _selectedTemplate;
    }

    public void setSelectedTemplate(String selectedTemplate) {
        this._selectedTemplate = selectedTemplate;
    }

    public String getSystemReceiverList() {
        return _systemReceiverList;
    }

    public void setSystemReceiverList(String systemReceiverList) {
        this._systemReceiverList = systemReceiverList;
    }

    public String getSingleReceiver() {
        return _singleReceiver;
    }

    public void setSingleReceiver(String singleReceiver) {
        this._singleReceiver = singleReceiver;
    }

    public String getSelectedReceiverNewList() {
        return _selectedReceiverNewList;
    }

    public void setSelectedReceiverNewList(String selectedReceiverNewList) {
        this._selectedReceiverNewList = selectedReceiverNewList;
    }

    public String getSelectedListEdit() {
        return _selectedListEditName;
    }

    public void setSelectedListEdit(String selectedListEdit) {
        this._selectedListEditName = selectedListEdit;
    }

    public String getReceiverListsName() {
        return _receiverListsName;
    }

    public void setReceiverListsName(String receiverListsName) {
        this._receiverListsName = receiverListsName;
    }

    public List<EmailReceiver> getEmailReceivers() {
        return _emailReceivers;
    }

    public void setEmailReceivers(List<EmailReceiver> emailReceivers) {
        this._emailReceivers = emailReceivers;
    }

    public String getSelectedEmailAddressPreview() {
        return _selectedEmailAddressPreview;
    }

    public void setSelectedEmailAddressPreview(String selectedEmailAddressPreview) {
        this._selectedEmailAddressPreview = selectedEmailAddressPreview;
    }

    public String getPreviewSubject() {
        return _previewSubject;
    }

    public void setPreviewSubject(String previewSubject) {
        this._previewSubject = previewSubject;
    }

    public String getPreviewBody() {
        return _previewBody;
    }

    public void setPreviewBody(String previewBody) {
        this._previewBody = previewBody;
    }

    public UIComponent getPreviewButton() {
        return _previewButton;
    }

    public void setPreviewButton(UIComponent previewButton) {
        this._previewButton = previewButton;
    }

    public List<EmailSentInfo> getEmailSentSuccess() {
        return _emailSentInfoDataTable;
    }
    
    public String getAttachement() {
        return _attachement;
    }

    public void setAttachement(String attachement) {
        this._attachement = attachement;
    }
    public int getSystemForEmail() {
        return _systemForEmail;
    }
     
    public void setSystemForEmail(int selectedSystem) {
        _systemForEmail = selectedSystem;
    }
    
    //</editor-fold>

}
