/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.entities.xtransient;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author muellermi
 */
@Entity

public class AccountInfo implements Serializable {
    @Id
    @Column(name = "acId")
    private Integer _accountId;
    @Column(name = "acCompany")
    private String _company;
    @Column(name = "acFirstName")
    private String _firstName;
    @Column(name = "acLastName")
    private String _lastName;
    @Column(name = "acMail")
    private String _email;

    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer _accountId) {
        this._accountId = _accountId;
    }

    public String getCompany() {
        return _company;
    }

    public void setCompany(String company) {
        this._company = company;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String _firstName) {
        this._firstName = _firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String _lastName) {
        this._lastName = _lastName;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;
    }
}
