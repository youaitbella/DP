package org.inek.dataportal.common.data.access;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.Function;
import org.inek.dataportal.api.enums.PortalType;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.adm.Announcement;
import org.inek.dataportal.common.data.adm.Config;
import org.inek.dataportal.common.data.common.Conversation;
import org.inek.dataportal.common.data.common.PortalAddress;
import org.inek.dataportal.common.data.common.Synchronizer;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Stage;
import org.inek.dataportal.common.helper.EnvironmentInfo;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author muellermi
 */
@RequestScoped
// todo: find better name
public class ConfigFacade extends AbstractDataAccess {

    private static final String FEATURE = "Feature:";

    private final Map<String, String> _configCache = new HashMap<>();

    private void putCache(String key, String value) {
        _configCache.put(key, value);
    }

    private void saveConfig(String key, String value) {
        putCache(key, value);
        Config config = new Config();
        config.setKey(key);
        config.setValue(value);
        merge(config);
    }

    public void saveConfig(ConfigKey key, String value) {
        saveConfig(key.name(), value);
    }

    public void saveConfig(Feature feature, boolean value) {
        saveConfig(FEATURE + feature.name(), "" + value);
    }

    public void saveConfig(ConfigKey key, boolean value) {
        saveConfig(key, "" + value);
    }

    public void saveConfig(ConfigKey key, int value) {
        saveConfig(key, "" + value);
    }

    public String readConfig(String key, String defaultValue) {
        if (_configCache.containsKey(key)) {
            return _configCache.get(key);
        }
        Config config = findFresh(Config.class, key);

        if (config == null) {
            saveConfig(key, defaultValue);
            return defaultValue;
        }
        putCache(key, config.getValue());
        return config.getValue();
    }

    public String readConfig(ConfigKey key) {
        return readConfig(key.name(), key.getDefault());
    }

    public String readConfig(ConfigKey key, String appendix) {
        String fullKey = key.name() + ":" + appendix;
        return readConfig(fullKey, key.getDefault());
    }

    public boolean readConfigBool(ConfigKey key) {
        return Boolean.parseBoolean(readConfig(key));
    }

    public boolean readConfigBool(ConfigKey key, String appendix) {
        return Boolean.parseBoolean(readConfig(key, appendix));
    }

    public boolean readConfigBool(Feature feature) {
        return Boolean.parseBoolean(readConfig(FEATURE + feature.name(), "false"));
    }

    public int readConfigInt(ConfigKey key) {
        try {
            return Integer.parseInt(readConfig(key.name(), key.getDefault()));
        } catch (NumberFormatException ex) {
            return key.getIntDefault();
        }
    }

    public String readPortalAddress(PortalType portalType, Stage stage) {
        String jpql = "Select pa from PortalAddress pa where pa._portalType = :portalType and pa._stage = :stage";
        TypedQuery<PortalAddress> query = getEntityManager().createQuery(jpql, PortalAddress.class);
        query.setParameter("portalType", portalType);
        query.setParameter("stage", stage);
        return query.getResultList().stream().findFirst().orElse(new PortalAddress()).getUrl();
    }

    public List<Announcement> findActiveWarnings(PortalType portalType) {
        String servername = EnvironmentInfo.getLocalServerName();
        String jpql = "Select a from Announcement a where a._portalType in :portalTypes and a._isActive = true "
                + "and (a._serverName = '' or a._serverName = :servername) "
                + "order by a._id";
        TypedQuery<Announcement> query = getEntityManager().createQuery(jpql, Announcement.class);
        List<PortalType> portalTypes = new ArrayList<>();
        portalTypes.add(portalType);
        portalTypes.add(PortalType.COMMON);
        query.setParameter("portalTypes", portalTypes);
        query.setParameter("servername", servername);
        return query.getResultList();
    }

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public boolean canFirstWriteSynchronizer(String key) {
        String fullKey = key + LocalDateTime.now().format(dateFormatter);
        Synchronizer item = new Synchronizer(fullKey);
        try {
            persist(item);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Transactional
    public Conversation saveConversation(Conversation conversation) {
        if (conversation.getId() == -1) {
            persist(conversation);
            return conversation;
        }
        return merge(conversation);
    }


    public List<Conversation> loadConversations(Function function, Integer id) {
        String jpql = "select c from Conversation c where c._function = :function and c._dataId = :id order by c._id";
        TypedQuery<Conversation> query = getEntityManager().createQuery(jpql, Conversation.class);
        query.setParameter("function", function);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
