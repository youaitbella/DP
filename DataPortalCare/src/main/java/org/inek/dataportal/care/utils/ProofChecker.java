package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.entities.Proof;
import org.inek.dataportal.care.entities.ProofExceptionFact;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;

import java.util.ArrayList;
import java.util.List;

public class ProofChecker {

    public static final String MORE_FAILUERS_THAN_TOTAL_SHIFTS =
            "Es sind mehr Schichten als \"nicht eingehalten\" eingetragen als Schichten insgesamt vorhanden sind";
    public static final String NO_NURSE_BUT_PATIENT =
            "Es sind keine Pflegekräfte eingetragen, obwohl Patienten vorhanden sind";
    public static final String NO_PATIENT_BUT_NURSE =
            "Es sind keine Patienten eingetragen, obwohl Pflegekräfte vorhanden sind. Bitte eintragen oder im Kommentarfeld erläutern.";
    public static final String SHIFT_BUT_NURSE =
            "Es sind weder Pflegekräfte noch Patienten eingetragen, obwohl Schichten vorhanden sind";
    public static final String NURSE_BUT_SHIFT =
            "Es sind Pflegekräfte eingetragen, aber es fehlen die zugehörigen Schichten";
    public static final String MISSING_SHIFT =
            "Es sind keine Schichten vorhanden. Bitte eintragen oder im Kommentarfeld erläutern.";
    public static final String PATIENT_PER_NURSE_LOW =
            "Das Verhältnis Patient/Pflegekraft ist auffällig niedrig. Bitte korrigieren oder im Kommentarfeld erläutern.";
    public static final String PATIENT_PER_NURSE_HIGH =
            "Das Verhältnis Patient/Pflegekraft ist auffällig hoch. Bitte korrigieren oder im Kommentarfeld erläutern.";

    public static List<String> proofIsReadyForSave(ProofRegulationBaseInformation baseInfo, int exceptionsFactsCount) {
        List<String> errorMessages = new ArrayList<>();
        addMessage(errorMessages, checkExceptionFacts(baseInfo, exceptionsFactsCount));
        return errorMessages;
    }

    public static List<String> proofIsReadyForSend(ProofRegulationBaseInformation baseInfo, int exceptionsFactsCount) {
        List<String> errorMessages = new ArrayList<>();
        addMessages(errorMessages, proofIsReadyForSave(baseInfo, exceptionsFactsCount));
        addMessages(errorMessages, checkProofPlausi(baseInfo));
        return errorMessages;
    }

    private static List<String> checkProofPlausi(ProofRegulationBaseInformation baseInfo) {
        List<String> messages = new ArrayList<>();
        for (Proof proof : baseInfo.getProofs()) {
            if (proof.getCountShift() < proof.getCountShiftNotRespected()) {
                addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                        + " Monat: " + proof.getMonth().getName()
                        + " Schicht: " + proof.getShift().getName()
                        + ": "
                        + MORE_FAILUERS_THAN_TOTAL_SHIFTS);
            }

            checkShiftNursePatient(messages, proof);
        }
        return messages;
    }

    @SuppressWarnings("CyclomaticComplexity")
    private static void checkShiftNursePatient(List<String> messages, Proof proof) {
        if (proof.getNurse() == 0 && proof.getPatientOccupancy() > 0) {
            addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                    + " Monat: " + proof.getMonth().getName()
                    + " Schicht: " + proof.getShift().getName()
                    + ": "
                    + NO_NURSE_BUT_PATIENT);
            return;
        }
        if (proof.getNurse() > 0 && proof.getPatientOccupancy() == 0 && "".equals(proof.getComment().trim())) {
            addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                    + " Monat: " + proof.getMonth().getName()
                    + " Schicht: " + proof.getShift().getName()
                    + ": "
                    + NO_PATIENT_BUT_NURSE);
            return;
        }
        if (proof.getNurse() == 0 && proof.getPatientOccupancy() == 0 && proof.getCountShift() > 0) {
            addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                    + " Monat: " + proof.getMonth().getName()
                    + " Schicht: " + proof.getShift().getName()
                    + ": "
                    + SHIFT_BUT_NURSE);
            return;
        }
        if (proof.getCountShift() == 0 && proof.getNurse() > 0) {
            addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                    + " Monat: " + proof.getMonth().getName()
                    + " Schicht: " + proof.getShift().getName()
                    + ": "
                    + NURSE_BUT_SHIFT);
            return;
        }
        if (proof.getCountShift() == 0 && "".equals(proof.getComment().trim())) {
            addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                    + " Monat: " + proof.getMonth().getName()
                    + " Schicht: " + proof.getShift().getName()
                    + ": "
                    + MISSING_SHIFT);
            return;
        }
        if (proof.getPatientPerNurse() < 0.5 && "".equals(proof.getComment().trim())) {
            addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                    + " Monat: " + proof.getMonth().getName()
                    + " Schicht: " + proof.getShift().getName()
                    + ": "
                    + PATIENT_PER_NURSE_LOW);
            return;
        }
        if (proof.getPatientPerNurse() > 100. && "".equals(proof.getComment().trim())) {
            addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                    + " Monat: " + proof.getMonth().getName()
                    + " Schicht: " + proof.getShift().getName()
                    + ": "
                    + PATIENT_PER_NURSE_HIGH);
        }
    }

    private static void addMessage(List<String> messages, String valueToAdd) {
        if (!"".equals(valueToAdd)) {
            messages.add(valueToAdd);
        }
    }

    private static void addMessages(List<String> messages, List<String> valueToAdd) {
        if (!valueToAdd.isEmpty()) {
            messages.addAll(valueToAdd);
        }
    }

    private static String checkExceptionFacts(ProofRegulationBaseInformation baseInfo, int exceptionsFactsCount) {
        for (Proof proof : baseInfo.getProofs()) {
            if (proof.getExceptionFact().size() > exceptionsFactsCount) {
                return "Für eine oder mehrere Stationen sind zuviele Ausnahmetatbestände angegeben";
            }
            for (ProofExceptionFact fact : proof.getExceptionFact()) {
                if (proof.getExceptionFact()
                        .stream()
                        .filter(c -> c.getExceptionFactId() == fact.getExceptionFactId()).count() > 1) {
                    return "Für eine oder mehrere Stationen sind doppelte Ausnahmetatbestände angegeben";
                }
            }
        }
        return "";
    }
}
