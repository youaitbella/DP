package org.inek.dataportal.common.data.access;

import javax.ejb.Stateless;
import org.inek.dataportal.common.data.adm.Config;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.data.AbstractDataAccess;

/**
 *
 * @author muellermi
 */
@Stateless
public class ConfigFacade extends AbstractDataAccess {
    private static final String FEATURE = "Feature:";

    public void save(ConfigKey key, String value) {
        save (key.name(), value);
    }

    public void save(Feature feature, boolean value) {
        save (FEATURE + feature.name(), "" + value);
    }

    private void save(String key, String value) {
        Config config = new Config();
        config.setKey(key);
        config.setValue(value);
        merge(config);
    }

    public String read(ConfigKey key) {
        Config config = findFresh(Config.class, key.name());
        
        if (config == null) {
            save(key, key.getDefault());
            return key.getDefault();
        }
        return config.getValue();
    }

    public String read(ConfigKey key, String appendix) {
        String fullKey = key.name() + ":" + appendix;
        Config config = findFresh(Config.class, fullKey);
        
        if (config == null) {
            save(fullKey, key.getDefault());
            return key.getDefault();
        }
        return config.getValue();
    }

    public void save(ConfigKey key, boolean value) {
        save(key, "" + value);
    }

    public boolean readBool(ConfigKey key) {
        return Boolean.parseBoolean(read(key));
    }

    public boolean readBool(ConfigKey key, String appendix) {
        return Boolean.parseBoolean(read(key, appendix));
    }

    public boolean readBool(Feature feature) {
        Config config = findFresh(Config.class, FEATURE + feature.name());
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
        Config config = findFresh(Config.class, key.name());
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

}
