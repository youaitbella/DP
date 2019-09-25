package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.entities.Proof;
import org.inek.dataportal.care.entities.ProofExceptionFact;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;

import java.util.ArrayList;
import java.util.List;

public class ProofChecker {
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
            if (proof.getNurse() == 0 && proof.getPatientOccupancy() > 0) {
                addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                        + " Monat: " + proof.getMonth().getName()
                        + " Schicht: " + proof.getShift().getName()
                        + ": Es sind keine Pflegekräfte eingetragen, obwohl Patienten vorhanden sind");
            }
            if (proof.getNurse() == 0 && proof.getPatientOccupancy() == 0 && proof.getCountShift() > 0) {
                addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                        + " Monat: " + proof.getMonth().getName()
                        + " Schicht: " + proof.getShift().getName()
                        + ": Es sind weder Pflegekräfte noch Patienten eingetragen, obwohl Schichten vorhanden sind");
            }
            if (proof.getCountShift() == 0 && proof.getNurse() > 0) {
                addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                        + " Monat: " + proof.getMonth().getName()
                        + " Schicht: " + proof.getShift().getName()
                        + ": Es sind Pflegekräfte eingetragen, obwohl die Anzahl der Schichten Null beträgt");
            }
            if (proof.getCountShift() < proof.getCountShiftNotRespected()) {
                addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                        + " Monat: " + proof.getMonth().getName()
                        + " Schicht: " + proof.getShift().getName()
                        + ": Es sind mehr Schichten als nicht eingehalten eingetragen als Schichten insgesamt vorhanden sind");
            }
            if (proof.getCountShift() == 0 && "".equals(proof.getComment().trim())) {
                addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                        + " Monat: " + proof.getMonth().getName()
                        + " Schicht: " + proof.getShift().getName()
                        + ": Entweder fehlen Schichten oder eine Erläuterung im Kommentarfeld,");
            }
            if (proof.getPatientPerNurse() < 0.5 && "".equals(proof.getComment().trim())) {
                addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                        + " Monat: " + proof.getMonth().getName()
                        + " Schicht: " + proof.getShift().getName()
                        + ": Der Betreuungsschlüssel ist auffällig hoch und es fehlt eine Erläuterung im Kommentarfeld,");
            }
            if (proof.getPatientPerNurse() > 100. && "".equals(proof.getComment().trim())) {
                addMessage(messages, "Station: " + proof.getProofRegulationStation().getStationName()
                        + " Monat: " + proof.getMonth().getName()
                        + " Schicht: " + proof.getShift().getName()
                        + ": Der Betreuungsschlüssel ist auffällig niedrig und es fehlt eine Erläuterung im Kommentarfeld,");
            }
        }
        return messages;
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
                if (proof.getExceptionFact().stream().filter(c -> c.getExceptionFactId() == fact.getExceptionFactId()).count() > 1) {
                    return "Für eine oder mehrere Stationen sind doppelte Ausnahmetatbestände angegeben";
                }
            }
        }
        return "";
    }
}
