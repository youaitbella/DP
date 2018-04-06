package org.inek.dataportal.common.overall;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.data.adm.Announcement;
import org.inek.dataportal.common.helper.EnvironmentInfo;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class AnnouncementBean {

    @Inject private ConfigFacade _config;
    @Inject private SessionController _sessionController;
    private List<Announcement> _announcements;

    public List<Announcement> getAnnouncements() {
        if (_announcements == null) {
            _announcements = _config.findActiveWarnings(_sessionController.getPortalType());
            if (isEnabled(ConfigKey.TestMode)) {
                Announcement announcement = new Announcement();
                announcement.setWarning(true);
                announcement.setText("### Testmodus [" + EnvironmentInfo.getServerName() + "] aktiv ###");
                _announcements.add(0, announcement);
            }
        }
        return _announcements;
    }

    public boolean isEnabled(ConfigKey key) {
        return _config.readConfigBool(key);
    }
    
}
