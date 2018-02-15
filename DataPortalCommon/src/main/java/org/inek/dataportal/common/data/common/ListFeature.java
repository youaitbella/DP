/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.common;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author muellermi
 */
@Entity(name = "listFeature")
public class ListFeature implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @Column(name = "feId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "feName")
    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Description">
    @Column(name = "feDescription")
    private String _description;

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals ">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this._id;
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
        final ListFeature other = (ListFeature) obj;
        if (this._id != other._id) {
            return false;
        }
        return true;
    }
    //</editor-fold>

}
