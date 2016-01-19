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
public class B1Bundesland implements BegleitEntity {
    
    private String gesamt;
    private int summe;
    private String blCode;

    public B1Bundesland() {
    }

    public B1Bundesland(String gesamt, int summe, String blCode) {
        this.gesamt = gesamt;
        this.summe = summe;
        this.blCode = blCode;
    }

    public String getGesamt() {
        return gesamt;
    }

    public void setGesamt(String gesamt) {
        this.gesamt = gesamt;
    }

    public int getSumme() {
        return summe;
    }

    public void setSumme(int summe) {
        this.summe = summe;
    }

    public String getBlCode() {
        return blCode;
    }

    public void setBlCode(String blCode) {
        this.blCode = blCode;
    }

    @Override
    public String getFileName() {
        return "B_1_KH_Bundesland.csv";
    }

    @Override
    public String getHeadLine() {
        return "Gesamt;Summe KH;BLCode";
    }
}
