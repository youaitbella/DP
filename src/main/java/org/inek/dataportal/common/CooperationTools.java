package org.inek.dataportal.common;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import org.inek.dataportal.facades.NubProposalFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;

/**
 * This class provides access to cooperations rights for one request Depending
 * on the current data, a couple of accesses might be necessary To mimimize the
 * db accesses, all cooperation data of one kind (achieved or granted) will be
 * read together and buffered (cached) for the current HTTP request. During one
 * request, either all lists for the user or a single dataset is used
 *
 * This class has be placed into request scope.
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class CooperationTools implements Serializable {

    @Inject
    CooperationRightFacade _cooperationRightFacade;
    @Inject
    SessionController _sessionController;
    @Inject
    NubProposalFacade _nubProposalFacade;
    @Inject
    AccountFacade _accountFacade;

    /**
     * gets the cooperation rights by delegating the first request to the
     * service and retrieving them from a local cache for subsequent requests
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
     * In normal workflow, only data to which the user has access to, is
     * displaed in the lists. But if some user tries to open data by its id,
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

    public boolean isAllowed(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        if (ownerId == _sessionController.getAccountId()) {
            return true;
        }
        CooperativeRight right = getAchievedRight(feature, ownerId, ik);
        return right != CooperativeRight.None;
    }

    /**
     * Data is readonly when - provided to InEK - is foreign data and no edit
     * right is granted to current user
     *
     * @param feature
     * @param state
     * @param ownerId
     * @return
     */
    public boolean isReadOnly(Feature feature, WorkflowStatus state, int ownerId) {
        return isReadOnly(feature, state, ownerId, -1);
    }

    public boolean isReadOnly(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        if (state.getValue() >= WorkflowStatus.Provided.getValue()) {
            return true;
        }
        if (ownerId == _sessionController.getAccountId()) {
            return false;
        }
        CooperativeRight right = getAchievedRight(feature, ownerId, ik);
        return !right.canWriteAlways() && !(state.getValue() >= WorkflowStatus.ApprovalRequested.getValue() && right.canWriteCompleted());
    }

    /**
     * the approval request will be enabled if the status is less than "approval
     * requested" and the user is allowed to write and if (is owned by other)
     * the user is alloed to write but not a superviror himself and if (owned by
     * himself) if (cooperativ supervisor exist) the current user is no
     * cooperative supervisor
     *
     * @param feature
     * @param state
     * @param ownerId
     * @return
     */
    public boolean isApprovalRequestEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isApprovalRequestEnabled(feature, state, ownerId, -1);
    }

    public boolean isApprovalRequestEnabled(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        if (state.getValue() >= WorkflowStatus.ApprovalRequested.getValue()) {
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
                .filter(r -> r.getOwnerId() == account.getId() && r.getIk() == ik && r.getCooperativeRight().canSeal())
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
     * determines, if data needs to be sealed by any ik supervisor
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
     * approval is requested or the user ownd both edit and sealing right
     *
     * @param feature
     * @param state
     * @param ownerId
     * @return
     */
    public boolean isSealedEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isSealedEnabled(feature, state, ownerId, -1);
    }

    public boolean isSealedEnabled(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        if (state.getValue() >= WorkflowStatus.Provided.getValue()) {
            return false;
        }
        Account account = _sessionController.getAccount();
        if (ownerId != account.getId()) {
            return getAchievedRight(feature, ownerId, ik).canSeal();
        }

        return !needsApproval(feature, ik);
    }

    /**
     * A supervisor may request a correction from the owner of a dataset This
     * function indicates, whether this request is feasible
     *
     * @param feature
     * @param state
     * @param ownerId
     * @return
     */
    public boolean isRequestCorrectionEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isRequestCorrectionEnabled(feature, state, ownerId, -1);
    }

    public boolean isRequestCorrectionEnabled(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        if (state.getValue() != WorkflowStatus.ApprovalRequested.getValue()) {
            return false;
        }
        Account account = _sessionController.getAccount();
        return ownerId != account.getId() && isSealedEnabled(feature, state, ownerId, ik);
    }

    public boolean isTakeEnabled(Feature feature, WorkflowStatus state, int ownerId) {
        return isTakeEnabled(feature, state, ownerId, -1);
    }

    /**
     * Indicates, if it is allowed to take ownership of a dataset
     *
     * @param feature
     * @param state
     * @param ownerId
     * @param ik
     * @return
     */
    public boolean isTakeEnabled(Feature feature, WorkflowStatus state, int ownerId, int ik) {
        Account account = _sessionController.getAccount();
        if (ownerId == account.getId()) {
            return false;
        }
        return getAchievedRight(feature, ownerId, ik).canTake();
    }

    public CooperativeRight getAchievedRight(Feature feature, int partnerId) {
        return getAchievedRight(feature, partnerId, -1);
    }

    /**
     * Determines and returns the achieved rights A user might get rights from
     * two sources: ik supervision or individual If an ik supervisor is needed,
     * than and individiual supervising right is canceled. If both rights are
     * provided, than determine the higher rights
     *
     * @param feature
     * @param partnerId
     * @param ik
     * @return
     */
    public CooperativeRight getAchievedRight(Feature feature, int partnerId, int ik) {
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

    private List<Account> _partners4Edit;

    public List<Account> getPartnersForEdit(Feature feature) {
        if (_partners4Edit == null) {
            Set<Integer> ids = determineAccountIds(feature, canReadCompleted());
            _partners4Edit = _accountFacade.getAccountsForIds(ids);
        }
        return _partners4Edit;
    }

    public static Predicate<CooperationRight> canReadCompleted() {
        return r -> r.getCooperativeRight().canReadCompleted();
    }

    public static Predicate<CooperationRight> canReadSealed() {
        return r -> r.getCooperativeRight().canReadSealed();
    }

    private Set<Integer> determineAccountIds(Feature feature, Predicate<CooperationRight> canRead) {
        Account account = _sessionController.getAccount();
        Set<Integer> ids = new HashSet<>();
        getCooperationRights(feature, account)
                .stream()
                .filter((right) -> right.getPartnerId() == account.getId())
                .filter(canRead)
                .forEach((right) -> {
                    if (right.getOwnerId() >= 0) {
                        ids.add(right.getOwnerId());
                    } else if (right.getOwnerId() == -1 && right.getIk() > 0) {
                        // user is supervisor, lets get all account ids, which might be supervised
                        ids.addAll(_cooperationRightFacade.getAccountIdsByFeatureandIk(feature, right.getIk()));
                    }
                });
        ids.remove(_sessionController.getAccountId());  // remove own id (if in set)
        return ids;
    }

    private List<Account> _partners4Display;
    public List<Account> getPartnersForDisplay(Feature feature) {
        if (_partners4Display == null) {
            Set<Integer> ids = determineAccountIds(feature, canReadSealed());
            _partners4Display = _accountFacade.getAccountsForIds(ids);

        }
        return _partners4Display;
    }

}
