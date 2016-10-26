/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "StatementOfParticipance", schema = "calc")
public class StatementOfParticipance implements Serializable{

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sopId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DataYear">
    @Column(name = "sopDataYear")
    private int _dataYear;
    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IK">
    @Column(name = "TE_IK")
    private int _ik;
    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "sopAccountId")
    private int _accountId;
    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property StatusId">
    @Column(name = "sopStatusId")
    private int _statusId;
    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        _statusId = statusId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "sopLastChanged")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastChanged =  Calendar.getInstance().getTime();
    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        _lastChanged = lastChanged;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property IsDrgCalc">
    @Column(name = "sopIsDrg")
    private boolean _drgCalc;
    public boolean isDrgCalc() {
        return _drgCalc;
    }

    public void setDrgCalc(boolean drgCalc) {
        _drgCalc = drgCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsPsyCalc">
    @Column(name = "sopIsPsy")
    private boolean _psyCalc;
    public boolean isPsyCalc() {
        return _psyCalc;
    }

    public void setPsyCalc(boolean psyCalc) {
        _psyCalc = psyCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsInvCalc">
    @Column(name = "sopIsInv")
    private boolean _invCalc;
    public boolean isInvCalc() {
        return _invCalc;
    }

    public void setInvCalc(boolean invCalc) {
        _invCalc = invCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsTpgCalc">
    @Column(name = "sopIsTpg")
    private boolean _tpgCalc;
    public boolean isTpgCalc() {
        return _tpgCalc;
    }

    public void setTpgCalc(boolean tpgCalc) {
        _tpgCalc = tpgCalc;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ClinicalDistributionModelDrg">
    @Column(name = "sopCdmDrg")
    private boolean _clinicalDistributionModelDrg;
    public boolean isClinicalDistributionModelDrg() {
        return _clinicalDistributionModelDrg;
    }

    public void setClinicalDistributionModelDrg(boolean clinicalDistributionModelDrg) {
        _clinicalDistributionModelDrg = clinicalDistributionModelDrg;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ClinicalDistributionModelPsy">
    @Column(name = "sopCdmPsy")
    private boolean _clinicalDistributionModelPsy;
    public boolean isClinicalDistributionModelPsy() {
        return _clinicalDistributionModelPsy;
    }

    public void setClinicalDistributionModelPsy(boolean clinicalDistributionModelPsy) {
        _clinicalDistributionModelPsy = clinicalDistributionModelPsy;
    }
    // </editor-fold>

    // todo: implement other fields
    // sopMultiyearDrg, sopMultiyearDrgText, sopMultiyearPsy, sopMultiyearPsyText, sopSection, sopSectionExtern, sopSectionCount, sopDrgCare, sopPsyDataIntensive, sopIsWithConsultant, sopConsultantCompany, sopConsultantSendMail, sopConsultantSalutation, sopConsultantTitle, sopConsultantFirstName, sopConsultantLastName, sopConsultantPhone, sopConsultantMail
    
    // <editor-fold defaultstate="collapsed" desc="hashCode + equals">
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
        return _id == other._id;
    }
    // </editor-fold>

    @PrePersist
    @PreUpdate
    public void tagModifiedDate() {
        _lastChanged = Calendar.getInstance().getTime();
    }
    
    
}
