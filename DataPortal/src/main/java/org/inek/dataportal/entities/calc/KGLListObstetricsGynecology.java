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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListObstetricsGynecology", schema = "calc")
@XmlRootElement
public class KGLListObstetricsGynecology implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogID")
    private int _id;
    
    public int getID() {
        return _id;
    }

    public void setID(int id) {
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
    private double _medicalServiceCnt;
    
    public double getMedicalServiceCnt() {
        return _medicalServiceCnt;
    }

    public void setMedicalServiceCnt(double medicalServiceCnt) {
        this._medicalServiceCnt = medicalServiceCnt;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ogAttendingDoctorCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogAttendingDoctorCnt")
    private double _attendingDoctorCnt;
    
    public double getAttendingDoctorCnt() {
        return _attendingDoctorCnt;
    }

    public void setAttendingDoctorCnt(double attendingDoctorCnt) {
        this._attendingDoctorCnt = attendingDoctorCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ogNursingServiceCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogNursingServiceCnt")
    private double _nursingServiceCnt;
    
    public double getNursingServiceCnt() {
        return _nursingServiceCnt;
    }

    public void setNursingServiceCnt(double nursingServiceCnt) {
        this._nursingServiceCnt = nursingServiceCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ogFunctionalServiceCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogFunctionalServiceCnt")
    private double _functionalServiceCnt;
    
    public double getFunctionalServiceCnt() {
        return _functionalServiceCnt;
    }

    public void setFunctionalServiceCnt(double functionalServiceCnt) {
        this._functionalServiceCnt = functionalServiceCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ogMidwifeCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogMidwifeCnt")
    private double _midwifeCnt;
    
    public double getMidwifeCnt() {
        return _midwifeCnt;
    }

    public void setMidwifeCnt(double midwifeCnt) {
        this._midwifeCnt = midwifeCnt;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ogAttendingMidwifeCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ogAttendingMidwifeCnt")
    private double _attendingMidwifeCnt;
    
    public double getAttendingMidwifeCnt() {
        return this._attendingMidwifeCnt;        
    }
    
    public void setAttendingMidwifeCnt(double attendingMidwifeCnt) {
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

    public KGLListObstetricsGynecology(int ogID) {
        this._id = ogID;
    }

    public KGLListObstetricsGynecology(int ogID, String ogCostCenterText, double ogMedicalServiceCnt, double ogAttendingDoctorCnt, double ogNursingServiceCnt, double ogFunctionalServiceCnt, double ogMidwifeCnt, double ogAttendingMidwifeCnt) {
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
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListObstetricsGynecology)) {
            return false;
        }
        KGLListObstetricsGynecology other = (KGLListObstetricsGynecology) object;
        return (this._id == other._id);
    }
 
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListObstetricsGynecology[ ogID=" + _id + " ]";
    }
    
}
