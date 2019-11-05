package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.bo.DatePair;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.WardNumber;
import org.inek.dataportal.care.entities.version.MapVersion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


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
    void checkBedCountForWardsTest() {
        List<DeptWard> stations = new ArrayList<>();

        DeptWard station1 = createDeptStation("Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptStation("Station A", "Fachabteilung 12", 1, 772548, "7000");
        DeptWard station3 = createDeptStation("Station A", "Fachabteilung 31", 1, 772550, "1600");
        DeptWard station4 = createDeptStation("Station a", "Fachabteilung 166", 1, 772550, "7000");

        station1.setBedCount(20);
        station2.setBedCount(20);
        station3.setBedCount(20);
        station4.setBedCount(20);

        stations.add(station1);
        stations.add(station2);
        stations.add(station3);
        stations.add(station4);

        List<String> errors = AggregatedWardsHelper.checkBedCountForWards(stations);

        Assertions.assertThat(errors).hasSize(0);
    }

    @Test
    void checkBedCountForWards1Test() {
        List<DeptWard> stations = new ArrayList<>();

        DeptWard station1 = createDeptStation("Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptStation("Station A", "Fachabteilung 12", 1, 772548, "7000");
        DeptWard station3 = createDeptStation("Station A", "Fachabteilung 31", 1, 772550, "1600");
        DeptWard station4 = createDeptStation("Station a", "Fachabteilung 166", 1, 772550, "7000");

        station1.setBedCount(20);
        station2.setBedCount(40);
        station3.setBedCount(20);
        station4.setBedCount(20);

        stations.add(station1);
        stations.add(station2);
        stations.add(station3);
        stations.add(station4);

        List<String> errors = AggregatedWardsHelper.checkBedCountForWards(stations);

        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.get(0)).contains("20", "40");
    }

    @Test
    void findAllValidityRangesMultipleRangesTest() {
        List<DeptWard> stations = new ArrayList<>();

        DeptWard station1 = createDeptStation(createDate(1, Month.JANUARY, 2019), createDate(31, Month.MARCH, 2019), "Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptStation(createDate(1, Month.APRIL, 2019), createDate(31, Month.DECEMBER, 2050), "Station A", "Fachabteilung 12", 1, 772548, "5600");
        DeptWard station3 = createDeptStation(createDate(1, Month.JANUARY, 2019), createDate(31, Month.DECEMBER, 2050), "Station A", "Fachabteilung 13", 1, 772548, "1600");
        DeptWard station4 = createDeptStation(createDate(1, Month.JANUARY, 2019), createDate(31, Month.DECEMBER, 2020), "Station A", "Fachabteilung 130", 1, 772548, "7000");

        stations.add(station1);
        stations.add(station2);
        stations.add(station3);
        stations.add(station4);

        Set<DatePair> allValidityRanges = AggregatedWardsHelper.findAllValidityRanges(stations);

        Assertions.assertThat(allValidityRanges).hasSize(3);
        Assertions.assertThat(allValidityRanges).containsExactlyInAnyOrder(new DatePair(createDate(1, Month.JANUARY, 2019), createDate(31, Month.MARCH, 2019))
                , new DatePair(createDate(1, Month.APRIL, 2019), createDate(31, Month.DECEMBER, 2020))
                , new DatePair(createDate(31, Month.DECEMBER, 2020), createDate(31, Month.DECEMBER, 2050)));
    }

    @Test
    void findAllValidityRangesMultipleSameRangesTest() {
        List<DeptWard> stations = new ArrayList<>();

        DeptWard station1 = createDeptStation(createDate(1, Month.JANUARY, 2018), createDate(31, Month.MARCH, 2018), "Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptStation(createDate(1, Month.APRIL, 2018), createDate(31, Month.DECEMBER, 2050), "Station A", "Fachabteilung 12", 1, 772548, "5600");
        DeptWard station3 = createDeptStation(createDate(1, Month.JANUARY, 2018), createDate(31, Month.DECEMBER, 2050), "Station A", "Fachabteilung 13", 1, 772548, "1600");
        DeptWard station4 = createDeptStation(createDate(1, Month.JANUARY, 2018), createDate(31, Month.DECEMBER, 2050), "Station A", "Fachabteilung 130", 1, 772548, "7000");

        stations.add(station1);
        stations.add(station2);
        stations.add(station3);
        stations.add(station4);

        Set<DatePair> allValidityRanges = AggregatedWardsHelper.findAllValidityRanges(stations);

        Assertions.assertThat(allValidityRanges).hasSize(2);
        Assertions.assertThat(allValidityRanges).containsExactlyInAnyOrder(new DatePair(createDate(1, Month.JANUARY, 2018), createDate(31, Month.MARCH, 2018))
                , new DatePair(createDate(1, Month.APRIL, 2018), createDate(31, Month.DECEMBER, 2050)));
    }

    @Test
    void groupStationListsByValidityMultipleValidityTest() {
        List<List<DeptWard>> lists = new ArrayList<>();

        List<DeptWard> stations = new ArrayList<>();

        DeptWard station1 = createDeptStation(createDate(1, Month.JANUARY, 2018), createDate(31, Month.MARCH, 2018), "Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptStation(createDate(1, Month.APRIL, 2018), createDate(31, Month.DECEMBER, 2050), "Station A", "Fachabteilung 12", 1, 772548, "5600");
        DeptWard station3 = createDeptStation(createDate(1, Month.JANUARY, 2018), createDate(31, Month.DECEMBER, 2050), "Station A", "Fachabteilung 13", 1, 772548, "1600");
        DeptWard station4 = createDeptStation(createDate(1, Month.JANUARY, 2018), createDate(31, Month.DECEMBER, 2050), "Station A", "Fachabteilung 130", 1, 772548, "7000");

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

        DeptWard station1 = createDeptStation(createDate(1, Month.JANUARY, 2019), createDate(31, Month.MARCH, 2019), "Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptStation(createDate(1, Month.APRIL, 2018), createDate(31, Month.DECEMBER, 2050), "Station A", "Fachabteilung 12", 1, 772548, "5600");
        DeptWard station3 = createDeptStation(createDate(1, Month.JANUARY, 2019), createDate(31, Month.AUGUST, 2050), "Station A", "Fachabteilung 13", 1, 772548, "1600");
        DeptWard station4 = createDeptStation(createDate(1, Month.FEBRUARY, 2019), createDate(31, Month.DECEMBER, 2050), "Station A", "Fachabteilung 130", 1, 772548, "7000");

        DatePair pair = new DatePair(createDate(1, Month.JANUARY, 2019), createDate(31, Month.AUGUST, 2019));

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

        DeptWard station1 = createDeptStation("Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptStation("Station A", "Fachabteilung 12", 1, 772548, "7000");
        DeptWard station3 = createDeptStation("Station A", "Fachabteilung 31", 1, 772550, "1600");
        DeptWard station4 = createDeptStation("Station a", "Fachabteilung 166", 1, 772550, "7000");

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

        DeptWard station1 = createDeptStation("Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard station2 = createDeptStation("Station A", "Fachabteilung 12", 1, 772548, "7000");
        DeptWard station3 = createDeptStation("Station A", "Fachabteilung 31", 2, 772548, "1600");
        DeptWard station4 = createDeptStation("Station a", "Fachabteilung 166", 2, 772548, "7000");

        wards.add(station1);
        wards.add(station2);
        wards.add(station3);
        wards.add(station4);

        List<List<DeptWard>> lists = AggregatedWardsHelper.groupStationsByNameAndLocationCodes(wards);

        Assertions.assertThat(lists).hasSize(2);
        Assertions.assertThat(lists.get(0)).containsExactly(station1, station2);
        Assertions.assertThat(lists.get(1)).containsExactly(station3, station4);
    }

    private DeptWard createDeptStation(Date validFrom, Date validTo, String name, String deptName, int p21, int vz, String fab) {
        DeptWard station = new DeptWard();
        station.setValidFrom(validFrom);
        station.setValidTo(validTo);
        station.setStationName(name);
        station.setDeptName(deptName);
        station.setLocationCodeP21(p21);
        station.setLocationCodeVz(vz);
        station.setFab(fab);
        return station;
    }

    private DeptWard createDeptStation(String name, String deptName, int p21, int vz, String fab) {
        return createDeptStation(createDate(1, Month.JANUARY, 2019), createDate(31, Month.DECEMBER, 2019), name, deptName, p21, vz, fab);
    }

    @Test
    void generateAggregatedWardsFromWardsWithMultipleStationsTest() {
        List<DeptWard> wards = new ArrayList<>();

        WardNumber wardNumber1 = createWardNumber(1);

        DeptWard station1 = createDeptStation("Station A", "Fachabteilung 1", 1, 772548, "1300");
        station1.setWardNumber(wardNumber1);
        station1.setMapVersion(createMapVersion(1));
        station1.setBedCount(20);

        WardNumber wardNumber2 = createWardNumber(2);

        DeptWard station2 = createDeptStation("Station A", "Fachabteilung 19", 1, 772548, "1600");
        station2.setWardNumber(wardNumber2);
        station2.setMapVersion(createMapVersion(2));
        station2.setBedCount(20);


        WardNumber wardNumber3 = createWardNumber(3);

        DeptWard station3 = createDeptStation("Station B", "Fachabteilung Neu", 2, 772548, "5600");
        station3.setWardNumber(wardNumber3);
        station3.setMapVersion(createMapVersion(3));
        station3.setBedCount(30);

        WardNumber wardNumber4 = createWardNumber(4);

        DeptWard station4 = createDeptStation("Station B", "Fachabteilung AltNeu", 2, 772548, "8000");
        station4.setWardNumber(wardNumber4);
        station4.setMapVersion(createMapVersion(4));
        station4.setBedCount(30);

        wards.add(station1);
        wards.add(station2);
        wards.add(station3);
        wards.add(station4);

        List<AggregatedWards> aggregatedWards = AggregatedWardsHelper.aggregatedWards(wards);

        Assertions.assertThat(aggregatedWards).hasSize(2);

        AggregatedWards aggregatedWard1 = aggregatedWards.get(0);

        Assertions.assertThat(aggregatedWard1.getValidFrom()).isEqualTo(createDate(1, Month.JANUARY, 2019));
        Assertions.assertThat(aggregatedWard1.getValidTo()).isEqualTo(createDate(31, Month.DECEMBER, 2019));
        Assertions.assertThat(aggregatedWard1.getWardName()).isEqualTo("Station A");
        Assertions.assertThat(aggregatedWard1.getDeptNames()).contains("Fachabteilung 1", "Fachabteilung 19");
        Assertions.assertThat(aggregatedWard1.getFabs()).contains("1300", "1600");
        Assertions.assertThat(aggregatedWard1.getLocationCode21()).isEqualTo(1);
        Assertions.assertThat(aggregatedWard1.getLocationCodeVz()).isEqualTo(772548);
        Assertions.assertThat(aggregatedWard1.getBeds()).isEqualTo(20);
        // TODO Prüfen des Sensitiven Bereiches


        AggregatedWards aggregatedWard2 = aggregatedWards.get(1);

        Assertions.assertThat(aggregatedWard2.getValidFrom()).isEqualTo(createDate(1, Month.JANUARY, 2019));
        Assertions.assertThat(aggregatedWard2.getValidTo()).isEqualTo(createDate(31, Month.DECEMBER, 2019));
        Assertions.assertThat(aggregatedWard2.getWardName()).isEqualTo("Station B");
        Assertions.assertThat(aggregatedWard2.getDeptNames()).contains("Fachabteilung Neu", "Fachabteilung AltNeu");
        Assertions.assertThat(aggregatedWard2.getFabs()).contains("5600", "8000");
        Assertions.assertThat(aggregatedWard2.getLocationCode21()).isEqualTo(2);
        Assertions.assertThat(aggregatedWard2.getLocationCodeVz()).isEqualTo(772548);
        Assertions.assertThat(aggregatedWard2.getBeds()).isEqualTo(30);
        // TODO Prüfen des Sensitiven Bereiches

    }

    private MapVersion createMapVersion(int i) {
        MapVersion version = new MapVersion();
        version.setId(i);
        return version;
    }

    private WardNumber createWardNumber(int i) {
        WardNumber wardNumber = new WardNumber();
        wardNumber.setId(i);
        return wardNumber;
    }

    private Date createDate(int day, Month month, int year) {
        return createDate(day, month, year, 1, 1, 1);
    }

    private Date createDate(int day, Month month, int year, int hour, int minute, int second) {
        LocalDateTime datetime = LocalDateTime.of(year, month, day, hour, minute, second);
        return java.util.Date.from(datetime
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }


}