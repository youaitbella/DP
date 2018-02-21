/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.StringUtil;

/**
 * Holds the Info where the data will be stored and which check to perform to validate the input.
 *
 * @param <T> Type of Element which holds the info (row in a table)
 * @param <I> Type of info to read (column in a Tablerow)
 *
 * @author kunkelan
 */
public class DataImportCheck<T, I> implements Serializable {

    private final ErrorCounter counter;
    private final QuintConsumer<T, String, BiConsumer<T, I>, String, ErrorCounter> check;
    private final BiConsumer<T, I> assign;
    private final String errorMsg;

    public DataImportCheck(
            ErrorCounter counter,
            QuintConsumer<T, String, BiConsumer<T, I>, String, ErrorCounter> check, BiConsumer<T, I> assign,
            String errorMsg) {
        this.counter = counter;
        this.check = check;
        this.assign = assign;
        this.errorMsg = errorMsg;
    }

    public void resetCounter() {
        counter.reset();
    }

    public void tryImport(T item, String data) {
        check.accept(item, data, assign, errorMsg, counter);
    }

    public static <T> void tryImportString(T item, String data, BiConsumer<T, String> assign, String errorMsg, ErrorCounter counter) {
        try {
            //@SuppressWarnings("unchecked") I val = (I) data;
            assign.accept(item, data);
        } catch (Exception ex) {
            counter.addColumnErrorMsg(errorMsg + ex.getMessage() + ": " + data);
        }
    }

    public static <T> void tryImportInteger(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(true);
            int val = nf.parse(data).intValue();
            if (val < 0) {
                assign.accept(item, 0);
                counter.addColumnErrorMsg(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
            } else {
                assign.accept(item, val);
            }
        } catch (ParseException ex) {
            assign.accept(item, 0);
            if (data.trim().isEmpty()) {
                counter.addColumnInfoMsg(errorMsg + "keinen Wert angegeben");
            } else {
                counter.addColumnErrorMsg(errorMsg + Utils.getMessage("msgNotANumber") + ": " + data);
            }
        }
    }

    public static <T> void tryImportRoundedInteger(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(false);
            int val = (int) Math.round(nf.parse(data).doubleValue());
            if (val < 0) {
                assign.accept(item, 0);
                counter.addColumnErrorMsg(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
            } else {
                assign.accept(item, val);
            }
        } catch (ParseException ex) {
            assign.accept(item, 0);
            if (data.trim().isEmpty()) {
                counter.addColumnInfoMsg(errorMsg + "keinen Wert angegeben");
            } else {
                counter.addColumnErrorMsg(errorMsg + Utils.getMessage("msgNotANumber") + ": " + data);
            }
        }
    }

    public static <T> void tryImportCostCenterId(T item, String data, BiConsumer<T, String> assign, String errorMsg, ErrorCounter counter) {
        List<String> allowedValues = Arrays.asList("11", "12", "13");
        if (allowedValues.contains(data)){
            tryImportString(item, data, assign, errorMsg, counter);
        } else{
            counter.addColumnErrorMsg(errorMsg + data);
        }
    }

    public static <T> void tryImportCostCenterId11(T item, String data, BiConsumer<T, String> assign, String errorMsg, ErrorCounter counter) {
        List<String> allowedValues = Arrays.asList("11");
        if (allowedValues.contains(data)) {
            tryImportString(item, data, assign, errorMsg, counter);
        } else {
            counter.addColumnErrorMsg(errorMsg + data);
        }
    }

    public static <T> void tryImportCostCenterId12(T item, String data, BiConsumer<T, String> assign, String errorMsg, ErrorCounter counter) {
        List<String> allowedValues = Arrays.asList("12");
        if (allowedValues.contains(data)) {
            tryImportString(item, data, assign, errorMsg, counter);
        } else {
            counter.addColumnErrorMsg(errorMsg + data);
        }
    }

    public static <T> void tryImportCostCenterId13(T item, String data, BiConsumer<T, String> assign, String errorMsg, ErrorCounter counter) {
        List<String> allowedValues = Arrays.asList("13");
        if (allowedValues.contains(data)) {
            tryImportString(item, data, assign, errorMsg, counter);
        } else {
            counter.addColumnErrorMsg(errorMsg + data);
        }
    }

    public static <T> void tryImportDoubleAsInt(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        try{
            int val = StringUtil.parseLocalizedDoubleAsInt(data);
            if (val < 0){
                assign.accept(item, 0);
                counter.addColumnErrorMsg(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
            } else {
                assign.accept(item, val);
            }
        } catch (NumberFormatException ex) {
            assign.accept(item, 0);
            if (data.trim().isEmpty()) {
                counter.addColumnInfoMsg(errorMsg + "keinen Wert angegeben");
            } else {
                counter.addColumnErrorMsg(errorMsg + Utils.getMessage("msgNotANumber") + ": " + data);
            }
        }
    }

    public static <T> void tryImportBoolean(T item, String data, BiConsumer<T, Boolean> assign, String errorMsg, ErrorCounter counter) {
        try {
            if ((data.trim().length() > 1) || (data.trim().length() == 1 && !data.trim().toLowerCase().equals("x"))) {
                assign.accept(item, false);
                counter.addColumnErrorMsg(errorMsg + " ist nicht leer, x oder X : " + data);
            } else {
                assign.accept(item, data.trim().toLowerCase().equals("x"));
            }
        } catch (Exception ex) {
            assign.accept(item, false);
            counter.addColumnErrorMsg(errorMsg + "(x oder leer erwartet) : " + data + "/n" + ex.getMessage());
        }
    }

    public static <T> void tryImportFremdvergabe(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        String lowerData = data.trim().toLowerCase();
        if (lowerData.startsWith("keine")) {
            assign.accept(item, 0);
        } else if (lowerData.startsWith("teilweise")) {
            assign.accept(item, 1);
        } else if (lowerData.startsWith("vollständig")) {
            assign.accept(item, 2);
        } else {
            assign.accept(item, 0);
            counter.addColumnErrorMsg(errorMsg + " " + data);
        }
    }

    public static <T> void tryImportServiceDocType(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        String lowerData = data.trim().toLowerCase();
        int type = 0;
        switch (lowerData) {
            case "hauskatalog": type = 1; break;
            case "dkg_nt": type = 2; break;
            case "ebm": type = 3; break;
            case "goä": type = 4; break;
            case "sonstiges": type = 5; break;
            default: type = 0;
        }
        if (type == 0) {
            counter.addColumnErrorMsg(errorMsg + " " + data);
        }
        assign.accept(item, type);
    }

    public static <T> void tryImportOccupancyType(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        String lowerData = data.trim().toLowerCase();
        int type = 0;
        switch (lowerData) {
            case "vollstationär": type = 1;
                break;
            case "teilstationär": type = 2;
                break;
            case "voll- und teilstationär": type = 3;
                break;
            default: type = 0;
        }
        if (type == 0) {
            counter.addColumnErrorMsg(errorMsg + " " + data);
        }
        assign.accept(item, type);
    }

//    private void tryImportInteger(T item, String data, BiConsumer<T ,Integer> bind, String errorMsg) {
//        try {
//            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
//            nf.setParseIntegerOnly(true);
//            int val = nf.parse(data).intValue();
//            if (val < 0){
//                bind.accept(item, 0);
//                counter.addColumnErrorMsg(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
//            } else {
//                bind.accept(item, val);
//            }
//        } catch (ParseException ex) {
//            bind.accept(item, 0);
//            if (data.trim().isEmpty()) {
//                counter.addColumnInfoMsg(errorMsg + "keinen Wert angegeben");
//            } else {
//                counter.addColumnErrorMsg(errorMsg + Utils.getMessage("msgNotANumber") + ": " + data);
//            }
//        }
//    }
    public static <T> void tryImportDouble(T item, String data, BiConsumer<T, Double> assign, String errorMsg, ErrorCounter counter) {
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(false);
            double val = nf.parse(data).doubleValue();
            if (val < 0) {
                assign.accept(item, 0.0);
                counter.addColumnErrorMsg(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
            } else {
                assign.accept(item, val);
            }
        } catch (ParseException ex) {
            assign.accept(item, 0.0);
            if (data.trim().isEmpty()) {
                counter.addColumnInfoMsg(errorMsg + "keinen Wert angegeben");
            } else {
                counter.addColumnErrorMsg(errorMsg + Utils.getMessage("msgNotANumber") + ": " + data);
            }
        }
    }

}
