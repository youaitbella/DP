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
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.entities.certification.EmailReceiver;
import org.inek.dataportal.entities.certification.MapEmailReceiverLabel;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.entities.certification.SystemAccountMapping;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Genders;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.facades.certification.EmailReceiverFacade;
import org.inek.dataportal.facades.certification.EmailReceiverLabelFacade;
import org.inek.dataportal.facades.certification.SystemFacade;

/**
 *
 * @author muellermi
 */
public class CertMail implements Serializable {
    
    private static final Logger _logger = Logger.getLogger("CertMail");
    
    //<editor-fold defaultstate="collapsed" desc="Email creation/preview fields.">
    private boolean _previewEnabled = false;
    private List<String> _emailList = new ArrayList<>();
    private String _selectedEmailAddressPreview = "";
    private String _previewSubject = "";
    private String _previewBody = "";
    private UIComponent _previewButton;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Email receiver lists - fields.">
    private String _selectedTemplate = "";
    private String _systemReceiverList = "";
    private String _singleReceiver = "";
    private String _selectedReceiverNewList = "";
    private String _selectedListEditName = "";
    private String _receiverListsName = "";
    private List<EmailReceiver> _emailReceivers;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Injections">
    private SystemFacade _systemFacade;
    
    private AccountFacade _accFacade;
    
    private EmailReceiverFacade _emailReceiverFacade;
     
    private EmailReceiverLabelFacade _emailReceiverLabelFacade;
    
    private MailTemplateFacade _emailTemplateFacade;
    //</editor-fold>
    
    public CertMail(MailTemplateFacade mailTemplateFacade, SystemFacade systemFacade, AccountFacade accFacade, 
            EmailReceiverFacade erFacade, EmailReceiverLabelFacade erlFacade) {
        _emailReceivers = new ArrayList<>();
        _systemFacade = systemFacade;
        _accFacade = accFacade;
        _emailReceiverLabelFacade = erlFacade;
        _emailReceiverFacade = erFacade;
        _emailTemplateFacade = mailTemplateFacade;
    }
    
    //<editor-fold defaultstate="collapsed" desc="SelectItems">
    public SelectItem[] getEmailTemplates() {
        List<SelectItem> emailTemplates = new ArrayList<>();
        emailTemplates.add(new SelectItem(""));
        List<MailTemplate> mts = _emailTemplateFacade.findTemplatesByFeature(Feature.CERT);
        mts.stream().forEach((t) -> {
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
        }
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
        if(_selectedListEditName != null) {
            int receiverListId = _emailReceiverLabelFacade.findEmailReceiverListByLabel(_selectedListEditName);
            _emailReceivers = _emailReceiverFacade.findAllEmailReceiverByListId(receiverListId);
        }
    }
    
    public String addReceiverToList() {
        if(_selectedReceiverNewList.equals(""))
            return ""; // throw exception here.
        String userEmail = _selectedReceiverNewList.substring(_selectedReceiverNewList.indexOf('(')+1, _selectedReceiverNewList.length()-1);
        EmailReceiver er = new EmailReceiver();
        er.setAccountId(_accFacade.findByMailOrUser(userEmail).getAccountId());
        if(!_selectedReceiverNewList.equals("")) {
            if(_emailReceivers.size() > 0) {
                er.setReceiverList(_emailReceivers.get(0).getReceiverList());
            } else if(_emailReceivers.isEmpty()) {
                er.setReceiverList(_emailReceiverFacade.getHighestEmailReceiverListId()+1);
            }
        } else {
            er.setReceiverList(_emailReceiverFacade.getHighestEmailReceiverListId()+1);
        }
        if(_emailReceivers.contains(er))
            return "";
        _emailReceivers.add(er);
        return "";
    }
    
    public String saveReceiverList() {
        if(_emailReceivers.size() <= 0)
            return ""; // throw exception here, to print specific message to user.
        if(_selectedListEditName != null && !_selectedListEditName.isEmpty()) {
            // Receiverliste existiert schon in der DB
            int receiverListId = _emailReceiverLabelFacade.findEmailReceiverListByLabel(_selectedListEditName);
            List<EmailReceiver> existingEmailReceivers = _emailReceiverFacade.findAllEmailReceiverByListId(receiverListId);
            existingEmailReceivers.stream().filter((er) -> (_emailReceivers.contains(er))).forEach((er) -> {
                _emailReceivers.remove(er);
            });
            _emailReceivers.stream().forEach((er) -> {
                _emailReceiverFacade.persist(er);
            });
        } else {
            if(_receiverListsName.isEmpty())
                return ""; // throw exception
            if(_emailReceiverLabelFacade.findEmailReceiverListByLabel(_receiverListsName) > -1)
                return ""; // throw exception - listname already in DB
            MapEmailReceiverLabel label = new MapEmailReceiverLabel();
            label.setEmailReceiverLabelId(_emailReceiverFacade.getHighestEmailReceiverListId()+1);
            label.setLabel(_receiverListsName);
            _emailReceiverLabelFacade.persist(label);
            _emailReceivers.stream().forEach((er) -> {
                _emailReceiverFacade.persist(er);
            });
        }
        initEmailReceiversTemplateList();
        return ""; // successfully saved
    }
    
    public String deleteReceiverList() {
        if(_selectedListEditName.equals("")) {
           return ""; // throw exception here. 
        }
        MapEmailReceiverLabel erl = _emailReceiverLabelFacade.find(_emailReceiverLabelFacade.findEmailReceiverListByLabel(_selectedListEditName));
        int erId = erl.getEmailReceiverLabelId();
        if(!_emailReceiverFacade.deleteAllEmailReceiverByListId(erId))
            return ""; // throw exception here.
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
    
    public String deleteReceiverFromTemplate(int erId, int accId) {
        EmailReceiver er = _emailReceiverFacade.find(erId);
        if(er != null) {
            _emailReceiverFacade.remove(er);
            initEmailReceiversTemplateList();
        }
        else {
            if(accId != -1) {
                for(EmailReceiver element : _emailReceivers) {
                    if(element.getAccountId() == accId) {
                        _emailReceivers.remove(element);
                        break;
                    }
                }
            }
        }
        return "";
    }
    
    public boolean previewEnabled() {
        return _previewEnabled;
    }
    
    public String showPreview() {
        if(inputVerificationOfEmailReceivers()){
            _previewEnabled = false;
            return "";
        }
        _emailList.clear();
        if(_systemReceiverList != null){
            buildEmailListBySystemName();
        } else if(_selectedListEditName != null){
            buildEmailListByReceiverList();
        } else if(_singleReceiver != null) {
            _emailList.add(_singleReceiver.substring(_singleReceiver.indexOf('(')+1, _singleReceiver.lastIndexOf(')')));
        }
        if(!checkForAvailableEmailReceivers())
            return "";
        _selectedEmailAddressPreview = _emailList.get(0);
        _previewEnabled = true;
        buildPreviewEmail();
        return "";
    }

    private boolean checkForAvailableEmailReceivers() throws ValidatorException {
        if(_emailList.size() <= 0) {
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
        List<SystemAccountMapping> map = rs.getMappings();
        map.stream().map((am) -> _accFacade.find(am.getAccountId()).getEmail()).forEach((email) -> {
            _emailList.add(email);
        });
    }

    private boolean inputVerificationOfEmailReceivers() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (_selectedTemplate == null) {
            ctx.addMessage(_previewButton.getClientId(ctx), new FacesMessage("Bitte wählen Sie ein Template aus!"));
            return true;
        }
        if(_systemReceiverList == null && _selectedListEditName == null && _singleReceiver == null) {
            ctx.addMessage(_previewButton.getClientId(ctx), new FacesMessage("Bitte wählen Sie eine Empfängerliste aus!"));
            return true;
        }
        return false;
    }
    
    public void buildPreviewEmail() {
        String salutation = buildEmailSalutation();
        String version = "";
        if(_systemReceiverList != null) {
            version = _systemReceiverList;
        }
        MailTemplate mt = _emailTemplateFacade.findByName(_selectedTemplate);
        _previewSubject = mt.getSubject().replace("{version}",version);
        _previewBody = mt.getBody().replace("{version}", version).replace("{salutation}", salutation);
    }

    private String buildEmailSalutation() {
        String receiver = _selectedEmailAddressPreview;
        Account receiverAccount = _accFacade.findByMailOrUser(receiver);
        String title = receiverAccount.getTitle().equals("") ? "" : " " + receiverAccount.getTitle();
        boolean isFemale = true;
        if(receiverAccount.getGender() == Genders.Male.id()) isFemale = false;
        String salutation = "Sehr " + (isFemale ? "geehrte Frau" : "geehrter Herr") + title + " " + receiverAccount.getLastName() + ",";
        return salutation;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter/Setter">
    public String getSelectedTemplate() {
        return _selectedTemplate;
    }

    public void setSelectedTemplate(String _selectedTemplate) {
        this._selectedTemplate = _selectedTemplate;
    }

    public String getSystemReceiverList() {
        return _systemReceiverList;
    }

    public void setSystemReceiverList(String _systemReceiverList) {
        this._systemReceiverList = _systemReceiverList;
    }

    public String getSingleReceiver() {
        return _singleReceiver;
    }

    public void setSingleReceiver(String _singleReceiver) {
        this._singleReceiver = _singleReceiver;
    }

    public String getSelectedReceiverNewList() {
        return _selectedReceiverNewList;
    }

    public void setSelectedReceiverNewList(String _selectedReceiverNewList) {
        this._selectedReceiverNewList = _selectedReceiverNewList;
    }

    public String getSelectedListEdit() {
        return _selectedListEditName;
    }

    public void setSelectedListEdit(String _selectedListEdit) {
        this._selectedListEditName = _selectedListEdit;
    }

    public String getReceiverListsName() {
        return _receiverListsName;
    }

    public void setReceiverListsName(String _receiverListsName) {
        this._receiverListsName = _receiverListsName;
    }
    
    public List<EmailReceiver> getEmailReceivers() {
        return _emailReceivers;
    }

    public void setEmailReceivers(List<EmailReceiver> _emailReceivers) {
        this._emailReceivers = _emailReceivers;
    }
    
    public String getSelectedEmailAddressPreview() {
        return _selectedEmailAddressPreview;
    }

    public void setSelectedEmailAddressPreview(String _selectedEmailAddressPreview) {
        this._selectedEmailAddressPreview = _selectedEmailAddressPreview;
    }
    
    public String getPreviewSubject() {
        return _previewSubject;
    }

    public void setPreviewSubject(String _previewSubject) {
        this._previewSubject = _previewSubject;
    }

    public String getPreviewBody() {
        return _previewBody;
    }

    public void setPreviewBody(String _previewBody) {
        this._previewBody = _previewBody;
    }
    
    public UIComponent getPreviewButton() {
        return _previewButton;
    }

    public void setPreviewButton(UIComponent _previewButton) {
        this._previewButton = _previewButton;
    }
    //</editor-fold>
}
