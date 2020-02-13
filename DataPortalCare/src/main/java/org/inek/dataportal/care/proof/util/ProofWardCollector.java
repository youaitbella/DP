package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.proof.ProofWardInfo;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.common.utils.Period;

import java.util.*;
import java.util.stream.Collectors;

public class ProofWardCollector {
    private List<DeptWard> wards = new ArrayList<>();
    private Date validFrom;
    private Date validTo;

    public ProofWardCollector(Date validFrom, Date validTo) {
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public int duration() {
        return DateUtils.diffDays(validFrom, validTo);
    }

    public void addDeptWard(DeptWard deptWard) {
        DeptWard ward = new DeptWard(deptWard);
        ward.setValidFrom(deptWard.getValidFrom().compareTo(validFrom) > 0 ? deptWard.getValidFrom() : validFrom);
        ward.setValidTo(deptWard.getValidTo().compareTo(validTo) < 0 ? deptWard.getValidTo() : validTo);
        if (wards.size() > 0
                && !wards.get(0).getWardName().toLowerCase().replace(" ", "")
                .equals(deptWard.getWardName().toLowerCase().replace(" ", ""))
                && wards.get(0).getLocationCodeVz() != deptWard.getLocationCodeVz()) {
            throw new IllegalArgumentException("Ward names and locations have to be equal");
        }
        wards.add(ward);
    }

    public String sensitiveAreas() {
        return wards.stream().map(w -> w.getDept().getSensitiveArea())
                .distinct()
                .sorted(String::compareTo)
                .collect(Collectors.joining(", "));
    }

    public List<ProofWardInfo> obtainProofWards() {
        List<ProofWardInfo> proofWardInfos = new ArrayList<>();
        if (wards.size() == 0) {
            return proofWardInfos;
        }

        List<Period> periods = obtainPeriods();
        return obtainProofWards(periods);
/*
        if (periods.size() > 1) {
            return obtainProofWards(periods);
        }
        proofWards.add(createProofWard(wards));
        return proofWards;
*/
    }

    private ProofWardInfo obtainProofWard() {
        return createProofWard(wards);
    }

    private ProofWardInfo createProofWard(List<DeptWard> deptWards) {
        DeptWard ward = deptWards.get(0);
        ProofWardInfo proofWardInfo = ProofWardInfo.builder()
                .wardName(ward.getWardName().trim().replace("  ", " "))
                .locationNumber(ward.getLocationCodeVz())
                .from(ward.getValidFrom())
                .to(ward.getValidTo())
                .addSensitiveArea(ward.getDept().getSensitiveArea())
                .addDept(ward.getFab())
                .addDeptName(ward.getDeptName())
                .beds(ward.getBedCount())
                .build();

        for (int i = 1; i < deptWards.size(); i++) {
            DeptWard deptWard = deptWards.get(i);
            proofWardInfo.addSensitiveArea(deptWard.getDept().getSensitiveArea());
            proofWardInfo.addDept(deptWard.getFab());
            proofWardInfo.addDeptName(deptWard.getDeptName());
        }
        return proofWardInfo;
    }

    private List<ProofWardInfo> obtainProofWards(List<Period> periods) {
        List<ProofWardInfo> proofWardInfos = new ArrayList<>();
        periods.stream().forEach(p -> {
            ProofWardCollector collector = new ProofWardCollector(p.from(), p.to());
            wards.stream()
                    .filter(w -> w.getValidFrom().compareTo(p.to()) <= 0)
                    .filter(w -> w.getValidTo().compareTo(p.from()) >= 0)
                    .forEach(ward -> {
                        collector.addDeptWard(ward);
                    });
            proofWardInfos.add(collector.obtainProofWard());
        });

        List<ProofWardInfo> mergedProofWardInfos = new ArrayList<>();
        ProofWardInfo mergedProofWardInfo = null;
        for (ProofWardInfo proofWardInfo : proofWardInfos) {
            if (mergedProofWardInfo == null) {
                mergedProofWardInfo = proofWardInfo;
            } else if (mergedProofWardInfo.sensitiveAreas().equals(proofWardInfo.sensitiveAreas())) {
                mergedProofWardInfo.merge(proofWardInfo);
            } else {
                mergedProofWardInfos.add(mergedProofWardInfo);
                mergedProofWardInfo = proofWardInfo;
            }
        }
        mergedProofWardInfos.add(mergedProofWardInfo);
        return mergedProofWardInfos;
    }

    private List<Period> obtainPeriods() {
        Set<Date> fromDates = new HashSet<>();
        Set<Date> toDates = new HashSet<>();
        wards.stream().forEach(w -> {
            fromDates.add(w.getValidFrom());
            toDates.add(w.getValidTo());
            if (w.getValidFrom().compareTo(validFrom) > 0) {
                toDates.add(DateUtils.addDays(w.getValidFrom(), -1));
            }
            if (w.getValidTo().compareTo(validTo) < 0) {
                fromDates.add(DateUtils.addDays(w.getValidTo(), 1));
            }
        });

        List<Period> periods = new ArrayList<>();
        fromDates.stream().sorted(Date::compareTo).forEachOrdered(from -> {
            toDates.stream().filter(toDate -> toDate.compareTo(from) >= 0).sorted(Date::compareTo).findFirst()
                    .ifPresent(toDate -> periods.add(new Period(from, toDate)));
        });
        return periods;
    }
}
