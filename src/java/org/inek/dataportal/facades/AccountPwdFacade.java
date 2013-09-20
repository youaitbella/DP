package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import javax.inject.Named;
import org.inek.dataportal.entities.AccountPwd;
import org.inek.dataportal.utils.Crypt;
import org.inek.dataportal.utils.SessionInfo;

/**
 *
 * @author muellermi
 */
@Stateless
public class AccountPwdFacade extends AbstractFacade<AccountPwd> {

    public AccountPwdFacade (){
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
        AccountPwd accountPwd = find(accountId);
        // temporarily check for old hash style too
        if (accountPwd.getPasswordHash().equals(Crypt.getPasswordHash(password, accountId))) {
            return true;
        }
        return false;
    }
 
        
}
