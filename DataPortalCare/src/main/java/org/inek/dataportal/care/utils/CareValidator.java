/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;

import java.util.stream.Collectors;

/**
 *
 * @author lautenti
 */
public class CareValidator {

    private static final String NO_STATIONS_MESSAGE = "Bitte geben Sie zu mindestens einem Bereich eine Station an.";

    public static String checkDeptBaseinformationIsAllowedToSend(DeptBaseInformation info) {
        String errorMessages = "";
        errorMessages += checkMinStationsForBaseinformation(info);
        return errorMessages;
    }

    private static String checkMinStationsForBaseinformation(DeptBaseInformation info) {
        String errorMessages = "";
        Boolean hasNoStation = true;
        for (Dept dept : info.getDepts().stream()
                .filter(c -> c.getRequired())
                .collect(Collectors.toList())) {
            if (dept.getDeptWards().size() > 0) {
                hasNoStation = false;
            }
        }

        if (hasNoStation && info.getDepts().stream()
                .filter(c -> c.getRequired()).count() > 0) {
            errorMessages = NO_STATIONS_MESSAGE;
        }

        return errorMessages;
    }

}
