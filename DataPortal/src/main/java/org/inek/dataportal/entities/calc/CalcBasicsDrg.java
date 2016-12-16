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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.inek.dataportal.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "BasicsDrg", schema = "calc")
public class CalcBasicsDrg implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public CalcBasicsDrg() {
        _kglBaseInformation = new KGLBaseInformation();
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bdId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DataYear">
    @Column(name = "bdDataYear")
    private int _dataYear;
    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IK">
    @Column(name = "bdIK")
    private int _ik;
    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Hospital Name">
    @Column(name = "bdHospitalName")
    private String _hospitalName;

    public String getHospitalName() {
        return _hospitalName;
    }

    public void setHospitalName(String _hospitalName) {
        this._hospitalName = _hospitalName;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Delivery Type">
    @Column(name = "bdDeliveryType")
    private byte _deliveryType;

    public byte getDeliveryType() {
        return _deliveryType;
    }

    public void setDeliveryType(byte _deliveryType) {
        this._deliveryType = _deliveryType;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Correction Note">
    @Column(name = "bdCorrectionNote")
    private String _correctionNote;

    public String getCorrectionNote() {
        return _correctionNote;
    }

    public void setCorrectionNote(String _correctionNote) {
        this._correctionNote = _correctionNote;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "bdAccountId")
    private int _accountId;
    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property StatusId / Status">
    @Column(name = "bdStatusId")
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
    
    // <editor-fold defaultstate="collapsed" desc="AdditionalInformationDrg">
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "aiId", referencedColumnName = "bdId")
    private List<AdditionalInformationDrg> _additionalInformationDrg;

    public List<AdditionalInformationDrg> getAdditionalInformationDrg() {
        return _additionalInformationDrg;
    }

    public void setAdditionalInformationDrg(List<AdditionalInformationDrg> _additionalInformationDrg) {
        this._additionalInformationDrg = _additionalInformationDrg;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="KGLBaseInformation">
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "biBasicsDrgId", referencedColumnName = "bdId")
    private KGLBaseInformation _kglBaseInformation;

    public KGLBaseInformation getKglBaseInformation() {
        return _kglBaseInformation;
    }

    public void setKglBaseInformation(KGLBaseInformation _kglBaseInformation) {
        this._kglBaseInformation = _kglBaseInformation;
    }
    
    // </editor-fold>
    
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
        final StatementOfParticipance other = (StatementOfParticipance) obj;
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
