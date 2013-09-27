package org.inek.dataportal.feature.nub;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Account;
import org.inek.dataportal.entities.CooperationRight;
import org.inek.dataportal.entities.NubProposal;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.CooperationRightFacade;

@Named @SessionScoped
public class NubSessionTools implements Serializable {

    protected static final Logger _logger = Logger.getLogger("NubSessionTools");
    private static final long serialVersionUID = 1L;

    @Inject private CooperationRightFacade _cooperationRightFacade;
    @Inject private SessionController _sessionController;

    // Seal own NUB is a marker, whether a NUB may be sealed by the owner (true)
    // or by a supervisor only (false)
    // It is used in coopearative environment 
    private Map<Integer, Boolean> _sealOwnNub;

    public Map<Integer, Boolean> getSealOwnNub() {
        ensureSealOwnNub();
        return _sealOwnNub;
    }

    /**
     * clears cache of sealOwnNub e.g. to ensure update after changing rights.
     */
    public void clearSealOwnNubCache() {
        _sealOwnNub = null;
    }

    private void ensureSealOwnNub() {
        if (_sealOwnNub != null) {
            return;
        }
        _sealOwnNub = new HashMap<>();
        Account account = _sessionController.getAccount();
        for (int ik : account.getFullIkList()) {
            _sealOwnNub.put(ik, !_cooperationRightFacade.hasSupervisor(Feature.NUB, ik));
        }
        List<CooperationRight> rights = _cooperationRightFacade
                .getGrantedCooperationRights(account.getAccountId(), Feature.NUB);
        for (CooperationRight right : rights) {
            if (right.getCooperativeRight().equals(CooperativeRight.ReadCompletedSealSupervisor)
                    || right.getCooperativeRight().equals(CooperativeRight.ReadWriteCompletedSealSupervisor)) {
                _sealOwnNub.put(right.getIk(), Boolean.FALSE);
            }
        }
    }

    public CooperativeRight getCooperativeRight(NubProposal nubProposal) {
        return _cooperationRightFacade.getAchievedCooperativeRight(
                nubProposal.getAccountId(), 
                _sessionController.getAccount().getAccountId(), 
                Feature.NUB, 
                nubProposal.getIk());
    }

}
