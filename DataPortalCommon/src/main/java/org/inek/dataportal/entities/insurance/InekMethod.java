/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.insurance;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "Category", catalog = "NUB", schema = "dbo")
public class InekMethod implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @Column(name = "caId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int value) {
        _id = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "caName")
    private String _name = "";

    public String getName() {
        return _name;
    }

    public void setName(String value) {
        _name = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InekMethod)) {
            return false;
        }
        InekMethod other = (InekMethod) object;
        return _id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.entities.InekMethod[id=" + _id + "]";
    }
    // </editor-fold>
    
}
