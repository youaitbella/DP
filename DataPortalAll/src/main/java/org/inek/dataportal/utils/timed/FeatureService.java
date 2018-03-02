/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.utils.timed;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountFeature;
import org.inek.dataportal.common.data.account.entities.AccountFeatureRequest;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.FeatureState;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.common.data.account.facade.AccountFeatureRequestFacade;
import org.inek.dataportal.requestmanager.FeatureRequestHandler;

/**
 *
 * @author muellermi
 */
@Singleton
@Startup
public class FeatureService {

    private static final Logger LOGGER = Logger.getLogger("FeatureService");
    @Inject private AccountFacade _accountFacade;
    @Inject private AccountFeatureRequestFacade _requestFacade;
    @Inject private FeatureRequestHandler _handler;

    @Schedule(hour = "0", info = "once a day")
    //    @Schedule(hour = "*", minute = "*/1", info = "every minute")  // use this for testing purpose
    public void startCheckFeatures() {
        checkFeatures();
    }
    
    @Asynchronous
    public void checkFeatures() {
        
        LOGGER.log(Level.INFO, "Start checkFeatures");
        check4requestedFeatures();
        check4orphantRequests();
    }

    private void check4requestedFeatures() {
        Set<Account> accounts = _accountFacade.getAccountsWithRequestedFeatures();
        for (Account account : accounts) {
            for (AccountFeature accFeature : account.getFeatures()) {
                if (accFeature.getFeatureState() == FeatureState.REQUESTED) {
                    Feature feature = accFeature.getFeature();
                    _handler.handleFeatureRequest(account, feature);
                }
            }
        }
    }

    /**
     * removes AccountFeatureRequests if no account or no corresponding feature
     * This might happen, if the user de-tagged a feature or deleted her account
     */
    private void check4orphantRequests() {
        List<AccountFeatureRequest> requests = _requestFacade.findAll();
        for (AccountFeatureRequest request : requests) {
            Account account = _accountFacade.findAccount(request.getAccountId());
            if (account == null) {
                _requestFacade.remove(request);
                continue;
            }
            Optional<AccountFeature> optFeature = account.getFeatures().stream().filter(f -> f.getFeature() == request.getFeature()).findFirst();
            if (!optFeature.isPresent() || optFeature.get().getFeatureState() != FeatureState.REQUESTED) {
                _requestFacade.remove(request);
            }
        }
    }
    
}
