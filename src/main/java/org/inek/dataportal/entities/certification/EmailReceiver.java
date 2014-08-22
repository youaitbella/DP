/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.certification;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "EmailReceivers", schema = "crt")
public class EmailReceiver implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "erId")
    private int _erId = -1;

    public int getEmailReceiversId() {
        return _erId;
    }

    public void setEmailReceiversId(int id) {
        _erId = id;
    }
    
    @Column(name = "erReceiverList")
    private int _receiverList = 0;

    public int getReceiverList() {
        return _receiverList;
    }

    public void setReceiverList(int receiverList) {
        _receiverList = receiverList;
    }
    
    @Column(name = "erAccountId")
    private int _accountId = 0;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int _accountId) {
        this._accountId = _accountId;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this._erId;
        hash = 89 * hash + this._receiverList;
        hash = 89 * hash + this._accountId;
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
        final EmailReceiver other = (EmailReceiver) obj;
        return _receiverList == other._receiverList && _erId == other._erId && _accountId == other._accountId;
    }

}
