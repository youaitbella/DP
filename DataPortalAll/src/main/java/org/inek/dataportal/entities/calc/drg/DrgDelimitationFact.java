/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.drg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.utils.Documentation;
import org.inek.dataportal.utils.IgnoreOnCompare;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "KGLListDelimitationFact", schema = "calc")
public class DrgDelimitationFact implements Serializable, BaseIdValue {

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dfId", updatable = false, nullable = false)
    private int _id;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property baseInformationId">
    @Column(name = "dfBaseInformationId")
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property contentTextId">
    @Column(name = "dfContentTextId")
    private int _contentTextId;    

    // conveniance method
    public int getContentTextId() {
        return _contentText.getId();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ContentText">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "dfContentTextId")
    @Documentation (name = "Beschreibung", rank = 1)
    @IgnoreOnCompare
    private DrgContentText _contentText;

    public DrgContentText getContentText() {
        return _contentText;
    }

    public void setContentText(DrgContentText contentText) {
        _contentText = contentText;
        _contentTextId = contentText.getId();
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
    @Documentation (name = "Personalkosten", rank = 10)
    private int _personalCost;

    public int getPersonalCost() {
        return _personalCost;
    }

    public void setPersonalCost(int personalCost) {
        this._personalCost = personalCost;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property materialCost">
    @Column(name = "dfMaterialCost")
    @Documentation (name = "Materialkosten", rank = 20)
    private int _materialCost;

    public int getMaterialCost() {
        return _materialCost;
    }

    public void setMaterialCost(int materialCost) {
        this._materialCost = materialCost;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property infraCost">
    @Column(name = "dfInfraCost")
    @Documentation (name = "Infrastrukturkosten", rank = 30)
    private int _infraCost;

    public int getInfraCost() {
        return _infraCost;
    }

    public void setInfraCost(int infraCost) {
        this._infraCost = infraCost;
    }
    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property requireInputs">
    @JsonIgnore
    public boolean isRequireInputs() {
        return getContentText().isInputRequired();
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this._id;
        if (this._id != -1) return hash;
        
        hash = 79 * hash + this._baseInformationId;
        hash = 79 * hash + this.getContentText().getId();
        hash = 79 * hash + (this._used ? 1 : 0);
        hash = 79 * hash + this._personalCost;
        hash = 79 * hash + this._materialCost;
        hash = 79 * hash + this._infraCost;
        return hash;
    }
    
    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DrgDelimitationFact)) {
            return false;
        }
        final DrgDelimitationFact other = (DrgDelimitationFact) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (this.getContentText().getId() != other.getContentText().getId()) {
            return false;
        }
        if (this._used != other._used) {
            return false;
        }
        if (this._personalCost != other._personalCost) {
            return false;
        }
        if (this._materialCost != other._materialCost) {
            return false;
        }
        return this._infraCost == other._infraCost;
    }
    
    @Override
    public String toString() {
        return "DrgDelimitationFact{" + "_id=" + _id + '}';
    }
    //</editor-fold>
    
}
