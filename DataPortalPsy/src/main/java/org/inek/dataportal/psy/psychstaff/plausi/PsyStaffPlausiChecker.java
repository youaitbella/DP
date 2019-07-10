package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;

import java.util.ArrayList;
import java.util.List;

public class PsyStaffPlausiChecker {

    private List<String> _errorMessages = new ArrayList<>();

    private List<PsyStaffPlausi> _plausis = new ArrayList<>();

    public PsyStaffPlausiChecker() {
        collectPlausis();
    }

    private void collectPlausis() {
        _plausis.add(new Plausi_9());
        _plausis.add(new Plausi_10());
    }

    public String getErrorMessages() {
        return _errorMessages.toString();
    }

    public boolean isErrorsFound() {
        return !_errorMessages.isEmpty();
    }

    public void checkPsyStaff(StaffProof staffProof) {
        for (PsyStaffPlausi plausi : _plausis) {
            if (!plausi.isPlausiCheckOk(staffProof)) {
                _errorMessages.add(plausi.getErrorMessage());
            }
        }
    }
}
