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
public class Participation {

    private String _state;
    private int _numHospitals;
    private int _numCases;
    private String _stateCode;

    public Participation() {
    }

    public Participation(String state, int numHospitals, int numCases, String stateCode) {
        this._state = state;
        this._numHospitals = numHospitals;
        this._numCases = numCases;
        this._stateCode = stateCode;
    }

    public String getState() {
        return _state;
    }

    public void setState(String state) {
        this._state = state;
    }

    public int getNumHospitals() {
        return _numHospitals;
    }

    public void setNumHospitals(int numHospitals) {
        this._numHospitals = numHospitals;
    }

    public int getNumCases() {
        return _numCases;
    }

    public void setNumCases(int numCases) {
        this._numCases = numCases;
    }

    public String getStateCode() {
        return _stateCode;
    }

    public void setStateCode(String stateCode) {
        this._stateCode = stateCode;
    }

}
