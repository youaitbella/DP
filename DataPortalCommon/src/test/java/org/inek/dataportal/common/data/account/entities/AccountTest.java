package org.inek.dataportal.common.data.account.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.ikadmin.entity.IkAdmin;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

/**
 *
 * @author muellermi
 */
public class AccountTest {

    @Test
    public void testDisclaimerNotConfirmed() {
        Account account = new Account();
        Assertions.assertThat(account.isDisclaimerConfirmed()).isFalse();
    }
    
    @Test
    public void testDisclaimerNotConfirmed2() {
        Account account = new Account();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        Date defaultDate = calendar.getTime();
        account.setIkAdminDisclaimer(defaultDate);
        Assertions.assertThat(account.isDisclaimerConfirmed()).isFalse();
    }
    
    @Test
    public void testDisclaimerConfirmed() {
        Account account = new Account();
        account.setIkAdminDisclaimer(new Date());
        Assertions.assertThat(account.isDisclaimerConfirmed()).isTrue();
    }
    
    @Test
    public void testDisclaimerConfirmed2() {
        Account account = new Account();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.DAY_OF_YEAR, 2);
        Date defaultDate = calendar.getTime();
        account.setIkAdminDisclaimer(defaultDate);
        Assertions.assertThat(account.isDisclaimerConfirmed()).isTrue();
    }

    @Test
    public void addIkAdminTest() {
        Account account = new Account();
        int ik = 222222222;

        List<Feature> featuresForIkAdmin = new ArrayList<>();

        account.addIkAdmin(ik, "", featuresForIkAdmin);

        Assertions.assertThat(account.getAdminIks()).isNotEmpty().size().isEqualTo(1);
        Assertions.assertThat(account.getAdminIks().get(0).getIk()).isEqualTo(ik);
        Assertions.assertThat(account.getAdminIks().get(0).getMailDomain()).isEqualTo("");
    }

    @Test
    public void addIkAdminWithFeaturesTest() {
        Account account = new Account();
        int ik = 222222222;

        List<Feature> featuresForIkAdmin = new ArrayList<>();
        featuresForIkAdmin.add(Feature.NUB);
        featuresForIkAdmin.add(Feature.CARE);
        featuresForIkAdmin.add(Feature.DROPBOX);

        account.addIkAdmin(ik, "", featuresForIkAdmin);

        IkAdmin ikAdmin = account.getAdminIks().get(0);
        Assertions.assertThat(ikAdmin.getIkAdminFeatures()).isNotEmpty().size().isEqualTo(3);
        Assertions.assertThat(ikAdmin.getIkAdminFeatures().get(0).getFeature()).isIn(featuresForIkAdmin);
        Assertions.assertThat(ikAdmin.getIkAdminFeatures().get(1).getFeature()).isIn(featuresForIkAdmin);
        Assertions.assertThat(ikAdmin.getIkAdminFeatures().get(2).getFeature()).isIn(featuresForIkAdmin);
    }

    @Test
    public void addIkAdminWithExistsAdminTest() {
        Account account = new Account();
        int ik = 222222222;

        List<Feature> featuresForIkAdmin = new ArrayList<>();
        featuresForIkAdmin.add(Feature.NUB);

        account.addIkAdmin(ik, "", featuresForIkAdmin);

        account.addIkAdmin(ik, "", featuresForIkAdmin);

        Assertions.assertThat(account.getAdminIks()).isNotEmpty().size().isEqualTo(1);
    }
    
}
