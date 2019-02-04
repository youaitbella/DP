package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.inek.dataportal.care.entities.ProofRegulationStation;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.Shift;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ProofFillerTest {

    @Test
    public void createProofEntrysFromStationsOneStationTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<Months> months = new ArrayList<>();
        months.add(Months.JANUARY);
        months.add(Months.FEBRUARY);
        months.add(Months.MARCH);

        List<ProofRegulationStation> stations = new ArrayList<>();
        stations.add(createNewStation("B1", "01", "0120", months));

        ProofFiller.createProofEntrysFromStations(info, stations, 2018, 1);

        Assertions.assertThat(info.getProofs()).hasSize(6);
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getMonth() == Months.JANUARY
                && c.getShift() == Shift.DAY)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getMonth() == Months.JANUARY
                && c.getShift() == Shift.NIGHT)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getMonth() == Months.FEBRUARY
                && c.getShift() == Shift.DAY)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getMonth() == Months.FEBRUARY
                && c.getShift() == Shift.NIGHT)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getMonth() == Months.MARCH
                && c.getShift() == Shift.DAY)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getMonth() == Months.MARCH
                && c.getShift() == Shift.NIGHT)).isTrue();

        Assertions.assertThat(info.getProofs().stream().findFirst().get().getProofRegulationStation().getFabName()).isEqualTo("B1");
    }

    @Test
    public void createProofEntrysFromStationsOneStationLeapYearTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<Months> months = new ArrayList<>();
        months.add(Months.JANUARY);
        months.add(Months.FEBRUARY);
        months.add(Months.MARCH);

        List<ProofRegulationStation> stations = new ArrayList<>();
        stations.add(createNewStation("B1", "01", "0120", months));

        ProofFiller.createProofEntrysFromStations(info, stations, 2008, 1);

        Assertions.assertThat(info.getProofs()).hasSize(6);
        Assertions.assertThat(info.getProofs().stream()
                .filter(c -> c.getMonth() == Months.FEBRUARY).findFirst().get().getCountShift()).isEqualTo(29);
        Assertions.assertThat(info.getProofs().stream()
                .filter(c -> c.getMonth() == Months.JANUARY).findFirst().get().getCountShift()).isEqualTo(31);
    }

    @Test
    public void createProofEntrysFromStationsOneStationNoLeapYearTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<Months> months = new ArrayList<>();
        months.add(Months.JANUARY);
        months.add(Months.FEBRUARY);
        months.add(Months.MARCH);

        List<ProofRegulationStation> stations = new ArrayList<>();
        stations.add(createNewStation("B1", "01", "0120", months));

        ProofFiller.createProofEntrysFromStations(info, stations, 2009, 1);

        Assertions.assertThat(info.getProofs()).hasSize(6);
        Assertions.assertThat(info.getProofs().stream()
                .filter(c -> c.getMonth() == Months.FEBRUARY).findFirst().get().getCountShift()).isEqualTo(28);
        Assertions.assertThat(info.getProofs().stream()
                .filter(c -> c.getMonth() == Months.JANUARY).findFirst().get().getCountShift()).isEqualTo(31);
    }

    @Test
    public void createProofEntrysFromStationsMultipleStationTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<Months> months = new ArrayList<>();
        months.add(Months.JANUARY);
        months.add(Months.FEBRUARY);
        months.add(Months.MARCH);

        List<ProofRegulationStation> stations = new ArrayList<>();
        stations.add(createNewStation("B1", "01", "0120", months));
        stations.add(createNewStation("B2", "01", "0126", months));
        stations.add(createNewStation("B4", "01", "0127", months));

        ProofFiller.createProofEntrysFromStations(info, stations, 2018, 1);

        Assertions.assertThat(info.getProofs()).hasSize(18);
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getProofRegulationStation().getFabName().equals("B1"))).hasSize(6);
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getProofRegulationStation().getFabName().equals("B2"))).hasSize(6);
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getProofRegulationStation().getFabName().equals("B4"))).hasSize(6);
    }

    @Test
    public void createProofEntrysFromStationsWithTwoValidMonthTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<Months> months = new ArrayList<>();
        months.add(Months.JANUARY);
        months.add(Months.FEBRUARY);

        List<ProofRegulationStation> stations = new ArrayList<>();
        stations.add(createNewStation("B1", "01", "0120", months));

        ProofFiller.createProofEntrysFromStations(info, stations, 2018, 1);

        Assertions.assertThat(info.getProofs()).hasSize(4);
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getMonth().equals(Months.MARCH))).hasSize(0);
    }

    @Test
    public void createProofEntrysFromStationsWithNoValidMonthTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<Months> months = new ArrayList<>();
        months.add(Months.DECEMBER);
        months.add(Months.NOVEMBER);
        months.add(Months.OCTOBER);

        List<ProofRegulationStation> stations = new ArrayList<>();
        stations.add(createNewStation("B1", "01", "0120", months));

        ProofFiller.createProofEntrysFromStations(info, stations, 2018, 1);

        Assertions.assertThat(info.getProofs()).hasSize(0);
    }

    @Test
    public void createProofEntrysFromStationsMultipleStationDiffernetMonthsTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<Months> months = new ArrayList<>();
        months.add(Months.JANUARY);
        months.add(Months.FEBRUARY);
        months.add(Months.MARCH);

        List<ProofRegulationStation> stations = new ArrayList<>();
        stations.add(createNewStation("B1", "01", "0120", months));
        stations.add(createNewStation("B2", "01", "0126", months));

        List<Months> months2 = new ArrayList<>();
        months2.add(Months.JANUARY);
        months2.add(Months.FEBRUARY);

        stations.add(createNewStation("B4", "01", "0127", months2));

        ProofFiller.createProofEntrysFromStations(info, stations, 2018, 1);

        Assertions.assertThat(info.getProofs()).hasSize(16);
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getProofRegulationStation().getFabName().equals("B1"))).hasSize(6);
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getProofRegulationStation().getFabName().equals("B2"))).hasSize(6);
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getProofRegulationStation().getFabName().equals("B4"))).hasSize(4);
    }

    private ProofRegulationStation createNewStation(String name, String location, String fab, List<Months> months) {
        ProofRegulationStation station = new ProofRegulationStation();
        station.setFabName(name);
        station.setFabNumber(fab);
        station.setLocationCode(location);
        for(Months month : months) {
            station.addNewValideMonth(month);
        }
        return station;
    }

}