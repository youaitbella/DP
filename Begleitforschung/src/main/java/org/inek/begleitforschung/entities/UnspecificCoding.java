/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.begleitforschung.entities;

/**
 *
 * @author vohldo
 */
public class UnspecificCoding {
    
    private String _type;
    private int _numSd;
    private int _numSdU;
    private double _fractionSdU;
    private int _numProc;
    private int _numProcU;
    private double _fractionProcU;

    public UnspecificCoding() {
    }

    public UnspecificCoding(String _type, int _numSd, int _numSdU, double _fractionSdU, int _numProc, int _numProcU, double _fractionProcU) {
        this._type = _type;
        this._numSd = _numSd;
        this._numSdU = _numSdU;
        this._fractionSdU = _fractionSdU;
        this._numProc = _numProc;
        this._numProcU = _numProcU;
        this._fractionProcU = _fractionProcU;
    }

    public String getType() {
        return _type;
    }

    public void setType(String _type) {
        this._type = _type;
    }

    public int getNumSd() {
        return _numSd;
    }

    public void setNumSd(int _numSd) {
        this._numSd = _numSd;
    }

    public int getNumSdU() {
        return _numSdU;
    }

    public void setNumSdU(int _numSdU) {
        this._numSdU = _numSdU;
    }

    public double getFractionSdU() {
        return _fractionSdU;
    }

    public void setFractionSdU(double _fractionSdU) {
        this._fractionSdU = _fractionSdU;
    }

    public int getNumProc() {
        return _numProc;
    }

    public void setNumProc(int _numProc) {
        this._numProc = _numProc;
    }

    public int getNumProcU() {
        return _numProcU;
    }

    public void setNumProcU(int _numProcU) {
        this._numProcU = _numProcU;
    }

    public double getFractionProcU() {
        return _fractionProcU;
    }

    public void setFractionProcU(double _fractionProcU) {
        this._fractionProcU = _fractionProcU;
    }
}
