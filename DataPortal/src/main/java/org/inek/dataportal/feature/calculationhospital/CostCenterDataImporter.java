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
import org.inek.dataportal.entities.calc.DrgCalcBasics;
import org.inek.dataportal.entities.calc.KGLListCostCenter;
import org.inek.dataportal.helper.BeanValidator;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.StringUtil;

/**
 *
 * @author muellermi
 */
public class CostCenterDataImporter {

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
            if (data.length != 8) {
                throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
            }
            KGLListCostCenter item = new KGLListCostCenter();
            item.setBaseInformationId(_calcBasics.getId());
            tryImportCostCenterId(item, data[0]);
            tryImportCostCenterNumber(item, data[1]);
            item.setCostCenterText(data[2]);
            tryImportCostVolume(item, data[3]);
            tryImportFullVigorCnt(item, data[4]);
            item.setServiceKey(data[5]);
            item.setServiceKeyDescription(data[6]);
            tryImportServiceSum(item, data[7]);
            
            String validateText = BeanValidator.validateData(item);
            if (!validateText.isEmpty()) {
                throw new IllegalArgumentException(validateText);
            }
            
            if(itemExists(item)) {
                _errorMsg += "\r\nZeile "+_totalCount+" bereits vorhanden.";
                return;
            }
            
            _calcBasics.getCostCenters().add(item);
        } catch (IllegalArgumentException ex) {
            _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + ex.getMessage();
            _errorCount++;
        }
    }
    
    private boolean itemExists(KGLListCostCenter item) {
        for(KGLListCostCenter cc : _calcBasics.getCostCenters()) {
            if( cc.getAmount() == item.getAmount() &&
                cc.getCostCenterId() == item.getCostCenterId() &&
                cc.getCostCenterNumber() == item.getCostCenterNumber() &&
                cc.getCostCenterText().equals(item.getCostCenterText()) &&
                cc.getFullVigorCnt() == item.getFullVigorCnt() &&
                cc.getServiceKey().equals(item.getServiceKey()) &&
                cc.getServiceKeyDescription().equals(item.getServiceKeyDescription()) &&
                cc.getServiceSum() == item.getServiceSum())
                return true;
        }
        return false;
    }

    private void tryImportCostCenterId(KGLListCostCenter item, String dataString) {
        List<String> allowedValues = Arrays.asList("11", "12", "13");
        if (allowedValues.contains(dataString)){
            item.setCostCenterId(Integer.parseInt(dataString));
        } else{
            throw new IllegalArgumentException("Keine zulässige Kostenstellengruppe: " + dataString);
        }
    }

    private void tryImportCostCenterNumber(KGLListCostCenter item, String dataString) {
        try{
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(true);
            int val = nf.parse(dataString).intValue();
            item.setCostCenterNumber(val);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("[Nummer der Kostenstelle] " + Utils.getMessage("msgNotAnInteger") + ": " + dataString);
        }
    }

    private void tryImportCostVolume(KGLListCostCenter item, String dataString) {
        try{
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(true);
            int val = nf.parse(dataString).intValue();
            if (val < 0){
                 throw new IllegalArgumentException("[Kostenvolumen] Wert darf nicht kleiner 0 sein: " + dataString);
            }
            item.setAmount(val);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("[Kostenvolumen] " + Utils.getMessage("msgNotAnInteger") + ": " + dataString);
        }
    }

    private void tryImportFullVigorCnt(KGLListCostCenter item, String dataString) {
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

    private void tryImportServiceSum(KGLListCostCenter item, String dataString) {
        try{
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(true);
            int val = nf.parse(dataString).intValue();
            if (val < 0){
                 throw new IllegalArgumentException("[Summe der Leistungseinheiten] Wert darf nicht kleiner 0 sein: " + dataString);
            }
            item.setServiceSum(val);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("[Summe der Leistungseinheiten] " + Utils.getMessage("msgNotAnInteger") + ": " + dataString);
        }
    }

}
