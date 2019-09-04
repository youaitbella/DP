package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

public class Plausi_9 implements PsyStaffPlausi {

    @Override
    public String getPId() {
        return "9";
    }

    @Override
    public String getErrorMessage() {
        return "Für Anlage 1 (Erw) wurde kein Ausnahmetatbestand ausgewählt und keine VK-Angaben eingetragen.";
    }

    @Override
    public boolean isPlausiCheckOk(StaffProof staffProof) {
        if (staffProof.getExclusionFactId1() == 0
                && staffProof.isForAdults()) {
            double sum1 = staffProof.getStaffProofsAgreed(PsychType.Adults).stream()
                    .mapToDouble(StaffProofAgreed::getStaffingComplete)
                    .sum();

            double sum2 = staffProof.getStaffProofsAgreed(PsychType.Adults).stream()
                    .mapToDouble(StaffProofAgreed::getStaffingBudget)
                    .sum();
            return !(sum1 == 0 && sum2 == 0);
        } else {
            return true;
        }
    }
}
