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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLNeonatCntCost", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLNeonatCntCost.findAll", query = "SELECT k FROM KGLNeonatCntCost k")
    , @NamedQuery(name = "KGLNeonatCntCost.findByNccID", query = "SELECT k FROM KGLNeonatCntCost k WHERE k.nccID = :nccID")
    , @NamedQuery(name = "KGLNeonatCntCost.findByNccValue", query = "SELECT k FROM KGLNeonatCntCost k WHERE k.nccValue = :nccValue")})
public class KGLNeonatCntCost implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "nccID")
    private Integer nccID;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "nccValue")
    private BigDecimal nccValue;
    @JoinColumn(name = "nccBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics nccBaseInformationID;
    @JoinColumn(name = "nccContentTextID", referencedColumnName = "ctID")
    @ManyToOne(optional = false)
    private DrgContentText nccContentTextID;

    public KGLNeonatCntCost() {
    }

    public KGLNeonatCntCost(Integer nccID) {
        this.nccID = nccID;
    }

    public KGLNeonatCntCost(Integer nccID, BigDecimal nccValue) {
        this.nccID = nccID;
        this.nccValue = nccValue;
    }

    public Integer getNccID() {
        return nccID;
    }

    public void setNccID(Integer nccID) {
        this.nccID = nccID;
    }

    public BigDecimal getNccValue() {
        return nccValue;
    }

    public void setNccValue(BigDecimal nccValue) {
        this.nccValue = nccValue;
    }

    public DrgCalcBasics getNccBaseInformationID() {
        return nccBaseInformationID;
    }

    public void setNccBaseInformationID(DrgCalcBasics nccBaseInformationID) {
        this.nccBaseInformationID = nccBaseInformationID;
    }

    public DrgContentText getNccContentTextID() {
        return nccContentTextID;
    }

    public void setNccContentTextID(DrgContentText nccContentTextID) {
        this.nccContentTextID = nccContentTextID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nccID != null ? nccID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLNeonatCntCost)) {
            return false;
        }
        KGLNeonatCntCost other = (KGLNeonatCntCost) object;
        if ((this.nccID == null && other.nccID != null) || (this.nccID != null && !this.nccID.equals(other.nccID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLNeonatCntCost[ nccID=" + nccID + " ]";
    }
    
}
