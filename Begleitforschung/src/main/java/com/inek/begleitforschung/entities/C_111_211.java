/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.entities;

/**
 *
 * @author vohldo
 */
public class C_111_211 {
    
    private int _type;
    private String _mdc;
    private String _name;
    private int _sumA; // SummeA
    private int _sumAw; // SummeAw
    private int _sumAm; // SummeAm
    private int _sumAu; // SummeAu
    private double _fractionW; // Anteilw
    private double _fractionM; // Anteilm
    private double _fractionU; // Anteilu

    public C_111_211(int _type, String _mdc, String _name, int _sumA, int _sumAw, int _sumAm, int _sumAu, double _fractionW, double _fractionM, double _fractionU) {
        this._type = _type;
        this._mdc = _mdc;
        this._name = _name;
        this._sumA = _sumA;
        this._sumAw = _sumAw;
        this._sumAm = _sumAm;
        this._sumAu = _sumAu;
        this._fractionW = _fractionW;
        this._fractionM = _fractionM;
        this._fractionU = _fractionU;
    }

    public int getType() {
        return _type;
    }

    public void setType(int _type) {
        this._type = _type;
    }

    public String getMdc() {
        return _mdc;
    }

    public void setMdc(String _mdc) {
        this._mdc = _mdc;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
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
}
