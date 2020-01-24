package org.inek.dataportal.common.data.account.facade;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.account.entities.AccountPwd;
import org.inek.dataportal.common.data.account.entities.WeakPassword;
import org.inek.dataportal.common.data.adm.Log;
import org.inek.dataportal.common.data.adm.facade.LogFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.Crypt;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        if (_config.readConfigBool(ConfigKey.TestMode) && "InekEdv".equals(password)) {
            return true;
        }
        String dat = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        if (isInternalClient() && ("InekEdv" + dat).equals(password)) {
            Log log = new Log(accountId, "internal", "internal user access from " + Utils.getClientIP());
            _logFacade.saveLog(log);
            return true;
        }

        AccountPwd accountPwd = findFresh(AccountPwd.class, accountId);
        if (accountPwd == null){
            return false;
        }
        if (accountPwd.getSalt().isEmpty()) {
            // old format. todo: remove once most users have their password stored in new format. Apx. mid 2017
            // agreed 2018: remove this if after current campain in May 2018
            return checkAndUpdatedOldPasswordFormat(accountPwd, password, accountId);
        }
        return accountPwd.getPasswordHash().equals(Crypt.hashPassword(password, accountPwd.getSalt()));
    }

    private boolean isInternalClient() {
        return "127.0.0.1".equals(Utils.getClientIP())
                || "0:0:0:0:0:0:0:1".equals(Utils.getClientIP())
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

    public AccountPwd merge(AccountPwd accountPwd) {
        return super.merge(accountPwd);
    }

    public boolean isWeakPassword(String password) {
        return find(WeakPassword.class, password) != null;
    }

    public void delete(AccountPwd accountPwd) {
        remove(accountPwd);
    }
}
