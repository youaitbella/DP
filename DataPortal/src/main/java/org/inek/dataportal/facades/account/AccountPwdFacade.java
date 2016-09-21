package org.inek.dataportal.facades.account;

import java.util.UUID;
import javax.ejb.Stateless;
import org.inek.dataportal.entities.account.AccountPwd;
import org.inek.dataportal.facades.AbstractFacade;
import org.inek.dataportal.utils.Crypt;

/**
 *
 * @author muellermi
 */
@Stateless
public class AccountPwdFacade extends AbstractFacade<AccountPwd> {

    public AccountPwdFacade() {
        super(AccountPwd.class);
    }

    public Boolean changePassword(int accountId, String oldPwd, String newPwd) {
        AccountPwd accountPwd = find(accountId);
        if (accountPwd != null && isCorrectPassword(accountId, oldPwd)) {
            accountPwd.setPasswordHash(Crypt.getPasswordHash(newPwd, accountId));
            merge(accountPwd);
        }
        // allways return true to hide "not found"
        return true;
    }

    public boolean isCorrectPassword(int accountId, final String password) {
        AccountPwd accountPwd = findFresh(accountId);
        if (accountPwd.getSalt().isEmpty()) {
            // old format
            return checkAndUpdatedOldPasswordFormat(accountPwd, password, accountId);
        }
        return accountPwd.getPasswordHash().equals(Crypt.hashPassword(password, accountPwd.getSalt()));
    }

    private boolean checkAndUpdatedOldPasswordFormat(AccountPwd accountPwd, final String password, int accountId) {
        if (!accountPwd.getPasswordHash().equals(Crypt.getPasswordHash(password, accountId))){
            return false;
        }
        String salt = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        accountPwd.setSalt(salt);
        accountPwd.setPasswordHash(Crypt.hashPassword(password, salt));
        merge(accountPwd);
        return true;
    }

    public void save(AccountPwd accountPwd) {
        persist(accountPwd);
    }

}
