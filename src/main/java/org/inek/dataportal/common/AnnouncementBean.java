package org.inek.dataportal.common;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.Announcement;
import org.inek.dataportal.facades.AnnouncementFacade;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class AnnouncementBean {
    @Inject
    private AnnouncementFacade _announcementFacade;
    private List<Announcement> _announcements;
    
    public List<Announcement> getAnnouncements() {
        if (_announcements == null){
           _announcements = _announcementFacade.findActiveWarnings();
        }
        return _announcements;
       
    }
    
}
