/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.entities.calc.iface.IdValue;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListStationAlternative", schema = "calc")
public class KGPListStationAlternative implements Serializable, IdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saID")
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _departmentName">
    @Size(max = 50)
    @Column(name = "saDepartmentName")
    private String _departmentName = "";

    public String getDepartmentName() {
        return _departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this._departmentName = departmentName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _alternative">
    @Size(max = 300)
    @Column(name = "saAlternative")
    private String _alternative = "";

    public String getAlternative() {
        return _alternative;
    }

    public void setAlternative(String alternative) {
        this._alternative = alternative;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "seBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "saBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DepartmentKey">
//    @JoinColumn(name = "seBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "saDepartmentKey")
    private int _departmentKey;

    public int getDepartmentKey() {
        return _departmentKey;
    }

    public void setDepartmentKey(int departmentKey) {
        this._departmentKey = departmentKey;
    }
    //</editor-fold>

    public KGPListStationAlternative() {
    }

    public KGPListStationAlternative(int sdID) {
        this._id = sdID;
    }

    public KGPListStationAlternative(int sdID, String seDepartmentName, String seAlternative) {
        this._id = sdID;
        this._departmentName = seDepartmentName;
        this._alternative = seAlternative;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 53 * hash + Objects.hashCode(this._departmentName);
        hash = 53 * hash + Objects.hashCode(this._alternative);
        hash = 53 * hash + this._baseInformationId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListStationAlternative)) {
            return false;
        }
        final KGPListStationAlternative other = (KGPListStationAlternative) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (!Objects.equals(this._departmentName, other._departmentName)) {
            return false;
        }
        return Objects.equals(this._alternative, other._alternative);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListStationDepartment[ sdID=" + _id + " ]";
    }
    //</editor-fold>

}
