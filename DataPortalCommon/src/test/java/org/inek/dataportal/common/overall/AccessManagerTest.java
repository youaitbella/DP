package org.inek.dataportal.common.overall;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.Right;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;

/**
 *
 * @author muellermi
 */
public class AccessManagerTest {

    public AccessManagerTest() {
    }

    @Test
    public void retrieveAllowedManagedIksReturnsTheOnlyOneAllowedIk() {
        final int allowedIk = 222222222;
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Read));
        SessionController sessionController = obtainSessionController(accessRights);
        AccessManager accessManager = new AccessManager(null, sessionController, null, null);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(Feature.NUB);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(allowedIk);
    }

    @Test
    public void retrieveAllowedManagedIksReturnsOnlyOneAllowedIkOutOfMultipleIk() {
        final int allowedIk = 222222222;
        final int deniedIk = 333333333;
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Read));
        accessRights.add(new AccessRight(0, deniedIk, Feature.NUB, Right.Deny));
        SessionController sessionController = obtainSessionController(accessRights);
        AccessManager accessManager = new AccessManager(null, sessionController, null, null);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(Feature.NUB);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(allowedIk);
    }

    @Test
    public void retrieveAllowedManagedIksReturnsNoIkForListOfDeniedIk() {
        final int deniedIk = 333333333;
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, deniedIk, Feature.NUB, Right.Deny));
        SessionController sessionController = obtainSessionController(accessRights);
        AccessManager accessManager = new AccessManager(null, sessionController, null, null);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(Feature.NUB);
        assertThat(result).isNotNull().isEmpty();
    }
    
    @Test
    public void retrieveAllowedManagedIksReturnsNoIkForEmptyAccessRights() {
        List<AccessRight> accessRights = new ArrayList<>();
        SessionController sessionController = obtainSessionController(accessRights);
        AccessManager accessManager = new AccessManager(null, sessionController, null, null);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(Feature.NUB);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveAllowedManagedIksReturnsNoIkForWrongFeature() {
        final int allowedIk = 222222222;
        final int deniedIk = 333333333;
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(0, allowedIk, Feature.NUB, Right.Read));
        accessRights.add(new AccessRight(0, deniedIk, Feature.NUB, Right.Deny));
        SessionController sessionController = obtainSessionController(accessRights);
        AccessManager accessManager = new AccessManager(null, sessionController, null, null);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(Feature.CERT);
        assertThat(result).isNotNull().isEmpty();
    }

    public SessionController obtainSessionController(List<AccessRight> accessRights) {
        SessionController sessionController = mock(SessionController.class);
        Account account = mock(Account.class);
        when(account.getAccessRights()).thenReturn(accessRights);
        when(sessionController.getAccount()).thenReturn(account);
        return sessionController;
    }

    
    @Test
    public void testRetrieveDeniedManagedIks() {
    }

    @Test
    public void testRetrieveDeniedForCreationIks() {
    }

    @Test
    public void testRetrieveAllManagedIks() {
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
    public void testGetPartnerIks() {
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

    @Test
    public void testDetermineAccountIds() {
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
