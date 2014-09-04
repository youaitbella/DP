package org.inek.dataportal.entities.certification;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "GrouperAction", schema = "crt")
public class GrouperAction implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gaID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SystemId">
    @Column(name = "gaSystemId")
    private int _systemId;
    public int getSystemId() {
        return _systemId;
    }

    public void setSystemId(int systemId) {
        this._systemId = systemId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "gaAccountId")
    private int _accountId;
    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PasswordRequest">
    @Column(name = "gaTimeStamp")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _timeStamp;
    public Date getTimeStamp() {
        return _timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        _timeStamp = timeStamp;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Action">
    @Column(name = "gaAction")
    private String _action;
    public String getAction() {
        return _action;
    }

    public void setAction(String action) {
        _action = action;
    }
    // </editor-fold>

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
        final GrouperAction other = (GrouperAction) obj;
        return _id == other._id;
    }
    // </editor-fold>

}
