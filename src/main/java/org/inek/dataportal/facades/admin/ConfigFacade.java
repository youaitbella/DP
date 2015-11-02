package org.inek.dataportal.facades.admin;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import org.inek.dataportal.entities.admin.Config;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.facades.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class ConfigFacade extends AbstractFacade<Config> {

    public ConfigFacade() {
        super(Config.class);
    }

    public void save(String key, String value) {
        Config config = new Config();
        config.setKey(key);
        config.setValue(value);
        merge(config);
    }

    public String read(String key, String defaultVal) {
        Config config = findFresh(key);
        
        if (config == null) {
            save(key, defaultVal);
            return defaultVal;
        }
        return config.getValue();
    }

    public void save(String key, boolean value) {
        save(key, "" + value);
    }

    public boolean read(String key, boolean defaultVal) {
        Config config = findFresh(key);
        if (config == null) {
            save(key, defaultVal);
            return defaultVal;
        }
        return Boolean.parseBoolean(config.getValue());
    }

    public void save(String key, int value) {
        save(key, "" + value);
    }

    public int read(String key, int defaultVal) {
        Config config = find(key);
        if (config == null) {
            save(key, defaultVal);
            return defaultVal;
        }
        try {
            return Integer.parseInt(config.getValue());
        } catch (NumberFormatException ex) {
            return defaultVal;
        }
    }

    @Schedule(month = "9", dayOfMonth = "1", hour = "0")
    private void enableNub() {
        save(ConfigKey.IsNubCreateEnabled.name(), true);
        save(ConfigKey.IsNubSendEnabled.name(), true);
    }

    @Schedule(month = "11", dayOfMonth = "3", hour = "0")
    private void disableCreateNub() {
        save(ConfigKey.IsNubCreateEnabled.name(), false);
    }

    @Schedule(month = "11", dayOfMonth = "4", hour = "0")
    private void disableSendNub() {
        save(ConfigKey.IsNubSendEnabled.name(), false);
    }

}
