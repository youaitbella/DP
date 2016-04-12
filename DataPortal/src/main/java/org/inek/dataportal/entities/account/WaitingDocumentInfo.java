package org.inek.dataportal.entities.account;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "WaitingDocument")
public class WaitingDocumentInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public WaitingDocumentInfo() {}

    public WaitingDocumentInfo(String name) {
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

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="List Accounts">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, orphanRemoval = true)
    @JoinTable(
            name = "mapWaitingDocAccount",
            joinColumns = @JoinColumn(name = "wdaWaitingDocumentId"),
            inverseJoinColumns = @JoinColumn(name = "wdaAccountId"))
    private List<Account> _accounts = new ArrayList<>();    

    public List<Account> getAccounts() {
        return _accounts;
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

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "wdName")
    private String _name;

    public String getName() {
        return _name;
    }
    // </editor-fold>
   
    // <editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "wdIk")
    private int _ik = -1;

    public int getIk() {
        return _ik;
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

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property JsonMail">
    @Column(name = "wdJsonMail")
    private String _jsonMail;

    public String getJsonMail() {
        return _jsonMail;
    }

    // </editor-fold>
    
    
}
