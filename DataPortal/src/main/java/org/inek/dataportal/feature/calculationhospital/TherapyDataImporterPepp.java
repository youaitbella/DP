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

    private int _errorCount = 0;

    public int getErrorCount() {
        return _errorCount;
    }

    private PeppCalcBasics _calcBasics;

    void setCalcBasics(PeppCalcBasics notice) {
        _calcBasics = notice;
    }

    public boolean containsError() {
        return !_errorMsg.isEmpty();
    }

    public String getMessage() {
        return (_totalCount - _errorCount) + " von " + _totalCount + " Zeilen gelesen\r\n\r\n" + _errorMsg;
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
                _errorMsg += "\r\nZeile "+_totalCount+" bereits vorhanden.";
                return;
            }

            _calcBasics.getTherapies().add(item);
        } catch (IllegalArgumentException ex) {
            _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + ex.getMessage();
            _errorCount++;
        }
    }
    
    private boolean itemExists(KGPListTherapy item) {
        for (KGPListTherapy t : _calcBasics.getTherapies()) {
            if (t.getCostCenterId() == item.getCostCenterId() &&
                    t.getCostCenterText().equals(t.getCostCenterText()) &&
                    t.getExternalService() == item.getExternalService() &&
                    t.getKeyUsed().equals(t.getKeyUsed()) &&
                    t.getServiceUnitsCt1() == item.getServiceUnitsCt1() &&
                    t.getPersonalCostCt1()== item.getPersonalCostCt1() &&
                    t.getServiceUnitsCt3a() == item.getServiceUnitsCt3a() &&
                    t.getPersonalCostCt3a()== item.getPersonalCostCt3a() &&
                    t.getServiceUnitsCt2() == item.getServiceUnitsCt2() &&
                    t.getPersonalCostCt2()== item.getPersonalCostCt2() &&
                    t.getServiceUnitsCt3b() == item.getServiceUnitsCt3b() &&
                    t.getPersonalCostCt3b()== item.getPersonalCostCt3b() &&
                    t.getServiceUnitsCt3c() == item.getServiceUnitsCt3c() &&
                    t.getPersonalCostCt3c()== item.getPersonalCostCt3c() &&
                    t.getServiceUnitsCt3() == item.getServiceUnitsCt3() &&
                    t.getPersonalCostCt3()== item.getPersonalCostCt3() 
                    ) {
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
                throw new IllegalArgumentException(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
            }
            bind.accept(item, val);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(errorMsg + Utils.getMessage("msgNotANumber") + ": " + data);
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
            throw new IllegalArgumentException(errorMsg + " " + data);
        }
    }
}
