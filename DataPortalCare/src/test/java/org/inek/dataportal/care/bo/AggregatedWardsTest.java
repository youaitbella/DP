package org.inek.dataportal.care.bo;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.version.MapVersion;
import org.junit.jupiter.api.Test;

import java.time.Month;

import static org.inek.dataportal.common.utils.DateUtils.createDate;
import static org.inek.dataportal.common.utils.DateUtils.getMaxDate;

class AggregatedWardsTest {

    @Test
    void distinctBedCountsReturnsOneValueIfAllTheSame() {

        AggregatedWards wards = new AggregatedWards(createDeptWard("Station A", "Fachabteilung 1", 1, 771234000, "1000", 50));
        wards.aggregate(createDeptWard("Station A", "Fachabteilung 2", 1, 771234000, "2000", 50));
        wards.aggregate(createDeptWard("Station A", "Fachabteilung 3", 1, 771234000, "3000", 50));

        Assertions.assertThat(wards.getDistinctBedCounts()).hasSize(1).containsOnly(50);
    }

    @Test
    void distinctBedCountsReturnsTwoValueIfTwoDifferentBedCountsProvided() {

        AggregatedWards wards = new AggregatedWards(createDeptWard("Station A", "Fachabteilung 1", 1, 771234000, "1000", 50));
        wards.aggregate(createDeptWard("Station A", "Fachabteilung 2", 1, 771234000, "2000", 50));
        wards.aggregate(createDeptWard("Station A", "Fachabteilung 3", 1, 771234000, "3000", 55));

        Assertions.assertThat(wards.getDistinctBedCounts()).hasSize(2).containsExactly(50, 55);
    }

    private DeptWard createDeptWard(String name, String deptName, int p21, int vz, String fab, int bedCount) {
        DeptWard deptWard = new DeptWard(new MapVersion());
        deptWard.setValidFrom(createDate(2020, Month.JANUARY, 1));
        deptWard.setValidTo(getMaxDate());
        deptWard.setWardName(name);
        deptWard.setDeptName(deptName);
        deptWard.setLocationCodeP21(p21);
        deptWard.setLocationCodeVz(vz);
        deptWard.setFab(fab);
        deptWard.setBedCount(bedCount);
        return deptWard;
    }


}