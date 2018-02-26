package org.inek.dataportal.common.feature.account.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "AccountPwd")
public class AccountPwd implements Serializable {
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Id
    @Column(name = "apAccountId")
    private Integer _accountId;
    
    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer id) {
        _accountId = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Salt">
    @Column(name = "apSalt")
    private String _salt = ""; 

    public String getSalt() {
        return _salt;
    }
    
    public void setSalt(String salt) {
        _salt = salt;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property PasswordHash">
    @Column(name = "apPasswordHash")
    private String _passwordHash = "";

    public String getPasswordHash() {
        return _passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        _passwordHash = passwordHash;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property LastModified">
    @Column(name = "apLastModified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastModified = null;

    public Date getLastModified() {
        return _lastModified;
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
        if (!(object instanceof AccountPwd)) {
            return false;
        }
        AccountPwd other = (AccountPwd) object;
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
    public void tagModifiedDate() {
        _lastModified = Calendar.getInstance().getTime();
    }
    
}
