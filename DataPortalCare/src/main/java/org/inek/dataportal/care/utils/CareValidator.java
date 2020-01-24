package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;

import java.util.stream.Collectors;

public class CareValidator {

    private static final String NO_STATIONS_MESSAGE = "Bitte geben Sie zu mindestens einem Bereich eine Station an.";

    public static String checkDeptBaseinformationIsAllowedToSend(DeptBaseInformation info) {
        String errorMessages = "";
        errorMessages += checkMinStationsForBaseinformation(info);
        return errorMessages;
    }

    private static String checkMinStationsForBaseinformation(DeptBaseInformation info) {
        String errorMessages = "";
        boolean hasMissing = info.getDepts().stream()
                .filter(c -> c.getRequired())
                .filter(c -> c.getSeeDeptAreaId() == 0)
                .filter(c -> c.getDeptWards().isEmpty())
                .map(c -> true)
                .findAny()
                .orElse(false);
        if (hasMissing) {
            errorMessages = "Bitte geben Sie zu allen Bereichen mindestens eine Station an.";
            return errorMessages;
        }

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
