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
@Table(name = "KGLListCostCenter", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListCostCenter.findAll", query = "SELECT k FROM KGLListCostCenter k")
    , @NamedQuery(name = "KGLListCostCenter.findByCcID", query = "SELECT k FROM KGLListCostCenter k WHERE k.ccID = :ccID")
    , @NamedQuery(name = "KGLListCostCenter.findByCcCostCenterID", query = "SELECT k FROM KGLListCostCenter k WHERE k.ccCostCenterID = :ccCostCenterID")
    , @NamedQuery(name = "KGLListCostCenter.findByCcCostCenterText", query = "SELECT k FROM KGLListCostCenter k WHERE k.ccCostCenterText = :ccCostCenterText")
    , @NamedQuery(name = "KGLListCostCenter.findByCcAmount", query = "SELECT k FROM KGLListCostCenter k WHERE k.ccAmount = :ccAmount")
    , @NamedQuery(name = "KGLListCostCenter.findByCcFullVigorCnt", query = "SELECT k FROM KGLListCostCenter k WHERE k.ccFullVigorCnt = :ccFullVigorCnt")
    , @NamedQuery(name = "KGLListCostCenter.findByCcServiceKey", query = "SELECT k FROM KGLListCostCenter k WHERE k.ccServiceKey = :ccServiceKey")
    , @NamedQuery(name = "KGLListCostCenter.findByCcServiceKeyDescription", query = "SELECT k FROM KGLListCostCenter k WHERE k.ccServiceKeyDescription = :ccServiceKeyDescription")
    , @NamedQuery(name = "KGLListCostCenter.findByCcServiceSum", query = "SELECT k FROM KGLListCostCenter k WHERE k.ccServiceSum = :ccServiceSum")})
public class KGLListCostCenter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ccID")
    private Integer ccID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ccCostCenterID")
    private int ccCostCenterID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ccCostCenterText")
    private String ccCostCenterText;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "ccAmount")
    private BigDecimal ccAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ccFullVigorCnt")
    private BigDecimal ccFullVigorCnt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ccServiceKey")
    private String ccServiceKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "ccServiceKeyDescription")
    private String ccServiceKeyDescription;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ccServiceSum")
    private BigDecimal ccServiceSum;
    @JoinColumn(name = "ccBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics ccBaseInformationID;

    public KGLListCostCenter() {
    }

    public KGLListCostCenter(Integer ccID) {
        this.ccID = ccID;
    }

    public KGLListCostCenter(Integer ccID, int ccCostCenterID, String ccCostCenterText, BigDecimal ccAmount, BigDecimal ccFullVigorCnt, String ccServiceKey, String ccServiceKeyDescription, BigDecimal ccServiceSum) {
        this.ccID = ccID;
        this.ccCostCenterID = ccCostCenterID;
        this.ccCostCenterText = ccCostCenterText;
        this.ccAmount = ccAmount;
        this.ccFullVigorCnt = ccFullVigorCnt;
        this.ccServiceKey = ccServiceKey;
        this.ccServiceKeyDescription = ccServiceKeyDescription;
        this.ccServiceSum = ccServiceSum;
    }

    public Integer getCcID() {
        return ccID;
    }

    public void setCcID(Integer ccID) {
        this.ccID = ccID;
    }

    public int getCcCostCenterID() {
        return ccCostCenterID;
    }

    public void setCcCostCenterID(int ccCostCenterID) {
        this.ccCostCenterID = ccCostCenterID;
    }

    public String getCcCostCenterText() {
        return ccCostCenterText;
    }

    public void setCcCostCenterText(String ccCostCenterText) {
        this.ccCostCenterText = ccCostCenterText;
    }

    public BigDecimal getCcAmount() {
        return ccAmount;
    }

    public void setCcAmount(BigDecimal ccAmount) {
        this.ccAmount = ccAmount;
    }

    public BigDecimal getCcFullVigorCnt() {
        return ccFullVigorCnt;
    }

    public void setCcFullVigorCnt(BigDecimal ccFullVigorCnt) {
        this.ccFullVigorCnt = ccFullVigorCnt;
    }

    public String getCcServiceKey() {
        return ccServiceKey;
    }

    public void setCcServiceKey(String ccServiceKey) {
        this.ccServiceKey = ccServiceKey;
    }

    public String getCcServiceKeyDescription() {
        return ccServiceKeyDescription;
    }

    public void setCcServiceKeyDescription(String ccServiceKeyDescription) {
        this.ccServiceKeyDescription = ccServiceKeyDescription;
    }

    public BigDecimal getCcServiceSum() {
        return ccServiceSum;
    }

    public void setCcServiceSum(BigDecimal ccServiceSum) {
        this.ccServiceSum = ccServiceSum;
    }

    public DrgCalcBasics getCcBaseInformationID() {
        return ccBaseInformationID;
    }

    public void setCcBaseInformationID(DrgCalcBasics ccBaseInformationID) {
        this.ccBaseInformationID = ccBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ccID != null ? ccID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListCostCenter)) {
            return false;
        }
        KGLListCostCenter other = (KGLListCostCenter) object;
        if ((this.ccID == null && other.ccID != null) || (this.ccID != null && !this.ccID.equals(other.ccID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListCostCenter[ ccID=" + ccID + " ]";
    }
    
}
