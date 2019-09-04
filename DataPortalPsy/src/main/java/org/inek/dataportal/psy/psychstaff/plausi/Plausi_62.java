package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Plausi_62 implements PsyStaffPlausi {

    private String _errorMessageTemplate = "Für die Berufsgruppe {bg} erscheint das Verhältnis der VK-Angaben " +
            "zwischen Anlage 1 (Erw) und Anlage 2 (Erw) unplausibel.";

    @Override
    public String getPId() {
        return "62";
    }

    @Override
    public String getErrorMessage() {
        return _errorMessageTemplate;
    }

    @Override
    public boolean isPlausiCheckOk(StaffProof staffProof) {
        if (staffProof.getExclusionFactId1() != 0 || staffProof.getExclusionFactId2() != 0) {
            return true;
        }

        if (!staffProof.isForAdults() || (staffProof.getStatusApx1() == 0 && staffProof.getStatusApx2() == 0)) {
            return true;
        }

        if (staffProof.getExclusionFactId1() == 0) {
            List<String> cats = new ArrayList<>();

            for (StaffProofAgreed staffProofAgreed : staffProof.getStaffProofsAgreed(PsychType.Adults)
                    .stream()
                    .filter(c -> c.getStaffingBudget() > 5)
                    .collect(Collectors.toList())) {

                Optional<StaffProofEffective> first = staffProof.getStaffProofsEffective(PsychType.Adults)
                        .stream()
                        .filter(c -> c.getOccupationalCategory().getId() == staffProofAgreed.getOccupationalCategory().getId())
                        .findFirst();

                if (first.isPresent()) {
                    StaffProofEffective staffProofEffective = first.get();

                    if (staffProofEffective.getStaffingComplete() / staffProofAgreed.getStaffingBudget() > 2
                            || staffProofEffective.getStaffingComplete() / staffProofAgreed.getStaffingBudget() < 0.5) {
                        cats.add(staffProofAgreed.getOccupationalCategory().getName());
                    }
                }
                else {
                    return true;
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
