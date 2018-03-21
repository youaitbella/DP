package org.inek.accountservice;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import static org.inek.accountservice.AccountREST.VALID_TIME;

/**
 *
 * @author muellermi
 */
public class AccountRESTTest {

    // tests won't execute parallel!
    private static final String ID = "12345";
    private static final int DELAY = 5;
    
    @Test
    public void storeIdAnRetrieveItByToken() {
        AccountREST.clear();
        String id = ID;
        AccountREST service = new AccountREST();
        String token = service.getToken(id);
        assertThat(service.getSize()).isEqualTo(1);
        assertThat(service.getAccountId(token)).isEqualTo(id);
        assertThat(service.getSize()).isEqualTo(0);
    }

    @Test
    public void storeIdAnRetrieveItByTokenUsingDifferentInstance() {
        AccountREST.clear();
        String id = ID;
        AccountREST service = new AccountREST();
        String token = service.getToken(id);
        assertThat(service.getSize()).isEqualTo(1);
        service = new AccountREST();
        assertThat(service.getAccountId(token)).isEqualTo(id);
        assertThat(service.getSize()).isEqualTo(0);
    }

    @Test
    public void storeIdAndReceiveNothingBackAfterDelay() throws InterruptedException {
        AccountREST.clear();
        String id = ID;
        AccountREST service = new AccountREST();
        String token = service.getToken(id);
        Thread.sleep(VALID_TIME + DELAY);
        assertThat(service.getAccountId(token)).isEqualTo("");
    }

    @Test
    public void anImmediateCleanDoesNotSweepOut() throws InterruptedException {
        AccountREST.clear();
        AccountREST service = new AccountREST();
        service.getToken("1");
        service.getToken("2");
        service.getToken("3");
        assertThat(service.getSize()).isEqualTo(3);
        service.sweepOld();
        assertThat(service.getSize()).isEqualTo(3);
    }

    @Test
    public void aCleanAfterTheValidTimeCleansOldEntries() throws InterruptedException {
        AccountREST.clear();
        AccountREST service = new AccountREST();
        service.getToken("1");
        service.getToken("2");
        service.getToken("3");
        Thread.sleep(VALID_TIME / 2);
        service.getToken("4");
        service.getToken("5");
        service.getToken("6");
        assertThat(service.getSize()).isEqualTo(6);
        Thread.sleep(VALID_TIME / 2 + DELAY);
        service.sweepOld();
        assertThat(service.getSize()).isEqualTo(3);
    }
}
