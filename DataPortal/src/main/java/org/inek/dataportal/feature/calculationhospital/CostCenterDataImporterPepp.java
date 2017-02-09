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
import org.inek.dataportal.entities.calc.KGPListCostCenter;
import org.inek.dataportal.entities.calc.PeppCalcBasics;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public class CostCenterDataImporterPepp {

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
            if (data.length != 8) {
                throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
            }
            KGPListCostCenter item = new KGPListCostCenter();
            item.setBaseInformationId(_calcBasics.getId());
            tryImportCostCenterId(item, data[0]);
            tryImportCostCenterNumber(item, data[1]);
            item.setCostCenterText(data[2]);
            tryImportCostVolume(item, data[3]);
            tryImportFullVigorCnt(item, data[4]);
            item.setServiceKey(data[5]);
            item.setServiceKeyDescription(data[6]);
            tryImportServiceSum(item, data[7]);
            
            _calcBasics.getCostCenters().add(item);
        } catch (IllegalArgumentException ex) {
            _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + ex.getMessage();
            _errorCount++;
        }
    }

    private void tryImportCostCenterId(KGPListCostCenter item, String dataString) {
        List<String> allowedValues = Arrays.asList("11", "12", "13");
        if (allowedValues.contains(dataString)){
            item.setCostCenterId(Integer.parseInt(dataString));
        } else{
            throw new IllegalArgumentException("Keine zulässige Kostenstellengruppe: " + dataString);
        }
    }

    private void tryImportCostCenterNumber(KGPListCostCenter item, String dataString) {
        try{
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(true);
            int val = nf.parse(dataString).intValue();
            item.setCostCenterNumber(val);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("[Nummer der Kostenstelle] " + Utils.getMessage("msgNotANumber") + ": " + dataString);
        }
    }

    private void tryImportCostVolume(KGPListCostCenter item, String dataString) {
        try{
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(true);
            int val = nf.parse(dataString).intValue();
            if (val < 0){
                 throw new IllegalArgumentException("[Kostenvolumen] Wert darf nicht kleiner 0 sein: " + dataString);
            }
            item.setAmount(val);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("[Kostenvolumen] " + Utils.getMessage("msgNotANumber") + ": " + dataString);
        }
    }

    private void tryImportFullVigorCnt(KGPListCostCenter item, String dataString) {
        try{
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(false);
            double val = nf.parse(dataString).doubleValue();
            if (val < 0){
                 throw new IllegalArgumentException("[Anzahl VK ÄD] Wert darf nicht kleiner 0 sein: " + dataString);
            }
            item.setFullVigorCnt(val);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("[Anzahl VK ÄD] " + Utils.getMessage("msgNotANumber") + ": " + dataString);
        }
    }

    private void tryImportServiceSum(KGPListCostCenter item, String dataString) {
        try{
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(true);
            int val = nf.parse(dataString).intValue();
            if (val < 0){
                 throw new IllegalArgumentException("[Summe der Leistungseinheiten] Wert darf nicht kleiner 0 sein: " + dataString);
            }
            item.setServiceSum(val);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("[Summe der Leistungseinheiten] " + Utils.getMessage("msgNotANumber") + ": " + dataString);
        }
    }

}