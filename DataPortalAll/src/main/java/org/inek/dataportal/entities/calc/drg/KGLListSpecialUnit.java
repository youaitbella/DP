/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.drg;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListSpecialUnit", schema = "calc")
@XmlRootElement
public class KGLListSpecialUnit implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suID", updatable = false, nullable = false)
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

    //<editor-fold defaultstate="collapsed" desc="BaseInformationId">
    @Column(name = "suBaseInformationId")
    private int _baseInformationId = -1;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="type">
    @Column(name = "suType")
    @Documentation(name = "Art der BE", rank = 10)
    private String _type = "";

    @Size(max = 300)
    public String getType() {
        return _type;
    }

    public void setType(String type) {
        this._type = type;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="caseCnt">
    @Column(name = "suCaseCnt")
    @Documentation(name = "Fallzahl", rank = 20)
    private int _caseCnt;

    public int getCaseCnt() {
        return _caseCnt;
    }

    public void setCaseCnt(int caseCnt) {
        this._caseCnt = caseCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cost">
    @Column(name = "suCost")
    @Documentation(name = "Kostenvolumen", rank = 30)
    private int _cost;

    public int getCost() {
        return _cost;
    }

    public void setCost(int cost) {
        this._cost = cost;
    }
    //</editor-fold>

    public KGLListSpecialUnit() {
    }

    public KGLListSpecialUnit(int suID) {
        this._id = suID;
    }

    public KGLListSpecialUnit(int suID, int baseInformationId, String suType, int suCaseCnt, int suCost) {
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
        hash = 97 * hash + this._cost;
        return hash;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
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
        if (this._cost != other._cost) {
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
