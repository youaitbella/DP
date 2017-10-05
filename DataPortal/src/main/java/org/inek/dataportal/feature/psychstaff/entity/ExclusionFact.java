/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listExclusionFact", schema = "psy")
public class ExclusionFact implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "efId")
    private int _id;
    
    public int getId() {
        return _id;
    }
    
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "efName")
    @Documentation(key = "lblName")
    private String _name = "";

    @Size(max = 50)
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property YearFrom">
    @Column(name = "efYearFrom")
    private int _yearFrom;
    
    public int getYearFrom() {
        return _yearFrom;
    }
    
    public void setYearFrom(int yearFrom) {
        this._yearFrom = yearFrom;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property YearTo">
    @Column(name = "efYearTo")
    private int _yearTo;
    
    public int getYearTo() {
        return _yearTo;
    }
    
    public void setYearTo(int yearTo) {
        this._yearTo = yearTo;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property AffectsApx1">
    @Column(name = "efAffectsApx1")
    private boolean _affectsApx1;
    
    public boolean isAffectsApx1() {
        return _affectsApx1;
    }
    
    public void setAffectsApx1(boolean affectsApx1) {
        _affectsApx1 = affectsApx1;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AffectsApx2">
    @Column(name = "efAffectsApx2")
    private boolean _affectsApx2;
    
    public boolean isAffectsApx2() {
        return _affectsApx2;
    }
    
    public void setAffectsApx2(boolean affectsApx2) {
        _affectsApx2 = affectsApx2;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property NeedReason">
    @Column(name = "efNeedReason")
    private boolean _needReason;
    
    public boolean isNeedReason() {
        return _needReason;
    }
    
    public void setNeedReason(boolean needReason) {
        _needReason = needReason;
    }
    //</editor-fold>
            
    //<editor-fold defaultstate="collapsed" desc="hashCode, equals, toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this._yearTo;
        hash = 97 * hash + Objects.hashCode(this._name);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExclusionFact other = (ExclusionFact) obj;
        if (this._yearTo != other._yearTo) {
            return false;
        }
        if (!Objects.equals(this._name, other._name)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return _name;
    }
    //</editor-fold>
    
}
