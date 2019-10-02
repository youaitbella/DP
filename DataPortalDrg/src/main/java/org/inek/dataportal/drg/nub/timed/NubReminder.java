package org.inek.dataportal.drg.nub.timed;

import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.structures.ProposalInfo;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.drg.nub.facades.NubRequestFacade;

import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author muellermi
 */
@Singleton
@Startup
public class NubReminder {

    private static final Logger LOGGER = Logger.getLogger("NubReminder");
    @Inject
    private AccountFacade _accountFacade;
    @Inject
    private NubRequestFacade _nubFacade;
    @Inject
    private Mailer _mailer;
    @Inject
    private ConfigFacade _config;

//    @Schedule(hour = "*", minute = "*", second = "*/10") // use this for testing purpose
//    public void remindSealTest() {
//        if (_config.canFirstWriteSynchronizer("Test")) {
//            System.out.println("I am the first");
//        } else {
//            System.out.println("Sombody had been quicker");
//        }
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
        if (!_config.canFirstWriteSynchronizer("NubDrgReminder")) {
            LOGGER.log(Level.INFO, "RemindNubSeal is started by other server");
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
        List<ProposalInfo> requests = _nubFacade.getNubRequestInfos(account.getId(), -1, -1, DataSet.AllOpen, "");
        boolean needsApproval = false;
        for (ProposalInfo request : requests) {
            int pos = request.getName().indexOf(" ", 100);
            String displayName = pos > 0 ? request.getName().substring(0, pos) + "..." : request.getName();
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
