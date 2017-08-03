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

    @SuppressWarnings("ParameterNumber")
    public C_112_212(int type, String level, int sumA, int sumAw, int sumAm, int sumAu,
            double fractionW, double fractionM, double fractionU, String sLevel) {
        this._type = type;
        this._level = level;
        this._sumA = sumA;
        this._sumAw = sumAw;
        this._sumAm = sumAm;
        this._sumAu = sumAu;
        this._fractionW = fractionW;
        this._fractionM = fractionM;
        this._fractionU = fractionU;
        this._sLevel = sLevel;
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        this._type = type;
    }

    public String getLevel() {
        return _level;
    }

    public void setLevel(String level) {
        this._level = level;
    }

    public int getSumA() {
        return _sumA;
    }

    public void setSumA(int sumA) {
        this._sumA = sumA;
    }

    public int getSumAw() {
        return _sumAw;
    }

    public void setSumAw(int sumAw) {
        this._sumAw = sumAw;
    }

    public int getSumAm() {
        return _sumAm;
    }

    public void setSumAm(int sumAm) {
        this._sumAm = sumAm;
    }

    public int getSumAu() {
        return _sumAu;
    }

    public void setSumAu(int sumAu) {
        this._sumAu = sumAu;
    }

    public double getFractionW() {
        return _fractionW;
    }

    public void setFractionW(double fractionW) {
        this._fractionW = fractionW;
    }

    public double getFractionM() {
        return _fractionM;
    }

    public void setFractionM(double fractionM) {
        this._fractionM = fractionM;
    }

    public double getFractionU() {
        return _fractionU;
    }

    public void setFractionU(double fractionU) {
        this._fractionU = fractionU;
    }

    public String getsLevel() {
        return _sLevel;
    }

    public void setsLevel(String sLevel) {
        this._sLevel = sLevel;
    }


}
