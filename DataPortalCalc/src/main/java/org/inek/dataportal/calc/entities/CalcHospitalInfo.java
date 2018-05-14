/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities;

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
import org.inek.dataportal.calc.enums.CalcInfoType;
import org.inek.dataportal.common.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
@Entity
@IdClass(CalcHospitalInfoId.class)
public class CalcHospitalInfo implements Serializable {

    public CalcHospitalInfo() {
    }

    public CalcHospitalInfo(int id, CalcInfoType type, int accountId, int dataYear, int ik,
            int statusId, String name, Date lastChanged, String customerName, String advisorMail, String customerTown) {
        this._id = id;
        this._type = type;
        this._accountId = accountId;
        this._dataYear = dataYear;
        this._ik = ik;
        this._statusId = statusId;
        this._name = name;
        this._lastChanged = lastChanged;
        this._customerName = customerName;
        this._agentName = advisorMail;
        this._customerTown = customerTown;
    }

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

    // <editor-fold defaultstate="collapsed" desc="Property AgentName">
    @Column(name = "AgentName")
    private String _agentName;

    public String getAgentName() {
        return _agentName;
    }

    public void setAdvisorMail(String advisorMail) {
        _agentName = advisorMail;
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

    // <editor-fold defaultstate="collapsed" desc="Property CustomerName">
    @Column(name = "customerTown")
    private String _customerTown;

    public String getCustomerTown() {
        return _customerTown;
    }

    public void setCustomerTown(String customerTown) {
        _customerTown = customerTown;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CustomerName">
    @Column(name = "customerName")
    private String _customerName;

    public String getCustomerName() {
        return _customerName;
    }

    public void setCustomerName(String customerName) {
        _customerName = customerName;
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
        int hash = 7;
        hash = 43 * hash + this._id;
        hash = 43 * hash + Objects.hashCode(this._type);
        hash = 43 * hash + this._accountId;
        hash = 43 * hash + Objects.hashCode(this._agentName);
        hash = 43 * hash + this._dataYear;
        hash = 43 * hash + this._ik;
        hash = 43 * hash + this._statusId;
        hash = 43 * hash + Objects.hashCode(this._name);
        hash = 43 * hash + Objects.hashCode(this._customerTown);
        hash = 43 * hash + Objects.hashCode(this._customerName);
        hash = 43 * hash + Objects.hashCode(this._lastChanged);
        return hash;
    }

    @SuppressWarnings("CyclomaticComplexity")
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
        final CalcHospitalInfo other = (CalcHospitalInfo) obj;
        if (this._id != other._id) {
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
        if (!Objects.equals(this._agentName, other._agentName)) {
            return false;
        }
        if (!Objects.equals(this._name, other._name)) {
            return false;
        }
        if (!Objects.equals(this._customerTown, other._customerTown)) {
            return false;
        }
        if (!Objects.equals(this._customerName, other._customerName)) {
            return false;
        }
        if (this._type != other._type) {
            return false;
        }
        if (!Objects.equals(this._lastChanged, other._lastChanged)) {
            return false;
        }
        return true;
    }
    // </editor-fold>
}
