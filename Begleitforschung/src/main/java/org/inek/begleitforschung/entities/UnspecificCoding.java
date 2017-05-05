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

    public UnspecificCoding(String type, int numSd, int numSdU, double fractionSdU, int numProc, int numProcU, double fractionProcU) {
        this._type = type;
        this._numSd = numSd;
        this._numSdU = numSdU;
        this._fractionSdU = fractionSdU;
        this._numProc = numProc;
        this._numProcU = numProcU;
        this._fractionProcU = fractionProcU;
    }

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        this._type = type;
    }

    public int getNumSd() {
        return _numSd;
    }

    public void setNumSd(int numSd) {
        this._numSd = numSd;
    }

    public int getNumSdU() {
        return _numSdU;
    }

    public void setNumSdU(int numSdU) {
        this._numSdU = numSdU;
    }

    public double getFractionSdU() {
        return _fractionSdU;
    }

    public void setFractionSdU(double fractionSdU) {
        this._fractionSdU = fractionSdU;
    }

    public int getNumProc() {
        return _numProc;
    }

    public void setNumProc(int numProc) {
        this._numProc = numProc;
    }

    public int getNumProcU() {
        return _numProcU;
    }

    public void setNumProcU(int numProcU) {
        this._numProcU = numProcU;
    }

    public double getFractionProcU() {
        return _fractionProcU;
    }

    public void setFractionProcU(double fractionProcU) {
        this._fractionProcU = fractionProcU;
    }
}
