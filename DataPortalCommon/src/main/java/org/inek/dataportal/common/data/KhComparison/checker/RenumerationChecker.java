package org.inek.dataportal.common.data.KhComparison.checker;

public class RenumerationChecker {

    public static boolean isFormalValidZe(String ze) {
        return ze.matches("(^ZP[0-9]{2}.[0-9]{2}|^ZP20[0-9]{2}-[0-9]{2}\\.{0,1}[0-9]{0,3})") && endWithNumber(ze);
    }

    public static boolean isFormalValidEt(String et) {
        return et.matches("^ET[0-9]{2}\\.[0-9]{2}") && endWithNumber(et);
    }

    public static boolean isFormalValidPepp(String pepp) {
        return pepp.matches("(^P[0-9]{3}[A-Z]$|^[P,T,Q][A-Z][0-9]{2}[A-Z])") || isPseudoPepp(pepp);
    }

    public static boolean isPseudoPepp(String pepp) {
        return pepp.matches("PUEL|PKOR");
    }

    private static boolean endWithNumber(String value) {
        return value.matches("^.*\\d$");
    }
}
