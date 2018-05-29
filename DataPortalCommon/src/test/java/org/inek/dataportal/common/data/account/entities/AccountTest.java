package org.inek.dataportal.common.data.account.entities;

import java.util.Calendar;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author muellermi
 */
public class AccountTest {

    @Test
    public void testDisclaimerNotConfirmed() {
        Account account = new Account();
        assertFalse(account.isDisclaimerConfirmed());
    }
    
    @Test
    public void testDisclaimerNotConfirmed2() {
        Account account = new Account();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        Date defaultDate = calendar.getTime();
        account.setIkAdminDisclaimer(defaultDate);
        assertFalse(account.isDisclaimerConfirmed());
    }
    
    @Test
    public void testDisclaimerConfirmed() {
        Account account = new Account();
        account.setIkAdminDisclaimer(new Date());
        assertTrue(account.isDisclaimerConfirmed());
    }
    
    @Test
    public void testDisclaimerConfirmed2() {
        Account account = new Account();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.DAY_OF_YEAR, 2);
        Date defaultDate = calendar.getTime();
        account.setIkAdminDisclaimer(defaultDate);
        assertTrue(account.isDisclaimerConfirmed());
    }
    
}
