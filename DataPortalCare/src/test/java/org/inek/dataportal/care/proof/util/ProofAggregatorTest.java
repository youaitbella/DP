package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.proof.ProofWard;
import org.inek.dataportal.care.testcommon.WardBuilder;
import org.inek.dataportal.common.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProofAggregatorTest {

    // todo: use different bed count, check avg,
    // ...
    @Test
    public void aggregateDeptWards() {

        List<DeptWard> wards = createWards();
        List<ProofWard> expected = new ArrayList<>();

        ProofWard proofWard1 = ProofWard.builder()
                .wardName("Station A")
                .locationNumber(772548)
                .from(DateUtils.createDate(2019, 12, 1))
                .to(DateUtils.createDate(2019, 12, 31))
                .addSensitiveArea("Intensiv")
                .addDept("0100")
                .addDept("3600")
                .build();
        expected.add(proofWard1);

        ProofWard proofWard2 = ProofWard.builder()
                .wardName("Station B")
                .locationNumber(772548)
                .from(DateUtils.createDate(2019, 12, 1))
                .to(DateUtils.createDate(2019, 12, 31))
                .addSensitiveArea("Intensiv")
                .addDept("3600")
                .addSensitiveArea("Neuro")
                .addDept("2800")
                .build();
        expected.add(proofWard2);

        List<ProofWard> proofWards = ProofAggregator.aggregateDeptWards(wards, DateUtils.createDate(2019, 12, 1), DateUtils.createDate(2019, 12, 31));

        assertThat(proofWards).isEqualTo(expected);

    }

    @Test
    public void aggregateDeptWards2() {

        List<DeptWard> wards = createWards();
        List<ProofWard> expected = new ArrayList<>();

        ProofWard proofWard1 = ProofWard.builder()
                .wardName("Station A")
                .locationNumber(772548)
                .from(DateUtils.createDate(2020, 1, 1))
                .to(DateUtils.createDate(2020, 1, 31))
                .addSensitiveArea("Intensiv")
                .addDept("0100")
                .addDept("3600")
                .build();
        expected.add(proofWard1);

        ProofWard proofWard2 = ProofWard.builder()
                .wardName("Station B")
                .locationNumber(772548)
                .from(DateUtils.createDate(2020, 1, 1))
                .to(DateUtils.createDate(2020, 1, 31))
                .addSensitiveArea("Neuro")
                .addDept("2800")
                .addDept("2801")
                .build();
        expected.add(proofWard2);

        List<ProofWard> proofWards = ProofAggregator.aggregateDeptWards(wards, DateUtils.createDate(2020, 1, 1), DateUtils.createDate(2020, 1, 31));

        assertThat(proofWards).isEqualTo(expected);
    }

    @Test
    public void aggregateDeptWards3() {

        List<DeptWard> wards = createWards();
        List<ProofWard> expected = new ArrayList<>();

        ProofWard proofWard1 = ProofWard.builder()
                .wardName("Station A")
                .locationNumber(772548)
                .from(DateUtils.createDate(2020, 2, 1))
                .to(DateUtils.createDate(2020, 2, 31))
                .addSensitiveArea("Intensiv")
                .addDept("0100")
                .addDept("3600")
                .build();
        expected.add(proofWard1);

        ProofWard proofWard2 = ProofWard.builder()
                .wardName("Station C")
                .locationNumber(772548)
                .from(DateUtils.createDate(2020, 2, 1))
                .to(DateUtils.createDate(2020, 2, 14))
                .addSensitiveArea("Intensiv")
                .addDept("2801")
                .build();
        expected.add(proofWard2);

        ProofWard proofWard3 = ProofWard.builder()
                .wardName("Station C")
                .locationNumber(772548)
                .from(DateUtils.createDate(2020, 2, 15))
                .to(DateUtils.createDate(2020, 2, 29))
                .addSensitiveArea("Intensiv")
                .addSensitiveArea("Neuro")
                .addDept("2800")
                .addDept("2801")
                .build();
        expected.add(proofWard3);

        List<ProofWard> proofWards = ProofAggregator.aggregateDeptWards(wards, DateUtils.createDate(2020, 2, 1), DateUtils.createDate(2020, 2, 29));
        assertThat(proofWards).isEqualTo(expected);
    }

    private List<DeptWard> createWards() {
        List<DeptWard> wards = new ArrayList<>();
        DeptWard deptWard1 = new WardBuilder("Station A").locationNumber(772548).sensitiveArea("Intensiv").dept("0100").create();
        wards.add(deptWard1);

        DeptWard deptWard2 = new WardBuilder("Station A").locationNumber(772548).sensitiveArea("Intensiv").dept("3600").create();
        wards.add(deptWard2);

        DeptWard deptWard3 = new WardBuilder("Station B").locationNumber(772548).sensitiveArea("Intensiv").dept("3600")
                .validTo(DateUtils.createDate(2019, 12, 31)).create();
        wards.add(deptWard3);

        DeptWard deptWard4 = new WardBuilder("Station B").locationNumber(772548).sensitiveArea("Neuro").dept("2800")
                .validTo(DateUtils.createDate(2020, 1, 15)).create();
        wards.add(deptWard4);

        DeptWard deptWard5 = new WardBuilder("Station B").locationNumber(772548).sensitiveArea("Neuro").dept("2801")
                .validFrom(DateUtils.createDate(2020, 1, 1)).create();
        wards.add(deptWard5);

        DeptWard deptWard6 = new WardBuilder("Station C").locationNumber(772548).sensitiveArea("Neuro").dept("2800")
                .validFrom(DateUtils.createDate(2020, 2, 15)).create();
        wards.add(deptWard6);

        DeptWard deptWard7 = new WardBuilder("Station C").locationNumber(772548).sensitiveArea("Intensiv").dept("2801")
                .validFrom(DateUtils.createDate(2020, 2, 1)).create();
        wards.add(deptWard7);

        return wards;
    }

}
