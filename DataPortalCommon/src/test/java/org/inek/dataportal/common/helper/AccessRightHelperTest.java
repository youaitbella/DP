package org.inek.dataportal.common.helper;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.data.ikadmin.entity.IkAdmin;
import org.inek.dataportal.common.enums.Right;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class AccessRightHelperTest {

    @Test
    public void ensureRightsForAccountFeatureWithNonExistsRightsTest() {
        Account acc = createNewAccount(2);
        acc.addFeature(Feature.NUB, true);
        acc.addFeature(Feature.CALCULATION_HOSPITAL, true);
        int ik = 222222222;
        List<Feature> features1 = new ArrayList<>();
        features1.add(Feature.NUB);

        List<Feature> features2 = new ArrayList<>();
        features2.add(Feature.CALCULATION_HOSPITAL);

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();
        ikAdminsForIk.add(createNewIkAdmin(createNewAccount(1), ik, features1));
        ikAdminsForIk.add(createNewIkAdmin(createNewAccount(3), ik, features2));

        Set<Integer> ikAdminsForEmailing = new HashSet<>();

        AccessRightHelper.ensureRightsForAccountFeature(acc, ikAdminsForIk, ikAdminsForEmailing, ik);

        Assertions.assertThat(acc.getAccessRights()).hasSize(2);
        Assertions.assertThat(acc.getAccessRights().stream().filter(c -> c.getFeature() == Feature.NUB).findFirst()
                .get().getRight()).isEqualTo(Right.Deny);
        Assertions.assertThat(acc.getAccessRights().stream().filter(c -> c.getFeature() == Feature.CALCULATION_HOSPITAL).findFirst()
                .get().getRight()).isEqualTo(Right.Deny);
        Assertions.assertThat(ikAdminsForEmailing).containsSequence(1,3);
    }

    @Test
    public void ensureRightsForAccountFeatureWithExistsRightsTest() {
        Account acc = createNewAccount(2);
        acc.addFeature(Feature.NUB, true);
        int ik = 222222222;
        AccessRight ar = new AccessRight(acc.getId(), ik, Feature.NUB, Right.Read);
        acc.addAccessRigth(ar);

        List<Feature> features1 = new ArrayList<>();
        features1.add(Feature.NUB);

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();
        ikAdminsForIk.add(createNewIkAdmin(createNewAccount(1), ik, features1));

        Set<Integer> ikAdminsForEmailing = new HashSet<>();

        AccessRightHelper.ensureRightsForAccountFeature(acc, ikAdminsForIk, ikAdminsForEmailing, ik);

        Assertions.assertThat(acc.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc.getAccessRights().stream().filter(c -> c.getFeature() == Feature.NUB).findFirst()
                .get().getRight()).isEqualTo(Right.Read);
        Assertions.assertThat(ikAdminsForEmailing).isEmpty();
    }

    @Test
    public void ensureRightsForAccountFeatureWithNonExistsRightsAndNoIkAdminTest() {
        Account acc = createNewAccount(2);
        acc.addFeature(Feature.NUB, true);
        acc.addFeature(Feature.CALCULATION_HOSPITAL, true);
        int ik = 222222222;

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();

        Set<Integer> ikAdminsForEmailing = new HashSet<>();

        AccessRightHelper.ensureRightsForAccountFeature(acc, ikAdminsForIk, ikAdminsForEmailing, ik);

        Assertions.assertThat(acc.getAccessRights()).hasSize(0);
        Assertions.assertThat(ikAdminsForEmailing).isEmpty();
    }



    @Test
    public void ensureRightsForNonAccountFeatureWithFeatuerWithIkAdminTest() {
        Account acc = createNewAccount(2);
        acc.addFeature(Feature.NUB, true);
        int ik = 222222222;
        List<Feature> features1 = new ArrayList<>();
        features1.add(Feature.CARE);

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();
        ikAdminsForIk.add(createNewIkAdmin(createNewAccount(1), ik, features1));

        Set<Integer> ikAdminsForEmailing = new HashSet<>();

        AccessRightHelper.ensureFeatureAndRightsForIkAdminOnly(acc, ikAdminsForIk, ikAdminsForEmailing, ik);

        Assertions.assertThat(acc.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc.getAccessRights().stream().filter(c -> c.getFeature() == Feature.CARE).findFirst()
                .get().getRight()).isEqualTo(Right.Deny);
        Assertions.assertThat(acc.getFeatures().stream().anyMatch(c -> c.getFeature() == Feature.CARE)).isTrue();
        Assertions.assertThat(ikAdminsForEmailing).contains(1);
    }

    @Test
    public void ensureRightsForNonAccountFeatureWithFeatuerWithNoIkAdminTest() {
        Account acc = createNewAccount(2);
        acc.addFeature(Feature.NUB, true);
        int ik = 222222222;

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();

        Set<Integer> ikAdminsForEmailing = new HashSet<>();

        AccessRightHelper.ensureFeatureAndRightsForIkAdminOnly(acc, ikAdminsForIk, ikAdminsForEmailing, ik);

        Assertions.assertThat(acc.getAccessRights()).hasSize(0);
        Assertions.assertThat(acc.getFeatures().stream().noneMatch(c -> c.getFeature() == Feature.CARE)).isTrue();
        Assertions.assertThat(ikAdminsForEmailing).isEmpty();
    }

    @Test
    public void ensureRightsForNonAccountFeatureWithExistsRightsTest() {
        Account acc = createNewAccount(2);
        acc.addFeature(Feature.CARE, true);
        int ik = 222222222;
        AccessRight ar = new AccessRight(acc.getId(), ik, Feature.CARE, Right.Read);
        acc.addAccessRigth(ar);

        List<Feature> features1 = new ArrayList<>();
        features1.add(Feature.CARE);

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();
        ikAdminsForIk.add(createNewIkAdmin(createNewAccount(1), ik, features1));

        Set<Integer> ikAdminsForEmailing = new HashSet<>();

        AccessRightHelper.ensureFeatureAndRightsForIkAdminOnly(acc, ikAdminsForIk, ikAdminsForEmailing, ik);

        Assertions.assertThat(acc.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc.getAccessRights().stream().filter(c -> c.getFeature() == Feature.CARE).findFirst()
                .get().getRight()).isEqualTo(Right.Read);
    }

    private IkAdmin createNewIkAdmin(Account account, int ik, List<Feature> features) {
        return new IkAdmin(account.getId(), ik, "", features);
    }

    private Account createNewAccount(int id) {
        Account acc = new Account();
        acc.setId(id);
        return acc;
    }


}