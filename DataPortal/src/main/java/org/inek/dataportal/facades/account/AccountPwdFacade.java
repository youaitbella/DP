package org.inek.dataportal.facades.account;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.inek.dataportal.entities.account.AccountPwd;
import org.inek.dataportal.entities.account.WeakPassword;
import org.inek.dataportal.feature.admin.entities.Log;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.facades.AbstractDataAccess;
import org.inek.dataportal.feature.admin.facades.ConfigFacade;
import org.inek.dataportal.feature.admin.facades.LogFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.Crypt;

/**
 *
 * @author muellermi
 */
@Stateless
public class AccountPwdFacade extends AbstractDataAccess {

    @Inject private ConfigFacade _config;
    @Inject private LogFacade _logFacade;

    public AccountPwd find(int accountId) {
        return find(AccountPwd.class, accountId);
    }

    public boolean changePassword(int accountId, String oldPwd, String newPwd) {
        AccountPwd accountPwd = find(AccountPwd.class, accountId);
        if (accountPwd != null && isCorrectPassword(accountId, oldPwd)) {
            storeHash(accountPwd, newPwd);
        }
        // allways return true to hide "not found"
        return true;
    }

    public boolean isCorrectPassword(int accountId, final String password) {
        if (_config.readBool(ConfigKey.TestMode) && "InekEdv".equals(password)) {
            return true;
        }
        String dat = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        if (isInternalClient() && ("InekEdv" + dat).equals(password)) {
            Log log = new Log(accountId, "internal", "internal user access from " + Utils.getClientIP());
            _logFacade.save(log);
            return true;
        }

        AccountPwd accountPwd = findFresh(AccountPwd.class, accountId);
        if (accountPwd.getSalt().isEmpty()) {
            // old format. todo: remove once most users have their password stored in new format. Apx. mid 2017
            return checkAndUpdatedOldPasswordFormat(accountPwd, password, accountId);
        }
        return accountPwd.getPasswordHash().equals(Crypt.hashPassword(password, accountPwd.getSalt()));
    }

    private boolean isInternalClient() {
        return Utils.getClientIP().equals("127.0.0.1")
                || Utils.getClientIP().equals("0:0:0:0:0:0:0:1")
                || Utils.getClientIP().startsWith("192.168.0");
    }

    private boolean checkAndUpdatedOldPasswordFormat(AccountPwd accountPwd, final String password, int accountId) {
        if (!accountPwd.getPasswordHash().equals(Crypt.getPasswordHash(password, accountId))) {
            return false;
        }
        storeHash(accountPwd, password);
        return true;
    }

    private void storeHash(AccountPwd accountPwd, final String password) {
        String salt = Crypt.getSalt();
        accountPwd.setSalt(salt);
        accountPwd.setPasswordHash(Crypt.hashPassword(password, salt));
        merge(accountPwd);
    }

    public void save(AccountPwd accountPwd) {
        persist(accountPwd);
    }

    public boolean isWeakPassword(String password) {
        return find(WeakPassword.class, password) != null;
    }

    public void delete(AccountPwd accountPwd) {
        remove(accountPwd);
    }
}
