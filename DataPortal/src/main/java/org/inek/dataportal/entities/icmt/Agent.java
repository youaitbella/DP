/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.entities.icmt;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "ccAgent", catalog="CallCenterDB", schema="dbo")
public class Agent implements Serializable {
    private static final Logger _logger = Logger.getLogger("Agent");
    private static final long serialVersionUID = 1L;
        
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property LoginName">
    @Column(name = "agLoginName")
    private String _loginName;

    public String getLoginName() {
        return _loginName;
    }

    public void setLoginName(String loginName) {
        _loginName = loginName;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property FirstName">
    @Column(name = "agFirstName")
    private String _firstName;

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        this._firstName = firstName;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property LastName">
    @Column(name = "agLastName")
    private String _lastName;

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Initials">
    @Column(name = "agInitials")
    private String _initials;

    public String getInitials() {
        return _initials;
    }

    public void setInitials(String initials) {
        _initials = initials;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Mail">
    @Column(name = "agEMail")
    private String _mail;

    public String getMail() {
        return _mail;
    }

    public void setMail(String mail) {
        this._mail = mail;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Phone">
    @Column(name = "agPhone")
    private String _phone;

    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        this._phone = phone;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Active">
    @Column(name = "agActive")
    private boolean _active;

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        _active = active;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property DomainId">
    @Column(name = "agDomainId")
    private String _domainId;

    public String getDomainId() {
        return _domainId;
    }

    public void setDomainId(String domainId) {
        this._domainId = domainId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property IgnoreUnknownNo">
    @Column(name = "agIgnoreUnknownNo")
    private boolean _ignoreUnknownNo;

    public boolean isIgnoreUnknownNo() {
        return _ignoreUnknownNo;
    }

    public void setIgnoreUnknownNo(boolean ignoreUnknownNo) {
        _ignoreUnknownNo = ignoreUnknownNo;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode & equals & toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this._id;
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Agent other = (Agent) obj;
        if (this._id != other._id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Agent{" + "firstName=" + _firstName + ", lastName=" + _lastName + '}';
    }
    //</editor-fold>
    
    @PrePersist
    @PreUpdate
    private void preventUpdate(){
        _logger.warning("Attempt to write Agent");
        throw new IllegalStateException("Attempt to write Agent");
    }
}
