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
@Table(name = "KGLListObstetricsGynecology", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListObstetricsGynecology.findAll", query = "SELECT k FROM KGLListObstetricsGynecology k")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByOgID", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k.ogID = :ogID")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByOgCostCenterText", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k.ogCostCenterText = :ogCostCenterText")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByOgMedicalServiceCnt", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k.ogMedicalServiceCnt = :ogMedicalServiceCnt")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByOgAttendingDoctorCnt", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k.ogAttendingDoctorCnt = :ogAttendingDoctorCnt")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByOgNursingServiceCnt", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k.ogNursingServiceCnt = :ogNursingServiceCnt")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByOgFunctionalServiceCnt", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k.ogFunctionalServiceCnt = :ogFunctionalServiceCnt")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByOgMidwifeCnt", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k.ogMidwifeCnt = :ogMidwifeCnt")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByOgAttendingMidwifeCnt", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k.ogAttendingMidwifeCnt = :ogAttendingMidwifeCnt")})
public class KGLListObstetricsGynecology implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogID")
    private Integer ogID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ogCostCenterText")
    private String ogCostCenterText;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogMedicalServiceCnt")
    private BigDecimal ogMedicalServiceCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogAttendingDoctorCnt")
    private BigDecimal ogAttendingDoctorCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogNursingServiceCnt")
    private BigDecimal ogNursingServiceCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogFunctionalServiceCnt")
    private BigDecimal ogFunctionalServiceCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogMidwifeCnt")
    private BigDecimal ogMidwifeCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogAttendingMidwifeCnt")
    private BigDecimal ogAttendingMidwifeCnt;
    @JoinColumn(name = "ogBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics ogBaseInformationID;

    public KGLListObstetricsGynecology() {
    }

    public KGLListObstetricsGynecology(Integer ogID) {
        this.ogID = ogID;
    }

    public KGLListObstetricsGynecology(Integer ogID, String ogCostCenterText, BigDecimal ogMedicalServiceCnt, BigDecimal ogAttendingDoctorCnt, BigDecimal ogNursingServiceCnt, BigDecimal ogFunctionalServiceCnt, BigDecimal ogMidwifeCnt, BigDecimal ogAttendingMidwifeCnt) {
        this.ogID = ogID;
        this.ogCostCenterText = ogCostCenterText;
        this.ogMedicalServiceCnt = ogMedicalServiceCnt;
        this.ogAttendingDoctorCnt = ogAttendingDoctorCnt;
        this.ogNursingServiceCnt = ogNursingServiceCnt;
        this.ogFunctionalServiceCnt = ogFunctionalServiceCnt;
        this.ogMidwifeCnt = ogMidwifeCnt;
        this.ogAttendingMidwifeCnt = ogAttendingMidwifeCnt;
    }

    public Integer getOgID() {
        return ogID;
    }

    public void setOgID(Integer ogID) {
        this.ogID = ogID;
    }

    public String getOgCostCenterText() {
        return ogCostCenterText;
    }

    public void setOgCostCenterText(String ogCostCenterText) {
        this.ogCostCenterText = ogCostCenterText;
    }

    public BigDecimal getOgMedicalServiceCnt() {
        return ogMedicalServiceCnt;
    }

    public void setOgMedicalServiceCnt(BigDecimal ogMedicalServiceCnt) {
        this.ogMedicalServiceCnt = ogMedicalServiceCnt;
    }

    public BigDecimal getOgAttendingDoctorCnt() {
        return ogAttendingDoctorCnt;
    }

    public void setOgAttendingDoctorCnt(BigDecimal ogAttendingDoctorCnt) {
        this.ogAttendingDoctorCnt = ogAttendingDoctorCnt;
    }

    public BigDecimal getOgNursingServiceCnt() {
        return ogNursingServiceCnt;
    }

    public void setOgNursingServiceCnt(BigDecimal ogNursingServiceCnt) {
        this.ogNursingServiceCnt = ogNursingServiceCnt;
    }

    public BigDecimal getOgFunctionalServiceCnt() {
        return ogFunctionalServiceCnt;
    }

    public void setOgFunctionalServiceCnt(BigDecimal ogFunctionalServiceCnt) {
        this.ogFunctionalServiceCnt = ogFunctionalServiceCnt;
    }

    public BigDecimal getOgMidwifeCnt() {
        return ogMidwifeCnt;
    }

    public void setOgMidwifeCnt(BigDecimal ogMidwifeCnt) {
        this.ogMidwifeCnt = ogMidwifeCnt;
    }

    public BigDecimal getOgAttendingMidwifeCnt() {
        return ogAttendingMidwifeCnt;
    }

    public void setOgAttendingMidwifeCnt(BigDecimal ogAttendingMidwifeCnt) {
        this.ogAttendingMidwifeCnt = ogAttendingMidwifeCnt;
    }

    public DrgCalcBasics getOgBaseInformationID() {
        return ogBaseInformationID;
    }

    public void setOgBaseInformationID(DrgCalcBasics ogBaseInformationID) {
        this.ogBaseInformationID = ogBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ogID != null ? ogID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListObstetricsGynecology)) {
            return false;
        }
        KGLListObstetricsGynecology other = (KGLListObstetricsGynecology) object;
        if ((this.ogID == null && other.ogID != null) || (this.ogID != null && !this.ogID.equals(other.ogID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListObstetricsGynecology[ ogID=" + ogID + " ]";
    }
    
}
