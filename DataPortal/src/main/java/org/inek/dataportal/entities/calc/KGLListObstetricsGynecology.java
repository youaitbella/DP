/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ogID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ogCostCenterText">
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
    @Column(name = "ogAttendingMidwifeCnt")
    private double _attendingMidwifeCnt;

    public double getAttendingMidwifeCnt() {
        return this._attendingMidwifeCnt;
    }

    public void setAttendingMidwifeCnt(double attendingMidwifeCnt) {
        this._attendingMidwifeCnt = attendingMidwifeCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ogCostTypeId">
    @Column(name = "ogCostTypeID")
    private double _costTypeId;

    public double getCostTypeId() {
        return _costTypeId;
    }

    public void setCostTypeId(double costTypeId) {
        this._costTypeId = costTypeId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ogBaseInformationId">
//    @JoinColumn(name = "ogBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "ogBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
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

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this._id;

        if (this._id != -1) {
            return hash;
        }

        hash = 19 * hash + Objects.hashCode(this._costCenterText);
        hash = 19 * hash + (int) (Double.doubleToLongBits(this._medicalServiceCnt) ^ (Double.doubleToLongBits(this._medicalServiceCnt) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(this._attendingDoctorCnt) ^ (Double.doubleToLongBits(this._attendingDoctorCnt) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(this._nursingServiceCnt) ^ (Double.doubleToLongBits(this._nursingServiceCnt) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(this._functionalServiceCnt) ^ (Double.doubleToLongBits(this._functionalServiceCnt) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(this._midwifeCnt) ^ (Double.doubleToLongBits(this._midwifeCnt) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(this._attendingMidwifeCnt) ^ (Double.doubleToLongBits(this._attendingMidwifeCnt) >>> 32));
        hash = 19 * hash + this._baseInformationId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KGLListObstetricsGynecology other = (KGLListObstetricsGynecology) obj;

        if (this._id != -1 && this._id == other._id) {
            return true;
        }

        if (this._id != other._id) {
            return false;
        }
        if (Double.doubleToLongBits(this._medicalServiceCnt) != Double.doubleToLongBits(other._medicalServiceCnt)) {
            return false;
        }
        if (Double.doubleToLongBits(this._attendingDoctorCnt) != Double.doubleToLongBits(other._attendingDoctorCnt)) {
            return false;
        }
        if (Double.doubleToLongBits(this._nursingServiceCnt) != Double.doubleToLongBits(other._nursingServiceCnt)) {
            return false;
        }
        if (Double.doubleToLongBits(this._functionalServiceCnt) != Double.doubleToLongBits(other._functionalServiceCnt)) {
            return false;
        }
        if (Double.doubleToLongBits(this._midwifeCnt) != Double.doubleToLongBits(other._midwifeCnt)) {
            return false;
        }
        if (Double.doubleToLongBits(this._attendingMidwifeCnt) != Double.doubleToLongBits(other._attendingMidwifeCnt)) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        return Objects.equals(this._costCenterText, other._costCenterText);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListObstetricsGynecology[ ogID=" + _id + " ]";
    }
    //</editor-fold>

}
