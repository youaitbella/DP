package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;

import java.util.ArrayList;
import java.util.List;

public class PsyStaffPlausiChecker {

    private List<PsyStaffPlausi> _errorPlausis = new ArrayList<>();

    private List<PsyStaffPlausi> _plausis = new ArrayList<>();

    public PsyStaffPlausiChecker() {
        collectPlausis();
    }

    private void collectPlausis() {
        _plausis.add(new Plausi_9());
        _plausis.add(new Plausi_10());
        _plausis.add(new Plausi_11());
        _plausis.add(new Plausi_12());
        _plausis.add(new Plausi_13());
        _plausis.add(new Plausi_14());
        _plausis.add(new Plausi_15());
        _plausis.add(new Plausi_16());
        _plausis.add(new Plausi_21());
        _plausis.add(new Plausi_22());
        _plausis.add(new Plausi_25());
        _plausis.add(new Plausi_26());
        _plausis.add(new Plausi_27());
        _plausis.add(new Plausi_28());
        _plausis.add(new Plausi_50());
        _plausis.add(new Plausi_51());
        _plausis.add(new Plausi_52());
        _plausis.add(new Plausi_53());
        _plausis.add(new Plausi_54());
        _plausis.add(new Plausi_55());
        _plausis.add(new Plausi_56());
        _plausis.add(new Plausi_57());
        _plausis.add(new Plausi_58());
        _plausis.add(new Plausi_59());
        _plausis.add(new Plausi_60());
        _plausis.add(new Plausi_61());
        _plausis.add(new Plausi_62());
        _plausis.add(new Plausi_63());
    }

    public String getErrorMessages() {
        return buildErrorMessageString();
    }

    public boolean isErrorsFound() {
        return !_errorPlausis.isEmpty();
    }

    public void checkPsyStaff(StaffProof staffProof) {
        for (PsyStaffPlausi plausi : _plausis) {
            if (!plausi.isPlausiCheckOk(staffProof)) {
                _errorPlausis.add(plausi);
            }
        }
    }

    private String buildErrorMessageString() {
        StringBuilder errorString = new StringBuilder();

        for (PsyStaffPlausi plausi : _errorPlausis) {
            errorString.append(buildStringForPlausi(plausi));
            errorString.append("\n");
        }

        return errorString.toString();
    }

    private String buildStringForPlausi(PsyStaffPlausi plausi) {
        return String.format("PrüfungsNr. %s: %s", plausi.getPId(), plausi.getErrorMessage());
    }
}
