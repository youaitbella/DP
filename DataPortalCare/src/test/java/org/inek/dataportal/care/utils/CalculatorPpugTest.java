package org.inek.dataportal.care.utils;


import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.proof.entity.Proof;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

class CalculatorPpugTest {

    @Test
    void calculatePatientPerNurseWithNullTest() {
        List<TestEntry> testEntrys = new ArrayList<>();

        testEntrys.add(new TestEntry(createProof(0, 0, 1), 0.4, 0));
        testEntrys.add(new TestEntry(createProof(0, 1, 2), 0.4, 0));
        testEntrys.add(new TestEntry(createProof(1, 0, 0), 0.2, 0));
        testEntrys.add(new TestEntry(createProof(0, 0, 0), 0.0, 0));

        for (TestEntry entry : testEntrys) {
            CalculatorPpug.calculatePatientPerNurse(entry.getProof(), entry.getPart());
            Assertions.assertThat(entry.getProof().getPatientPerNurse())
                    .as(getTestEntryString(entry))
                    .isEqualTo(entry.getResult());
        }
    }

    @Test
    void countHelpeNurseChargeableWithNullTest() {
        List<TestEntry> testEntrys = new ArrayList<>();

        testEntrys.add(new TestEntry(createProof(0, 0, 1), 0.4, 0));
        testEntrys.add(new TestEntry(createProof(0, 0, 0), 0.4, 0));
        testEntrys.add(new TestEntry(createProof(0, 0, 0), 0.2, 0));
        testEntrys.add(new TestEntry(createProof(0, 0, 0), 0.0, 0));

        for (TestEntry entry : testEntrys) {
            CalculatorPpug.calculateCountHelpeNurseChargeable(entry.getProof(), entry.getPart());
            Assertions.assertThat(entry.getProof().getPatientPerNurse())
                    .as(getTestEntryString(entry))
                    .isEqualTo(entry.getResult());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, 0.5, 2, 0.4, 2.41",
            "0.5, 0.5, 9.65, 0.4, 11.63",
            "0.5, 0.5, 2, 0.08, 3.70",
            "0.75, 0, 9.65, 0.2, 12.87",
            "0.75, 0.35, 9.65, 0.2, 10.27",
            "1, 0.35, 28.75, 0.4, 21.30",
            "1, 0.5, 2, 0.08, 1.83",
            "1, 0.5, 24, 0.15, 20.34",
            "1, 1, 15, 0.08, 13.76",
            "1, 1, 15, 0.1, 13.51",
            "1, 1, 15, 0.15, 12.71",
            "1, 1, 15, 0.1, 13.51",
            "1.25, 0.25, 28.75, 0.4, 19.17",
            "2.35, 0.75, 28.75, 0.2, 9.78",
            "2.5, 0.5, 28.75, 0.2, 9.58",
            "3, 0.25, 25, 0.1, 7.69",
            "4, 1, 12, 0.08, 2.76",
            "3.5, 1.5, 42, 0.2, 9.59",
            "2, 0, 34.37, 0.15, 17.19",
            "0.94, 0.94, 29.80, 0.4, 18.98",
            "2.38, 1.03, 21.20, 0.2, 7.11",
            "3.38, 0.88, 26.40, 0.2, 6.24"
    })
    void calculatePatientPerNurseTest(double nurse, double helpNurse, double patients, double part, double result) {
        Proof proof = createProof(nurse, helpNurse, patients);
        CalculatorPpug.calculatePatientPerNurse(proof, 1 - part);
        Assertions.assertThat(proof.getPatientPerNurse()).isEqualTo(result);
    }

    @Test
    void countHelpeNurseChargeableTest() {
        List<TestEntry> testEntrysCountHelpeNurseChargeable = createTestEntrysCountHelpeNurseChargeable();

        for (TestEntry entry : testEntrysCountHelpeNurseChargeable) {
            CalculatorPpug.calculateCountHelpeNurseChargeable(entry.getProof(), 1 - entry.getPart());
            Assertions.assertThat(entry.getProof().getCountHelpeNurseChargeable())
                    .as(getTestEntryString(entry))
                    .isEqualTo(entry.getResult());
        }
    }

    private String getTestEntryString(TestEntry entry) {
        String result = "";
        result += "Nurse: " + entry.getProof().getNurse() + " ";
        result += "HelpNurse: " + entry.getProof().getHelpNurse() + " ";
        result += "D. PatientOccupancy: " + entry.getProof().getPatientOccupancy() + " ";
        result += "Part: " + entry.getPart() + " ";
        return result;
    }


    private List<TestEntry> createTestEntrysCountHelpeNurseChargeable() {
        List<TestEntry> entrys = new ArrayList<>();
        entrys.add(new TestEntry(createProof(0.5, 0.5, 2), 0.4, 0.33));
        entrys.add(new TestEntry(createProof(0.5, 0.5, 9.65), 0.4, 0.33));
        entrys.add(new TestEntry(createProof(0.5, 0.5, 2), 0.08, 0.04));
        entrys.add(new TestEntry(createProof(0.75, 0, 9.65), 0.2, 0.19));
        entrys.add(new TestEntry(createProof(0.75, 0.35, 9.65), 0.2, 0.19));
        entrys.add(new TestEntry(createProof(1, 0.35, 28.75), 0.4, 0.67));
        entrys.add(new TestEntry(createProof(1, 0.5, 2), 0.08, 0.09));
        entrys.add(new TestEntry(createProof(1, 0.5, 24), 0.15, 0.18));
        entrys.add(new TestEntry(createProof(1, 1, 15), 0.08, 0.09));
        entrys.add(new TestEntry(createProof(1, 1, 15), 0.1, 0.11));
        entrys.add(new TestEntry(createProof(1, 1, 15), 0.15, 0.18));
        entrys.add(new TestEntry(createProof(1, 1, 15), 0.1, 0.11));
        entrys.add(new TestEntry(createProof(1.25, 0.25, 28.75), 0.4, 0.83));
        entrys.add(new TestEntry(createProof(2.35, 0.75, 28.75), 0.2, 0.59));
        entrys.add(new TestEntry(createProof(2.5, 0.5, 28.75), 0.2, 0.63));
        entrys.add(new TestEntry(createProof(3, 0.25, 25), 0.1, 0.33));
        entrys.add(new TestEntry(createProof(4, 1, 12), 0.08, 0.35));
        entrys.add(new TestEntry(createProof(2.78, 0, 7.50), 0.2, 0.70));
        return entrys;
    }

    private Proof createProof(double nurse, double helpNurse,
                              double patientOccupancy) {
        Proof proof = new Proof();
        proof.setNurse(nurse);
        proof.setHelpNurse(helpNurse);
        proof.setPatientOccupancy(patientOccupancy);
        return proof;
    }


    private class TestEntry {
        private Proof _proof;
        private double part;
        private double _result;

        public TestEntry(Proof proof, double part, double result) {
            this._proof = proof;
            this.part = part;
            this._result = result;
        }

        public Proof getProof() {
            return _proof;
        }

        public double getPart() {
            return part;
        }


        public double getResult() {
            return _result;
        }

    }
}