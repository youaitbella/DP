package org.inek.dataportal.facades.account;

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
