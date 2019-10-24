package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.entities.DeptStation;
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
    void groupStationsByNameAndLocationCodesWithMultipleStationsVzTest() {
        List<DeptStation> wards = new ArrayList<>();

        DeptStation station1 = createDeptStation("Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptStation station2 = createDeptStation("Station A", "Fachabteilung 12", 1, 772548, "7000");
        DeptStation station3 = createDeptStation("Station A", "Fachabteilung 31", 1, 772550, "1600");
        DeptStation station4 = createDeptStation("Station a", "Fachabteilung 166", 1, 772550, "7000");

        wards.add(station1);
        wards.add(station2);
        wards.add(station3);
        wards.add(station4);

        List<List<DeptStation>> lists = AggregatedWardsHelper.groupStationsByNameAndLocationCodes(wards);

        Assertions.assertThat(lists).hasSize(2);
        Assertions.assertThat(lists.get(0)).containsExactly(station1, station2);
        Assertions.assertThat(lists.get(1)).containsExactly(station3, station4);
    }

    @Test
    void groupStationsByNameAndLocationCodesWithMultipleStationsP21Test() {
        List<DeptStation> wards = new ArrayList<>();

        DeptStation station1 = createDeptStation("Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptStation station2 = createDeptStation("Station A", "Fachabteilung 12", 1, 772548, "7000");
        DeptStation station3 = createDeptStation("Station A", "Fachabteilung 31", 2, 772548, "1600");
        DeptStation station4 = createDeptStation("Station a", "Fachabteilung 166", 2, 772548, "7000");

        wards.add(station1);
        wards.add(station2);
        wards.add(station3);
        wards.add(station4);

        List<List<DeptStation>> lists = AggregatedWardsHelper.groupStationsByNameAndLocationCodes(wards);

        Assertions.assertThat(lists).hasSize(2);
        Assertions.assertThat(lists.get(0)).containsExactly(station1, station2);
        Assertions.assertThat(lists.get(1)).containsExactly(station3, station4);
    }

    private DeptStation createDeptStation(String name, String deptName, int p21, int vz, String fab) {
        DeptStation station = new DeptStation();
        station.setStationName(name);
        station.setDeptName(deptName);
        station.setLocationCodeP21(p21);
        station.setLocationCodeVz(vz);
        station.setFab(fab);
        return station;
    }

    @Test
    void generateAggregatedWardsFromWardsWithMultipleStationsTest() {
        List<DeptStation> wards = new ArrayList<>();

        WardNumber wardNumber1 = createWardNumber(1);

        DeptStation station1 = createDeptStation("Station A", "Fachabteilung 1", 1, 772548, "1300");
        station1.setValidFrom(createDate(1, Month.JANUARY, 2019));
        station1.setValidTo(createDate(31, Month.DECEMBER, 2019));
        station1.setWardNumber(wardNumber1);
        station1.setMapVersion(createMapVersion(1));
        station1.setBedCount(20);

        WardNumber wardNumber2 = createWardNumber(2);

        DeptStation station2 = createDeptStation("Station A", "Fachabteilung 19", 1, 772548, "1600");
        station2.setValidFrom(createDate(1, Month.JANUARY, 2019));
        station2.setValidTo(createDate(31, Month.DECEMBER, 2019));
        station2.setWardNumber(wardNumber2);
        station2.setMapVersion(createMapVersion(2));
        station2.setBedCount(20);


        WardNumber wardNumber3 = createWardNumber(3);

        DeptStation station3 = createDeptStation("Station B", "Fachabteilung Neu", 2, 772548, "5600");
        station3.setValidFrom(createDate(1, Month.JANUARY, 2019));
        station3.setValidTo(createDate(31, Month.DECEMBER, 2019));
        station3.setWardNumber(wardNumber3);
        station3.setMapVersion(createMapVersion(3));
        station3.setBedCount(30);

        WardNumber wardNumber4 = createWardNumber(4);

        DeptStation station4 = createDeptStation("Station B", "Fachabteilung AltNeu", 2, 772548, "8000");
        station4.setValidFrom(createDate(1, Month.JANUARY, 2019));
        station4.setValidTo(createDate(31, Month.DECEMBER, 2019));
        station4.setWardNumber(wardNumber4);
        station4.setMapVersion(createMapVersion(4));
        station4.setBedCount(30);

        wards.add(station1);
        wards.add(station2);
        wards.add(station3);
        wards.add(station4);

        List<AggregatedWards> aggregatedWards = AggregatedWardsHelper.generateAggregatedWardsFromWards(wards);

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