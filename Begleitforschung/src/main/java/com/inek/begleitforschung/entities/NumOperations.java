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
public class NumOperations {
    
    private int _type;
    private String _code;
    private String _codeName;
    private int _cases;
    private int _hospitals;
    private double _casesHospital;
    private double _percentage10;
    private double _percentage20;
    private double _percentage30;
    private double _percentage40;
    private double _percentage50;

    public NumOperations() {
    }
    
    public NumOperations(int _type, String _code, String _codeName, int _cases, int _hospitals, double _casesHospital, double _percentage10, double _percentage20, double _percentage30, double _percentage40, double _percentage50) {
        this._type = _type;
        this._code = _code;
        this._codeName = _codeName;
        this._cases = _cases;
        this._hospitals = _hospitals;
        this._casesHospital = _casesHospital;
        this._percentage10 = _percentage10;
        this._percentage20 = _percentage20;
        this._percentage30 = _percentage30;
        this._percentage40 = _percentage40;
        this._percentage50 = _percentage50;
    }

    public int getType() {
        return _type;
    }

    public void setType(int _type) {
        this._type = _type;
    }

    public String getCode() {
        return _code;
    }

    public void setCode(String _code) {
        this._code = _code;
    }

    public String getCodeName() {
        return _codeName;
    }

    public void setCodeName(String _codeName) {
        this._codeName = _codeName;
    }

    public int getCases() {
        return _cases;
    }

    public void setCases(int _cases) {
        this._cases = _cases;
    }

    public int getHospitals() {
        return _hospitals;
    }

    public void setHospitals(int _hospitals) {
        this._hospitals = _hospitals;
    }

    public double getPercentage10() {
        return _percentage10;
    }

    public void setPercentage10(double _percentage10) {
        this._percentage10 = _percentage10;
    }

    public double getPercentage20() {
        return _percentage20;
    }

    public void setPercentage20(double _percentage20) {
        this._percentage20 = _percentage20;
    }

    public double getPercentage30() {
        return _percentage30;
    }

    public void setPercentage30(double _percentage30) {
        this._percentage30 = _percentage30;
    }

    public double getPercentage40() {
        return _percentage40;
    }

    public void setPercentage40(double _percentage40) {
        this._percentage40 = _percentage40;
    }

    public double getPercentage50() {
        return _percentage50;
    }

    public void setPercentage50(double _percentage50) {
        this._percentage50 = _percentage50;
    }

    public double getCasesHospital() {
        return _casesHospital;
    }
 
    public void setCasesHospital(double _casesHospital) {
        this._casesHospital = _casesHospital;
    }
}
