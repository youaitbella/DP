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
public class PartialInpatientCare {
    
    private int _type;
    private String _area;
    private String _areaName;
    private int _numKh;
    private int numPartialInpatient;

    public PartialInpatientCare() {
    }

    public PartialInpatientCare(int _type, String _area, String _areaName, int _numKh, int numPartialInpatient) {
        this._type = _type;
        this._area = _area;
        this._areaName = _areaName;
        this._numKh = _numKh;
        this.numPartialInpatient = numPartialInpatient;
    }

    public int getType() {
        return _type;
    }

    public void setType(int _type) {
        this._type = _type;
    }

    public String getArea() {
        return _area;
    }

    public void setArea(String _area) {
        this._area = _area;
    }

    public String getAreaName() {
        return _areaName;
    }

    public void setAreaName(String _areaName) {
        this._areaName = _areaName;
    }

    public int getNumKh() {
        return _numKh;
    }

    public void setNumKh(int _numKh) {
        this._numKh = _numKh;
    }

    public int getNumPartialInpatient() {
        return numPartialInpatient;
    }

    public void setNumPartialInpatient(int numPartialInpatient) {
        this.numPartialInpatient = numPartialInpatient;
    }
    
    
}
