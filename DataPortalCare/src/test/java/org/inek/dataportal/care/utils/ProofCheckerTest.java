package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.entities.Proof;
import org.inek.dataportal.care.entities.ProofExceptionFact;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.inek.dataportal.care.entities.ProofRegulationStation;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.Shift;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.inek.dataportal.care.utils.ProofChecker.*;

class ProofCheckerTest {

    @Test
    void proofIsReadyForSaveTest() {
        ProofRegulationBaseInformation baseInfo = new ProofRegulationBaseInformation();

        Proof proof1 = new Proof();
        Proof proof2 = new Proof();

        ProofExceptionFact fact1 = new ProofExceptionFact();
        fact1.setExceptionFactId(1);

        ProofExceptionFact fact2 = new ProofExceptionFact();
        fact2.setExceptionFactId(1);

        ProofExceptionFact fact3 = new ProofExceptionFact();
        fact3.setExceptionFactId(2);

        proof1.addExceptionFact(fact1);
        proof1.addExceptionFact(fact3);

        baseInfo.addProof(proof1);
        baseInfo.addProof(proof2);

        Assertions.assertThat(ProofChecker.proofIsReadyForSave(baseInfo, 2)).isEmpty();

        proof1.addExceptionFact(fact2);

        Assertions.assertThat(ProofChecker.proofIsReadyForSave(baseInfo, 2))
                .hasSize(1)
                .containsOnly("Für eine oder mehrere Stationen sind zuviele Ausnahmetatbestände angegeben");

        proof1.removeExceptionFact(fact3);

        Assertions.assertThat(ProofChecker.proofIsReadyForSave(baseInfo, 2))
                .hasSize(1)
                .containsOnly("Für eine oder mehrere Stationen sind doppelte Ausnahmetatbestände angegeben");
    }

    @Test
    void proofIsReadyForSendTestReturns_MISSING_SHIFT_IfAllDataIsMissing() {
        ProofRegulationBaseInformation baseInfo = createBaseInfo();
        List<String> messages = ProofChecker.proofIsReadyForSend(baseInfo, 0);
        Assertions.assertThat(messages)
                .hasSize(2)
                .contains("Station: Station H1 Monat: Januar Schicht: Tag: " + MISSING_SHIFT)
                .contains("Station: Station H2 Monat: Januar Schicht: Nacht: " + MISSING_SHIFT);
    }

    @Test
    void proofIsReadyForSendTestDoesNotReturn_MISSING_SHIFT_IfAllDataIsMissingButComment() {
        ProofRegulationBaseInformation baseInfo = createBaseInfo();
        baseInfo.getProofs().get(0).setComment("closed");
        List<String> messages = ProofChecker.proofIsReadyForSend(baseInfo, 0);
        Assertions.assertThat(messages)
                .hasSize(1)
                .containsOnly("Station: Station H2 Monat: Januar Schicht: Nacht: " + MISSING_SHIFT);
    }

    @Test
    void proofIsReadyForSendTestReturns_Mismatch_IfOnlyPatientOrNursesAreGiven() {
        ProofRegulationBaseInformation baseInfo = createBaseInfo();
        baseInfo.getProofs().get(0).setPatientOccupancy(10);
        baseInfo.getProofs().get(1).setNurse(10);
        List<String> messages = ProofChecker.proofIsReadyForSend(baseInfo, 0);
        Assertions.assertThat(messages)
                .hasSize(2)
                .contains("Station: Station H1 Monat: Januar Schicht: Tag: " + NO_NURSE_BUT_PATIENT)
                .contains("Station: Station H2 Monat: Januar Schicht: Nacht: " + NO_PATIENT_BUT_NURSE);
    }

    @Test
    void proofIsReadyForSendTestReturns_Mismatch_and_MORE_FAILUERS_THAN_TOTAL_SHIFTS_IfOnlyPatientOrNursesAreGivenAndMoreFailuresThanSifts() {
        ProofRegulationBaseInformation baseInfo = createBaseInfo();
        baseInfo.getProofs().get(0).setPatientOccupancy(10);
        baseInfo.getProofs().get(0).setCountShiftNotRespected(1);
        baseInfo.getProofs().get(1).setNurse(10);
        baseInfo.getProofs().get(1).setCountShiftNotRespected(10);
        baseInfo.getProofs().get(1).setCountShift(5);
        List<String> messages = ProofChecker.proofIsReadyForSend(baseInfo, 0);
        Assertions.assertThat(messages)
                .hasSize(4)
                .contains("Station: Station H1 Monat: Januar Schicht: Tag: " + NO_NURSE_BUT_PATIENT)
                .contains("Station: Station H1 Monat: Januar Schicht: Tag: " + MORE_FAILUERS_THAN_TOTAL_SHIFTS)
                .contains("Station: Station H2 Monat: Januar Schicht: Nacht: " + NO_PATIENT_BUT_NURSE)
                .contains("Station: Station H2 Monat: Januar Schicht: Nacht: " + MORE_FAILUERS_THAN_TOTAL_SHIFTS);
    }

    @Test
    void proofIsReadyForSendTestReturn_SHIFT_BUT_NURSE_IfOnlyShiftHasValue() {
        ProofRegulationBaseInformation baseInfo = createBaseInfo();
        baseInfo.getProofs().get(0).setComment("closed");
        baseInfo.getProofs().get(1).setCountShift(5);
        List<String> messages = ProofChecker.proofIsReadyForSend(baseInfo, 0);
        Assertions.assertThat(messages)
                .hasSize(1)
                .containsOnly("Station: Station H2 Monat: Januar Schicht: Nacht: " + SHIFT_BUT_NURSE);
    }

    @Test
    void proofIsReadyForSendTestReturn_NURSE_BUT_SHIFT_IfShiftIsMissingButNuresAndPatient() {
        ProofRegulationBaseInformation baseInfo = createBaseInfo();
        baseInfo.getProofs().get(0).setComment("closed");
        baseInfo.getProofs().get(1).setPatientOccupancy(8);
        baseInfo.getProofs().get(1).setNurse(8);
        List<String> messages = ProofChecker.proofIsReadyForSend(baseInfo, 0);
        Assertions.assertThat(messages)
                .hasSize(1)
                .containsOnly("Station: Station H2 Monat: Januar Schicht: Nacht: " + NURSE_BUT_SHIFT);
    }

    @Test
    void proofIsReadyForSendTestReturn_PATIENT_PER_NURSE_HIGH_IfLowAndNotCommented() {
        ProofRegulationBaseInformation baseInfo = createBaseInfo();
        baseInfo.getProofs().get(0).setCountShift(1);
        baseInfo.getProofs().get(0).setPatientOccupancy(101);
        baseInfo.getProofs().get(0).setNurse(1);
        baseInfo.getProofs().get(0).setComment("really");
        baseInfo.getProofs().get(1).setCountShift(1);
        baseInfo.getProofs().get(1).setPatientOccupancy(101);
        baseInfo.getProofs().get(1).setNurse(1);
        CalculatorPpug.calculateAll(baseInfo.getProofs().get(1));
        List<String> messages = ProofChecker.proofIsReadyForSend(baseInfo, 0);
        Assertions.assertThat(messages)
                .hasSize(1)
                .containsOnly("Station: Station H2 Monat: Januar Schicht: Nacht: " + PATIENT_PER_NURSE_HIGH);
    }

    @Test
    void proofIsReadyForSendTestReturn_PATIENT_PER_NURSE_LOW_IfLowAndNotCommented() {
        ProofRegulationBaseInformation baseInfo = createBaseInfo();
        baseInfo.getProofs().get(0).setCountShift(1);
        baseInfo.getProofs().get(0).setPatientOccupancy(1);
        baseInfo.getProofs().get(0).setNurse(10);
        baseInfo.getProofs().get(0).setComment("really");
        baseInfo.getProofs().get(1).setCountShift(1);
        baseInfo.getProofs().get(1).setPatientOccupancy(1);
        baseInfo.getProofs().get(1).setNurse(10);
        CalculatorPpug.calculateAll(baseInfo.getProofs().get(1));
        List<String> messages = ProofChecker.proofIsReadyForSend(baseInfo, 0);
        Assertions.assertThat(messages)
                .hasSize(1)
                .containsOnly("Station: Station H2 Monat: Januar Schicht: Nacht: " + PATIENT_PER_NURSE_LOW);
    }

    private ProofRegulationBaseInformation createBaseInfo() {
        ProofRegulationBaseInformation baseInfo = new ProofRegulationBaseInformation();

        Proof proof1 = new Proof();

        ProofRegulationStation station = new ProofRegulationStation();
        station.setStationName("Station H1");

        proof1.setProofRegulationStation(station);
        proof1.setShift(Shift.DAY);
        proof1.setMonth(Months.JANUARY);

        Proof proof2 = new Proof();

        ProofRegulationStation station2 = new ProofRegulationStation();
        station2.setStationName("Station H2");

        proof2.setProofRegulationStation(station2);
        proof2.setShift(Shift.NIGHT);
        proof2.setMonth(Months.JANUARY);

        baseInfo.addProof(proof1);
        baseInfo.addProof(proof2);
        return baseInfo;
    }
}