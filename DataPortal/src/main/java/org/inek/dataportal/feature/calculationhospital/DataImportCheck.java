/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.function.BiConsumer;
import org.inek.dataportal.helper.Utils;

/**
 * Holds the Info where the data will be stored and which check to perform to validate the input.
 *
 * @author kunkelan
 */
public class DataImportCheck<T, I> implements Serializable {

    private final ErrorCounter counter;
    private final QuintConsumer<T, String, BiConsumer<T, I>, String, ErrorCounter> check;
    private final BiConsumer<T, I> assign;
    private final String errorMsg;

    public DataImportCheck(ErrorCounter counter, QuintConsumer<T, String, BiConsumer<T, I>, String, ErrorCounter> check, BiConsumer<T, I> assign, String errorMsg) {
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
            throw new IllegalArgumentException(ex.getMessage() + "\n" + errorMsg + data);
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

//    private void tryImportString(T item, String data, BiConsumer<T, String> bind, String errorMsg) {
//        try {
//            bind.accept(item, data);
//        } catch (Exception ex) {
//            throw new IllegalArgumentException(ex.getMessage() + "\n" + errorMsg + data);
//        }
//    }
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
    private void tryImportDouble(T item, String data, BiConsumer<T, Double> bind, String errorMsg) {
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(false);
            double val = nf.parse(data).doubleValue();
            if (val < 0) {
                throw new IllegalArgumentException(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
            }
            bind.accept(item, val);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(errorMsg + Utils.getMessage("msgNotANumber") + ": " + data);
        }
    }

    private void tryImportBoolean(T item, String data, BiConsumer<T, Boolean> bind, String errorMsg) {
        try {
            if (data.trim().length() > 1) {
                throw new IllegalArgumentException(errorMsg + " ist nicht leer, x oder X : " + data);
            } else if (data.trim().length() == 1 && !data.trim().toLowerCase().equals("x")) {
                throw new IllegalArgumentException(errorMsg + " ist nicht leer, x oder X : " + data);
            }
            bind.accept(item, data.trim().toLowerCase().equals("x"));
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage() + "\n" + errorMsg + data);
        }
    }

    private void tryImportFremdvergabe(T item, String data, BiConsumer<T, Integer> bind, String errorMsg) {
        String lowerData = data.trim().toLowerCase();
        if (lowerData.startsWith("keine")) {
            bind.accept(item, 0);
        } else if (lowerData.startsWith("teilweise")) {
            bind.accept(item, 1);
        } else if (lowerData.startsWith("vollständig")) {
            bind.accept(item, 2);
        } else {
            bind.accept(item, 0);
            counter.addColumnErrorMsg(errorMsg + " " + data);
        }
    }

}
