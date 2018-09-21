/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.ikadmin.entity;

import org.inek.dataportal.common.data.account.iface.Person;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * An excerpt of Account for fast info within IkAdmin
 * @author muellermi
 */
@Entity
@Table(name = "Account")
public class User implements Serializable, Person {

    private static final long serialVersionUID = 1L;


    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @Column(name = "acId")
    private int _id;
    @Override
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

    // <editor-fold defaultstate="collapsed" desc="Property Email">
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

    @Override
    public String getCompany() {
        return _company;
    }

    public void setCompany(String company) {
        _company = company;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this._id;
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
        final User other = (User) obj;
        return this._id == other._id;
    }
    // </editor-fold>

}
