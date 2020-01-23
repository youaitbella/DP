package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.common.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ProofAggregatorTest {


    @Test
    public void testAggregateDeptWards() {

        List<DeptWard> wards = createWards();


        ProofAggregator.aggregateDeptWards(wards, DateUtils.createDate(2020, 1, 1), DateUtils.createDate(2020, 1, 31));

    }

    private List<DeptWard> createWards() {
        List<DeptWard> wards = new ArrayList<>();
        DeptWard deptWard = new DeptWard();

        wards.add(deptWard);

        return wards;
    }

}
