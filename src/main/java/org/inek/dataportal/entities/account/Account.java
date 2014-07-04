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
import java.util.Set;
import javax.persistence.*;
import org.inek.dataportal.entities.admin.InekRole;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "Account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acId")
    private Integer _accountId;
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
    private boolean _nubConfirmation = false;

    @Column(name = "acMessageCopy")
    private boolean _messageCopy = false;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "afAccountId", referencedColumnName = "acId")
    @OrderBy("_sequence")
    private List<AccountFeature> _features;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "aaiAccountId", referencedColumnName = "acId")
    @OrderBy("_ik")
    private List<AccountAdditionalIK> _additionalIKs;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "mapAccountInekRole", schema = "adm",
            joinColumns = @JoinColumn(name = "aiAccountId"),
            inverseJoinColumns = @JoinColumn(name = "aiInekRoleId"))
    private List<InekRole> _inekRoles;

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer id) {
        _accountId = id;
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

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public int getGender() {
        return _gender;
    }

    public void setGender(int isFemale) {
        _gender = isFemale;
    }

    public String getTitle() {
        return _title;
    }

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

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

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

    public void setNubConfirmation(boolean _nubConfirmation) {
        this._nubConfirmation = _nubConfirmation;
    }

    public boolean isMessageCopy() {
        return _messageCopy;
    }

    public void setMessageCopy(boolean _messageCopy) {
        this._messageCopy = _messageCopy;
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

    public List<InekRole> getInekRoles() {
        return _inekRoles;
    }

    public void setInekRoles(List<InekRole> inekRoles) {
        _inekRoles = inekRoles;
    }

    // </editor-fold>
    
    public Set<Integer> getFullIkList() {
        Set<Integer> iks = new HashSet<>();
        if (_ik != null && _ik > 0) {iks.add(_ik);}
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

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_accountId != null ? _accountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((_accountId == null && other.getAccountId() != null) || (_accountId != null && !_accountId.equals(other.getAccountId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.Account[id=" + _accountId + "]";
    }
    // </editor-fold>
}
