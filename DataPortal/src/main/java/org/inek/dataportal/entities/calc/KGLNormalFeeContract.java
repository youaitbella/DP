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
@Table(catalog = "dataportaldev", schema = "calc")
@XmlRootElement
public class KGLNormalFeeContract implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "nfcID")
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
    @Column(name = "nfcDivision")
    private String _division = "";

    public String getDivision() {
        return _division;
    }

    public void setDivision(String division) {
        this._division = division;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _departmentKey">
    @Basic(optional = false)
    @NotNull
    @Size(max = 4)
    @Column(name = "nfcDepartmentKey")
    private String _departmentKey = "";

    public String getDepartmentKey() {
        return _departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this._departmentKey = departmentKey;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _caseCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "nfcCaseCnt")
    private double _caseCnt;

    public double getCaseCnt() {
        return _caseCnt;
    }

    public void setCaseCnt(double caseCnt) {
        this._caseCnt = caseCnt;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _amount">
    @Basic(optional = false)
    @NotNull
    @Column(name = "nfcAmount")
    private double _amount;

    public double getAmount() {
        return _amount;
    }

    public void setAmount(double amount) {
        this._amount = amount;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationID">
//    @JoinColumn(name = "nfcBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "nfcBaseInformationID")
    private int _baseInformationID;
    
    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    //</editor-fold>
    
    public KGLNormalFeeContract() {
    }

    public KGLNormalFeeContract(Integer nfcID) {
        this._id = nfcID;
    }

    public KGLNormalFeeContract(Integer nfcID, String nfcDivision, String nfcDepartmentKey, double nfcCaseCnt, double nfcAmount) {
        this._id = nfcID;
        this._division = nfcDivision;
        this._departmentKey = nfcDepartmentKey;
        this._caseCnt = nfcCaseCnt;
        this._amount = nfcAmount;
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLNormalFeeContract)) {
            return false;
        }
        KGLNormalFeeContract other = (KGLNormalFeeContract) object;
        return this._id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLNormalFeeContract[ nfcID=" + _id + " ]";
    }
    
}
