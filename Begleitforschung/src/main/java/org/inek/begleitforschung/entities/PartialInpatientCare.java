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
public class PartialInpatientCare {

    private int _type;
    private String _area;
    private String _areaName;
    private int _numKh;
    private int numPartialInpatient;

    public PartialInpatientCare() {
    }

    public PartialInpatientCare(int type, String area, String areaName, int numKh, int numPartialInpatient) {
        this._type = type;
        this._area = area;
        this._areaName = areaName;
        this._numKh = numKh;
        this.numPartialInpatient = numPartialInpatient;
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        this._type = type;
    }

    public String getArea() {
        return _area;
    }

    public void setArea(String area) {
        this._area = area;
    }

    public String getAreaName() {
        return _areaName;
    }

    public void setAreaName(String areaName) {
        this._areaName = _areaName;
    }

    public int getNumKh() {
        return _numKh;
    }

    public void setNumKh(int numKh) {
        this._numKh = _numKh;
    }

    public int getNumPartialInpatient() {
        return numPartialInpatient;
    }

    public void setNumPartialInpatient(int numPartialInpatient) {
        this.numPartialInpatient = numPartialInpatient;
    }

    
}
