/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByID", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k._id = :ogID")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByCostCenterText", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k._costCenterText = :ogCostCenterText")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByMedicalServiceCnt", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k._medicalServiceCnt = :ogMedicalServiceCnt")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByAttendingDoctorCnt", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k._attendingDoctorCnt = :ogAttendingDoctorCnt")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByNursingServiceCnt", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k._nursingServiceCnt = :ogNursingServiceCnt")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByFunctionalServiceCnt", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k._functionalServiceCnt = :ogFunctionalServiceCnt")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByMidwifeCnt", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k._midwifeCnt = :ogMidwifeCnt")
    , @NamedQuery(name = "KGLListObstetricsGynecology.findByAttendingMidwifeCnt", query = "SELECT k FROM KGLListObstetricsGynecology k WHERE k._attendingMidwifeCnt = :ogAttendingMidwifeCnt")})
public class KGLListObstetricsGynecology implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogID")
    private Integer _id;
    
    public Integer getID() {
        return _id;
    }

    public void setID(Integer id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ogCostCenterText">
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "ogCostCenterText")
    private String _costCenterText = "";
    
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ogMedicalServiceCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogMedicalServiceCnt")
    private BigDecimal _medicalServiceCnt;
    
    public BigDecimal getMedicalServiceCnt() {
        return _medicalServiceCnt;
    }

    public void setMedicalServiceCnt(BigDecimal medicalServiceCnt) {
        this._medicalServiceCnt = medicalServiceCnt;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ogAttendingDoctorCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogAttendingDoctorCnt")
    private BigDecimal _attendingDoctorCnt;
    
    public BigDecimal getAttendingDoctorCnt() {
        return _attendingDoctorCnt;
    }

    public void setAttendingDoctorCnt(BigDecimal attendingDoctorCnt) {
        this._attendingDoctorCnt = attendingDoctorCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ogNursingServiceCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogNursingServiceCnt")
    private BigDecimal _nursingServiceCnt;
    
    public BigDecimal getNursingServiceCnt() {
        return _nursingServiceCnt;
    }

    public void setNursingServiceCnt(BigDecimal nursingServiceCnt) {
        this._nursingServiceCnt = nursingServiceCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ogFunctionalServiceCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogFunctionalServiceCnt")
    private BigDecimal _functionalServiceCnt;
    
    public BigDecimal getFunctionalServiceCnt() {
        return _functionalServiceCnt;
    }

    public void setFunctionalServiceCnt(BigDecimal functionalServiceCnt) {
        this._functionalServiceCnt = functionalServiceCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ogMidwifeCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogMidwifeCnt")
    private BigDecimal _midwifeCnt;
    
    public BigDecimal getMidwifeCnt() {
        return _midwifeCnt;
    }

    public void setMidwifeCnt(BigDecimal midwifeCnt) {
        this._midwifeCnt = midwifeCnt;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ogAttendingMidwifeCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogAttendingMidwifeCnt")
    private BigDecimal _attendingMidwifeCnt;
    
    public BigDecimal getAttendingMidwifeCnt() {
        return this._attendingMidwifeCnt;        
    }
    
    public void setAttendingMidwifeCnt(BigDecimal attendingMidwifeCnt) {
        this._attendingMidwifeCnt = attendingMidwifeCnt;        
    }
    // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="ogBaseInformationID">
//    @JoinColumn(name = "ogBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogBaseInformationID")
    private int _baseInformationID;
    
    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    // </editor-fold>
    
    public KGLListObstetricsGynecology() {
    }

    public KGLListObstetricsGynecology(Integer ogID) {
        this._id = ogID;
    }

    public KGLListObstetricsGynecology(Integer ogID, String ogCostCenterText, BigDecimal ogMedicalServiceCnt, BigDecimal ogAttendingDoctorCnt, BigDecimal ogNursingServiceCnt, BigDecimal ogFunctionalServiceCnt, BigDecimal ogMidwifeCnt, BigDecimal ogAttendingMidwifeCnt) {
        this._id = ogID;
        this._costCenterText = ogCostCenterText;
        this._medicalServiceCnt = ogMedicalServiceCnt;
        this._attendingDoctorCnt = ogAttendingDoctorCnt;
        this._nursingServiceCnt = ogNursingServiceCnt;
        this._functionalServiceCnt = ogFunctionalServiceCnt;
        this._midwifeCnt = ogMidwifeCnt;
        this._attendingMidwifeCnt = ogAttendingMidwifeCnt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListObstetricsGynecology)) {
            return false;
        }
        KGLListObstetricsGynecology other = (KGLListObstetricsGynecology) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }
 
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListObstetricsGynecology[ ogID=" + _id + " ]";
    }
    
}
