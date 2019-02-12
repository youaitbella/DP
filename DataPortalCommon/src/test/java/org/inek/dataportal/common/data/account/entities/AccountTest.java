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
        Date defaultDate = getDefaultDate(1);
        account.setIkAdminDisclaimer(defaultDate);
        Assertions.assertThat(account.isDisclaimerConfirmed()).isFalse();
    }

    private Date getDefaultDate(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.DAY_OF_YEAR, i);
        return calendar.getTime();
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
        Date defaultDate = getDefaultDate(2);
        account.setIkAdminDisclaimer(defaultDate);
        Assertions.assertThat(account.isDisclaimerConfirmed()).isTrue();
    }

    @Test
    public void addIkAdminTest() {
        Account account = new Account();
        int ik = 222222222;

        List<Feature> featuresForIkAdmin = new ArrayList<>();

        account.updateIkAdmin(ik, "", featuresForIkAdmin);

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

        account.updateIkAdmin(ik, "", featuresForIkAdmin);

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

        account.updateIkAdmin(ik, "", featuresForIkAdmin);

        account.updateIkAdmin(ik, "", featuresForIkAdmin);

        Assertions.assertThat(account.getAdminIks()).isNotEmpty().size().isEqualTo(1);
    }
    
}
