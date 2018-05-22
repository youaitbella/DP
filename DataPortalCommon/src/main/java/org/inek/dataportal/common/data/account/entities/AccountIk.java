/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.common.data.account.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "AccountAdditionalIK")
public class AccountIk implements Serializable {

    private static final long serialVersionUID = 1L;

    public AccountIk() {
    }

    public AccountIk(int accountId, int ik) {
        _accountId = accountId;
        _ik = ik;
    }

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aaiId")
    private Integer _id;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "aaiIK")
    private Integer _ik;

    public Integer getIK() {
        return _ik;
    }

    public void setIK(Integer ik) {
        _ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "aaiAccountId")
    private Integer _accountId;

    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer accountId) {
        _accountId = accountId;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this._ik);
        hash = 89 * hash + Objects.hashCode(this._accountId);
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
        final AccountIk other = (AccountIk) obj;
        if (!Objects.equals(this._ik, other._ik)) {
            return false;
        }
        if (!Objects.equals(this._accountId, other._accountId)) {
            return false;
        }
        return true;
    }

}
