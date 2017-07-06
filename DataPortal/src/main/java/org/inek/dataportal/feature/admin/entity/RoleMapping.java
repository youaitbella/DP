/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.admin.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "mapAccountInekRole", schema = "adm")
@IdClass(MapAccountInekRoleId.class)
public class RoleMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    
    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Id
    @Column(name = "aiAccountId")
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
    @Column(name = "aiInekRoleId")
    private int _inekRoleId = -1;

    public int getInekRoleId() {
        return _inekRoleId;
    }

    public void setInekRoleId(int id) {
        _inekRoleId = id;
    }
    // </editor-fold>

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this._accountId;
        hash = 89 * hash + this._inekRoleId;
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
        final RoleMapping other = (RoleMapping) obj;
        return _accountId == other._accountId && _inekRoleId == other._inekRoleId;
    }
    
}
