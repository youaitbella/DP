package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

public class Plausi_14 implements PsyStaffPlausi {

    @Override
    public String getPId() {
        return "14";
    }

    @Override
    public String getErrorMessage() {
        return "Für Anlage 1 (KJP) wurden VK-Angaben in der Spalte in der Spalte \"Stellenbesetzung für eine vollständige Umsetzung der " +
                "Psych-PV\" übermittelt, jedoch fehlen die Angaben in der Spalte \"Stellenbesetzung als Budgetgrundlage in VK\". " +
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

            return !(sum1 > 0 && sum2 == 0);
        } else {
            return true;
        }
    }
}
