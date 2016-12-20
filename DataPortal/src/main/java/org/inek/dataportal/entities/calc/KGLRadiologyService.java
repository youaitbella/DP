/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLRadiologyService", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLRadiologyService.findAll", query = "SELECT k FROM KGLRadiologyService k")
    , @NamedQuery(name = "KGLRadiologyService.findByRsID", query = "SELECT k FROM KGLRadiologyService k WHERE k.rsID = :rsID")
    , @NamedQuery(name = "KGLRadiologyService.findByRsOpsCode", query = "SELECT k FROM KGLRadiologyService k WHERE k.rsOpsCode = :rsOpsCode")
    , @NamedQuery(name = "KGLRadiologyService.findByRsServiceCost", query = "SELECT k FROM KGLRadiologyService k WHERE k.rsServiceCost = :rsServiceCost")
    , @NamedQuery(name = "KGLRadiologyService.findByRsCaseCntStationary", query = "SELECT k FROM KGLRadiologyService k WHERE k.rsCaseCntStationary = :rsCaseCntStationary")
    , @NamedQuery(name = "KGLRadiologyService.findByRsCaseCntAmbulant", query = "SELECT k FROM KGLRadiologyService k WHERE k.rsCaseCntAmbulant = :rsCaseCntAmbulant")
    , @NamedQuery(name = "KGLRadiologyService.findByRsAbulantAmount", query = "SELECT k FROM KGLRadiologyService k WHERE k.rsAbulantAmount = :rsAbulantAmount")})
public class KGLRadiologyService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "rsID")
    private Integer rsID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "rsOpsCode")
    private String rsOpsCode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "rsServiceCost")
    private BigDecimal rsServiceCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rsCaseCntStationary")
    private int rsCaseCntStationary;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rsCaseCntAmbulant")
    private int rsCaseCntAmbulant;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rsAbulantAmount")
    private BigDecimal rsAbulantAmount;
    @JoinColumn(name = "rsBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics rsBaseInformationID;
    @JoinColumn(name = "rsContentTextID", referencedColumnName = "ctID")
    @ManyToOne(optional = false)
    private DrgContentText rsContentTextID;

    public KGLRadiologyService() {
    }

    public KGLRadiologyService(Integer rsID) {
        this.rsID = rsID;
    }

    public KGLRadiologyService(Integer rsID, String rsOpsCode, BigDecimal rsServiceCost, int rsCaseCntStationary, int rsCaseCntAmbulant, BigDecimal rsAbulantAmount) {
        this.rsID = rsID;
        this.rsOpsCode = rsOpsCode;
        this.rsServiceCost = rsServiceCost;
        this.rsCaseCntStationary = rsCaseCntStationary;
        this.rsCaseCntAmbulant = rsCaseCntAmbulant;
        this.rsAbulantAmount = rsAbulantAmount;
    }

    public Integer getRsID() {
        return rsID;
    }

    public void setRsID(Integer rsID) {
        this.rsID = rsID;
    }

    public String getRsOpsCode() {
        return rsOpsCode;
    }

    public void setRsOpsCode(String rsOpsCode) {
        this.rsOpsCode = rsOpsCode;
    }

    public BigDecimal getRsServiceCost() {
        return rsServiceCost;
    }

    public void setRsServiceCost(BigDecimal rsServiceCost) {
        this.rsServiceCost = rsServiceCost;
    }

    public int getRsCaseCntStationary() {
        return rsCaseCntStationary;
    }

    public void setRsCaseCntStationary(int rsCaseCntStationary) {
        this.rsCaseCntStationary = rsCaseCntStationary;
    }

    public int getRsCaseCntAmbulant() {
        return rsCaseCntAmbulant;
    }

    public void setRsCaseCntAmbulant(int rsCaseCntAmbulant) {
        this.rsCaseCntAmbulant = rsCaseCntAmbulant;
    }

    public BigDecimal getRsAbulantAmount() {
        return rsAbulantAmount;
    }

    public void setRsAbulantAmount(BigDecimal rsAbulantAmount) {
        this.rsAbulantAmount = rsAbulantAmount;
    }

    public DrgCalcBasics getRsBaseInformationID() {
        return rsBaseInformationID;
    }

    public void setRsBaseInformationID(DrgCalcBasics rsBaseInformationID) {
        this.rsBaseInformationID = rsBaseInformationID;
    }

    public DrgContentText getRsContentTextID() {
        return rsContentTextID;
    }

    public void setRsContentTextID(DrgContentText rsContentTextID) {
        this.rsContentTextID = rsContentTextID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rsID != null ? rsID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLRadiologyService)) {
            return false;
        }
        KGLRadiologyService other = (KGLRadiologyService) object;
        if ((this.rsID == null && other.rsID != null) || (this.rsID != null && !this.rsID.equals(other.rsID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLRadiologyService[ rsID=" + rsID + " ]";
    }
    
}
