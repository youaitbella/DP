/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListCentralFocus", schema = "calc")
@XmlRootElement
public class KGLListCentralFocus implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cfID")
    private int _id = -1;
    
    public int getId() {
        return _id;
    }
    
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
        
    //<editor-fold defaultstate="collapsed" desc="text">
    @Basic(optional = false)
    @NotNull
    @Size(max = 300)
    @Column(name = "cfText")
    private String _text = "";

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="caseCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cfCaseCnt")
    private int _caseCnt;

    public int getCaseCnt() {
        return _caseCnt;
    }

    public void setCaseCnt(int caseCnt) {
        this._caseCnt = caseCnt;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="personalCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cfPersonalCost")
    private double _personalCost;
    
    public double getPersonalCost() {
        return _personalCost;
    }

    public void setPersonalCost(double personalCost) {
        this._personalCost = personalCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="materialcost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cfMaterialcost")
    private double _materialcost;
    
    public double getMaterialcost() {
        return _materialcost;
    }

    public void setMaterialcost(double materialcost) {
        this._materialcost = materialcost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="_infraCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cfInfraCost")
    private double _infraCost;
    
    public double getInfraCost() {
        return _infraCost;
    }

    public void setInfraCost(double infraCost) {
        this._infraCost = infraCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="remunerationAmount">
    @Basic(optional = false)
    @NotNull
    @Column(name = "cfRemunerationAmount")
    private double _remunerationAmount;

    public double getRemunerationAmount() {
        return _remunerationAmount;
    }

    public void setRemunerationAmount(double remunerationAmount) {
        this._remunerationAmount = remunerationAmount;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="_remunerationKey">
    @Basic(optional = false)
    @NotNull
    @Size(max = 8)
    @Column(name = "cfRemunerationKey")
    private String _remunerationKey = "";
    
    public String getRemunerationKey() {
        return _remunerationKey;
    }

    public void setRemunerationKey(String remunerationKey) {
        this._remunerationKey = remunerationKey;
    }
    //</editor-fold>

    public KGLListCentralFocus() {
    }

    public KGLListCentralFocus(Integer cfID) {
        this._id = cfID;
    }

    public KGLListCentralFocus(int cfID, String cfText, int cfCaseCnt, double cfPersonalCost, double cfMaterialcost, double cfInfraCost, double cfRemunerationAmount, String cfRemunerationKey) {
        this._id = cfID;
        this._text = cfText;
        this._caseCnt = cfCaseCnt;
        this._personalCost = cfPersonalCost;
        this._materialcost = cfMaterialcost;
        this._infraCost = cfInfraCost;
        this._remunerationAmount = cfRemunerationAmount;
        this._remunerationKey = cfRemunerationKey;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 41 * hash + Objects.hashCode(this._text);
        hash = 41 * hash + this._caseCnt;
        hash = 41 * hash + (int) (Double.doubleToLongBits(this._personalCost) ^ (Double.doubleToLongBits(this._personalCost) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(this._materialcost) ^ (Double.doubleToLongBits(this._materialcost) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(this._infraCost) ^ (Double.doubleToLongBits(this._infraCost) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(this._remunerationAmount) ^ (Double.doubleToLongBits(this._remunerationAmount) >>> 32));
        hash = 41 * hash + Objects.hashCode(this._remunerationKey);
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
        final KGLListCentralFocus other = (KGLListCentralFocus) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._caseCnt != other._caseCnt) {
            return false;
        }
        if (Double.doubleToLongBits(this._personalCost) != Double.doubleToLongBits(other._personalCost)) {
            return false;
        }
        if (Double.doubleToLongBits(this._materialcost) != Double.doubleToLongBits(other._materialcost)) {
            return false;
        }
        if (Double.doubleToLongBits(this._infraCost) != Double.doubleToLongBits(other._infraCost)) {
            return false;
        }
        if (Double.doubleToLongBits(this._remunerationAmount) != Double.doubleToLongBits(other._remunerationAmount)) {
            return false;
        }
        if (!Objects.equals(this._text, other._text)) {
            return false;
        }
        if (!Objects.equals(this._remunerationKey, other._remunerationKey)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListCentralFocus[ cfID=" + _id + " ]";
    }
    //</editor-fold>
}
