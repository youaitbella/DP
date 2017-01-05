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
@Table(name = "KGLRadiologyService", schema = "calc")
@XmlRootElement
public class KGLRadiologyService implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "rsID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _opsCode">
    @Basic(optional = false)
    @NotNull
    @Size(max = 10)
    @Column(name = "rsOpsCode")
    private String _opsCode = "";

    public String getOpsCode() {
        return _opsCode;
    }

    public void setOpsCode(String opsCode) {
        this._opsCode = opsCode;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceCost">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rsServiceCost")
    private double _serviceCost;

    public double getServiceCost() {
        return _serviceCost;
    }

    public void setServiceCost(double serviceCost) {
        this._serviceCost = serviceCost;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _caseCntStationary">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rsCaseCntStationary")
    private int _caseCntStationary;

    public int getCaseCntStationary() {
        return _caseCntStationary;
    }

    public void setCaseCntStationary(int caseCntStationary) {
        this._caseCntStationary = caseCntStationary;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _caseCntAmbulant">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rsCaseCntAmbulant")
    private int _caseCntAmbulant;

    public int getCaseCntAmbulant() {
        return _caseCntAmbulant;
    }

    public void setCaseCntAmbulant(int caseCntAmbulant) {
        this._caseCntAmbulant = caseCntAmbulant;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _ambulantAmount">
    @Basic(optional = false)
    @NotNull
    @Column(name = "rsAbulantAmount")
    private double _ambulantAmount;

    public double getAmbulantAmount() {
        return _ambulantAmount;
    }

    public void setAmbulantAmount(double abulantAmount) {
        this._ambulantAmount = abulantAmount;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property rsBaseInformationID">
//    @JoinColumn(name = "rsBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "rsBaseInformationID")
    private int rsBaseInformationID;

    public int getRsBaseInformationID() {
        return rsBaseInformationID;
    }

    public void setRsBaseInformationID(int rsBaseInformationID) {
        this.rsBaseInformationID = rsBaseInformationID;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property rsContentTextID">
//    @JoinColumn(name = "rsContentTextID", referencedColumnName = "ctID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "rsContentTextID")
    private int rsContentTextID;

    public int getRsContentTextID() {
        return rsContentTextID;
    }

    public void setRsContentTextID(int rsContentTextID) {
        this.rsContentTextID = rsContentTextID;
    }
    //</editor-fold>
    

    public KGLRadiologyService() {
    }

    public KGLRadiologyService(Integer rsID) {
        this._id = rsID;
    }

    public KGLRadiologyService(Integer rsID, String rsOpsCode, double rsServiceCost, int rsCaseCntStationary, int rsCaseCntAmbulant, double rsAbulantAmount) {
        this._id = rsID;
        this._opsCode = rsOpsCode;
        this._serviceCost = rsServiceCost;
        this._caseCntStationary = rsCaseCntStationary;
        this._caseCntAmbulant = rsCaseCntAmbulant;
        this._ambulantAmount = rsAbulantAmount;
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLRadiologyService)) {
            return false;
        }
        KGLRadiologyService other = (KGLRadiologyService) object;
        return this._id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLRadiologyService[ rsID=" + _id + " ]";
    }
    
}
