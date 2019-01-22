package org.inek.dataportal.common.overall;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.cooperation.entities.CooperationRight;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRightFacade;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.CooperativeRight;
import org.inek.dataportal.common.enums.Right;
import org.inek.dataportal.common.enums.WorkflowStatus;
import static org.inek.dataportal.common.overall.AccessManager.canReadCompleted;
import static org.inek.dataportal.common.overall.AccessManager.canReadSealed;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;

/**
 *
 * @author muellermi
 */
public class CI_AccessManagerTest {

    //<editor-fold defaultstate="collapsed" desc="Prepare test data">
    private final int unmanagedIk1 = 111111111;
    private final int unmanagedIk2 = 222222222;
    private final int unmanagedIk3 = 333333333;
    private final int allowedManagedIk = 444444444;
    private final int deniedManagedIk = 555555555;

    private final int userAccountId = 4711;
    private final int noneAccountId = 0;
    private final int readWriteSealAccountId = 1;
    private final int readSealedAccountId = 2;
    private final Feature testFeature = Feature.ADDITIONAL_COST;

    public CI_AccessManagerTest() {
    }

    private AccessManager obtainAccessManager() {
        return obtainAccessManager(new ArrayList<>(), false);
    }

    private AccessManager obtainAccessManager(List<AccessRight> accessRights) {
        return obtainAccessManager(accessRights, false);
    }

    private AccessManager obtainAccessManager(List<AccessRight> accessRights, boolean isInekUser) {
        return obtainAccessManager(accessRights, isInekUser, obtainFullIkSet());
    }

    private AccessManager obtainAccessManager(List<AccessRight> accessRights, boolean isInekUser, Set<Integer> fullIkSet) {
        List<CooperationRight> cooperationRights = new ArrayList<>();
        cooperationRights.add(
                new CooperationRight(noneAccountId, userAccountId, unmanagedIk1, testFeature, CooperativeRight.None));
        cooperationRights.add(
                new CooperationRight(readWriteSealAccountId, userAccountId, unmanagedIk1, testFeature, CooperativeRight.ReadWriteSeal));
        cooperationRights.add(
                new CooperationRight(readSealedAccountId, userAccountId, unmanagedIk2, testFeature, CooperativeRight.ReadOnly));
        cooperationRights.add(
                new CooperationRight(readSealedAccountId, userAccountId, -1, Feature.DRG_PROPOSAL, CooperativeRight.ReadOnly));

        return obtainAccessManager(accessRights, cooperationRights, isInekUser, fullIkSet);
    }

    private AccessManager obtainAccessManager(List<AccessRight> accessRights, List<CooperationRight> cooperationRights,
            boolean isInekUser) {
        return obtainAccessManager(accessRights, cooperationRights, isInekUser, obtainFullIkSet());
    }

    private AccessManager obtainAccessManager(List<AccessRight> accessRights, List<CooperationRight> cooperationRights,
            boolean isInekUser, Set<Integer> fullIkSet) {

        Set<Integer> accountIks = new HashSet<>();
        accountIks.add(allowedManagedIk);
        Set<Integer> responibleForIks = new HashSet<>();
        responibleForIks.add(unmanagedIk1);
        responibleForIks.add(unmanagedIk2);

        Account userAccount = mock(Account.class);
        when(userAccount.getId()).thenReturn(userAccountId);
        when(userAccount.getAccessRights()).thenReturn(accessRights);
        when(userAccount.getFullIkSet()).thenReturn(fullIkSet);
        when(userAccount.obtainResponsibleForIks(Feature.HC_INSURANCE, accountIks)).thenReturn(responibleForIks);

        SessionController sessionController = mock(SessionController.class);
        when(sessionController.isInekUser(testFeature)).thenReturn(isInekUser);
        when(sessionController.getAccount()).thenReturn(userAccount);
        when(sessionController.getAccountId()).thenReturn(userAccountId);

        CooperationRightFacade cooperationRightFacade = mock(CooperationRightFacade.class);
        when(cooperationRightFacade.getCooperationRights(testFeature, userAccount))
                .thenReturn(cooperationRights.stream().filter(r -> r.getFeature() == testFeature).collect(Collectors.
                        toList()));
        when(cooperationRightFacade.getCooperationRights(Feature.DRG_PROPOSAL, userAccount))
                .thenReturn(cooperationRights.stream().filter(r -> r.getFeature() == Feature.DRG_PROPOSAL).
                        collect(Collectors.toList()));

        ManagedIkCache ikCache = mock(ManagedIkCache.class);
        when(ikCache.isManaged(allowedManagedIk, testFeature)).thenReturn(true);
        when(ikCache.isManaged(allowedManagedIk, Feature.HC_INSURANCE)).thenReturn(true);
        when(ikCache.isManaged(deniedManagedIk, testFeature)).thenReturn(true);
        when(ikCache.isManaged(unmanagedIk1, testFeature)).thenReturn(false);
        when(ikCache.isManaged(unmanagedIk2, testFeature)).thenReturn(false);
        when(ikCache.isManaged(unmanagedIk3, testFeature)).thenReturn(false);

        Set<Integer> correlatedIks = new HashSet<>();
        correlatedIks.add(unmanagedIk1);

        when(ikCache.retrieveCorrelatedIks(Feature.HC_INSURANCE, accountIks, responibleForIks)).thenReturn(correlatedIks);
        AccessManager accessManager = new AccessManager(cooperationRightFacade, sessionController, ikCache);
        return accessManager;
    }

    private Set<Integer> obtainFullIkSet() {
        Set<Integer> fullIkSet = new HashSet<>();
        fullIkSet.add(unmanagedIk1);
        fullIkSet.add(unmanagedIk2);
        fullIkSet.add(unmanagedIk2);
        fullIkSet.add(allowedManagedIk);
        fullIkSet.add(deniedManagedIk);
        return fullIkSet;
    }
    //</editor-fold>

    @Test
    public void retrieveAllowedManagedIksReturnsTheOnlyOneAllowedIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, testFeature, Right.Read));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(testFeature);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(allowedManagedIk);
    }

    @Test
    public void retrieveAllowedManagedIksReturnsOnlyOneAllowedIkOutOfMultipleIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, testFeature, Right.Read));
        accessRights.add(new AccessRight(userAccountId, deniedManagedIk, testFeature, Right.Deny));
        accessRights.add(new AccessRight(userAccountId, unmanagedIk1, testFeature, Right.All));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(testFeature);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(allowedManagedIk);
    }

    @Test
    public void retrieveAllowedManagedIksReturnsNoIkForListOfDeniedIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, deniedManagedIk, testFeature, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(testFeature);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveAllowedManagedIksReturnsNoIkForEmptyAccessRights() {
        List<AccessRight> accessRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(testFeature);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveAllowedManagedIksReturnsNoIkForWrongFeature() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, testFeature, Right.Read));
        accessRights.add(new AccessRight(userAccountId, deniedManagedIk, testFeature, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(Feature.CERT);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void retrieveAllowedManagedIksReturnsResponsibleForIks() {
        // todo
    }

    @Test
    public void retrieveAllManagedIksReturnsAllIkForFeature() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, testFeature, Right.Read));
        accessRights.add(new AccessRight(userAccountId, deniedManagedIk, testFeature, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllManagedIks(testFeature);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(allowedManagedIk, deniedManagedIk);
    }

    @Test
    public void isAccessAllowedForInekUserReturnsTrue() {

        List<AccessRight> accessRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights, true);
        boolean result = accessManager.isAccessAllowed(testFeature, WorkflowStatus.Accepted, -1, -1);
        assertThat(result).isTrue();

    }

    @Test
    public void isAccessAllowedForOwnIdWithoutIkReturnsTrue() {

        List<AccessRight> accessRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights);
        boolean result = accessManager.isAccessAllowed(testFeature, WorkflowStatus.Accepted, userAccountId, -1);
        assertThat(result).isTrue();
    }

    @Test
    public void isAccessAllowedForReadSealedAccountIdWithoutIkReturnsFalse() {

        List<AccessRight> accessRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights);
        boolean result = accessManager.
                isAccessAllowed(Feature.DRG_PROPOSAL, WorkflowStatus.Accepted, readSealedAccountId, -1);
        assertThat(result).isTrue();
    }

    @Test
    public void isAccessAllowedForNoneAccountIdWithoutIkReturnsFalse() {
        List<AccessRight> accessRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights);
        boolean result = accessManager.isAccessAllowed(testFeature, WorkflowStatus.Accepted, noneAccountId, -1);
        assertThat(result).isFalse();
    }

    @Test
    public void isAccessAllowedForOwnAccountIdWithDeniedIkReturnsFalse() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, deniedManagedIk, testFeature, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        boolean result = accessManager.isAccessAllowed(testFeature, WorkflowStatus.Accepted, userAccountId, deniedManagedIk);
        assertThat(result).isFalse();
    }

    @Test
    public void isAccessAllowedForOwnAccountIdWithAllowedIkReturnsTrue() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, testFeature, Right.Write));
        AccessManager accessManager = obtainAccessManager(accessRights);
        boolean result = accessManager.isAccessAllowed(testFeature, WorkflowStatus.Accepted, userAccountId, allowedManagedIk);
        assertThat(result).isTrue();
    }

    @Test
    public void testIsReadOnly() {
    }

    @Test
    public void isApprovalRequestEnabledWithWorkflowstatusHigherThanCorrectionRequestedReturnsFalse() {
        AccessManager accessManager = obtainAccessManager();
        boolean result = accessManager.isApprovalRequestEnabled(
                testFeature, WorkflowStatus.ApprovalRequested, userAccountId, allowedManagedIk);
        assertThat(result).isFalse();
    }

    @Test
    public void isApprovalRequestEnabledForWorkflowstatusCorrectionRequestedAndHasUpdateButtonReturnsFalse() {
        AccessManager accessManager = obtainAccessManager();
        boolean result = accessManager.isApprovalRequestEnabled(
                testFeature, WorkflowStatus.CorrectionRequested, userAccountId, allowedManagedIk, true);
        assertThat(result).isFalse();
    }

    @Test
    public void isApprovalRequestEnabledForAccessrightReadOnly() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, testFeature, Right.Read));
        AccessManager accessManager = obtainAccessManager(accessRights);
        boolean result = accessManager.isApprovalRequestEnabled(
                testFeature, WorkflowStatus.CorrectionRequested, userAccountId, allowedManagedIk, false);
        assertThat(result).isFalse();
    }

    @Test
    public void isApprovalRequestEnabledForOwnerEqualsUserAndCanCooperativeWriteButNotSealReturnTrue() {
        List<CooperationRight> cooperationRights = new ArrayList<>();
        cooperationRights.add(new CooperationRight(
                readSealedAccountId, userAccountId, unmanagedIk1, testFeature, CooperativeRight.ReadWrite));
        List<AccessRight> accessRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
        boolean result = accessManager.isApprovalRequestEnabled(
                testFeature, WorkflowStatus.CorrectionRequested, readSealedAccountId, unmanagedIk1, false);
        assertThat(result).isTrue();
    }

// todo: remove or adopt to new rights    
//    @Test
//    public void isApprovalRequestEnabledForOwnerUnqualsUserAndDoesntNeedsApprovalReturnFalse() {
//        List<CooperationRight> cooperationRights = new ArrayList<>();
//        List<AccessRight> accessRights = new ArrayList<>();
//        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
//        boolean result = accessManager.isApprovalRequestEnabled(
//                testFeature, WorkflowStatus.CorrectionRequested, userAccountId, allowedManagedIk, false);
//        assertThat(result).isFalse();
//    }
//
//    @Test
//    public void isApprovalRequestEnabledForOwnerUnqualsUserAndNeedsApprovalReturnTrue() {
//        List<CooperationRight> cooperationRights = new ArrayList<>();
//        cooperationRights.add(new CooperationRight(
//                userAccountId, readSealedAccountId, unmanagedIk1, testFeature, CooperativeRight.ReadWriteTakeSeal));
//        List<AccessRight> accessRights = new ArrayList<>();
//        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
//        boolean result = accessManager.isApprovalRequestEnabled(
//                testFeature, WorkflowStatus.CorrectionRequested, userAccountId, unmanagedIk1, false);
//        assertThat(result).isFalse();
//    }

    @Test
    public void isSealedEnabledForUpdateButtonTrueAndWorkflowStatusCorrectionRequestedReturnFalse() {
        AccessManager accessManager = obtainAccessManager();
        boolean result = accessManager.isSealedEnabled(
                testFeature, WorkflowStatus.CorrectionRequested, userAccountId, allowedManagedIk, true);
        assertThat(result).isFalse();
    }

    @Test
    public void isSealedEnabledForWorkflowStatusEqualsOrHigherThanProvidedReturnFalse() {
        AccessManager accessManager = obtainAccessManager();
        boolean result = accessManager.isSealedEnabled(
                testFeature, WorkflowStatus.Provided, userAccountId, allowedManagedIk, true);
        assertThat(result).isFalse();
    }

    @Test
    public void isSealedEnabledForExistingIkAndUserHasAccessRightReturnTrue() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, testFeature, Right.Seal));
        AccessManager accessManager = obtainAccessManager(accessRights);
        boolean result = accessManager.isSealedEnabled(
                testFeature, WorkflowStatus.New, userAccountId, allowedManagedIk, false);
        assertThat(result).isTrue();
    }

    @Test
    public void isSealedEnabledForUserHasCooperativeRightReadWriteTakeSealReturnTrue() {
        List<AccessRight> accessRights = new ArrayList<>();
        List<CooperationRight> cooperationRights = new ArrayList<>();
        cooperationRights.add(new CooperationRight(readSealedAccountId, userAccountId, allowedManagedIk, testFeature, CooperativeRight.ReadWriteTakeSeal));
        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
        boolean result = accessManager.isSealedEnabled(
                testFeature, WorkflowStatus.New, readSealedAccountId, allowedManagedIk);
        assertThat(result).isTrue();
    }

    @Test
    public void testIsDeleteEnabledWithoutIkReturnsTrueIfManagerIsOptional() {
        AccessManager accessManager = obtainAccessManager();
        boolean result = accessManager.isDeleteEnabled(testFeature, userAccountId, 0);
        assertThat(result).isTrue();
    }

    @Test
    public void testIsDeleteEnabledWithoutIkReturnsFalseIfForeignData() {
        AccessManager accessManager = obtainAccessManager();
        boolean result = accessManager.isDeleteEnabled(testFeature, noneAccountId, 0);
        assertThat(result).isFalse();
    }

    @Test
    public void testIsDeleteEnabledWithoutIkReturnsFalseIfManagerIsMandatory() {
        AccessManager accessManager = obtainAccessManager();
        boolean result = accessManager.isDeleteEnabled(Feature.HC_HOSPITAL, userAccountId, 0);
        assertThat(result).isFalse();
    }

    @Test
    public void testIsDeleteEnabledWithCanWrite() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, testFeature, Right.Create));
        List<CooperationRight> cooperationRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
        boolean result = accessManager.isDeleteEnabled(testFeature, userAccountId, allowedManagedIk);
        assertThat(result).isTrue();
    }

    @Test
    public void isDeleteEnabledWithCanSealOnlyReturnsFalse() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, testFeature, Right.Seal));
        List<CooperationRight> cooperationRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
        boolean result = accessManager.isDeleteEnabled(testFeature, userAccountId, allowedManagedIk);
        assertThat(result).isFalse();
    }

    @Test
    public void IsDeleteEnabledWithoutCanSealNorWrite() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, testFeature, Right.Deny));
        List<CooperationRight> cooperationRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
        boolean result = accessManager.isDeleteEnabled(testFeature, userAccountId, allowedManagedIk);
        assertThat(result).isFalse();
    }

    @Test
    public void isUpdateEnabledWithoutWorkflowStatusCorrectionRequestedReturnsFalse() {
        AccessManager accessManager = obtainAccessManager();
        boolean result = accessManager.isUpdateEnabled(testFeature, WorkflowStatus.Accepted, userAccountId, allowedManagedIk);
        assertThat(result).isFalse();
    }

    @Test
    public void isUpdateEnabledCanNotSealNorWriteCompletedReturnsFalse() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, deniedManagedIk, testFeature, Right.Read));
        List<CooperationRight> cooperationRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
        boolean result = accessManager.isUpdateEnabled(testFeature, WorkflowStatus.CorrectionRequested, userAccountId, deniedManagedIk);
        assertThat(result).isFalse();
    }

    @Test
    public void isUpdateEnabledCanSealReturnsTrue() {

        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, testFeature, Right.Seal));
        List<CooperationRight> cooperationRights = new ArrayList<>();
        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
        boolean result = accessManager.isUpdateEnabled(testFeature, WorkflowStatus.CorrectionRequested, userAccountId, allowedManagedIk);
        assertThat(result).isTrue();
    }

    @Test
    public void isUpdateEnabledCanWriteReturnsTrue() {
        List<AccessRight> accessRights = new ArrayList<>();
        List<CooperationRight> cooperationRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, testFeature, Right.Write));
        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
        boolean result = accessManager.isUpdateEnabled(testFeature, WorkflowStatus.CorrectionRequested, userAccountId, allowedManagedIk);
        assertThat(result).isTrue();
    }

    @Test
    public void isUpdateEnabledWithNoIkAndUserIsOwnerReturnsTrue() {
        AccessManager accessManager = obtainAccessManager();
        boolean result = accessManager.isUpdateEnabled(testFeature, WorkflowStatus.CorrectionRequested, userAccountId, allowedManagedIk);
        assertThat(result).isTrue();
    }

    @Test
    public void isUpdateEnabledWithNoIkAndUserIsNotOwnerAndHasNoneCoopRightFalse() {
        List<AccessRight> accessRights = new ArrayList<>();
        List<CooperationRight> cooperationRights = new ArrayList<>();
        cooperationRights.add(
                new CooperationRight(readSealedAccountId, userAccountId, allowedManagedIk, testFeature, CooperativeRight.None));
        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
        boolean result = accessManager.isUpdateEnabled(testFeature, WorkflowStatus.CorrectionRequested, readSealedAccountId, allowedManagedIk);
        assertThat(result).isFalse();
    }

    @Test
    public void isUpdateEnabledWithNoIkAndUserIsNotOwnerAndCanSealReturnsTrue() {
        List<AccessRight> accessRights = new ArrayList<>();
        List<CooperationRight> cooperationRights = new ArrayList<>();
        cooperationRights.add(
                new CooperationRight(readSealedAccountId, userAccountId, allowedManagedIk, testFeature, CooperativeRight.ReadWriteSeal));
        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
        boolean result = accessManager.isUpdateEnabled(testFeature, WorkflowStatus.CorrectionRequested, readSealedAccountId, allowedManagedIk);
        assertThat(result).isTrue();
    }

    @Test
    public void isUpdateEnabledWithNoIkAndUserIsNotOwnerAndWriteCompletedReturnsTrue() {
        List<AccessRight> accessRights = new ArrayList<>();
        List<CooperationRight> cooperationRights = new ArrayList<>();
        cooperationRights.add(
                new CooperationRight(readSealedAccountId, userAccountId, allowedManagedIk, testFeature, CooperativeRight.ReadWrite));
        AccessManager accessManager = obtainAccessManager(accessRights, cooperationRights, false);
        boolean result = accessManager.isUpdateEnabled(testFeature, WorkflowStatus.CorrectionRequested, readSealedAccountId, allowedManagedIk);
        assertThat(result).isTrue();
    }

    @Test
    public void isTakeEnabledWithOwnerEqualsUserReturnsFalse() {

        AccessManager accessManager = obtainAccessManager();
        boolean result = accessManager.isTakeEnabled(testFeature, WorkflowStatus.New, userAccountId, allowedManagedIk);
        assertThat(result).isFalse();

    }

    @Test
    public void isTakeEnabledForUserHasCooperativeRightReadWriteTakeReturnsTrue() {

        List<CooperationRight> cooperationRights = new ArrayList<>();
        cooperationRights.add(
                new CooperationRight(readSealedAccountId, userAccountId, allowedManagedIk, testFeature, CooperativeRight.ReadWriteTakeSeal));
        AccessManager accessManager = obtainAccessManager(null, cooperationRights, false);
        boolean result = accessManager.isTakeEnabled(testFeature, WorkflowStatus.New, readSealedAccountId, allowedManagedIk);
        assertThat(result).isTrue();

    }

    @Test
    public void isTakeEnabledForUserHasNoCooperativeRightsReturnsFalse() {

        AccessManager accessManager = obtainAccessManager();
        boolean result = accessManager.isTakeEnabled(testFeature, WorkflowStatus.New, readSealedAccountId, allowedManagedIk);
        assertThat(result).isFalse();

    }

    @Test
    public void testCanReadCompleted() {
    }

    @Test
    public void testCanReadSealed() {
    }

    @Test
    public void testCanWriteAlways() {
    }

    @Test
    public void determineAccountIdsForNullAccountReturnsEmptySet() {

        SessionController sessionController = mock(SessionController.class);
        when(sessionController.getAccount()).thenReturn(null);
        AccessManager accessManager = new AccessManager(null, sessionController, null);
        Set<Integer> result = accessManager.determineAccountIds(testFeature, canReadSealed());
        assertThat(result).isNotNull().isEmpty();
    }

// todo: remove or adopt to new rights    
//    @Test
//    public void determineAccountIdsForAccountNotNullReturnsSetOfPartnerIdsWithAtLeastReadSealed() {
//        Feature feature = testFeature;
//        AccessManager accessManager = obtainAccessManager();
//
//        Set<Integer> result = accessManager.determineAccountIds(feature, canReadSealed());
//        assertThat(result).isNotNull().isNotEmpty().
//                containsOnly(userAccountId, readWriteSealAccountId, readSealedAccountId);
//
//    }

    @Test
    public void determineAccountIdsForAccountWitoutPartnersReturnsOnlyOwnAccount() {
        Feature feature = testFeature;
        AccessManager accessManager = obtainAccessManager(new ArrayList<>(), new ArrayList<>(), false);

        Set<Integer> result = accessManager.determineAccountIds(feature, canReadSealed());
        assertThat(result).isNotNull().isNotEmpty().containsOnly(userAccountId);

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
    public void obtainIksForCreationOnDeniedIkReturnsEmpty() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, deniedManagedIk, Feature.ADDITIONAL_COST, Right.Deny));
        accessRights.add(new AccessRight(userAccountId, deniedManagedIk, Feature.NUB, Right.Write));
        accessRights.add(new AccessRight(userAccountId, deniedManagedIk, Feature.SPECIFIC_FUNCTION, Right.All));

        Set<Integer> fullIkSet = new HashSet<>();
        fullIkSet.add(deniedManagedIk);

        AccessManager accessManager = obtainAccessManager(accessRights, false, fullIkSet);
        Set<Integer> result = accessManager.ObtainIksForCreation(Feature.ADDITIONAL_COST);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void obtainIksForCreationOnDeniedIkPlusAdditionalIkReturnsAdditionalIkOnly() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, deniedManagedIk, Feature.ADDITIONAL_COST, Right.Deny));
        accessRights.add(new AccessRight(userAccountId, deniedManagedIk, Feature.NUB, Right.Write));
        accessRights.add(new AccessRight(userAccountId, deniedManagedIk, Feature.SPECIFIC_FUNCTION, Right.All));

        Set<Integer> fullIkSet = new HashSet<>();
        fullIkSet.add(deniedManagedIk);
        fullIkSet.add(unmanagedIk1);

        AccessManager accessManager = obtainAccessManager(accessRights, false, fullIkSet);
        Set<Integer> result = accessManager.ObtainIksForCreation(Feature.ADDITIONAL_COST);
        assertThat(result).isNotNull().containsOnly(unmanagedIk1);
    }

    @Test
    public void obtainIksForCreationOnAllowedIkPlusAdditionalIkReturnsBothIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, Feature.ADDITIONAL_COST, Right.Create));
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, Feature.NUB, Right.Write));
        accessRights.add(new AccessRight(userAccountId, allowedManagedIk, Feature.SPECIFIC_FUNCTION, Right.All));

        Set<Integer> fullIkSet = new HashSet<>();
        fullIkSet.add(allowedManagedIk);
        fullIkSet.add(unmanagedIk1);

        AccessManager accessManager = obtainAccessManager(accessRights, false, fullIkSet);
        Set<Integer> result = accessManager.ObtainIksForCreation(Feature.ADDITIONAL_COST);
        assertThat(result).isNotNull().containsOnly(unmanagedIk1, allowedManagedIk);
    }

    /*
    todo: convert to obtainIksForCreation tests?
    @Test
    public void retrieveAllowedForCreationIksReturnsOnlyOneDeniedForCreationIkOutOfMultipleIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedIk, testFeature, Right.Create));
        accessRights.add(new AccessRight(userAccountId, deniedIk, testFeature, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedForCreationIks(testFeature);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(allowedIk);
    }

    @Test
    public void retrieveAllowedForCreationIksReturnsOnlyOneCorrelatedAndResponsibleIK() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedIk, Feature.HC_INSURANCE, Right.Create));
        accessRights.add(new AccessRight(userAccountId, deniedIk, Feature.HC_INSURANCE, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedForCreationIks(Feature.HC_INSURANCE);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(unmanagedIk1);
    }
    
    @Test
    public void retrieveAllowedForCreationIksReturnsEmptyForNoneIkWithCreateRight() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedIk, Feature.HC_INSURANCE, Right.Write));
        accessRights.add(new AccessRight(userAccountId, deniedIk, Feature.HC_INSURANCE, Right.Create));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedForCreationIks(Feature.HC_INSURANCE);
        assertThat(result).isNotNull().isEmpty();
    }
    
    
    
     */
    @Test
    public void testObtainAllowedIks() {
    }

}
