package org.inek.dataportal.entities.account;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.inek.dataportal.utils.DateUtils;

@Entity
@Table(name = "AccountDocument")
public class AccountDocument implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adId")
    private Integer _adId;
    
    @Column(name = "adAccountId")
    private Integer _accountId;
    
    @Column(name = "adTimestamp")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _timestamp;
    
    @Column(name = "adValidUntil")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _validUntil;

    @Column(name = "adName")
    private String _name;
    
    @Column(name = "adDomain")
    private String _domain;

    @Lob
    @Column(name = "adContent")
    private byte[] _content;
    
    @Column(name = "adIsRead")
    private boolean _read;
    
    public Integer getId() {
        return _adId;
    }

    public void setId(Integer _id) {
        this._adId = _id;
    }

    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer _accountId) {
        this._accountId = _accountId;
    }

    public Date getTimestamp() {
        return _timestamp;
    }

    public void setTimestamp(Date _timestamp) {
        this._timestamp = _timestamp;
    }

    public Date getValidUntil() {
        return _validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this._validUntil = validUntil;
    }
    
    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getDomain() {
        return _domain;
    }

    public void setDomain(String domain) {
        this._domain = domain;
    }
    
    public byte[] getContent() {
        return _content;
    }

    public void setContent(byte[] _content) {
        this._content = _content;
    }

    public boolean isRead() {
        return _read;
    }

    public void setRead(boolean _read) {
        this._read = _read;
    }
    
    @Transient
    private int _validity = 1000;

    public int getValidity() {
        return _validity;
    }

    public void setValidity(int validity) {
        this._validity = validity;
    }
    
    @PrePersist
    @PreUpdate
    private void tagCreated() {
        _timestamp = Calendar.getInstance().getTime();
        if (_validUntil == null){
            _validUntil = DateUtils.getDateWithDayOffset(_validity);
        }
    }
    
}
