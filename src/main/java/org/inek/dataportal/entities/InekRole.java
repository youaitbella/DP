/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.inek.dataportal.enums.Feature;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listInekRole", schema = "adm")
public class InekRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "irId")
    private Integer _id;
    @Column(name = "irText")
    private String _text;
    @Column(name = "irFeature")
    @Enumerated(EnumType.STRING)
    private Feature _feature;
    @Column(name = "irIsWriteEnabled")
    private boolean  _isWriteEnabled;

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        this._feature = feature;
    }

    public boolean isIsWriteEnabled() {
        return _isWriteEnabled;
    }

    public void setIsWriteEnabled(boolean isWriteEnabled) {
        this._isWriteEnabled = isWriteEnabled;
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
        if (!(object instanceof InekRole)) {
            return false;
        }
        InekRole other = (InekRole) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.Role[id=" + _id + "]";
    }
    // </editor-fold>
    
}
