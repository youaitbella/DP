package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Plausi_60 implements PsyStaffPlausi {

    private String _errorMessageTemplate = "In Anlage 1 (Erw) erscheint das Verhältnis der Angaben zwischen den Spalten " +
            "\"Stellenbesetzung für eine vollständige Umsetzung der Psych-PV in VK\" und \"Stellenbesetzung als Budgetgrundlage in VK\" " +
            "für die Berufsgruppe(n) {bg} unplausibel.";

    @Override
    public String getPId() {
        return "60";
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

            for (StaffProofAgreed staffProofAgreed : staffProof.getStaffProofsAgreed(PsychType.Adults)
                    .stream()
                    .filter(c -> c.getStaffingComplete() > 5)
                    .collect(Collectors.toList())) {
                if (staffProofAgreed.getStaffingBudget() / staffProofAgreed.getStaffingComplete() > 2
                        || staffProofAgreed.getStaffingBudget() / staffProofAgreed.getStaffingComplete() < 0.5) {
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
