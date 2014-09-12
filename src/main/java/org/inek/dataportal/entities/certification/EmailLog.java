/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.certification;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "EmailLog", schema = "crt")
public class EmailLog implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "elId")
    private int _elId = -1;

    public int getEmailLogId() {
        return _elId;
    }

    public void setEmailLogId(int id) {
        _elId = id;
    }
    
    @Column(name = "elSystemId")
    private int _systemId = -1;

    public int getSystemId() {
        return _systemId;
    }

    public void setSystemId(int systemId) {
        _systemId = systemId;
    }
    
    @Column(name = "elSenderAccountId")
    private int _senderAccountId = 0;

    public int getSenderAccountId() {
        return _senderAccountId;
    }

    public void setSenderAccountId(int _senderAccountId) {
        this._senderAccountId = _senderAccountId;
    }
    
    @Column(name = "elReceiverAccountId")
    private int _receiverAccountId = 0;

    public int getReceiverAccountId() {
        return _receiverAccountId;
    }

    public void setReceiverAccountId(int _receiverAccountId) {
        this._receiverAccountId = _receiverAccountId;
    }
    
    @Column(name = "elTemplateId")
    private int _templateId = 0;

    public int getTemplateId() {
        return _templateId;
    }

    public void setTemplateId(int _templateId) {
        this._templateId = _templateId;
    }
    
    @Column(name = "elSent")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _sent = new Date();

    public Date getSent() {
        return _sent;
    }

    public void setSent(Date _sent) {
        this._sent = _sent;
    }
    
    @Column(name = "elType")
    private int _type = -1;

    public int getType() {
        return _type;
    }

    public void setType(int _type) {
        this._type = _type;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 16 * hash + this._elId;
        hash = 16 * hash + this._receiverAccountId;
        hash = 16 * hash + this._senderAccountId;
        hash = 16 * hash + this._systemId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EmailLog other = (EmailLog) obj;
        return _senderAccountId == other._senderAccountId && _elId == other._elId 
                && _receiverAccountId == other._receiverAccountId && _systemId == other._systemId;
    }

}
