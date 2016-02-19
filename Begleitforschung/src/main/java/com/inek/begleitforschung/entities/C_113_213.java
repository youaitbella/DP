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
public class C_113_213 {
    
    private int _type;
    private String _drg;
    private String _name;
    private int sumA;
    private double avgLos;
    private double avgStdDeviation;
    private int sumKla;
    private double fractionKla;
    private int sumLla;
    private double fractionLla;

    public C_113_213() {
    }

    public C_113_213(int _type, String _drg, String _name, int sumA, double avgLos, double avgStdDeviation, int sumKla, double fractionKla, int sumLla, double fractionLla) {
        this._type = _type;
        this._drg = _drg;
        this._name = _name;
        this.sumA = sumA;
        this.avgLos = avgLos;
        this.avgStdDeviation = avgStdDeviation;
        this.sumKla = sumKla;
        this.fractionKla = fractionKla;
        this.sumLla = sumLla;
        this.fractionLla = fractionLla;
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

    public int getSumA() {
        return sumA;
    }

    public void setSumA(int sumA) {
        this.sumA = sumA;
    }

    public double getAvgLos() {
        return avgLos;
    }

    public void setAvgLos(double avgLos) {
        this.avgLos = avgLos;
    }

    public double getAvgStdDeviation() {
        return avgStdDeviation;
    }

    public void setAvgStdDeviation(double avgStdDeviation) {
        this.avgStdDeviation = avgStdDeviation;
    }

    public int getSumKla() {
        return sumKla;
    }

    public void setSumKla(int sumKla) {
        this.sumKla = sumKla;
    }

    public double getFractionKla() {
        return fractionKla;
    }

    public void setFractionKla(double fractionKla) {
        this.fractionKla = fractionKla;
    }

    public int getSumLla() {
        return sumLla;
    }

    public void setSumLla(int sumLla) {
        this.sumLla = sumLla;
    }

    public double getFractionLla() {
        return fractionLla;
    }

    public void setFractionLla(double fractionLla) {
        this.fractionLla = fractionLla;
    }
    
}
