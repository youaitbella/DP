/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
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
@Table(catalog = "dataportaldev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListMedInfra.findAll", query = "SELECT k FROM KGLListMedInfra k")
    , @NamedQuery(name = "KGLListMedInfra.findByMiID", query = "SELECT k FROM KGLListMedInfra k WHERE k.miID = :miID")
    , @NamedQuery(name = "KGLListMedInfra.findByMiCostTypeID", query = "SELECT k FROM KGLListMedInfra k WHERE k.miCostTypeID = :miCostTypeID")
    , @NamedQuery(name = "KGLListMedInfra.findByMiCostCenter", query = "SELECT k FROM KGLListMedInfra k WHERE k.miCostCenter = :miCostCenter")
    , @NamedQuery(name = "KGLListMedInfra.findByMiCostCenterText", query = "SELECT k FROM KGLListMedInfra k WHERE k.miCostCenterText = :miCostCenterText")
    , @NamedQuery(name = "KGLListMedInfra.findByMiKeyUsed", query = "SELECT k FROM KGLListMedInfra k WHERE k.miKeyUsed = :miKeyUsed")
    , @NamedQuery(name = "KGLListMedInfra.findByMiAmount", query = "SELECT k FROM KGLListMedInfra k WHERE k.miAmount = :miAmount")})
public class KGLListMedInfra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer miID;
    @Basic(optional = false)
    @NotNull
    private int miCostTypeID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    private String miCostCenter;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String miCostCenterText;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    private String miKeyUsed;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    private BigDecimal miAmount;
    @JoinColumn(name = "miBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics miBaseInformationID;

    public KGLListMedInfra() {
    }

    public KGLListMedInfra(Integer miID) {
        this.miID = miID;
    }

    public KGLListMedInfra(Integer miID, int miCostTypeID, String miCostCenter, String miCostCenterText, String miKeyUsed, BigDecimal miAmount) {
        this.miID = miID;
        this.miCostTypeID = miCostTypeID;
        this.miCostCenter = miCostCenter;
        this.miCostCenterText = miCostCenterText;
        this.miKeyUsed = miKeyUsed;
        this.miAmount = miAmount;
    }

    public Integer getMiID() {
        return miID;
    }

    public void setMiID(Integer miID) {
        this.miID = miID;
    }

    public int getMiCostTypeID() {
        return miCostTypeID;
    }

    public void setMiCostTypeID(int miCostTypeID) {
        this.miCostTypeID = miCostTypeID;
    }

    public String getMiCostCenter() {
        return miCostCenter;
    }

    public void setMiCostCenter(String miCostCenter) {
        this.miCostCenter = miCostCenter;
    }

    public String getMiCostCenterText() {
        return miCostCenterText;
    }

    public void setMiCostCenterText(String miCostCenterText) {
        this.miCostCenterText = miCostCenterText;
    }

    public String getMiKeyUsed() {
        return miKeyUsed;
    }

    public void setMiKeyUsed(String miKeyUsed) {
        this.miKeyUsed = miKeyUsed;
    }

    public BigDecimal getMiAmount() {
        return miAmount;
    }

    public void setMiAmount(BigDecimal miAmount) {
        this.miAmount = miAmount;
    }

    public DrgCalcBasics getMiBaseInformationID() {
        return miBaseInformationID;
    }

    public void setMiBaseInformationID(DrgCalcBasics miBaseInformationID) {
        this.miBaseInformationID = miBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (miID != null ? miID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListMedInfra)) {
            return false;
        }
        KGLListMedInfra other = (KGLListMedInfra) object;
        if ((this.miID == null && other.miID != null) || (this.miID != null && !this.miID.equals(other.miID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListMedInfra[ miID=" + miID + " ]";
    }
    
}
