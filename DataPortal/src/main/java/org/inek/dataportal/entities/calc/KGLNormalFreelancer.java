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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
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
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    //<editor-fold defaultstate="collapsed" desc="Property ">
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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
    @Basic(optional = false)
    @NotNull
    @Column(name = "nfAmount")
    private double _amount;

    public double getAmount() {
        return _amount;
    }

    public void setAmount(double amount) {
        this._amount = amount;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
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

    public KGLNormalFreelancer(Integer nfID, String nfDivision, double nfFullVigorCnt, double nfAmount, boolean nfCostType1, boolean nfCostType6c) {
        this._id = nfID;
        this._division = nfDivision;
        this._fullVigorCnt = nfFullVigorCnt;
        this._amount = nfAmount;
        this._costType1 = nfCostType1;
        this._costType6c = nfCostType6c;
    }


    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLNormalFreelancer)) {
            return false;
        }
        KGLNormalFreelancer other = (KGLNormalFreelancer) object;
        return this._id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLNormalFreelancer[ nfID=" + _id + " ]";
    }
    
}
