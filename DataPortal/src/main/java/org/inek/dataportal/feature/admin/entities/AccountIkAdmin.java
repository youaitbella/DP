/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.admin.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "mapAccountIkAdmin", schema = "adm")
@IdClass(MapAccountIk.class)
public class AccountIkAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    
    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Id
    @Column(name = "[aiaAccountId]")
    private int _accountId = -1;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int id) {
        _accountId = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InekRoleId">
    @Id
    @Column(name = "aiaIk")
    private int _ik = -1;

    public int getIk() {
        return _ik;
    }

    public void setIk(int id) {
        _ik = id;
    }
    // </editor-fold>

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this._accountId;
        hash = 89 * hash + this._ik;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccountIkAdmin other = (AccountIkAdmin) obj;
        return _accountId == other._accountId && _ik == other._ik;
    }
    
}
