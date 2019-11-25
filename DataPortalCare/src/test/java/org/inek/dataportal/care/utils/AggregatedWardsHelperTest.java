package org.inek.dataportal.care.utils;

import javafx.util.Pair;
import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.bo.DatePair;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.version.MapVersion;
import org.inek.dataportal.common.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static org.inek.dataportal.common.utils.DateUtils.createDate;
import static org.inek.dataportal.common.utils.DateUtils.getMaxDate;


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

    // temp disabled @Test
    void findAllValidityRangesMultipleRangesTest() {
        List<DeptWard> stations = new ArrayList<>();

        DeptWard station1 = createDeptWard(createDate(2019, Month.JANUARY, 1), createDate(2019, Month.MARCH, 31), "Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptWard(createDate(2019, Month.APRIL, 1), getMaxDate(), "Station A", "Fachabteilung 12", 1, 772548, "5600");
        DeptWard station3 = createDeptWard(createDate(2019, Month.JANUARY, 1), getMaxDate(), "Station A", "Fachabteilung 13", 1, 772548, "1600");
        DeptWard station4 = createDeptWard(createDate(2019, Month.JANUARY, 1), createDate(2020, Month.DECEMBER, 31), "Station A", "Fachabteilung 130", 1, 772548, "7000");

        stations.add(station1);
        stations.add(station2);
        stations.add(station3);
        stations.add(station4);

        Set<DatePair> allValidityRanges = AggregatedWardsHelper.findAllValidityRanges(stations);

        Assertions.assertThat(allValidityRanges).hasSize(3);
        Assertions.assertThat(allValidityRanges).containsExactlyInAnyOrder(new DatePair(createDate(2019, Month.JANUARY, 1), createDate(2019, Month.MARCH, 31))
                , new DatePair(createDate(2019, Month.APRIL, 1), createDate(2020, Month.DECEMBER, 31))
                , new DatePair(createDate(2020, Month.DECEMBER, 31), getMaxDate()));
    }

    // temp disabled @Test
    void findAllValidityRangesMultipleSameRangesTest() {
        List<DeptWard> stations = new ArrayList<>();

        DeptWard station1 = createDeptWard(createDate(2018, Month.JANUARY, 1), createDate(2018, Month.MARCH, 31), "Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptWard(createDate(2018, Month.APRIL, 1), getMaxDate(), "Station A", "Fachabteilung 12", 1, 772548, "5600");
        DeptWard station3 = createDeptWard(createDate(2018, Month.JANUARY, 1), getMaxDate(), "Station A", "Fachabteilung 13", 1, 772548, "1600");
        DeptWard station4 = createDeptWard(createDate(2018, Month.JANUARY, 1), getMaxDate(), "Station A", "Fachabteilung 130", 1, 772548, "7000");

        stations.add(station1);
        stations.add(station2);
        stations.add(station3);
        stations.add(station4);

        Set<DatePair> allValidityRanges = AggregatedWardsHelper.findAllValidityRanges(stations);

        Assertions.assertThat(allValidityRanges).hasSize(2);
        Assertions.assertThat(allValidityRanges).containsExactlyInAnyOrder(new DatePair(createDate(2018, Month.JANUARY, 1), createDate(2018, Month.MARCH, 31))
                , new DatePair(createDate(2018, Month.APRIL, 1), getMaxDate()));
    }

    // temp disabled @Test
    void groupStationListsByValidityMultipleValidityTest() {
        List<List<DeptWard>> lists = new ArrayList<>();

        List<DeptWard> stations = new ArrayList<>();

        DeptWard station1 = createDeptWard(createDate(2018, Month.JANUARY, 1), createDate(2018, Month.MARCH, 31), "Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptWard(createDate(2018, Month.APRIL, 1), getMaxDate(), "Station A", "Fachabteilung 12", 1, 772548, "5600");
        DeptWard station3 = createDeptWard(createDate(2018, Month.JANUARY, 1), getMaxDate(), "Station A", "Fachabteilung 13", 1, 772548, "1600");
        DeptWard station4 = createDeptWard(createDate(2018, Month.JANUARY, 1), getMaxDate(), "Station A", "Fachabteilung 130", 1, 772548, "7000");

        stations.add(station1);
        stations.add(station2);
        stations.add(station3);
        stations.add(station4);

        lists.add(stations);

        List<List<DeptWard>> resultLists = AggregatedWardsHelper.groupStationListsByValidity(lists);
        Assertions.assertThat(resultLists).hasSize(2);
        Assertions.assertThat(resultLists.get(1)).containsExactly(station1, station3, station4);
        Assertions.assertThat(resultLists.get(0)).containsExactly(station2, station3, station4);
    }

    @Test
    void findStationsInDatePairRangeWithSameDateTest() {
        List<DeptWard> stations = new ArrayList<>();

        DeptWard station1 = createDeptWard(createDate(2019, Month.JANUARY, 1), createDate(2019, Month.MARCH, 31), "Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptWard(createDate(2018, Month.APRIL, 1), getMaxDate(), "Station A", "Fachabteilung 12", 1, 772548, "5600");
        DeptWard station3 = createDeptWard(createDate(2019, Month.JANUARY, 1), createDate(2050, Month.AUGUST, 31), "Station A", "Fachabteilung 13", 1, 772548, "1600");
        DeptWard station4 = createDeptWard(createDate(2019, Month.FEBRUARY, 1), getMaxDate(), "Station A", "Fachabteilung 130", 1, 772548, "7000");

        DatePair pair = new DatePair(createDate(2019, Month.JANUARY, 1), createDate(2019, Month.AUGUST, 31));

        stations.add(station1);
        stations.add(station2);
        stations.add(station3);
        stations.add(station4);

        List<DeptWard> stationsInDatePairRange = AggregatedWardsHelper.findStationsInDatePairRange(stations, pair);

        Assertions.assertThat(stationsInDatePairRange).containsExactlyInAnyOrder(station2, station3);
    }

    @Test
    void groupStationsByNameAndLocationCodesWithMultipleStationsVzTest() {
        List<DeptWard> wards = new ArrayList<>();

        DeptWard station1 = createDeptWard("Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptWard("Station A", "Fachabteilung 12", 1, 772548, "7000");
        DeptWard station3 = createDeptWard("Station A", "Fachabteilung 31", 1, 772550, "1600");
        DeptWard station4 = createDeptWard("Station a", "Fachabteilung 166", 1, 772550, "7000");

        wards.add(station1);
        wards.add(station2);
        wards.add(station3);
        wards.add(station4);

        List<List<DeptWard>> lists = AggregatedWardsHelper.groupStationsByNameAndLocationCodes(wards);

        Assertions.assertThat(lists).hasSize(2);
        Assertions.assertThat(lists.get(0)).containsExactly(station1, station2);
        Assertions.assertThat(lists.get(1)).containsExactly(station3, station4);
    }

    @Test
    void groupStationsByNameAndLocationCodesWithMultipleStationsP21Test() {
        List<DeptWard> wards = new ArrayList<>();

        DeptWard station1 = createDeptWard("Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptWard("Station A", "Fachabteilung 12", 1, 772548, "7000");
        DeptWard station3 = createDeptWard("Station A", "Fachabteilung 31", 2, 772548, "1600");
        DeptWard station4 = createDeptWard("Station a", "Fachabteilung 166", 2, 772548, "7000");

        wards.add(station1);
        wards.add(station2);
        wards.add(station3);
        wards.add(station4);

        List<List<DeptWard>> lists = AggregatedWardsHelper.groupStationsByNameAndLocationCodes(wards);

        Assertions.assertThat(lists).hasSize(2);
        Assertions.assertThat(lists.get(0)).containsExactly(station1, station2);
        Assertions.assertThat(lists.get(1)).containsExactly(station3, station4);
    }

    @Test
    void evaluate() {
        List<DeptWard> wards = new ArrayList<>();

        wards.add(createDeptWard("Station A", "Fachabteilung 1", 1, 772548, "1300"));
        wards.add(createDeptWard("Station A", "Fachabteilung 12", 1, 772548, "7000"));
        wards.add(createDeptWard("Station A", "Fachabteilung 31", 2, 772548, "1600"));
        wards.add(createDeptWard(createDate(2019, Month.JANUARY, 1), createDate(2019, Month.DECEMBER, 31),
                "Station a", "Fachabteilung 166", 2, 772548, "7000"));
        wards.add(createDeptWard(createDate(2020, Month.JANUARY, 1), DateUtils.getMaxDate(),
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

    private DeptWard createDeptWard(Date validFrom, Date validTo, String name, String deptName, int p21, int vz, String fab) {
        DeptWard station = new DeptWard(new MapVersion());
        station.setValidFrom(validFrom);
        station.setValidTo(validTo);
        station.setWardName(name);
        station.setDeptName(deptName);
        station.setLocationCodeP21(p21);
        station.setLocationCodeVz(vz);
        station.setFab(fab);
        return station;
    }

    private DeptWard createDeptWard(String name, String deptName, int p21, int vz, String fab) {
        return createDeptWard(createDate(2019, Month.JANUARY, 1), createDate(2019, Month.DECEMBER, 31), name, deptName, p21, vz, fab);
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

        List<AggregatedWards> aggregatedWards = AggregatedWardsHelper.aggregatedWards(wards);

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

    private int versionId;

    private synchronized MapVersion createMapVersion() {
        MapVersion version = new MapVersion(0);
        version.setId(++versionId);
        return version;
    }


}