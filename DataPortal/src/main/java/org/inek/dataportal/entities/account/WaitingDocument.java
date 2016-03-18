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
import org.inek.dataportal.entities.Document;

@Entity
@Table(name = "WaitingDocument")
public class WaitingDocument implements Serializable, Document {
    
    private static final long serialVersionUID = 1L;
    
    public WaitingDocument() {}

    public WaitingDocument(String name) {
        _name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wdId")
    private Integer _id;
    
    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "wdAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property AgentAccountId">
    @Column(name = "wdAgentAccountId")
    private int _agentAccountId;

    public int getAgentAccountId() {
        return _agentAccountId;
    }

    public void setAgentAccountId(int agentAccountId) {
        _agentAccountId = agentAccountId;
    }
    // </editor-fold>
        
    @Column(name = "wdTimestamp")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _timestamp;
    

    @Column(name = "wdName")
    private String _name;
    
    @Lob
    @Column(name = "wdContent")
    private byte[] _content;
    
   
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Date getTimestamp() {
        return _timestamp;
    }

    public void setTimestamp(Date timestamp) {
        _timestamp = timestamp;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    @Column(name = "wdDocumentDomainId", updatable = false, insertable = false)
    private int _domainId;

    public int getDomainId() {
        return _domainId;
    }
    @OneToOne()
    @JoinColumn(name = "wdDocumentDomainId")
    private DocumentDomain _domain;

    public DocumentDomain getDomain() {
        return _domain;
    }

    public void setDomain(DocumentDomain domain) {
        _domain = domain;
    }
    
    @Override
    public byte[] getContent() {
        return _content;
    }

    @Override
    public void setContent(byte[] content) {
        _content = content;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Property Validity">
    @Column(name = "wdValidity")
    private int _validity = 1000;

    public int getValidity() {
        return _validity;
    }

    public void setValidity(int validity) {
        _validity = validity;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property JsonMail">
    @Column(name = "wdJsonMail")
    private String _jsonMail;

    public String getJsonMail() {
        return _jsonMail;
    }

    public void setJsonMail(String jsonMail) {
        _jsonMail = jsonMail;
    }
    // </editor-fold>
    
    @PrePersist
    @PreUpdate
    private void tagCreated() {
        _timestamp = Calendar.getInstance().getTime();
    }
    
}
