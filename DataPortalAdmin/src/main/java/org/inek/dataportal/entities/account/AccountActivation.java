package org.inek.dataportal.entities.account;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "AccountActivation")
@IdClass(AccountActivationId.class)
public class AccountActivation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "aaAccountId")
    private Integer _accountId;
    
    @Id
    @Column(name = "aaGUID")
    private String _guid;
    
    @Column(name = "aaCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _created;

    // <editor-fold defaultstate="collapsed" desc="getter / setter">

    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer accountId) {
        this._accountId = accountId;
    }

    public String getGuid() {
        return _guid;
    }

    public void setGuid(String guid) {
        this._guid = guid;
    }

    public Date getCreated() {
        return _created;
    }

    public void setCreated(Date created) {
        this._created = created;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getAccountId() != null ? getAccountId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccountPwd)) {
            return false;
        }
        AccountPwd other = (AccountPwd) object;
        if ((getAccountId() == null && other.getAccountId() != null) || (getAccountId() != null && !_accountId.equals(other.getAccountId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.AccountPwd[id=" + getAccountId() + "]";
    }
    // </editor-fold>
}
