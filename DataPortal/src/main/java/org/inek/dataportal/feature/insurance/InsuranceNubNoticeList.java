package org.inek.dataportal.feature.insurance;

import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.insurance.InsuranceNubNotice;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;

@Named
@RequestScoped
public class InsuranceNubNoticeList {

    private static final Logger _logger = Logger.getLogger("InsuranceNubNoticeList");
    @Inject SessionController _sessionController;

    private List<InsuranceNubNotice> _openMessages;
    private List<InsuranceNubNotice> _providedMessages;

    public List<InsuranceNubNotice> getNubRequests() {
        if (_openMessages == null) {
            // todo: _openMessages = ...
        }
        return _openMessages;
    }

    public List<InsuranceNubNotice> getSealedNubRequests() {
        if (_providedMessages == null) {
            //_providedMessages = ...
        }
        return _providedMessages;
    }

    public String newInsuranceNubNotice() {
        // if the user hit the browser's back-button, a request might be still active. 
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("Insurance");
        return Pages.InsuranceNubNoticeEditAddress.URL();
    }


}
