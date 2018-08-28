package org.inek.dataportal.common.overall;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.cooperation.entities.CooperationRight;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRightFacade;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.CooperativeRight;
import org.inek.dataportal.common.enums.Right;
import static org.inek.dataportal.common.overall.AccessManager.canReadCompleted;
import static org.inek.dataportal.common.overall.AccessManager.canReadSealed;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;

/**
 *
 * @author muellermi
 */
public class AccessManagerTest {

    private final int allowedIk = 222222222;
    private final int deniedIk = 333333333;
    private final int testIk4 = 444444444;
    private final int testIk5 = 555555555;
    private final int testIk6 = 666666666;

    public AccessManagerTest() {
    }

    public AccessManager obtainAccessManager(List<AccessRight> accessRights) {
        SessionController sessionController = mock(SessionController.class);
        Account account = mock(Account.class);
        when(account.getAccessRights()).thenReturn(accessRights);
        when(sessionController.getAccount()).thenReturn(account);
        AccessManager accessManager = new AccessManager(null, sessionController);
        return accessManager;
    }

    @Test
    public void retrieveAllowedManagedIksReturnsTheOnlyOneAllowedIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Read));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(Feature.NUB);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(allowedIk);
    }

    @Test
    public void retrieveAllowedManagedIksReturnsOnlyOneAllowedIkOutOfMultipleIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Read));
        accessRights.add(new AccessRight(0, deniedIk, Feature.NUB, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(Feature.NUB);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(allowedIk);
    }

    @Test
    public void retrieveAllowedManagedIksReturnsNoIkForListOfDeniedIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, deniedIk, Feature.NUB, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(Feature.NUB);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveAllowedManagedIksReturnsNoIkForEmptyAccessRights() {
        List<AccessRight> accessRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(Feature.NUB);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveAllowedManagedIksReturnsNoIkForWrongFeature() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Read));
        accessRights.add(new AccessRight(0, deniedIk, Feature.NUB, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(Feature.CERT);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveDeniedManagedIksReturnsOnlyOneDeniedIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, deniedIk, Feature.NUB, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveDeniedManagedIks(Feature.NUB);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(deniedIk);
    }

    @Test
    public void retrieveDeniedManagedIksReturnsOnlyOneDeniedIkOutOfMultipleIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Read));
        accessRights.add(new AccessRight(0, deniedIk, Feature.NUB, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveDeniedManagedIks(Feature.NUB);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(deniedIk);
    }

    @Test
    public void retrieveDeniedManagedIksReturnsNoIkForListOfDeniedIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Read));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveDeniedManagedIks(Feature.NUB);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveDeniedManagedIksReturnsNoIkForEmptyAccessRights() {
        List<AccessRight> accessRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveDeniedManagedIks(Feature.NUB);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveDeniedManagedIksReturnsNoIkForWrongFeature() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Read));
        accessRights.add(new AccessRight(0, deniedIk, Feature.NUB, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveDeniedManagedIks(Feature.CERT);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveDeniedForCreationIksReturnsOnlyOneDeniedForCreationIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Read));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveDeniedForCreationIks(Feature.NUB);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(allowedIk);
    }

    @Test
    public void retrieveDeniedForCreationIksReturnsEmptyIfTheOnlyOneIkIsAllowedForCreation() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Create));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveDeniedForCreationIks(Feature.NUB);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveDeniedForCreationIksReturnsOnlyOneDeniedForCreationIkOutOfMultipleIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Create));
        accessRights.add(new AccessRight(0, deniedIk, Feature.NUB, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveDeniedForCreationIks(Feature.NUB);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(deniedIk);
    }

    @Test
    public void retrieveDeniedForCreationIksReturnsNoIkForEmptyAccessRights() {
        List<AccessRight> accessRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveDeniedForCreationIks(Feature.NUB);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveDeniedForCreationIksReturnsNoIkForWrongFeature() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Read));
        accessRights.add(new AccessRight(0, deniedIk, Feature.NUB, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveDeniedForCreationIks(Feature.CERT);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveAllManagedIksReturnsAllIkForFeature() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Read));
        accessRights.add(new AccessRight(0, deniedIk, Feature.NUB, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveDeniedForCreationIks(Feature.NUB);
        assertThat(result).isNotNull().isNotEmpty().contains(allowedIk).contains(deniedIk);
    }

    @Test
    public void testIsAccessAllowed_3args() {
    }

    @Test
    public void testIsAccessAllowed_4args() {
    }

    @Test
    public void testIsReadOnly_3args() {
    }

    @Test
    public void testIsReadOnly_4args() {
    }

    @Test
    public void testIsApprovalRequestEnabled_3args() {
    }

    @Test
    public void testIsApprovalRequestEnabled_4args() {
    }

    @Test
    public void testIsApprovalRequestEnabled_5args() {
    }

    @Test
    public void testIsSealedEnabled_3args() {
    }

    @Test
    public void testIsSealedEnabled_4args() {
    }

    @Test
    public void testIsSealedEnabled_5args() {
    }

    @Test
    public void testIsRequestCorrectionEnabled_3args() {
    }

    @Test
    public void testIsRequestCorrectionEnabled_4args() {
    }

    @Test
    public void testIsDeleteEnabled() {
    }

    @Test
    public void testIsUpdateEnabled_3args() {
    }

    @Test
    public void testIsUpdateEnabled_4args() {
    }

    @Test
    public void testIsTakeEnabled_3args() {
    }

    @Test
    public void testIsTakeEnabled_4args() {
    }

    @Test
    public void testCanReadCompleted_0args() {
    }

    @Test
    public void testCanReadSealed_0args() {
    }

    @Test
    public void testCanWriteAlways() {
    }

    public AccessManager obtainAccessManager(Account userAccount, Feature feature,
            List<CooperationRight> cooperationRights) {
        SessionController sessionController = mock(SessionController.class);
        when(sessionController.getAccount()).thenReturn(userAccount);
        CooperationRightFacade cooperationRightFacade = mock(CooperationRightFacade.class);
        when(cooperationRightFacade.getCooperationRights(feature, userAccount)).thenReturn(cooperationRights);
        AccessManager accessManager = new AccessManager(cooperationRightFacade, sessionController);
        return accessManager;
    }

    @Test
    public void determineAccountIdsForNullAccountReturnsEmptySet() {

        SessionController sessionController = mock(SessionController.class);
        when(sessionController.getAccount()).thenReturn(null);
        AccessManager accessManager = new AccessManager(null, sessionController);
        Set<Integer> result = accessManager.determineAccountIds(Feature.NUB, canReadSealed());
        assertThat(result).isNotNull().isEmpty();

    }

    @Test
    public void determineAccountIdsForAccountNotNullReturnsSetOfPartnerIdsWithAtLeastReadCompleated() {
        Feature feature = Feature.NUB;

        Account partnerAccount = mock(Account.class);
        when(partnerAccount.getId()).thenReturn(2);

        Account partner2Account = mock(Account.class);
        when(partner2Account.getId()).thenReturn(3);

        Account userAccount = mock(Account.class);
        when(userAccount.getId()).thenReturn(1);

        List<CooperationRight> cooperationRights = new ArrayList<>();
        cooperationRights.add(
                new CooperationRight(partnerAccount.getId(), userAccount.getId(), testIk4, feature, CooperativeRight.ReadWriteSeal));
        cooperationRights.add(
                new CooperationRight(partner2Account.getId(), userAccount.getId(), testIk5, feature, CooperativeRight.ReadSealed));

        AccessManager accessManager = obtainAccessManager(userAccount, feature, cooperationRights);

        Set<Integer> result = accessManager.determineAccountIds(feature, canReadCompleted());
        assertThat(result).isNotNull().isNotEmpty().containsOnly(1, 2);

    }

    @Test
    public void determineAccountIdsForAccountNotNullReturnsSetOfPartnerIdsWithAtLeastReadSealed() {
        Feature feature = Feature.NUB;

        Account userAccount = mock(Account.class);
        when(userAccount.getId()).thenReturn(1);

        Account partnerAccount = mock(Account.class);
        when(partnerAccount.getId()).thenReturn(2);

        Account partner2Account = mock(Account.class);
        when(partner2Account.getId()).thenReturn(3);

        List<CooperationRight> cooperationRights = new ArrayList<>();
        cooperationRights.add(
                new CooperationRight(partnerAccount.getId(), userAccount.getId(), testIk4, feature, CooperativeRight.ReadWriteSeal));
        cooperationRights.add(
                new CooperationRight(partner2Account.getId(), userAccount.getId(), testIk5, feature, CooperativeRight.ReadSealed));


        AccessManager accessManager = obtainAccessManager(userAccount, feature, cooperationRights);

        Set<Integer> result = accessManager.determineAccountIds(feature, canReadSealed());
        assertThat(result).isNotNull().isNotEmpty().containsOnly(1, 2, 3);

    }

    @Test
    public void determineAccountIdsForAccountWitoutPartnersReturnsOnlyOwnAccount() {
        Feature feature = Feature.NUB;

        Account userAccount = mock(Account.class);
        when(userAccount.getId()).thenReturn(1);

        List<CooperationRight> cooperationRights = new ArrayList<>();

        AccessManager accessManager = obtainAccessManager(userAccount, feature, cooperationRights);

        Set<Integer> result = accessManager.determineAccountIds(feature, canReadSealed());
        assertThat(result).isNotNull().isNotEmpty().containsOnly(1);

    }

    @Test
    public void testCanReadSealed_Feature_int() {
    }

    @Test
    public void testCanReadSealed_3args() {
    }

    @Test
    public void testCanReadCompleted_Feature_int() {
    }

    @Test
    public void testCanReadCompleted_3args() {
    }

    @Test
    public void testCanReadAlways_Feature_int() {
    }

    @Test
    public void testCanReadAlways_3args() {
    }

    @Test
    public void testIsReadAllowed() {
    }

    @Test
    public void testIsEditAllowed() {
    }

    @Test
    public void testIsCreateAllowed_3args() {
    }

    @Test
    public void testIsSendAllowed() {
    }

    @Test
    public void testObtainIksForCreation() {
    }

    @Test
    public void testIsCreateAllowed_Feature() {
    }

    @Test
    public void testObtainAllowedIks() {
    }

}
