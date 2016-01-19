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
public class A3 implements BegleitEntity {
    
    private String art;
    private int AnzNDGesU;
    private float AnteilNDU;
    private int AnzProzGes;
    private int AnzProzGesU;
    private float AnteilProzU;

    public A3() {
    }
    
    public A3(String art, int AnzNDGesU, float AnteilNDU, int AnzProzGes, int AnzProzGesU, float AnteilProzU) {
        this.art = art;
        this.AnzNDGesU = AnzNDGesU;
        this.AnteilNDU = AnteilNDU;
        this.AnzProzGes = AnzProzGes;
        this.AnzProzGesU = AnzProzGesU;
        this.AnteilProzU = AnteilProzU;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public int getAnzNDGesU() {
        return AnzNDGesU;
    }

    public void setAnzNDGesU(int AnzNDGesU) {
        this.AnzNDGesU = AnzNDGesU;
    }

    public float getAnteilNDU() {
        return AnteilNDU;
    }

    public void setAnteilNDU(float AnteilNDU) {
        this.AnteilNDU = AnteilNDU;
    }

    public int getAnzProzGes() {
        return AnzProzGes;
    }

    public void setAnzProzGes(int AnzProzGes) {
        this.AnzProzGes = AnzProzGes;
    }

    public int getAnzProzGesU() {
        return AnzProzGesU;
    }

    public void setAnzProzGesU(int AnzProzGesU) {
        this.AnzProzGesU = AnzProzGesU;
    }

    public float getAnteilProzU() {
        return AnteilProzU;
    }

    public void setAnteilProzU(float AnteilProzU) {
        this.AnteilProzU = AnteilProzU;
    }

    @Override
    public String getFileName() {
        return "A_3_Unspezif_Kodierung.csv";
    }

    @Override
    public String getHeadLine() {
        return "Art;AnzNDGes;AnzNDGesU;AnteilNDU;AnzProzGes;AnzProzGesU;AnteilProzU";
    }
}
