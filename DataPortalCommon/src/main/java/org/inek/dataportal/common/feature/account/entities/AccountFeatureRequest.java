package org.inek.dataportal.common.feature.account.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.data.converter.FeatureConverter;

/**
 *
 * @author muellermi
 */
@Entity
public class AccountFeatureRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="PropertyId">
    @Id
    @Column(name = "afrId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer _id;
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "afrAccountId")
    private Integer _accountId;
    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer accountId) {
        _accountId = accountId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Feature">
    @Column(name = "afrFeatureId")
    @Convert(converter = FeatureConverter.class)
    private Feature _feature;
    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ApprovalKey">
    @Column(name = "afrApprovalKey")
    private String _approvalKey = "";
    public String getApprovalKey() {
        return _approvalKey;
    }

    public void setApprovalKey(String approvlaKey) {
        _approvalKey = approvlaKey;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CreationDate">
    @Column(name = "afrCreationDate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate;

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
