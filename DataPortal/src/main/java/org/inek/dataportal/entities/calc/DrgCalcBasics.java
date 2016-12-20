/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.inek.dataportal.enums.WorkflowStatus;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "KGLBaseInformation", schema = "calc")
public class DrgCalcBasics implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "biID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property DataYear">
    @Column(name = "biDataYear")
    private int _dataYear;
    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property IK">
    @Column(name = "biIK")
    private int _ik;
    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Hospital Name">
    @Column(name = "biHospitalName")
    private String _hospitalName;

    public String getHospitalName() {
        return _hospitalName;
    }

    public void setHospitalName(String _hospitalName) {
        this._hospitalName = _hospitalName;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Delivery Type">
    @Column(name = "biDeliveryType")
    private byte _deliveryType;

    public byte getDeliveryType() {
        return _deliveryType;
    }

    public void setDeliveryType(byte _deliveryType) {
        this._deliveryType = _deliveryType;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Correction Note">
    @Column(name = "biCorrectionNote")
    private String _correctionNote;

    public String getCorrectionNote() {
        return _correctionNote;
    }

    public void setCorrectionNote(String _correctionNote) {
        this._correctionNote = _correctionNote;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "biAccountId")
    private int _accountId;
    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property StatusId / Status">
    @Column(name = "biStatusId")
    private int _statusId;
    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        _statusId = statusId;
    }
    
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    public void setStatus(WorkflowStatus status) {
        _statusId = status.getValue();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "bdLastChanged")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastChanged =  Calendar.getInstance().getTime();
    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        _lastChanged = lastChanged;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property SumCalcCost">
    @Column(name = "biSumCalcCost")
    private float _sumCalcCost;

    public float getSumCalcCost() {
        return _sumCalcCost;
    }

    public void setSumCalcCost(float _sumCalcCost) {
        this._sumCalcCost = _sumCalcCost;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CntCaseInStation">
    @Column(name = "biCntCaseInStation")
    private int _cntCaseInStation;

    public int getCntCaseInStation() {
        return _cntCaseInStation;
    }

    public void setCntCaseInStation(int _cntCaseInStation) {
        this._cntCaseInStation = _cntCaseInStation;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CntCasePartialStation">
    @Column(name = "biCntCasePartialStation")
    private int _cntCasePartialStation;

    public int getCntCasePartialStation() {
        return _cntCasePartialStation;
    }

    public void setCntCasePartialStation(int _cntCasePartialStation) {
        this._cntCasePartialStation = _cntCasePartialStation;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property DaysPartialStation">
    @Column(name = "biDaysPartialStation")
    private int _daysPartialStation;

    public int getDaysPartialStation() {
        return _daysPartialStation;
    }

    public void setDaysPartialStation(int _daysPartialStation) {
        this._daysPartialStation = _daysPartialStation;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property PatientEscort">
    @Column(name = "biPatientEscort")
    private int _patientEscort;

    public int getPatientEscort() {
        return _patientEscort;
    }

    public void setPatientEscort(int _patientEscort) {
        this._patientEscort = _patientEscort;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property PreStation">
    @Column(name = "biPreStation")
    private int _preStation = -1;
    
    public int getPreStation() {
        return _preStation;
    }

    public void setPreStation(int _preStation) {
        this._preStation = _preStation;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Beds">
    @Column(name = "biBeds")
    private int _beds = -1;
    
    public int getBeds() {
        return _beds;
    }

    public void setBeds(int _beds) {
        this._beds = _beds;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CntPartial">
    @Column(name = "biCntPartial")
    private int _cntPartial = -1;
    
    public int getCntPartial() {
        return _cntPartial;
    }

    public void setCntPartial(int _cntPartial) {
        this._cntPartial = _cntPartial;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CntLocation">
    @Column(name = "biCntLocation")
    private int _cntLocation = -1;
    
    public int getCntLocation() {
        return _cntLocation;
    }

    public void setCntLocation(int _cntLocation) {
        this._cntLocation = _cntLocation;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property DifLocationSupply">
    @Column(name = "biDifLocationSupply")
    private boolean _difLocationSupply;
    
    public boolean getDifLocationSupply() {
        return _difLocationSupply;
    }

    public void setDifLocationSupply(boolean _difLocationSupply) {
        this._difLocationSupply = _difLocationSupply;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property SpecialUnit">
    @Column(name = "biSpecialUnit")
    private boolean _specialUnit;
    
    public boolean getSpecialUnit() {
        return _specialUnit;
    }

    public void setSpecialUnit(boolean _specialUnit) {
        this._specialUnit = _specialUnit;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CentralFocus">
    @Column(name = "biCentralFocus")
    private boolean _centralFocus;
    
    public boolean getCentralFocus() {
        return _centralFocus;
    }

    public void setCentralFocus(boolean _centralFocus) {
        this._centralFocus = _centralFocus;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property NeonatLvl">
    @Column(name = "biNeonatLvl")
    private int _neonatLvl;
    
    public int getNeonatLvl() {
        return _neonatLvl;
    }

    public void setNeonatLvl(int _neonatLvl) {
        this._neonatLvl = _neonatLvl;
    }
    // </editor-fold>
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dfBaseInformationId", referencedColumnName = "bdId")
    private List<DrgDelimitationFact> _delimitationFacts = new Vector<>();

    public List<DrgDelimitationFact> getDelimitationFacts() {
        return _delimitationFacts;
    }

    public void setDelimitationFacts(List<DrgDelimitationFact> _delimitationFacts) {
        this._delimitationFacts = _delimitationFacts;
    }
    
    // <editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this._id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DrgCalcBasics other = (DrgCalcBasics) obj;
        return _id == other.getId();
    }
    
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.BasicsDrg[ id=" + _id + " ]";
    }
    
    // </editor-fold>
    
    @PrePersist
    @PreUpdate
    public void tagModifiedDate() {
        _lastChanged = Calendar.getInstance().getTime();
    }

}
