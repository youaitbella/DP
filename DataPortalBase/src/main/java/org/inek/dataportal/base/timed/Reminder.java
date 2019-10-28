package org.inek.dataportal.base.timed;

import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.mail.Mailer;

import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author muellermi
 */
@Singleton
@Startup
public class Reminder {

    private static final Logger LOGGER = Logger.getLogger("NubReminder");
    @Inject
    private AccountFacade _accountFacade;
    @Inject
    private Mailer _mailer;
    @Inject
    private ConfigFacade _config;
    @Inject
    private TimerAccess _timeAccess;

    private static final String QEURY_NUB_DRG = "select nubAccountId as Id, max(nubIk) as IK,"
            + "    dbo.ConcatenateDeli(iif(name = '' or name = ' (*)', '<kein Name angegeben>' + name, "
            + "        iif(len(name) > 100, left(name, 100) + '...', name)), char(13) + char(10)) as [Text]\n"
            + "from (\n"
            + "    select nubAccountId, nubIk, iif(nubDisplayName = '', nubName, nubDisplayName) + iif(nubstatus < 5, '', ' (*)') as name\n"
            + "    from NubProposal\n"
            + "    where nubstatus < 10 and nubtargetyear > datepart(year, getdate()) and nubAccountId in (\n"
            + "        select distinct acId\n"
            + "        from NubProposal\n"
            + "        join account on nubAccountId = acId\n"
            + "        where nubstatus < 10 and nubtargetyear > datepart(year, getdate()) and acCustomerTypeId = 5\n"
            + "    )\n"
            + ") d\n"
            + "group by nubAccountId\n";

    private static final String QEURY_NUB_PEPP = "select nubCreatedByAccountId as Id, max(nubIk) as IK,"
            + "    dbo.ConcatenateDeli(iif(name = '' or name = ' (*)', '<kein Name angegeben>' + name, "
            + "        iif(len(name) > 100, left(name, 100) + '...', name)), char(13) + char(10)) as [Text]\n"
            + "from (\n"
            + "    select nubCreatedByAccountId, nubIk, "
            + "       iif(nubDisplayName = '', nubName, nubDisplayName) + iif(nubStatusId < 5, '', ' (*)') as name\n"
            + "    from psy.NubRequest\n"
            + "    where nubStatusId < 10 and nubtargetyear > datepart(year, getdate()) and nubCreatedByAccountId in (\n"
            + "        select distinct acId\n"
            + "        from NubProposal\n"
            + "        join account on nubCreatedByAccountId = acId\n"
            + "        where nubStatusId < 10 and nubtargetyear > datepart(year, getdate()) and acCustomerTypeId = 5\n"
            + "    )\n"
            + ") d\n"
            + "group by nubCreatedByAccountId\n";


    @Schedule(hour = "*", minute = "*", second = "*/10") // use this for testing purpose
    private void remindSealTest() {
//        remindSeal(ConfigKey.RemindNubSeal, QEURY_NUB_DRG, "NUB reminder");
//        remindSeal(ConfigKey.RemindNubPeppSeal, QEURY_NUB_PEPP, "PSY-NUB reminder");
//        remindSeal(ConfigKey.RemindCareProof, QUERY_CARE_PROOF, "CareProofReminder");
    }

    @Schedule(month = "10", dayOfMonth = "24", hour = "0")
    private void remindSeal7DaysBefore() {
        remindSeal(ConfigKey.RemindNubSeal, QEURY_NUB_DRG, "NUB reminder");
        remindSeal(ConfigKey.RemindNubPeppSeal, QEURY_NUB_PEPP, "PSY-NUB reminder");
    }

    @Schedule(month = "10", dayOfMonth = "30", hour = "0")
    private void remindSeal1DayBefore() {
        remindSeal(ConfigKey.RemindNubSeal, QEURY_NUB_DRG, "NUB reminder");
        remindSeal(ConfigKey.RemindNubPeppSeal, QEURY_NUB_PEPP, "PSY-NUB reminder");
    }


    /**
     * As a service, we will inform all accounts with non-sealed NUB requests
     * one week and one day before the official end of delivery
     */
    @Asynchronous
    private void remindSeal(ConfigKey configKey, String sql, String template) {
        if (!_config.readConfigBool(configKey)) {
            LOGGER.log(Level.INFO, configKey.name() + " is not enabled");
            return;
        }
        if (!_config.canFirstWriteSynchronizer(configKey.name())) {
            LOGGER.log(Level.INFO, configKey.name() + " is started by other server");
            return;
        }
        LOGGER.log(Level.INFO, "Start " + configKey.name());
        for (IdIkText idText : _timeAccess.retrieveIdTexts(sql)) {
            Account account = _accountFacade.findAccount(idText.getId());
            if (account.isReminderMail()) {
                sendReminderMail(account, idText.getIk(), idText.getText(), template);
                if (_config.readConfigBool(ConfigKey.TestMode)) {
                    break;  // in test mode stop after sending one
                }
            }
        }
    }

    private boolean sendReminderMail(Account account, int ik, String text, String template) {
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("{IK}", "" + ik);
        substitutions.put("{listOpenNUB}", "" + text);
        substitutions.put("{date}", "" + text);
        return _mailer.sendMailWithTemplate(template, substitutions, account);
    }

}
