package org.inek.dataportal.care.proof.util;

import javafx.util.Pair;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.proof.ProofWard;
import org.inek.dataportal.common.utils.DateUtils;

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
                && !wards.get(0).getWardName().equals(deptWard.getWardName())
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

    public List<ProofWard> obtainProofWards() {
        List<ProofWard> proofWards = new ArrayList<>();
        if (wards.size() == 0) {
            return proofWards;
        }

        List<Pair<Date, Date>> periods = obtainPeriods();
        return obtainProofWards(periods);
/*
        if (periods.size() > 1) {
            return obtainProofWards(periods);
        }
        proofWards.add(createProofWard(wards));
        return proofWards;
*/
    }

    private ProofWard obtainProofWard() {
        return createProofWard(wards);
    }

    private ProofWard createProofWard(List<DeptWard> deptWards) {
        DeptWard ward = deptWards.get(0);
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

        for (int i = 1; i < deptWards.size(); i++) {
            proofWard.addSensitiveArea(ward.getDept().getSensitiveArea());
            proofWard.addDept(ward.getFab());
            proofWard.addDeptName(ward.getDeptName());
        }
        return proofWard;
    }

    private List<ProofWard> obtainProofWards(List<Pair<Date, Date>> periods) {
        List<ProofWard> proofWards = new ArrayList<>();
        periods.stream().forEach(p -> {
            Date periodFrom = p.getKey();
            Date periodTo = p.getValue();
            ProofWardCollector collector = new ProofWardCollector(periodFrom, periodTo);
            wards.stream()
                    .filter(w -> w.getValidFrom().compareTo(periodTo) <= 0)
                    .filter(w -> w.getValidTo().compareTo(periodFrom) >= 0)
                    .forEach(ward -> {
                        collector.addDeptWard(ward);
                    });
            proofWards.add(collector.obtainProofWard());
        });

        List<ProofWard> mergedProofWards = new ArrayList<>();
        ProofWard mergedProofWard = null;
        for (ProofWard proofWard : proofWards) {
            if (mergedProofWard == null) {
                mergedProofWard = proofWard;
            } else if (mergedProofWard.sensitiveAreas().equals(proofWard.sensitiveAreas())) {
                mergedProofWard.merge(proofWard);
            } else {
                mergedProofWards.add(mergedProofWard);
                mergedProofWard = proofWard;
            }
        }
        mergedProofWards.add(mergedProofWard);
        return mergedProofWards;
    }

    private List<Pair<Date, Date>> obtainPeriods() {
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

        List<Pair<Date, Date>> periods = new ArrayList<>();
        fromDates.stream().sorted(Date::compareTo).forEachOrdered(from -> {
            toDates.stream().filter(toDate -> toDate.compareTo(from) >= 0).sorted(Date::compareTo).findFirst()
                    .ifPresent(toDate -> periods.add(new Pair<>(from, toDate)));
        });
        return periods;
    }
}
