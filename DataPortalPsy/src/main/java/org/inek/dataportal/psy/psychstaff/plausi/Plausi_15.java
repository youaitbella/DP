package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

public class Plausi_15 implements PsyStaffPlausi {

    @Override
    public String getPId() {
        return "15";
    }

    @Override
    public String getErrorMessage() {
        return "Für Anlage 1 (Erw) wurden VK-Angaben in der Spalte in der Spalte \"Stellenbesetzung als Budgetgrundlage in VK\" übermittelt, " +
                "jedoch fehlen die Angaben in der Spalte \"Stellenbesetzung für eine vollständige Umsetzung der Psych-PV\". " +
                "Bitte prüfen Sie Ihre Angaben und nehmen Sie bitte ggf. Korrekturen vor.";
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

            return !(sum2 > 0 && sum1 == 0);
        } else {
            return true;
        }
    }
}
