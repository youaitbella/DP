package org.inek.dataportal.entities.account;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.inek.dataportal.entities.Document;
import org.inek.dataportal.utils.DateUtils;

@Entity
@Table(name = "AccountDocument")
public class AccountDocument implements Serializable, Document {
    
    private static final long serialVersionUID = 1L;
    
    public AccountDocument() {}

    public AccountDocument(String name) {
        _name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adId")
    private Integer _id;
    
    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "adAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property UploadAccountId">
    @Column(name = "adAgentAccountId")
    private int _agentAccountId;

    public int getAgentAccountId() {
        return _agentAccountId;
    }

    public void setAgentAccountId(int agentAccountId) {
        _agentAccountId = agentAccountId;
    }
    // </editor-fold>
        
    @Column(name = "adCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _created;
    
    @Column(name = "adLastChanged")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastChanged;

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this._lastChanged = lastChanged;
    }
    
    @Column(name = "adValidUntil")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _validUntil;

    @Column(name = "adName")
    private String _name;
    
    @Lob
    @Column(name = "adContent")
    private byte[] _content;
    
    @Column(name = "adIsRead")
    private boolean _read;
    
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Date getCreated() {
        return _created;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getValidUntil() {
        return _validUntil;
    }

    public void setValidUntil(Date validUntil) {
        _validUntil = validUntil;
    }
    
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    @Column(name = "adDocumentDomainId", updatable = false, insertable = false)
    private int _domainId;

    public int getDomainId() {
        return _domainId;
    }

    @OneToOne()
    @JoinColumn(name = "adDocumentDomainId")
    private DocumentDomain _domain;

    public DocumentDomain getDomain() {
        return _domain;
    }

    public void setDomain(DocumentDomain domain) {
        _domain = domain;
    }
    
    public byte[] getContent() {
        return _content;
    }

    public void setContent(byte[] content) {
        _content = content;
    }

    public boolean isRead() {
        return _read;
    }

    public void setRead(boolean read) {
        _read = read;
    }
    
    @Transient
    private int _validity = 1000;

    public int getValidity() {
        return _validity;
    }

    public void setValidity(int validity) {
        _validity = validity;
    }
    
    @PrePersist
    private void tagCreated() {
        _created = Calendar.getInstance().getTime();
        if (_validUntil == null){
            _validUntil = DateUtils.getDateWithDayOffset(_validity);
        }
        tagChanged();
    }
    
    @PreUpdate
    private void tagChanged() {
        _lastChanged = Calendar.getInstance().getTime();
    }
    
}
