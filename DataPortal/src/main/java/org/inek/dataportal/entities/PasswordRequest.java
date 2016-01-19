package org.inek.dataportal.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "PasswordRequest")
public class PasswordRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "prAccountId")
    private Integer _accountId;
    @Column(name = "prPasswordHash")
    private String _passwordHash;
    @Column(name = "prActivationKey")
    private String _activationKey = "";
    @Column(name = "prCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate = null;

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer id) {
        _accountId = id;
    }

    public String getPasswordHash() {
        return _passwordHash == null ? "" : _passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        _passwordHash = passwordHash;
    }

    public String getPassword() {
        return "**********";
    }

    public String getActivationKey() {
        return _activationKey;
    }

    public void setActivationKey(String activationKey) {
        this._activationKey = activationKey;
    }

    public Date getCreationTS() {
        return _creationDate;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_accountId != null ? _accountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PasswordRequest)) {
            return false;
        }
        PasswordRequest other = (PasswordRequest) object;
        if ((_accountId == null && other.getAccountId() != null) || (_accountId != null && !_accountId.equals(other.getAccountId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.AccountPwd[id=" + _accountId + "]";
    }
    // </editor-fold>
    
    @PrePersist
    @PreUpdate
    private void tagCreated() {
        _creationDate = Calendar.getInstance().getTime();
    }
    
}
