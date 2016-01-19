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
public class A2 implements BegleitEntity {
    
    private String art;
    private int kennzahl;
    private float anteil;

    public A2() {
    }

    public A2(String art, int kennzahl, float anteil) {
        this.art = art;
        this.kennzahl = kennzahl;
        this.anteil = anteil;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public int getKennzahl() {
        return kennzahl;
    }

    public void setKennzahl(int kennzahl) {
        this.kennzahl = kennzahl;
    }

    public float getAnteil() {
        return anteil;
    }

    public void setAnteil(float anteil) {
        this.anteil = anteil;
    }
    
    
    
    @Override
    public String getFileName() {
        return "A_2_Datenqualitaet.csv";
    }

    @Override
    public String getHeadLine() {
        return "Art;Kennzahl;Anteil";
    }
    
}
