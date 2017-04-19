/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "CalcBasicsAutopsy", schema = "calc")
public class CalcBasicsAutopsy implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cbaId", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="dataYear">
    @Column(name = "cbaDataYear")
    @Documentation(key = "lblYearData")
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "cbaIK")
    @Documentation(key = "lblIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "cbaAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "cbaLastChanged")
    @Documentation(name = "Stand")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChanged;

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this._lastChanged = lastChanged;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accountIdLastChange">
    @Column(name = "cbaLastChangedBy")
    private int _accountIdLastChange;

    public int getAccountIdLastChange() {
        return _accountIdLastChange;
    }

    public void setAccountIdLastChange(int accountId) {
        this._accountIdLastChange = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sealed">
    @Column(name = "cbaSealed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _sealed = new Date (0,0,1);

    public Date getSealed() {
        return _sealed;
    }

    public void setSealed(Date sealed) {
        this._sealed = sealed;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property StatusId">
    @Column(name = "cbaStatusId")
    private int _statusId;

    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        this._statusId = statusId;
    }

    @Documentation(key = "lblWorkstate", rank = 10)
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    public void setStatus(WorkflowStatus status) {
        _statusId = status.getId();
    }
    //</editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="Property HasCostCenterForensic">
    @Column(name = "cbaHasCostCenterForensic")
    private boolean _hasCostCenterForensic;

    public boolean getHasCostCenterForensic() {
        return _hasCostCenterForensic;
    }

    public void setHasCostCenterForensic(boolean hasCostCenterForensic) {
        _hasCostCenterForensic = hasCostCenterForensic;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FullVigorMedicalBeforeAccrual">
    @Column(name = "cbaFullVigorMedicalBeforeAccrual")
    private double _fullVigorMedicalBeforeAccrual;

    public double getFullVigorMedicalBeforeAccrual() {
        return _fullVigorMedicalBeforeAccrual;
    }

    public void setFullVigorMedicalBeforeAccrual(double fullVigorMedicalBeforeAccrual) {
        _fullVigorMedicalBeforeAccrual = fullVigorMedicalBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FullVigorMedicalAfterAccrual">
    @Column(name = "cbaFullVigorMedicalAfterAccrual")
    private double _fullVigorMedicalAfterAccrual;

    public double getFullVigorMedicalAfterAccrual() {
        return _fullVigorMedicalAfterAccrual;
    }

    public void setFullVigorMedicalAfterAccrual(double fullVigorMedicalAfterAccrual) {
        _fullVigorMedicalAfterAccrual = fullVigorMedicalAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeMedicalBeforeAccrual">
    @Column(name = "cbaCostVolumeMedicalBeforeAccrual")
    private int _costVolumeMedicalBeforeAccrual;

    public int getCostVolumeMedicalBeforeAccrual() {
        return _costVolumeMedicalBeforeAccrual;
    }

    public void setCostVolumeMedicalBeforeAccrual(int costVolumeMedicalBeforeAccrual) {
        _costVolumeMedicalBeforeAccrual = costVolumeMedicalBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeMedicalAfterAccrual">
    @Column(name = "cbaCostVolumeMedicalAfterAccrual")
    private int _costVolumeMedicalAfterAccrual;

    public int getCostVolumeMedicalAfterAccrual() {
        return _costVolumeMedicalAfterAccrual;
    }

    public void setCostVolumeMedicalAfterAccrual(int costVolumeMedicalAfterAccrual) {
        _costVolumeMedicalAfterAccrual = costVolumeMedicalAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FullVigorOtherBeforeAccrual">
    @Column(name = "cbaFullVigorOtherBeforeAccrual")
    private double _fullVigorOtherBeforeAccrual;

    public double getFullVigorOtherBeforeAccrual() {
        return _fullVigorOtherBeforeAccrual;
    }

    public void setFullVigorOtherBeforeAccrual(double fullVigorOtherBeforeAccrual) {
        _fullVigorOtherBeforeAccrual = fullVigorOtherBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FullVigorOtherAfterAccrual">
    @Column(name = "cbaFullVigorOtherAfterAccrual")
    private double _fullVigorOtherAfterAccrual;

    public double getFullVigorOtherAfterAccrual() {
        return _fullVigorOtherAfterAccrual;
    }

    public void setFullVigorOtherAfterAccrual(double fullVigorOtherAfterAccrual) {
        _fullVigorOtherAfterAccrual = fullVigorOtherAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeOtherBeforeAccrual">
    @Column(name = "cbaCostVolumeOtherBeforeAccrual")
    private int _costVolumeOtherBeforeAccrual;

    public int getCostVolumeOtherBeforeAccrual() {
        return _costVolumeOtherBeforeAccrual;
    }

    public void setCostVolumeOtherBeforeAccrual(int costVolumeOtherBeforeAccrual) {
        _costVolumeOtherBeforeAccrual = costVolumeOtherBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeOtherAfterAccrual">
    @Column(name = "cbaCostVolumeOtherAfterAccrual")
    private int _costVolumeOtherAfterAccrual;

    public int getCostVolumeOtherAfterAccrual() {
        return _costVolumeOtherAfterAccrual;
    }

    public void setCostVolumeOtherAfterAccrual(int costVolumeOtherAfterAccrual) {
        _costVolumeOtherAfterAccrual = costVolumeOtherAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeMedicalInfraBeforeAccrual">
    @Column(name = "cbaCostVolumeMedicalInfraBeforeAccrual")
    private int _costVolumeMedicalInfraBeforeAccrual;

    public int getCostVolumeMedicalInfraBeforeAccrual() {
        return _costVolumeMedicalInfraBeforeAccrual;
    }

    public void setCostVolumeMedicalInfraBeforeAccrual(int costVolumeMedicalInfraBeforeAccrual) {
        _costVolumeMedicalInfraBeforeAccrual = costVolumeMedicalInfraBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeMedicalInfraAfterAccrual">
    @Column(name = "cbaCostVolumeMedicalInfraAfterAccrual")
    private int _costVolumeMedicalInfraAfterAccrual;

    public int getCostVolumeMedicalInfraAfterAccrual() {
        return _costVolumeMedicalInfraAfterAccrual;
    }

    public void setCostVolumeMedicalInfraAfterAccrual(int costVolumeMedicalInfraAfterAccrual) {
        _costVolumeMedicalInfraAfterAccrual = costVolumeMedicalInfraAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeNonMedicalInfraBeforeAccrual">
    @Column(name = "cbaCostVolumeNonMedicalInfraBeforeAccrual")
    private int _costVolumeNonMedicalInfraBeforeAccrual;

    public int getCostVolumeNonMedicalInfraBeforeAccrual() {
        return _costVolumeNonMedicalInfraBeforeAccrual;
    }

    public void setCostVolumeNonMedicalInfraBeforeAccrual(int costVolumeNonMedicalInfraBeforeAccrual) {
        _costVolumeNonMedicalInfraBeforeAccrual = costVolumeNonMedicalInfraBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeNonMedicalInfraAfterAccrual">
    @Column(name = "cbaCostVolumeNonMedicalInfraAfterAccrual")
    private int _costVolumeNonMedicalInfraAfterAccrual;

    public int getCostVolumeNonMedicalInfraAfterAccrual() {
        return _costVolumeNonMedicalInfraAfterAccrual;
    }

    public void setCostVolumeNonMedicalInfraAfterAccrual(int costVolumeNonMedicalInfraAfterAccrual) {
        _costVolumeNonMedicalInfraAfterAccrual = costVolumeNonMedicalInfraAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CalcAutopsys">
    @Column(name = "cbaCalcAutopsys")
    private int _calcAutopsys;

    public int getCalcAutopsys() {
        return _calcAutopsys;
    }

    public void setCalcAutopsys(int calcAutopsys) {
        _calcAutopsys = calcAutopsys;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeForensic">
    @Column(name = "cbaCostVolumeForensic")
    private int _costVolumeForensic;

    public int getCostVolumeForensic() {
        return _costVolumeForensic;
    }

    public void setCostVolumeForensic(int costVolumeForensic) {
        _costVolumeForensic = costVolumeForensic;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeForensicAccural">
    @Column(name = "cbaCostVolumeForensicAccural")
    private int _costVolumeForensicAccural;

    public int getCostVolumeForensicAccural() {
        return _costVolumeForensicAccural;
    }

    public void setCostVolumeForensicAccural(int costVolumeForensicAccural) {
        _costVolumeForensicAccural = costVolumeForensicAccural;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeLaboratoryBeforeAccrual">
    @Column(name = "cbaCostVolumeLaboratoryBeforeAccrual")
    private int _costVolumeLaboratoryBeforeAccrual;

    public int getCostVolumeLaboratoryBeforeAccrual() {
        return _costVolumeLaboratoryBeforeAccrual;
    }

    public void setCostVolumeLaboratoryBeforeAccrual(int costVolumeLaboratoryBeforeAccrual) {
        _costVolumeLaboratoryBeforeAccrual = costVolumeLaboratoryBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeLaboratoryAfterAccrual">
    @Column(name = "cbaCostVolumeLaboratoryAfterAccrual")
    private int _costVolumeLaboratoryAfterAccrual;

    public int getCostVolumeLaboratoryAfterAccrual() {
        return _costVolumeLaboratoryAfterAccrual;
    }

    public void setCostVolumeLaboratoryAfterAccrual(int costVolumeLaboratoryAfterAccrual) {
        _costVolumeLaboratoryAfterAccrual = costVolumeLaboratoryAfterAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CaseCountXRay">
    @Column(name = "cbaCaseCountXRay")
    private int _caseCountXRay;

    public int getCaseCountXRay() {
        return _caseCountXRay;
    }

    public void setCaseCountXRay(int caseCountXRay) {
        _caseCountXRay = caseCountXRay;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property cbaCostVolumeXRayBeforeAccrual">
    @Column(name = "cbaCostVolumeXRayBeforeAccrual")
    private int _costVolumeXRayBeforeAccrual;

    public int getCostVolumeXRayBeforeAccrual() {
        return _costVolumeXRayBeforeAccrual;
    }

    public void setCostVolumeXRayBeforeAccrual(int costVolumeXRayBeforeAccrual) {
        _costVolumeXRayBeforeAccrual = costVolumeXRayBeforeAccrual;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeXRayAfterAccrual">
    @Column(name = "cbaCostVolumeXRayAfterAccrual")
    private int _costVolumeXRayAfterAccrual;

    public int getCostVolumeXRayAfterAccrual() {
        return _costVolumeXRayAfterAccrual;
    }

    public void setCostVolumeXRayAfterAccrual(int costVolumeXRayAfterAccrual) {
        _costVolumeXRayAfterAccrual = costVolumeXRayAfterAccrual;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals, toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this._id;
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
        final CalcBasicsAutopsy other = (CalcBasicsAutopsy) obj;
        if (this._id != other._id) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "CalcBasicsAutopsy{" + "_id=" + _id + '}';
    }
    //</editor-fold>
    
}
