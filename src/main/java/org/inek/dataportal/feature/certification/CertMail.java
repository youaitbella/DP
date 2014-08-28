package org.inek.dataportal.feature.certification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.entities.certification.EmailReceiver;
import org.inek.dataportal.entities.certification.MapEmailReceiverLabel;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.facades.certification.EmailReceiverFacade;
import org.inek.dataportal.facades.certification.EmailReceiverLabelFacade;
import org.inek.dataportal.facades.certification.SystemFacade;

/**
 *
 * @author muellermi
 */
@Named
@SessionScoped
public class CertMail implements Serializable {
    
    private static final Logger _logger = Logger.getLogger("CertMail");
    private String _selectedTemplate = "";
    private String _receiverList = "";
    private String _systemReceiverList = "";
    private String _singleReceiver = "";
    private String _selectedReceiverNewList = "";
    private String _selectedListEditName = "";
    private String _receiverListsName = "";
    private List<EmailReceiver> _emailReceivers;

    @Inject
    private MailTemplateFacade _mailTemplateFacade;
    
    @Inject
    private SystemFacade _systemFacade;
    
    @Inject
    private AccountFacade _accFacade;
    
    @Inject
    private EmailReceiverFacade _emailReceiverFacade;
    
    @Inject 
    private EmailReceiverLabelFacade _emailReceiverLabelFacade;
    
    public CertMail() {
        _emailReceivers = new ArrayList<>();
    }

    private void initEmailReceiversTemplateList() {
        _emailReceivers.clear();
        if(_selectedListEditName != null) {
            int receiverListId = _emailReceiverLabelFacade.findEmailReceiverListByLabel(_selectedListEditName);
            _emailReceivers = _emailReceiverFacade.findAllEmailReceiverByListId(receiverListId);
        }
    }
    
    public SelectItem[] getEmailTemplates() {
        List<SelectItem> emailTemplates = new ArrayList<>();
        emailTemplates.add(new SelectItem(""));
        List<MailTemplate> mts = _mailTemplateFacade.findTemplatesByFeature(Feature.CERT);
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
    
    public void receiverChanged(AjaxBehaviorEvent event) {
        switch (event.getComponent().getId()) {
            case "selectedSystemReceiverList":
                _receiverList = "";
                _singleReceiver = "";
                break;
            case "selectedReceiverList":
                _systemReceiverList = "";
                _singleReceiver = "";
                break;
            case "selectedReceiver":
                _systemReceiverList = "";
                _receiverList = "";
                break;
        }
    }
    
    public String addReceiverToList() {
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
            int receiverListId = _emailReceiverLabelFacade.findEmailReceiverListByLabel(_receiverListsName);
            for(EmailReceiver er : _emailReceivers) {
                _emailReceiverFacade.persist(er);
            }
        }
        return ""; // successfully saved
    }
    
    public String getCompanyNameByAccId(int id) {
        return _accFacade.find(id).getCompany();
    }
    
    public void editReceiverListChanged(AjaxBehaviorEvent event) {
        setReceiverListsName(_selectedListEditName);
        initEmailReceiversTemplateList();
    }
    
    public boolean renderEmailReceiverTable() {
        return _emailReceivers.size() > 0;
    }

    public String getSelectedTemplate() {
        return _selectedTemplate;
    }

    public void setSelectedTemplate(String _selectedTemplate) {
        this._selectedTemplate = _selectedTemplate;
    }

    public String getReceiverList() {
        return _receiverList;
    }

    public void setReceiverList(String _receiverList) {
        this._receiverList = _receiverList;
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
}
