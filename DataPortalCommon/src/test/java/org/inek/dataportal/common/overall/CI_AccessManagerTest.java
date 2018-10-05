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
    private final int allowedIk = 444444444;
    private final int deniedIk = 555555555;
    
    private final int userAccountId = 4711;
    private final int noneAccountId = 0;
    private final int readWriteSealAccountId = 1;
    private final int readSealedAccountId = 2;
    private final Feature testFeature = Feature.NUB;
    
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
                new CooperationRight(readSealedAccountId, userAccountId, unmanagedIk2, testFeature, CooperativeRight.ReadSealed));
        cooperationRights.add(
                new CooperationRight(readSealedAccountId, userAccountId, -1, Feature.DRG_PROPOSAL, CooperativeRight.ReadSealed));
        
        return obtainAccessManager(accessRights, cooperationRights, isInekUser, fullIkSet);
    }
    
    private AccessManager obtainAccessManager(List<AccessRight> accessRights, List<CooperationRight> cooperationRights,
            boolean isInekUser) {
        return obtainAccessManager(accessRights, cooperationRights, isInekUser, obtainFullIkSet());
    }
    
    private AccessManager obtainAccessManager(List<AccessRight> accessRights, List<CooperationRight> cooperationRights,
            boolean isInekUser, Set<Integer> fullIkSet) {
        
        Set<Integer> accountIks = new HashSet<>();
        accountIks.add(allowedIk);
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
        when(ikCache.isManaged(allowedIk, testFeature)).thenReturn(true);
        when(ikCache.isManaged(allowedIk, Feature.HC_INSURANCE)).thenReturn(true);
        when(ikCache.isManaged(deniedIk, testFeature)).thenReturn(true);
        when(ikCache.isManaged(unmanagedIk1, testFeature)).thenReturn(false);
        when(ikCache.isManaged(unmanagedIk2, testFeature)).thenReturn(false);
        when(ikCache.isManaged(unmanagedIk3, testFeature)).thenReturn(false);
        
        
        Set<Integer> correlatedIks = new HashSet<>();
        correlatedIks.add(unmanagedIk1);
        
        when(ikCache.retriveCorrelatedIks(Feature.HC_INSURANCE, accountIks, responibleForIks)).thenReturn(correlatedIks);
        AccessManager accessManager = new AccessManager(cooperationRightFacade, sessionController, ikCache);
        return accessManager;
    }
    
    private Set<Integer> obtainFullIkSet() {
        Set<Integer> fullIkSet = new HashSet<>();
        fullIkSet.add(unmanagedIk1);
        fullIkSet.add(unmanagedIk2);
        fullIkSet.add(unmanagedIk2);
        fullIkSet.add(allowedIk);
        fullIkSet.add(deniedIk);
        return fullIkSet;
    }
    //</editor-fold>
    
    @Test
    public void retrieveAllowedManagedIksReturnsTheOnlyOneAllowedIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedIk, testFeature, Right.Read));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(testFeature);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(allowedIk);
    }

    @Test
    public void retrieveAllowedManagedIksReturnsOnlyOneAllowedIkOutOfMultipleIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedIk, testFeature, Right.Read));
        accessRights.add(new AccessRight(userAccountId, deniedIk, testFeature, Right.Deny));  
        accessRights.add(new AccessRight(userAccountId, unmanagedIk1, testFeature, Right.All));  
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllowedManagedIks(testFeature);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(allowedIk);
    }

    @Test
    public void retrieveAllowedManagedIksReturnsNoIkForListOfDeniedIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, deniedIk, testFeature, Right.Deny));
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
        accessRights.add(new AccessRight(userAccountId, allowedIk, testFeature, Right.Read));
        accessRights.add(new AccessRight(userAccountId, deniedIk, testFeature, Right.Deny));
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
        accessRights.add(new AccessRight(userAccountId, allowedIk, testFeature, Right.Read));
        accessRights.add(new AccessRight(userAccountId, deniedIk, testFeature, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        Set<Integer> result = accessManager.retrieveAllManagedIks(testFeature);
        assertThat(result).isNotNull().isNotEmpty().containsOnly(allowedIk, deniedIk);
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
        accessRights.add(new AccessRight(userAccountId, deniedIk, testFeature, Right.Deny));
        AccessManager accessManager = obtainAccessManager(accessRights);
        boolean result = accessManager.isAccessAllowed(testFeature, WorkflowStatus.Accepted, userAccountId, deniedIk);
        assertThat(result).isFalse();
    }

    @Test
    public void isAccessAllowedForOwnAccountIdWithAllowedIkReturnsTrue() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedIk, testFeature, Right.Write));
        AccessManager accessManager = obtainAccessManager(accessRights);
        boolean result = accessManager.isAccessAllowed(testFeature, WorkflowStatus.Accepted, userAccountId, allowedIk);
        assertThat(result).isTrue();
    }

    @Test
    public void testIsReadOnly() {
    }

    @Test
    public void testIsApprovalRequestEnabled() {
    }

    @Test
    public void testIsSealedEnabled() {
    }

    @Test
    public void testIsRequestCorrectionEnabled() {
    }

    @Test
    public void testIsDeleteEnabled() {
    }

    @Test
    public void testIsUpdateEnabled() {
    }

    @Test
    public void testIsTakeEnabled() {
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

    @Test
    public void determineAccountIdsForAccountNotNullReturnsSetOfPartnerIdsWithAtLeastReadCompleated() {
        Feature feature = testFeature;

        AccessManager accessManager = obtainAccessManager();

        Set<Integer> result = accessManager.determineAccountIds(feature, canReadCompleted());
        assertThat(result).isNotNull().isNotEmpty().containsOnly(userAccountId, readWriteSealAccountId);

    }

    @Test
    public void determineAccountIdsForAccountNotNullReturnsSetOfPartnerIdsWithAtLeastReadSealed() {
        Feature feature = testFeature;
        AccessManager accessManager = obtainAccessManager();

        Set<Integer> result = accessManager.determineAccountIds(feature, canReadSealed());
        assertThat(result).isNotNull().isNotEmpty().
                containsOnly(userAccountId, readWriteSealAccountId, readSealedAccountId);

    }

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
        accessRights.add(new AccessRight(userAccountId, deniedIk, Feature.ADDITIONAL_COST, Right.Deny));
        accessRights.add(new AccessRight(userAccountId, deniedIk, Feature.NUB, Right.Write));
        accessRights.add(new AccessRight(userAccountId, deniedIk, Feature.SPECIFIC_FUNCTION, Right.All));

        Set<Integer> fullIkSet = new HashSet<>();
        fullIkSet.add(deniedIk);

        AccessManager accessManager = obtainAccessManager(accessRights, false, fullIkSet);
        Set<Integer> result = accessManager.ObtainIksForCreation(Feature.ADDITIONAL_COST);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void obtainIksForCreationOnDeniedIkPlusAdditionalIkReturnsAdditionalIkOnly() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, deniedIk, Feature.ADDITIONAL_COST, Right.Deny));
        accessRights.add(new AccessRight(userAccountId, deniedIk, Feature.NUB, Right.Write));
        accessRights.add(new AccessRight(userAccountId, deniedIk, Feature.SPECIFIC_FUNCTION, Right.All));

        Set<Integer> fullIkSet = new HashSet<>();
        fullIkSet.add(deniedIk);
        fullIkSet.add(unmanagedIk1);

        AccessManager accessManager = obtainAccessManager(accessRights, false, fullIkSet);
        Set<Integer> result = accessManager.ObtainIksForCreation(Feature.ADDITIONAL_COST);
        assertThat(result).isNotNull().containsOnly(unmanagedIk1);
    }

    @Test
    public void obtainIksForCreationOnAllowedIkPlusAdditionalIkReturnsBothIk() {
        List<AccessRight> accessRights = new ArrayList<>();
        accessRights.add(new AccessRight(userAccountId, allowedIk, Feature.ADDITIONAL_COST, Right.Create));
        accessRights.add(new AccessRight(userAccountId, allowedIk, Feature.NUB, Right.Write));
        accessRights.add(new AccessRight(userAccountId, allowedIk, Feature.SPECIFIC_FUNCTION, Right.All));

        Set<Integer> fullIkSet = new HashSet<>();
        fullIkSet.add(allowedIk);
        fullIkSet.add(unmanagedIk1);

        AccessManager accessManager = obtainAccessManager(accessRights, false, fullIkSet);
        Set<Integer> result = accessManager.ObtainIksForCreation(Feature.ADDITIONAL_COST);
        assertThat(result).isNotNull().containsOnly(unmanagedIk1, allowedIk);
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
