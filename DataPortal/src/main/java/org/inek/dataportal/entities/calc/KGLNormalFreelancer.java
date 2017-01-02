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
    @NamedQuery(name = "KGLNormalFreelancer.findAll", query = "SELECT k FROM KGLNormalFreelancer k")
    , @NamedQuery(name = "KGLNormalFreelancer.findByNfID", query = "SELECT k FROM KGLNormalFreelancer k WHERE k.nfID = :nfID")
    , @NamedQuery(name = "KGLNormalFreelancer.findByNfDivision", query = "SELECT k FROM KGLNormalFreelancer k WHERE k.nfDivision = :nfDivision")
    , @NamedQuery(name = "KGLNormalFreelancer.findByNfFullVigorCnt", query = "SELECT k FROM KGLNormalFreelancer k WHERE k.nfFullVigorCnt = :nfFullVigorCnt")
    , @NamedQuery(name = "KGLNormalFreelancer.findByNfAmount", query = "SELECT k FROM KGLNormalFreelancer k WHERE k.nfAmount = :nfAmount")
    , @NamedQuery(name = "KGLNormalFreelancer.findByNfCostType1", query = "SELECT k FROM KGLNormalFreelancer k WHERE k.nfCostType1 = :nfCostType1")
    , @NamedQuery(name = "KGLNormalFreelancer.findByNfCostType6c", query = "SELECT k FROM KGLNormalFreelancer k WHERE k.nfCostType6c = :nfCostType6c")})
public class KGLNormalFreelancer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer nfID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    private String nfDivision;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    private BigDecimal nfFullVigorCnt;
    @Basic(optional = false)
    @NotNull
    private BigDecimal nfAmount;
    @Basic(optional = false)
    @NotNull
    private boolean nfCostType1;
    @Basic(optional = false)
    @NotNull
    private boolean nfCostType6c;
    @JoinColumn(name = "nfBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics nfBaseInformationID;

    public KGLNormalFreelancer() {
    }

    public KGLNormalFreelancer(Integer nfID) {
        this.nfID = nfID;
    }

    public KGLNormalFreelancer(Integer nfID, String nfDivision, BigDecimal nfFullVigorCnt, BigDecimal nfAmount, boolean nfCostType1, boolean nfCostType6c) {
        this.nfID = nfID;
        this.nfDivision = nfDivision;
        this.nfFullVigorCnt = nfFullVigorCnt;
        this.nfAmount = nfAmount;
        this.nfCostType1 = nfCostType1;
        this.nfCostType6c = nfCostType6c;
    }

    public Integer getNfID() {
        return nfID;
    }

    public void setNfID(Integer nfID) {
        this.nfID = nfID;
    }

    public String getNfDivision() {
        return nfDivision;
    }

    public void setNfDivision(String nfDivision) {
        this.nfDivision = nfDivision;
    }

    public BigDecimal getNfFullVigorCnt() {
        return nfFullVigorCnt;
    }

    public void setNfFullVigorCnt(BigDecimal nfFullVigorCnt) {
        this.nfFullVigorCnt = nfFullVigorCnt;
    }

    public BigDecimal getNfAmount() {
        return nfAmount;
    }

    public void setNfAmount(BigDecimal nfAmount) {
        this.nfAmount = nfAmount;
    }

    public boolean getNfCostType1() {
        return nfCostType1;
    }

    public void setNfCostType1(boolean nfCostType1) {
        this.nfCostType1 = nfCostType1;
    }

    public boolean getNfCostType6c() {
        return nfCostType6c;
    }

    public void setNfCostType6c(boolean nfCostType6c) {
        this.nfCostType6c = nfCostType6c;
    }

    public DrgCalcBasics getNfBaseInformationID() {
        return nfBaseInformationID;
    }

    public void setNfBaseInformationID(DrgCalcBasics nfBaseInformationID) {
        this.nfBaseInformationID = nfBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nfID != null ? nfID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLNormalFreelancer)) {
            return false;
        }
        KGLNormalFreelancer other = (KGLNormalFreelancer) object;
        if ((this.nfID == null && other.nfID != null) || (this.nfID != null && !this.nfID.equals(other.nfID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLNormalFreelancer[ nfID=" + nfID + " ]";
    }
    
}
