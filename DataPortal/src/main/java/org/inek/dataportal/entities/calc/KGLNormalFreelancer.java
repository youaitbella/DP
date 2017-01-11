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
@Table(name = "KGLNormalFreelancer", schema = "calc")
@XmlRootElement
public class KGLNormalFreelancer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "nfID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _division">
    @Basic(optional = false)
    @NotNull
    @Size(max = 300)
    @Column(name = "nfDivision")
    private String _division = "";

    public String getDivision() {
        return _division;
    }

    public void setDivision(String division) {
        this._division = division;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _fullVigorCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "nfFullVigorCnt")
    private double _fullVigorCnt;

    public double getFullVigorCnt() {
        return _fullVigorCnt;
    }

    public void setFullVigorCnt(double fullVigorCnt) {
        this._fullVigorCnt = fullVigorCnt;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _amount">
    @Basic(optional = false)
    @NotNull
    @Column(name = "nfAmount")
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costType1">
    @Basic(optional = false)
    @NotNull
    @Column(name = "nfCostType1")
    private boolean _costType1;

    public boolean isCostType1() {
        return _costType1;
    }

    public void setCostType1(boolean costType1) {
        this._costType1 = costType1;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costType6c">
    @Basic(optional = false)
    @NotNull
    @Column(name = "nfCostType6c")
    private boolean _costType6c;

    public boolean isCostType6c() {
        return _costType6c;
    }

    public void setCostType6c(boolean costType6c) {
        this._costType6c = costType6c;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationID">
//    @JoinColumn(name = "nfBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "nfBaseInformationID")
    private int _baseInformationID;

    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    //</editor-fold>
    

    public KGLNormalFreelancer() {
    }

    public KGLNormalFreelancer(Integer nfID) {
        this._id = nfID;
    }

    public KGLNormalFreelancer(Integer nfID, String nfDivision, double nfFullVigorCnt, int nfAmount, boolean nfCostType1, boolean nfCostType6c) {
        this._id = nfID;
        this._division = nfDivision;
        this._fullVigorCnt = nfFullVigorCnt;
        this._amount = nfAmount;
        this._costType1 = nfCostType1;
        this._costType6c = nfCostType6c;
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
        hash = 67 * hash + this._baseInformationID;
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
        if (this._id != -1 && this._id == other._id) {
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
        if (this._baseInformationID != other._baseInformationID) {
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
