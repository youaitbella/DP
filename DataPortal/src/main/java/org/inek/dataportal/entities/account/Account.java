/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
import org.inek.dataportal.entities.admin.InekRole;
import org.inek.dataportal.entities.certification.RemunerationSystem;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "Account")
public class Account implements Serializable, Person {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acId")
    private Integer _id;
    @Column(name = "acLastModified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastModified = null;
    @Column(name = "acIsDeactivated")
    private boolean _isDeactivated = false;
    @Column(name = "acUser")
    private String _user;
    @Column(name = "acMail")
    private String _email;
    @Column(name = "acGender")
    private int _gender = 0;
    @Column(name = "acTitle")
    private String _title = "";
    @Column(name = "acFirstName")
    private String _firstName = "";
    @Column(name = "acLastName")
    private String _lastName = "";
    @Column(name = "acInitials")
    private String _initials = "";
    @Column(name = "acPhone")
    private String _phone = "";
    @Column(name = "acRoleId")
    private int _roleId = -1;
    @Column(name = "acCompany")
    private String _company = "";
    @Column(name = "acCustomerTypeId")
    private int _customerTypeId = -1;
    @Column(name = "acIK")
    private Integer _ik = -1;
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
    private boolean _autoSave;

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

    // <editor-fold defaultstate="collapsed" desc="Property CertSystems">
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "Grouper", schema = "crt",
            joinColumns = @JoinColumn(name = "grAccountId"),
            inverseJoinColumns = @JoinColumn(name = "grSystemId"))
    private List<RemunerationSystem> _systems;

    public List<RemunerationSystem> getRemuneratiosSystems() {
        return _systems;
    }

    public void setRemuneratiosSystems(List<RemunerationSystem> systems) {
        _systems = systems;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

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

    @Override
    public String getEmail() {
        return _email;
    }

    @Override
    public void setEmail(String email) {
        _email = email;
    }

    @Override
    public int getGender() {
        return _gender;
    }

    @Override
    public void setGender(int isFemale) {
        _gender = isFemale;
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public void setTitle(String title) {
        _title = title;
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

    @Override
    public String getFirstName() {
        return _firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    @Override
    public String getLastName() {
        return _lastName;
    }

    @Override
    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getInitials() {
        return _initials;
    }

    public void setInitials(String initials) {
        _initials = initials;
    }

    public String getCompany() {
        return _company;
    }

    public void setCompany(String company) {
        _company = company;
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

    // </editor-fold>
    
    public Set<Integer> getFullIkList() {
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

    // <editor-fold defaultstate="collapsed" desc="Transient Property ReportViaPortal">
    @Transient
    private boolean _reportViaPortal = true;

    public boolean isReportViaPortal() {
        return _reportViaPortal;
    }

    public void setReportViaPortal(boolean reportViaPortal) {
        _reportViaPortal = reportViaPortal;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Transient Property Tag">
    @Transient
    private String _tag = "";

    public String getReportViaPortal() {
        return _tag;
    }

    public void setReportViaPortal(String tag) {
        _tag = tag;
    }
    // </editor-fold>

    
}
