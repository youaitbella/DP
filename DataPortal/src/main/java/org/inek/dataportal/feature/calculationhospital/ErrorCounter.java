/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Several {@linkplain DataImportCheck} will supply information about checking progress. This information will be stored
 * here for later retrievment.
 *
 * @author kunkelan
 */
public class ErrorCounter implements Serializable {

    private static Map<String, ErrorCounter> counters = new HashMap<>();

    private String _errorMsg = "";
    private int _infoColumnCount = 0;
    private int _errorRowCount = 0;
    private int _totalCount = 0;
    private int _errorColumnCount = 0;

    private ErrorCounter() {
        // use only ErrorCounters via obtainErrorCounter
    }

    /**
     * Return the named ErrorCounter, generating one if not exists.
     *
     * @param importer name who demands an ErrorCounter.
     * @return the named ErrorCounter
     */
    public static ErrorCounter obtainErrorCounter(String importer) {
        if (!counters.containsKey(importer)) {
            counters.put(importer, new ErrorCounter());
        }
        return counters.get(importer);
    }

    /**
     * For a new upload clear the old message.
     */
    public void reset() {
        _errorMsg = "";
        _infoColumnCount = 0;
        _errorRowCount = 0;
        _totalCount = 0;
        _errorColumnCount = 0;
    }

    public boolean containsError() {
        return !_errorMsg.contains("Fehler");
    }

    public void incRowCounter() {
        _totalCount++;
    }

    public void addRowErrorMsg(String message) {
        _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + message;
        _errorRowCount++;
    }

    public void addColumnErrorMsg(String message) {
        _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + message;
        _errorColumnCount++;
    }

    public void addColumnInfoMsg(String message) {
        _errorMsg += "\r\nHinweis in Zeile " + _totalCount + ": " + message;
        _infoColumnCount++;
    }

    public void addChangeColumn(int oldVal, int newVal) {
        _errorMsg += "\r\nZeile " + _totalCount
                + " bereits vorhanden. Spalte aktualisiert : alt " + oldVal + " neu " + newVal;
    }

    public int getTotalCount() {
        return _totalCount;
    }

    public int getInfoColumnCount() {
        return _errorColumnCount;
    }

    public int getErrorRowCount() {
        return _errorRowCount;
    }

    public int getErrorColumnCount() {
        return _errorColumnCount;
    }

    public String getMessage() {
        return (_totalCount - _errorRowCount) + " von " + _totalCount + " Zeilen gelesen\r\n\r\n"
                + _errorColumnCount + " fehlerhafte Spalte(n) eingelesen\n"
                + _infoColumnCount + " nicht angegebene Werte\n\n"
                + _errorMsg;
    }

}
