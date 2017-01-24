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
@Table(name = "KGPListStationServiceCost", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListStationServiceCost.findAll", query = "SELECT k FROM KGPListStationServiceCost k")})
public class KGPListStationServiceCost implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscID")
    private Integer sscID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscCostCenterID")
    private int sscCostCenterID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscCostCenterNumber")
    private int sscCostCenterNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "sscStation")
    private String sscStation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscBedCnt")
    private int sscBedCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscReceivingStation")
    private boolean sscReceivingStation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscRegularCareDays")
    private int sscRegularCareDays;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscRegularWeight")
    private int sscRegularWeight;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscIntensiveCareDays")
    private int sscIntensiveCareDays;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscIntensiveWeight")
    private int sscIntensiveWeight;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscMedicalServiceCnt")
    private BigDecimal sscMedicalServiceCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscNursingServiceCnt")
    private BigDecimal sscNursingServiceCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscPsychologistCnt")
    private BigDecimal sscPsychologistCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscSocialWorkerCnt")
    private BigDecimal sscSocialWorkerCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscSpecialTherapistCnt")
    private BigDecimal sscSpecialTherapistCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscFunctionalServiceCnt")
    private BigDecimal sscFunctionalServiceCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscMedicalServiceAmount")
    private int sscMedicalServiceAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscNursingServiceAmount")
    private int sscNursingServiceAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscPsychologistAmount")
    private int sscPsychologistAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscSocialWorkerAmount")
    private int sscSocialWorkerAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscSpecialTherapistAmount")
    private int sscSpecialTherapistAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscFunctionalServiceAmount")
    private int sscFunctionalServiceAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscMedicalInfrastructureAmount")
    private int sscMedicalInfrastructureAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sscNonMedicalInfrastructureAmount")
    private int sscNonMedicalInfrastructureAmount;
    @JoinColumn(name = "sscBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private PeppCalcBasics sscBaseInformationID;
    @JoinColumn(name = "sscMappingPsyPV", referencedColumnName = "pptID")
    @ManyToOne(optional = false)
    private KGPListPsyPVTypes sscMappingPsyPV;

    public KGPListStationServiceCost() {
    }

    public KGPListStationServiceCost(Integer sscID) {
        this.sscID = sscID;
    }

    public KGPListStationServiceCost(Integer sscID, int sscCostCenterID, int sscCostCenterNumber, String sscStation, int sscBedCnt, boolean sscReceivingStation, int sscRegularCareDays, int sscRegularWeight, int sscIntensiveCareDays, int sscIntensiveWeight, BigDecimal sscMedicalServiceCnt, BigDecimal sscNursingServiceCnt, BigDecimal sscPsychologistCnt, BigDecimal sscSocialWorkerCnt, BigDecimal sscSpecialTherapistCnt, BigDecimal sscFunctionalServiceCnt, int sscMedicalServiceAmount, int sscNursingServiceAmount, int sscPsychologistAmount, int sscSocialWorkerAmount, int sscSpecialTherapistAmount, int sscFunctionalServiceAmount, int sscMedicalInfrastructureAmount, int sscNonMedicalInfrastructureAmount) {
        this.sscID = sscID;
        this.sscCostCenterID = sscCostCenterID;
        this.sscCostCenterNumber = sscCostCenterNumber;
        this.sscStation = sscStation;
        this.sscBedCnt = sscBedCnt;
        this.sscReceivingStation = sscReceivingStation;
        this.sscRegularCareDays = sscRegularCareDays;
        this.sscRegularWeight = sscRegularWeight;
        this.sscIntensiveCareDays = sscIntensiveCareDays;
        this.sscIntensiveWeight = sscIntensiveWeight;
        this.sscMedicalServiceCnt = sscMedicalServiceCnt;
        this.sscNursingServiceCnt = sscNursingServiceCnt;
        this.sscPsychologistCnt = sscPsychologistCnt;
        this.sscSocialWorkerCnt = sscSocialWorkerCnt;
        this.sscSpecialTherapistCnt = sscSpecialTherapistCnt;
        this.sscFunctionalServiceCnt = sscFunctionalServiceCnt;
        this.sscMedicalServiceAmount = sscMedicalServiceAmount;
        this.sscNursingServiceAmount = sscNursingServiceAmount;
        this.sscPsychologistAmount = sscPsychologistAmount;
        this.sscSocialWorkerAmount = sscSocialWorkerAmount;
        this.sscSpecialTherapistAmount = sscSpecialTherapistAmount;
        this.sscFunctionalServiceAmount = sscFunctionalServiceAmount;
        this.sscMedicalInfrastructureAmount = sscMedicalInfrastructureAmount;
        this.sscNonMedicalInfrastructureAmount = sscNonMedicalInfrastructureAmount;
    }

    public Integer getSscID() {
        return sscID;
    }

    public void setSscID(Integer sscID) {
        this.sscID = sscID;
    }

    public int getSscCostCenterID() {
        return sscCostCenterID;
    }

    public void setSscCostCenterID(int sscCostCenterID) {
        this.sscCostCenterID = sscCostCenterID;
    }

    public int getSscCostCenterNumber() {
        return sscCostCenterNumber;
    }

    public void setSscCostCenterNumber(int sscCostCenterNumber) {
        this.sscCostCenterNumber = sscCostCenterNumber;
    }

    public String getSscStation() {
        return sscStation;
    }

    public void setSscStation(String sscStation) {
        this.sscStation = sscStation;
    }

    public int getSscBedCnt() {
        return sscBedCnt;
    }

    public void setSscBedCnt(int sscBedCnt) {
        this.sscBedCnt = sscBedCnt;
    }

    public boolean getSscReceivingStation() {
        return sscReceivingStation;
    }

    public void setSscReceivingStation(boolean sscReceivingStation) {
        this.sscReceivingStation = sscReceivingStation;
    }

    public int getSscRegularCareDays() {
        return sscRegularCareDays;
    }

    public void setSscRegularCareDays(int sscRegularCareDays) {
        this.sscRegularCareDays = sscRegularCareDays;
    }

    public int getSscRegularWeight() {
        return sscRegularWeight;
    }

    public void setSscRegularWeight(int sscRegularWeight) {
        this.sscRegularWeight = sscRegularWeight;
    }

    public int getSscIntensiveCareDays() {
        return sscIntensiveCareDays;
    }

    public void setSscIntensiveCareDays(int sscIntensiveCareDays) {
        this.sscIntensiveCareDays = sscIntensiveCareDays;
    }

    public int getSscIntensiveWeight() {
        return sscIntensiveWeight;
    }

    public void setSscIntensiveWeight(int sscIntensiveWeight) {
        this.sscIntensiveWeight = sscIntensiveWeight;
    }

    public BigDecimal getSscMedicalServiceCnt() {
        return sscMedicalServiceCnt;
    }

    public void setSscMedicalServiceCnt(BigDecimal sscMedicalServiceCnt) {
        this.sscMedicalServiceCnt = sscMedicalServiceCnt;
    }

    public BigDecimal getSscNursingServiceCnt() {
        return sscNursingServiceCnt;
    }

    public void setSscNursingServiceCnt(BigDecimal sscNursingServiceCnt) {
        this.sscNursingServiceCnt = sscNursingServiceCnt;
    }

    public BigDecimal getSscPsychologistCnt() {
        return sscPsychologistCnt;
    }

    public void setSscPsychologistCnt(BigDecimal sscPsychologistCnt) {
        this.sscPsychologistCnt = sscPsychologistCnt;
    }

    public BigDecimal getSscSocialWorkerCnt() {
        return sscSocialWorkerCnt;
    }

    public void setSscSocialWorkerCnt(BigDecimal sscSocialWorkerCnt) {
        this.sscSocialWorkerCnt = sscSocialWorkerCnt;
    }

    public BigDecimal getSscSpecialTherapistCnt() {
        return sscSpecialTherapistCnt;
    }

    public void setSscSpecialTherapistCnt(BigDecimal sscSpecialTherapistCnt) {
        this.sscSpecialTherapistCnt = sscSpecialTherapistCnt;
    }

    public BigDecimal getSscFunctionalServiceCnt() {
        return sscFunctionalServiceCnt;
    }

    public void setSscFunctionalServiceCnt(BigDecimal sscFunctionalServiceCnt) {
        this.sscFunctionalServiceCnt = sscFunctionalServiceCnt;
    }

    public int getSscMedicalServiceAmount() {
        return sscMedicalServiceAmount;
    }

    public void setSscMedicalServiceAmount(int sscMedicalServiceAmount) {
        this.sscMedicalServiceAmount = sscMedicalServiceAmount;
    }

    public int getSscNursingServiceAmount() {
        return sscNursingServiceAmount;
    }

    public void setSscNursingServiceAmount(int sscNursingServiceAmount) {
        this.sscNursingServiceAmount = sscNursingServiceAmount;
    }

    public int getSscPsychologistAmount() {
        return sscPsychologistAmount;
    }

    public void setSscPsychologistAmount(int sscPsychologistAmount) {
        this.sscPsychologistAmount = sscPsychologistAmount;
    }

    public int getSscSocialWorkerAmount() {
        return sscSocialWorkerAmount;
    }

    public void setSscSocialWorkerAmount(int sscSocialWorkerAmount) {
        this.sscSocialWorkerAmount = sscSocialWorkerAmount;
    }

    public int getSscSpecialTherapistAmount() {
        return sscSpecialTherapistAmount;
    }

    public void setSscSpecialTherapistAmount(int sscSpecialTherapistAmount) {
        this.sscSpecialTherapistAmount = sscSpecialTherapistAmount;
    }

    public int getSscFunctionalServiceAmount() {
        return sscFunctionalServiceAmount;
    }

    public void setSscFunctionalServiceAmount(int sscFunctionalServiceAmount) {
        this.sscFunctionalServiceAmount = sscFunctionalServiceAmount;
    }

    public int getSscMedicalInfrastructureAmount() {
        return sscMedicalInfrastructureAmount;
    }

    public void setSscMedicalInfrastructureAmount(int sscMedicalInfrastructureAmount) {
        this.sscMedicalInfrastructureAmount = sscMedicalInfrastructureAmount;
    }

    public int getSscNonMedicalInfrastructureAmount() {
        return sscNonMedicalInfrastructureAmount;
    }

    public void setSscNonMedicalInfrastructureAmount(int sscNonMedicalInfrastructureAmount) {
        this.sscNonMedicalInfrastructureAmount = sscNonMedicalInfrastructureAmount;
    }

    public PeppCalcBasics getSscBaseInformationID() {
        return sscBaseInformationID;
    }

    public void setSscBaseInformationID(PeppCalcBasics sscBaseInformationID) {
        this.sscBaseInformationID = sscBaseInformationID;
    }

    public KGPListPsyPVTypes getSscMappingPsyPV() {
        return sscMappingPsyPV;
    }

    public void setSscMappingPsyPV(KGPListPsyPVTypes sscMappingPsyPV) {
        this.sscMappingPsyPV = sscMappingPsyPV;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sscID != null ? sscID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGPListStationServiceCost)) {
            return false;
        }
        KGPListStationServiceCost other = (KGPListStationServiceCost) object;
        if ((this.sscID == null && other.sscID != null) || (this.sscID != null && !this.sscID.equals(other.sscID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListStationServiceCost[ sscID=" + sscID + " ]";
    }
    
}
