package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.proof.ProofWard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProofAggregator {

    public static List<ProofWard> aggregateDeptWards(final List<DeptWard> wards, final Date validFrom, final Date validTo) {
        List<ProofWard> proofWards = new ArrayList<>();
        wards.stream()
                .filter(w -> w.getValidFrom().compareTo(validFrom) <= 0)
                .filter(w -> w.getValidTo().compareTo(validTo) >= 0)
                .forEach(w -> {
                    //proofWards.add()
                });
        return proofWards;
    }


}
