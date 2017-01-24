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
@Table(name = "KGPListCostCenter", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListCostCenter.findAll", query = "SELECT k FROM KGPListCostCenter k")})
public class KGPListCostCenter implements Serializable {

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
    @Column(name = "ccCostCenterNumber")
    private int ccCostCenterNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ccCostCenterText")
    private String ccCostCenterText;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ccAmount")
    private int ccAmount;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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
    private PeppCalcBasics ccBaseInformationID;

    public KGPListCostCenter() {
    }

    public KGPListCostCenter(Integer ccID) {
        this.ccID = ccID;
    }

    public KGPListCostCenter(Integer ccID, int ccCostCenterID, int ccCostCenterNumber, String ccCostCenterText, int ccAmount, BigDecimal ccFullVigorCnt, String ccServiceKey, String ccServiceKeyDescription, BigDecimal ccServiceSum) {
        this.ccID = ccID;
        this.ccCostCenterID = ccCostCenterID;
        this.ccCostCenterNumber = ccCostCenterNumber;
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

    public int getCcCostCenterNumber() {
        return ccCostCenterNumber;
    }

    public void setCcCostCenterNumber(int ccCostCenterNumber) {
        this.ccCostCenterNumber = ccCostCenterNumber;
    }

    public String getCcCostCenterText() {
        return ccCostCenterText;
    }

    public void setCcCostCenterText(String ccCostCenterText) {
        this.ccCostCenterText = ccCostCenterText;
    }

    public int getCcAmount() {
        return ccAmount;
    }

    public void setCcAmount(int ccAmount) {
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

    public PeppCalcBasics getCcBaseInformationID() {
        return ccBaseInformationID;
    }

    public void setCcBaseInformationID(PeppCalcBasics ccBaseInformationID) {
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
        if (!(object instanceof KGPListCostCenter)) {
            return false;
        }
        KGPListCostCenter other = (KGPListCostCenter) object;
        if ((this.ccID == null && other.ccID != null) || (this.ccID != null && !this.ccID.equals(other.ccID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListCostCenter[ ccID=" + ccID + " ]";
    }
    
}
