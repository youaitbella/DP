/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cfID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="baseInformationID">
    @Column(name = "cfBaseInformationID")
    private int _baseInformationID = -1;

    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="text">
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
    @Column(name = "cfPersonalCost")
    private int _personalCost;

    public int getPersonalCost() {
        return _personalCost;
    }

    public void setPersonalCost(int personalCost) {
        this._personalCost = personalCost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="materialcost">
    @Column(name = "cfMaterialcost")
    private int _materialcost;

    public int getMaterialcost() {
        return _materialcost;
    }

    public void setMaterialcost(int materialcost) {
        this._materialcost = materialcost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="_infraCost">
    @Column(name = "cfInfraCost")
    private int _infraCost;

    public int getInfraCost() {
        return _infraCost;
    }

    public void setInfraCost(int infraCost) {
        this._infraCost = infraCost;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="remunerationAmount">
    @Column(name = "cfRemunerationAmount")
    private int _remunerationAmount;

    public int getRemunerationAmount() {
        return _remunerationAmount;
    }

    public void setRemunerationAmount(int remunerationAmount) {
        this._remunerationAmount = remunerationAmount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="_remunerationKey">
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

    public KGLListCentralFocus(int cfID, int baseInformationID, String cfText, int cfCaseCnt, int cfPersonalCost, int cfMaterialcost, int cfInfraCost, int cfRemunerationAmount, String cfRemunerationKey) {
        this._id = cfID;
        this._baseInformationID = baseInformationID;
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
        hash = 41 * hash + this._baseInformationID;
        hash = 41 * hash + Objects.hashCode(this._text);
        hash = 41 * hash + this._caseCnt;
        hash = 41 * hash + this._personalCost;
        hash = 41 * hash + this._materialcost;
        hash = 41 * hash + this._infraCost;
        hash = 41 * hash + this._remunerationAmount;
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
        if (this._baseInformationID != other._baseInformationID) {
            return false;
        }
        if (this._caseCnt != other._caseCnt) {
            return false;
        }
        if (this._personalCost != other._personalCost) {
            return false;
        }
        if (this._materialcost != other._materialcost) {
            return false;
        }
        if (this._infraCost != other._infraCost) {
            return false;
        }
        if (this._remunerationAmount != other._remunerationAmount) {
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
