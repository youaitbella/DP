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

/*

    DeptWard deptWard1 = new WardBuilder("Station A").vz(772548).sensitiveArea("Intensiv").fab("0100").create();
        wards.add(deptWard1);

    DeptWard deptWard2 = new WardBuilder("Station A").vz(772548).sensitiveArea("Intensiv").fab("3600").create();
        wards.add(deptWard2);

    DeptWard deptWard3 = new WardBuilder("Station B").vz(772548).sensitiveArea("Intensiv").fab("3600")
            .validTo(DateUtils.createDate(2019,12,31))
*/

    @Test
    public void testAggregateDeptWards() {

        List<DeptWard> wards = createWards();
        List<ProofWard> expected = new ArrayList<>();

        ProofWard proofWard1 = new ProofWard();

        proofWard1.setWardName("Station A");
        proofWard1.setFrom(DateUtils.createDate(2019, 12, 1));
        proofWard1.setTo(DateUtils.createDate(2019, 12, 31));
        proofWard1.setLocationNumber(772548);
        proofWard1.addSensitiveArea("Intensiv");
        proofWard1.addDept("0100");
        proofWard1.addDept("3600");
        expected.add(proofWard1);

        ProofWard proofWard2 = new ProofWard();

        proofWard2.setWardName("Station B");
        proofWard2.setFrom(DateUtils.createDate(2019, 12, 1));
        proofWard2.setTo(DateUtils.createDate(2019, 12, 31));
        proofWard2.setLocationNumber(772548);
        proofWard2.addSensitiveArea("Intensiv");
        proofWard2.addDept("3600");
        expected.add(proofWard2);

        List<ProofWard> proofWards = ProofAggregator.aggregateDeptWards(wards, DateUtils.createDate(2019, 12, 1), DateUtils.createDate(2019, 12, 31));

        assertThat(proofWards).isEqualTo(expected);

    }

    @Test
    public void testAggregateDeptWards2() {

        List<DeptWard> wards = createWards();

        List<ProofWard> proofWards = ProofAggregator.aggregateDeptWards(wards, DateUtils.createDate(2020, 1, 1), DateUtils.createDate(2020, 1, 31));

    }

    @Test
    public void testAggregateDeptWards3() {

        List<DeptWard> wards = createWards();

        List<ProofWard> proofWards = ProofAggregator.aggregateDeptWards(wards, DateUtils.createDate(2020, 2, 1), DateUtils.createDate(2020, 2, 29));
        List<ProofWard> expected = new ArrayList<>();
        assertThat(proofWards).isEqualTo(expected);
    }

    private List<DeptWard> createWards() {
        List<DeptWard> wards = new ArrayList<>();
        DeptWard deptWard1 = new WardBuilder("Station A").vz(772548).sensitiveArea("Intensiv").fab("0100").create();
        wards.add(deptWard1);

        DeptWard deptWard2 = new WardBuilder("Station A").vz(772548).sensitiveArea("Intensiv").fab("3600").create();
        wards.add(deptWard2);

        DeptWard deptWard3 = new WardBuilder("Station B").vz(772548).sensitiveArea("Intensiv").fab("3600")
                .validTo(DateUtils.createDate(2019, 12, 31)).create();
        wards.add(deptWard3);

        DeptWard deptWard4 = new WardBuilder("Station B").vz(772548).sensitiveArea("Neuro").fab("2800")
                .validTo(DateUtils.createDate(2020, 1, 15)).create();
        wards.add(deptWard4);

        DeptWard deptWard5 = new WardBuilder("Station B").vz(772548).sensitiveArea("Neuro").fab("2801")
                .validFrom(DateUtils.createDate(2020, 1, 1)).create();
        wards.add(deptWard5);

        DeptWard deptWard6 = new WardBuilder("Station C").vz(772548).sensitiveArea("Neuro").fab("2800")
                .validFrom(DateUtils.createDate(2020, 2, 15)).create();
        wards.add(deptWard6);

        DeptWard deptWard7 = new WardBuilder("Station C").vz(772548).sensitiveArea("Intensiv").fab("2801")
                .validFrom(DateUtils.createDate(2020, 2, 1)).create();
        wards.add(deptWard7);

        return wards;
    }

}
