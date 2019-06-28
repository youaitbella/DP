package org.inek.dataportal.common.helper;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.data.ikadmin.entity.AccountResponsibility;
import org.inek.dataportal.common.data.ikadmin.entity.IkAdmin;
import org.inek.dataportal.common.enums.Right;
import org.junit.jupiter.api.Test;

import java.util.*;

class AccessRightHelperTest {

    @Test
    public void userCanGetAllRightWithMaxUsers() {
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.HC_HOSPITAL, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.HC_HOSPITAL, Right.All));
        rights.add(new AccessRight(13, 222222222, Feature.HC_HOSPITAL, Right.All));
        rights.add(new AccessRight(14, 222222222, Feature.HC_HOSPITAL, Right.Write));

        Assertions.assertThat(AccessRightHelper.userCanGetAllRight(rights, Feature.HC_HOSPITAL, 222222222)).isFalse();
        Assertions.assertThat(rights).hasSize(4);
    }

    @Test
    public void userCanGetAllRightWithExactUsers() {
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.HC_HOSPITAL, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.HC_HOSPITAL, Right.All));
        rights.add(new AccessRight(13, 222222222, Feature.HC_HOSPITAL, Right.All));

        Assertions.assertThat(AccessRightHelper.userCanGetAllRight(rights, Feature.HC_HOSPITAL, 222222222)).isFalse();
        Assertions.assertThat(rights).hasSize(3);
    }

    @Test
    public void userCanGetAllRightWithFreePlaceUsers() {
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.HC_HOSPITAL, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.HC_HOSPITAL, Right.All));
        rights.add(new AccessRight(13, 222222222, Feature.HC_HOSPITAL, Right.Deny));

        Assertions.assertThat(AccessRightHelper.userCanGetAllRight(rights, Feature.HC_HOSPITAL, 222222222)).isTrue();
        Assertions.assertThat(rights).hasSize(3);
    }

    @Test
    public void accessWriteHasNotToMuchUsersWithToMuchUsersTest() {
        StringBuilder errorMessage = new StringBuilder();
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.HC_HOSPITAL, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.HC_HOSPITAL, Right.All));
        rights.add(new AccessRight(13, 222222222, Feature.HC_HOSPITAL, Right.All));
        rights.add(new AccessRight(14, 222222222, Feature.HC_HOSPITAL, Right.Write));

        Assertions.assertThat(AccessRightHelper.accessWriteHasNotToMuchUsers(rights, errorMessage)).isFalse();
        Assertions.assertThat(errorMessage).isNotEmpty();
    }

    @Test
    public void accessWriteHasNotToMuchUsersWithNotToMuchUsers_2Test() {
        StringBuilder errorMessage = new StringBuilder();
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.CARE, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.CARE, Right.All));
        rights.add(new AccessRight(13, 222222222, Feature.CARE, Right.All));
        rights.add(new AccessRight(14, 222222222, Feature.CARE, Right.Write));

        Assertions.assertThat(AccessRightHelper.accessWriteHasNotToMuchUsers(rights, errorMessage)).isTrue();
        Assertions.assertThat(errorMessage).isEmpty();
    }

    @Test
    public void accessWriteHasNotToMuchUsersWithNotToMuchUsersMultipleIkTest() {
        StringBuilder errorMessage = new StringBuilder();
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.HC_HOSPITAL, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.HC_HOSPITAL, Right.All));
        rights.add(new AccessRight(13, 222222222, Feature.HC_HOSPITAL, Right.All));
        rights.add(new AccessRight(11, 222222223, Feature.HC_HOSPITAL, Right.Write));

        Assertions.assertThat(AccessRightHelper.accessWriteHasNotToMuchUsers(rights, errorMessage)).isTrue();
        Assertions.assertThat(errorMessage).isEmpty();
    }

    @Test
    public void accessWriteHasNotToMuchUsersWithNotToMuchUsersTest() {
        StringBuilder errorMessage = new StringBuilder();
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.HC_HOSPITAL, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.HC_HOSPITAL, Right.All));
        rights.add(new AccessRight(13, 222222222, Feature.HC_HOSPITAL, Right.All));
        rights.add(new AccessRight(14, 222222222, Feature.HC_HOSPITAL, Right.Deny));

        Assertions.assertThat(AccessRightHelper.accessWriteHasNotToMuchUsers(rights, errorMessage)).isTrue();
        Assertions.assertThat(errorMessage).isEmpty();
    }

    @Test
    public void accessWriteHasNotToMuchUsersWithNotToMuchUsersAndResponsibleForIkTest() {
        StringBuilder errorMessage = new StringBuilder();
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.HC_INSURANCE, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.HC_INSURANCE, Right.All));
        rights.add(new AccessRight(13, 222222222, Feature.HC_INSURANCE, Right.All));
        rights.add(new AccessRight(14, 222222222, Feature.HC_INSURANCE, Right.Deny));

        Assertions.assertThat(AccessRightHelper.accessWriteHasNotToMuchUsers(rights, errorMessage)).isTrue();
        Assertions.assertThat(errorMessage).isEmpty();
    }

    @Test
    public void accessWriteHasNotToMuchUsersWithToMuchUsersAndResponsibleForIkTest() {
        StringBuilder errorMessage = new StringBuilder();
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.HC_INSURANCE, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.HC_INSURANCE, Right.All));
        rights.add(new AccessRight(13, 222222222, Feature.HC_INSURANCE, Right.All));
        rights.add(new AccessRight(14, 222222222, Feature.HC_INSURANCE, Right.All));

        Assertions.assertThat(AccessRightHelper.accessWriteHasNotToMuchUsers(rights, errorMessage)).isTrue();
        Assertions.assertThat(errorMessage).isEmpty();
    }

    @Test
    public void accessWriteHasMinOneWithAccesRigthWithOneFeatureOnlyDenyTest() {
        StringBuilder errorMessage = new StringBuilder();
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.CERT, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.DRG_PROPOSAL, Right.Deny));

        Assertions.assertThat(AccessRightHelper.accessWriteHasMinOneWithAccesRigth(rights, errorMessage)).isFalse();
        Assertions.assertThat(errorMessage.toString()).isNotEmpty();
    }

    @Test
    public void accessWriteHasMinOneWithAccesRigthWithNoFeatureOnlyDenyTest() {
        StringBuilder errorMessage = new StringBuilder();
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.CERT, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.DRG_PROPOSAL, Right.Deny));
        rights.add(new AccessRight(13, 222222222, Feature.DRG_PROPOSAL, Right.Write));

        Assertions.assertThat(AccessRightHelper.accessWriteHasMinOneWithAccesRigth(rights, errorMessage)).isTrue();
        Assertions.assertThat(errorMessage).isEmpty();
    }

    @Test
    public void accessWriteHasMinOneWithAccesRigthWithOneFeatureOnlyDenyMultiIkTest() {
        StringBuilder errorMessage = new StringBuilder();
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.CERT, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.DRG_PROPOSAL, Right.Deny));
        rights.add(new AccessRight(13, 222222222, Feature.DRG_PROPOSAL, Right.Write));
        rights.add(new AccessRight(11, 222222223, Feature.CALCULATION_HOSPITAL, Right.Deny));
        rights.add(new AccessRight(12, 222222223, Feature.CALCULATION_HOSPITAL, Right.Deny));
        rights.add(new AccessRight(13, 222222223, Feature.CALCULATION_HOSPITAL, Right.Deny));

        Assertions.assertThat(AccessRightHelper.accessWriteHasMinOneWithAccesRigth(rights, errorMessage)).isFalse();
        Assertions.assertThat(errorMessage).isNotEmpty();
    }

    @Test
    public void accessWriteHasMinOneWithAccesRigthWithOneFeatureOnlyDenyMultiIk_2Test() {
        StringBuilder errorMessage = new StringBuilder();
        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.CERT, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.DRG_PROPOSAL, Right.Deny));
        rights.add(new AccessRight(13, 222222222, Feature.DRG_PROPOSAL, Right.Write));
        rights.add(new AccessRight(11, 222222223, Feature.CALCULATION_HOSPITAL, Right.Deny));
        rights.add(new AccessRight(12, 222222223, Feature.CALCULATION_HOSPITAL, Right.Deny));
        rights.add(new AccessRight(13, 222222223, Feature.CALCULATION_HOSPITAL, Right.Deny));
        rights.add(new AccessRight(14, 222222224, Feature.CALCULATION_HOSPITAL, Right.All));

        Assertions.assertThat(AccessRightHelper.accessWriteHasMinOneWithAccesRigth(rights, errorMessage)).isFalse();
        Assertions.assertThat(errorMessage).isNotEmpty();
    }

    @Test
    public void responsibilitiesHasNotToMuchUsersWithNotToMuchUsersTest() {
        StringBuilder errorMessage = new StringBuilder();

        Map<String, List<AccountResponsibility>> responsibleForIks = new HashMap<>();

        List<AccountResponsibility> accountResponsibilities1 = new ArrayList<>();
        accountResponsibilities1.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        responsibleForIks.put("1", accountResponsibilities1);

        List<AccountResponsibility> accountResponsibilities2 = new ArrayList<>();
        accountResponsibilities2.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        responsibleForIks.put("2", accountResponsibilities2);

        Assertions.assertThat(AccessRightHelper.responsibilitiesHasNotToMuchUsers(responsibleForIks, errorMessage)).isTrue();
        Assertions.assertThat(errorMessage).isEmpty();

        List<AccountResponsibility> accountResponsibilities3 = new ArrayList<>();
        accountResponsibilities3.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        responsibleForIks.put("3", accountResponsibilities3);

        Assertions.assertThat(AccessRightHelper.responsibilitiesHasNotToMuchUsers(responsibleForIks, errorMessage)).isTrue();
        Assertions.assertThat(errorMessage).isEmpty();
    }

    @Test
    public void responsibilitiesHasNotToMuchUsersWithToMuchUsersTest() {
        StringBuilder errorMessage = new StringBuilder();

        Map<String, List<AccountResponsibility>> responsibleForIks = new HashMap<>();

        List<AccountResponsibility> accountResponsibilities1 = new ArrayList<>();
        accountResponsibilities1.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        responsibleForIks.put("1", accountResponsibilities1);

        List<AccountResponsibility> accountResponsibilities2 = new ArrayList<>();
        accountResponsibilities2.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        responsibleForIks.put("2", accountResponsibilities2);

        List<AccountResponsibility> accountResponsibilities3 = new ArrayList<>();
        accountResponsibilities3.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        responsibleForIks.put("3", accountResponsibilities3);

        List<AccountResponsibility> accountResponsibilities4 = new ArrayList<>();
        accountResponsibilities4.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        responsibleForIks.put("4", accountResponsibilities4);

        Assertions.assertThat(AccessRightHelper.responsibilitiesHasNotToMuchUsers(responsibleForIks, errorMessage)).isFalse();
        Assertions.assertThat(errorMessage).isNotEmpty();
    }

    @Test
    public void responsibilitiesHasNotToMuchUsersWithNotToMuchUsersMultipleIkTest() {
        StringBuilder errorMessage = new StringBuilder();

        Map<String, List<AccountResponsibility>> responsibleForIks = new HashMap<>();

        List<AccountResponsibility> accountResponsibilities1 = new ArrayList<>();
        accountResponsibilities1.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        responsibleForIks.put("1", accountResponsibilities1);

        List<AccountResponsibility> accountResponsibilities2 = new ArrayList<>();
        accountResponsibilities2.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        responsibleForIks.put("2", accountResponsibilities2);

        List<AccountResponsibility> accountResponsibilities3 = new ArrayList<>();
        accountResponsibilities3.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        accountResponsibilities3.add(createNewAccountResponsibility(22222223, Feature.HC_INSURANCE));
        responsibleForIks.put("3", accountResponsibilities3);

        Assertions.assertThat(AccessRightHelper.responsibilitiesHasNotToMuchUsers(responsibleForIks, errorMessage)).isTrue();
        Assertions.assertThat(errorMessage).isEmpty();
    }

    @Test
    public void responsibilitiesHasNotToMuchUsersWithToMuchUsersMultipleIkTest() {
        StringBuilder errorMessage = new StringBuilder();

        Map<String, List<AccountResponsibility>> responsibleForIks = new HashMap<>();

        List<AccountResponsibility> accountResponsibilities1 = new ArrayList<>();
        accountResponsibilities1.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        responsibleForIks.put("1", accountResponsibilities1);

        List<AccountResponsibility> accountResponsibilities2 = new ArrayList<>();
        accountResponsibilities2.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        responsibleForIks.put("2", accountResponsibilities2);

        List<AccountResponsibility> accountResponsibilities3 = new ArrayList<>();
        accountResponsibilities3.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        accountResponsibilities3.add(createNewAccountResponsibility(22222223, Feature.HC_INSURANCE));
        responsibleForIks.put("3", accountResponsibilities3);

        List<AccountResponsibility> accountResponsibilities4 = new ArrayList<>();
        accountResponsibilities4.add(createNewAccountResponsibility(22222222, Feature.HC_INSURANCE));
        responsibleForIks.put("4", accountResponsibilities4);

        Assertions.assertThat(AccessRightHelper.responsibilitiesHasNotToMuchUsers(responsibleForIks, errorMessage)).isFalse();
        Assertions.assertThat(errorMessage).isNotEmpty();
    }

    private AccountResponsibility createNewAccountResponsibility(int dataIk, Feature feature) {
        AccountResponsibility responsibility = new AccountResponsibility();
        responsibility.setDataIk(dataIk);
        responsibility.setFeature(feature);
        return responsibility;
    }


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
        Assertions.assertThat(ikAdminsForEmailing).containsSequence(1, 3);
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


    @Test
    public void ensureAccountsWithExistingIkAdminTest() {
        Account acc1 = createNewAccount(2);
        acc1.addFeature(Feature.CARE, true);

        Account acc2 = createNewAccount(3);
        acc2.addFeature(Feature.CARE, true);

        List<Account> accounts1 = new ArrayList<>();
        accounts1.add(acc1);
        accounts1.add(acc2);

        int ik = 222222222;

        AccessRight ar1 = new AccessRight(acc1.getId(), ik, Feature.CARE, Right.Read);
        acc1.addAccessRigth(ar1);
        AccessRight ar2 = new AccessRight(acc2.getId(), ik, Feature.CARE, Right.Read);
        acc2.addAccessRigth(ar2);

        List<Feature> features1 = new ArrayList<>();
        features1.add(Feature.CARE);

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();
        ikAdminsForIk.add(createNewIkAdmin(createNewAccount(1), ik, features1));

        AccessRightHelper.ensureRightsForAccounts(accounts1, ikAdminsForIk, ik);
        Assertions.assertThat(acc1.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc1.getAccessRights().get(0).getRight()).isEqualTo(Right.Read);
        Assertions.assertThat(acc2.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc2.getAccessRights().get(0).getRight()).isEqualTo(Right.Read);

    }

    @Test
    public void ensureAccountsWithoutExistingIkAdminTest() {
        Account acc1 = createNewAccount(2);
        acc1.addFeature(Feature.CARE, true);

        Account acc2 = createNewAccount(3);
        acc2.addFeature(Feature.CARE, true);

        List<Account> accounts1 = new ArrayList<>();
        accounts1.add(acc1);
        accounts1.add(acc2);

        int ik = 222222222;

        AccessRight ar1 = new AccessRight(acc1.getId(), ik, Feature.CARE, Right.Read);
        acc1.addAccessRigth(ar1);
        AccessRight ar2 = new AccessRight(acc2.getId(), ik, Feature.CARE, Right.Read);
        acc2.addAccessRigth(ar2);

        AccessRightHelper.ensureRightsForAccounts(accounts1, new ArrayList<>(), ik);

        Assertions.assertThat(acc1.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc1.getAccessRights().get(0).getRight()).isEqualTo(Right.Deny);
        Assertions.assertThat(acc2.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc2.getAccessRights().get(0).getRight()).isEqualTo(Right.Deny);

    }

    @Test
    public void ensureAccountAccesRightsWithIkAdminForAnotherFeatureTest() {
        Account acc1 = createNewAccount(1);
        acc1.addFeature(Feature.CARE, true);

        Account acc2 = createNewAccount(2);
        acc2.addFeature(Feature.CARE, true);

        List<Account> accounts1 = new ArrayList<>();
        accounts1.add(acc1);
        accounts1.add(acc2);

        int ik = 222222222;

        AccessRight ar1 = new AccessRight(acc1.getId(), ik, Feature.CARE, Right.Read);
        acc1.addAccessRigth(ar1);
        AccessRight ar2 = new AccessRight(acc2.getId(), ik, Feature.CARE, Right.Read);
        acc2.addAccessRigth(ar2);

        List<Feature> features1 = new ArrayList<>();
        features1.add(Feature.CALCULATION_HOSPITAL);

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();
        ikAdminsForIk.add(createNewIkAdmin(createNewAccount(3), ik, features1));

        AccessRightHelper.ensureRightsForAccounts(accounts1, ikAdminsForIk, ik);
        Assertions.assertThat(acc1.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc1.getAccessRights().get(0).getRight()).isEqualTo(Right.Deny);
        Assertions.assertThat(acc2.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc2.getAccessRights().get(0).getRight()).isEqualTo(Right.Deny);
    }


    @Test
    public void ensureAccountsWithExistingIkAdminsTest() {
        Account acc1 = createNewAccount(2);
        acc1.addFeature(Feature.CARE, true);

        Account acc2 = createNewAccount(3);
        acc2.addFeature(Feature.CARE, true);

        List<Account> accounts1 = new ArrayList<>();
        accounts1.add(acc1);
        accounts1.add(acc2);

        int ik = 222222222;

        AccessRight ar1 = new AccessRight(acc1.getId(), ik, Feature.CARE, Right.Read);
        acc1.addAccessRigth(ar1);
        AccessRight ar2 = new AccessRight(acc2.getId(), ik, Feature.CARE, Right.Read);
        acc2.addAccessRigth(ar2);

        List<Feature> features1 = new ArrayList<>();
        features1.add(Feature.CARE);

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();
        ikAdminsForIk.add(createNewIkAdmin(createNewAccount(1), ik, features1));
        ikAdminsForIk.add(createNewIkAdmin(createNewAccount(4), ik, features1));

        AccessRightHelper.ensureRightsForAccounts(accounts1, ikAdminsForIk, ik);
        Assertions.assertThat(acc1.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc1.getAccessRights().get(0).getRight()).isEqualTo(Right.Read);
        Assertions.assertThat(acc2.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc2.getAccessRights().get(0).getRight()).isEqualTo(Right.Read);

    }

    @Test
    public void ensureAccountsWithMissingAccesRigthTest() {
        Account acc1 = createNewAccount(2);
        acc1.addFeature(Feature.CARE, true);

        Account acc2 = createNewAccount(3);
        acc2.addFeature(Feature.CARE, true);

        List<Account> accounts1 = new ArrayList<>();
        accounts1.add(acc1);
        accounts1.add(acc2);

        int ik = 222222222;

        AccessRight ar1 = new AccessRight(acc1.getId(), ik, Feature.CARE, Right.Read);
        acc1.addAccessRigth(ar1);

        List<Feature> features1 = new ArrayList<>();
        features1.add(Feature.CARE);

        AccessRightHelper.ensureRightsForAccounts(accounts1, new ArrayList<>(), ik);
        Assertions.assertThat(acc1.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc1.getAccessRights().get(0).getRight()).isEqualTo(Right.Deny);
        Assertions.assertThat(acc2.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc2.getAccessRights().get(0).getRight()).isEqualTo(Right.Deny);
    }

    @Test
    public void ensureAccountsWithMissingAccesRigth1Test() {
        Account acc1 = createNewAccount(2);
        acc1.addFeature(Feature.CARE, true);

        Account acc2 = createNewAccount(3);
        acc2.addFeature(Feature.CARE, true);

        List<Account> accounts1 = new ArrayList<>();
        accounts1.add(acc1);
        accounts1.add(acc2);

        int ik = 222222222;

        AccessRight ar1 = new AccessRight(acc1.getId(), ik, Feature.CARE, Right.Read);
        acc1.addAccessRigth(ar1);
        AccessRight ar2 = new AccessRight(acc1.getId(), ik, Feature.HC_HOSPITAL, Right.Read);
        acc2.addAccessRigth(ar2);

        List<Feature> features1 = new ArrayList<>();
        features1.add(Feature.CARE);

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();
        ikAdminsForIk.add(createNewIkAdmin(createNewAccount(1), ik, features1));


        AccessRightHelper.ensureRightsForAccounts(accounts1, ikAdminsForIk, ik);

        Assertions.assertThat(acc1.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc1.getAccessRights().get(0).getRight()).isEqualTo(Right.Read);
        Assertions.assertThat(acc2.getAccessRights()).hasSize(2);
        Assertions.assertThat(acc2.getAccessRights().stream().allMatch(c -> c.getRight().equals(Right.Deny))).isTrue();
    }

    @Test
    public void ensureAccountsWithDifferentIks() {
        Account acc1 = createNewAccount(2);
        acc1.addFeature(Feature.CARE, true);

        Account acc2 = createNewAccount(3);
        acc2.addFeature(Feature.CARE, true);

        List<Account> accounts1 = new ArrayList<>();
        accounts1.add(acc1);
        accounts1.add(acc2);

        int ik1 = 222222222;
        int ik2 = 222222223;

        AccessRight ar1 = new AccessRight(acc1.getId(), ik1, Feature.CARE, Right.Read);
        acc1.addAccessRigth(ar1);

        AccessRight ar2 = new AccessRight(acc1.getId(), ik2, Feature.HC_HOSPITAL, Right.Read);
        acc2.addAccessRigth(ar2);

        List<Feature> features1 = new ArrayList<>();
        features1.add(Feature.CARE);

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();
        ikAdminsForIk.add(createNewIkAdmin(createNewAccount(1), ik1, features1));


        AccessRightHelper.ensureRightsForAccounts(accounts1, ikAdminsForIk, ik1);

        Assertions.assertThat(acc1.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc1.getAccessRights().get(0).getRight()).isEqualTo(Right.Read);
        Assertions.assertThat(acc2.getAccessRights()).hasSize(2);
        Assertions.assertThat(acc2.getAccessRights().stream().anyMatch(ar -> ar.getFeature().equals(Feature.HC_HOSPITAL) && ar.getRight().equals(Right.Read) && ar.getIk() == ik2)).isTrue();

        Assertions.assertThat(acc2.getAccessRights().stream().anyMatch(ar -> ar.getFeature().equals(Feature.CARE) && ar.getRight().equals(Right.Deny) && ar.getIk() == ik1)).isTrue();
    }

    @Test
    public void ensureAccountsWithDifferentIksAndWihtoutIkAdmin() {
        Account acc1 = createNewAccount(2);
        acc1.addFeature(Feature.CARE, true);

        Account acc2 = createNewAccount(3);
        acc2.addFeature(Feature.CARE, true);
        acc2.addFeature(Feature.HC_HOSPITAL, true);

        List<Account> accounts1 = new ArrayList<>();
        accounts1.add(acc1);
        accounts1.add(acc2);

        int ik1 = 222222222;
        int ik2 = 222222223;

        AccessRight ar1 = new AccessRight(acc1.getId(), ik1, Feature.CARE, Right.Read);
        acc1.addAccessRigth(ar1);

        AccessRight ar2 = new AccessRight(acc1.getId(), ik2, Feature.CARE, Right.Read);
        acc2.addAccessRigth(ar2);

        List<IkAdmin> ikAdminsForIk = new ArrayList<>();


        AccessRightHelper.ensureRightsForAccounts(accounts1, ikAdminsForIk, ik1);

        Assertions.assertThat(acc1.getAccessRights()).hasSize(1);
        Assertions.assertThat(acc1.getAccessRights().get(0).getRight()).isEqualTo(Right.Deny);
        Assertions.assertThat(acc2.getAccessRights()).hasSize(3);
        Assertions.assertThat(acc2.getAccessRights().stream().anyMatch(ar -> ar.getFeature().equals(Feature.CARE) && ar.getRight().equals(Right.Read) && ar.getIk() == ik2)).isTrue();
        Assertions.assertThat(acc2.getAccessRights().stream().anyMatch(ar -> ar.getFeature().equals(Feature.CARE) && ar.getRight().equals(Right.Deny) && ar.getIk() == ik1)).isTrue();
        Assertions.assertThat(acc2.getAccessRights().stream().anyMatch(ar -> ar.getFeature().equals(Feature.HC_HOSPITAL) && ar.getRight().equals(Right.Deny) && ar.getIk() == ik1)).isTrue();
    }

}


