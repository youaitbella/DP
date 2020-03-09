package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.proof.ProofWardInfo;
import org.inek.dataportal.care.testcommon.WardBuilder;
import org.inek.dataportal.common.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProofAggregatorTest {

    @Test
    public void aggregateDeptWards() {

        List<DeptWard> wards = createWards();
        List<ProofWardInfo> expected = new ArrayList<>();

        ProofWardInfo proofWardInfo1 = ProofWardInfo.builder()
                .wardName("Station A")
                .locationNumber(772548)
                .from(DateUtils.createDate(2019, 12, 1))
                .to(DateUtils.createDate(2019, 12, 31))
                .addSensitiveArea("Intensiv")
                .addDept("0100")
                .addDept("3600")
                .addDeptName("x")
                .build();
        expected.add(proofWardInfo1);

        ProofWardInfo proofWardInfo2 = ProofWardInfo.builder()
                .wardName("Station B")
                .locationNumber(772548)
                .from(DateUtils.createDate(2019, 12, 1))
                .to(DateUtils.createDate(2019, 12, 31))
                .addSensitiveArea("Intensiv")
                .addDept("3600")
                .addSensitiveArea("Neuro")
                .addDept("2800")
                .addDeptName("x")
                .build();
        expected.add(proofWardInfo2);

        List<ProofWardInfo> proofWardInfos = ProofAggregator.aggregateDeptWards(wards, DateUtils.createDate(2019, 12, 1), DateUtils.createDate(2019, 12, 31));
        assertThat(proofWardInfos).containsAll(expected);
    }

    @Test
    public void aggregateDeptWards2() {

        List<DeptWard> wards = createWards();
        List<ProofWardInfo> expected = new ArrayList<>();

        ProofWardInfo proofWardInfo1 = ProofWardInfo.builder()
                .wardName("Station A")
                .locationNumber(772548)
                .from(DateUtils.createDate(2020, 1, 1))
                .to(DateUtils.createDate(2020, 1, 31))
                .addSensitiveArea("Intensiv")
                .addDept("0100")
                .addDept("3600")
                .addDeptName("x")
                .build();
        expected.add(proofWardInfo1);

        ProofWardInfo proofWardInfo2 = ProofWardInfo.builder()
                .wardName("Station B")
                .locationNumber(772548)
                .from(DateUtils.createDate(2020, 1, 1))
                .to(DateUtils.createDate(2020, 1, 31))
                .addSensitiveArea("Neuro")
                .addDept("2800")
                .addDept("2801")
                .addDeptName("x")
                .build();
        expected.add(proofWardInfo2);

        List<ProofWardInfo> proofWardInfos = ProofAggregator.aggregateDeptWards(wards, DateUtils.createDate(2020, 1, 1), DateUtils.createDate(2020, 1, 31));

        assertThat(proofWardInfos).containsAll(expected);
    }

    @Test
    public void aggregateDeptWards3() {

        List<DeptWard> wards = createWards();
        List<ProofWardInfo> expected = new ArrayList<>();

        ProofWardInfo proofWardInfo1 = ProofWardInfo.builder()
                .wardName("Station A")
                .locationNumber(772548)
                .from(DateUtils.createDate(2020, 2, 1))
                .to(DateUtils.createDate(2020, 2, 29))
                .addSensitiveArea("Intensiv")
                .addDept("0100")
                .addDept("3600")
                .addDeptName("x")
                .build();
        expected.add(proofWardInfo1);

        ProofWardInfo proofWardInfo2 = ProofWardInfo.builder()
                .wardName("Station B")
                .locationNumber(772548)
                .from(DateUtils.createDate(2020, 2, 1))
                .to(DateUtils.createDate(2020, 2, 29))
                .addSensitiveArea("Neuro")
                .addDept("2801")
                .addDeptName("x")
                .build();
        expected.add(proofWardInfo2);

        ProofWardInfo proofWardInfo3 = ProofWardInfo.builder()
                .wardName("Station C")
                .locationNumber(772548)
                .from(DateUtils.createDate(2020, 2, 1))
                .to(DateUtils.createDate(2020, 2, 14))
                .addSensitiveArea("Intensiv")
                .addDept("2801")
                .addDeptName("x")
                .beds(10)
                .build();
        expected.add(proofWardInfo3);

        ProofWardInfo proofWardInfo4 = ProofWardInfo.builder()
                .wardName("Station C")
                .locationNumber(772548)
                .from(DateUtils.createDate(2020, 2, 15))
                .to(DateUtils.createDate(2020, 2, 29))
                .addSensitiveArea("Intensiv")
                .addSensitiveArea("Neuro")
                .addDept("2800")
                .addDept("2801")
                .addDeptName("x")
                .beds(10)
                .build();
        expected.add(proofWardInfo4);

        List<ProofWardInfo> proofWardInfos = ProofAggregator.aggregateDeptWards(wards, DateUtils.createDate(2020, 2, 1), DateUtils.createDate(2020, 2, 29));
        assertThat(proofWardInfos).containsAll(expected);
    }

    @Test
    public void aggregateDeptWardsReturnsAverageBeds() {

        List<DeptWard> wards = createWards();

        ProofWardInfo proofWardInfo1 = ProofWardInfo.builder()
                .wardName("Station D")
                .locationNumber(772548)
                .from(DateUtils.createDate(2020, 4, 1))
                .to(DateUtils.createDate(2020, 4, 30))
                .addSensitiveArea("Neuro")
                .addDept("2800")
                .addDept("2801")
                .addDeptName("x")
                .beds(11)
                .build();


        List<ProofWardInfo> proofWardInfos = ProofAggregator.aggregateDeptWards(wards, DateUtils.createDate(2020, 4, 1), DateUtils.createDate(2020, 4, 30));
        assertThat(proofWardInfos).contains(proofWardInfo1);
    }

    @Test
    public void aggregateDeptWardsReturnsAverageBeds2() {

        List<DeptWard> wards = createWards();

        List<ProofWardInfo> proofWardInfos = ProofAggregator.aggregateDeptWards(wards, DateUtils.createDate(2020, 6, 1), DateUtils.createDate(2020, 6, 30));
        double beds = proofWardInfos.stream().filter(w -> w.getWardName().equals("Station E")).findFirst().orElseThrow(() -> new IllegalArgumentException("not found")).getBeds();
        assertThat(beds).isEqualTo(12.5);
    }

    private List<DeptWard> createWards() {
        List<DeptWard> wards = new ArrayList<>();

        DeptWard deptWard1 = new WardBuilder("Station A").locationNumber(772548).sensitiveArea("Intensiv").dept("0100").deptName("x").create();
        wards.add(deptWard1);

        DeptWard deptWard2 = new WardBuilder("Station A").locationNumber(772548).sensitiveArea("Intensiv").dept("3600").deptName("x").create();
        wards.add(deptWard2);

        DeptWard deptWard3 = new WardBuilder("Station B").locationNumber(772548).sensitiveArea("Intensiv").dept("3600").deptName("x")
                .validTo(DateUtils.createDate(2019, 12, 31)).create();
        wards.add(deptWard3);

        DeptWard deptWard4 = new WardBuilder("Station B").locationNumber(772548).sensitiveArea("Neuro").dept("2800").deptName("x")
                .validTo(DateUtils.createDate(2020, 1, 15)).create();
        wards.add(deptWard4);

        DeptWard deptWard5 = new WardBuilder("Station B").locationNumber(772548).sensitiveArea("Neuro").dept("2801").deptName("x")
                .validFrom(DateUtils.createDate(2020, 1, 1)).create();
        wards.add(deptWard5);

        DeptWard deptWard6 = new WardBuilder("Station C").locationNumber(772548).sensitiveArea("Neuro").dept("2800").deptName("x")
                .validFrom(DateUtils.createDate(2020, 2, 15)).bedCount(10).create();
        wards.add(deptWard6);

        DeptWard deptWard7 = new WardBuilder("Station C").locationNumber(772548).sensitiveArea("Intensiv").dept("2801").deptName("x")
                .validFrom(DateUtils.createDate(2020, 2, 1)).bedCount(10).create();
        wards.add(deptWard7);

        DeptWard deptWard8 = new WardBuilder("Station D").locationNumber(772548).sensitiveArea("Neuro").dept("2800").deptName("x")
                .validFrom(DateUtils.createDate(2020, 4, 1))
                .validTo(DateUtils.createDate(2020, 4, 15)).bedCount(10).create();
        wards.add(deptWard8);

        DeptWard deptWard9 = new WardBuilder("Station D").locationNumber(772548).sensitiveArea("Neuro").dept("2801").deptName("x")
                .validFrom(DateUtils.createDate(2020, 4, 16)).bedCount(12).create();
        wards.add(deptWard9);

        DeptWard deptWard10 = new WardBuilder("Station E").locationNumber(772548).sensitiveArea("Neuro").dept("2800").deptName("x")
                .validFrom(DateUtils.createDate(2020, 6, 1))
                .validTo(DateUtils.createDate(2020, 6, 15)).bedCount(10).create();
        wards.add(deptWard10);

        DeptWard deptWard11 = new WardBuilder("Station E").locationNumber(772548).sensitiveArea("Neuro").dept("2801").deptName("x")
                .validFrom(DateUtils.createDate(2020, 6, 16)).bedCount(15).create();
        wards.add(deptWard11);

        return wards;
    }

}
