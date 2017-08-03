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
    
    @SuppressWarnings("ParameterNumber")
    public C_113_213(int type, String drg, String name, int sumA, double avgLos, double avgStdDeviation,
            int sumKla, double fractionKla, int sumLla, double fractionLla) {
        this._type = type;
        this._drg = drg;
        this._name = name;
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
