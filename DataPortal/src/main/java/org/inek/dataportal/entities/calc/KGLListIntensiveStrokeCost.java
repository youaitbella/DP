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
@Table(name = "KGLListIntensiveStrokeCost")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListIntensiveStrokeCost.findAll", query = "SELECT k FROM KGLListIntensiveStrokeCost k")})
public class KGLListIntensiveStrokeCost implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscID")
    private Integer iscID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscIntensiveType")
    private int iscIntensiveType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscCostCenterID")
    private int iscCostCenterID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "iscCostCenterText")
    private String iscCostCenterText;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "iscDepartmentKey")
    private String iscDepartmentKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "iscDepartmentAssignment")
    private String iscDepartmentAssignment;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscMedicalServiceCnt")
    private BigDecimal iscMedicalServiceCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscNursingServiceCnt")
    private BigDecimal iscNursingServiceCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscFunctionalServiceCnt")
    private BigDecimal iscFunctionalServiceCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscMedicalServiceCost")
    private BigDecimal iscMedicalServiceCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscNursingServiceCost")
    private BigDecimal iscNursingServiceCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscFunctionalServiceCost")
    private BigDecimal iscFunctionalServiceCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscOverheadsMedicine")
    private BigDecimal iscOverheadsMedicine;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscOverheadMedicalGoods")
    private BigDecimal iscOverheadMedicalGoods;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscMedicalInfrastructureCost")
    private BigDecimal iscMedicalInfrastructureCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iscNonMedicalInfrastructureCost")
    private BigDecimal iscNonMedicalInfrastructureCost;
    @JoinColumn(name = "iscBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics iscBaseInformationID;

    public KGLListIntensiveStrokeCost() {
    }

    public KGLListIntensiveStrokeCost(Integer iscID) {
        this.iscID = iscID;
    }

    public KGLListIntensiveStrokeCost(Integer iscID, int iscIntensiveType, int iscCostCenterID, String iscCostCenterText, String iscDepartmentKey, String iscDepartmentAssignment, BigDecimal iscMedicalServiceCnt, BigDecimal iscNursingServiceCnt, BigDecimal iscFunctionalServiceCnt, BigDecimal iscMedicalServiceCost, BigDecimal iscNursingServiceCost, BigDecimal iscFunctionalServiceCost, BigDecimal iscOverheadsMedicine, BigDecimal iscOverheadMedicalGoods, BigDecimal iscMedicalInfrastructureCost, BigDecimal iscNonMedicalInfrastructureCost) {
        this.iscID = iscID;
        this.iscIntensiveType = iscIntensiveType;
        this.iscCostCenterID = iscCostCenterID;
        this.iscCostCenterText = iscCostCenterText;
        this.iscDepartmentKey = iscDepartmentKey;
        this.iscDepartmentAssignment = iscDepartmentAssignment;
        this.iscMedicalServiceCnt = iscMedicalServiceCnt;
        this.iscNursingServiceCnt = iscNursingServiceCnt;
        this.iscFunctionalServiceCnt = iscFunctionalServiceCnt;
        this.iscMedicalServiceCost = iscMedicalServiceCost;
        this.iscNursingServiceCost = iscNursingServiceCost;
        this.iscFunctionalServiceCost = iscFunctionalServiceCost;
        this.iscOverheadsMedicine = iscOverheadsMedicine;
        this.iscOverheadMedicalGoods = iscOverheadMedicalGoods;
        this.iscMedicalInfrastructureCost = iscMedicalInfrastructureCost;
        this.iscNonMedicalInfrastructureCost = iscNonMedicalInfrastructureCost;
    }

    public Integer getIscID() {
        return iscID;
    }

    public void setIscID(Integer iscID) {
        this.iscID = iscID;
    }

    public int getIscIntensiveType() {
        return iscIntensiveType;
    }

    public void setIscIntensiveType(int iscIntensiveType) {
        this.iscIntensiveType = iscIntensiveType;
    }

    public int getIscCostCenterID() {
        return iscCostCenterID;
    }

    public void setIscCostCenterID(int iscCostCenterID) {
        this.iscCostCenterID = iscCostCenterID;
    }

    public String getIscCostCenterText() {
        return iscCostCenterText;
    }

    public void setIscCostCenterText(String iscCostCenterText) {
        this.iscCostCenterText = iscCostCenterText;
    }

    public String getIscDepartmentKey() {
        return iscDepartmentKey;
    }

    public void setIscDepartmentKey(String iscDepartmentKey) {
        this.iscDepartmentKey = iscDepartmentKey;
    }

    public String getIscDepartmentAssignment() {
        return iscDepartmentAssignment;
    }

    public void setIscDepartmentAssignment(String iscDepartmentAssignment) {
        this.iscDepartmentAssignment = iscDepartmentAssignment;
    }

    public BigDecimal getIscMedicalServiceCnt() {
        return iscMedicalServiceCnt;
    }

    public void setIscMedicalServiceCnt(BigDecimal iscMedicalServiceCnt) {
        this.iscMedicalServiceCnt = iscMedicalServiceCnt;
    }

    public BigDecimal getIscNursingServiceCnt() {
        return iscNursingServiceCnt;
    }

    public void setIscNursingServiceCnt(BigDecimal iscNursingServiceCnt) {
        this.iscNursingServiceCnt = iscNursingServiceCnt;
    }

    public BigDecimal getIscFunctionalServiceCnt() {
        return iscFunctionalServiceCnt;
    }

    public void setIscFunctionalServiceCnt(BigDecimal iscFunctionalServiceCnt) {
        this.iscFunctionalServiceCnt = iscFunctionalServiceCnt;
    }

    public BigDecimal getIscMedicalServiceCost() {
        return iscMedicalServiceCost;
    }

    public void setIscMedicalServiceCost(BigDecimal iscMedicalServiceCost) {
        this.iscMedicalServiceCost = iscMedicalServiceCost;
    }

    public BigDecimal getIscNursingServiceCost() {
        return iscNursingServiceCost;
    }

    public void setIscNursingServiceCost(BigDecimal iscNursingServiceCost) {
        this.iscNursingServiceCost = iscNursingServiceCost;
    }

    public BigDecimal getIscFunctionalServiceCost() {
        return iscFunctionalServiceCost;
    }

    public void setIscFunctionalServiceCost(BigDecimal iscFunctionalServiceCost) {
        this.iscFunctionalServiceCost = iscFunctionalServiceCost;
    }

    public BigDecimal getIscOverheadsMedicine() {
        return iscOverheadsMedicine;
    }

    public void setIscOverheadsMedicine(BigDecimal iscOverheadsMedicine) {
        this.iscOverheadsMedicine = iscOverheadsMedicine;
    }

    public BigDecimal getIscOverheadMedicalGoods() {
        return iscOverheadMedicalGoods;
    }

    public void setIscOverheadMedicalGoods(BigDecimal iscOverheadMedicalGoods) {
        this.iscOverheadMedicalGoods = iscOverheadMedicalGoods;
    }

    public BigDecimal getIscMedicalInfrastructureCost() {
        return iscMedicalInfrastructureCost;
    }

    public void setIscMedicalInfrastructureCost(BigDecimal iscMedicalInfrastructureCost) {
        this.iscMedicalInfrastructureCost = iscMedicalInfrastructureCost;
    }

    public BigDecimal getIscNonMedicalInfrastructureCost() {
        return iscNonMedicalInfrastructureCost;
    }

    public void setIscNonMedicalInfrastructureCost(BigDecimal iscNonMedicalInfrastructureCost) {
        this.iscNonMedicalInfrastructureCost = iscNonMedicalInfrastructureCost;
    }

    public DrgCalcBasics getIscBaseInformationID() {
        return iscBaseInformationID;
    }

    public void setIscBaseInformationID(DrgCalcBasics iscBaseInformationID) {
        this.iscBaseInformationID = iscBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iscID != null ? iscID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListIntensiveStrokeCost)) {
            return false;
        }
        KGLListIntensiveStrokeCost other = (KGLListIntensiveStrokeCost) object;
        if ((this.iscID == null && other.iscID != null) || (this.iscID != null && !this.iscID.equals(other.iscID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListIntensiveStrokeCost[ iscID=" + iscID + " ]";
    }
    
}
