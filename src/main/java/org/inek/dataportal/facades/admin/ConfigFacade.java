package org.inek.dataportal.facades.admin;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import org.inek.dataportal.entities.admin.Config;
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
        Config config = find(key);
        if (config == null) {
            return defaultVal;
        }
        return config.getValue();
    }

    public void save(String key, boolean value) {
        save(key, "" + value);
    }

    public boolean read(String key, boolean defaultVal) {
        Config config = find(key);
        if (config == null) {
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
            return defaultVal;
        }
        try {
            return Integer.parseInt(config.getValue());
        } catch (NumberFormatException ex) {
            return defaultVal;
        }
    }

    @Schedule(hour = "*", minute = "*", second = "*/10")
    private void test() {
        save("test", true);
        boolean result = read("test", false);
        assert (result);
    }

}
