/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "KGLListDelimitationFact", schema = "calc")
public class DrgDelimitationFact implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dfId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property baseInformationId">
    @Column(name = "dfBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property contentTextId">
    @Column(name = "dfContentTextId")
    private int _contentTextId;

    public int getContentTextId() {
        return _contentTextId;
    }

    public void setContentTextId(int contentTextId) {
        this._contentTextId = contentTextId;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property used">
    @Column(name = "dfUsed")
    private boolean _used;

    public boolean isUsed() {
        return _used;
    }

    public void setUsed(boolean used) {
        this._used = used;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property personalCost">
    @Column(name = "dfPersonalCost")
    private double _personalCost;

    public double getPersonalCost() {
        return _personalCost;
    }

    public void setPersonalCost(double personalCost) {
        this._personalCost = personalCost;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property materialCost">
    @Column(name = "dfMaterialCost")
    private double _materialCost;

    public double getMaterialCost() {
        return _materialCost;
    }

    public void setMaterialCost(double materialCost) {
        this._materialCost = materialCost;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property infraCost">
    @Column(name = "dfInfraCost")
    private double _infraCost;

    public double getInfraCost() {
        return _infraCost;
    }

    public void setInfraCost(double infraCost) {
        this._infraCost = infraCost;
    }
    // </editor-fold>

    @Transient
    private String _label;

    public String getLabel() {
        return _label;
    }

    public void setLabel(String label) {
        this._label = label;
    }
}
