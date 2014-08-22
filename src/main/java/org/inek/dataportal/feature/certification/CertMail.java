package org.inek.dataportal.feature.certification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
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
@RequestScoped
public class CertMail {

    private static final Logger _logger = Logger.getLogger("CertMail");
    private int _selectedTemplate = 0;
    private int _receiverList = 0;
    private int _systemReceiverList = 0;
    private int _singleReceiver = 0;
    private int _selectedReceiverNewList = 0;

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
        labels.stream().forEach((l) -> {
            emailReceivers.add(new SelectItem(l.getLabel()));
        });
        return emailReceivers.toArray(new SelectItem[emailReceivers.size()]);
    }
    
    public void receiverChanged(AjaxBehaviorEvent event) {
        switch (event.getComponent().getId()) {
            case "selectedSystemReceiverList":
                _receiverList = 0;
                _singleReceiver = 0;
                break;
            case "selectedReceiverList":
                _systemReceiverList = 0;
                _singleReceiver = 0;
                break;
            case "selectedReceiver":
                _systemReceiverList = 0;
                _receiverList = 0;
                break;
        }
    }

    public int getSelectedTemplate() {
        return _selectedTemplate;
    }

    public void setSelectedTemplate(int _selectedTemplate) {
        this._selectedTemplate = _selectedTemplate;
    }

    public int getReceiverList() {
        return _receiverList;
    }

    public void setReceiverList(int _receiverList) {
        this._receiverList = _receiverList;
    }

    public int getSystemReceiverList() {
        return _systemReceiverList;
    }

    public void setSystemReceiverList(int _systemReceiverList) {
        this._systemReceiverList = _systemReceiverList;
    }

    public int getSingleReceiver() {
        return _singleReceiver;
    }

    public void setSingleReceiver(int _singleReceiver) {
        this._singleReceiver = _singleReceiver;
    }

    public int getSelectedReceiverNewList() {
        return _selectedReceiverNewList;
    }

    public void setSelectedReceiverNewList(int _selectedReceiverNewList) {
        this._selectedReceiverNewList = _selectedReceiverNewList;
    }
}
