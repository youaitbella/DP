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
@Table(name = "KGLPKMSAlternative", schema = "calc")
@XmlRootElement
public class KGLPKMSAlternative implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "paID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _department">
    @Basic(optional = false)
    @NotNull
    @Size(max = 200)
    @Column(name = "paDepartment")
    private String _department = "";

    public String getDepartment() {
        return _department;
    }

    public void setDepartment(String department) {
        this._department = department;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _departmentKey">
    @Basic(optional = false)
    @NotNull
    @Size(max = 4)
    @Column(name = "paDepartmentKey")
    private String _departmentKey = "";

    public String getDepartmentKey() {
        return _departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this._departmentKey = departmentKey;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _alternative">
    @Basic(optional = false)
    @NotNull
    @Size(max = 200)
    @Column(name = "paAlternative")
    private String _alternative = "";

    public String getAlternative() {
        return _alternative;
    }

    public void setAlternative(String alternative) {
        this._alternative = alternative;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationID">
//    @JoinColumn(name = "paBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "paBaseInformationID")
    private int _baseInformationID;

    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    //</editor-fold>

    public KGLPKMSAlternative() {
    }

    public KGLPKMSAlternative(Integer paID) {
        this._id = paID;
    }

    public KGLPKMSAlternative(Integer paID, String paDepartment, String paDepartmentKey, String paAlternative) {
        this._id = paID;
        this._department = paDepartment;
        this._departmentKey = paDepartmentKey;
        this._alternative = paAlternative;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 61 * hash + Objects.hashCode(this._department);
        hash = 61 * hash + Objects.hashCode(this._departmentKey);
        hash = 61 * hash + Objects.hashCode(this._alternative);
        hash = 61 * hash + this._baseInformationID;
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
        final KGLPKMSAlternative other = (KGLPKMSAlternative) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._baseInformationID != other._baseInformationID) {
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
        return "org.inek.dataportal.entities.calc.KGLPKMSAlternative[ paID=" + _id + " ]";
    }
    //</editor-fold>
    
}
