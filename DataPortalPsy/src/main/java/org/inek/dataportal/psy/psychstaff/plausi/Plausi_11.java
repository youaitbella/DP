package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

public class Plausi_11 implements PsyStaffPlausi {

    @Override
    public String getPId() {
        return "11";
    }

    @Override
    public String getErrorMessage() {
        return "Für Anlage 1 (Erw) wurden VK-Angaben übermittelt, jedoch fehlen die Angaben zu den Durchschnittskosten je Berufsgruppe.";
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

            double sum3 = staffProof.getStaffProofsAgreed(PsychType.Adults).stream()
                    .mapToDouble(StaffProofAgreed::getAvgCost)
                    .sum();

            return !((sum1 > 0 || sum2 > 0) && sum3 == 0);
        } else {
            return true;
        }
    }
}
