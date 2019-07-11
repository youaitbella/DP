package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

public class Plausi_12 implements PsyStaffPlausi {

    @Override
    public String getPId() {
        return "12";
    }

    @Override
    public String getErrorMessage() {
        return "Für Anlage 1 (KJP) wurden VK-Angaben übermittelt, jedoch fehlen die Angaben zu den Durchschnittskosten je Berufsgruppe. " +
                "Bitte prüfen Sie Ihre Angaben und nehmen Sie bitte ggf. Korrekturen vor.";
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

            double sum3 = staffProof.getStaffProofsAgreed(PsychType.Kids).stream()
                    .mapToDouble(StaffProofAgreed::getAvgCost)
                    .sum();

            return !((sum1 > 0 || sum2 > 0) && sum3 == 0);
        } else {
            return true;
        }
    }
}
