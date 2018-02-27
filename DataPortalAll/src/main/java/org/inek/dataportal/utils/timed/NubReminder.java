package org.inek.dataportal.utils.timed;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.inek.dataportal.entities.nub.NubRequest;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Singleton
@Startup
public class NubReminder {

    private static final Logger LOGGER = Logger.getLogger("NubReminder");
    @Inject private AccountFacade _accountFacade;
    @Inject private NubRequestFacade _nubFacade;
    @Inject private Mailer _mailer;
    @Inject private ConfigFacade _config;

//    @Schedule(hour = "*", minute = "*/1", info = "every minute") // use this for testing purpose
//    public void remindSealTest() {
//        remindSeal();
//    }
    @Schedule(month = "10", dayOfMonth = "24", hour = "0")
    public void remindSeal7DaysBefore() {
        remindSeal();
    }

    @Schedule(month = "10", dayOfMonth = "30", hour = "0")
    public void remindSeal1DayBefore() {
        remindSeal();
    }

    /**
     * As a service, we will inform all accounts with non-sealed NUB requests
     * one week and one day before the official end of delivery
     */
    @Asynchronous
    public void remindSeal() {
        if (!_config.readConfigBool(ConfigKey.RemindNubSeal) || _config.readConfigBool(ConfigKey.TestMode)) {
            LOGGER.log(Level.INFO, "RemindNubSeal is not enabled");
            return;
        }
        LOGGER.log(Level.INFO, "Start remindSeal");
        Map<Integer, Integer> accounts = _nubFacade.countOpenPerIk();
        for (int accountId : accounts.keySet()) {
            Account account = _accountFacade.findAccount(accountId);
            if (account.isNubInformationMail()) {
                sendReminderMail(account);
            }
        }
    }

    private boolean sendReminderMail(Account account) {
        MailTemplate template = _mailer.getMailTemplate("NUB reminder");
        if (template == null) {
            return false;
        }

        String salutation = _mailer.getFormalSalutation(account);
        String body = template.getBody()
                .replace("{formalSalutation}", salutation)
                .replace("{listOpenNUB}", getOpenNubs(account));
        return _mailer.sendMailFrom("NUB Datenannahme <nub@inek-drg.de>", account.getEmail(), template.getBcc(), template.getSubject(), body);
    }

    private String getOpenNubs(Account account) {
        StringBuilder sb = new StringBuilder();
        List<NubRequest> requests = _nubFacade.findAll(account.getId(), DataSet.AllOpen, "");
        boolean needsApproval = false;
        for (NubRequest request : requests) {
            String displayName = request.getDisplayName().trim();
            if (displayName.isEmpty()) {
                int pos = request.getName().indexOf(" ", 100);
                displayName = pos > 0 ? request.getName().substring(0, pos) + "..." : request.getName();
            }
            if (displayName.isEmpty()) {
                displayName = "<kein Name angegeben>";
            }
            sb.append(" - ").append(displayName);
            if (request.getStatus() == WorkflowStatus.ApprovalRequested) {
                sb.append(" (*)");
                needsApproval = true;
            }
            sb.append("\r\n");
        }
        if (needsApproval) {
            sb.append("\r\n");
            sb.append("Die mit (*) markierte(n) Anfrage(n) warten auf eine Freigabe durch Ihren Kooperationspartner oder Supervisor. ");
            sb.append("Bitte informieren Sie diesen entsprechend.");
            sb.append("\r\n");
        }
        return sb.toString();
    }

}
