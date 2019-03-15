package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.inek.dataportal.care.entities.ProofRegulationStation;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.SensitiveArea;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CareSignatureCreaterTest {

    @Test
    void createSignatureDifferentIkTest() {
        ProofRegulationBaseInformation info = createNewBaseInformation();

        String signature = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature).startsWith("PV");

        info.setIk(222222223);
        String signature2 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature2).startsWith("PV").isNotEqualTo(signature);

        info.setIk(222222222);
        String signature3 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature3).startsWith("PV").isEqualTo(signature);
    }

    @Test
    void createSignatureDifferentYearTest() {
        ProofRegulationBaseInformation info = createNewBaseInformation();

        String signature = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature).startsWith("PV");

        info.setYear(2019);
        String signature2 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature2).startsWith("PV").isNotEqualTo(signature);

        info.setYear(2018);
        String signature3 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature3).startsWith("PV").isEqualTo(signature);
    }

    @Test
    void createSignatureDifferentQuarderTest() {
        ProofRegulationBaseInformation info = createNewBaseInformation();

        String signature = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature).startsWith("PV");

        info.setQuarter(2);
        String signature2 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature2).startsWith("PV").isNotEqualTo(signature);

        info.setQuarter(1);
        String signature3 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature3).startsWith("PV").isEqualTo(signature);
    }

    @Test
    void createSignatureDifferentCountShiftTest() {
        ProofRegulationBaseInformation info = createNewBaseInformation();

        info.getProofs().get(0).setCountShift(10);
        String signature = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature).startsWith("PV");

        info.getProofs().get(0).setCountShift(20);
        String signature2 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature2).startsWith("PV").isNotEqualTo(signature);

        info.getProofs().get(0).setCountShift(10);
        String signature3 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature3).startsWith("PV").isEqualTo(signature);
    }

    @Test
    void createSignatureDifferentNurseTest() {
        ProofRegulationBaseInformation info = createNewBaseInformation();

        info.getProofs().get(0).setNurse(10);
        String signature = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature).startsWith("PV");

        info.getProofs().get(0).setNurse(2.0);
        String signature2 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature2).startsWith("PV").isNotEqualTo(signature);

        info.getProofs().get(0).setNurse(10);
        String signature3 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature3).startsWith("PV").isEqualTo(signature);
    }

    @Test
    void createSignatureDifferentHelpNurseTest() {
        ProofRegulationBaseInformation info = createNewBaseInformation();

        info.getProofs().get(0).setHelpNurse(10);
        String signature = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature).startsWith("PV");

        info.getProofs().get(0).setHelpNurse(2.0);
        String signature2 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature2).startsWith("PV").isNotEqualTo(signature);

        info.getProofs().get(0).setHelpNurse(10);
        String signature3 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature3).startsWith("PV").isEqualTo(signature);
    }

    @Test
    void createSignatureDifferentPatientOccupancyTest() {
        ProofRegulationBaseInformation info = createNewBaseInformation();

        info.getProofs().get(0).setPatientOccupancy(10);
        String signature = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature).startsWith("PV");

        info.getProofs().get(0).setPatientOccupancy(2.0);
        String signature2 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature2).startsWith("PV").isNotEqualTo(signature);

        info.getProofs().get(0).setPatientOccupancy(10);
        String signature3 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature3).startsWith("PV").isEqualTo(signature);
    }

    @Test
    void createSignatureDifferentCountShiftNotRespectedTest() {
        ProofRegulationBaseInformation info = createNewBaseInformation();

        info.getProofs().get(0).setCountShiftNotRespected(10);
        String signature = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature).startsWith("PV");

        info.getProofs().get(0).setCountShiftNotRespected(2);
        String signature2 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature2).startsWith("PV").isNotEqualTo(signature);

        info.getProofs().get(0).setCountShiftNotRespected(10);
        String signature3 = CareSignatureCreater.createPvSignature(info);
        Assertions.assertThat(signature3).startsWith("PV").isEqualTo(signature);
    }

    private ProofRegulationBaseInformation createNewBaseInformation() {
        ProofRegulationBaseInformation info = new ProofRegulationBaseInformation();
        info.setIk(222222222);
        info.setYear(2018);
        info.setQuarter(1);

        List<Months> months = new ArrayList<>();
        months.add(Months.JANUARY);
        months.add(Months.FEBRUARY);
        months.add(Months.MARCH);

        List<ProofRegulationStation> stations = new ArrayList<>();
        stations.add(createNewStation("B1", "01", "0120", months));

        ProofFiller.createProofEntrysFromStations(info, stations, 2018, 1);
        return info;
    }

    private ProofRegulationStation createNewStation(String name, String location, String fab, List<Months> months) {
        ProofRegulationStation station = new ProofRegulationStation();
        station.setSensitiveArea(SensitiveArea.GERIATRIE);
        station.setFabName(name);
        station.setFabNumber(fab);
        station.setLocationCode(location);
        for (Months month : months) {
            station.addNewValideMonth(month);
        }
        return station;
    }
}