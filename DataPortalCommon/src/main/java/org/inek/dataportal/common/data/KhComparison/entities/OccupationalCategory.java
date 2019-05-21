/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @ManyToOne()
    @JoinColumn(name = "ocPersonnelGroupId")
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

    public String getName() {
        return _name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Number">
    @Column(name = "ocNumber")
    private String _number = "";

    public String getNumber() {
        return _number;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Order">
    @Column(name = "ocOrder")
    private int _order = 0;

    public int getOrder() {
        return _order;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsPsyStaff">
    @Column(name = "ocIsPsyStaff")
    private Boolean _isPsyStaff = false;

    public Boolean getIsPsyStaff() {
        return _isPsyStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsAeb">
    @Column(name = "ocIsAeb")
    private Boolean _isAeb = false;

    public Boolean getIsAeb() {
        return _isAeb;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals, toString">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OccupationalCategory that = (OccupationalCategory) o;
        return _order == that._order &&
                Objects.equals(_personnelGroup, that._personnelGroup) &&
                Objects.equals(_name, that._name) &&
                Objects.equals(_number, that._number) &&
                Objects.equals(_isPsyStaff, that._isPsyStaff) &&
                Objects.equals(_isAeb, that._isAeb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_personnelGroup, _name, _number, _order, _isPsyStaff, _isAeb);
    }

    @Override
    public String toString() {
        return _name;
    }
    //</editor-fold>

}
