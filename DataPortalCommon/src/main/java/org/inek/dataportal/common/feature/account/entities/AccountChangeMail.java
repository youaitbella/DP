package org.inek.dataportal.common.feature.account.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "AccountChangeMail")
public class AccountChangeMail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "acmAccountId")
    private Integer _accountId;
    @Column(name = "acmMail")
    private String _mail;
    @Column(name = "acmActivationKey")
    private String _activationKey = "";
    @Column(name = "acmCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate = null;

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer id) {
        _accountId = id;
    }

    public String getMail() {
        return _mail == null ? "" : _mail;
    }
    public void setMail(String mail) {
        _mail = mail;
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
        if (!(object instanceof AccountChangeMail)) {
            return false;
        }
        AccountChangeMail other = (AccountChangeMail) object;
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
