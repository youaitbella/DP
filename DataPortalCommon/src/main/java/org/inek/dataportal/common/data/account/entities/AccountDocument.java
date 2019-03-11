package org.inek.dataportal.common.data.account.entities;

import org.inek.dataportal.common.data.account.iface.Document;
import org.inek.dataportal.common.utils.DateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "AccountDocument")
public class AccountDocument implements Serializable, Document {
    
    private static final long serialVersionUID = 1L;
    
    public AccountDocument() {}

    public AccountDocument(String name) {
        _name = name;
    }

    //<editor-fold desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adId")
    private Integer _id;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    //</editor-fold>

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

    //<editor-fold desc="Property Created">
    @Column(name = "adCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _created;

    public Date getCreated() {
        return _created;
    }

    public void setCreated(Date created) {
        _created = created;
    }
    //</editor-fold>

    //<editor-fold desc="Property LastChanged">
    @Column(name = "adLastChanged")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastChanged;

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this._lastChanged = lastChanged;
    }
    //</editor-fold>

    //<editor-fold desc="Property ValidUntil">
    @Column(name = "adValidUntil")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _validUntil;

    public Date getValidUntil() {
        return _validUntil;
    }

    public void setValidUntil(Date validUntil) {
        _validUntil = validUntil;
    }
    //</editor-fold>

    //<editor-fold desc="Property Name">
    @Column(name = "adName")
    private String _name;

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }
    //</editor-fold>

    //<editor-fold desc="Property Content">
    @Lob
    @Column(name = "adContent")
    private byte[] _content;

    @Override
    public byte[] getContent() {
        return _content;
    }

    @Override
    public void setContent(byte[] content) {
        _content = content;
    }
    //</editor-fold>

    //<editor-fold desc="Property IsRead">
    @Column(name = "adIsRead")
    private boolean _read;

    public boolean isRead() {
        return _read;
    }

    public void setRead(boolean read) {
        _read = read;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SenderIk">
    @Column(name = "adSenderIk")
    private int _senderIk;
    
    public int getSenderIk() {
        return _senderIk;
    }
    
    public void setSenderIk(int senderIk) {
        this._senderIk = senderIk;
    }
    //</editor-fold>

    //<editor-fold desc="Property DocumentDomain">
    @ManyToOne()
    @JoinColumn(name = "adDocumentDomainId")
    private DocumentDomain _domain;

    public DocumentDomain getDomain() {
        return _domain;
    }

    public void setDomain(DocumentDomain domain) {
        _domain = domain;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DocumentId">
    @Column(name = "adDocumentId")
    private int _documentId;

    public int getDocumentId() {
        return _documentId;
    }

    public void setDocumentId(int documentId) {
        this._documentId = documentId;
    }
    //</editor-fold>


    //<editor-fold desc="Transioent Validity">
    @Transient
    private int _validity = 1000;

    public int getValidity() {
        return _validity;
    }

    public void setValidity(int validity) {
        _validity = validity;
    }
    //</editor-fold>
    
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

    //<editor-fold desc="Property SendToProcess">
    @Column(name = "adSendToProcess")
    private boolean _sendToProcess;

    public boolean isSendToProcess() {
        return _sendToProcess;
    }

    public void setSendToProcess(boolean sendToProcess) {
        this._sendToProcess = sendToProcess;
    }
    //</editor-fold>
}
