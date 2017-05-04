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
public class C_121_221_State_Size {
    
    private int _type;
    private String _state;
    private String _level;
    private int _numHospital;
    private double _avgCases;
    private double _avgLos;
    private double _cmi;
    private double amtStf;
    private int _stateCode;
    private String _bedSort;

    public C_121_221_State_Size() {
    }
    
    public C_121_221_State_Size(int _type, String _state, String _level, int _numHospital, double _avgCases, double _avgLos, double _cmi, double amtStf, String _stateCode, String _bedSort) {
        this._type = _type;
        this._state = _state.substring(3);
        this._level = _level;
        this._numHospital = _numHospital;
        this._avgCases = _avgCases;
        this._avgLos = _avgLos;
        this._cmi = _cmi;
        this.amtStf = amtStf;
        this._stateCode = Integer.parseInt(_stateCode);
        this._bedSort = _bedSort;
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        this._type = type;
    }

    public String getState() {
        return _state;
    }

    public void setState(String state) {
        this._state = state;
    }

    public String getLevel() {
        return _level;
    }

    public void setLevel(String level) {
        this._level = level;
    }

    public int getNumHospital() {
        return _numHospital;
    }

    public void setNumHospital(int numHospital) {
        this._numHospital = numHospital;
    }

    public double getAvgCases() {
        return _avgCases;
    }

    public void setAvgCases(double avgCases) {
        this._avgCases = avgCases;
    }

    public double getAvgLos() {
        return _avgLos;
    }

    public void setAvgLos(double avgLos) {
        this._avgLos = avgLos;
    }

    public double getCmi() {
        return _cmi;
    }

    public void setCmi(double cmi) {
        this._cmi = cmi;
    }

    public double getAmtStf() {
        return amtStf;
    }

    public void setAmtStf(double amtStf) {
        this.amtStf = amtStf;
    }

    public int getStateCode() {
        return _stateCode;
    }

    public void setStateCode(int stateCode) {
        this._stateCode = stateCode;
    }

    public String getBedSort() {
        return _bedSort;
    }

    public void setBedSort(String bedSort) {
        this._bedSort = bedSort;
    }
    
    
}
