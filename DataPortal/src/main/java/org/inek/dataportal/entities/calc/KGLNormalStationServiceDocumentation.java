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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLNormalStationServiceDocumentation", schema = "calc")
public class KGLNormalStationServiceDocumentation implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nssID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _used">
    @Column(name = "nssUsed")
    private boolean _used;

    public boolean isUsed() {
        return _used;
    }

    public void setUsed(boolean used) {
        this._used = used;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _department">
    @Size(max = 200)
    @Column(name = "nssDepartment")
    private String _department = "";

    public String getDepartment() {
        return _department;
    }

    public void setDepartment(String department) {
        this._department = department;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _departmentKey">
    @Size(max = 4)
    @Column(name = "nssDepartmentKey")
    private String _departmentKey = "";

    public String getDepartmentKey() {
        return _departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this._departmentKey = departmentKey;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _alternative">
    @Size(max = 200)
    @Column(name = "nssAlternative")
    private String _alternative = "";

    public String getAlternative() {
        return _alternative;
    }

    public void setAlternative(String alternative) {
        this._alternative = alternative;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationID">
//    @JoinColumn(name = "nssBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "nssBaseInformationID")
    private int _baseInformationID;

    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _contentTextID">
//    @JoinColumn(name = "nssContentTextID", referencedColumnName = "ctID")
//    @ManyToOne(optional = false)
    @Column(name = "nssContentTextID")
    private int _contentTextID;

    public int getContentTextID() {
        return _contentTextID;
    }

    public void setContentTextID(int contentTextID) {
        this._contentTextID = contentTextID;
    }
    //</editor-fold>

    @Transient
    private String _label;

    public String getLabel() {
        return _label;
    }

    public void setLabel(String _label) {
        this._label = _label;
    }

    public KGLNormalStationServiceDocumentation() {
    }

    public KGLNormalStationServiceDocumentation(int nssID) {
        this._id = nssID;
    }

    public KGLNormalStationServiceDocumentation(int nssID, boolean nssUsed, String nssDepartment, String nssDepartmentKey, String nssAlternative) {
        this._id = nssID;
        this._used = nssUsed;
        this._department = nssDepartment;
        this._departmentKey = nssDepartmentKey;
        this._alternative = nssAlternative;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 59 * hash + (this._used ? 1 : 0);
        hash = 59 * hash + Objects.hashCode(this._department);
        hash = 59 * hash + Objects.hashCode(this._departmentKey);
        hash = 59 * hash + Objects.hashCode(this._alternative);
        hash = 59 * hash + this._baseInformationID;
        hash = 59 * hash + this._contentTextID;
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
        final KGLNormalStationServiceDocumentation other = (KGLNormalStationServiceDocumentation) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._used != other._used) {
            return false;
        }
        if (this._baseInformationID != other._baseInformationID) {
            return false;
        }
        if (this._contentTextID != other._contentTextID) {
            return false;
        }
        if (!Objects.equals(this._department, other._department)) {
            return false;
        }
        if (!Objects.equals(this._departmentKey, other._departmentKey)) {
            return false;
        }
        if (!Objects.equals(this._alternative, other._alternative)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLNormalStationServiceDocumentation[ nssID=" + _id + " ]";
    }
    //</editor-fold>

}
