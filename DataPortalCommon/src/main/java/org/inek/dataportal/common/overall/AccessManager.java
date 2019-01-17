package org.inek.dataportal.common.overall;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.cooperation.entities.CooperationRight;
import org.inek.dataportal.common.enums.CooperativeRight;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.IkReference;
import org.inek.dataportal.api.enums.IkUsage;
import org.inek.dataportal.api.enums.ManagedBy;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRightFacade;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.Right;

/**
 * This class provides access to cooperations rights for one request. Depending on the current data, a couple of
 * accesses * might be necessary. To mimimize the db accesses, all cooperation data of one kind (achieved or granted)
 * will be read together and buffered (cached) for the current HTTP request. * This class has to be placed into request
 * scope.
 *
 * Starting in Dec 2017, tha concept of an IK admin has been introduced. Such an admin may grant or revoke rights for a
 * given ik. If available, these rights will override cooperative rights.
 *
 * @author muellermi
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

    /**
     * gets the cooperation rights by delegating the first request to the service and retrieving them from a local cache
     * for subsequent requests.
     *
     * @param feature
     * @param account
     *
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
        CooperativeRight cooperativeRight = getCooperationRights(feature, account)
                .stream()
                .filter(r -> r.getOwnerId() == partnerId && r.getIk() == ik && r.getPartnerId() == account.getId())
                .findAny()
                .orElse(new CooperationRight())
                .getCooperativeRight();
        return cooperativeRight;
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
        Set<Integer> responsibleForIks = _sessionController.getAccount().obtainResponsibleForIks(feature, iks);
        return responsibleForIks;
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
        // todo: check
        if (_sessionController.isInekUser(feature)) {
            return true;
        }

        if (ik > 0) {
            if (feature.getIkUsage() == IkUsage.ByResposibility || feature.getIkUsage() == IkUsage.ByResponsibilityAndCorrelation) {

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
        if (state.getId() >= WorkflowStatus.Provided.getId()) {
            return false;
        }
        if (state == WorkflowStatus.New && ik <= 0 && !isCreateAllowed(feature)) {
            return false;
        }
        
        if (feature.getManagedBy() == ManagedBy.None
                || feature.getIkReference() == IkReference.None
                || feature.getManagedBy() == ManagedBy.InekOrIkAdmin && !_ikCache.isManaged(ik, feature)) {

            return isUnmanagedWritable(ownerAccountId, feature, ik, state);

        }

        if (ik <= 0) {
            return false;
        }

        AccessRight right = obtainAccessRights(feature, r -> r.getIk() == ik).findFirst().orElse(new AccessRight());
        return right.canWrite();
    }

    private boolean isUnmanagedWritable(int ownerAccountId, Feature feature, int ik, WorkflowStatus state) {
        if (ownerAccountId == _sessionController.getAccountId()) {
            return true;
        }
        CooperativeRight right = getAchievedRight(feature, ownerAccountId, ik);
        return right.canWriteAlways()
                || (state.getId() >= WorkflowStatus.ApprovalRequested.getId() && right.canWriteCompleted());
    }

    /**
     * 
     * @param feature
     * @param state
     * @param ownerId
     * @return
     * @deprecated use isWriteable instead
     */
    @Deprecated
    public boolean isReadOnly(Feature feature, WorkflowStatus state, int ownerId) {
        return isReadOnly(feature, state, ownerId, -1);
    }

    /**
     * 
     * @param feature
     * @param state
     * @param ownerId
     * @param ik
     * @return
     * @deprecated use isWriteable instead
     */
    @Deprecated
    public boolean isReadOnly(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        return !isWritable(feature, state, ownerId, ik);
    }

    /**
     * the approval request will be enabled if the status is less than "approval requested" and the user is allowed to
     * write and if (is owned by other) the user is allowed to write but not a superviror himself and if (owned by
     * himself) if (cooperativ supervisor exist) the current user is no cooperative supervisor.
     *
     * @param feature
     * @param state
     * @param ownerId
     *
     * @return
     */
    public boolean isApprovalRequestEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isApprovalRequestEnabled(feature, state, ownerId, -1);
    }

    public boolean isApprovalRequestEnabled(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        return isApprovalRequestEnabled(feature, state, ownerId, ik, false);
    }

    public boolean isApprovalRequestEnabled(Feature feature, WorkflowStatus state, int ownerId, int ik,
            boolean hasUpdateButton) {
        // todo: check
        if (state.getId() >= WorkflowStatus.ApprovalRequested.getId()) {
            return false;
        }
        if (hasUpdateButton && state == WorkflowStatus.CorrectionRequested) {
            return false;
        }
        if (isReadOnly(feature, state, ownerId, ik)) {
            return false;
        }
        Account account = _sessionController.getAccount();
        if (ownerId != account.getId()) {
            // Its not the user's data
            // Thus, he can request approval, if he is allowed to write,
            // but not to seal (the latter would enable seal button instead)
            CooperativeRight right = getAchievedRight(feature, ownerId, ik);
            return right.canWriteCompleted() && !right.canSeal();
        }
        return needsApproval(feature, ik);

    }

    private boolean needsApproval(Feature feature, int ik) {
        Account account = _sessionController.getAccount();

        List<CooperationRight> coopSupervisors = getCooperationRights(feature, account)
                .stream()
                .filter(r -> r.getOwnerId() == account.getId()
                && r.getIk() == ik && r.getCooperativeRight().isSupervisor())
                .collect(Collectors.toList());

        return coopSupervisors.size() > 0;
    }

    /**
     * sealing|providing is enabled when it is the own data and the providing right is not granted to any other or the
     * user owns the sealing right and approval is requested or the user owned both edit and sealing right.
     *
     * @param feature
     * @param state
     * @param ownerId
     *
     * @return
     */
    public boolean isSealedEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isSealedEnabled(feature, state, ownerId, -1);
    }

    public boolean isSealedEnabled(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        return isSealedEnabled(feature, state, ownerId, ik, false);
    }

    public boolean isSealedEnabled(Feature feature, WorkflowStatus state, int ownerId, int ik, boolean hasUpdateButton) {
        // todo: check
        if (hasUpdateButton && state == WorkflowStatus.CorrectionRequested) {
            return false;
        }
        if (state.getId() >= WorkflowStatus.Provided.getId()) {
            return false;
        }
        if (ik > 0) {
            Optional<AccessRight> right = obtainAccessRights(feature, r -> r.getIk() == ik).findFirst();
            if (right.isPresent()) {
                return right.get().canSeal();
            }
        }

        Account account = _sessionController.getAccount();
        if (ownerId != account.getId()) {
            return getAchievedRight(feature, ownerId, ik).canSeal();
        }

        return !needsApproval(feature, ik);
    }

    /**
     * A supervisor may request a correction from the owner of a dataset This function indicates, whether this request
     * is feasible.
     *
     * @param feature
     * @param state
     * @param ownerId
     *
     * @return
     */
    public boolean isRequestCorrectionEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isRequestCorrectionEnabled(feature, state, ownerId, -1);
    }

    public boolean isRequestCorrectionEnabled(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        // todo: check
        if (state.getId() != WorkflowStatus.ApprovalRequested.getId()) {
            return false;
        }
        Account account = _sessionController.getAccount();
        return ownerId != account.getId() && isSealedEnabled(feature, state, ownerId, ik);
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
            return achievedRight.canSeal() || achievedRight.canWriteCompleted();
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

    public static Predicate<CooperationRight> canReadCompleted() {
        return r -> r.getCooperativeRight().canReadCompleted();
    }

    public static Predicate<CooperationRight> canReadSealed() {
        return r -> r.getCooperativeRight().canReadSealed();
    }

    public Set<Integer> determineAccountIds(Feature feature, Predicate<CooperationRight> canRead) {
        // todo: check
        Account account = _sessionController.getAccount();
        if (account == null) {
            LOGGER.log(Level.WARNING, "Accessmanager called without logged in user");
            return new HashSet<>();
        }

        Set<Integer> ids = getCooperationRights(feature, account)
                .stream()
                .filter(right -> right.getPartnerId() == account.getId())
                .filter(canRead)
                .map(right -> right.getOwnerId())
                .collect(Collectors.toSet());
        ids.add(account.getId());  // user always has the right to see his own

        return ids;
    }

    public boolean canReadSealed(Feature feature, int partnerId) {
        return canReadSealed(feature, partnerId, -1);
    }

    public boolean canReadSealed(Feature feature, int partnerId, int ik) {
        // todo: check
        CooperativeRight achievedRight = getAchievedRight(feature, partnerId, ik);
        return achievedRight.canReadSealed();
    }

    public boolean canReadCompleted(Feature feature, int partnerId) {
        return canReadCompleted(feature, partnerId, -1);
    }

    public boolean canReadCompleted(Feature feature, int partnerId, int ik) {
        // todo: check
        CooperativeRight achievedRight = getAchievedRight(feature, partnerId, ik);
        return achievedRight.canReadCompleted();
    }

    public boolean canReadAlways(Feature feature, int partnerId) {
        return canReadAlways(feature, partnerId, -1);
    }

    public boolean canReadAlways(Feature feature, int partnerId, int ik) {
        CooperativeRight achievedRight = getAchievedRight(feature, partnerId, ik);
        return achievedRight.canReadAlways();
    }

    public Set<Integer> ObtainIksForCreation(Feature feature) {
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

    public Boolean isCreateAllowed(Feature feature) {
        // todo: check
        if (feature.getIkReference() == IkReference.None) {
            return true;
        }
        return ObtainIksForCreation(feature).size() > 0;
    }

    public Set<Integer> ObtainAllowedIks(Feature feature) {
        Set<Integer> iks = _sessionController.getAccount().getFullIkSet();
        Set<Integer> deniedIks = retrieveDeniedManagedIks(feature);
        iks.removeAll(deniedIks);
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

    public Boolean isWriteAllowed(Feature feature, int ik) {
        for (AccessRight right : _sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.canWrite() && c.getFeature() == feature)
                .collect(Collectors.toList())) {
            if (right.getIk() == ik) {
                return true;
            }
        }
        return false;

    }
}
