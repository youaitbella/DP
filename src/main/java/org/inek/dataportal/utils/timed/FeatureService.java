/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.utils.timed;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountFeature;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.FeatureState;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.requestmanager.FeatureRequestHandler;

/**
 *
 * @author muellermi
 */
@Singleton
public class FeatureService {

    private static final Logger _logger = Logger.getLogger("FeatureService");
    @EJB private AccountFacade _accountFacade;
    @EJB private FeatureRequestHandler _handler;

    @Schedule(hour = "0", info = "once a day")
    public void checkFeatures() {
        _logger.log(Level.INFO, "Start checkFeatures");
        check4requestedFeatures();
    }

    private void check4requestedFeatures() {
        List<Account> accounts = _accountFacade.getAccountsWithRequestedFeatures();
        for (Account account : accounts) {
            for (AccountFeature accFeature : account.getFeatures()) {
                if (accFeature.getFeatureState() == FeatureState.REQUESTED) {
                    Feature feature = accFeature.getFeature();
                    _handler.handleFeatureRequest(account, feature);
                }
            }
        }
    }

}
