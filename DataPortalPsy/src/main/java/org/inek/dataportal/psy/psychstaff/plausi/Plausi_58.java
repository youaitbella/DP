package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

import java.util.ArrayList;
import java.util.List;

public class Plausi_58 implements PsyStaffPlausi {

    private String _errorMessageTemplate = "Für die Berufsgruppe(n) {bg} in Anlage 1 (Erw) wurden auffällig hohe Durchschnittskosten ausgewiesen.";

    @Override
    public String getPId() {
        return "58";
    }

    @Override
    public String getErrorMessage() {
        return _errorMessageTemplate;
    }

    @Override
    public boolean isPlausiCheckOk(StaffProof staffProof) {
        if (staffProof.getExclusionFactId1() == 0
                && staffProof.isForAdults()) {
            List<String> cats = new ArrayList<>();

            for (StaffProofAgreed staffProofAgreed : staffProof.getStaffProofsAgreed(PsychType.Adults)) {
                if (staffProofAgreed.getAvgCost() > 150000) {
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
