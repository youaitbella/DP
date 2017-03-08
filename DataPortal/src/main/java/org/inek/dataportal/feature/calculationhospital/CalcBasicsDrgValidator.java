/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import org.inek.dataportal.entities.calc.DrgCalcBasics;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.MessageContainer;

/**
 *
 * @author muellermi
 */
public class CalcBasicsDrgValidator {
    
   public static MessageContainer composeMissingFieldsMessage(DrgCalcBasics calcBasics) {
        MessageContainer message = new MessageContainer();

        checkField(message, calcBasics.getIk(), 100000000, 999999999, "lblIK", "form:ikMulti", "todo:page");

        return message;
    }

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
