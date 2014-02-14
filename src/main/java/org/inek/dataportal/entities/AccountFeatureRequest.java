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
public class AccountFeatureRequest implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "afrAccountId")
    private Integer _accountId;
    @Column(name = "afrApprovalKey")
    private String _approvalKey = "";
    @Column(name = "afrCreationDate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate;

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public String getApprovalKey() {
        return _approvalKey;
    }

    public void setApprovalKey(String approvlaKey) {
        _approvalKey = approvlaKey;
    }

    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer accountId) {
        _accountId = accountId;
    }

    public Date getCreationDate() {
        return _creationDate;
    }
    // </editor-fold>

    @PrePersist
    public void tagCreationDate() {
        _creationDate = Calendar.getInstance().getTime();
    }
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_accountId != null ? _accountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccountFeatureRequest)) {
            return false;
        }
        AccountFeatureRequest other = (AccountFeatureRequest) object;
        if ((_accountId == null && other.getAccountId() != null) || (_accountId != null && !_accountId.equals(other.getAccountId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.AccountFeatureRequest[id=" + _accountId + "]";
    }
    // </editor-fold>
   
}
