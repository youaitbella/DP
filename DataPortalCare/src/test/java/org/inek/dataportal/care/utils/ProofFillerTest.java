package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.entities.DeptStation;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.Shift;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ProofFillerTest {

    @Test
    public void createProofEntrysFromStationsOneStationTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<DeptStation> stations = new ArrayList<>();
        stations.add(createNewDeptStation("B1", "01", "0120"));

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

        Assertions.assertThat(info.getProofs().stream().findFirst().get().getDeptStation().getDeptName()).isEqualTo("B1");
    }

    @Test
    public void createProofEntrysFromStationsOneStationLeapYearTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<DeptStation> stations = new ArrayList<>();
        stations.add(createNewDeptStation("B1", "01", "0120"));

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

        List<DeptStation> stations = new ArrayList<>();
        stations.add(createNewDeptStation("B1", "01", "0120"));

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

        List<DeptStation> stations = new ArrayList<>();
        stations.add(createNewDeptStation("B1", "01", "0120"));
        stations.add(createNewDeptStation("B2", "01", "0126"));
        stations.add(createNewDeptStation("B4", "01", "0127"));

        ProofFiller.createProofEntrysFromStations(info, stations, 2018, 1);

        Assertions.assertThat(info.getProofs()).hasSize(18);
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getDeptStation().getDeptName() == "B1")).hasSize(6);
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getDeptStation().getDeptName() == "B2")).hasSize(6);
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getDeptStation().getDeptName() == "B4")).hasSize(6);
    }

    private DeptStation createNewDeptStation(String name, String location, String fab) {
        DeptStation station = new DeptStation();
        station.setDeptName(name);
        station.setDeptNumber(fab);
        station.setLocationCode(location);
        return station;
    }

}