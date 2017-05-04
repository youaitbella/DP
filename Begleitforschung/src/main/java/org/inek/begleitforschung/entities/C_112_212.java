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
public class C_112_212 {
    
    private int _type;
    private String _level; //Stufe
    private int _sumA; // SummeA
    private int _sumAw; // SummeAw
    private int _sumAm; // SummeAm
    private int _sumAu; // SummeAu
    private double _fractionW; // Anteilw
    private double _fractionM; // Anteilm
    private double _fractionU; // Anteilu
    private String _sLevel; // SSTUFE

    public C_112_212() {
    }

    public C_112_212(int _type, String _level, int _sumA, int _sumAw, int _sumAm, int _sumAu, double _fractionW, double _fractionM, double _fractionU, String _sLevel) {
        this._type = _type;
        this._level = _level;
        this._sumA = _sumA;
        this._sumAw = _sumAw;
        this._sumAm = _sumAm;
        this._sumAu = _sumAu;
        this._fractionW = _fractionW;
        this._fractionM = _fractionM;
        this._fractionU = _fractionU;
        this._sLevel = _sLevel;
    }

    public int getType() {
        return _type;
    }

    public void setType(int _type) {
        this._type = _type;
    }

    public String getLevel() {
        return _level;
    }

    public void setLevel(String _level) {
        this._level = _level;
    }

    public int getSumA() {
        return _sumA;
    }

    public void setSumA(int _sumA) {
        this._sumA = _sumA;
    }

    public int getSumAw() {
        return _sumAw;
    }

    public void setSumAw(int _sumAw) {
        this._sumAw = _sumAw;
    }

    public int getSumAm() {
        return _sumAm;
    }

    public void setSumAm(int _sumAm) {
        this._sumAm = _sumAm;
    }

    public int getSumAu() {
        return _sumAu;
    }

    public void setSumAu(int _sumAu) {
        this._sumAu = _sumAu;
    }

    public double getFractionW() {
        return _fractionW;
    }

    public void setFractionW(double _fractionW) {
        this._fractionW = _fractionW;
    }

    public double getFractionM() {
        return _fractionM;
    }

    public void setFractionM(double _fractionM) {
        this._fractionM = _fractionM;
    }

    public double getFractionU() {
        return _fractionU;
    }

    public void setFractionU(double _fractionU) {
        this._fractionU = _fractionU;
    }

    public String getsLevel() {
        return _sLevel;
    }

    public void setsLevel(String _sLevel) {
        this._sLevel = _sLevel;
    }
    
    
}
