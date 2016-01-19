/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.entities.account;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aaiId")
    private Integer _id;
    @Column(name = "aaiIK")
    private Integer _ik;

    @Transient
    private String _name;
   
    public AccountAdditionalIK() {
    }

    public AccountAdditionalIK(final Integer ik) {
        _ik = ik;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer Id) {
        this._id = Id;
    }

    public Integer getIK() {
        return _ik;
    }

    public void setIK(Integer ik) {
        _ik = ik;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    // </editor-fold>

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
