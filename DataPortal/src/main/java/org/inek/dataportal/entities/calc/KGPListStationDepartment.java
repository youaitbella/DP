/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListStationDepartment", schema = "calc")
@XmlRootElement
public class KGPListStationDepartment implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "sdID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _departmentName">
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "seDepartmentName")
    private String _departmentName = "";

    public String getDepartmentName() {
        return _departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this._departmentName = departmentName;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _alternative">
    @Basic(optional = false)
    @NotNull
    @Size(max = 300)
    @Column(name = "seAlternative")
    private String _alternative = "";

    public String getAlternative() {
        return _alternative;
    }

    public void setAlternative(String alternative) {
        this._alternative = alternative;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "seBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "seBaseInformationID")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>
    

    public KGPListStationDepartment() {
    }

    public KGPListStationDepartment(int sdID) {
        this._id = sdID;
    }

    public KGPListStationDepartment(int sdID, String seDepartmentName, String seAlternative) {
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
        if (!(obj instanceof KGPListStationDepartment)) {
            return false;
        }
        final KGPListStationDepartment other = (KGPListStationDepartment) obj;
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
