/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.admin;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountAdditionalIK;
import org.inek.dataportal.enums.Feature;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listInekRole", schema = "adm")
public class InekRole implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "irId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Text">
    @Column(name = "irText")
    private String _text;

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Feature">
    @Column(name = "irFeature")
    @Enumerated(EnumType.STRING)
    private Feature _feature;

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        this._feature = feature;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property WriteEnabled">
    @Column(name = "irIsWriteEnabled")
    private boolean _isWriteEnabled;

    public boolean isWriteEnabled() {
        return _isWriteEnabled;
    }

    public void setWriteEnabled(boolean isWriteEnabled) {
        this._isWriteEnabled = isWriteEnabled;
    }
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="Property Mapping">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "aiInekRoleId", referencedColumnName = "irId")
    private List<RoleMapping> _mappings;

    public List<RoleMapping> getMappings() {
        return _mappings;
    }

    public void setMappings(List<RoleMapping> _mappings) {
        _mappings = _mappings;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Property Accounts">
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
//    @JoinTable(
//            name = "mapAccountInekRole", schema = "adm",
//            joinColumns = @JoinColumn(name = "aiInekRoleId"),
//            inverseJoinColumns = @JoinColumn(name = "aiAccountId"))
//    private List<Account> _accounts;
//
//    public List<Account> getAccounts() {
//        return _accounts;
//    }
//
//    public void setAccounts(List<Account> accounts) {
//        _accounts = accounts;
//    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        if (_id >= 0) {
            return _id;
        }
        int hash = 7;
        hash = 17 * hash + this._id;
        hash = 17 * hash + Objects.hashCode(this._text);
        hash = 17 * hash + Objects.hashCode(this._feature);
        hash = 17 * hash + (this._isWriteEnabled ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InekRole)) {
            return false;
        }
        InekRole other = (InekRole) object;
        if (_id >= 0 && _id == other._id) {
            return true;
        }
        return fullyEquals(other);
    }

    @Override
    public String toString() {
        return "org.inek.entities.Role[id=" + _id + "]";
    }
    // </editor-fold>

    public boolean fullyEquals(InekRole other) {
        return _id == other._id
                && _text.equals(other._text)
                && _feature.equals(other._feature)
                && _isWriteEnabled == other._isWriteEnabled;
    }

    public InekRole copy() {
        InekRole copy = new InekRole();
        copy.setId(this.getId());
        copy.setFeature(this.getFeature());
        copy.setText(this.getText());
        copy.setWriteEnabled(this.isWriteEnabled());
        return copy;
    }

}
