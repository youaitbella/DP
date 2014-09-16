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
                    break;
                case TestFailed2:
                    errors = _grouper.getTestError2();
                    break;
                case TestFailed3:
                    errors = _grouper.getTestError3();
                    break;
                case CertFailed1:
                    errors = _grouper.getCertError1();
                    break;
                case CertFailed2:
                    errors = _grouper.getCertError2();
                    break;
            }
        }
        return errors;
    }
    
    public String getCertState() {
        return "Zertifizierungsstatus: " + _grouper.getCertStatus().getLabel();
    }
    
    public boolean renderErrorField() {
        return getNumberOfErrors() > 0;
    }
}
