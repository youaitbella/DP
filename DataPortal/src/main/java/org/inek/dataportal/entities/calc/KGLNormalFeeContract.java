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
    @NamedQuery(name = "KGLNormalFeeContract.findAll", query = "SELECT k FROM KGLNormalFeeContract k")
    , @NamedQuery(name = "KGLNormalFeeContract.findByNfcID", query = "SELECT k FROM KGLNormalFeeContract k WHERE k.nfcID = :nfcID")
    , @NamedQuery(name = "KGLNormalFeeContract.findByNfcDivision", query = "SELECT k FROM KGLNormalFeeContract k WHERE k.nfcDivision = :nfcDivision")
    , @NamedQuery(name = "KGLNormalFeeContract.findByNfcDepartmentKey", query = "SELECT k FROM KGLNormalFeeContract k WHERE k.nfcDepartmentKey = :nfcDepartmentKey")
    , @NamedQuery(name = "KGLNormalFeeContract.findByNfcCaseCnt", query = "SELECT k FROM KGLNormalFeeContract k WHERE k.nfcCaseCnt = :nfcCaseCnt")
    , @NamedQuery(name = "KGLNormalFeeContract.findByNfcAmount", query = "SELECT k FROM KGLNormalFeeContract k WHERE k.nfcAmount = :nfcAmount")})
public class KGLNormalFeeContract implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer nfcID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    private String nfcDivision;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    private String nfcDepartmentKey;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    private BigDecimal nfcCaseCnt;
    @Basic(optional = false)
    @NotNull
    private BigDecimal nfcAmount;
    @JoinColumn(name = "nfcBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics nfcBaseInformationID;

    public KGLNormalFeeContract() {
    }

    public KGLNormalFeeContract(Integer nfcID) {
        this.nfcID = nfcID;
    }

    public KGLNormalFeeContract(Integer nfcID, String nfcDivision, String nfcDepartmentKey, BigDecimal nfcCaseCnt, BigDecimal nfcAmount) {
        this.nfcID = nfcID;
        this.nfcDivision = nfcDivision;
        this.nfcDepartmentKey = nfcDepartmentKey;
        this.nfcCaseCnt = nfcCaseCnt;
        this.nfcAmount = nfcAmount;
    }

    public Integer getNfcID() {
        return nfcID;
    }

    public void setNfcID(Integer nfcID) {
        this.nfcID = nfcID;
    }

    public String getNfcDivision() {
        return nfcDivision;
    }

    public void setNfcDivision(String nfcDivision) {
        this.nfcDivision = nfcDivision;
    }

    public String getNfcDepartmentKey() {
        return nfcDepartmentKey;
    }

    public void setNfcDepartmentKey(String nfcDepartmentKey) {
        this.nfcDepartmentKey = nfcDepartmentKey;
    }

    public BigDecimal getNfcCaseCnt() {
        return nfcCaseCnt;
    }

    public void setNfcCaseCnt(BigDecimal nfcCaseCnt) {
        this.nfcCaseCnt = nfcCaseCnt;
    }

    public BigDecimal getNfcAmount() {
        return nfcAmount;
    }

    public void setNfcAmount(BigDecimal nfcAmount) {
        this.nfcAmount = nfcAmount;
    }

    public DrgCalcBasics getNfcBaseInformationID() {
        return nfcBaseInformationID;
    }

    public void setNfcBaseInformationID(DrgCalcBasics nfcBaseInformationID) {
        this.nfcBaseInformationID = nfcBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nfcID != null ? nfcID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLNormalFeeContract)) {
            return false;
        }
        KGLNormalFeeContract other = (KGLNormalFeeContract) object;
        if ((this.nfcID == null && other.nfcID != null) || (this.nfcID != null && !this.nfcID.equals(other.nfcID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLNormalFeeContract[ nfcID=" + nfcID + " ]";
    }
    
}
