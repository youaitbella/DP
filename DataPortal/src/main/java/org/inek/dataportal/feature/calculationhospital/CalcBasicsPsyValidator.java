/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import org.inek.dataportal.entities.calc.PeppCalcBasics;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.MessageContainer;

/**
 *
 * @author muellermi
 */
public class CalcBasicsPsyValidator {
    
   public static MessageContainer composeMissingFieldsMessage(PeppCalcBasics calcBasics) {
        MessageContainer message = new MessageContainer();

        checkBasics(calcBasics, message);
        checkBasicExplanation(calcBasics, message);
        checkExternalServiceProvision(calcBasics, message);
        checkTherapeuticScope(calcBasics, message);
        checkRadiology(calcBasics, message);
        checkLaboratory(calcBasics, message);
        checkDiagnosticScope(calcBasics, message);
        checkStationaryScope(calcBasics, message);
        checkMedicalInfrastructure(calcBasics, message);
        checkNonMedicalInfrastructure(calcBasics, message);
        checkStaffCost(calcBasics, message);

        return message;
    }

    //<editor-fold defaultstate="collapsed" desc="checkBasics">
   private static void checkBasics(PeppCalcBasics calcBasics, MessageContainer message) {
       checkField(message, calcBasics.getIk(), 100000000, 999999999, "lblIK", "form:ikMulti", "todo:page");
   }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="checkBasicExplanation">
    private static void checkBasicExplanation(PeppCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="checkExternalServiceProvision">
    private static void checkExternalServiceProvision(PeppCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="checkOpAn">
    private static void checkTherapeuticScope(PeppCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="checkRadiology">
    private static void checkRadiology(PeppCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="checkLaboratory">
    private static void checkLaboratory(PeppCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="checkDiagnosticScope">
    private static void checkDiagnosticScope(PeppCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="checkNormalWard">
    private static void checkStationaryScope(PeppCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="checkMedicalInfrastructure">
    private static void checkMedicalInfrastructure(PeppCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="checkNonMedicalInfrastructure">
    private static void checkNonMedicalInfrastructure(PeppCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="checkStaffCost">
    private static void checkStaffCost(PeppCalcBasics calcBasics, MessageContainer message) {
    }
    //</editor-fold>
    
    private static void checkField(MessageContainer message, String value, String msgKey, String elementId, String topicName) {
        if (Utils.isNullOrEmpty(value)) {
            applyMessageValues(message, msgKey, topicName, elementId);
        }
    }

    private static void checkField(MessageContainer message, Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId, String topicName) {
        if (value == null
                || minValue != null && value < minValue
                || maxValue != null && value > maxValue) {
            applyMessageValues(message, msgKey, topicName, elementId);
        }
    }

    private static void applyMessageValues(MessageContainer message, String msgKey, String topicName, String elementId) {
        message.setMessage(message.getMessage() + "\\r\\n" + Utils.getMessage(msgKey));
        if (message.getTopic().isEmpty()) {
            message.setTopic(topicName);
            message.setElementId(elementId);
        }
    }
    
}
