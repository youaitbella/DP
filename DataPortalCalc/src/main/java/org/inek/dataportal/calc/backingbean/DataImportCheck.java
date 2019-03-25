/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.backingbean;

import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.StringUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Holds the Info where the data will be stored and which check to perform to validate the input.
 *
 * @param <T> Type of Element which holds the info (row in a table)
 * @param <I> Type of info to read (column in a Tablerow)
 * @author kunkelan
 */
class DataImportCheck<T, I> implements Serializable {

    private static final long serialVersionUID = 1L;
    private final QuintConsumer<T, String, BiConsumer<T, I>, String, ErrorCounter> check;
    private final BiConsumer<T, I> assign;
    private final String errorMsg;

    DataImportCheck(
            QuintConsumer<T, String, BiConsumer<T, I>, String, ErrorCounter> check, BiConsumer<T, I> assign,
            String errorMsg) {
        this.check = check;
        this.assign = assign;
        this.errorMsg = errorMsg + ": ";
    }

    void tryImport(T item, String data, ErrorCounter counter) {
        check.accept(item, data, assign, errorMsg, counter);
    }

    static <T> void tryImportString(T item, String data, BiConsumer<T, String> assign, String errorMsg, ErrorCounter counter) {
        try {
            //@SuppressWarnings("unchecked") I val = (I) data;
            assign.accept(item, data);
        } catch (Exception ex) {
            counter.addColumnErrorMsg(errorMsg + ex.getMessage() + ": " + data);
        }
    }

    static <T> void tryImportInteger(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        try {
            int val = NumberParser.parseInteger(data);
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

    static <T> void tryImportRoundedInteger(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        try {
            int val = (int) Math.round(NumberParser.parseDouble(data));
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

    static <T> void tryImportCostCenterId(T item, String data, BiConsumer<T, String> assign, String errorMsg, ErrorCounter counter) {
        List<String> allowedValues = Arrays.asList("11", "12", "13");
        if (allowedValues.contains(data)) {
            tryImportString(item, data, assign, errorMsg, counter);
        } else {
            counter.addColumnErrorMsg(errorMsg + data + " (Erlaubte Werte: 11, 12, 13)");
        }
    }

    static <T> void tryImportCostCenterId11(T item, String data, BiConsumer<T, String> assign, String errorMsg, ErrorCounter counter) {
        if ("11".equalsIgnoreCase(data)) {
            tryImportString(item, data, assign, errorMsg, counter);
        } else {
            counter.addColumnErrorMsg(errorMsg + data + " (Erlaubte Werte: 11)");
        }
    }

    static <T> void tryImportCostCenterId12(T item, String data, BiConsumer<T, String> assign, String errorMsg, ErrorCounter counter) {
        List<String> allowedValues = Arrays.asList("12");
        if ("12".equalsIgnoreCase(data)) {
            tryImportString(item, data, assign, errorMsg, counter);
        } else {
            counter.addColumnErrorMsg(errorMsg + data + " (Erlaubte Werte: 12)");
        }
    }

    static <T> void tryImportCostCenterId13(T item, String data, BiConsumer<T, String> assign, String errorMsg, ErrorCounter counter) {
        List<String> allowedValues = Arrays.asList("13");
        if ("13".equalsIgnoreCase(data)) {
            tryImportString(item, data, assign, errorMsg, counter);
        } else {
            counter.addColumnErrorMsg(errorMsg + data + " (Erlaubte Werte: 13)");
        }
    }

    static <T> void tryImportDoubleAsInt(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        try {
            int val = StringUtil.parseLocalizedDoubleAsInt(data);
            if (val < 0) {
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

    static <T> void tryImportFractionOrPercentAsInteger(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        try {
            Double val = StringUtil.parseLocalizedDouble(data);
            if (val < 0) {
                assign.accept(item, 0);
                counter.addColumnErrorMsg(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
            } else if (val > 0 && val < 1) {
                assign.accept(item, (int) Math.round(val * 100));
            } else {
                assign.accept(item, (int) Math.round(val));
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

    static <T> void tryImportBoolean(T item, String data, BiConsumer<T, Boolean> assign, String errorMsg, ErrorCounter counter) {
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

    static <T> void tryImportFremdvergabe(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
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

    static <T> void tryImportLabServiceArea(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        String lowerData = data.trim().toLowerCase();
        int type;
        switch (lowerData) {
            case "blutprodukte":
                type = 1;
                break;
            case "stammzellenaufbereitung":
                type = 2;
                break;
            case "sonstiges":
                type = 3;
                break;
            default:
                type = 0;
        }
        if (type == 0) {
            counter.addColumnErrorMsg(errorMsg + " " + data + " (Erlaubte Werte: Blutprodukte, Stammzellenaufbereitung, Sonstiges)");
        }
        assign.accept(item, type);
    }

    static <T> void tryImportServiceDocType(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        String lowerData = data.trim().toLowerCase();
        int type;
        switch (lowerData) {
            case "hauskatalog":
                type = 1;
                break;
            case "dkg_nt":
                type = 2;
                break;
            case "dkg-nt":
                type = 2;
                break;
            case "ebm":
                type = 3;
                break;
            case "goä":
                type = 4;
                break;
            case "sonstiges":
                type = 5;
                break;
            default:
                type = 0;
        }
        if (type == 0) {
            counter.addColumnErrorMsg(errorMsg + " " + data + " (Erlaubte Werte: Hauskatalog, DKG_NT, DKG-NT, EBM, GOÄ, Sonstiges)");
        }
        assign.accept(item, type);
    }

    static <T> void tryImportOccupancyType(T item, String data, BiConsumer<T, Integer> assign, String errorMsg, ErrorCounter counter) {
        String lowerData = data.trim().toLowerCase();
        int type;
        switch (lowerData) {
            case "vollstationär":
                type = 1;
                break;
            case "teilstationär":
                type = 2;
                break;
            case "voll- und teilstationär":
                type = 3;
                break;
            default:
                type = 0;
        }
        if (type == 0) {
            counter.addColumnErrorMsg(errorMsg + " " + data + " (Erlaubte Werte: Vollstationär, Teilstationär, Voll. und Teilstationär)");
        }
        assign.accept(item, type);
    }

    static <T> void tryImportDouble(T item, String data, BiConsumer<T, Double> assign, String errorMsg, ErrorCounter counter) {
        try {
            double val = NumberParser.parseDouble(data);
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

    static <T> void tryImportDoubleBetween0and1(T item, String data, BiConsumer<T, Double> assign, String errorMsg, ErrorCounter counter) {
        try {
            double val = NumberParser.parseDouble(data);
            if (val < 0.0) {
                assign.accept(item, 0.0);
                counter.addColumnErrorMsg(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
            }
            if (val > 1.0) {
                assign.accept(item, 0.0);
                counter.addColumnErrorMsg(errorMsg + "Wert darf nicht größer als 1.0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
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
