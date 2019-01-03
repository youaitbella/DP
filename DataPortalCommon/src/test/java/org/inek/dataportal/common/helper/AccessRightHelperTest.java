package org.inek.dataportal.common.helper;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.ikadmin.entity.IkAdmin;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class AccessRightHelperTest {

    @Test
    public void ensureRightsForAccountFeatureTest() {
        Account acc = new Account();
        acc.addFeature(Feature.NUB, true);
        acc.addFeature(Feature.CALCULATION_HOSPITAL, true);
        int ik = 222222222;
        List<Feature> features = new ArrayList<>();
        features.add(Feature.NUB);
        features.add(Feature.CALCULATION_HOSPITAL);

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();
        ikAdminsForIk.add(createNewIkAdmin(new Account(), ik, features));

        Set<Integer> ikAdminsForEmailing = new HashSet<>();

        AccessRightHelper.ensureRightsForAccountFeature(acc, ikAdminsForIk, ikAdminsForEmailing, ik);

        Assertions.assertThat(acc.getAccessRights()).hasSize(2);
    }

    @Test
    public void ensureRightsForNonAccountFeatureTest() {

    }

    @Test
    public IkAdmin createNewIkAdmin(Account account, int ik, List<Feature> features) {
        return new IkAdmin(account.getId(), ik, "", features);
    }


}