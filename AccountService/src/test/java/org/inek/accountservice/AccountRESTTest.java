package org.inek.accountservice;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author muellermi
 */
public class AccountRESTTest {

    // tests won't execute parallel!
    
    @Test
    public void storeIdAnRetrieveItByToken() {
        AccountREST.clear();
        String id = "12345";
        AccountREST service = new AccountREST();
        String token = service.getToken(id);
        assertThat(service.getSize()).isEqualTo(1);
        assertThat(service.getAccountId(token)).isEqualTo(id);
        assertThat(service.getSize()).isEqualTo(0);
    }

    @Test
    public void storeIdAnRetrieveItByTokenUsingDifferentInstance() {
        AccountREST.clear();
        String id = "12345";
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
        String id = "12345";
        AccountREST service = new AccountREST();
        String token = service.getToken(id);
        Thread.sleep(2001);
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
        Thread.sleep(1001);
        service.getToken("4");
        service.getToken("5");
        service.getToken("6");
        assertThat(service.getSize()).isEqualTo(6);
        Thread.sleep(1001);
        service.sweepOld();
        assertThat(service.getSize()).isEqualTo(3);
    }
}
