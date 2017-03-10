/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import org.inek.dataportal.entities.calc.DrgCalcBasics;
import org.inek.dataportal.entities.calc.KGLListKstTop;
import org.inek.dataportal.entities.calc.KGLOpAn;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.MessageContainer;

/**
 *
 * @author muellermi
 */
public class CalcBasicsDrgValidator {

    public static MessageContainer composeMissingFieldsMessage(DrgCalcBasics calcBasics) {
        MessageContainer message = new MessageContainer();

        checkBasics(calcBasics, message);
        checkBasicExplanation(calcBasics, message);
        checkExternalServiceProvision(calcBasics, message);
        checkOpAn(calcBasics, message);
        checkMaternityRoom(calcBasics, message);
        checkCardiology(calcBasics, message);
        checkEndosoppy(calcBasics, message);
        checkRadiology(calcBasics, message);
        checkLaboratory(calcBasics, message);
        checkDiagnosticScope(calcBasics, message);
        checkNormalWard(calcBasics, message);
        checkIntensiveCare(calcBasics, message);
        checkStrokeUnit(calcBasics, message);
        checkMedicalInfrastructure(calcBasics, message);
        checkNonMedicalInfrastructure(calcBasics, message);
        checkStaffCost(calcBasics, message);
        checkValvularIntervention(calcBasics, message);
        checkNeonat(calcBasics, message);

        return message;
    }

    //<editor-fold defaultstate="collapsed" desc="checkBasics">
    private static void checkBasics(DrgCalcBasics calcBasics, MessageContainer message) {
        checkField(message, calcBasics.getIk(), 100000000, 999999999, "lblIK", "calcBasics:ikMulti", "lblFrontPage");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkBasicExplanation">
    private static void checkBasicExplanation(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkExternalServiceProvision">
    private static void checkExternalServiceProvision(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkOpAn">
    private static void checkOpAn(DrgCalcBasics calcBasics, MessageContainer message) {
        KGLOpAn opAn = calcBasics.getOpAn();
        if (!opAn.isCentralOP()) {
            return;
        }
        checkField(message, opAn.getCentralOPCnt(), 1, 99, "Die Anzahl der OPs ist umplausibel", "", "lblCalcOpAn");

        checkField(message, opAn.getMedicalServiceSnzOP(), 1, 4, "Bitte Schnitt-Naht-Zeit OP ÄD wählen", "", "lblCalcOpAn");
        checkField(message, opAn.getFunctionalServiceSnzOP(), 1, 4, "Bitte Schnitt-Naht-Zeit OP FD/MTD wählen", "", "lblCalcOpAn");
        if (opAn.getMedicalServiceSnzOP() == 4 || opAn.getFunctionalServiceSnzOP() == 4) {
            checkField(message, opAn.getDescriptionSnzOP(), "Bitte SNZ Alternative OP angeben", "", "lblCalcOpAn");
        }
        checkField(message, opAn.getMedicalServiceRzOP(), 1, 4, "Bitte Rüstzeit OP ÄD wählen", "", "lblCalcOpAn");
        checkField(message, opAn.getFunctionalServiceRzOP(), 1, 4, "Bitte Rüstzeit OP FD/MTD wählen", "", "lblCalcOpAn");
        if (opAn.getMedicalServiceRzOP() == 4 || opAn.getFunctionalServiceRzOP() == 4) {
            checkField(message, opAn.getDescriptionRzOP(), "Bitte Rüstzeit Alternative OP angeben", "", "lblCalcOpAn");
        }
        checkField(message, opAn.getMedicalServiceSnzAN(), 1, 4, "Bitte Schnitt-Naht-Zeit AN ÄD wählen", "", "lblCalcOpAn");
        checkField(message, opAn.getFunctionalServiceSnzAN(), 1, 4, "Bitte Schnitt-Naht-Zeit AN FD/MTD wählen", "", "lblCalcOpAn");
        if (opAn.getMedicalServiceSnzAN() == 4 || opAn.getFunctionalServiceSnzAN() == 4) {
            checkField(message, opAn.getDescriptionSnzAN(), "Bitte SNZ Alternative AN angeben", "", "lblCalcOpAn");
        }
        checkField(message, opAn.getMedicalServiceRzAN(), 1, 4, "Bitte Rüstzeit ANÄD wählen", "", "lblCalcOpAn");
        checkField(message, opAn.getFunctionalServiceRzAN(), 1, 4, "Bitte Rüstzeit AN FD/MTD wählen", "", "lblCalcOpAn");
        if (opAn.getMedicalServiceRzAN() == 4 || opAn.getFunctionalServiceRzAN() == 4) {
            checkField(message, opAn.getDescriptionRzAN(), "Bitte Rüstzeit Alternative OP angeben", "", "lblCalcOpAn");
        }

        checkField(message, opAn.getMedicalServiceAmountOP(), 1, 999999999, "Bitte Leistungsminuten OP ÄD angeben", "", "lblCalcOpAn");
        checkField(message, opAn.getFunctionalServiceAmountOP(), 1, 999999999, "Bitte Leistungsminuten OP FD/MTD angeben", "", "lblCalcOpAn");
        checkField(message, opAn.getMedicalServiceAmountAN(), 1, 999999999, "Bitte Leistungsminuten AN ÄD angeben", "", "lblCalcOpAn");
        checkField(message, opAn.getFunctionalServiceAmountAN(), 1, 999999999, "Bitte Leistungsminuten AN FD/MTD angeben", "", "lblCalcOpAn");

        int line = 0;
        for (KGLListKstTop top : calcBasics.getKstTopOp()) {
            line++;
            if (top.isEmpty()) {
                applyMessageValues(message, "Top 3 Leistung, Zeile " + line + ": Bitte angeben", "lblCalcOpAn", "");
            } else {
                checkField(message, top.getText(), "Top 3 Leistung, Zeile " + line + ": Bitte Bezeichnung angeben", "", "lblCalcOpAn");
                checkField(message, top.getCaseCount(), 1, 9999999, "Top 3 Leistung, Zeile " + line + ": Bitte Fallzahl angeben", "", "lblCalcOpAn");
                checkField(message, top.getAmount(), 1, 9999999, "Top 3 Leistung, Zeile " + line + ": Bitte Erlösvolumen angeben", "", "lblCalcOpAn");
                checkField(message, top.getDelimitationAmount(), 1, 9999999, "Top 3 Leistung, Zeile " + line + ": Bitte abgegr. Kostenvolumen angeben", "", "lblCalcOpAn");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkMaternityRoom">
    private static void checkMaternityRoom(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkCardiology">
    private static void checkCardiology(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkEndosoppy">
    private static void checkEndosoppy(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkRadiology">
    private static void checkRadiology(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkLaboratory">
    private static void checkLaboratory(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkDiagnosticScope">
    private static void checkDiagnosticScope(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkNormalWard">
    private static void checkNormalWard(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkIntensiveCare">
    private static void checkIntensiveCare(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkStrokeUnit">
    private static void checkStrokeUnit(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkMedicalInfrastructure">
    private static void checkMedicalInfrastructure(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkNonMedicalInfrastructure">
    private static void checkNonMedicalInfrastructure(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkStaffCost">
    private static void checkStaffCost(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkValvularIntervention">
    private static void checkValvularIntervention(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="checkNeonat">
    private static void checkNeonat(DrgCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>

    private static void checkField(MessageContainer message, String value, String msgKey, String elementId, String topicKey) {
        if (Utils.isNullOrEmpty(value)) {
            applyMessageValues(message, msgKey, topicKey, elementId);
        }
    }

    private static void checkField(MessageContainer message, Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId, String topicKey) {
        if (value == null
                || minValue != null && value < minValue
                || maxValue != null && value > maxValue) {
            applyMessageValues(message, msgKey, topicKey, elementId);
        }
    }

    private static void applyMessageValues(MessageContainer message, String msgKey, String topicKey, String elementId) {
        message.setMessage(message.getMessage() + "\\r\\n" + Utils.getMessageOrKey(msgKey));
        if (message.getTopic().isEmpty()) {
            message.setTopic(topicKey);
            message.setElementId(elementId);
        }
    }

}
