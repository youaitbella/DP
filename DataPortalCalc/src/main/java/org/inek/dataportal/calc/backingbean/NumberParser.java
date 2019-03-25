package org.inek.dataportal.calc.backingbean;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

public class NumberParser {

    public static final String NOT_AN_INTEGER = "Keine Ganzzahl";
    public static final String NOT_A_NUMBER = "Keine Gleitkommazahl";

    public static int parseInteger(String data) throws ParseException {
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
        nf.setParseIntegerOnly(true);

        try {
            ParsePosition parsePosition = new ParsePosition(0);
            int val = nf.parse(data, parsePosition).intValue();
            if (parsePosition.getIndex() != data.length()) {
                parsePosition.setErrorIndex(parsePosition.getIndex());
                throw new ParseException(NOT_AN_INTEGER, parsePosition.getIndex());
            }
            return val;
        } catch (NullPointerException ex) {
            throw new ParseException(NOT_AN_INTEGER, 0);
        }
    }

    public static double parseDouble(String data) throws ParseException {
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
        nf.setParseIntegerOnly(false);

        try {
            ParsePosition parsePosition = new ParsePosition(0);
            double val = nf.parse(data, parsePosition).doubleValue();
            if (parsePosition.getIndex() != data.length()) {
                parsePosition.setErrorIndex(parsePosition.getIndex());
                throw new ParseException(NOT_A_NUMBER, parsePosition.getIndex());
            }
            return val;
        } catch (NullPointerException ex) {
            throw new ParseException(NOT_A_NUMBER, 0);
        }
    }
}
