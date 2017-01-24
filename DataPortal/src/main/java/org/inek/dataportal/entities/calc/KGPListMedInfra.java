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
@Table(name = "KGPListMedInfra", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListMedInfra.findAll", query = "SELECT k FROM KGPListMedInfra k")})
public class KGPListMedInfra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "miID")
    private Integer miID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "miCostTypeID")
    private int miCostTypeID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "miCostCenterNumber")
    private String miCostCenterNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "miCostCenterText")
    private String miCostCenterText;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "miKeyUsed")
    private String miKeyUsed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "miAmount")
    private int miAmount;
    @JoinColumn(name = "miBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private PeppCalcBasics miBaseInformationID;

    public KGPListMedInfra() {
    }

    public KGPListMedInfra(Integer miID) {
        this.miID = miID;
    }

    public KGPListMedInfra(Integer miID, int miCostTypeID, String miCostCenterNumber, String miCostCenterText, String miKeyUsed, int miAmount) {
        this.miID = miID;
        this.miCostTypeID = miCostTypeID;
        this.miCostCenterNumber = miCostCenterNumber;
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

    public String getMiCostCenterNumber() {
        return miCostCenterNumber;
    }

    public void setMiCostCenterNumber(String miCostCenterNumber) {
        this.miCostCenterNumber = miCostCenterNumber;
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

    public int getMiAmount() {
        return miAmount;
    }

    public void setMiAmount(int miAmount) {
        this.miAmount = miAmount;
    }

    public PeppCalcBasics getMiBaseInformationID() {
        return miBaseInformationID;
    }

    public void setMiBaseInformationID(PeppCalcBasics miBaseInformationID) {
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
        if (!(object instanceof KGPListMedInfra)) {
            return false;
        }
        KGPListMedInfra other = (KGPListMedInfra) object;
        if ((this.miID == null && other.miID != null) || (this.miID != null && !this.miID.equals(other.miID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListMedInfra[ miID=" + miID + " ]";
    }
    
}
