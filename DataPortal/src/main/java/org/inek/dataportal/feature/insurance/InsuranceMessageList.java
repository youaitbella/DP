package org.inek.dataportal.feature.insurance;

import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.nub.NubInsuranceMessage;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;

@Named
@RequestScoped
public class InsuranceMessageList {

    private static final Logger _logger = Logger.getLogger("InsuranceMessageList");
    @Inject SessionController _sessionController;

    private List<NubInsuranceMessage> _openMessages;
    private List<NubInsuranceMessage> _providedMessages;

    public List<NubInsuranceMessage> getNubRequests() {
        if (_openMessages == null) {
            // todo: _openMessages = ...
        }
        return _openMessages;
    }

    public List<NubInsuranceMessage> getSealedNubRequests() {
        if (_providedMessages == null) {
            //_providedMessages = ...
        }
        return _providedMessages;
    }

    public String newInsuranceMessage() {
        // if the user hit the browser's back-button, a request might be still active. 
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("EditInsuranceMessage");
        return Pages.InsuranceMessageEditAddress.URL();
    }


}
