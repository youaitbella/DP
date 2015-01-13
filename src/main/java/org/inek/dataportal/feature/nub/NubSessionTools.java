package org.inek.dataportal.feature.nub;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.cooperation.CooperationRight;
import org.inek.dataportal.entities.NubProposal;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;

@Named @SessionScoped
public class NubSessionTools implements Serializable {

    private static final Logger _logger = Logger.getLogger("NubSessionTools");
    private static final long serialVersionUID = 1L;

    @Inject private CooperationRightFacade _cooperationRightFacade;
    @Inject private SessionController _sessionController;

    private String _nubFilter = "";
    public String getNubFilter() {
        return _nubFilter;
    }

    public void setNubFilter(String nubFilter) {
        _nubFilter = nubFilter;
    }

    // Seal own NUB is a marker, whether a NUB may be sealed by the owner (true)
    // or by a supervisor only (false)
    // It is used in coopearative environment
    private Map<Integer, Boolean> _sealOwnNub;
    private Set<Integer> _managedAccounts;

    public Map<Integer, Boolean> getSealOwnNub() {
        ensureSealOwnNub();
        return _sealOwnNub;
    }

    public Set<Integer> getManagedAccounts() {
        ensureManagedAcounts();
        return _managedAccounts;
    }

    /**
     * clears cache of sealOwnNub e.g. to ensure update after changing rights.
     */
    public void clearCache() {
        _sealOwnNub = null;
        _managedAccounts = null;
    }

    private void ensureSealOwnNub() {
        if (_sealOwnNub != null) {
            return;
        }
        ensureManagedAcounts();
        _sealOwnNub = new HashMap<>();
        Account account = _sessionController.getAccount();
        for (int ik : account.getFullIkList()) {
            // allowed for own NUB if supervisor herself or no supervisor exists
            _sealOwnNub.put(ik, _cooperationRightFacade.isSupervisor(Feature.NUB, ik, account.getAccountId()) || !_cooperationRightFacade.hasSupervisor(Feature.NUB, ik));
        }
        List<CooperationRight> rights = _cooperationRightFacade
                .getGrantedCooperationRights(account.getAccountId(), Feature.NUB);
        for (CooperationRight right : rights) {
            if (right.getCooperativeRight().equals(CooperativeRight.ReadCompletedSealSupervisor)
                    || right.getCooperativeRight().equals(CooperativeRight.ReadWriteCompletedSealSupervisor)
                    || right.getCooperativeRight().equals(CooperativeRight.ReadWriteSealSupervisor)) {
                _sealOwnNub.put(right.getIk(), Boolean.FALSE);
            }
        }
    }

    private void ensureManagedAcounts() {
        if (_managedAccounts != null) {
            return;
        }
        Account account = _sessionController.getAccount();
        _managedAccounts = _cooperationRightFacade.isSupervisorFor(Feature.NUB, account);

    }

    public CooperativeRight getCooperativeRight(NubProposal nubProposal) {
        return _cooperationRightFacade.getCooperativeRight(
                nubProposal.getAccountId(),
                _sessionController.getAccountId(),
                Feature.NUB,
                nubProposal.getIk());
    }

    public CooperativeRight getSupervisorRight(NubProposal nub) {
        return _cooperationRightFacade.getSupervisorRight(Feature.NUB, nub.getIk(), _sessionController.getAccountId());
    }

}
