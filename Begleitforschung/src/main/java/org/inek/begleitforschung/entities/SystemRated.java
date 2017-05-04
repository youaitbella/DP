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
public class SystemRated {
    
    private int _type;
    private String _drg;
    private String _name;
    private double _relativeWeight;
    private int _numPrimaryDepartment;
    private double _fractionOverall;

    public SystemRated() {
    }

    public SystemRated(int _type, String _drg, String _name, double _relativeWeight, int _numPrimaryDepartment, double _fractionOverall) {
        this._type = _type;
        this._drg = _drg;
        this._name = _name;
        this._relativeWeight = _relativeWeight;
        this._numPrimaryDepartment = _numPrimaryDepartment;
        this._fractionOverall = _fractionOverall;
    }

    public int getType() {
        return _type;
    }

    public void setType(int _type) {
        this._type = _type;
    }

    public String getDrg() {
        return _drg;
    }

    public void setDrg(String _drg) {
        this._drg = _drg;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public double getRelativeWeight() {
        return _relativeWeight;
    }

    public void setRelativeWeight(double _relativeWeight) {
        this._relativeWeight = _relativeWeight;
    }

    public int getNumPrimaryDepartment() {
        return _numPrimaryDepartment;
    }

    public void setNumPrimaryDepartment(int _numPrimaryDepartment) {
        this._numPrimaryDepartment = _numPrimaryDepartment;
    }

    public double getFractionOverall() {
        return _fractionOverall;
    }

    public void setFractionOverall(double _fractionOverall) {
        this._fractionOverall = _fractionOverall;
    }
    
    
}
