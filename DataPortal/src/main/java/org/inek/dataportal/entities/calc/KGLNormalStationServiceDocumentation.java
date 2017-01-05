/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
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
@Table(name = "KGLNormalStationServiceDocumentation", schema = "calc")
@XmlRootElement
public class KGLNormalStationServiceDocumentation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "nssID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
    @Basic(optional = false)
    @NotNull
    @Column(name = "nssUsed")
    private boolean _used;

    public boolean isUsed() {
        return _used;
    }

    public void setUsed(boolean used) {
        this._used = used;
    }
    //</editor-fold>
    
    @Basic(optional = false)
    @NotNull
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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
    @Basic(optional = false)
    @NotNull
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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
    @Basic(optional = false)
    @NotNull
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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
//    @JoinColumn(name = "nssBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "nssBaseInformationID")
    private int _baseInformationID;

    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
//    @JoinColumn(name = "nssContentTextID", referencedColumnName = "ctID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "nssContentTextID")
    private int _contentTextID;
    //</editor-fold>

    public int getContentTextID() {
        return _contentTextID;
    }

    public void setContentTextID(int contentTextID) {
        this._contentTextID = contentTextID;
    }
    

    public KGLNormalStationServiceDocumentation() {
    }

    public KGLNormalStationServiceDocumentation(Integer nssID) {
        this._id = nssID;
    }

    public KGLNormalStationServiceDocumentation(int nssID, boolean nssUsed, String nssDepartment, String nssDepartmentKey, String nssAlternative) {
        this._id = nssID;
        this._used = nssUsed;
        this._department = nssDepartment;
        this._departmentKey = nssDepartmentKey;
        this._alternative = nssAlternative;
    }


    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLNormalStationServiceDocumentation)) {
            return false;
        }
        KGLNormalStationServiceDocumentation other = (KGLNormalStationServiceDocumentation) object;
        return this._id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLNormalStationServiceDocumentation[ nssID=" + _id + " ]";
    }
    
}
