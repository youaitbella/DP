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
            return !_cooperationRightFacade.isSupervisor(feature, ik, _sessionController.getAccountId());
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

    public CooperativeRight getAchievedRight(Feature feature, int partnerId, int ik) {
        Triple<Feature, Integer, Integer> triple = new Triple<>(feature, partnerId, ik);
        if (!_achivedRights.containsKey(triple)) {
            CooperativeRight right = _cooperationRightFacade.getCooperativeRight(
                    partnerId, // this right is "owned" by the partner
                    _sessionController.getAccountId(),
                    feature,
                    ik);
            _achivedRights.put(triple, right);
        }
        return _achivedRights.get(triple);
    }

    private void putAchivedRightIntoCache(CooperationRight right) {
        Triple<Feature, Integer, Integer> triple = new Triple<>(right.getFeature(), right.getOwnerId(), right.getIk());
        if (!_achivedRights.containsKey(triple)) {
            _achivedRights.put(triple, right.getCooperativeRight());
        }
    }

    private void putGrantedRightIntoCache(CooperationRight right) {
        Triple<Feature, Integer, Integer> triple = new Triple<>(right.getFeature(), right.getPartnerId(), right.getIk());
        if (!_grantedRights.containsKey(triple)) {
            _grantedRights.put(triple, right.getCooperativeRight());
        }
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

    private void loadGrantedRights(Feature feature) {
        loadGrantedRights(feature, -1);
    }

    private void loadGrantedRights(Feature feature, int ik) {
        List<CooperationRight> cooperationRights = _cooperationRightFacade.getGrantedCooperationRights(_sessionController.getAccountId(), feature);
        cooperationRights.stream().forEach((right) -> {
            putGrantedRightIntoCache(right);
        });
        if (ik > 0) {
            // there might be an ik supervisor
            cooperationRights = _cooperationRightFacade.getSupervisorRights(ik, feature);
            cooperationRights.stream().forEach((right) -> {
                putGrantedRightIntoCache(right);
            });

        }
    }

    List<Account> _partners4Edit;

    public List<Account> getPartnersForEdit(Feature feature) {
        if (_partners4Edit == null) {
            Set<Integer> ids = new HashSet<>();
            List<CooperationRight> cooperationRights = _cooperationRightFacade.getAchievedCooperationRights(_sessionController.getAccountId(), feature);
            for (CooperationRight right : cooperationRights) {
                putAchivedRightIntoCache(right);
                if (right.getOwnerId() >= 0 && right.getCooperativeRight().canReadCompleted()) {
                    ids.add(right.getOwnerId());
                }
                // especially for NUB, there might be a supervisor based on ik
                if (feature == Feature.NUB && right.getOwnerId() == -1
                        && right.getCooperativeRight().canReadCompleted()) {
                    ids.addAll(_nubProposalFacade.findAccountIdForIk(right.getIk()));
                }
            }
            ids.remove(_sessionController.getAccountId());  // remove own id (if in set)
            _partners4Edit = _accountFacade.getAccountsForIds(ids);
        }
        return _partners4Edit;
    }

}
