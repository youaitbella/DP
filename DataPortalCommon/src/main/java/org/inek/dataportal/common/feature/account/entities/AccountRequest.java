/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.feature.account.entities;

import org.inek.dataportal.common.feature.account.iface.Person;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.inek.dataportal.common.utils.Crypt;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "AccountRequest")
public class AccountRequest implements Serializable, Person {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "arId")
    private int _id;
    @Column(name = "arCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate;
    @Column(name = "arUser")
    private String _user;
    @Column(name = "arMail")
    private String _email;
    @Column(name = "arPassword")
    private String _passwordHash;
    @Column(name = "arActivationKey")
    private String _activationKey = "";
    @Column(name = "arGender")
    private int _gender;
    @Column(name = "arTitle")
    private String _title = "";
    @Column(name = "arFirstName")
    private String _firstName = "";
    @Column(name = "arLastName")
    private String _lastName = "";
    @Column(name = "arPhone")
    private String _phone = "";
    @Column(name = "arRoleId")
    private Integer _roleId = -1;
    @Column(name = "arCompany")
    private String _company = "";
    @Column(name = "arCustomerTypeId")
    private Integer _customerTypeId = -1;
    @Column(name = "arIK")
    private Integer _ik = -1;
    @Column(name = "arStreet")
    private String _street = "";
    @Column(name = "arPostalCode")
    private String _postalCode = "";
    @Column(name = "arTown")
    private String _town = "";
    @Column(name = "arCustomerPhone")
    private String _customerPhone = "";
    @Column(name = "arCustomerFax")
    private String _customerFax = "";

    // <editor-fold defaultstate="collapsed" desc="Property Salt">
    @Column(name = "arSalt")
    private String _salt = "";

    public String getSalt() {
        return _salt;
    }

    public void setSalt(String salt) {
        _salt = salt;
    }
    // </editor-fold>

    @PrePersist
    public void tagModifiedDate() {
        _creationDate = Calendar.getInstance().getTime();
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public int getId() {
        return _id;
    }

    public Date getCreationDate() {
        return _creationDate;
    }

    /**
     * @return the _activationKey
     */
    public String getActivationKey() {
        return _activationKey;
    }

    /**
     * @param activationKey the _activationKey to set
     */
    public void setActivationKey(String activationKey) {
        _activationKey = activationKey;
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

    public Integer getRoleId() {
        return _roleId;
    }

    public void setRoleId(Integer roleId) {
        _roleId = roleId;
    }

    public Integer getCustomerTypeId() {
        return _customerTypeId;
    }

    public void setCustomerTypeId(Integer customerTypeId) {
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

    public void setAccountId(int id) {
        _id = id;
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

    public String getPasswordHash() {
        return _passwordHash == null ? "" : _passwordHash;
    }

    public String getPassword() {
        return "**********";
    }

    public void setPassword(String password) {
        _salt = Crypt.getSalt();
        _passwordHash = Crypt.hashPassword(password, _salt);
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this._id;
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
        final AccountRequest other = (AccountRequest) obj;
        if (this._id != other._id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.Account[id=" + _id + "]";
    }
    // </editor-fold>
}
