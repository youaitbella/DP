package org.inek.dataportal.calc.backingbean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Several {@linkplain DataImportCheck} will supply information about checking progress. This information will be stored
 * here for later retrievment.
 *
 * @author kunkelan
 */
public final class ErrorCounter implements Serializable {

    @SuppressWarnings("ConstantName")
    private static final long serialVersionUID = 1L;
    private String _errorMsg = "";
    private int _infoColumnCount = 0;
    private int _errorRowCount = 0;
    private int _totalCount = 0;
    private int _errorColumnCount = 0;

    ErrorCounter() {
    }

    /**
     * For a new upload clear the old message.
     */
    void reset() {
        _errorMsg = "";
        _infoColumnCount = 0;
        _errorRowCount = 0;
        _totalCount = 0;
        _errorColumnCount = 0;
    }

    boolean containsError() {
        return _errorMsg.contains("Fehler") || _errorMsg.contains("Hinweis");
    }

    void incRowCounter() {
        _totalCount++;
    }

    void addRowErrorMsg(String message) {
        _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + message;
        _errorRowCount++;
    }

    void addColumnErrorMsg(String message) {
        _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + message;
        _errorColumnCount++;
    }

    void addColumnInfoMsg(String message) {
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
        String msg = (_totalCount - _errorRowCount) + " von " + _totalCount + " Zeilen gelesen\r\n";
        if (_errorColumnCount > 0){
            msg += _errorColumnCount + " fehlerhafte Spalte(n) eingelesen (siehe unten). Bitte prüfen Sie die Daten.\r\n";
        }
        if (_infoColumnCount > 0){
            msg += _infoColumnCount + " nicht angegebene Werte (siehe unten). Bitte prüfen Sie die Daten.\r\n";
        }
        return msg + _errorMsg;
    }

}
