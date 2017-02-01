/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListDelimitationFact", schema = "calc")
@XmlRootElement
public class KGPListDelimitationFact implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "dfID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property used">
    @Basic(optional = false)
    @NotNull
    @Column(name = "dfUsed")
    private boolean _used;

    public boolean isUsed() {
        return _used;
    }

    public void setUsed(boolean used) {
        this._used = used;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property personalCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "dfPersonalCost")
    private int _personalCost;

    public int getPersonalCost() {
        return _personalCost;
    }

    public void setPersonalCost(int personalCost) {
        this._personalCost = personalCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property materialCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "dfMaterialcost")
    private int _materialCost;

    public int getMaterialCost() {
        return _materialCost;
    }

    public void setMaterialCost(int materialcost) {
        this._materialCost = materialcost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property infraCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "dfInfraCost")
    private int _infraCost;

    public int getInfraCost() {
        return _infraCost;
    }

    public void setInfraCost(int infraCost) {
        this._infraCost = infraCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property baseInformationId">
    @Column(name = "dfBaseInformationID")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property contentTextId">
    @Column(name = "dfContentTextID")
    private int _contentTextId;

    public int getContentTextId() {
        return _contentTextId;
    }

    public void setContentTextId(int contentTextId) {
        this._contentTextId = contentTextId;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property ContentText">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "dfContentTextId")
    private DrgContentText _contentText;

    public DrgContentText getContentText() {
        return _contentText;
    }

    public void setContentText(DrgContentText contentText) {
        _contentText = contentText;
    }
    // </editor-fold>

    public KGPListDelimitationFact() {
    }

    public KGPListDelimitationFact(Integer dfID) {
        this._id = dfID;
    }

    public KGPListDelimitationFact(Integer dfID, boolean dfUsed, int dfPersonalCost, int dfMaterialcost, int dfInfraCost) {
        this._id = dfID;
        this._used = dfUsed;
        this._personalCost = dfPersonalCost;
        this._materialCost = dfMaterialcost;
        this._infraCost = dfInfraCost;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 29 * hash + (this._used ? 1 : 0);
        hash = 29 * hash + this._personalCost;
        hash = 29 * hash + this._materialCost;
        hash = 29 * hash + this._infraCost;
        hash = 29 * hash + this._baseInformationId;
        hash = 29 * hash + this._contentTextId;
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListDelimitationFact)) {
            return false;
        }
        final KGPListDelimitationFact other = (KGPListDelimitationFact) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
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
        if (this._infraCost != other._infraCost) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (this._contentTextId != other._contentTextId) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListDelimitationFact[ dfID=" + _id + " ]";
    }
    //</editor-fold>
    
}
