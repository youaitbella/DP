/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.common.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author muellermi
 */
public class StringUtil {

    public static boolean isNullOrEmpty(String test) {
        return test == null || test.isEmpty();
    }

    public static String[] splitAtUnquotedSemicolon(String line) {
        if (line.trim().length() == 0) {
            return new String[]{""};
        }

        if (line.endsWith(";")) {
            line = line + " ";
        }

        int start = 0;
        List<String> toks = new ArrayList<>();
        boolean withinQuote = false;
        char lastChar = ';';
        
        for (int end = 0; end < line.length(); end++) {
            char c = line.charAt(end);
            switch (c) {
                case ';':
                    if (!withinQuote) {
                        toks.add(line.substring(start, end));
                        start = end + 1;
                    }
                    break;
                case '\"':
                    if (!withinQuote && lastChar == ';'){
                        withinQuote = true;
                    } else if (withinQuote && end + 1 < line.length() && line.charAt(end + 1) == ';'){
                        withinQuote = false;
                    }
                    break;
                default:
                    // do nothing
                    break;
            }
            lastChar = c;
        }
        if (start < line.length()) {
            toks.add(line.substring(start));
        }

        removeQuotes(toks);

        String[] result = new String[toks.size()];
        return toks.toArray(result);
    }

    private static void removeQuotes(List<String> toks) {
        for (int i = 0; i < toks.size(); i++) {
            String trimmedTok = toks.get(i).trim();
            if (trimmedTok.length() > 1 && trimmedTok.startsWith("\"") && trimmedTok.endsWith("\"")) {
                toks.set(i, trimmedTok.substring(1, trimmedTok.length() - 1));
            }
        }
    }

    public static int parseLocalizedDoubleAsInt(String input) {
        return (int) Math.round(parseLocalizedDouble(input));
    }

    public static double parseLocalizedDouble(String input) {
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
            Number n = nf.parse(input);
            return n.doubleValue();
        } catch (ParseException e) {
            throw new NumberFormatException();
        }
    }

    /**
     * transforms a filter as keyed in by the user into a filter which might be used within a SQL where always: handle
     * quatation if filter is an IK (nine digits) or contains a wildcard --> no other action else --> surround by
     * wildcards
     *
     * @param filter
     * @return
     */
    public static String getSqlFilter(String filter) {
        String sqlFfilter = filter.trim().replace("'", "");
        if (sqlFfilter.isEmpty()) {
            return "";
        }
        if (!sqlFfilter.matches("[\\d]{9}") && !sqlFfilter.matches("[\\d]{4}") && !sqlFfilter.contains("%")) {
            sqlFfilter = "%" + sqlFfilter + "%";
        }
        return "'" + sqlFfilter + "'";
    }

}
