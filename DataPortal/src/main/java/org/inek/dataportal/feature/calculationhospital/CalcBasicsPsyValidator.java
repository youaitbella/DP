/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.util.List;
import org.inek.dataportal.entities.calc.KGPListStationServiceCost;
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
        checkInfrastructure(calcBasics, message);
        checkStaffCost(calcBasics, message);

        return message;
    }

    //<editor-fold defaultstate="collapsed" desc="checkBasics">
    private static void checkBasics(PeppCalcBasics calcBasics, MessageContainer message) {
        checkField(message, calcBasics.getIk(), 100000000, 999999999, 
                "lblIK", "calcBasicsPepp:ikMulti", "TopicFrontPage");
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
    
    //<editor-fold defaultstate="collapsed" desc="checkTherapeuticScope">
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
       //lblCalcStation
       List<KGPListStationServiceCost> stationServiceCosts = calcBasics.getStationServiceCosts();
        for (KGPListStationServiceCost serviceCost : stationServiceCosts) {
            checkField(message, serviceCost.getStation(), "Stationsname muss angegeben werden.", "", "lblCalcStation"); 
            serviceCost.getBedCnt(); // count beds have to be > 0
            // cnt and amount should be both > 0 or 0
            serviceCost.getFunctionalServiceAmount();
            serviceCost.getFunctionalServiceCnt();
            serviceCost.getMedicalServiceAmount();
            serviceCost.getMedicalServiceCnt();
            serviceCost.getNursingServiceAmount();
            serviceCost.getNursingServiceCnt();
            serviceCost.getPsychologistAmount();
            serviceCost.getPsychologistCnt();
            serviceCost.getSocialWorkerAmount();
            serviceCost.getSocialWorkerCnt();
            serviceCost.getSpecialTherapistAmount();
            serviceCost.getSpecialTherapistCnt();
            
        }
       
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="checkMedicalInfrastructure">
    private static void checkInfrastructure(PeppCalcBasics calcBasics, MessageContainer message) {
        if (getMedInfraSum(calcBasics, 170) < 0) {
            applyMessageValues(message, "Die Summe der Kostenvolumina der med. Infrastruktur darf nicht negativ sein.",
                    "TopicCalcMedicalInfrastructure", "");
        }
        if (getMedInfraSum(calcBasics, 180) < 0) {
            applyMessageValues(message, "Die Summe der Kostenvolumina der med. Infrastruktur darf nicht negativ sein.",
                    "TopicCalcNonMedicalInfrastructure", "");
        }
    }
    
    public static int getMedInfraSum(PeppCalcBasics calcBasics, int type) {
        int sumAmount = calcBasics.getKgpMedInfraList()
                .stream()
                .filter(m -> m.getCostTypeId() == type)
                .mapToInt(m -> m.getAmount())
                .sum();
        return sumAmount;
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
    
    /**
     * Fill data msgKey, elementId, topicKey in the given MessageContainer when the value is empty.
     * 
     * @param message the container to hold the message and point of error.
     * @param value empty value signals an error.
     * @param msgKey error message can be literal or a property key.
     * @param elementId element of a page to be checked.
     * @param topicKey page where an element will be checked.
     */
    private static void checkField(
            MessageContainer message, String value, String msgKey, String elementId, String topicKey) {
        if (Utils.isNullOrEmpty(value)) {
            applyMessageValues(message, msgKey, topicKey, elementId);
        }
    }

    /**
     * Check as seen above for integer values between a range, failing if value is less or greater the given range.
     * @param message container to hold the error message and position.
     * @param value to check, error if less than minValue or greater than maxValue.
     * @param minValue lower bound of valid value.
     * @param maxValue upper bound of valid value.
     * @param msgKey error text literally or as property id.
     * @param elementId element of the page to be checked.
     * @param topicKey page to be checked.
     */
    private static void checkField(
            MessageContainer message, Integer value, Integer minValue, Integer maxValue, String msgKey, 
            String elementId, String topicKey) {
        if (value == null
                || minValue != null && value < minValue
                || maxValue != null && value > maxValue) {
            applyMessageValues(message, msgKey, topicKey, elementId);
        }
    }

    /**
     * Copy the parameter into the message container, performing some checks.
     * 
     * @param message container to hold error message and position (page and element)
     * @param msgKey error message or property id containing the message.
     * @param topicKey page where the error occurs.
     * @param elementId element of the page can be empty.
     */
    private static void applyMessageValues(MessageContainer message, String msgKey, String topicKey, String elementId) {
        message.setMessage(message.getMessage() + "\\r\\n" + Utils.getMessageOrKey(msgKey));
        if (message.getTopic().isEmpty()) {
            message.setTopic(topicKey);
            message.setElementId(elementId);
        }
    }
    
}
