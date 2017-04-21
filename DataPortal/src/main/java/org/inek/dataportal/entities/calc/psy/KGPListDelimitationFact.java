/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.psy;

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

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListDelimitationFact", schema = "calc")
public class KGPListDelimitationFact implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dfID", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property used">
    @Column(name = "dfUsed")
    @Documentation(name = "bitte markieren", rank = 10)
    private boolean _used;

    public boolean isUsed() {
        return _used;
    }

    public void setUsed(boolean used) {
        this._used = used;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property personalCost">
    @Column(name = "dfPersonalCost")
    @Documentation(name = "Personalkosten", rank = 20)
    private int _personalCost;

    public int getPersonalCost() {
        return _personalCost;
    }

    public void setPersonalCost(int personalCost) {
        this._personalCost = personalCost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property materialCost">
    @Column(name = "dfMaterialcost")
    @Documentation(name = "Sachkosten", rank = 30)
    private int _materialCost;

    public int getMaterialCost() {
        return _materialCost;
    }

    public void setMaterialCost(int materialcost) {
        this._materialCost = materialcost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property infraCost">
    @Column(name = "dfInfraCost")
    @Documentation(name = "Infrastrukturkosten", rank = 40)
    private int _infraCost;

    public int getInfraCost() {
        return _infraCost;
    }

    public void setInfraCost(int infraCost) {
        this._infraCost = infraCost;
    }
    //</editor-fold>

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
    @Column(name = "dfContentTextID")
    private int _contentTextId;

    public int getContentTextId() {
        return _contentText.getId();
    }

    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ContentText">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "dfContentTextId")
    private KGPListContentText _contentText;

    public KGPListContentText getContentText() {
        return _contentText;
    }

    public void setContentText(KGPListContentText contentText) {
        _contentText = contentText;
        _contentTextId = contentText.getId();
    }
    // </editor-fold>

    @JsonIgnore
    public boolean isRequireInputs() {
        return getContentText().isInputRequired();
    }

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
