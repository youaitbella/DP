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
@Table(name = "KGLListSpecialUnit", schema = "calc")
@XmlRootElement
public class KGLListSpecialUnit implements Serializable {
    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "suID")
    private int _id = -1;
    
    public int getId() {
        return _id;
    }
    
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="BaseInformationID">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "suBaseInformationID")
    private int _baseInformationId = -1;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="type">
    @Basic(optional = false)
    @NotNull
    @Size(max = 300)
    @Column(name = "suType")
    private String _type = "";
    
    public String getType() {
        return _type;
    }

    public void setType(String type) {
        this._type = type;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="caseCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "suCaseCnt")
    private int _caseCnt;
    
    public int getCaseCnt() {
        return _caseCnt;
    }

    public void setCaseCnt(int caseCnt) {
        this._caseCnt = caseCnt;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="cost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "suCost")
    private double _cost;


    public double getCost() {
        return _cost;
    }

    public void setCost(double cost) {
        this._cost = cost;
    }
    //</editor-fold>
    
    public KGLListSpecialUnit() {
    }

    public KGLListSpecialUnit(int suID) {
        this._id = suID;
    }

    public KGLListSpecialUnit(int suID, int baseInformationId, String suType, int suCaseCnt, double suCost) {
        this._id = suID;
        this._baseInformationId = baseInformationId;
        this._type = suType;
        this._caseCnt = suCaseCnt;
        this._cost = suCost;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 97 * hash + this._baseInformationId;
        hash = 97 * hash + Objects.hashCode(this._type);
        hash = 97 * hash + this._caseCnt;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this._cost) ^ (Double.doubleToLongBits(this._cost) >>> 32));
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
        final KGLListSpecialUnit other = (KGLListSpecialUnit) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._caseCnt != other._caseCnt) {
            return false;
        }
        if (Double.doubleToLongBits(this._cost) != Double.doubleToLongBits(other._cost)) {
            return false;
        }
        if (!Objects.equals(this._type, other._type)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListSpecialUnit[ suID=" + _id + " ]";
    }
    //</editor-fold>
    
}
