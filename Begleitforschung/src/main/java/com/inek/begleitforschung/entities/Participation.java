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
public class Participation {
    
    private String _state;
    private int _numHospitals;
    private int _numCases;
    private String _stateCode;

    public Participation() {
    }

    public Participation(String _state, int _numHospitals, int _numCases, String _stateCode) {
        this._state = _state;
        this._numHospitals = _numHospitals;
        this._numCases = _numCases;
        this._stateCode = _stateCode;
    }

    public String getState() {
        return _state;
    }

    public void setState(String _state) {
        this._state = _state;
    }

    public int getNumHospitals() {
        return _numHospitals;
    }

    public void setNumHospitals(int _numHospitals) {
        this._numHospitals = _numHospitals;
    }

    public int getNumCases() {
        return _numCases;
    }

    public void setNumCases(int _numCases) {
        this._numCases = _numCases;
    }

    public String getStateCode() {
        return _stateCode;
    }

    public void setStateCode(String _stateCode) {
        this._stateCode = _stateCode;
    }
    
}
