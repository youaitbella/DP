package org.inek.dataportal.feature.certification;

import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.certification.Grouper;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
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
    
    public boolean passedTestPhase() {
        return _grouper.getTestError1() == 0 || _grouper.getTestError2() == 0 || _grouper.getTestError3() == 0;
    }
    
    public boolean isCertified() {
        return passedTestPhase() && (_grouper.getCertError1() == 0 || _grouper.getCertError2() == 0);
    }
}
