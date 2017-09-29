/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.admin.entity;

import org.inek.dataportal.feature.psychstaff.entity.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listReportTemplate", schema = "adm")
public class ReportTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rtId")
    private int _id;
    
    public int getId() {
        return _id;
    }
    
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "rtName")
    private String _name = "";

    @Size(max = 100)
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Address">
    @Column(name = "rtAddress")
    private String _address = "";

    @Size(max = 200)
    public String getAddress() {
        return _address;
    }

    public void setAddress(String address) {
        _address = address;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals, toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this._id;
        hash = 97 * hash + Objects.hashCode(this._address);
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
        final ReportTemplate other = (ReportTemplate) obj;
        if (this._id != other._id) {
            return false;
        }
        if (!Objects.equals(this._address, other._address)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return _address;
    }
    //</editor-fold>
    
}
