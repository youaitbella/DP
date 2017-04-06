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
public final class CalcBasicsPsyValidator {

    private CalcBasicsPsyValidator() {
        // utility class, should not be created
    }
    
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
        final int minIkWith9Digits = 100000000;
        final int maxIkWith9Digits = 999999999;
        checkField(message, calcBasics.getIk(), minIkWith9Digits, maxIkWith9Digits, 
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
        final String pageName = "lblCalcStation";
        final String elementId = "";
        final int unbelievableAmountOfBeds = 1000;
        List<KGPListStationServiceCost> stationServiceCosts = calcBasics.getStationServiceCosts();
        for (KGPListStationServiceCost serviceCost : stationServiceCosts) {
            checkField(message, serviceCost.getStation(), "Stationsname muss angegeben werden.", elementId, pageName); 
            checkField(message, serviceCost.getBedCnt(), 1, unbelievableAmountOfBeds, 
                    "Anzahl Betten darf nicht leer sein.", elementId, pageName);
            // cnt and amount should be both > 0 or 0
            checkFieldsBothEmptyOrSet(message, 
                    serviceCost.getFunctionalServiceAmount(), serviceCost.getFunctionalServiceCnt(),
                    "Funktionsdienst", elementId, pageName);
            checkFieldsBothEmptyOrSet(message, 
                    serviceCost.getMedicalServiceAmount(), serviceCost.getMedicalServiceCnt(),
                    "ärztlichen Dienst", elementId, pageName);
            checkFieldsBothEmptyOrSet(message, 
                    serviceCost.getNursingServiceAmount(), serviceCost.getNursingServiceCnt(),
                    "Pflegedienst", elementId, pageName);
            checkFieldsBothEmptyOrSet(message, 
                    serviceCost.getPsychologistAmount(), serviceCost.getPsychologistCnt(),
                    "psychologischen Dienst", elementId, pageName);
            checkFieldsBothEmptyOrSet(message, 
                    serviceCost.getSocialWorkerAmount(), serviceCost.getSocialWorkerCnt(),
                    "sozialien Dienst", elementId, pageName);
            checkFieldsBothEmptyOrSet(message, 
                    serviceCost.getSpecialTherapistAmount(), serviceCost.getSpecialTherapistCnt(),
                    "spezialtherapeutischen Dienst", elementId, pageName);            
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

    private static void checkFieldsBothEmptyOrSet(MessageContainer message, 
            int personalCosts, double personalCnt, 
            String area, String elementId, String topicKey) {
        if (!(0 == personalCnt && 0 == personalCosts)) {
            if (0 != personalCnt) {
                applyMessageValues(message, "Personal im " + area + " ohne Kosten", topicKey, elementId);
            } else {
                applyMessageValues(message, "Kosten im " + area + " ohne Personal", topicKey, elementId);
            }
        }
    }
    
    /**
     * Copy the parameter into the message container, performing some checks.
     * Several messages will be combined, only one topic and element can be given.
     * So when there are errors on several pages only the first one will be reached by this message container.
     * But all errors found will be mentioned.
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
