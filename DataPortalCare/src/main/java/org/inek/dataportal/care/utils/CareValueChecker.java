package org.inek.dataportal.care.utils;

public class CareValueChecker {

    public static boolean isValidFabNumber(String value) {
        return value.matches("^[0-9]{4}$");
    }

    public static boolean isFormalValidVzNumber(String value) {
        return value.matches("^77[0-9]{4}000$");
    }
}
