package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

public class Plausi_21 implements PsyStaffPlausi {

    @Override
    public String getPId() {
        return "21";
    }

    @Override
    public String getErrorMessage() {
        return "Für Anlage 2 (Erw) wurde kein Ausnahmetatbestand ausgewählt und keine VK-Angaben übermittelt.";
    }

    @Override
    public boolean isPlausiCheckOk(StaffProof staffProof) {
        if (staffProof.getExclusionFactId2() == 0
                && staffProof.isForAdults()) {
            return staffProof.getStaffProofsEffective(PsychType.Adults).stream()
                    .mapToDouble(StaffProofEffective::getStaffingComplete)
                    .sum() > 0;
        } else {
            return true;
        }
    }
}
