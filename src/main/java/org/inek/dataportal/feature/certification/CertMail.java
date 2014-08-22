package org.inek.dataportal.feature.certification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
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
    private int _singleReceiver = 0;

    @Inject
    private MailTemplateFacade _mailTemplateFacade;
    
    @Inject
    private SystemFacade _systemFacade;
    
    @Inject
    private AccountFacade _accFacade;
    
    public SelectItem[] getEmailTemplates() {
        List<SelectItem> emailTemplates = new ArrayList<>();
        emailTemplates.add(new SelectItem());
        List<MailTemplate> mts = _mailTemplateFacade.findTemplatesByFeature(Feature.CERT);
        for(MailTemplate t : mts) {
           emailTemplates.add(new SelectItem("" + t.getId() + " - " + t.getName()));
        }
        return emailTemplates.toArray(new SelectItem[emailTemplates.size()]);
    }
    
    public SelectItem[] getReceiverLists() {
        List<SelectItem> receiverList = new ArrayList<>();
        receiverList.add(new SelectItem());
        List<RemunerationSystem> systems = _systemFacade.findAll();
        for(RemunerationSystem s : systems) {
            receiverList.add(new SelectItem("System: " + s.getDisplayName()));
        }
        return receiverList.toArray(new SelectItem[receiverList.size()]);
    }
    
    public SelectItem[] getSingleReceivers() {
        List<SelectItem> singleReceivers = new ArrayList<>();
        singleReceivers.add(new SelectItem());
        List<Account> accountsWithCert = _accFacade.getAccounts4Feature(Feature.CERT);
        for(Account acc : accountsWithCert) {
            singleReceivers.add(new SelectItem(acc.getCompany() + " (" + acc.getEmail() + ")"));
        }
        return singleReceivers.toArray(new SelectItem[singleReceivers.size()]);
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

    public int getSingleReceiver() {
        return _singleReceiver;
    }

    public void setSingleReceiver(int _singleReceiver) {
        this._singleReceiver = _singleReceiver;
    }
}
