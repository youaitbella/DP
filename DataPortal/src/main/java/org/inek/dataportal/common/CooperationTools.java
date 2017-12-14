package org.inek.dataportal.common;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.cooperation.CooperationRight;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;

/**
 * This class provides access to cooperations rights for one request Depending
 * on the current data, a couple of accesses might be necessary To mimimize the
 * db accesses, all cooperation data of one kind (achieved or granted) will be
 * read together and buffered (cached) for the current HTTP request. During one
 * request, either all lists for the user or a single dataset is used. During
 * one request, the same feature is used. Thus, caching ignores the feature.
 *
 * This class has be placed into request scope.
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class CooperationTools implements Serializable {

    @Inject private CooperationRightFacade _cooperationRightFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;

    /**
     * gets the cooperation rights by delegating the first request to the
     * service and retrieving them from a local cache for subsequent requests.
     *
     * @param feature
     * @param account
     * @return
     */
    private List<CooperationRight> getCooperationRights(Feature feature, Account account) {
        if (_cooperationRights == null) {
            _cooperationRights = _cooperationRightFacade.getCooperationRights(feature, account);
        }
        return _cooperationRights;
    }

    private List<CooperationRight> _cooperationRights;

    /**
     * Determines and returns the achieved rights. A user might get rights from
     * two sources: ik supervision or individual. If an ik supervisor is needed,
     * than and individiual supervising right is canceled. If both rights are
     * provided, than determine the higher rights.
     *
     * @param feature
     * @param partnerId
     * @param ik
     * @return
     */
    private CooperativeRight getAchievedRight(Feature feature, int partnerId, int ik) {
        Account account = _sessionController.getAccount();
        CooperativeRight right = getCooperationRights(feature, account)
                .stream()
                .filter(r -> r.getOwnerId() == -1 && r.getIk() == ik && r.getPartnerId() == account.getId())
                .findAny()
                .orElse(new CooperationRight())
                .getCooperativeRight();
        String coopRights = getCooperationRights(feature, account)
                .stream()
                .filter(r -> r.getOwnerId() == partnerId && r.getIk() == ik && r.getPartnerId() == account.getId())
                .findAny()
                .orElse(new CooperationRight())
                .getCooperativeRight()
                .getRightsAsString();
        if (needIkSupervisor(feature, account, ik)) {
            // remove cooperative supervising right
            coopRights = coopRights.substring(0, 3) + "0";
        }
        return right.mergeRightFromStrings(coopRights);
    }

    
    /**
     * In normal workflow, only data the user has access to, will be
     * displayed in the lists. But if some user tries to open data by its id,
     * this might be an non-authorized access. Within the editing function, it
     * should be tested, wheater the acces is allowed or not.
     *
     * @param feature
     * @param state
     * @param ownerId
     * @return
     */
    public boolean isAllowed(Feature feature, WorkflowStatus state, int ownerId) {
        return isAllowed(feature, state, ownerId, -1);
    }

    public boolean isAllowed(Feature feature, WorkflowStatus state, int ownerId, Integer ik) {
        if (ownerId == _sessionController.getAccountId()) {
            return true;
        }
        CooperativeRight right = getAchievedRight(feature, ownerId, ik == null ? -1 : ik);
        return right != CooperativeRight.None;
    }

    /**
     * Data is readonly when - provided to InEK - is foreign data and no edit
     * right is granted to current user.
     *
     * @param feature
     * @param state
     * @param ownerId
     * @return
     */
    public boolean isReadOnly(Feature feature, WorkflowStatus state, int ownerId) {
        return isReadOnly(feature, state, ownerId, -1);
    }

    public boolean isReadOnly(Feature feature, WorkflowStatus state, int ownerId, Integer ik) {
        if (state.getId() >= WorkflowStatus.Provided.getId()) {
            return true;
        }
        if (ownerId == _sessionController.getAccountId()) {
            return false;
        }
        CooperativeRight right = getAchievedRight(feature, ownerId, ik == null ? -1 : ik);
        return !right.canWriteAlways() && !(state.getId() >= WorkflowStatus.ApprovalRequested.getId() && right.canWriteCompleted());
    }

    /**
     * the approval request will be enabled if the status is less than "approval
     * requested" and the user is allowed to write and if (is owned by other)
     * the user is allowed to write but not a superviror himself and if (owned by
     * himself) if (cooperativ supervisor exist) the current user is no
     * cooperative supervisor.
     *
     * @param feature
     * @param state
     * @param ownerId
     * @return
     */
    public boolean isApprovalRequestEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isApprovalRequestEnabled(feature, state, ownerId, -1);
    }

    public boolean isApprovalRequestEnabled(Feature feature, WorkflowStatus state, int ownerId, Integer ik) {
        return isApprovalRequestEnabled(feature, state, ownerId, ik, false);
    }
    
    public boolean isApprovalRequestEnabled(Feature feature, WorkflowStatus state, int ownerId, Integer ik, boolean hasUpdateButton) {
        if (state.getId() >= WorkflowStatus.ApprovalRequested.getId()) {
            return false;
        }
        if (hasUpdateButton && state ==  WorkflowStatus.CorrectionRequested) {
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
            CooperativeRight right = getAchievedRight(feature, ownerId, ik == null ? -1 : ik);
            return right.canWriteCompleted() && !right.canSeal();
        }
        return needsApproval(feature, ik == null ? -1 : ik);

    }

    private boolean needsApproval(Feature feature, int ik) {
        Account account = _sessionController.getAccount();

        List<CooperationRight> coopSupervisors = getCooperationRights(feature, account)
                .stream()
                .filter(r -> r.getOwnerId() == account.getId() && r.getIk() == ik && r.getCooperativeRight().isSupervisor())
                .collect(Collectors.toList());

        if (!needIkSupervisor(feature, account, ik)) {
            // No ik supervisor needed. Must request approval if at least one supervisor exists
            return coopSupervisors.size() > 0;
        }

        boolean isSelfIkSupervisor = getCooperationRights(feature, account)
                .stream()
                .anyMatch(r -> r.getOwnerId() == -1 && r.getIk() == ik && r.getPartnerId() == account.getId());

        if (!isSelfIkSupervisor) {
            // user is not himself ik supervisor --> needs to request approval
            return true;
        }

        // user is ik supervisor himself
        // he needs to request approval, if he granted supervison rights to any other ik supervisor
        boolean hasGrantedSupervision = coopSupervisors
                .stream()
                .anyMatch(cr -> getCooperationRights(feature, account)
                        .stream()
                        .anyMatch(r -> r.getOwnerId() == -1 && r.getIk() == ik && r.getPartnerId() == cr.getPartnerId()));

        return hasGrantedSupervision;
    }

    /**
     * Determines, if data needs to be sealed by any ik supervisor.
     *
     * @param feature
     * @param account
     * @param ik
     * @return
     */
    public boolean needIkSupervisor(Feature feature, Account account, int ik) {
        boolean needIkSupervisor = getCooperationRights(feature, account)
                .stream()
                .anyMatch(r -> r.getOwnerId() == -1 && r.getIk() == ik);
        return needIkSupervisor;
    }

    /**
     * sealing|providing is enabled when it is the own data and the providing
     * right is not granted to any other or the user owns the sealing right and
     * approval is requested or the user owned both edit and sealing right.
     *
     * @param feature
     * @param state
     * @param ownerId
     * @return
     */
    public boolean isSealedEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isSealedEnabled(feature, state, ownerId, -1);
    }

    public boolean isSealedEnabled(Feature feature, WorkflowStatus state, int ownerId, Integer ik) {
        return isSealedEnabled(feature, state, ownerId, ik, false);
    }
    
    public boolean isSealedEnabled(Feature feature, WorkflowStatus state, int ownerId, Integer ik, boolean hasUpdateButton) {
        if (hasUpdateButton && state ==  WorkflowStatus.CorrectionRequested) {
            return false;
        }
        if (state.getId() >= WorkflowStatus.Provided.getId()) {
            return false;
        }
        Account account = _sessionController.getAccount();
        if (ownerId != account.getId()) {
            return getAchievedRight(feature, ownerId, ik == null ? -1 : ik).canSeal();
        }

        return !needsApproval(feature, ik == null ? -1 : ik);
    }

    /**
     * A supervisor may request a correction from the owner of a dataset This
     * function indicates, whether this request is feasible.
     *
     * @param feature
     * @param state
     * @param ownerId
     * @return
     */
    public boolean isRequestCorrectionEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isRequestCorrectionEnabled(feature, state, ownerId, -1);
    }

    public boolean isRequestCorrectionEnabled(Feature feature, WorkflowStatus state, int ownerId, Integer ik) {
        if (state.getId() != WorkflowStatus.ApprovalRequested.getId()) {
            return false;
        }
        Account account = _sessionController.getAccount();
        return ownerId != account.getId() && isSealedEnabled(feature, state, ownerId, ik == null ? -1 : ik);
    }

    /**
     * update is enabled when correction is requested by inek 
     * and it's the user's data or the user is allowed to seal or o write.
     *
     * @param feature
     * @param state
     * @param ownerId
     * @return
     */
    public boolean isUpdateEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isUpdateEnabled(feature, state, ownerId, -1);
    }

    public boolean isUpdateEnabled(Feature feature, WorkflowStatus state, int ownerId, Integer ik) {
        if (state !=  WorkflowStatus.CorrectionRequested) {
            return false;
        }
        Account account = _sessionController.getAccount();
        if (ownerId != account.getId()) {
            CooperativeRight achievedRight = getAchievedRight(feature, ownerId, ik == null ? -1 : ik);
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
     * @return
     */
    public boolean isTakeEnabled(Feature feature, WorkflowStatus state, int ownerId, Integer ik) {
        Account account = _sessionController.getAccount();
        if (ownerId == account.getId()) {
            return false;
        }
        return getAchievedRight(feature, ownerId, ik == null ? -1 : ik).canTake();
    }

    public CooperativeRight getAchievedRight(Feature feature, int partnerId) {
        return getAchievedRight(feature, partnerId, -1);
    }

    
    private final Map<Integer, Set<Integer>> _partnerIks = new ConcurrentHashMap<>();
    
    /**
     * Collects all iks for given feature and partner where the current account
     * achieved rights.
     *
     * @param feature
     * @param partnerId
     * @return
     */
    public Set<Integer> getPartnerIks(Feature feature, int partnerId) {
        if (!_partnerIks.containsKey(partnerId)) {
            //System.out.println("getPartnerIks " + partnerId);
            Account account = _sessionController.getAccount();
            Set<Integer> iks = getIksFromCooperativeRights(feature, account, partnerId);
            // next check, whether the user is aupervisor for this feature and if an ik of the partner is supervised
            boolean isSupervisor = getCooperationRights(feature, account)
                    .stream()
                    .anyMatch(r -> r.getOwnerId() == -1 && r.getIk() > 0 && r.getPartnerId() == account.getId());
            if (isSupervisor) {
                Account partnerAccount = _accountFacade.findAccount(partnerId);
                Set<Integer> partnerIks = partnerAccount.getFullIkSet();
                for (int ik : partnerIks) {
                    boolean isSupervised = getCooperationRights(feature, account)
                            .stream()
                            .anyMatch(r -> r.getOwnerId() == -1 && r.getIk() == ik && r.getPartnerId() == account.getId());
                    if (isSupervised) {
                        iks.add(ik);
                    }
                }
            }
            _partnerIks.put(partnerId, iks);
        }
        return _partnerIks.get(partnerId);

    }

    private Set<Integer> getIksFromCooperativeRights(Feature feature, Account account, int partnerId) {
        // get iks from cooperative rights
        Set<Integer> iks = getCooperationRights(feature, account)
                .stream()
                .filter(r -> r.getOwnerId() == partnerId && r.getPartnerId() == account.getId()
                        && r.getIk() > 0 && r.getCooperativeRight() != CooperativeRight.None)
                .map(r -> r.getIk())
                .collect(Collectors.toSet());
        return iks;
    }

    public static Predicate<CooperationRight> canReadCompleted() {
        return r -> r.getCooperativeRight().canReadCompleted();
    }

    public static Predicate<CooperationRight> canReadSealed() {
        return r -> r.getCooperativeRight().canReadSealed();
    }

    public static Predicate<CooperationRight> canWriteAlways() {
        return r -> r.getCooperativeRight().canWriteAlways();
    }

    public Set<Integer> determineAccountIds(Feature feature, Predicate<CooperationRight> canRead) {
        Account account = _sessionController.getAccount();
        Set<Integer> ids = new LinkedHashSet<>();
        ids.add(account.getId());  // user always has the right to see his own
        getCooperationRights(feature, account)
                .stream()
                .filter((right) -> right.getPartnerId() == account.getId())
                .filter(canRead)
                .forEach((right) -> {
                    if (right.getOwnerId() >= 0) {
                        ids.add(right.getOwnerId());
                    } else if (right.getOwnerId() == -1 && right.getIk() > 0) {
                        // user is supervisor, lets get all account ids, which might be supervised
                        ids.addAll(_cooperationRightFacade.getAccountIdsByFeatureAndIk(feature, right.getIk()));
                    }
                });
        //ids.remove(_sessionController.getAccountId());  // remove own id (if in set)
        return ids;
    }

    public boolean canReadSealed(Feature feature, int partnerId, int ik) {
        CooperativeRight achievedRight = getAchievedRight(feature, partnerId, ik);
        return achievedRight.canReadSealed();
    }

    public boolean canReadCompleted(Feature feature, int partnerId, int ik) {
        CooperativeRight achievedRight = getAchievedRight(feature, partnerId, ik);
        return achievedRight.canReadCompleted();
    }

    public boolean canReadAlways(Feature feature, int partnerId, int ik) {
        CooperativeRight achievedRight = getAchievedRight(feature, partnerId, ik);
        return achievedRight.canReadAlways();
    }

}
