package org.inek.dataportal.care.utils;


import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.entities.Proof;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CalculatorPpugTest {

    @Test
    void calculatePatientPerNurseWithNullTest() {
        List<TestEntry> testEntrys= new ArrayList<>();

        testEntrys.add(new TestEntry(createProof(0, 0, 1, 0.4), 0));
        testEntrys.add(new TestEntry(createProof(0, 1, 2, 0.4), 0));
        testEntrys.add(new TestEntry(createProof(1, 0, 0, 0.2), 0));
        testEntrys.add(new TestEntry(createProof(0, 0, 0, 0.0), 0));

        for(TestEntry entry : testEntrys) {
            CalculatorPpug.calculatePatientPerNurse(entry.getProof());
            Assertions.assertThat(entry.getProof().getPatientPerNurse())
                    .as(getTestEntryString(entry))
                    .isEqualTo(entry.getResult());
        }
    }

    @Test
    void countHelpeNurseChargeableWithNullTest() {
        List<TestEntry> testEntrys= new ArrayList<>();

        testEntrys.add(new TestEntry(createProof(0, 0, 1, 0.4), 0));
        testEntrys.add(new TestEntry(createProof(0, 0, 0, 0.4), 0));
        testEntrys.add(new TestEntry(createProof(0, 0, 0, 0.2), 0));
        testEntrys.add(new TestEntry(createProof(0, 0, 0, 0.0), 0));

        for(TestEntry entry : testEntrys) {
            CalculatorPpug.calculateCountHelpeNurseChargeable(entry.getProof());
            Assertions.assertThat(entry.getProof().getPatientPerNurse())
                    .as(getTestEntryString(entry))
                    .isEqualTo(entry.getResult());
        }
    }

    @Test
    void calculatePatientPerNurseTest() {
        List<TestEntry> testEntrysPatientPerNurse = createTestEntrysPatientPerNurse();

        for(TestEntry entry : testEntrysPatientPerNurse) {
            CalculatorPpug.calculatePatientPerNurse(entry.getProof());
            Assertions.assertThat(entry.getProof().getPatientPerNurse())
                    .as(getTestEntryString(entry))
                    .isEqualTo(entry.getResult());
        }
    }

    @Test
    void countHelpeNurseChargeableTest() {
        List<TestEntry> testEntrysCountHelpeNurseChargeable = createTestEntrysCountHelpeNurseChargeable();

        for(TestEntry entry : testEntrysCountHelpeNurseChargeable) {
            CalculatorPpug.calculateCountHelpeNurseChargeable(entry.getProof());
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


    private List<TestEntry> createTestEntrysPatientPerNurse() {
        List<TestEntry> entrys = new ArrayList<>();
        entrys.add(new TestEntry(createProof(0.5, 0.5, 2, 0.4), 2.41));
        entrys.add(new TestEntry(createProof(0.5, 0.5, 9.65, 0.4), 11.63));
        entrys.add(new TestEntry(createProof(0.5, 0.5, 2, 0.08), 3.70));
        entrys.add(new TestEntry(createProof(0.75, 0, 9.65, 0.2), 12.87));
        entrys.add(new TestEntry(createProof(0.75, 0.35, 9.65, 0.2), 10.27));
        entrys.add(new TestEntry(createProof(1, 0.35, 28.75, 0.4), 21.30));
        entrys.add(new TestEntry(createProof(1, 0.5, 2, 0.08), 1.83));
        entrys.add(new TestEntry(createProof(1, 0.5, 24, 0.15), 20.34));
        entrys.add(new TestEntry(createProof(1, 1, 15, 0.08), 13.76));
        entrys.add(new TestEntry(createProof(1, 1, 15, 0.1), 13.51));
        entrys.add(new TestEntry(createProof(1, 1, 15, 0.15), 12.71));
        entrys.add(new TestEntry(createProof(1, 1, 15, 0.1), 13.51));
        entrys.add(new TestEntry(createProof(1.25, 0.25, 28.75, 0.4), 19.17));
        entrys.add(new TestEntry(createProof(2.35, 0.75, 28.75, 0.2), 9.78));
        entrys.add(new TestEntry(createProof(2.5, 0.5, 28.75, 0.2), 9.58));
        entrys.add(new TestEntry(createProof(3, 0.25, 25, 0.1), 7.69));
        entrys.add(new TestEntry(createProof(4, 1, 12, 0.08), 2.76));
        entrys.add(new TestEntry(createProof(3.5, 1.5, 42, 0.2), 9.59));
        entrys.add(new TestEntry(createProof(2, 0, 34.37, 0.15), 17.19));
        entrys.add(new TestEntry(createProof(0.94, 0.94, 29.80, 0.4), 18.98));
        entrys.add(new TestEntry(createProof(2.38, 1.03, 21.20, 0.2), 7.11));
        entrys.add(new TestEntry(createProof(3.38, 0.88, 26.40, 0.2), 6.24));
        return entrys;
    }

    private List<TestEntry> createTestEntrysCountHelpeNurseChargeable() {
        List<TestEntry> entrys = new ArrayList<>();
        entrys.add(new TestEntry(createProof(0.5, 0.5, 2, 0.4), 0.33));
        entrys.add(new TestEntry(createProof(0.5, 0.5, 9.65, 0.4), 0.33));
        entrys.add(new TestEntry(createProof(0.5, 0.5, 2, 0.08), 0.04));
        entrys.add(new TestEntry(createProof(0.75, 0, 9.65, 0.2), 0.19));
        entrys.add(new TestEntry(createProof(0.75, 0.35, 9.65, 0.2), 0.19));
        entrys.add(new TestEntry(createProof(1, 0.35, 28.75, 0.4), 0.67));
        entrys.add(new TestEntry(createProof(1, 0.5, 2, 0.08), 0.09));
        entrys.add(new TestEntry(createProof(1, 0.5, 24, 0.15), 0.18));
        entrys.add(new TestEntry(createProof(1, 1, 15, 0.08), 0.09));
        entrys.add(new TestEntry(createProof(1, 1, 15, 0.1), 0.11));
        entrys.add(new TestEntry(createProof(1, 1, 15, 0.15), 0.18));
        entrys.add(new TestEntry(createProof(1, 1, 15, 0.1), 0.11));
        entrys.add(new TestEntry(createProof(1.25, 0.25, 28.75, 0.4), 0.83));
        entrys.add(new TestEntry(createProof(2.35, 0.75, 28.75, 0.2), 0.59));
        entrys.add(new TestEntry(createProof(2.5, 0.5, 28.75, 0.2), 0.63));
        entrys.add(new TestEntry(createProof(3, 0.25, 25, 0.1), 0.33));
        entrys.add(new TestEntry(createProof(4, 1, 12, 0.08), 0.35));
        entrys.add(new TestEntry(createProof(2.78, 0, 7.50, 0.2), 0.70));
        return entrys;
    }

    private Proof createProof(double nurse, double helpNurse,
                              double patientOccupancy,
                              double part) {
        Proof proof = new Proof();
        proof.setNurse(nurse);
        proof.setHelpNurse(helpNurse);
        proof.setPatientOccupancy(patientOccupancy);
        proof.setPart(part);
        return proof;
    }


    private class TestEntry {
        private Proof _proof;
        private double _result;

        public TestEntry(Proof proof, double result) {
            setProof(proof);
            setResult(result);
        }

        public Proof getProof() {
            return _proof;
        }

        public void setProof(Proof proof) {
            this._proof = proof;
        }

        public double getPart() {
            return _proof.getPart();
        }


        public double getResult() {
            return _result;
        }

        public void setResult(double result) {
            this._result = result;
        }
    }
}