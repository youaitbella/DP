package com.inek.begleitforschung.entities;

/**
 * File: BegleitfBr_Drg_2014_A_1_KH.csv
 * @author vohldo
 */
public class A1 implements BegleitEntity {
    
    private String bundesland;
    private Integer anzahlKh;
    private Integer anzahlFälle;
    private String bundeslandCode;

    public A1() {
    }

    public A1(String bundesland, Integer anzahlKh, Integer anzahlFälle, String bundeslandCode) {
        this.bundesland = bundesland;
        this.anzahlKh = anzahlKh;
        this.anzahlFälle = anzahlFälle;
        this.bundeslandCode = bundeslandCode;
    }
    
    public String getBundesland() {
        return bundesland;
    }

    public void setBundesland(String bundesland) {
        this.bundesland = bundesland;
    }

    public Integer getAnzahlKh() {
        return anzahlKh;
    }

    public void setAnzahlKh(Integer anzahlKh) {
        this.anzahlKh = anzahlKh;
    }

    public Integer getAnzahlFälle() {
        return anzahlFälle;
    }

    public void setAnzahlFälle(Integer anzahlFälle) {
        this.anzahlFälle = anzahlFälle;
    }

    public String getBundeslandCode() {
        return bundeslandCode;
    }

    public void setBundeslandCode(String BundeslandCode) {
        this.bundeslandCode = BundeslandCode;
    }
    
    @Override
    public String getFileName() {
        return "A_1_KH.csv";
    }

    @Override
    public String getHeadLine() {
        return "Bundesland_A;AnzahlKH;AnzahlFall;BLCode";
    }
    
    
}
