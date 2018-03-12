package org.inek.dataportal.feature.certification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.entities.certification.Grouper;
import org.inek.dataportal.entities.certification.MapEmailReceiverLabel;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.CertMailType;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.enums.Genders;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.certification.EmailLogFacade;
import org.inek.dataportal.facades.certification.EmailReceiverLabelFacade;
import org.inek.dataportal.facades.certification.GrouperFacade;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.mail.MailTemplateFacade;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.mail.Mailer;

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
    private int _selectedEmailAddressPreview = 0;
    private String _previewSubject = "";
    private String _previewBody = "";
    private String _attachement = "";
    private UIComponent _previewButton;
    private final List<EmailSentInfo> _emailSentInfoDataTable = new ArrayList<>();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Email receiver lists - fields.">
    private String _selectedTemplate = "";
    private int _systemReceiverId = 0;
    private int _singleReceiver = 0;
    private String _selectedReceiverNewList = "";
    private String _selectedListEditName = "";
    private String _receiverListsName = "";
    private int _systemForEmail = -1;
    //private List<EmailReceiver> _emailReceivers;
    private List<GrouperEmailReceiver> _receiverEmails = new ArrayList<>();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Injections">
    @Inject
    private SystemFacade _systemFacade;

    @Inject
    private AccountFacade _accFacade;

    @Inject
    private EmailReceiverLabelFacade _emailReceiverLabelFacade;

    @Inject
    private MailTemplateFacade _emailTemplateFacade;

    @Inject
    private EmailLogFacade _emailLogFacade;

    @Inject
    private SessionController _sessionController;

    @Inject
    private GrouperFacade _grouperFacade;
    //</editor-fold>

    public void changedSystemReceiver() {
        buildEmailReceiverListSystem();
        _previewEnabled = false;
    }

    //<editor-fold defaultstate="collapsed" desc="SelectItems">
    public SelectItem[] getEmailTemplates() {
        List<SelectItem> emailTemplates = new ArrayList<>();
        emailTemplates.add(new SelectItem(""));
        List<MailTemplate> mts = _emailTemplateFacade.findTemplatesByFeature(Feature.CERT);
        mts.stream().forEach((t) -> {
            if (t.getType() == CertMailType.Information.getId() || t.getType() == CertMailType.Opening.getId()) {
                emailTemplates.add(new SelectItem(t.getName()));
            }
        });
        return emailTemplates.toArray(new SelectItem[emailTemplates.size()]);
    }

    public SelectItem[] getSystemReceiverLists() {
        List<SelectItem> receiverList = new ArrayList<>();
        receiverList.add(new SelectItem(""));
        List<RemunerationSystem> systems = _systemFacade.findAllFresh();
        systems.stream().forEach((s) -> {
            receiverList.add(new SelectItem(s.getId(), s.getDisplayName()));
        });
        return receiverList.toArray(new SelectItem[receiverList.size()]);
    }

    public SelectItem[] getSingleReceivers() {
        List<SelectItem> singleReceivers = new ArrayList<>();
        singleReceivers.add(new SelectItem(""));
        List<Account> accountsWithCert = _accFacade.getAccounts4Feature(Feature.CERT);
        accountsWithCert.stream().forEach((acc) -> {
            singleReceivers.add(new SelectItem(acc.getId(), acc.getCompany() + " (" + acc.getEmail() + ")"));
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

    public List<SelectItem> getEmailAddressForPreview() {
        List<SelectItem> items = _receiverEmails
                .stream()
                .filter(a -> a.isSend())
                .map(a -> new SelectItem(a.getGrouper().getAccountId(), a.getGrouper().getAccount().getEmail()))
                .collect(Collectors.toList());
        return items;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="AjaxBehavior events">
    public void editReceiverListChanged(AjaxBehaviorEvent event) {
        setReceiverListsName(_selectedListEditName);
    }

    public void changedPreviewReceiver(AjaxBehaviorEvent event) {
        buildPreviewEmail();
    }
    //</editor-fold>

    public String getCompanyNameByAccId(int id) {
        return _accFacade.findAccount(id).getCompany();
    }

    public int getSystemReceiverId() {
        return _systemReceiverId;
    }

    public void setSystemReceiverId(int systemReceiverId) {
        this._systemReceiverId = systemReceiverId;
    }

    public List<GrouperEmailReceiver> getReceiverEmails() {
        return _receiverEmails;
    }

    public boolean renderEmailReceiverTable() {
        return getReceiverEmails().size() > 0;
    }

    public boolean renderEmailSentSuccessTable() {
        return _emailSentInfoDataTable.size() > 0;
    }

    public boolean renderAttachementText() {
        return !_attachement.isEmpty();
    }

    public boolean previewEnabled() {
        return _previewEnabled;
    }

    public String showPreview() {
        if (_systemReceiverId != 0) {
            //buildEmailReceiverListSystem();
            //} else if (_singleReceiver != null) {
            // TODO: _emailReceiverList
        }
        if (!checkForAvailableEmailReceivers()) {
            return "";
        }
        _previewEnabled = true;
        _emailSentInfoDataTable.clear();
        _selectedEmailAddressPreview = _receiverEmails.stream()
                .filter(e -> e.isSend())
                .findFirst()
                .get()
                .getGrouper()
                .getAccount()
                .getId();
        buildPreviewEmail();
        return "";
    }

    private boolean checkForAvailableEmailReceivers() throws ValidatorException {
        if (_receiverEmails.size() <= 0) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(_previewButton.getClientId(ctx), new FacesMessage("Es gibt keine Emailempfänger für die ausgewählte Liste!"));
            _previewEnabled = false;
            return false;
        }
        return true;
    }

    public void buildPreviewEmail() {
        Account acc = _accFacade.findFreshAccount(_selectedEmailAddressPreview);
        String salutation = buildEmailSalutation(acc);
        String company = acc.getCompany();
        MailTemplate mt = _emailTemplateFacade.findByName(_selectedTemplate);
        _previewSubject = mt.getSubject().replace("{company}", company);
        _previewBody = mt.getBody().replace("{salutation}", salutation)
                .replace("{company}", company)
                .replace("{sender}", _sessionController.getAccount().getFirstName() + " " + _sessionController.getAccount().getLastName());
    }

    private String buildEmailSalutation(Account account) {
        String title = "".equals(account.getTitle()) ? "" : " " + account.getTitle();
        boolean isFemale = true;
        if (account.getGender() == Genders.Male.id()) {
            isFemale = false;
        }
        String salutation = "Sehr " + (isFemale ? "geehrte Frau" : "geehrter Herr") + title + " " + account.getLastName() + ",";
        return salutation;
    }

    public String getAccountName(int accId) {
        return _accFacade.findAccount(accId).getFirstName() + " " + _accFacade.findAccount(accId).getLastName();
    }

    //<editor-fold defaultstate="collapsed" desc="Getter/Setter">
    public String getSelectedTemplate() {
        return _selectedTemplate;
    }

    public void setSelectedTemplate(String selectedTemplate) {
        this._selectedTemplate = selectedTemplate;
    }

    public int getSingleReceiver() {
        return _singleReceiver;
    }

    public void setSingleReceiver(int singleReceiver) {
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

    public int getSelectedEmailAddressPreview() {
        return _selectedEmailAddressPreview;
    }

    public void setSelectedEmailAddressPreview(int selectedEmailAddressPreview) {
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
    private void buildEmailReceiverListSystem() {
        _receiverEmails.clear();
        if (_systemReceiverId == 0) {
            return;
        }
        RemunerationSystem system = _systemFacade.findFresh(_systemReceiverId);
        system.getGrouperList().stream()
                .forEach(g -> _receiverEmails
                .add(new GrouperEmailReceiver(g, Mailer.buildCC(_grouperFacade.findGrouperEmailReceivers(g.getAccount())))));
    }

    public String sendMails() {
        _emailSentInfoDataTable.clear();
        MailTemplate mt = _emailTemplateFacade.findByName(_selectedTemplate);

        _receiverEmails.stream()
                .filter(a -> a.isSend())
                .forEach(a -> {
                    String salutation = buildEmailSalutation(a._grouper.getAccount());
                    String company = a.getGrouper().getAccount().getCompany();
                    String subject = mt.getSubject().replace("{company}", company);
                    String body = mt.getBody().replace("{salutation}", salutation)
                            .replace("{company}", company)
                            .replace("{sender}", _sessionController.getAccount().getFirstName() + " "
                                    + _sessionController.getAccount().getLastName());
                    try {
                        if (!_sessionController.getMailer().sendMailFrom(mt.getFrom(), a.getGrouper().getAccount().getEmail(),
                                a.getCcEmails(), mt.getBcc(), subject, body, _attachement)) {
                            throw new Exception("Fehler bei Mailversand!");
                        }
                        _emailSentInfoDataTable.add(new EmailSentInfo(a.getGrouper().getAccount().getEmail(), mt.getBcc(), "Erfolgreich"));
                    } catch (Exception ex) {
                        _emailSentInfoDataTable
                                .add(new EmailSentInfo(a.getGrouper().getAccount().getEmail(), mt.getBcc(), "Fehler!\n" + ex.getMessage()));
                    }
                });
        return "";
    }

    public class GrouperEmailReceiver {

        private Grouper _grouper;
        private String _ccEmails;
        private boolean _send = true;

        public GrouperEmailReceiver(Grouper grouper, String ccs) {
            _grouper = grouper;
            _ccEmails = ccs;
        }

        public Grouper getGrouper() {
            return _grouper;
        }

        public void setGrouper(Grouper grouper) {
            this._grouper = grouper;
        }

        public String getCcEmails() {
            return _ccEmails;
        }

        public void setCcEmails(String ccEmails) {
            this._ccEmails = ccEmails;
        }

        public boolean isSend() {
            return _send;
        }

        public void setSend(boolean send) {
            this._send = send;
        }
    }
}
