/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities;

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
 * @author vohldo/schlappajo
 */
@Entity
@Table(name = "StructureInvolved", schema = "mvh")
public class ModelIntentionStructureInvolved implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "siId")
    private Integer _Id;
    
    @Column(name = "siType")
    private Integer _Type;

    @Column(name = "siIK")
    private Integer _IK;

    @Column(name = "siName")
    private String _Name;
    
    @Column(name = "siStreet")
    private String _Street;
    
    @Column(name = "siZip")
    private Integer _Zip;
    
    @Column(name = "siLocation")
    private Integer _Location;
    
    @Column(name = "siRegCare")
    private Integer _regCare;
    
    @Column(name = "siContactPerson")
    private Integer _ContactPerson;
    
    @Column(name = "siPhone")
    private Integer _Phone;
    
    @Column(name = "siEMail")
    private Integer _EMail;   

    
  
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Integer getId() {
        return _Id;
    }

    public void setId(Integer _Id) {
        this._Id = _Id;
    }

    public Integer getType() {
        return _Type;
    }

    public void setType(Integer _Type) {
        this._Type = _Type;
    }

    public Integer getIK() {
        return _IK;
    }

    public void setIK(Integer _IK) {
        this._IK = _IK;
    }

    public String getName() {
        return _Name;
    }

    public void setName(String _Name) {
        this._Name = _Name;
    }

    public String getStreet() {
        return _Street;
    }

    public void setStreet(String _Street) {
        this._Street = _Street;
    }

    public Integer getZip() {
        return _Zip;
    }

    public void setZip(Integer _Zip) {
        this._Zip = _Zip;
    }

    public Integer getLocation() {
        return _Location;
    }

    public void setLocation(Integer _Location) {
        this._Location = _Location;
    }

    public Integer getRegCare() {
        return _regCare;
    }

    public void setRegCare(Integer _regCare) {
        this._regCare = _regCare;
    }

    public Integer getContactPerson() {
        return _ContactPerson;
    }

    public void setContactPerson(Integer _ContactPerson) {
        this._ContactPerson = _ContactPerson;
    }

    public Integer getPhone() {
        return _Phone;
    }

    public void setPhone(Integer _Phone) {
        this._Phone = _Phone;
    }

    public Integer getEMail() {
        return _EMail;
    }

    public void setEMail(Integer _EMail) {
        this._EMail = _EMail;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_Id != null ? _Id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModelIntentionStructureInvolved)) {
            return false;
        }
        ModelIntentionStructureInvolved other = (ModelIntentionStructureInvolved) object;
        if ((_Id == null && other.getId()!= null) || (_Id != null && !_Id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.StructureInvolved[id=" + _Id + "]";
    }

    // </editor-fold>
}
