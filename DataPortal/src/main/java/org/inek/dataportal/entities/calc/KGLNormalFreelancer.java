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
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLNormalFreelancer", schema = "calc")
public class KGLNormalFreelancer implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nfID", updatable = false, nullable = false)
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

    //<editor-fold defaultstate="collapsed" desc="Property _division">
    @Column(name = "nfDivision")
    @Documentation (name = "Bereich", rank = 10)
    private String _division = "";

    @Size(max = 300)
    public String getDivision() {
        return _division;
    }

    public void setDivision(String division) {
        this._division = division;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _fullVigorCnt">
    @Column(name = "nfFullVigorCnt")
    @Documentation (name = "AnzahlVK", rank = 20)
    private double _fullVigorCnt;

    public double getFullVigorCnt() {
        return _fullVigorCnt;
    }

    public void setFullVigorCnt(double fullVigorCnt) {
        this._fullVigorCnt = fullVigorCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _amount">
    @Column(name = "nfAmount")
    @Documentation (name = "Kostenvolumen", rank = 30)
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costType1">
    @Column(name = "nfCostType1")
    @Documentation (name = "KoArtGr 1", rank = 40)
    private boolean _costType1;

    public boolean isCostType1() {
        return _costType1;
    }

    public void setCostType1(boolean costType1) {
        this._costType1 = costType1;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costType6c">
    @Column(name = "nfCostType6c")
    @Documentation (name = "KoArtGr 6c", rank = 50)
    private boolean _costType6c;

    public boolean isCostType6c() {
        return _costType6c;
    }

    public void setCostType6c(boolean costType6c) {
        this._costType6c = costType6c;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "nfBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "nfBaseInformationId")
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

    //</editor-fold>
    public KGLNormalFreelancer() {
    }

    public KGLNormalFreelancer(int baseId) {
        _baseInformationId = baseId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 67 * hash + Objects.hashCode(this._division);
        hash = 67 * hash + (int) (Double.doubleToLongBits(this._fullVigorCnt) ^ (Double.doubleToLongBits(this._fullVigorCnt) >>> 32));
        hash = 67 * hash + this._amount;
        hash = 67 * hash + (this._costType1 ? 1 : 0);
        hash = 67 * hash + (this._costType6c ? 1 : 0);
        hash = 67 * hash + this._baseInformationId;
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
        final KGLNormalFreelancer other = (KGLNormalFreelancer) obj;
        if (this._id > -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (Double.doubleToLongBits(this._fullVigorCnt) != Double.doubleToLongBits(other._fullVigorCnt)) {
            return false;
        }
        if (this._amount != other._amount) {
            return false;
        }
        if (this._costType1 != other._costType1) {
            return false;
        }
        if (this._costType6c != other._costType6c) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (!Objects.equals(this._division, other._division)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLNormalFreelancer[ nfID=" + _id + " ]";
    }

}
