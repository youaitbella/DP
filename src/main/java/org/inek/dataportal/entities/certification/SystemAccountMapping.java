/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.certification;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "mapSystemAccount", schema = "crt")
@IdClass(MapSystemAccount.class)
public class SystemAccountMapping implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Property InekRoleId">
    @Id
    @Column(name = "msaSystemId")
    private int _systemId = -1;

    public int getSystemId() {
        return _systemId;
    }

    public void setSystemId(int id) {
        _systemId = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Id
    @Column(name = "msaAccountId")
    private int _accountId = -1;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int id) {
        _accountId = id;
    }
    // </editor-fold>

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this._systemId;
        hash = 89 * hash + this._accountId;
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
        final SystemAccountMapping other = (SystemAccountMapping) obj;
        return _accountId == other._accountId && _systemId == other._systemId;
    }

}
