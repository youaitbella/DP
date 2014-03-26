/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "Log", schema = "adm")
public class Log implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logId")
    private Integer _id;
    
    @Column(name = "logTS")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate = null;

    @Column(name = "logAccountId")
    private int _accountId;
    
    @Column(name = "logMessage")
    private String _message;
    
    @Column(name = "logSession")
    private String _session;
    

    public Log(){}
    public Log(int accountId, String session, String message){
        _accountId=accountId;
        _session=session;
        _message = message;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Integer getId() {
        return _id;
    }

    public Date getCreation() {
        return _creationDate;
    }

    public void setId(Integer iId) {
        _id = iId;
    }

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }

    public String getSession() {
        return _session;
    }

    public void setSession(String session) {
        _session = session;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Log)) {
            return false;
        }
        Log other = (Log) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.AccountFeature[id=" + _id + "]";
    }
    // </editor-fold>
    
    @PrePersist
    @PreUpdate
    private void setTS() {
        _creationDate = Calendar.getInstance().getTime();
    }
    
}
