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

    public SystemRated(int type, String drg, String name, double relativeWeight, int numPrimaryDepartment, double fractionOverall) {
        this._type = type;
        this._drg = drg;
        this._name = name;
        this._relativeWeight = relativeWeight;
        this._numPrimaryDepartment = numPrimaryDepartment;
        this._fractionOverall = fractionOverall;
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        this._type = type;
    }

    public String getDrg() {
        return _drg;
    }

    public void setDrg(String drg) {
        this._drg = drg;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public double getRelativeWeight() {
        return _relativeWeight;
    }

    public void setRelativeWeight(double relativeWeight) {
        this._relativeWeight = relativeWeight;
    }

    public int getNumPrimaryDepartment() {
        return _numPrimaryDepartment;
    }

    public void setNumPrimaryDepartment(int numPrimaryDepartment) {
        this._numPrimaryDepartment = numPrimaryDepartment;
    }

    public double getFractionOverall() {
        return _fractionOverall;
    }

    public void setFractionOverall(double fractionOverall) {
        this._fractionOverall = fractionOverall;
    }

    
}
