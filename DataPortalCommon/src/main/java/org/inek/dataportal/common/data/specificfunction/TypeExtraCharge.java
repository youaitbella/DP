/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.specificfunction;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author aitbellayo
 */
@Entity
@Table(name = "listTypeExtraCharge", schema = "spf")
public class TypeExtraCharge implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tecId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "tecName")
    @Documentation(key = "lblName")
    private String _name = "";

    //</editor-fold>
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals, toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this._id;
        hash = 67 * hash + Objects.hashCode(this._name);
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
        final TypeExtraCharge other = (TypeExtraCharge) obj;
        if (this._id != other._id) {
            return false;
        }
        if (!Objects.equals(this._name, other._name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TypeExtraCharge{" + "_id=" + _id + ", _name=" + _name + '}';
    }

    //</editor-fold>
}
