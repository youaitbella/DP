package org.inek.dataportal.base.utils.timed;

import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author muellermi
 */

@Stateless
public class ConfigSwitcher {
    @Inject private ConfigFacade _configFacade;
    
    @Schedule(month = "9", dayOfMonth = "1", hour = "0")
    private void enableNub() {
        _configFacade.saveConfig(ConfigKey.IsNubCreateEnabled, true);
        _configFacade.saveConfig(ConfigKey.IsNubSendEnabled, true);
        _configFacade.saveConfig(ConfigKey.IsPsyNubSendEnabled, true);
        _configFacade.saveConfig(ConfigKey.IsPsyNubCreateEnabled, true);
    }

    @Schedule(month = "11", dayOfMonth = "3", hour = "0")
    private void disableCreateNub() {
        _configFacade.saveConfig(ConfigKey.IsNubCreateEnabled, false);
        _configFacade.saveConfig(ConfigKey.IsPsyNubCreateEnabled, false);
    }

    @Schedule(month = "11", dayOfMonth = "4", hour = "0")
    private void disableSendNub() {
        _configFacade.saveConfig(ConfigKey.IsNubSendEnabled, false);
        _configFacade.saveConfig(ConfigKey.IsPsyNubSendEnabled, false);
    }

    @Schedule(month = "4", dayOfMonth = "1", hour = "0")
    private void disableCreateDrgProposal() {
        _configFacade.saveConfig(ConfigKey.IsDrgProposalCreateEnabled, false);
    }

    @Schedule(month = "4", dayOfMonth = "3", hour = "0")
    private void disableSendDrgProposal() {
        _configFacade.saveConfig(ConfigKey.IsDrgProposalSendEnabled, false);
    }
    
}
