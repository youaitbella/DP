package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.proof.ProofWard;

import java.util.*;

public class ProofAggregator {

    public static List<ProofWard> aggregateDeptWards(final List<DeptWard> wards, final Date validFrom, final Date validTo) {
        Map<String, ProofWard> proofWards = new HashMap<>();
        wards.stream()
                .filter(w -> w.getValidFrom().compareTo(validFrom) <= 0)
                .filter(w -> w.getValidTo().compareTo(validTo) >= 0)
                .map(w -> {
                    DeptWard ward = new DeptWard(w);
                    ward.setValidFrom(w.getValidFrom().compareTo(validFrom) > 0 ? w.getValidFrom() : validFrom);
                    ward.setValidTo(w.getValidTo().compareTo(validTo) < 0 ? w.getValidTo() : validTo);
                    return ward;
                })
                .forEach(ward -> {
                    String key = ward.getWardName() + "|" + ward.getLocationCodeVz();
                    if (!proofWards.containsKey(key)) {
                        ProofWard proofWard = ProofWard.builder()
                                .wardName(ward.getWardName())
                                .locationNumber(ward.getLocationCodeVz())
                                .from(ward.getValidFrom())
                                .to(ward.getValidTo())
                                .addSensitiveArea(ward.getDept().getSensitiveArea())
                                .addDept(ward.getFab())
                                .addDeptName(ward.getDeptName())
                                .beds(ward.getBedCount())
                                .build();
                        proofWards.put(key, proofWard);
                    } else {
                        ProofWard proofWard = proofWards.get(key);
                        proofWard.addSensitiveArea(ward.getDept().getSensitiveArea());
                        proofWard.addDept(ward.getFab());
                        proofWard.addDeptName(ward.getDeptName());
                    }
                });
        return new ArrayList<>(proofWards.values());
    }


}
