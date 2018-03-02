/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import org.inek.dataportal.enums.CalcInfoType;
import org.inek.dataportal.common.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
@Entity @IdClass(CalcHospitalInfoId.class)
public class CalcHospitalInfo implements Serializable {
        // todo: include hospital name and city into CalcHospitalInfo to avoid extra data access

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @Column(name = "Id")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Type">
    @Id
    @Column(name = "Type")
    @Enumerated(EnumType.STRING)
    private CalcInfoType _type;

    public CalcInfoType getType() {
        return _type;
    }

    public void setType(CalcInfoType type) {
        _type = type;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "AccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DataYear">
    @Column(name = "DataYear")
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        _dataYear = dataYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IK">
    @Column(name = "IK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property StatusId / Status">
    @Column(name = "StatusId")
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
        _statusId = status.getId();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "Name")
    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "LastChanged")
    private Date _lastChanged;

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        _lastChanged = lastChanged;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Hash & Equals">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }

        hash = 83 * hash + _type.hashCode();
        hash = 83 * hash + this._accountId;
        hash = 83 * hash + this._dataYear;
        hash = 83 * hash + this._ik;
        hash = 83 * hash + this._statusId;
        hash = 83 * hash + Objects.hashCode(this._name);
        return hash;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof CalcHospitalInfo)) {
            return false;
        }
        final CalcHospitalInfo other = (CalcHospitalInfo) obj;

        if (this._id != -1 && this._id == other._id) {
            return true;
        }

        if (this._id != other._id) {
            return false;
        }
        if (this._type != other._type) {
            return false;
        }
        if (this._accountId != other._accountId) {
            return false;
        }
        if (this._dataYear != other._dataYear) {
            return false;
        }
        if (this._ik != other._ik) {
            return false;
        }
        if (this._statusId != other._statusId) {
            return false;
        }
        if (!Objects.equals(this._name, other._name)) {
            return false;
        }
        return true;
    }
    // </editor-fold>

}
