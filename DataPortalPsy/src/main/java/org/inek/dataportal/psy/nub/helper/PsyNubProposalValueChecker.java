package org.inek.dataportal.psy.nub.helper;

import org.inek.dataportal.common.data.KhComparison.checker.RenumerationChecker;

import java.io.Serializable;

public class PsyNubProposalValueChecker implements Serializable {

    public static boolean isValidPostalCode(String value) {
        try {
            Integer tmp = Integer.parseInt(value);
            if (tmp > 99999 || tmp < 0) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isValidStringForDateValue(String value) {
        if (value.isEmpty()) {
            return true;
        }
        String regex = "0[1-9]\\/[0-9][0-9]|1[1-2]\\/[0-9][0-9]";
        return value.matches(regex);
    }

    public static String checkPeppString(String value) {
        String[] pepps = value.split("\\s|,|\r|\n");
        StringBuilder invalidPepps = new StringBuilder();
        for (String pepp : pepps) {
            if (pepp.isEmpty()) {
                continue;
            }
            if (!RenumerationChecker.isFormalValidPepp(pepp)) {
                if (invalidPepps.length() > 0) {
                    invalidPepps.append(", ");
                }
                invalidPepps.append(pepp);
            }
        }
        if (invalidPepps.length() > 0) {
            invalidPepps.insert(0, "Ungültige Pepp(s): ");
        }
        return invalidPepps.toString();
    }
}
