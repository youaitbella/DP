package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.common.data.KhComparison.entities.OccupationalCategory;
import org.inek.dataportal.psy.psychstaff.entity.ExclusionFact;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

public class PlausiTestHelper {

    public static ExclusionFact getExclusionFact(int id) {
        ExclusionFact ef = new ExclusionFact();
        ef.setId(id);
        return ef;
    }

    public static StaffProofAgreed createStaffProofAgreed(PsychType psychType, int occupancyId, double complete, double budget, double cost) {
        StaffProofAgreed staffProofAgreed = new StaffProofAgreed();
        staffProofAgreed.setPsychType(psychType);
        staffProofAgreed.setOccupationalCategory(createOccupationalCategory(occupancyId));
        staffProofAgreed.setStaffingComplete(complete);
        staffProofAgreed.setStaffingBudget(budget);
        staffProofAgreed.setAvgCost(cost);
        return staffProofAgreed;
    }

    public static OccupationalCategory createOccupationalCategory(int id) {
        OccupationalCategory category = new OccupationalCategory();
        category.setId(id);
        return category;
    }

    public static StaffProofEffective createStaffProofEffective(PsychType psychType, int occupancyId, double complete, double psych, double nonPsych, double other) {
        StaffProofEffective staffProofEffective = new StaffProofEffective();
        staffProofEffective.setPsychType(psychType);
        staffProofEffective.setOccupationalCategory(createOccupationalCategory(occupancyId));
        staffProofEffective.setStaffingComplete(complete);
        staffProofEffective.setStaffingDeductionPsych(psych);
        staffProofEffective.setStaffingDeductionNonPsych(nonPsych);
        staffProofEffective.setStaffingDeductionOther(other);
        return staffProofEffective;
    }
}
