package org.inek.dataportal.common;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
import org.inek.dataportal.helper.structures.Triple;

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
        CooperativeRight right = getAchievedRight(feature, ownerId);
        return right.canWriteAlways() || state.getValue() >= WorkflowStatus.ApprovalRequested.getValue() && right.canWriteCompleted();
    }

    /**
     * the approval request will be enabled if the status is less than "approval
     * requested" and the user is allowed to write and if (mandatory supervisor
     * exists) the current user is no mandatory supervisor and if (mandatory no
     * supervisor exists) if (cooperativ supervisor exist) the current user is
     * no cooperative supervisor
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
        if (ik > 0 && _cooperationRightFacade.hasSupervisor(feature, ik)) {
            boolean isSupervisor = _cooperationRightFacade.isIkSupervisor(feature, ik, _sessionController.getAccountId());
        }
        boolean hasSupervisor;
        if (ownerId == _sessionController.getAccountId()) {
            return getGrantedRight(feature, ownerId).isSupervisor();
        } else {
            List<CooperationRight> grantedRights = _cooperationRightFacade.getGrantedCooperationRights(ownerId, feature);
            hasSupervisor = grantedRights.stream().anyMatch(r -> r.getCooperativeRight().isSupervisor());
            // todo: is user himself supervisor?
            assert (false);
            return false;
        }
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
        return true;
    }

    Map<Triple<Feature, Integer, Integer>, CooperativeRight> _achivedRights = new ConcurrentHashMap<>();

    public CooperativeRight getAchievedRight(Feature feature, int partnerId) {
        return getAchievedRight(feature, partnerId, -1);
    }

    /**
     * Determines and returns the achieved rights
     * A user might get rights from two sources: ik supervision or individual
     * If an ik supervisor is needed, than and individiual supervising right is canceled.
     * If both rights are provided, than determine the higher rights
     * @param feature
     * @param partnerId
     * @param ik
     * @return 
     */
    public CooperativeRight getAchievedRight(Feature feature, int partnerId, int ik) {
        Account account = _sessionController.getAccount();
        boolean needIkSupervisor = getCooperationRights(feature, account)
                .stream()
                .anyMatch(r -> r.getOwnerId() == -1 && r.getIk() == ik);
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
        if (needIkSupervisor){
            // remove cooperative supervising right
            coopRights = coopRights.substring(0, 2) + "0";
        }
        return right.mergeRightFromStrings(coopRights);
    }

    Map<Triple<Feature, Integer, Integer>, CooperativeRight> _grantedRights = new ConcurrentHashMap<>();

    public CooperativeRight getGrantedRight(Feature feature, int partnerId) {
        return getGrantedRight(feature, partnerId, -1);
    }

    public CooperativeRight getGrantedRight(Feature feature, int partnerId, int ik) {
        Triple<Feature, Integer, Integer> triple = new Triple<>(feature, partnerId, ik);
        if (!_grantedRights.containsKey(triple)) {
            CooperativeRight right = _cooperationRightFacade.getCooperativeRight(
                    _sessionController.getAccountId(),
                    partnerId,
                    feature,
                    ik);
            _grantedRights.put(triple, right);
        }
        return _grantedRights.get(triple);
    }

    private List<Account> _partners4Edit;

    public List<Account> getPartnersForEdit(Feature feature) {
        if (_partners4Edit == null) {
            Account account = _sessionController.getAccount();
            Set<Integer> ids = new HashSet<>();

            getCooperationRights(feature, account)
                    .stream()
                    .filter((right) -> right.getPartnerId() == account.getId() && right.getCooperativeRight().canReadCompleted())
                    .forEach((right) -> {
                if (right.getOwnerId() >= 0) {
                    ids.add(right.getOwnerId());
                } else if (right.getOwnerId() == -1 && right.getIk() > 0) {
                    // user is supervisor, lets get all account ids, which might be supervised
                    ids.addAll(_cooperationRightFacade.getAccountIdsByFeatureandIk(feature, right.getIk()));
                }
            });
            ids.remove(_sessionController.getAccountId());  // remove own id (if in set)
            _partners4Edit = _accountFacade.getAccountsForIds(ids);
        }
        return _partners4Edit;
    }

    private List<Account> _partners4Display;
    public List<Account> getPartnersForDisplay(Feature feature) {
        if (_partners4Display == null){
            
        }
        return _partners4Display;
    }

}
