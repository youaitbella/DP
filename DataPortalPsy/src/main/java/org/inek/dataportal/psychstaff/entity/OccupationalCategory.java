/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psychstaff.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listOccupationalCategory", schema = "psy")
public class OccupationalCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ocId")
    private int _id;
    
    public int getId() {
        return _id;
    }
    
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property PersonnelGroupId">
    @OneToOne()
    @PrimaryKeyJoinColumn(name = "ocPersonnelGroupId")
    private PersonnelGroup _personnelGroup;
    
    public PersonnelGroup getPersonnelGroup() {
        return _personnelGroup;
    }
    
    public void setPersonnelGroup(PersonnelGroup personnelGroup) {
        this._personnelGroup = personnelGroup;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "ocName")
    @Documentation(key = "lblName")
    private String _name = "";

    @Size(max = 50)
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals, toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + _personnelGroup.hashCode();
        hash = 97 * hash + Objects.hashCode(_name);
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
        final OccupationalCategory other = (OccupationalCategory) obj;
        if (this._personnelGroup != other._personnelGroup) {
            return false;
        }
        if (!Objects.equals(this._name, other._name)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return _name;
    }
    //</editor-fold>
    
}
