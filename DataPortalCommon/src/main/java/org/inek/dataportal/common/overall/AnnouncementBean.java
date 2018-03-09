package org.inek.dataportal.common.overall;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.data.adm.Announcement;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class AnnouncementBean {

    @Inject private ConfigFacade _config;
    private List<Announcement> _announcements;

    public List<Announcement> getAnnouncements() {
        if (_announcements == null) {
            _announcements = _config.findActiveWarnings();
            if (isEnabled(ConfigKey.TestMode)) {
                Announcement announcement = new Announcement();
                announcement.setWarning(true);
                announcement.setText("### Testmodus aktiv ###");
                _announcements.add(0, announcement);
            }
        }
        return _announcements;
    }

    public boolean isEnabled(ConfigKey key) {
        return _config.readConfigBool(key);
    }
    
}
