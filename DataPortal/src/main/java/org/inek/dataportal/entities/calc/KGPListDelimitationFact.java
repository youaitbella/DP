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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListDelimitationFact", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListDelimitationFact.findAll", query = "SELECT k FROM KGPListDelimitationFact k")})
public class KGPListDelimitationFact implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "dfID")
    private Integer dfID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dfUsed")
    private boolean dfUsed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dfPersonalCost")
    private int dfPersonalCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dfMaterialcost")
    private int dfMaterialcost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dfInfraCost")
    private int dfInfraCost;
    @JoinColumn(name = "dfBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private PeppCalcBasics dfBaseInformationID;
    @JoinColumn(name = "dfContentTextID", referencedColumnName = "ctID")
    @ManyToOne(optional = false)
    private KGPListContentText dfContentTextID;

    public KGPListDelimitationFact() {
    }

    public KGPListDelimitationFact(Integer dfID) {
        this.dfID = dfID;
    }

    public KGPListDelimitationFact(Integer dfID, boolean dfUsed, int dfPersonalCost, int dfMaterialcost, int dfInfraCost) {
        this.dfID = dfID;
        this.dfUsed = dfUsed;
        this.dfPersonalCost = dfPersonalCost;
        this.dfMaterialcost = dfMaterialcost;
        this.dfInfraCost = dfInfraCost;
    }

    public Integer getDfID() {
        return dfID;
    }

    public void setDfID(Integer dfID) {
        this.dfID = dfID;
    }

    public boolean getDfUsed() {
        return dfUsed;
    }

    public void setDfUsed(boolean dfUsed) {
        this.dfUsed = dfUsed;
    }

    public int getDfPersonalCost() {
        return dfPersonalCost;
    }

    public void setDfPersonalCost(int dfPersonalCost) {
        this.dfPersonalCost = dfPersonalCost;
    }

    public int getDfMaterialcost() {
        return dfMaterialcost;
    }

    public void setDfMaterialcost(int dfMaterialcost) {
        this.dfMaterialcost = dfMaterialcost;
    }

    public int getDfInfraCost() {
        return dfInfraCost;
    }

    public void setDfInfraCost(int dfInfraCost) {
        this.dfInfraCost = dfInfraCost;
    }

    public PeppCalcBasics getDfBaseInformationID() {
        return dfBaseInformationID;
    }

    public void setDfBaseInformationID(PeppCalcBasics dfBaseInformationID) {
        this.dfBaseInformationID = dfBaseInformationID;
    }

    public KGPListContentText getDfContentTextID() {
        return dfContentTextID;
    }

    public void setDfContentTextID(KGPListContentText dfContentTextID) {
        this.dfContentTextID = dfContentTextID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dfID != null ? dfID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGPListDelimitationFact)) {
            return false;
        }
        KGPListDelimitationFact other = (KGPListDelimitationFact) object;
        if ((this.dfID == null && other.dfID != null) || (this.dfID != null && !this.dfID.equals(other.dfID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListDelimitationFact[ dfID=" + dfID + " ]";
    }
    
}
