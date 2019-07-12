package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

public class Plausi_22 implements PsyStaffPlausi {

    @Override
    public String getPId() {
        return "22";
    }

    @Override
    public String getErrorMessage() {
        return "Für Anlage 2 (KJP) wurde kein Ausnahmetatbestand ausgewählt und keine VK-Angaben übermittelt. " +
                "Bitte prüfen Sie Ihre Angaben und nehmen Sie bitte ggf. Korrekturen vor.";
    }

    @Override
    public boolean isPlausiCheckOk(StaffProof staffProof) {
        if (staffProof.getExclusionFactId2() == 0
                && staffProof.isForKids()) {
            return staffProof.getStaffProofsEffective(PsychType.Kids).stream()
                    .mapToDouble(StaffProofEffective::getStaffingComplete)
                    .sum() > 0;
        } else {
            return true;
        }
    }
}
