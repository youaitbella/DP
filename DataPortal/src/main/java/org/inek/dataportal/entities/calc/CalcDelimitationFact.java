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

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "KGLListDelimitationFact", schema = "calc")
public class CalcDelimitationFact implements Serializable {
    
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
    
    @Column(name = "dfBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int _baseInformationId) {
        this._baseInformationId = _baseInformationId;
    }
    
    @Column(name = "dfContentTextId")
    private int _contentTextId;

    public int getContentTextId() {
        return _contentTextId;
    }

    public void setContentTextId(int _contentTextId) {
        this._contentTextId = _contentTextId;
    }
    
    @Column(name = "dfUsed")
    private boolean _used;

    public boolean isUsed() {
        return _used;
    }

    public void setUsed(boolean _used) {
        this._used = _used;
    }
    
    @Column(name = "dfPersonalCost")
    private double _personalCost;

    public double getPersonalCost() {
        return _personalCost;
    }

    public void setPersonalCost(double _personalCost) {
        this._personalCost = _personalCost;
    }
    
    @Column(name = "dfMaterialCost")
    private double _materialCost;

    public double getMaterialCost() {
        return _materialCost;
    }

    public void setMaterialCost(double _materialCost) {
        this._materialCost = _materialCost;
    }
    
    @Column(name = "dfInfraCost")
    private double _infraCost;

    public double getInfraCost() {
        return _infraCost;
    }

    public void setInfraCost(double _infraCost) {
        this._infraCost = _infraCost;
    }
    
}
