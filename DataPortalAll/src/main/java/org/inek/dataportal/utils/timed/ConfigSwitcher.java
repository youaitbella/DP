package org.inek.dataportal.utils.timed;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.feature.admin.facade.ConfigFacade;

/**
 *
 * @author muellermi
 */

@Stateless
public class ConfigSwitcher {
    @Inject private ConfigFacade _configFacade;
    
    @Schedule(month = "9", dayOfMonth = "1", hour = "0")
    private void enableNub() {
        _configFacade.save(ConfigKey.IsNubCreateEnabled, true);
        _configFacade.save(ConfigKey.IsNubSendEnabled, true);
    }

    @Schedule(month = "11", dayOfMonth = "3", hour = "0")
    private void disableCreateNub() {
        _configFacade.save(ConfigKey.IsNubCreateEnabled, false);
    }

    @Schedule(month = "11", dayOfMonth = "4", hour = "0")
    private void disableSendNub() {
        _configFacade.save(ConfigKey.IsNubSendEnabled, false);
    }

    @Schedule(month = "4", dayOfMonth = "1", hour = "0")
    private void disableCreateDrgProposal() {
        _configFacade.save(ConfigKey.IsDrgProposalCreateEnabled, false);
    }

    @Schedule(month = "4", dayOfMonth = "3", hour = "0")
    private void disableSendDrgProposal() {
        _configFacade.save(ConfigKey.IsDrgProposalSendEnabled, false);
    }
    
}
