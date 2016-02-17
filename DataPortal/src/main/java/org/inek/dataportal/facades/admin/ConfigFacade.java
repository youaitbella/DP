package org.inek.dataportal.facades.admin;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import org.inek.dataportal.entities.admin.Config;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
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

    public void save(ConfigKey key, String value) {
        Config config = new Config();
        config.setKey(key.name());
        config.setValue(value);
        merge(config);
    }

    public void save(Feature feature, boolean value) {
        Config config = new Config();
        config.setKey("feature_" + feature.name());
        config.setValue("" + value);
        merge(config);
    }

    public String read(ConfigKey key) {
        Config config = findFresh(key.name());
        
        if (config == null) {
            save(key, key.getDefault());
            return key.getDefault();
        }
        return config.getValue();
    }

    public void save(ConfigKey key, boolean value) {
        save(key, "" + value);
    }

    public boolean readBool(ConfigKey key) {
        Config config = findFresh(key.name());
        if (config == null) {
            save(key, key.getBoolDefault());
            return key.getBoolDefault();
        }
        return Boolean.parseBoolean(config.getValue());
    }

    public boolean readBool(Feature feature) {
        Config config = findFresh("feature_" + feature.name());
        if (config == null) {
            save(feature, true);
            return true;
        }
        return Boolean.parseBoolean(config.getValue());
    }

    public void save(ConfigKey key, int value) {
        save(key, "" + value);
    }

    public int readInt(ConfigKey key) {
        Config config = findFresh(key.name());
        if (config == null) {
            save(key, key.getIntDefault());
            return key.getIntDefault();
        }
        try {
            return Integer.parseInt(config.getValue());
        } catch (NumberFormatException ex) {
            return key.getIntDefault();
        }
    }

    @Schedule(month = "9", dayOfMonth = "1", hour = "0")
    private void enableNub() {
        save(ConfigKey.IsNubCreateEnabled, true);
        save(ConfigKey.IsNubSendEnabled, true);
    }

    @Schedule(month = "11", dayOfMonth = "3", hour = "0")
    private void disableCreateNub() {
        save(ConfigKey.IsNubCreateEnabled, false);
    }

    @Schedule(month = "11", dayOfMonth = "4", hour = "0")
    private void disableSendNub() {
        save(ConfigKey.IsNubSendEnabled, false);
    }

}
