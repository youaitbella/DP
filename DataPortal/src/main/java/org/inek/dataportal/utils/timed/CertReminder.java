package org.inek.dataportal.utils.timed;

import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.inek.dataportal.entities.certification.Grouper;
import org.inek.dataportal.facades.certification.GrouperFacade;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Singleton
@Startup
public class CertReminder {

    @Inject
    private Mailer _mailer;
    @Inject
    private GrouperFacade _grouperFacade;

    //@Schedule(minute = "*/1")
    @Schedule(hour = "*/12")
    public void remind() {
        remindNotReleasedGrouper();
    }

    public void remindNotReleasedGrouper() {
        List<Grouper> gr = _grouperFacade.getGrouperWithoutWebsideRealease();
        if (!gr.isEmpty()) {
            sendReminderMail(gr);
        }
    }

    private void sendReminderMail(List<Grouper> grouper) {
        String grouperList = buildGrouperList(grouper);

        _mailer.sendMail("tim.lautenschlaeger@inek-drg.de", "Unveröffetnlichte Grouperhersteller", grouperList);
    }

    private String buildGrouperList(List<Grouper> grouper) {
        String list = "Folgende Zertifizierte Grouper sind noch nicht auf der Homepage veröffentlicht: \n";
        for (Grouper gr : grouper) {
            list += gr.getAccount().getCompany() + "\t" + gr.getName() + "\t" + gr.getCertification() + "\n";
        }
        return list;
    }
}
