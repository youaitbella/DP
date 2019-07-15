package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

public class Plausi_10 implements PsyStaffPlausi {

    @Override
    public String getPId() {
        return "10";
    }

    @Override
    public String getErrorMessage() {
        return "Für Anlage 1 (KJP) wurde kein Ausnahmetatbestand ausgewählt und keine VK-Angaben eingetragen.";
    }

    @Override
    public boolean isPlausiCheckOk(StaffProof staffProof) {
        if (staffProof.getExclusionFactId1() == 0
                && staffProof.isForKids()) {
            double sum1 = staffProof.getStaffProofsAgreed(PsychType.Kids).stream()
                    .mapToDouble(StaffProofAgreed::getStaffingComplete)
                    .sum();

            double sum2 = staffProof.getStaffProofsAgreed(PsychType.Kids).stream()
                    .mapToDouble(StaffProofAgreed::getStaffingBudget)
                    .sum();
            return !(sum1 == 0 && sum2 == 0);
        } else {
            return true;
        }
    }
}
