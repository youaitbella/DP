package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

public class Plausi_25 implements PsyStaffPlausi {

    @Override
    public String getPId() {
        return "25";
    }

    @Override
    public String getErrorMessage() {
        return "Das Verhältnis der übermittelten Ist-PsychPV-Personalkosten in Anlage 2 (Erw) zu der Summe der vereinbarten " +
                "Durchschnittskosten * VK aus Anlage 1 (Erw) erscheint unplausibel niedrig (Die Ist-Personalkosten liegen um mehr als 50% " +
                "unter den vereinbarten Personalkosten). Bitte prüfen Sie Ihre Angaben und nehmen Sie bitte ggf. Korrekturen vor.";
    }

    @Override
    public boolean isPlausiCheckOk(StaffProof staffProof) {
        if (!staffProof.isForAdults()) {
            return true;
        }
        if (staffProof.getExclusionFactId1() == 0
                && staffProof.getExclusionFactId2() == 0
                && staffProof.isForAdults()) {

                return staffProof.getStaffProofsEffective(PsychType.Kids).stream()
                    .mapToDouble(StaffProofEffective::getStaffingComplete)
                    .sum() > 0;

        } else {
            return true;
        }
    }


    /*
        "spmIsForAdults=1
        und Anlage 1 mit spmExclusionFactId1=0
        und (sum((StaffingcompleteAgreed) > 0
                 or  sum(StaffingBudgetAgreed) > 0)
        und AvgCostAgreed > 0
        und Anlage 2 mit spmExclusionFactid2=0
        und sum(StaffingcompleteEffekt) > 0
        und EffectiveCosts > 0
        and EffectiveCosts/(AvgCostAgreed * sum(StaffingBudgetAgreed)) < 0.5"
    */
}
