/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import org.inek.dataportal.entities.calc.KGPListTherapy;
import org.inek.dataportal.entities.calc.PeppCalcBasics;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public class TherapyDataImporterPepp {

    private String _errorMsg = "";
    private int _totalCount = 0;

    public int getTotalCount() {
        return _totalCount;
    }

    private int _errorRowCount = 0;

    public int getErrorRowCount() {
        return _errorRowCount;
    }

    private int _errorColumnCount = 0;

    public int getErrorColumnCount() {
        return _errorColumnCount;
    }

    private PeppCalcBasics _calcBasics;

    void setCalcBasics(PeppCalcBasics notice) {
        _calcBasics = notice;
    }

    public boolean containsError() {
        return !_errorMsg.isEmpty();
    }

    public String getMessage() {
        return (_totalCount - _errorRowCount) + " von " + _totalCount + " Zeilen gelesen\r\n\r\n" + _errorMsg;
    }
    
    public void tryImportLine(String line) {
        _totalCount++;
        try {
            if (line.endsWith(";")) {
                line = line + " ";
            }
            String[] data = line.split(";");
            if (data.length != 16) {
                throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
            }
            KGPListTherapy item = new KGPListTherapy();
            item.setBaseInformationId(_calcBasics.getId());
            
            tryImportInteger(item, data[0], (i,s) -> i.setCostCenterId(s), "Keine zulässige KST-Gruppe (23-26) : ");
            tryImportString(item, data[1], (i,s) -> i.setCostCenterText(s), "Ungültige Zeichenkette: ");
            tryImportFremdvergabe(item, data[2], (i,s) -> i.setExternalService(s), "Keine zulässige Leistungserbringung 'Keine, Teilweise, Vollständige Fremdvergabe' : ");
            
            tryImportString(item, data[3], (i,s) -> i.setKeyUsed(s), "Kein gültiger Leistungsschlüssel: ");
            tryImportInteger(item, data[4], (i,s) -> i.setServiceUnitsCt1(s), "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 1: ");
            tryImportInteger(item, data[5], (i,s) -> i.setPersonalCostCt1(s), "Ungültiger Wert für Personalkosten KoArtGr 1: ");
            tryImportInteger(item, data[6], (i,s) -> i.setServiceUnitsCt3a(s), "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 3a: ");
            tryImportInteger(item, data[7], (i,s) -> i.setPersonalCostCt3a(s), "Ungültiger Wert für Personalkosten KoArtGr 3a: ");
            tryImportInteger(item, data[8], (i,s) -> i.setServiceUnitsCt2(s), "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 2: ");
            tryImportInteger(item, data[9], (i,s) -> i.setPersonalCostCt2(s), "Ungültiger Wert für Personalkosten KoArtGr 2: ");
            tryImportInteger(item, data[10], (i,s) -> i.setServiceUnitsCt3b(s), "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 3b: ");
            tryImportInteger(item, data[11], (i,s) -> i.setPersonalCostCt3b(s), "Ungültiger Wert für Personalkosten KoArtGr 3b: ");
            tryImportInteger(item, data[12], (i,s) -> i.setServiceUnitsCt3c(s), "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 3c: ");
            tryImportInteger(item, data[13], (i,s) -> i.setPersonalCostCt3c(s), "Ungültiger Wert für Personalkosten KoArtGr 3c: ");
            tryImportInteger(item, data[14], (i,s) -> i.setServiceUnitsCt3(s), "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 3: ");
            tryImportInteger(item, data[15], (i,s) -> i.setPersonalCostCt3(s), "Ungültiger Wert für Personalkosten KoArtGr 3: ");
                        
            if(itemExists(item)) {
                return;
            }

            _calcBasics.getTherapies().add(item);
        } catch (IllegalArgumentException ex) {
            addRowErrorMsg(ex.getMessage());
        }
    }
    
    private void addRowErrorMsg(String message) {
        _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + message;
        _errorRowCount++;
    }
    
    private void addColumnErrorMsg(String message) {
        _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + message;
        _errorColumnCount++;
    }
    
    private void addChangeColumn(int oldVal, int newVal) {
        _errorMsg += "\r\nZeile "+_totalCount+" bereits vorhanden. Spalte aktualisiert : alt " + oldVal + " neu " + newVal;
    }
    
    private boolean itemExists(KGPListTherapy item) {
        for (KGPListTherapy t : _calcBasics.getTherapies()) {
            if (t.getCostCenterId() == item.getCostCenterId() &&
                    t.getCostCenterText().equals(t.getCostCenterText()) &&
                    t.getExternalService() == item.getExternalService() &&
                    t.getKeyUsed().equals(t.getKeyUsed())) {
                
                boolean valueChanged = false;

                if (t.getServiceUnitsCt1() != item.getServiceUnitsCt1()) {
                    addChangeColumn(t.getServiceUnitsCt1(), item.getServiceUnitsCt1());
                    t.setServiceUnitsCt1(item.getServiceUnitsCt1());
                    valueChanged = true;
                }
                if (t.getPersonalCostCt1() != item.getPersonalCostCt1()) {
                    addChangeColumn(t.getPersonalCostCt1(), item.getPersonalCostCt1());
                    t.setPersonalCostCt1(item.getPersonalCostCt1());
                    valueChanged = true;
                }
                if (t.getServiceUnitsCt3a() != item.getServiceUnitsCt3a()) {
                    addChangeColumn(t.getServiceUnitsCt3a(), item.getServiceUnitsCt3a());
                    t.setServiceUnitsCt3a(item.getServiceUnitsCt3a());
                    valueChanged = true;
                }
                if (t.getPersonalCostCt3a() != item.getPersonalCostCt3a()) {
                    addChangeColumn(t.getPersonalCostCt3a(), item.getPersonalCostCt3a());
                    t.setPersonalCostCt3a(item.getPersonalCostCt3a());
                    valueChanged = true;
                }
                if (t.getServiceUnitsCt2() != item.getServiceUnitsCt2()) {
                    addChangeColumn(t.getServiceUnitsCt2(), item.getServiceUnitsCt2());
                    t.setServiceUnitsCt2(item.getServiceUnitsCt2());
                    valueChanged = true;
                }
                if (t.getPersonalCostCt2() != item.getPersonalCostCt2()) {
                    addChangeColumn(t.getPersonalCostCt2(), item.getPersonalCostCt2());
                    t.setPersonalCostCt2(item.getPersonalCostCt2());
                    valueChanged = true;
                }
                if (t.getServiceUnitsCt3b() != item.getServiceUnitsCt3b()) {
                    addChangeColumn(t.getServiceUnitsCt3b(), item.getServiceUnitsCt3b());
                    t.setServiceUnitsCt3b(item.getServiceUnitsCt3b());
                    valueChanged = true;
                }
                if (t.getPersonalCostCt3b() != item.getPersonalCostCt3b()) {
                    addChangeColumn(t.getPersonalCostCt3b(), item.getPersonalCostCt3b());
                    t.setPersonalCostCt3b(item.getPersonalCostCt3b());
                    valueChanged = true;
                }
                if (t.getServiceUnitsCt3c() != item.getServiceUnitsCt3c()) {
                    addChangeColumn(t.getServiceUnitsCt3c(), item.getServiceUnitsCt3c());
                    t.setServiceUnitsCt3c(item.getServiceUnitsCt3c());
                    valueChanged = true;
                }
                if (t.getPersonalCostCt3c() != item.getPersonalCostCt3c()) {
                    addChangeColumn(t.getPersonalCostCt3c(), item.getPersonalCostCt3c());
                    t.setPersonalCostCt3c(item.getPersonalCostCt3c());
                    valueChanged = true;
                }
                if (t.getServiceUnitsCt3() != item.getServiceUnitsCt3()) {
                    addChangeColumn(t.getServiceUnitsCt3(), item.getServiceUnitsCt3());
                    t.setServiceUnitsCt3(item.getServiceUnitsCt3());
                    valueChanged = true;
                }
                if (t.getPersonalCostCt3() != item.getPersonalCostCt3()) {
                    addChangeColumn(t.getPersonalCostCt3(), item.getPersonalCostCt3());
                    t.setPersonalCostCt3(item.getPersonalCostCt3());
                    valueChanged = true;
                }
                if (!valueChanged) {
                    _errorMsg += "\r\nZeile "+_totalCount+" bereits vorhanden.";
                }
                return true;
            }
        }
        return false;
    }

    private void tryImportString(KGPListTherapy item, String data, BiConsumer<KGPListTherapy, String> bind, String errorMsg) {
        try {
            bind.accept(item, data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage() + "\n" + errorMsg + data);
        }
    }
    
    private void tryImportInteger(KGPListTherapy item, String data, BiConsumer<KGPListTherapy, Integer> bind, String errorMsg) {
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(true);
            int val = nf.parse(data).intValue();
            if (val < 0){
                bind.accept(item, 0);
                addColumnErrorMsg(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
            } else {
                bind.accept(item, val);
            }
        } catch (ParseException ex) {
            bind.accept(item, 0);
            addColumnErrorMsg(errorMsg + Utils.getMessage("msgNotANumber") + ": " + data);
        }
    }
    
    private void tryImportDouble(KGPListTherapy item, String data, BiConsumer<KGPListTherapy, Double> bind, String errorMsg) {
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(false);
            double val = nf.parse(data).doubleValue();
            if (val < 0){
                throw new IllegalArgumentException(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
            }
            bind.accept(item, val);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(errorMsg + Utils.getMessage("msgNotANumber") + ": " + data);
        }
    }
    
    private void tryImportBoolean(KGPListTherapy item, String data, BiConsumer<KGPListTherapy, Boolean> bind, String errorMsg) {
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

    private void tryImportFremdvergabe(KGPListTherapy item, String data, BiConsumer<KGPListTherapy, Integer> bind, String errorMsg) {
        String lowerData = data.trim().toLowerCase();
        if (lowerData.startsWith("keine")) {
            bind.accept(item, 0);
        } else if (lowerData.startsWith("teilweise")) {
            bind.accept(item, 1);
        } else if (lowerData.startsWith("vollständig")) {
            bind.accept(item, 2);
        } else {
            bind.accept(item, 0);
            addColumnErrorMsg(errorMsg + " " + data);
        }
    }
}
