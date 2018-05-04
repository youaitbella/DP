/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.common.data.account.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "AccountAdditionalIK")
public class AccountAdditionalIK implements Serializable {

    private static final long serialVersionUID = 1L;

    public AccountAdditionalIK() {
    }

    public AccountAdditionalIK(int accountId, int ik) {
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

    //<editor-fold defaultstate="collapsed" desc="Property IsMainIk">
    @Column(name = "aaiIsMainIk")
    private boolean _mainIk;

    public boolean isMainIk() {
        return _mainIk;
    }

    public void setMainIk(boolean mainIk) {
        _mainIk = mainIk;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccountAdditionalIK)) {
            return false;
        }
        AccountAdditionalIK other = (AccountAdditionalIK) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.AccountAdditionalIK[id=" + _id + "]";
    }
    // </editor-fold>

}
