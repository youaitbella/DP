package org.inek.dataportal.feature.certification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.entities.certification.Grouper;
import org.inek.dataportal.enums.CertMailType;
import org.inek.dataportal.enums.CertStatus;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.facades.certification.EmailLogFacade;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class CertGrouperResults {

    private static final Logger _logger = Logger.getLogger("CertGrouperResults");
    private Grouper _grouper;
    private int _runs = 0;
    private String _selectedTemplate = "";
    private String _attachement = "";
    
    @Inject
    private AccountFacade _accFacade;
    
    @Inject
    private SystemFacade _sysFacade;
    
    @Inject
    private MailTemplateFacade _mtFacade;
    
    @Inject
    private EmailLogFacade _elFacade;
    
    public String showResults(Grouper grouper) {
        _grouper = grouper;
        
        return Pages.CertGrouperResults.RedirectURL();
    }
    
    public String getSystemName() {
        return _sysFacade.find(_grouper.getSystemId()).getDisplayName();
    }
    
    public String getCompanyName() {
        return _accFacade.find(_grouper.getAccountId()).getCompany();
    }
    
    public String getEmailReceivers() {
        return _accFacade.find(_grouper.getAccountId()).getEmail() + ";" + _grouper.getEmailCopy();
    }
    
    public String getEmailReceiver() {
        return _accFacade.find(_grouper.getAccountId()).getEmail();
    }
    
    public boolean hasNotDeliveredData() {
        return _grouper.getCertStatus() == CertStatus.New || _grouper.getCertStatus() == CertStatus.PasswordRequested;
    }

    public String getSelectedTemplate() {
        return _selectedTemplate;
    }

    public void setSelectedTemplate(String _selectedTemplate) {
        this._selectedTemplate = _selectedTemplate;
    }
    
    public List<SelectItem> getTemplates() {
        List<SelectItem> temp = new ArrayList<>();
        List<MailTemplate> mts = _mtFacade.findTemplatesByFeature(Feature.CERT);
        switch(_grouper.getCertStatus()) {
            case TestFailed1:
            case TestFailed2:
            case TestFailed3:
                addTemplatesToList(temp, mts, CertMailType.ErrorTest);
                break;
            case CertFailed1:
            case CertFailed2:
                addTemplatesToList(temp, mts, CertMailType.ErrorCert);
                break;
            case TestSucceed:
                addTemplatesToList(temp, mts, CertMailType.PassedTest);
                break;
            case CertSucceed:
                addTemplatesToList(temp, mts, CertMailType.Certified);
                break;
        }
        return temp;
    }
    
    private void addTemplatesToList(List<SelectItem> list, List<MailTemplate> mts, CertMailType type) {
        for(MailTemplate mt : mts) {
            if(mt.getType() == type.getId()) {
                list.add(new SelectItem(mt.getName()));
            }
        }
    }
    
    public int getNumberOfErrors() {
        int errors = 0;
        if(!hasNotDeliveredData()) {
            switch(_grouper.getCertStatus()) {
                case TestFailed1:
                    errors = _grouper.getTestError1();
                    _runs = 1;
                    break;
                case TestFailed2:
                    errors = _grouper.getTestError2();
                    _runs = 2;
                    break;
                case TestFailed3:
                    errors = _grouper.getTestError3();
                    _runs = 3;
                    break;
                case CertFailed1:
                    errors = _grouper.getCertError1();
                    _runs = 1;
                    break;
                case CertFailed2:
                    errors = _grouper.getCertError2();
                    _runs = 2;
                    break;
            }
        }
        return errors;
    }

    public int getRuns() {
        return _runs;
    }
    
    public String getCertState() {
        return "Zertifizierungsstatus: " + _grouper.getCertStatus().getLabel();
    }
    
    public boolean containsErrorInLatestUpload() {
        return getNumberOfErrors() > 0;
    }
    
    public boolean hasReceivedEmailToCurrentState() {
        CertMailType mailType = null;
        int numofMails = 0;
        switch(_grouper.getCertStatus()) {
            case CertFailed1:
                numofMails = 1;
                mailType = CertMailType.ErrorCert;
                break;
            case CertFailed2:
                numofMails = 2;
                mailType = CertMailType.ErrorCert;
                break;
            case TestFailed1:
                numofMails = 1;
                mailType = CertMailType.ErrorTest;
                break;
            case TestFailed2:
                numofMails = 2;
                mailType = CertMailType.ErrorTest;
                break;
            case TestFailed3:
                numofMails = 3;
                mailType = CertMailType.ErrorTest;
                break;
            case TestSucceed:
                numofMails = 1;
                mailType = CertMailType.PassedTest;
                break;
            case CertSucceed:
                numofMails = 1;
                mailType = CertMailType.Certified;
                break;
        }
        if(mailType == null)
            return false;
        return _elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(_grouper.getSystemId(), _grouper.getAccountId(), mailType.getId()).size() >= numofMails;
    }
    
    public String getReiceiver() {
        return _accFacade.find(_grouper.getAccountId()).getEmail();
    }
    
    public String getCC() {
        return _grouper.getEmailCopy();
    }
    
    public String getBCC() {
        if(_selectedTemplate.equals(""))
            return "";
        return _mtFacade.findByName(_selectedTemplate).getBcc();
    }

    public String getAttachement() {
        return _attachement;
    }

    public void setAttachement(String _attachement) {
        this._attachement = _attachement;
    }
    
    public String getBody() {
        if(_selectedTemplate.equals(""))
            return "";
        return _mtFacade.findByName(_selectedTemplate).getBody().replace("{}", ""); //TODO
    }
}
