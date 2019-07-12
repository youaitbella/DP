package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

public class Plausi_26 implements PsyStaffPlausi {

    @Override
    public String getPId() {
        return "26";
    }

    @Override
    public String getErrorMessage() {
        return "Das Verhältnis der übermittelten Ist-PsychPV-Personalkosten in Anlage 2 (KJP) zu der Summe der vereinbarten " +
                "Durchschnittskosten * VK aus Anlage 1 (Erw) erscheint unplausibel niedrig (Die Ist-Personalkosten liegen um mehr als 50% " +
                "unter den vereinbarten Personalkosten). Bitte prüfen Sie Ihre Angaben und nehmen Sie bitte ggf. Korrekturen vor.";
    }

    @Override
    public boolean isPlausiCheckOk(StaffProof staffProof) {

        if (staffProof.getExclusionFactId1() != 0 || staffProof.getExclusionFactId2() != 0) {
            return true;
        }

        if (!staffProof.isForKids() || (staffProof.getStatusApx1() == 0 && staffProof.getStatusApx2() == 0)) {
            return true;
        }

        double sumStaffCompleteAgreed = staffProof.getStaffProofsAgreed(PsychType.Kids).stream()
                .mapToDouble(StaffProofAgreed::getStaffingComplete)
                .sum();

        double sumStaffBudgetAgreed = staffProof.getStaffProofsAgreed(PsychType.Kids).stream()
                .mapToDouble(StaffProofAgreed::getStaffingBudget)
                .sum();

        double sumAvgCostAgreed = staffProof.getStaffProofsAgreed(PsychType.Kids).stream()
                .mapToDouble(StaffProofAgreed::getAvgCost)
                .sum();

        double sumStaffingCompleteEffetive = staffProof.getStaffProofsEffective(PsychType.Kids).stream()
                .mapToDouble(StaffProofEffective::getStaffingComplete)
                .sum();


        if ((sumStaffCompleteAgreed > 0 || sumStaffBudgetAgreed > 0)
                && sumAvgCostAgreed > 0
                && sumStaffingCompleteEffetive > 0
                && staffProof.getKidsEffectiveCosts() > 0) {

            double value = calculateValue(staffProof, sumStaffBudgetAgreed > 0);

            double v = staffProof.getKidsEffectiveCosts() / value;
            return !(v < 0.5);

        }
        else {
            return true;
        }
    }

    private double calculateValue(StaffProof staffProof, boolean usingBudget) {
        double sum = 0;

        for (StaffProofAgreed staffProofAgreed : staffProof.getStaffProofsAgreed(PsychType.Kids)) {
            if (usingBudget) {
                sum += staffProofAgreed.getStaffingBudget() * staffProofAgreed.getAvgCost();
            }
            else {
                sum += staffProofAgreed.getStaffingComplete() * staffProofAgreed.getAvgCost();
            }
        }
        return sum;
    }
}
