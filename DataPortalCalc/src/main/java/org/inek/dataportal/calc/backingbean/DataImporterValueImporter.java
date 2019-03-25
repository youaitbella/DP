package org.inek.dataportal.calc.backingbean;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

public class DataImporterValueImporter {

    public static int parseInteger(String data) throws ParseException {
        if ("".equals(data) || data.contains(",")) {
            throw new ParseException("keine Ganzzahl", 0);
        }
        try {
            int tempInt = Integer.parseInt(data);
        } catch (Exception ex) {
            throw new ParseException("keine Ganzzahl", 0);
        }

        NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
        nf.setParseIntegerOnly(true);

        ParsePosition parsePosition = new ParsePosition(0);
        int val = nf.parse(data, parsePosition).intValue();
        if (parsePosition.getIndex() != data.length()) {
            parsePosition.setErrorIndex(parsePosition.getIndex());
            throw new ParseException("keine Ganzzahl", parsePosition.getIndex());
        }
        return val;
    }

    public static double parseDouble(String data) throws ParseException {
        if ("".equals(data)) {
            throw new ParseException("keine Gleitkommazahl", 0);
        }
        try {
            String tmpValue = data.replace(",", ".");
            double tempDouble = Double.parseDouble(tmpValue);
        } catch (Exception ex) {
            throw new ParseException("keine Gleitkommazahl", 0);
        }

        NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
        nf.setParseIntegerOnly(false);

        ParsePosition parsePosition = new ParsePosition(0);
        double val = nf.parse(data, parsePosition).doubleValue();
        if (parsePosition.getIndex() != data.length()) {
            parsePosition.setErrorIndex(parsePosition.getIndex());
            throw new ParseException("keine Gleitkommazahl", parsePosition.getIndex());
        }
        return val;
    }
}
