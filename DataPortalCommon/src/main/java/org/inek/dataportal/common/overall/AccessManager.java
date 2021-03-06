package org.inek.dataportal.common.overall;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.IkReference;
import org.inek.dataportal.api.enums.IkUsage;
import org.inek.dataportal.api.enums.ManagedBy;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.cooperation.entities.CooperationRight;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRightFacade;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.CooperativeRight;
import org.inek.dataportal.common.enums.Right;
import org.inek.dataportal.common.enums.WorkflowStatus;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class provides answers to questions like
 * Do we grant write access to the current user for given data?
 * Is the data in a writeable state?
 * Is the user allowed to send teh data=
 * and much more.
 *
 * Beside the maintenance functions which store the rights,
 * the AccessManager is the only class allowed to handle rights.
 * 
 * ik is mandatory for the data:
 * If an admin is manadory and no admin exists, then no right is granted
 * 
 * ik is mandatory for the data:
 * If an admin for that ik function combination exists, then ik admin rights are used (ignore cooperation)
 *
 * else grant access to the onwer or use cooperative rights
 *
 * The rights depend on the user iks. That are thoese iks, the user registers in the user maintenance
 * For some functions, the user is responsible for other ik (we call it data ik)
 * Thus, we need to retrive rights for the user ik and use data ik within the data
 * 
 * ByResponsibilityAndCorrelation:
 * For some data ik only one dedicated user ik is leading
 * In such a case, access is restricted to read, if the user ik is not the leading ik
 * 
 */
@Named
@RequestScoped
public class AccessManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger("AccessManager");

    private CooperationRightFacade _cooperationRightFacade;
    private SessionController _sessionController;
    private ManagedIkCache _ikCache;

    public AccessManager() {
    }

    @Inject
    public AccessManager(CooperationRightFacade cooperationRightFacade,
                         SessionController sessionController,
                         ManagedIkCache ikCache) {
        _cooperationRightFacade = cooperationRightFacade;
        _sessionController = sessionController;
        _ikCache = ikCache;
    }

    public Account getSessionAccount() {
        return _sessionController.getAccount();
    }

    public int getSessionAccountId() {
        return _sessionController.getAccount().getId();
    }

    /**
     * gets the cooperation rights by delegating the first request to the service and retrieving them from a local cache
     * for subsequent requests.
     *
     * @param feature
     * @param account
     * @return
     */
    private List<CooperationRight> getCooperationRights(Feature feature, Account account) {
        if (!_cooperationRights.containsKey(feature)) {
            _cooperationRights.put(feature, _cooperationRightFacade.getCooperationRights(feature, account));
        }
        return _cooperationRights.get(feature);
    }

    private final Map<Feature, List<CooperationRight>> _cooperationRights = new HashMap<>();

    /**
     * Determines and returns the achieved rights as granted by the partner.
     *
     * @param feature
     * @param partnerId
     * @param ik
     *
     * @return
     */
    private CooperativeRight getAchievedRight(Feature feature, int partnerId, int ik) {
        Account account = _sessionController.getAccount();
        return getCooperationRights(feature, account)
                .stream()
                .filter(r -> r.getOwnerId() == partnerId && r.getIk() == ik && r.getPartnerId() == account.getId())
                .findAny()
                .orElse(new CooperationRight())
                .getCooperativeRight();
    }

    private Stream<AccessRight> obtainAccessRights(Feature feature, Predicate<AccessRight> predicate) {
        List<AccessRight> accessRights = _sessionController
                .getAccount()
                .getAccessRights();
        return accessRights.stream().filter(r -> r.getFeature() == feature).filter(predicate);
    }

    private Set<Integer> retrieveIkSet(Feature feature, Predicate<AccessRight> predicate) {
        return obtainAccessRights(feature, predicate)
                .map(r -> r.getIk())
                .collect(Collectors.toSet());
    }

    /**
     * retrieve all ik depending on the user's access rights where a manager exists
     *
     * @param feature
     *
     * @return
     */
    public Set<Integer> retrieveAllManagedIks(Feature feature) {
        return retrieveIkSet(feature, r -> _ikCache.isManaged(r.getIk(), feature));
    }

    public Set<Integer> retrieveAllowedManagedIks(Feature feature) {
        Predicate<AccessRight> predicate = r -> r.getRight() != Right.Deny && _ikCache.isManaged(r.getIk(), feature);
        Set<Integer> iks = retrieveIkSet(feature, predicate);
        if (feature.getIkUsage() == IkUsage.Direct) {
            return iks;
        }
        return _sessionController.getAccount().obtainResponsibleForIks(feature, iks);
    }

    public Set<Integer> retrieveDeniedManagedIks(Feature feature) {
        Predicate<AccessRight> predicate = r -> r.getRight() == Right.Deny && _ikCache.isManaged(r.getIk(), feature);
        return retrieveIkSet(feature, predicate);
    }

    /**
     * In normal workflow, only data the user has access to, will be displayed in the lists. But if some user tries to
     * open data by its id (via URL), this might be an non-authorized access. Within the dialog, it should be tested,
     * wheater the access is allowed or not. For a uniform interface, we pass in the state although it is ignored yet.
     * It migt be respected in the future.
     *
     * @param feature
     * @param state
     * @param ownerId
     *
     * @return
     */
    public boolean isAccessAllowed(Feature feature, WorkflowStatus state, int ownerId) {
        return isAccessAllowed(feature, state, ownerId, -1);
    }

    public boolean isAccessAllowed(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        if (_sessionController.isInekUser(feature)) {
            return true;
        }

        if (ik > 0) {
            if (feature.getIkUsage() == IkUsage.ByResposibility || feature.getIkUsage() == IkUsage.ByResponsibilityAndCorrelation) {
                return isResponsibleAccessible(feature, ik, AccessRight::canRead);
            }
            if (feature.getManagedBy() == ManagedBy.IkAdminOnly && !_ikCache.isManaged(ik, feature)) {
                return false;
            }
            Optional<AccessRight> right = obtainAccessRights(feature, r -> r.getIk() == ik).findFirst();
            if (right.isPresent()) {
                return right.get().canRead();
            }
            if (feature.getManagedBy() == ManagedBy.IkAdminOnly) {
                return false;
            }
        }

        if (ownerId == _sessionController.getAccountId()) {
            return true;
        }

        CooperativeRight right = getAchievedRight(feature, ownerId, ik);
        return right != CooperativeRight.None;
    }

    public boolean isWritable(Feature feature, WorkflowStatus state, int ownerAccountId) {
        return isWritable(feature, state, ownerAccountId, -1);
    }

    public boolean isWritable(Feature feature, WorkflowStatus state, int ownerAccountId, int ik) {
        return isAccessible(feature, state, ownerAccountId, ik, AccessRight::canWrite, CooperativeRight::canWrite);
    }

    /**
     * Method for convenience only. Preferably use use isWriteable.
     */
    public boolean isReadOnly(Feature feature, WorkflowStatus state, int ownerId) {
        return isReadOnly(feature, state, ownerId, -1);
    }

    /**
     * Method for convenience only. Preferably use use isWriteable.
     */
    public boolean isReadOnly(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        return !isWritable(feature, state, ownerId, ik);
    }

    /**
     * sealing|providing is enabled when it is the own data and the providing right is not granted to any other or the
     * user owns the sealing right and approval is requested or the user owned both edit and sealing right.
     *
     * @param feature
     * @param state
     * @param ownerAccountId
     * @return
     */
    public boolean isSealedEnabled(Feature feature, WorkflowStatus state, int ownerAccountId) {
        return isSealedEnabled(feature, state, ownerAccountId, -1);
    }

    public boolean isSealedEnabled(Feature feature, WorkflowStatus state, int ownerAccountId, int ik) {
        return isAccessible(feature, state, ownerAccountId, ik, AccessRight::canSeal, CooperativeRight::canSeal);
    }

    private boolean isAccessible(Feature feature, WorkflowStatus state, int ownerAccountId, int ik,
                                Predicate<AccessRight> checkAccessRight, Predicate<CooperativeRight> checkCooperativeRight) {

        if (state.getId() >= WorkflowStatus.Provided.getId()) {
            return false;
        }
        if (ik <= 0 && state == WorkflowStatus.New && isCreateAllowed(feature)) {
            return true;
        }

        if (feature.getManagedBy() == ManagedBy.None || feature.getIkReference() == IkReference.None) {
            return isUnmanagedAccessible(ownerAccountId, feature, ik, checkCooperativeRight);
        }

        if (ik <= 0) {
            return false;
        }

        if (feature.getIkUsage() == IkUsage.ByResponsibilityAndCorrelation) {
            return isCorrelationAccessible(feature, ik, checkAccessRight);
        }

        if (feature.getIkUsage() == IkUsage.ByResposibility) {
            return isResponsibleAccessible(feature, ik, checkAccessRight);
        }

        if (feature.getManagedBy() == ManagedBy.InekOrIkAdmin && !_ikCache.isManaged(ik, feature)) {
            return isUnmanagedAccessible(ownerAccountId, feature, ik, checkCooperativeRight);
        }

        return userHasAccessForUserIk(feature, ik, checkAccessRight);
    }

    private boolean isResponsibleAccessible(Feature feature, int dataIk, Predicate<AccessRight> check) {
        Set<Integer> userIks = _sessionController.getAccount().obtainUserIks(feature, dataIk);
        return _sessionController.getAccount().getAccessRights()
                .stream()
                .anyMatch(r -> userIks.contains(r.getIk()) && r.getFeature() == feature && check.test(r));
    }

    private boolean isCorrelationAccessible(Feature feature, int ik, Predicate<AccessRight> check) {
        int userIk = _ikCache.retrieveUserIkFromCorrelation(feature, ik);
        if (_sessionController.getAccount().getFullIkSet().contains(userIk)) {
            return userHasAccessForUserIk(feature, userIk, check);
        } else {
            return false;
        }
    }

    private boolean isUnmanagedAccessible(int ownerAccountId, Feature feature, int ik, Predicate<CooperativeRight> check) {
        if (ownerAccountId == _sessionController.getAccountId()) {
            return true;
        }
        CooperativeRight right = getAchievedRight(feature, ownerAccountId, ik);
        return check.test(right);
    }

    public boolean userHasReadAccess(Feature feature, int ik) {
        return userHasAccess(feature, ik, AccessRight::canRead);
    }

    public boolean userHasWriteAccess(Feature feature, int ik) {
        return userHasAccess(feature, ik, AccessRight::canWrite);
    }

    private boolean userHasAccess(Feature feature, int ik, Predicate<AccessRight> check) {
        if (feature.getIkUsage() == IkUsage.ByResponsibilityAndCorrelation) {
            return isCorrelationAccessible(feature, ik, check);
        }
        if (feature.getIkUsage() == IkUsage.ByResposibility) {
            return isResponsibleAccessible(feature, ik, check);
        }
        return userHasAccessForUserIk(feature, ik, check);
    }

    private boolean userHasAccessForUserIk(Feature feature, int userIk, Predicate<AccessRight> check) {
        return _sessionController.getAccount().getAccessRights()
                .stream()
                .anyMatch(r -> r.getIk() == userIk && r.getFeature() == feature && check.test(r));
    }

    /**
     * the approval request will be enabled if the user is enabled to write, but has no right to send the data
     *
     * @param feature
     * @param state
     * @param ownerId
     * @param ik
     *
     * @return
     */
    public boolean isApprovalRequestEnabled(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        if (state.getId() >= WorkflowStatus.Provided.getId()) {
            return false;
        }
        if (!_ikCache.isManaged(ik, feature)) {
            return false;
        }

        return isWritable(feature, state, ownerId, ik) && !isSealedEnabled(feature, state, ownerId, ik);
    }


    public boolean isDeleteEnabled(Feature feature, int ownerAccountId, int ik) {

        if (feature.getManagedBy() == ManagedBy.None
                || feature.getIkReference() == IkReference.None
                || feature.getManagedBy() == ManagedBy.InekOrIkAdmin && !_ikCache.isManaged(ik, feature)) {
            return ownerAccountId == _sessionController.getAccountId();
        }

        if (ik <= 0) {
            return false;
        }

        AccessRight right = obtainAccessRights(feature, r -> r.getIk() == ik).findFirst().orElse(new AccessRight());
        return right.canCreate();

    }

    /**
     * update is enabled when correction is requested by inek and it's the user's data or the user is allowed to seal or
     * to write.
     *
     * @param feature
     * @param state
     * @param ownerId
     *
     * @return
     */
    public boolean isUpdateEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isUpdateEnabled(feature, state, ownerId, -1);
    }

    public boolean isUpdateEnabled(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        // todo: check
        if (state != WorkflowStatus.CorrectionRequested) {
            return false;
        }
        if (ik > 0) {
            Optional<AccessRight> right = obtainAccessRights(feature, r -> r.getIk() == ik).findFirst();
            if (right.isPresent()) {
                return right.get().canWrite() || right.get().canSeal();
            }
        }
        Account account = _sessionController.getAccount();
        if (ownerId != account.getId()) {
            CooperativeRight achievedRight = getAchievedRight(feature, ownerId, ik);
            return achievedRight.canSeal(); // todo: check || achievedRight.canWrite();
        }

        return true;
    }

    public boolean isTakeEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isTakeEnabled(feature, state, ownerId, -1);
    }

    /**
     * Indicates, if it is allowed to take ownership of a dataset.
     *
     * @param feature
     * @param state
     * @param ownerId
     * @param ik
     *
     * @return
     */
    public boolean isTakeEnabled(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        // todo: check
        Account account = _sessionController.getAccount();
        if (ownerId == account.getId()) {
            return false;
        }
        return getAchievedRight(feature, ownerId, ik).canTake();
    }

    public Set<Integer> determineAccountIds(Feature feature) {
        // todo: check
        Account account = _sessionController.getAccount();
        if (account == null) {
            LOGGER.log(Level.WARNING, "Accessmanager called without logged in user");
            return new HashSet<>();
        }

        Set<Integer> ids = getCooperationRights(feature, account)
                .stream()
                .filter(right -> right.getPartnerId() == account.getId())
                .filter(right -> right.getCooperativeRight() != CooperativeRight.None)
                .map(right -> right.getOwnerId())
                .collect(Collectors.toSet());

        ids.add(account.getId());  // user always has the right to see his own

        return ids;
    }

    public boolean canRead(Feature feature, int partnerId) {
        return canRead(feature, partnerId, -1);
    }

    public boolean canRead(Feature feature, int partnerId, int ik) {
        // todo: check
        CooperativeRight achievedRight = getAchievedRight(feature, partnerId, ik);
        return achievedRight.canRead();
    }

    public Set<Integer> obtainAllowedIks(Feature feature) {
        Set<Integer> iks = _sessionController.getAccount().getFullIkSet();
        Set<Integer> deniedIks = retrieveDeniedManagedIks(feature);
        iks.removeAll(deniedIks);
        return retrieveEffectiveIks(feature, iks);
    }

    public boolean isCreateAllowed(Feature feature) {
        if (feature.getIkReference() == IkReference.None) {
            return true;
        }
        return obtainIksForCreation(feature).size() > 0;
    }

    public Set<Integer> obtainIksForCreation(Feature feature) {
        if (feature.getManagedBy() == ManagedBy.IkAdminOnly) {
            return retrieveAllowedForCreationIks(feature);
        }

        Set<Integer> iks = _sessionController.getAccount().getFullIkSet();
        Set<Integer> deniedIks = retrieveDeniedForCreationIks(feature);
        iks.removeAll(deniedIks);
        return iks;
    }

    private Set<Integer> retrieveAllowedForCreationIks(Feature feature) {
        Set<Integer> iks = retrieveIkSet(feature, r -> r.getRight().canCreate());
        iks.removeIf(ik -> feature.getManagedBy() == ManagedBy.IkAdminOnly && !_ikCache.isManaged(ik, feature));
        return retrieveEffectiveIks(feature, iks);
    }

    private Set<Integer> retrieveEffectiveIks(Feature feature, Set<Integer> iks) {
        if (feature.getIkReference() == IkReference.None || feature.getIkUsage() == IkUsage.Direct) {
            return iks;
        }
        Set<Integer> responsibleForIks = _sessionController.getAccount().
                obtainResponsibleForIks(feature, iks);
        if (feature.getIkUsage() == IkUsage.ByResponsibilityAndCorrelation) {
            responsibleForIks = _ikCache.retrieveCorrelatedIks(feature, iks, responsibleForIks);
        }
        return responsibleForIks;
    }

    private Set<Integer> retrieveDeniedForCreationIks(Feature feature) {
        return retrieveIkSet(feature, r -> !r.getRight().canCreate());
    }

    public boolean ikIsManaged(int ik, Feature feature) {
        return _ikCache.isManaged(ik, feature);
    }
}
