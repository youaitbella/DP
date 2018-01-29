package org.inek.dataportal.entities.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
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

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wdId")
    private int _id;
    
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="List Accounts">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "mapWaitingDocAccount",
            joinColumns = @JoinColumn(name = "wdaWaitingDocumentId"),
            inverseJoinColumns = @JoinColumn(name = "wdaAccountId"))
    private List<Account> _accounts = new ArrayList<>();    

    public List<Account> getAccounts() {
        return _accounts;
    }

    public void setAccounts(List<Account> accounts) {
        _accounts = accounts;
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
        
    // <editor-fold defaultstate="collapsed" desc="Property Timestamp">
    @Column(name = "wdTimestamp")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _timestamp;
    
    public Date getTimestamp() {
        return _timestamp;
    }

    public void setTimestamp(Date timestamp) {
        _timestamp = timestamp;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "wdName")
    private String _name;

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>
   
    // <editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "wdIk")
    private int _ik = -1;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Content">
    @Lob
    @Column(name = "wdContent")
    private byte[] _content;
    
    @Override
    public byte[] getContent() {
        return _content;
    }

    @Override
    public void setContent(byte[] content) {
        _content = content;
    }
    // </editor-fold>
   

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
