package org.inek.dataportal.common.helper.nub;

public class NubValueFormatter {

    public static String formatValuesForDatabase(String value) {
        String[] strings = value.split("\\s|,|\r|\n|;");
        String formatted = "";
        for (String s : strings) {
            if (s.isEmpty()) {
                continue;
            }
            if (formatted.length() > 0) {
                formatted += ", ";
            }
            formatted += s;
        }
        return formatted;
    }
}
