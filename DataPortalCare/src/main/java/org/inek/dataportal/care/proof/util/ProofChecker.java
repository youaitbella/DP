package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.care.proof.entity.ProofExceptionFact;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.common.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public static final String WARD = "Station: ";
    public static final String MONTH = " Monat: ";
    public static final String SHIFT = " Schicht: ";

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
                addMessage(messages, WARD + proof.getProofWard().getName()
                        + MONTH + proof.getMonth().getName()
                        + SHIFT + proof.getShift().getName()
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
            addMessage(messages, WARD + proof.getProofWard().getName()
                    + MONTH + proof.getMonth().getName()
                    + SHIFT + proof.getShift().getName()
                    + ": "
                    + NO_NURSE_BUT_PATIENT);
            return;
        }
        if (proof.getNurse() > 0 && proof.getPatientOccupancy() == 0 && "".equals(proof.getComment().trim())) {
            addMessage(messages, WARD + proof.getProofWard().getName()
                    + MONTH + proof.getMonth().getName()
                    + SHIFT + proof.getShift().getName()
                    + ": "
                    + NO_PATIENT_BUT_NURSE);
            return;
        }
        if (proof.getNurse() == 0 && proof.getPatientOccupancy() == 0 && proof.getCountShift() > 0) {
            addMessage(messages, WARD + proof.getProofWard().getName()
                    + MONTH + proof.getMonth().getName()
                    + SHIFT + proof.getShift().getName()
                    + ": "
                    + SHIFT_BUT_NURSE);
            return;
        }
        if (proof.getCountShift() == 0 && proof.getNurse() > 0) {
            addMessage(messages, WARD + proof.getProofWard().getName()
                    + MONTH + proof.getMonth().getName()
                    + SHIFT + proof.getShift().getName()
                    + ": "
                    + NURSE_BUT_SHIFT);
            return;
        }
        if (proof.getCountShift() == 0 && "".equals(proof.getComment().trim())) {
            addMessage(messages, WARD + proof.getProofWard().getName()
                    + MONTH + proof.getMonth().getName()
                    + SHIFT + proof.getShift().getName()
                    + ": "
                    + MISSING_SHIFT);
            return;
        }
        if (proof.getPatientPerNurse() < 0.5 && "".equals(proof.getComment().trim())) {
            addMessage(messages, WARD + proof.getProofWard().getName()
                    + MONTH + proof.getMonth().getName()
                    + SHIFT + proof.getShift().getName()
                    + ": "
                    + PATIENT_PER_NURSE_LOW);
            return;
        }
        if (proof.getPatientPerNurse() > 100. && "".equals(proof.getComment().trim())) {
            addMessage(messages, WARD + proof.getProofWard().getName()
                    + MONTH + proof.getMonth().getName()
                    + SHIFT + proof.getShift().getName()
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

    public static String checkForMissingLocationNumber(List<DeptWard> wards, int year, int quarter) {
        Date fromDate = DateUtils.createDate(year, (quarter * 3) - 2, 1);
        Date toDate = DateUtils.createDate(year, quarter * 3, quarter == 1 || quarter == 4 ? 31 : 30);
        String errorMsg = wards.stream()
                .filter(w -> w.getLocationCodeVz() == 0)
                .filter(w -> w.getValidFrom().compareTo(toDate) <= 0)
                .filter(w -> w.getValidTo().compareTo(fromDate) >= 0)
                .map(w -> "Ungültige Standortnummer. Sensitiver Bereich: " + w.getDept().getSensitiveArea()
                        + ", Standort: " + w.getLocationText()
                        + ", FAB: " + w.getFab()
                        + ", Stationsname: " + w.getWardName()
                )
                .collect(Collectors.joining("\\r\\n \\r\\n"));
        if (!errorMsg.isEmpty()) {
            errorMsg = "Sie haben in Ihrer Meldung nach § 5 Abs. 3 und 4 PpUGV noch mindestens eine ungültige " +
                    "Standortnummer (Format: 77xxxx000) eingetragen. " +
                    "Die gültige Standortnummer ist über den Menüpunkt „Umbenennung oder " +
                    "strukturelle Veränderung (§ 5 Abs. 4 PpUGV)“ einzutragen, " +
                    "bevor Sie eine Quartalsmeldung anlegen können."
                    + "\\r\\n \\r\\n" + errorMsg;
        }
        return errorMsg;
    }
}
