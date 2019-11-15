package org.inek.dataportal.care.utils;

public class CareValueChecker {

    public static boolean isValidFabNumber(String value) {
        return value.matches("^[0-9]{4}$");
    }

    public static boolean isFormalValidVzNumber(String value) {
        return value.matches("^77[0-9]{4}000$");
    }

    public static boolean startsWithFormalValidVzNumber(String value) {
        return value.matches("^\\s*77[0-9]{4}000((\\s+.*)|(\\s*))$");
    }

    public static int extractFormalValidVzNumber(String value) {
        if (!startsWithFormalValidVzNumber(value)) {
            return 0;
        }
        return Integer.parseInt(value.trim().substring(0, 9));
    }
}
