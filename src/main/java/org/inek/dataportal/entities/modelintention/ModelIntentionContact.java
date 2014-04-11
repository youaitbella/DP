package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * A contact might be any (juristic) person, who is involved into the model intent,
 * e.g. partner, hospital, insurance
 * 
 */
@Entity
@Table(name = "Contacts", schema = "mvh")
public class ModelIntentionContact implements Serializable {

    private static final long serialVercsonUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "csId")
    private Integer _id;
    
    @Column (name = "csModelIntentionId")
    private Integer _modelIntentionId;
    
    @Column(name = "csContactTypeId")
    private Integer _type;

    @Column(name = "csIK")
    private Integer _ik;

    @Column(name = "csName")
    private String _name;
    
    @Column(name = "csStreet")
    private String _street;
    
    @Column(name = "csZip")
    private Integer _zip;
    
    @Column(name = "csLocation")
    private Integer _Town;
    
    @Column(name = "csRegCare")
    private Integer _regCare;
    
    @Column(name = "csContactPerson")
    private String _contactPerson;
    
    @Column(name = "csPhone")
    private Integer _phone;
    
    @Column(name = "csEMail")
    private String _email;   
  
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer _Id) {
        _id = _Id;
    }

    public Integer getType() {
        return _type;
    }

    public void setType(Integer _Type) {
        _type = _Type;
    }

    public Integer getIK() {
        return _ik;
    }

    public void setIK(Integer _IK) {
        _ik = _IK;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _Name) {
        _name = _Name;
    }

    public String getStreet() {
        return _street;
    }

    public void setStreet(String _Street) {
        _street = _Street;
    }

    public Integer getZip() {
        return _zip;
    }

    public void setZip(Integer _Zip) {
        _zip = _Zip;
    }

    public Integer getTown() {
        return _Town;
    }

    public void setTown(Integer _Town) {
        _Town = _Town;
    }

    public Integer getRegCare() {
        return _regCare;
    }

    public void setRegCare(Integer _regCare) {
        _regCare = _regCare;
    }

    public String getContactPerson() {
        return _contactPerson;
    }

    public void setContactPerson(String _ContactPerson) {
        _contactPerson = _ContactPerson;
    }

    public Integer getPhone() {
        return _phone;
    }

    public void setPhone(Integer _Phone) {
        _phone = _Phone;
    }

    public String getEMail() {
        return _email;
    }

    public void setEMail(String _EMail) {
        _email = _EMail;
    }

    public Integer getModelIntentionId() {
        return _modelIntentionId;
    }

    public void setModelIntentionId(Integer modelIntentionId) {
        _modelIntentionId = modelIntentionId;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModelIntentionContact)) {
            return false;
        }
        ModelIntentionContact other = (ModelIntentionContact) object;
        if ((_id == null && other.getId()!= null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.StructureInvolved[id=" + _id + "]";
    }

    // </editor-fold>
}
