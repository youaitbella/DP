package org.inek.dataportal.common;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.feature.admin.entities.Announcement;
import org.inek.dataportal.feature.admin.facades.AnnouncementFacade;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class AnnouncementBean {

    @Inject private AnnouncementFacade _announcementFacade;
    @Inject private ApplicationTools _appTools;
    private List<Announcement> _announcements;

    public List<Announcement> getAnnouncements() {
        if (_announcements == null) {
            _announcements = _announcementFacade.findActiveWarnings();
            if (_appTools.isEnabled(ConfigKey.TestMode)) {
                Announcement announcement = new Announcement();
                announcement.setWarning(true);
                announcement.setText("### Testmodus aktiv ###");
                _announcements.add(0, announcement);
            }
        }
        return _announcements;

    }

}
