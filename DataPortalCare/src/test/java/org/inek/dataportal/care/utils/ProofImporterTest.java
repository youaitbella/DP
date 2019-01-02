package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.entities.Proof;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.inek.dataportal.care.entities.ProofRegulationStation;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.SensitiveArea;
import org.inek.dataportal.care.enums.Shift;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


class ProofImporterTest {

    private String FILE_BASE_FOLDER = "src\\test\\resources\\";

    @Test
    void handleProofUploadWrongValuesTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<ProofRegulationStation> stations = new ArrayList<>();

        stations.add(createNewStation(SensitiveArea.GERIATRIE, "0200", "Geriatrie", "G1", "1"));
        stations.add(createNewStation(SensitiveArea.INTENSIVMEDIZIN, "3600", "Intensivmedizin", "INT interdisziplinär", "1"));
        stations.add(createNewStation(SensitiveArea.INTENSIVMEDIZIN, "0100", "Innere Medizin", "K1", "1"));
        stations.add(createNewStation(SensitiveArea.KARDIOLOGIE, "0100", "Innere Medizin", "K1", "1"));

        createProofs(info, stations, 2019, 1);

        Assertions.assertThat(info.getProofs()).hasSize(24);

        File file = new File(FILE_BASE_FOLDER + "ProofExampleWrongValues.xlsx");

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception ex) {

        }

        ProofImporter importer = new ProofImporter();

        Assertions.assertThat(importer.handleProofUpload(info, inputStream)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getNurse() > 0)).isFalse();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getHelpNurse() > 0)).isFalse();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getPatientOccupancy() > 0)).isFalse();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getCountShiftNotRespected() > 0)).isFalse();
    }

    @Test
    void handleProofUploadFullImportTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<ProofRegulationStation> stations = new ArrayList<>();

        stations.add(createNewStation(SensitiveArea.GERIATRIE, "0200", "Geriatrie", "G1", "1"));
        stations.add(createNewStation(SensitiveArea.INTENSIVMEDIZIN, "3600", "Intensivmedizin", "INT interdisziplinär", "1"));
        stations.add(createNewStation(SensitiveArea.INTENSIVMEDIZIN, "0100", "Innere Medizin", "K1", "1"));
        stations.add(createNewStation(SensitiveArea.KARDIOLOGIE, "0100", "Innere Medizin", "K1", "1"));

        createProofs(info, stations, 2019, 1);

        Assertions.assertThat(info.getProofs()).hasSize(24);

        File file = new File(FILE_BASE_FOLDER + "ProofExampleFull.xlsx");

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception ex) {

        }

        ProofImporter importer = new ProofImporter();

        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getNurse() == 0)).hasSize(24);
        Assertions.assertThat(importer.handleProofUpload(info, inputStream)).isTrue();
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getNurse() == 0)).hasSize(0);
    }

    @Test
    void handleProofUploadToManyDecimalPlacesImportTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<ProofRegulationStation> stations = new ArrayList<>();

        stations.add(createNewStation(SensitiveArea.GERIATRIE, "0200", "Geriatrie", "G1", "1"));

        createProofs(info, stations, 2019, 1);
        Assertions.assertThat(info.getProofs()).hasSize(6);

        File file = new File(FILE_BASE_FOLDER + "ProofExampleToManyDecimals.xlsx");

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception ex) {

        }

        ProofImporter importer = new ProofImporter();

        Assertions.assertThat(importer.handleProofUpload(info, inputStream)).isTrue();

        Optional<Proof> proof = info.getProofs().stream()
                .filter(c -> c.getShift() == Shift.DAY)
                .filter(c -> c.getMonth() == Months.JANUARY)
                .findFirst();

        Assertions.assertThat(proof.isPresent()).isTrue();
        Assertions.assertThat(proof.get().getCountShift()).isEqualTo(31);
        Assertions.assertThat(proof.get().getNurse()).isEqualTo(3.56);
        Assertions.assertThat(proof.get().getHelpNurse()).isEqualTo(1.57);
        Assertions.assertThat(proof.get().getPatientOccupancy()).isEqualTo(42.55);
        Assertions.assertThat(proof.get().getCountShiftNotRespected()).isEqualTo(1);
    }

    private ProofRegulationStation createNewStation(SensitiveArea area, String fabNumber, String fabName,
                                                    String stationName, String locationCode) {
        ProofRegulationStation station = new ProofRegulationStation();
        station.setSensitiveArea(area);
        station.setFabNumber(fabNumber);
        station.setFabName(fabName);
        station.setStationName(stationName);
        station.setLocationCode(locationCode);
        return station;
    }

    private void createProofs(ProofRegulationBaseInformation info, List<ProofRegulationStation> stations, int year, int quarter) {
        ProofFiller.createProofEntrysFromStations(info, stations, year, quarter);
    }
}