/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.function.BiConsumer;
import org.inek.dataportal.entities.calc.KGPListMedInfra;
import org.inek.dataportal.entities.calc.PeppCalcBasics;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.StringUtil;

/**
 *
 * @author muellermi
 */
public class MedInfraDataImporterPepp {

    private String _errorMsg = "";
    private int _totalCount = 0;
    private int _costTypeId = 0;

    public int getTotalCount() {
        return _totalCount;
    }

    private int _errorRowCount = 0;
    private int _errorColumnCount = 0;

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

    /**
     * For upload set the value 170 med infra and 180 non med infra. All other values will be reported as error.
     * @param costTypeId 
     */    
    void setCostTypeId(int costTypeId) {
        _costTypeId = costTypeId;
    }

    public boolean containsError() {
        return !_errorMsg.isEmpty();
    }

    public String getMessage() {
        return (_totalCount - _errorRowCount) + " von " + _totalCount + " Zeilen gelesen\r\n\r\n" + _errorColumnCount + " fehlerhafte Spalte(n) eingelesen " + _errorMsg;
    }
    
    public void tryImportLine(String line) {
        if (_costTypeId != 170 && _costTypeId != 180) {
            throw new IllegalStateException("internal error: cost type not set");
        }
        _totalCount++;
        try {
            if (line.endsWith(";")) {
                line = line + " ";
            }
            String[] data = StringUtil.splitAtUnquotedSemicolon(line);
            if (data.length != 4) {
                throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
            }
            KGPListMedInfra item = new KGPListMedInfra();
            item.setBaseInformationId(_calcBasics.getId());
            item.setCostTypeId(_costTypeId);

            tryImportString(item, data[0], (i,s) -> i.setCostCenterNumber(s), "Nummer der Kostenstelle ungültig: ");
            tryImportString(item, data[1], (i,s) -> i.setCostCenterText(s), "Name der Kostenstelle ungültig: ");
            tryImportString(item, data[2], (i,s) -> i.setKeyUsed(s), "Verwendeter Schlüssel ungültig: ");
            tryImportInteger(item, data[3], (i,s) -> i.setAmount(s), "Kostenvolumen ungültig: ");
                        
            if(itemExists(item)) {
                return;
            }

            _calcBasics.getKgpMedInfraList().add(item);
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
    
    private boolean itemExists(KGPListMedInfra item) {
        for (KGPListMedInfra infra : _calcBasics.getKgpMedInfraList()) {
            if (infra.getCostCenterNumber().equals(item.getCostCenterNumber()) &&
                    infra.getCostCenterText().equals(item.getCostCenterText()) &&
                    infra.getKeyUsed().equals(item.getKeyUsed())
                    ) {
                if (infra.getAmount() != item.getAmount()) {
                    _errorMsg += "\r\nZeile "+_totalCount+" bereits vorhanden. Spalte aktualisiert : alt " + infra.getAmount() + " neu " + item.getAmount();
                    infra.setAmount(item.getAmount());
                } else {
                    _errorMsg += "\r\nZeile "+_totalCount+" bereits vorhanden.";
                }
                return true;
            }
        }
        return false;
    }

    private void tryImportString(KGPListMedInfra item, String data, BiConsumer<KGPListMedInfra, String> bind, String errorMsg) {
        try {
            bind.accept(item, data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage() + "\n" + errorMsg + data);
        }
    }
    
    private void tryImportInteger(KGPListMedInfra item, String data, BiConsumer<KGPListMedInfra, Integer> bind, String errorMsg) {
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
            addColumnErrorMsg(errorMsg + "Wert ist keine gültige Zahl: " + Utils.getMessage("msgNotANumber") + ": " + data);
        }
    }
    
    private void tryImportDouble(KGPListMedInfra item, String data, BiConsumer<KGPListMedInfra, Double> bind, String errorMsg) {
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(false);
            double val = nf.parse(data).doubleValue();
            if (val < 0){
                bind.accept(item, 0.0);
                throw new IllegalArgumentException(errorMsg + "Wert darf nicht kleiner 0 sein: " + Utils.getMessage("msgNotANumber") + ": " + data);
            }
            bind.accept(item, val);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(errorMsg + Utils.getMessage("msgNotANumber") + ": " + data);
        }
    }
    
}
