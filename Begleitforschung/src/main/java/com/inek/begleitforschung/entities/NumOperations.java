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
    
    public NumOperations(int type, String code, String codeName, int cases, int hospitals, double casesHospital, double percentage10, double percentage20, double percentage30, double percentage40, double percentage50) {
        this._type = type;
        this._code = code;
        this._codeName = codeName;
        this._cases = cases;
        this._hospitals = hospitals;
        this._casesHospital = casesHospital;
        this._percentage10 = percentage10;
        this._percentage20 = percentage20;
        this._percentage30 = percentage30;
        this._percentage40 = percentage40;
        this._percentage50 = percentage50;
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        this._type = type;
    }

    public String getCode() {
        return _code;
    }

    public void setCode(String code) {
        this._code = code;
    }

    public String getCodeName() {
        return _codeName;
    }

    public void setCodeName(String codeName) {
        this._codeName = codeName;
    }

    public int getCases() {
        return _cases;
    }

    public void setCases(int cases) {
        this._cases = cases;
    }

    public int getHospitals() {
        return _hospitals;
    }

    public void setHospitals(int hospitals) {
        this._hospitals = hospitals;
    }

    public double getPercentage10() {
        return _percentage10;
    }

    public void setPercentage10(double percentage10) {
        this._percentage10 = percentage10;
    }

    public double getPercentage20() {
        return _percentage20;
    }

    public void setPercentage20(double percentage20) {
        this._percentage20 = percentage20;
    }

    public double getPercentage30() {
        return _percentage30;
    }

    public void setPercentage30(double percentage30) {
        this._percentage30 = percentage30;
    }

    public double getPercentage40() {
        return _percentage40;
    }

    public void setPercentage40(double percentage40) {
        this._percentage40 = percentage40;
    }

    public double getPercentage50() {
        return _percentage50;
    }

    public void setPercentage50(double percentage50) {
        this._percentage50 = percentage50;
    }

    public double getCasesHospital() {
        return _casesHospital;
    }
 
    public void setCasesHospital(double casesHospital) {
        this._casesHospital = casesHospital;
    }
}
