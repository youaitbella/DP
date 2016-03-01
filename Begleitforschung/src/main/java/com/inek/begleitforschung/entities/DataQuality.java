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
public class DataQuality {
    
    private String _type;
    private int _num;
    private double _fraction;

    public DataQuality() {
    }

    public DataQuality(String _type, int _num, double _fraction) {
        this._type = _type;
        this._num = _num;
        this._fraction = _fraction;
    }

    public String getType() {
        return _type;
    }

    public void setType(String _type) {
        this._type = _type;
    }

    public int getNum() {
        return _num;
    }

    public void setNum(int _num) {
        this._num = _num;
    }

    public double getFraction() {
        return _fraction;
    }

    public void setFraction(double _fraction) {
        this._fraction = _fraction;
    }
}
