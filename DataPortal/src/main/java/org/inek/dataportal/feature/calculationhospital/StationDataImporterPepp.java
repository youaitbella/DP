/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.BiConsumer;
import javax.servlet.http.Part;
import org.inek.dataportal.entities.calc.KGPListStationServiceCost;
import org.inek.dataportal.entities.calc.PeppCalcBasics;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public class StationDataImporterPepp {
    
    //<editor-fold defaultstate="collapsed" desc="file">
    private Part _file;
    private int _columnCnt;
    
    public Part getFile() {
        return _file;
    }
    
    public void setFile(Part file) {
        _file = file;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="show journal">
    private boolean _showJournal = false;
    
    public boolean isShowJournal() {
        return _showJournal;
    }
    
    public void setShowJournal(boolean showJournal) {
        this._showJournal = showJournal;
    }
    
    public void toggleJournal() {
        _showJournal = !_showJournal;
    }
    //</editor-fold>
    
    public void downloadTemplate() {
        Utils.downloadText(HEADLINE + "\n", "Station_kstg_21_22.csv");
    }


    public void uploadNotices() {
        try {
            if (_file != null) {
                Scanner scanner = new Scanner(_file.getInputStream());
                if (!scanner.hasNextLine()) {
                    return;
                }
                _columnCnt = HEADLINE.split(";").length;
                while (scanner.hasNextLine()) {
                    String line = Utils.convertFromUtf8(scanner.nextLine());
                    if (!line.equals(HEADLINE)) {
                        tryImportLine(line);
                    }
                }
                _showJournal = _errorMsg.contains("Fehler");
            }
        } catch (IOException | NoSuchElementException e) {
        }
    }

    
    private static final String HEADLINE = "Nummer der Kostenstelle;Station;Eindeutige Zuordnung nach Psych-PV* (A, S, G, KJP, P);Anzahl Betten;bettenführende Aufnahmestation (bitte ankreuzen);Summe Pflegetage Regelbehandlung;Summe Gewichtungspunkte** Regelbehandlung;Summe Pflegetage Intensivbehandlung;Summe Gewichtungspunkte** Intensivbehandlung;VK Ärztlicher Dienst;VK Pflegedienst/Erziehungsdienst;VK Psychologen;VK Sozialarbeiter/Sozial-/Heil-pädagogen;VK Spezialtherapeuten;VK med.-techn. Dienst/Funktionsdienst;Kosten Ärztlicher Dienst;Kosten Pflegedienst/Erziehungsdienst;Kosten Psychologen;Kosten Sozialarbeiter/Sozial-/Heil-pädagogen;Kosten Spezialtherapeuten;Kosten med.-techn. Dienst/Funktionsdienst;Kosten med. Infrastruktur;Kosten nicht med. Infrastruktur";
    private String _errorMsg = "";
    private int _totalCount = 0;

    public int getTotalCount() {
        return _totalCount;
    }

    private int _errorRowCount = 0;
    private int _errorColumnCount = 0;
    private int _infoColumnCount = 0;

    public int getErrorRowCount() {
        return _errorRowCount;
    }

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
        return (_totalCount - _errorRowCount) + " von " + _totalCount + " Zeilen gelesen\r\n\r\n" 
                + _errorColumnCount + " fehlerhafte Spalte(n) eingelesen\n" 
                + _infoColumnCount + " nicht angegebene Werte\n\n" + _errorMsg;
    }
    
    public void tryImportLine(String line) {
        _totalCount++;
        try {
            List<String> data = splitAtUnquotedSemicolon(line);
            if (data.size() != _columnCnt) {
                throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
            }
            KGPListStationServiceCost item = new KGPListStationServiceCost();
            item.setBaseInformationId(_calcBasics.getId());
         
//            Nummer der Kostenstelle;
//            Station;
//            Eindeutige Zuordnung nach Psych-PV* (A, S, G, KJP, P);
//            Anzahl Betten;
//            bettenführende Aufnahmestation (bitte ankreuzen);
//            Summe Pflegetage Regelbehandlung;
//            Summe Gewichtungspunkte** Regelbehandlung;
//            Summe Pflegetage Intensivbehandlung;
//            Summe Gewichtungspunkte** Intensivbehandlung;
//            VK Ärztlicher Dienst;
//            VK Pflegedienst/Erziehungsdienst;
//            VK Psychologen;
//            VK Sozialarbeiter/Sozial-/Heil-pädagogen;
//            VK Spezialtherapeuten;
//            VK med.-techn. Dienst/Funktionsdienst;
//            Kosten Ärztlicher Dienst;
//            Kosten Pflegedienst/Erziehungsdienst;
//            Kosten Psychologen;
//            Kosten Sozialarbeiter/Sozial-/Heil-pädagogen;
//            Kosten Spezialtherapeuten;
//            Kosten med.-techn. Dienst/Funktionsdienst;
//            Kosten med. Infrastruktur;
//            Kosten nicht med. Infrastruktur

            tryImportString(item, data.get(0), (i,s) -> i.setCostCenterNumber(s), "Nummer der Kostenstelle ungültig: ");
            tryImportString(item, data.get(1), (i,s) -> i.setStation(s), "Name der Station ungültig: ");
            tryImportString(item, data.get(2), (i,s) -> i.setPsyPvMapping(s), "Eindeutige Zuordnung nach Psych-PV* (A, S, G, KJP, P) ungültig: ");
            tryImportInteger(item, data.get(3), (i,s) -> i.setBedCnt(s), "Anzahl Betten ungültig: ");
            tryImportBoolean(item, data.get(4), (i,s) -> i.setReceivingStation(s), "bettenführende Aufnahmestation (bitte ankreuzen) ungültig: ");
            tryImportInteger(item, data.get(5), (i,s) -> i.setRegularCareDays(s), "Summe Pflegetage Regelbehandlung ungültig: ");
            tryImportInteger(item, data.get(6), (i,s) -> i.setRegularWeight(s), "Summe Gewichtungspunkte** Regelbehandlung ungültig: ");
            tryImportInteger(item, data.get(7), (i,s) -> i.setIntensiveCareDays(s), "Summe Pflegetage Intensivbehandlung ungültig: ");
            tryImportInteger(item, data.get(8), (i,s) -> i.setIntensiveWeight(s), "Summe Gewichtungspunkte** Intensivbehandlung ungültig: ");
            tryImportDouble(item, data.get(9), (i,s) -> i.setMedicalServiceCnt(s), "VK Ärztlicher Dienst ungültig: ");
            tryImportDouble(item, data.get(10), (i,s) -> i.setNursingServiceCnt(s), "VK Pflegedienst/Erziehungsdienst ungültig: ");
            tryImportDouble(item, data.get(11), (i,s) -> i.setPsychologistCnt(s), "VK Psychologen ungültig: ");
            tryImportDouble(item, data.get(12), (i,s) -> i.setSocialWorkerCnt(s), "VK Sozialarbeiter/Sozial-/Heil-pädagogen ungültig: ");
            tryImportDouble(item, data.get(13), (i,s) -> i.setSpecialTherapistCnt(s), "VK Spezialtherapeuten ungültig: ");
            tryImportDouble(item, data.get(14), (i,s) -> i.setFunctionalServiceCnt(s), "VK med.-techn. Dienst/Funktionsdienst ungültig: ");
            tryImportInteger(item, data.get(15), (i,s) -> i.setMedicalServiceAmount(s), "Kosten Ärztlicher Dienst ungültig: ");
            tryImportInteger(item, data.get(16), (i,s) -> i.setNursingServiceAmount(s), "Kosten Pflegedienst/Erziehungsdienst ungültig: ");
            tryImportInteger(item, data.get(17), (i,s) -> i.setPsychologistAmount(s), "Kosten Psychologen ungültig: ");
            tryImportInteger(item, data.get(18), (i,s) -> i.setSocialWorkerAmount(s), "Kosten Sozialarbeiter/Sozial-/Heil-pädagogen ungültig: ");
            tryImportInteger(item, data.get(19), (i,s) -> i.setSpecialTherapistAmount(s), "Kosten Spezialtherapeuten ungültig: ");
            tryImportInteger(item, data.get(20), (i,s) -> i.setFunctionalServiceAmount(s), "Kosten med.-techn. Dienst/Funktionsdienst ungültig: ");
            tryImportInteger(item, data.get(21), (i,s) -> i.setMedicalInfrastructureAmount(s), "Kosten med. Infrastruktur ungültig: ");
            tryImportInteger(item, data.get(22), (i,s) -> i.setNonMedicalInfrastructureAmount(s), "Kosten nicht med. Infrastruktur ungültig: ");
                        
            if(itemExists(item)) {
                return;
            }

            _calcBasics.getStationServiceCosts().add(item);
        } catch (IllegalArgumentException ex) {
            addRowErrorMsg(ex.getMessage());
        }
    }

    private List<String> splitAtUnquotedSemicolon(String line) {
        if (line.endsWith(";")) {
            line = line + " ";
        }

        int start = 0;
        List<String> toks = new ArrayList<>();
        boolean withinQuote = false;
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
                    withinQuote = !withinQuote;
                    break;
            }
        }
        if (start < line.length()) {
            toks.add(line.substring(start));
        }

        for (int i = 0; i < toks.size(); i++) {
            String trimmedTok = toks.get(i).trim();
            if (trimmedTok.startsWith("\"") && trimmedTok.endsWith("\"")) {
                toks.set(i, trimmedTok.substring(1, trimmedTok.length()-1));
            }
        }
        
        return toks;
    }
    
    private void addRowErrorMsg(String message) {
        _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + message;
        _errorRowCount++;
    }
    
    private void addColumnErrorMsg(String message) {
        _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + message;
        _errorColumnCount++;
    }
    
    private void addColumnInfoMsg(String message) {
        _errorMsg += "\r\nHinweis in Zeile " + _totalCount + ": " + message;
        _infoColumnCount++;
    }
    
    private void addChangeColumn(String oldVal, String newVal) {
        _errorMsg += "\r\nZeile "+_totalCount+" bereits vorhanden. Spalte aktualisiert : alt " + oldVal + " neu " + newVal;
    }
    
    private void addChangeColumn(boolean oldVal, boolean newVal) {
        _errorMsg += "\r\nZeile "+_totalCount+" bereits vorhanden. Spalte aktualisiert : alt " + (oldVal ? "x" : " ") + " neu " + (newVal ? "x" : " ");
    }
    
    private void addChangeColumn(int oldVal, int newVal) {
        addChangeColumn(String.valueOf(oldVal), String.valueOf(newVal));
    }
    
    private void addChangeColumn(double oldVal, double newVal) {
        addChangeColumn(String.valueOf(oldVal), String.valueOf(newVal));
    }
    
    private boolean itemExists(KGPListStationServiceCost item) {
        for (KGPListStationServiceCost costs : _calcBasics.getStationServiceCosts()) {
            if (costs.getCostCenterNumber().equals(item.getCostCenterNumber()) &&
                    costs.getStation().equals(item.getStation())
                    ) {
                boolean valueChanged = false;

                if (!costs.getPsyPvMapping().equalsIgnoreCase(item.getPsyPvMapping())) {
                    addChangeColumn(costs.getPsyPvMapping(), item.getPsyPvMapping());
                    costs.setPsyPvMapping(item.getPsyPvMapping());
                    valueChanged = true;
                }
                if (costs.getBedCnt() != item.getBedCnt()) {
                    addChangeColumn(costs.getBedCnt(), item.getBedCnt());
                    costs.setBedCnt(item.getBedCnt());
                    valueChanged = true;
                }
                if (costs.isReceivingStation() != item.isReceivingStation()) {
                    addChangeColumn(costs.isReceivingStation(), item.isReceivingStation());
                    costs.setReceivingStation(item.isReceivingStation());
                    valueChanged = true;
                }
                if (costs.getRegularCareDays() != item.getRegularCareDays()) {
                    addChangeColumn(costs.getRegularCareDays(), item.getRegularCareDays());
                    costs.setRegularCareDays(item.getRegularCareDays());
                    valueChanged = true;
                }
                if (costs.getRegularWeight() != item.getRegularWeight()) {
                    addChangeColumn(costs.getRegularWeight(), item.getRegularWeight());
                    costs.setRegularWeight(item.getRegularWeight());
                    valueChanged = true;
                }
                if (costs.getIntensiveCareDays() != item.getIntensiveCareDays()) {
                    addChangeColumn(costs.getIntensiveCareDays(), item.getIntensiveCareDays());
                    costs.setIntensiveCareDays(item.getIntensiveCareDays());
                    valueChanged = true;
                }
                if (costs.getIntensiveWeight() != item.getIntensiveWeight()) {
                    addChangeColumn(costs.getIntensiveWeight(), item.getIntensiveWeight());
                    costs.setIntensiveWeight(item.getIntensiveWeight());
                    valueChanged = true;
                }
                if (costs.getMedicalServiceCnt() != item.getMedicalServiceCnt()) {
                    addChangeColumn(costs.getMedicalServiceCnt(), item.getMedicalServiceCnt());
                    costs.setMedicalServiceCnt(item.getMedicalServiceCnt());
                    valueChanged = true;
                }
                if (costs.getNursingServiceCnt() != item.getNursingServiceCnt()) {
                    addChangeColumn(costs.getNursingServiceCnt(), item.getNursingServiceCnt());
                    costs.setNursingServiceCnt(item.getNursingServiceCnt());
                    valueChanged = true;
                }
                if (costs.getPsychologistCnt() != item.getPsychologistCnt()) {
                    addChangeColumn(costs.getPsychologistCnt(), item.getPsychologistCnt());
                    costs.setPsychologistCnt(item.getPsychologistCnt());
                    valueChanged = true;
                }
                if (costs.getSocialWorkerCnt() != item.getSocialWorkerCnt()) {
                    addChangeColumn(costs.getSocialWorkerCnt(), item.getSocialWorkerCnt());
                    costs.setSocialWorkerCnt(item.getSocialWorkerCnt());
                    valueChanged = true;
                }
                if (costs.getSpecialTherapistCnt() != item.getSpecialTherapistCnt()) {
                    addChangeColumn(costs.getSpecialTherapistCnt(), item.getSpecialTherapistCnt());
                    costs.setSpecialTherapistCnt(item.getSpecialTherapistCnt());
                    valueChanged = true;
                }
                if (costs.getFunctionalServiceCnt() != item.getFunctionalServiceCnt()) {
                    addChangeColumn(costs.getFunctionalServiceCnt(), item.getFunctionalServiceCnt());
                    costs.setFunctionalServiceCnt(item.getFunctionalServiceCnt());
                    valueChanged = true;
                }
                if (costs.getMedicalServiceAmount() != item.getMedicalServiceAmount()) {
                    addChangeColumn(costs.getMedicalServiceAmount(), item.getMedicalServiceAmount());
                    costs.setMedicalServiceAmount(item.getMedicalServiceAmount());
                    valueChanged = true;
                }
                if (costs.getNursingServiceAmount() != item.getNursingServiceAmount()) {
                    addChangeColumn(costs.getNursingServiceAmount(), item.getNursingServiceAmount());
                    costs.setNursingServiceAmount(item.getNursingServiceAmount());
                    valueChanged = true;
                }
                if (costs.getPsychologistAmount() != item.getPsychologistAmount()) {
                    addChangeColumn(costs.getPsychologistAmount(), item.getPsychologistAmount());
                    costs.setPsychologistAmount(item.getPsychologistAmount());
                    valueChanged = true;
                }
                if (costs.getSocialWorkerAmount() != item.getSocialWorkerAmount()) {
                    addChangeColumn(costs.getSocialWorkerAmount(), item.getSocialWorkerAmount());
                    costs.setSocialWorkerAmount(item.getSocialWorkerAmount());
                    valueChanged = true;
                }
                if (costs.getSpecialTherapistAmount() != item.getSpecialTherapistAmount()) {
                    addChangeColumn(costs.getSpecialTherapistAmount(), item.getSpecialTherapistAmount());
                    costs.setSpecialTherapistAmount(item.getSpecialTherapistAmount());
                    valueChanged = true;
                }
                if (costs.getFunctionalServiceAmount() != item.getFunctionalServiceAmount()) {
                    addChangeColumn(costs.getFunctionalServiceAmount(), item.getFunctionalServiceAmount());
                    costs.setFunctionalServiceAmount(item.getFunctionalServiceAmount());
                    valueChanged = true;
                }
                if (costs.getMedicalInfrastructureAmount() != item.getMedicalInfrastructureAmount()) {
                    addChangeColumn(costs.getMedicalInfrastructureAmount(), item.getMedicalInfrastructureAmount());
                    costs.setMedicalInfrastructureAmount(item.getMedicalInfrastructureAmount());
                    valueChanged = true;
                }
                if (costs.getNonMedicalInfrastructureAmount() != item.getNonMedicalInfrastructureAmount()) {
                    addChangeColumn(costs.getNonMedicalInfrastructureAmount(), item.getNonMedicalInfrastructureAmount());
                    costs.setNonMedicalInfrastructureAmount(item.getNonMedicalInfrastructureAmount());
                    valueChanged = true;
                }
                
                return true;

            }
        }
        return false;
    }

    private void tryImportString(KGPListStationServiceCost item, String data, BiConsumer<KGPListStationServiceCost, String> bind, String errorMsg) {
        try {
            bind.accept(item, data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage() + "\n" + errorMsg + data);
        }
    }
    
    private void tryImportInteger(KGPListStationServiceCost item, String data, BiConsumer<KGPListStationServiceCost, Integer> bind, String errorMsg) {
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
            if (data.isEmpty()) {
                addColumnInfoMsg(errorMsg + "keinen Wert angegeben");
            } else {
                addColumnErrorMsg(errorMsg + Utils.getMessage("msgNotANumber") + ": " + data);
            }
        }
    }
    
    private void tryImportDouble(KGPListStationServiceCost item, String data, BiConsumer<KGPListStationServiceCost, Double> bind, String errorMsg) {
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(false);
            double val = nf.parse(data).doubleValue();
            if (val < 0){
                bind.accept(item, 0.0);
                addColumnErrorMsg(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
            } else {
                bind.accept(item, val);
            }
        } catch (ParseException ex) {
            bind.accept(item, 0.0);
            if (data.isEmpty()) {
                addColumnInfoMsg(errorMsg + "keinen Wert angegeben");
            } else {
                addColumnErrorMsg(errorMsg + Utils.getMessage("msgNotANumber") + ": " + data);
            }
        }
    }
    
    private void tryImportBoolean(KGPListStationServiceCost item, String data, BiConsumer<KGPListStationServiceCost, Boolean> bind, String errorMsg) {
        try {
            if ((data.trim().length() > 1) || (data.trim().length() == 1 && !data.trim().toLowerCase().equals("x"))) {
                bind.accept(item, false);
                addColumnErrorMsg(errorMsg + " ist nicht leer, x oder X : " + data);
            } else {
                bind.accept(item, data.trim().toLowerCase().equals("x"));
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage() + "\n" + errorMsg + data);
        }
    }
    
}
