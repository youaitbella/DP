/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.domain;


/**
 *
 * @author muellermi
 */
public class UserMasterData {
    private int _id;
    private String _user;
    private String _email;
    private String _password;
    private String _repeatPassword;
    private String _title;
    private String _firstName;
    private String _lastName;
    private String _contactPhone;
    private int _contactRole;  // job title
    private String _company;
    private int _customerType;
    private Integer _ik;
    private String _street;
    private String _postalCode;
    private String _town;
    private String _phone;
    private String _fax;
    
    private boolean _approved;
    

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
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

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public String getRepeatPassword() {
        return _repeatPassword;
    }

    public void setRepeatPassword(String password) {
        _repeatPassword = password;
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

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getContactPhone() {
        return _contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        _contactPhone = contactPhone;
    }

    public int getContactRole() {
        return _contactRole;
    }

    public void setContactRole(int contactRole) {
        _contactRole = contactRole;
    }

    public int getCustomerType() {
        return _customerType;
    }

    public void setCustomerType(int customerType) {
        _customerType = customerType;
    }

    public Integer getIk() {
        return _ik;
    }

    public void setIk(Integer ik) {
        _ik = ik;
    }

    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public String getFax() {
        return _fax;
    }

    public void setFax(String fax) {
        _fax = fax;
    }

    public boolean isApproved() {
        return _approved;
    }

    public void setApproved(boolean approved) {
        _approved = approved;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserMasterData)) {
            return false;
        }
        UserMasterData other = (UserMasterData) object;
        return getId() == other.getId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + getId();
        return hash;
    }

    @Override
    public String toString() {
        return getUser() + " (" + getFirstName() + " " + getLastName() + ")";
    }

    // </editor-fold>


}


