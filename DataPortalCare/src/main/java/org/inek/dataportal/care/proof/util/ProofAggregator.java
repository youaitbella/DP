package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.proof.ProofWardInfo;

import java.util.*;

public class ProofAggregator {

    public static List<ProofWardInfo> aggregateDeptWards(final List<DeptWard> wards, final Date validFrom, final Date validTo) {
        Map<String, ProofWardCollector> wardCollectors = new HashMap<>();
        wards.stream()
                .filter(w -> w.getValidFrom().compareTo(validTo) <= 0)
                .filter(w -> w.getValidTo().compareTo(validFrom) >= 0)
                .forEach(ward -> {
                    String key = ward.getWardName().toLowerCase().replace(" ", "") + "|" + ward.getLocationCodeVz();
                    ProofWardCollector collector = wardCollectors.computeIfAbsent(key, (i) -> new ProofWardCollector(validFrom, validTo));
                    collector.addDeptWard(ward);
                    wardCollectors.put(key, collector);
                });
        List<ProofWardInfo> proofWardInfos = new ArrayList<>();
        for (ProofWardCollector collector : wardCollectors.values()) {
            proofWardInfos.addAll(collector.obtainProofWards());
        }
        return proofWardInfos;
    }


}
