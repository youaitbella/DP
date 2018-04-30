/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.admin.dao;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 *
 * @author muellermi
 */
@Entity
public class AccountInfo implements Serializable {

//    public AccountInfo() {
//    }
//
//    public AccountInfo(int accountId, String accountFirstName, String accountLastName, String accountEmail, int count) {
//        _accountId = accountId;
//        _accountFirstName = accountFirstName;
//        _accountLastName = accountLastName;
//        _accountEmail = accountEmail;
//        _count = count;
//    }
    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Id
    @Column(name = "acId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountFirstName">
    @Column(name = "acFirstName")
    private String _accountFirstName;

    public String getAccountFirstName() {
        return _accountFirstName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountLastName">
    @Column(name = "acLastName")
    private String _accountLastName;

    public String getAccountLastName() {
        return _accountLastName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountEmail">
    @Column(name = "acEmail")
    private String _accountEmail;

    public String getAccountEmail() {
        return _accountEmail;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Selected">
    @Transient
    private boolean _isSelected = false;

    public boolean isSelected() {
        return _isSelected;
    }

    public void setSelected(boolean isSelected) {
        _isSelected = isSelected;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Count">
    @Column(name = "acCount")
    private int _count;

    public int getCount() {
        return _count;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        return _accountId;
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
        AccountInfo other = (AccountInfo) obj;
        return this._accountId == other._accountId;
    }

}
