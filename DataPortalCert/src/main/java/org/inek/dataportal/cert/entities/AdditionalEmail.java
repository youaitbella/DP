/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.cert.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "CertAdditionalEmail", schema = "crt")
public class AdditionalEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caeId")
    private Integer _id;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = id;
    }

    @Column(name = "caeAccountId")
    private int _accountId = 0;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }

    @Column(name = "caeEmail")
    private String _email = "";

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        this._email = email;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this._accountId;
        hash = 37 * hash + Objects.hashCode(this._email);
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
        final AdditionalEmail other = (AdditionalEmail) obj;
        if (this._accountId != other._accountId) {
            return false;
        }
        if (!Objects.equals(this._email, other._email)) {
            return false;
        }
        return true;
    }

}
