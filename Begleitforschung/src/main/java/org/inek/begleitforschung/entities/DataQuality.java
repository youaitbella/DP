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
public class DataQuality {

    private String _type;
    private int _num;
    private double _fraction;

    public DataQuality() {
    }

    public DataQuality(String type, int num, double fraction) {
        this._type = type;
        this._num = num;
        this._fraction = fraction;
    }

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        this._type = type;
    }

    public int getNum() {
        return _num;
    }

    public void setNum(int num) {
        this._num = num;
    }

    public double getFraction() {
        return _fraction;
    }

    public void setFraction(double fraction) {
        this._fraction = fraction;
    }
}
