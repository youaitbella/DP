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


class CI_ProofImporterTest {

    private String FILE_BASE_FOLDER = "src\\test\\resources\\";

    @Test
    void handleProofUploadToHighValuesValuesTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<ProofRegulationStation> stations = new ArrayList<>();

        stations.add(createNewStation(SensitiveArea.GERIATRIE, "0200", "Geriatrie", "G1", "1"));

        createProofs(info, stations, 2019, 1);

        Assertions.assertThat(info.getProofs()).hasSize(6);

        InputStream inputStream = getInputStreamFromExcel("ProofExampleWrongValues.xlsx");

        ProofImporter importer = new ProofImporter(false);

        Assertions.assertThat(importer.handleProofUpload(info, inputStream)).isTrue();
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getMonth().equals(Months.JANUARY) && c.getShift().equals(Shift.NIGHT))
                .findFirst().get().getCountShift()).isEqualTo(31);
        Assertions.assertThat(importer.getMessage()).contains("Die Anzahl der Schichten in Zelle H3 ist unplausibel");
    }

    @Test
    void handleProofUploadWrongValuesTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<ProofRegulationStation> stations = new ArrayList<>();

        stations.add(createNewStation(SensitiveArea.GERIATRIE, "0200", "Geriatrie", "G1", "1"));

        createProofs(info, stations, 2019, 1);

        Assertions.assertThat(info.getProofs()).hasSize(6);

        InputStream inputStream = getInputStreamFromExcel("ProofExampleWrongValues.xlsx");

        ProofImporter importer = new ProofImporter(false);

        Assertions.assertThat(importer.handleProofUpload(info, inputStream)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getNurse() > 0)).isFalse();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getHelpNurse() > 0)).isFalse();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getPatientOccupancy() > 0)).isFalse();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getCountShiftNotRespected() > 0)).isFalse();
    }

    @Test
    void handleProofUploadNumerAsTextTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<ProofRegulationStation> stations = new ArrayList<>();

        stations.add(createNewStation(SensitiveArea.UNFALLCHIRUGIE, "1600", "Unfallchirurgie", "ST2A Station 2A - Unfallchirurgie", ""));

        createProofs(info, stations, 2019, 1);

        Assertions.assertThat(info.getProofs()).hasSize(6);


        InputStream inputStream = getInputStreamFromExcel("ProofExampleNumberAsText.xlsx");

        ProofImporter importer = new ProofImporter(false);

        Assertions.assertThat(importer.handleProofUpload(info, inputStream)).isTrue();
        Assertions.assertThat(importer.getMessage()).isEqualTo("Zeilen erfolgreich eingelesen: 2\n");
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getNurse() == 2.14)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getHelpNurse() == 0.33)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getPatientOccupancy() == 39.71)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getCountShiftNotRespected() == 31)).isTrue();

        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getNurse() == 1.19)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getHelpNurse() == 0)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getPatientOccupancy() == 39.97)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getCountShiftNotRespected() == 30)).isTrue();

    }

    private InputStream getInputStreamFromExcel(String excelFile) {
        File file = new File(FILE_BASE_FOLDER + excelFile);

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception ex) {

        }
        return inputStream;
    }

    @Test
    void handleProofUploadEmptyFabNumberTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<ProofRegulationStation> stations = new ArrayList<>();

        stations.add(createNewStation(SensitiveArea.GERIATRIE, "", "Geriatrie", "G1", "1"));

        createProofs(info, stations, 2019, 1);

        Assertions.assertThat(info.getProofs()).hasSize(6);

        InputStream inputStream = getInputStreamFromExcel("ProofExampleEmptyFabNumber.xlsx");

        ProofImporter importer = new ProofImporter(false);

        Assertions.assertThat(importer.handleProofUpload(info, inputStream)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getNurse() > 0)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getHelpNurse() > 0)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getPatientOccupancy() > 0)).isTrue();
        Assertions.assertThat(info.getProofs().stream().anyMatch(c -> c.getCountShiftNotRespected() > 0)).isTrue();
    }

    @Test
    void handleProofUploadWrongCellLengthTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<ProofRegulationStation> stations = new ArrayList<>();

        stations.add(createNewStation(SensitiveArea.GERIATRIE, "0200", "Geriatrie", "G1", "1"));

        createProofs(info, stations, 2019, 1);

        Assertions.assertThat(info.getProofs()).hasSize(6);

        InputStream inputStream = getInputStreamFromExcel("ProofExampleWrongCellLength.xlsx");

        ProofImporter importer = new ProofImporter(false);

        Assertions.assertThat(importer.handleProofUpload(info, inputStream)).isFalse();
        Assertions.assertThat(importer.getMessage()).contains("Nicht genug Spalten in Zeile 1", "Nicht genug Spalten in Zeile 2");
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

        InputStream inputStream = getInputStreamFromExcel("ProofExampleFull.xlsx");

        ProofImporter importer = new ProofImporter(false);

        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getNurse() == 0)).hasSize(24);
        Assertions.assertThat(importer.handleProofUpload(info, inputStream)).isTrue();
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getNurse() == 0)).hasSize(0);
    }

    @Test
    void handleProofUploadFullImportWithCommentTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<ProofRegulationStation> stations = new ArrayList<>();

        stations.add(createNewStation(SensitiveArea.GERIATRIE, "0200", "Geriatrie", "G1", "1"));
        stations.add(createNewStation(SensitiveArea.INTENSIVMEDIZIN, "3600", "Intensivmedizin", "INT interdisziplinär", "1"));
        stations.add(createNewStation(SensitiveArea.INTENSIVMEDIZIN, "0100", "Innere Medizin", "K1", "1"));
        stations.add(createNewStation(SensitiveArea.KARDIOLOGIE, "0100", "Innere Medizin", "K1", "1"));

        createProofs(info, stations, 2019, 1);

        Assertions.assertThat(info.getProofs()).hasSize(24);

        InputStream inputStream = getInputStreamFromExcel("ProofExampleFullWithComment.xlsx");

        ProofImporter importer = new ProofImporter(true);

        Assertions.assertThat(importer.handleProofUpload(info, inputStream)).isTrue();
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getComment().equals(""))).hasSize(22);
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getComment().equals("Kommentar mit Sonderzeichen !\"§$%&/()"))).hasSize(1);
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getComment().equals("Keinen Kommentar, Aber andere Anmerkungen"))).hasSize(1);
    }

    @Test
    void handleProofUploadFullImportWithCommentNoBwHospitalTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<ProofRegulationStation> stations = new ArrayList<>();

        stations.add(createNewStation(SensitiveArea.GERIATRIE, "0200", "Geriatrie", "G1", "1"));
        stations.add(createNewStation(SensitiveArea.INTENSIVMEDIZIN, "3600", "Intensivmedizin", "INT interdisziplinär", "1"));
        stations.add(createNewStation(SensitiveArea.INTENSIVMEDIZIN, "0100", "Innere Medizin", "K1", "1"));
        stations.add(createNewStation(SensitiveArea.KARDIOLOGIE, "0100", "Innere Medizin", "K1", "1"));

        createProofs(info, stations, 2019, 1);

        Assertions.assertThat(info.getProofs()).hasSize(24);

        InputStream inputStream = getInputStreamFromExcel("ProofExampleFullWithComment.xlsx");

        ProofImporter importer = new ProofImporter(false);

        Assertions.assertThat(importer.handleProofUpload(info, inputStream)).isTrue();
        Assertions.assertThat(info.getProofs().stream().filter(c -> c.getComment().equals(""))).hasSize(24);
        Assertions.assertThat(importer.getMessage()).isNotEqualTo("");
    }

    @Test
    void handleProofUploadToManyDecimalPlacesImportTest() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();

        List<ProofRegulationStation> stations = new ArrayList<>();

        stations.add(createNewStation(SensitiveArea.GERIATRIE, "0200", "Geriatrie", "G1", "1"));

        createProofs(info, stations, 2019, 1);
        Assertions.assertThat(info.getProofs()).hasSize(6);

        InputStream inputStream = getInputStreamFromExcel("ProofExampleToManyDecimals.xlsx");

        ProofImporter importer = new ProofImporter(false);

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
        List<Months> months = new ArrayList<>();
        months.add(Months.JANUARY);
        months.add(Months.FEBRUARY);
        months.add(Months.MARCH);

        ProofRegulationStation station = new ProofRegulationStation();
        station.setSensitiveArea(area);
        station.setFabNumber(fabNumber);
        station.setFabName(fabName);
        station.setStationName(stationName);
        station.setLocationCode(locationCode);
        for (Months month : months) {
            station.addNewValideMonth(month);
        }
        return station;
    }

    private void createProofs(ProofRegulationBaseInformation info, List<ProofRegulationStation> stations, int year, int quarter) {
        ProofFiller.createProofEntrysFromStations(info, stations, year, quarter);
    }
}