package org.inek.dataportal.care.utils;

import javafx.util.Pair;
import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.common.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.inek.dataportal.care.testcommon.WardBuilder.createDeptWard;
import static org.inek.dataportal.common.utils.DateUtils.createDate;


class AggregatedWardsHelperTest {

    @ParameterizedTest
    @CsvSource({"Station A,StationA"
            , "Station              A,Station A"
            , "Station A,Station a"
            , "Station A,Stationa"})
    void stringsAreEqualTestValidValues(String value1, String value2) {
        Assertions.assertThat(AggregatedWardsHelper.stringsAreEqual(value1, value2)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"Station B,StationA"
            , "Station              B,Station A"
            , "Station A,asl dksa jkldsakl dsajkl d"
            , "Station A,A Station"})
    void stringsAreEqualTestNotValidValues(String value1, String value2) {
        Assertions.assertThat(AggregatedWardsHelper.stringsAreEqual(value1, value2)).isFalse();
    }

    @Test
    void evaluate() {
        List<DeptWard> wards = new ArrayList<>();

        wards.add(createDeptWard("Station A", "Fachabteilung 1", 1, 772548, "1300"));
        wards.add(createDeptWard("Station A", "Fachabteilung 12", 1, 772548, "7000"));
        wards.add(createDeptWard("Station A", "Fachabteilung 31", 2, 772548, "1600"));
        wards.add(createDeptWard(createDate(2019, Month.JANUARY, 1), createDate(2019, Month.DECEMBER, 31),
                "Station a", "Fachabteilung 166", 2, 772548, "7000"));
        wards.add(createDeptWard(createDate(2020, Month.JANUARY, 1), DateUtils.MAX_DATE,
                "Station a", "Fachabteilung 166", 2, 772548, "7000"));

        Map<String, List<DeptWard>> wardMap = wards.stream().collect(Collectors.groupingBy(this::getKey));
        Assertions.assertThat(wardMap).hasSize(2);

        for (List<DeptWard> deptWards : wardMap.values()) {
            List<Date> toDates = deptWards.stream().map(w -> w.getValidTo()).distinct().sorted(Date::compareTo).collect(Collectors.toList());
            List<Pair<Date, Date>> fromToDates = new ArrayList<>();
            deptWards.stream().map(w -> w.getValidFrom()).distinct().sorted(Date::compareTo).forEachOrdered(from -> {
                toDates.stream().filter(toDate -> toDate.compareTo(from) >= 0).findFirst().ifPresent(toDate -> fromToDates.add(new Pair<>(from, toDate)));
            });
            System.out.println(fromToDates.size());
        }

    }

    private String getKey(DeptWard ward) {
        String key = ward.getLocationCodeP21()
                + "|" + ward.getLocationText()
                + "|" + ward.getWardName().toLowerCase().replace(" ", "")
                + "|" + (ward.getDept() == null ? "???" : ward.getDept().getDeptArea() == 3 ? "Intensiv" : "Other");
        return key;
    }


    @Test
    void emptyWardsGenereateEmptyList() {
        List<DeptWard> wards = new ArrayList<>();
        List<AggregatedWards> aggregatedWards = new AggregatedWardsHelper().aggregatedWards(wards);
        Assertions.assertThat(aggregatedWards).hasSize(0);
    }

    // temp disabled @Test
    void generateAggregatedWardsFromWardsWithMultipleStationsTest() {
        List<DeptWard> wards = new ArrayList<>();


        DeptWard station1 = createDeptWard("Station A", "Fachabteilung 1", 1, 772548, "1300");
        // todo: station1.setMapVersion(createMapVersion(1));
        station1.setBedCount(20);


        DeptWard station2 = createDeptWard("Station A", "Fachabteilung 19", 1, 772548, "1600");
        // todo: station2.setMapVersion(createMapVersion(2));
        station2.setBedCount(20);


        DeptWard station3 = createDeptWard("Station B", "Fachabteilung Neu", 2, 772548, "5600");
        // todo: station3.setMapVersion(createMapVersion(3));
        station3.setBedCount(30);


        DeptWard station4 = createDeptWard("Station B", "Fachabteilung AltNeu", 2, 772548, "8000");
        // todo: station4.setMapVersion(createMapVersion(4));
        station4.setBedCount(30);

        wards.add(station1);
        wards.add(station2);
        wards.add(station3);
        wards.add(station4);

        List<AggregatedWards> aggregatedWards = new AggregatedWardsHelper().aggregatedWards(wards);

        Assertions.assertThat(aggregatedWards).hasSize(2);

        AggregatedWards aggregatedWard1 = aggregatedWards.get(0);

        Assertions.assertThat(aggregatedWard1.getValidFrom()).isEqualTo(createDate(2019, Month.JANUARY, 1));
        Assertions.assertThat(aggregatedWard1.getValidTo()).isEqualTo(createDate(2019, Month.DECEMBER, 31));
        Assertions.assertThat(aggregatedWard1.getWardName()).isEqualTo("Station A");
        Assertions.assertThat(aggregatedWard1.getDeptNames()).contains("Fachabteilung 1", "Fachabteilung 19");
        Assertions.assertThat(aggregatedWard1.getFabs()).contains("1300", "1600");
        Assertions.assertThat(aggregatedWard1.getLocationCode21()).isEqualTo(1);
        Assertions.assertThat(aggregatedWard1.getLocationCodeVz()).isEqualTo(772548);
        Assertions.assertThat(aggregatedWard1.getBeds()).isEqualTo(20);
        // TODO Prüfen des Sensitiven Bereiches


        AggregatedWards aggregatedWard2 = aggregatedWards.get(1);

        Assertions.assertThat(aggregatedWard2.getValidFrom()).isEqualTo(createDate(2019, Month.JANUARY, 1));
        Assertions.assertThat(aggregatedWard2.getValidTo()).isEqualTo(createDate(2019, Month.DECEMBER, 31));
        Assertions.assertThat(aggregatedWard2.getWardName()).isEqualTo("Station B");
        Assertions.assertThat(aggregatedWard2.getDeptNames()).contains("Fachabteilung Neu", "Fachabteilung AltNeu");
        Assertions.assertThat(aggregatedWard2.getFabs()).contains("5600", "8000");
        Assertions.assertThat(aggregatedWard2.getLocationCode21()).isEqualTo(2);
        Assertions.assertThat(aggregatedWard2.getLocationCodeVz()).isEqualTo(772548);
        Assertions.assertThat(aggregatedWard2.getBeds()).isEqualTo(30);
        // TODO Prüfen des Sensitiven Bereiches

    }



}