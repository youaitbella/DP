/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.account.entities;

import org.inek.dataportal.common.data.account.iface.Person;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.FeatureState;
import org.inek.dataportal.common.data.adm.InekRole;
import org.inek.dataportal.common.data.ikadmin.entity.AccountIkAdmin;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "Account")
public class Account implements Serializable, Person {

    private static final long serialVersionUID = 1L;
    

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acId")
    private int _id = -1;
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Gender">
    @Column(name = "acGender")
    private int _gender = 0;
    @Override
    public int getGender() {
        return _gender;
    }

    @Override
    public void setGender(int isFemale) {
        _gender = isFemale;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Title">
    @Column(name = "acTitle")
    private String _title = "";
    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public void setTitle(String title) {
        _title = title;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FirstName">
    @Column(name = "acFirstName")
    private String _firstName = "";
    @Override
    public String getFirstName() {
        return _firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        _firstName = firstName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastName">
    @Column(name = "acLastName")
    private String _lastName = "";
    @Override
    public String getLastName() {
        return _lastName;
    }

    @Override
    public void setLastName(String lastName) {
        _lastName = lastName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Mail">
    @Column(name = "acMail")
    private String _email;
    @Override
    public String getEmail() {
        return _email;
    }

    @Override
    public void setEmail(String email) {
        _email = email;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Company">
    @Column(name = "acCompany")
    private String _company = "";

    public String getCompany() {
        return _company;
    }

    public void setCompany(String company) {
        _company = company;
    }
    // </editor-fold>
    
    @Column(name = "acLastModified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastModified = null;
    @Column(name = "acIsDeactivated")
    private boolean _isDeactivated = false;
    @Column(name = "acUser")
    private String _user;
    @Column(name = "acInitials")
    private String _initials = "";
    @Column(name = "acPhone")
    private String _phone = "";
    @Column(name = "acRoleId")
    private int _roleId = -1;
    @Column(name = "acCustomerTypeId")
    private int _customerTypeId = -1;
    @Column(name = "acIK")
    private Integer _ik = -1;   // todo: change type to int
    @Column(name = "acStreet")
    private String _street = "";
    @Column(name = "acPostalCode")
    private String _postalCode = "";
    @Column(name = "acTown")
    private String _town = "";
    @Column(name = "acCustomerPhone")
    private String _customerPhone = "";
    @Column(name = "acCustomerFax")
    private String _customerFax = "";

    @Column(name = "acNubConfirmation")
    private boolean _nubConfirmation = true;

    @Column(name = "acMessageCopy")
    private boolean _messageCopy = true;

    @Column(name = "acNubInformationMail")
    private boolean _nubInformationMail = true;

    public boolean isNubInformationMail() {
        return _nubInformationMail;
    }

    public void setNubInformationMail(boolean nubInformationMail) {
        _nubInformationMail = nubInformationMail;
    }

    // <editor-fold defaultstate="collapsed" desc="Property DropBoxHoldTime">
    @Column(name = "acDropBoxHoldTime")
    private int _dropBoxHoldTime=100;

    @Min(30) @Max(1500)
    public int getDropBoxHoldTime() {
        return _dropBoxHoldTime;
    }

    public void setDropBoxHoldTime(int dropBoxHoldTime) {
        _dropBoxHoldTime = dropBoxHoldTime;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AutoSave">
    @Column(name = "acAutoSave")
    private boolean _autoSave = true;  // default new accounts to autosave

    public boolean isAutoSave() {
        return _autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        _autoSave = autoSave;
    }
    // </editor-fold>

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "afAccountId", referencedColumnName = "acId")
    @OrderBy("_sequence")
    private List<AccountFeature> _features;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "aaiAccountId", referencedColumnName = "acId")
    @OrderBy("_ik")
    private List<AccountAdditionalIK> _additionalIKs;

    // <editor-fold defaultstate="collapsed" desc="Property AdminIks">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "aiaAccountId", referencedColumnName = "acId")
    @OrderBy("_ik")
    private List<AccountIkAdmin> _adminIks;

    public List<AccountIkAdmin> getAdminIks() {
        return _adminIks;
    }

    public void setAdminIks(List<AccountIkAdmin> adminIks) {
        this._adminIks = adminIks;
    }

    public void removeIkAdmin(int ik) {
        _adminIks.removeIf(ai -> ai.getIk() == ik);
    }

    /**
     * Adds an ik and returns true, if ik could be added, false otherwise (ik existed)
     * @param ik
     * @param mailDomain
     * @return 
     */
    public boolean addIkAdmin(int ik, String mailDomain) {
        Optional<AccountIkAdmin> admin = _adminIks.stream().filter(ai -> ai.getIk() == ik).findAny();
        if (admin.isPresent()){
            admin.get().setMailDomain(mailDomain);
            return false;
        }
        _adminIks.add(new AccountIkAdmin(_id, ik, mailDomain));
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InekRoles">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "mapAccountInekRole", schema = "adm",
            joinColumns = @JoinColumn(name = "aiAccountId"),
            inverseJoinColumns = @JoinColumn(name = "aiInekRoleId"))
    private List<InekRole> _inekRoles;

    public List<InekRole> getInekRoles() {
        return _inekRoles;
    }

    public void setInekRoles(List<InekRole> inekRoles) {
        _inekRoles = inekRoles;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Date getLastModified() {
        return _lastModified;
    }

    public boolean isDeactivated() {
        return _isDeactivated;
    }

    public void setDeactivated(boolean isDeactivated) {
        _isDeactivated = isDeactivated;
    }

    public String getUser() {
        return _user;
    }

    public void setUser(String user) {
        _user = user;
    }

    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public int getRoleId() {
        return _roleId;
    }

    public void setRoleId(int roleId) {
        _roleId = roleId;
    }

    public int getCustomerTypeId() {
        return _customerTypeId;
    }

    public void setCustomerTypeId(int customerTypeId) {
        _customerTypeId = customerTypeId;
    }

    public Integer getIK() {
        return _ik == null || _ik < 0 ? null : _ik;
    }

    public void setIK(Integer ik) {
        _ik = ik == null ? -1 : ik;
    }

    public String getCustomerPhone() {
        return _customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        _customerPhone = customerPhone;
    }

    public String getCustomerFax() {
        return _customerFax;
    }

    public void setCustomerFax(String customerFax) {
        _customerFax = customerFax;
    }

    public String getInitials() {
        return _initials;
    }

    public void setInitials(String initials) {
        _initials = initials;
    }

    public String getStreet() {
        return _street;
    }

    public void setStreet(String street) {
        _street = street;
    }

    public String getPostalCode() {
        return _postalCode;
    }

    public void setPostalCode(String postalCode) {
        _postalCode = postalCode;
    }

    public String getTown() {
        return _town;
    }

    public void setTown(String town) {
        _town = town;
    }

    public boolean isNubConfirmation() {
        return _nubConfirmation;
    }

    public void setNubConfirmation(boolean nubConfirmation) {
        _nubConfirmation = nubConfirmation;
    }

    public boolean isMessageCopy() {
        return _messageCopy;
    }

    public void setMessageCopy(boolean messageCopy) {
        _messageCopy = messageCopy;
    }

    public void addFeature(Feature feature, boolean isApproved) {
        FeatureState state = feature.needsApproval() 
                ? (isApproved ? FeatureState.APPROVED : FeatureState.NEW) 
                : FeatureState.SIMPLE;
        _features.add(new AccountFeature(_features.size(), state, feature));
    }

    public void setFeatures(List<AccountFeature> features) {
        _features = features;
    }

    public List<AccountFeature> getFeatures() {
        if (_features == null) {
            _features = new ArrayList<>();
        }
        return _features;
    }

    public void setAdditionalIKs(List<AccountAdditionalIK> additionalIKs) {
        _additionalIKs = additionalIKs;
    }

    public List<AccountAdditionalIK> getAdditionalIKs() {
        if (_additionalIKs == null) {
            _additionalIKs = new ArrayList<>();
        }
        return _additionalIKs;
    }

    public void addIk(int ik){
        _additionalIKs.add(new AccountAdditionalIK(_id, ik));
    }
    // </editor-fold>
    
    public Set<Integer> getFullIkSet() {
        Set<Integer> iks = new HashSet<>();
        if (_ik != null && _ik > 0) {
            iks.add(_ik);
        }
        for (AccountAdditionalIK addIk : getAdditionalIKs()) {
            iks.add(addIk.getIK());
        }
        return iks;
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    public void nullIK() {
        if (_ik != null && _ik == -1) {
            // in db, -1 will indicate a non-existant IK
            _ik = null;
        }
    }

    @PrePersist
    @PreUpdate
    public void tagModifiedDate() {
        _lastModified = Calendar.getInstance().getTime();
        if (_ik == null) {
            _ik = -1;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this._id);
        return hash;
    }

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
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
        final Account other = (Account) obj;
        return Objects.equals(this._id, other._id);
    }

    @Override
    public String toString() {
        return "org.inek.entities.Account[id=" + _id + "]";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Transient Property Selected">
    @Transient
    private boolean _selected;

    public boolean isSelected() {
        return _selected;
    }

    public void setSelected(boolean selected) {
        _selected = selected;
    }
    // </editor-fold>



}