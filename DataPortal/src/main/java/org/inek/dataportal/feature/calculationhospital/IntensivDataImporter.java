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
import java.util.function.Consumer;
import org.inek.dataportal.entities.calc.DrgCalcBasics;
import org.inek.dataportal.entities.calc.KGLListCostCenter;
import org.inek.dataportal.entities.calc.KGLListIntensivStroke;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.StringUtil;

/**
 *
 * @author muellermi
 */
public class IntensivDataImporter {

    private String _errorMsg = "";
    private int _totalCount = 0;

    public int getTotalCount() {
        return _totalCount;
    }

    private int _errorCount = 0;

    public int getErrorCount() {
        return _errorCount;
    }

    private DrgCalcBasics _calcBasics;

    void setCalcBasics(DrgCalcBasics notice) {
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
            String[] data = StringUtil.splitAtUnquotedSemicolon(line);
            if (data.length != 22) {
                throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount") + " Erwartet: 22, gefunden: " + data.length);
            }
            KGLListIntensivStroke item = new KGLListIntensivStroke();
            item.setBaseInformationId(_calcBasics.getId());
            item.setIntensiveType(1);
            tryImportString(item,  data[0], (i,s) -> i.setCostCenterText(s), "Kein zulässiger Intensivstationsname: ");
            tryImportString(item,  data[1], (i,s) -> i.setDepartmentAssignment(s), "Kein zulässiger Abteilungsname: ");
            tryImportInteger(item, data[2], (i,s) -> i.setBedCnt(s), "[Anzahl Betten] ");
            tryImportInteger(item, data[3], (i,s) -> i.setCaseCnt(s), "[Anzahl Fälle] ");
            tryImportBoolean(item, data[4], (i,s) -> i.setOps8980(s), "[Mindestmerkmale OPS 8-980 erfüllt] ");
            tryImportBoolean(item, data[5], (i,s) -> i.setOps898f(s), "[Mindestmerkmale OPS 8-98f erfüllt] ");
            tryImportString(item,  data[6], (i,s) -> i.setMinimumCriteriaPeriod(s), "Nicht zulässig [Mindestmerkmale nur erfüllt im Zeitabschnitt: ");
            tryImportInteger(item, data[7], (i,s) -> i.setIntensivHoursWeighted(s), "[Summe_gewichtete_Intensivstunden] ");
            tryImportInteger(item, data[8], (i,s) -> i.setIntensivHoursNotweighted(s), "[Summe_ungewichtete_Intensivstunden] ");
            tryImportInteger(item, data[9], (i,s) -> i.setWeightMinimum(s), "[Minimum] ");
            tryImportInteger(item, data[10], (i,s) -> i.setWeightMaximum(s), "[Maximum] ");
            tryImportString(item,  data[11], (i,s) -> i.setWeightDescription(s), "Nicht zulässig [Erläuterung] : ");
            tryImportDouble(item,  data[12], (i,s) -> i.setMedicalServiceCnt(s), "[Vollkraft ÄD] ");
            tryImportDouble(item,  data[13], (i,s) -> i.setNursingServiceCnt(s), "[Vollkraft PD] ");
            tryImportDouble(item,  data[14], (i,s) -> i.setFunctionalServiceCnt(s), "[Vollkraft FD] ");
            tryImportInteger(item, data[15], (i,s) -> i.setMedicalServiceCost(s), "[Kosten_ÄD] ");
            tryImportInteger(item, data[16], (i,s) -> i.setNursingServiceCost(s), "[Kosten_PD] ");
            tryImportInteger(item, data[17], (i,s) -> i.setFunctionalServiceCost(s), "[Kosten_FD] ");
            tryImportInteger(item, data[18], (i,s) -> i.setOverheadsMedicine(s), "[Kosten_GK_Arzneimittel] ");
            tryImportInteger(item, data[19], (i,s) -> i.setOverheadMedicalGoods(s), "[Kosten_GK_med_Sachbedarf] ");
            tryImportInteger(item, data[20], (i,s) -> i.setMedicalInfrastructureCost(s), "[Kosten_med_Infra] ");
            tryImportInteger(item, data[21], (i,s) -> i.setNonMedicalInfrastructureCost(s), "[Kosten_nicht_med_Infra] ");
            
            if (itemExists(item)) {
                _errorMsg += "\r\nZeile "+_totalCount+" bereits vorhanden.";
                return;
            }
            
            _calcBasics.getIntensivStrokes().add(item);
        } catch (IllegalArgumentException ex) {
            _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + ex.getMessage();
            _errorCount++;
        }
    }

    private boolean itemExists(KGLListIntensivStroke item) {
        for(KGLListIntensivStroke is : _calcBasics.getIntensivStrokes()) {
            if( is.getIntensiveType() == item.getIntensiveType() &&
                    is.getCostCenterText().equals(item.getCostCenterText()) &&
                    is.getDepartmentAssignment().equals(item.getDepartmentAssignment()) &&
                    is.getIntensiveType()== item.getIntensiveType() &&
                    is.getBedCnt() == item.getBedCnt() &&
                    is.getCaseCnt() == item.getCaseCnt() &&
                    is.getOps8980() == item.getOps8980() &&
                    is.getOps898f() == item.getOps898f() &&
                    is.getOps8981() == item.getOps8981() &&
                    is.getOps898b() == item.getOps898b() &&
                    is.getMinimumCriteriaPeriod().equals(item.getMinimumCriteriaPeriod()) &&
                    is.getWeightDescription().equals(item.getWeightDescription()) &&
                    is.getIntensivHoursWeighted() == item.getIntensivHoursWeighted() &&
                    is.getIntensivHoursNotweighted() == item.getIntensivHoursNotweighted() &&
                    is.getWeightMinimum() == item.getWeightMinimum() &&
                    is.getWeightMaximum() == item.getWeightMaximum() &&
                    is.getMedicalServiceCnt() == item.getMedicalServiceCnt() &&
                    is.getNursingServiceCnt() == item.getNursingServiceCnt() &&
                    is.getFunctionalServiceCnt() == item.getFunctionalServiceCnt() &&
                    is.getMedicalServiceCost() == item.getMedicalServiceCost() &&
                    is.getNursingServiceCost() == item.getNursingServiceCost() &&
                    is.getFunctionalServiceCost() == item.getFunctionalServiceCost() &&
                    is.getOverheadsMedicine() == item.getOverheadsMedicine() &&
                    is.getOverheadMedicalGoods() == item.getOverheadMedicalGoods() &&
                    is.getMedicalInfrastructureCost() == item.getMedicalInfrastructureCost() &&
                    is.getNonMedicalInfrastructureCost() == item.getNonMedicalInfrastructureCost() )
                    
                return true;
        }
        return false;
    }
    
    private void tryImportString(KGLListIntensivStroke item, String data, BiConsumer<KGLListIntensivStroke, String> bind, String errorMsg) {
        try {
            bind.accept(item, data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage() + "\n" + errorMsg + data);
        }
    }
    
    private void tryImportInteger(KGLListIntensivStroke item, String data, BiConsumer<KGLListIntensivStroke, Integer> bind, String errorMsg) {
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
    
    private void tryImportDouble(KGLListIntensivStroke item, String data, BiConsumer<KGLListIntensivStroke, Double> bind, String errorMsg) {
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
    
    private void tryImportBoolean(KGLListIntensivStroke item, String data, BiConsumer<KGLListIntensivStroke, Boolean> bind, String errorMsg) {
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
    
}
