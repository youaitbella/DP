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
    
    public ModelIntentionContact (){}
    public ModelIntentionContact (int contactTypeId){_contactTypeId = contactTypeId;}
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "csId")
    private Integer _id;
    
    @Column (name = "csModelIntentionId")
    private int _modelIntentionId;
    
    @Column(name = "csContactTypeId")
    private int _contactTypeId;

    @Column(name = "csIK")
    private int _ik = -1;

    @Column(name = "csName")
    private String _name = "";
    
    @Column(name = "csStreet")
    private String _street = "";
    
    @Column(name = "csZip")
    private int _zip;
    
    @Column(name = "csTown")
    private String _town = "";
    
    @Column(name = "csRegCare")
    private int _regCare;
    
    @Column(name = "csContactPerson")
    private String _contactPerson = "";
    
    @Column(name = "csPhone")
    private String _phone = "";
    
    @Column(name = "csEMail")
    private String _email = "";
  
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer _Id) {
        _id = _Id;
    }

    public int getContactTypeId() {
        return _contactTypeId;
    }

    public void setContactTypeId(int contactTypeId) {
        _contactTypeId = contactTypeId;
    }

    public Integer getIk() {
        return _ik < 0 ? null : _ik;
    }

    public void setIk(Integer ik) {
        _ik = ik == null ? -1 : ik;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getStreet() {
        return _street;
    }

    public void setStreet(String street) {
        _street = street;
    }

    public int getZip() {
        return _zip;
    }

    public void setZip(int zip) {
        _zip = zip;
    }

    public String getTown() {
        return _town;
    }

    public void setTown(String town) {
        _town = town;
    }

    public int getRegCare() {
        return _regCare;
    }

    public void setRegCare(int regCare) {
        _regCare = regCare;
    }

    public String getContactPerson() {
        return _contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        _contactPerson = contactPerson;
    }

    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
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
