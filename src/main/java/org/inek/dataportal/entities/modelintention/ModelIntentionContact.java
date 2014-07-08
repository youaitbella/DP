package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.inek.dataportal.utils.Documentation;

/**
 * A contact might be any (juristic) person, who is involved into the model
 * intent, e.g. partner, hospital, insurance
 *
 */
@Entity
@Table(name = "Contacts", schema = "mvh")
public class ModelIntentionContact implements Serializable {

    private static final long serialVercsonUID = 1L;

    public ModelIntentionContact() {
    }

    public ModelIntentionContact(int contactTypeId) {
        _contactTypeId = contactTypeId;
    }

    // <editor-fold defaultstate="collapsed" desc="Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "csId")
    private Integer _id;
    
    public Integer getId() {
        return _id;
    }

    public void setId(Integer _Id) {
        _id = _Id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ModelIntentionId">
    @Column(name = "csModelIntentionId")
   
    private Integer _modelIntentionId;

    public Integer getModelIntentionId() {
        return _modelIntentionId;
    }

    public void setModelIntentionId(Integer modelIntentionId) {
        _modelIntentionId = modelIntentionId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ContactTypeId">
    @Column(name = "csContactTypeId")
    @Documentation(name = "Art", translateValue = "1=headerModelIntentionContract; 2=headerModelIntentionProvider; 3=headerModelIntentionCostInsurance", omitOnEmpty = true)
    private int _contactTypeId;
    
    public int getContactTypeId() {
        return _contactTypeId;
    }

    public void setContactTypeId(int contactTypeId) {
        _contactTypeId = contactTypeId;
    }
     // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="IK">
    @Column(name = "csIK")
    @Documentation(key = "lblIK", translateValue = "-1=empty")
    private int _ik = -1;
    
     public Integer getIk() {
        return _ik < 0 ? null : _ik;
    }

    public void setIk(Integer ik) {
        _ik = ik == null ? -1 : ik;
    }
     // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Name">
    @Column(name = "csName")
    @Documentation(key = "lblName")
    private String _name = "";
    
    @Size(max = 100)
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
     // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Street">
    @Column(name = "csStreet")
    @Documentation(key = "lblStreet")
    private String _street = "";
    
    @Size(max = 100)
    public String getStreet() {
        return _street;
    }

    public void setStreet(String street) {
        _street = street;
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Zip">
    @Column(name = "csZip")
   // @Pattern(regexp = "\\b((?:0[1-46-9]\\d{3})|(?:[1-357-9]\\d{4})|(?:[4][0-24-9]\\d{3})|(?:[6][013-9]\\d{3}))\\b", message = "Die Eingabe entspricht keiner Postleitzahl.")
    @Documentation(key = "lblPostalCode")
    private String _zip = "";
    
    @Pattern(regexp = "^$|^[0-9]{5}$", message = "Die Eingabe entspricht keiner Postleitzahl.")
    public String getZip() {
        return _zip;
    }

    public void setZip(String zip) {
        _zip = zip;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Town">
    @Column(name = "csTown")
    @Documentation(key = "lblTown")
    private String _town = "";
    
    @Size(max = 50)
    public String getTown() {
        return _town;
    }

    public void setTown(String town) {
        _town = town;
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property RegCare">
    @Column(name = "csRegCare")
    @Documentation(key = "lblRegCare")
    private boolean _regCare;

    public boolean getRegCare() {
        return _regCare;
    }

    public void setRegCare(boolean regCare) {
        _regCare = regCare;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ContactPerson">
    @Column(name = "csContactPerson")
    @Documentation(key = "lblContactPerson")
    private String _contactPerson = "";
    
    @Size(max = 100)
    public String getContactPerson() {
        return _contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        _contactPerson = contactPerson;
    }
     // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Phone">
    @Column(name = "csPhone")
    @Documentation(key = "lblPhone")
    private String _phone = "";
    
    @Size(max = 50)
    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="EMail">
    @Column(name = "csEMail")
    @Documentation(key = "lblMail")
    private String _email = "";
    
   // @Pattern(regexp = "^[_A-Za-z0-9-]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$")
    @Size(max = 50)
    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }
    // </editor-fold>
    
   

    public boolean isEmpty() {
        return _ik == -1
                && _name.isEmpty()
                && _street.isEmpty()
                && _zip.isEmpty()
                && _town.isEmpty()
                && !_regCare
                && _contactPerson.isEmpty()
                && _phone.isEmpty()
                && _email.isEmpty();
    }

 
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ModelIntentionContact)) {
            return false;
        }
        ModelIntentionContact other = (ModelIntentionContact) object;
        return Objects.equals(_id, other._id)
                && (_id != null
                || _contactTypeId == other._contactTypeId && equalsFunctional(other));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this._id);
        if (_id == null) {
            hash = 83 * hash + this._contactTypeId;
            hash = 83 * hash + this._ik;
            if (_ik == -1) {
                hash = 83 * hash + Objects.hashCode(this._name);
                hash = 83 * hash + Objects.hashCode(this._town);
            }
        }
        return hash;
    }

    @Override
    public String toString() {
        return "org.inek.entities.StructureInvolved[id=" + _id + "]";
    }

    // </editor-fold>
    /**
     * Two contacts are functional equal, if they have the same ik or the same
     * name and town, even thought they might differ in other properties.
     *
     * @param other
     * @return
     */
    public boolean equalsFunctional(ModelIntentionContact other) {
        if (other == null) {
            return false;
        }
        return _ik == other._ik
                && (_ik >= 0
                || this._name.equals(other._name)
                && this._town.equals(other._town));
    }

}
