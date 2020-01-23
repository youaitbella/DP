package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.version.MapVersion;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.care.proof.entity.ProofExceptionFact;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.care.proof.entity.ProofRegulationStation;
import org.inek.dataportal.care.utils.CalculatorPpug;
import org.inek.dataportal.common.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.inek.dataportal.care.proof.util.ProofChecker.*;

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

        assertThat(ProofChecker.proofIsReadyForSave(baseInfo, 2)).isEmpty();

        proof1.addExceptionFact(fact2);

        assertThat(ProofChecker.proofIsReadyForSave(baseInfo, 2))
                .hasSize(1)
                .containsOnly("Für eine oder mehrere Stationen sind zuviele Ausnahmetatbestände angegeben");

        proof1.removeExceptionFact(fact3);

        assertThat(ProofChecker.proofIsReadyForSave(baseInfo, 2))
                .hasSize(1)
                .containsOnly("Für eine oder mehrere Stationen sind doppelte Ausnahmetatbestände angegeben");
    }

    @Test
    void proofIsReadyForSendTestReturns_MISSING_SHIFT_IfAllDataIsMissing() {
        ProofRegulationBaseInformation baseInfo = createBaseInfo();
        List<String> messages = ProofChecker.proofIsReadyForSend(baseInfo, 0);
        assertThat(messages)
                .hasSize(2)
                .contains("Station: Station H1 Monat: Januar Schicht: Tag: " + MISSING_SHIFT)
                .contains("Station: Station H2 Monat: Januar Schicht: Nacht: " + MISSING_SHIFT);
    }

    @Test
    void proofIsReadyForSendTestDoesNotReturn_MISSING_SHIFT_IfAllDataIsMissingButComment() {
        ProofRegulationBaseInformation baseInfo = createBaseInfo();
        baseInfo.getProofs().get(0).setComment("closed");
        List<String> messages = ProofChecker.proofIsReadyForSend(baseInfo, 0);
        assertThat(messages)
                .hasSize(1)
                .containsOnly("Station: Station H2 Monat: Januar Schicht: Nacht: " + MISSING_SHIFT);
    }

    @Test
    void proofIsReadyForSendTestReturns_Mismatch_IfOnlyPatientOrNursesAreGiven() {
        ProofRegulationBaseInformation baseInfo = createBaseInfo();
        baseInfo.getProofs().get(0).setPatientOccupancy(10);
        baseInfo.getProofs().get(1).setNurse(10);
        List<String> messages = ProofChecker.proofIsReadyForSend(baseInfo, 0);
        assertThat(messages)
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
        assertThat(messages)
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
        assertThat(messages)
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
        assertThat(messages)
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
        assertThat(messages)
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
        assertThat(messages)
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

    @Test
    public void checkForMissingLocationNumberReturnsErrorIfAWardWithoutNumberExists() {
        List<DeptWard> wards = createWards();
        String msg = checkForMissingLocationNumber(wards, 2019, 1);
        assertThat(msg).isNotEmpty();
    }

    @Test
    public void checkForMissingLocationNumberReturnsEmptyIfAllWardsContainNumber() {
        List<DeptWard> wards = createWards();
        String msg = checkForMissingLocationNumber(wards, 2020, 1);
        assertThat(msg).isEmpty();
    }

    private List<DeptWard> createWards() {
        List<DeptWard> wards = new ArrayList<>();
        DeptWard ward1 = createDeptWard(DateUtils.createDate(2018, 1, 1),
                DateUtils.createDate(2050, 1, 1),
                "Station A", "Fachabteilung 1", 1, 772548, "1300");
        DeptWard ward2 = createDeptWard(DateUtils.createDate(2018, 1, 1),
                DateUtils.createDate(2019, 12, 1),
                "Station A", "Fachabteilung 1", 1, 0, "1300");
        DeptWard ward3 = createDeptWard(DateUtils.createDate(2020, 1, 1),
                DateUtils.createDate(2050, 1, 1),
                "Station A", "Fachabteilung 1", 1, 772548, "1300");
        wards.add(ward1);
        wards.add(ward2);
        wards.add(ward3);
        return wards;
    }

    private DeptWard createDeptWard(Date validFrom, Date validTo, String name, String deptName, int p21, int vz, String fab) {
        Dept dept = new Dept();
        dept.setSensitiveArea("Intensiv");
        DeptWard ward = new DeptWard(new MapVersion());
        ward.setDept(dept);
        ward.setValidFrom(validFrom);
        ward.setValidTo(validTo);
        ward.setWardName(name);
        ward.setDeptName(deptName);
        ward.setLocationCodeP21(p21);
        ward.setLocationCodeVz(vz);
        ward.setFab(fab);
        return ward;
    }

}