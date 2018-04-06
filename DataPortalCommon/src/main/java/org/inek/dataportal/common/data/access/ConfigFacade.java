package org.inek.dataportal.common.data.access;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.common.data.adm.Config;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.adm.Announcement;
import org.inek.dataportal.common.data.common.PortalAddress;
import org.inek.dataportal.common.enums.PortalType;
import org.inek.dataportal.common.enums.Stage;

/**
 *
 * @author muellermi
 */
@Stateless
public class ConfigFacade extends AbstractDataAccess {
    private static final String FEATURE = "Feature:";

    public void saveConfig(ConfigKey key, String value) {
        saveConfig (key.name(), value);
    }

    public void saveConfig(Feature feature, boolean value) {
        saveConfig (FEATURE + feature.name(), "" + value);
    }

    private void saveConfig(String key, String value) {
        Config config = new Config();
        config.setKey(key);
        config.setValue(value);
        merge(config);
    }

    public String readConfig(ConfigKey key) {
        Config config = findFresh(Config.class, key.name());
        
        if (config == null) {
            saveConfig(key, key.getDefault());
            return key.getDefault();
        }
        return config.getValue();
    }

    public String readConfig(ConfigKey key, String appendix) {
        String fullKey = key.name() + ":" + appendix;
        Config config = findFresh(Config.class, fullKey);
        
        if (config == null) {
            saveConfig(fullKey, key.getDefault());
            return key.getDefault();
        }
        return config.getValue();
    }

    public void saveConfig(ConfigKey key, boolean value) {
        saveConfig(key, "" + value);
    }

    public boolean readConfigBool(ConfigKey key) {
        return Boolean.parseBoolean(readConfig(key));
    }

    public boolean readConfigBool(ConfigKey key, String appendix) {
        return Boolean.parseBoolean(readConfig(key, appendix));
    }

    public boolean readConfigBool(Feature feature) {
        Config config = findFresh(Config.class, FEATURE + feature.name());
        if (config == null) {
            saveConfig(feature, true);
            return true;
        }
        return Boolean.parseBoolean(config.getValue());
    }

    public void saveConfig(ConfigKey key, int value) {
        saveConfig(key, "" + value);
    }

    public int readConfigInt(ConfigKey key) {
        Config config = findFresh(Config.class, key.name());
        if (config == null) {
            saveConfig(key, key.getIntDefault());
            return key.getIntDefault();
        }
        try {
            return Integer.parseInt(config.getValue());
        } catch (NumberFormatException ex) {
            return key.getIntDefault();
        }
    }

    public String readPortalAddress(PortalType portalType, Stage stage){
        String jpql = "Select pa from PortalAddress pa where pa._portalType = :portalType and pa._stage = :stage";
        TypedQuery<PortalAddress> query = getEntityManager().createQuery(jpql, PortalAddress.class);
        query.setParameter("portalType", portalType);
        query.setParameter("stage", stage);
        return query.getResultList().stream().findFirst().orElse(new PortalAddress()).getUrl();
    }
    
    public List<Announcement> findActiveWarnings(PortalType portalType) {
        String jpql = "Select a from Announcement a where a._portalType in :portalTypes and a._isActive = true order by a._id";
        TypedQuery<Announcement> query = getEntityManager().createQuery(jpql, Announcement.class);
        List<PortalType> portalTypes = new ArrayList<>();
        portalTypes.add(portalType);
        portalTypes.add(PortalType.COMMON);
        query.setParameter("portalTypes", portalTypes);
        return query.getResultList();
    }
   
}
