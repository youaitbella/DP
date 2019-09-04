package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

import java.util.ArrayList;
import java.util.List;

public class Plausi_51 implements PsyStaffPlausi {

    private String _errorMessageTemplate = "Für Anlage 1 (KJP) wurde für die Berufsgruppe(n) {bg} Durchschnittskosten angegeben, " +
            "aber keine VK-Anzahl übermittelt.";

    @Override
    public String getPId() {
        return "51";
    }

    @Override
    public String getErrorMessage() {
        return _errorMessageTemplate;
    }

    @Override
    public boolean isPlausiCheckOk(StaffProof staffProof) {
        if (staffProof.getExclusionFactId1() == 0
                && staffProof.isForKids()) {
            List<String> cats = new ArrayList<>();

            for (StaffProofAgreed staffProofAgreed : staffProof.getStaffProofsAgreed(PsychType.Kids)) {
                if (staffProofAgreed.getStaffingComplete() == 0
                        && staffProofAgreed.getStaffingBudget() == 0
                        && staffProofAgreed.getAvgCost() > 0) {
                    cats.add(staffProofAgreed.getOccupationalCategory().getName());
                }
            }

            addCatsToErrorMessage(cats);

            return cats.isEmpty();
        } else {
            return true;
        }
    }

    private void addCatsToErrorMessage(List<String> cats) {
        String collectedCats = String.join(",", cats);
        _errorMessageTemplate = _errorMessageTemplate.replace("{bg}", collectedCats);
    }
}
