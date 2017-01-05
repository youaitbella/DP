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
@Table(name = "KGLPKMSAlternative", schema = "calc")
@XmlRootElement
public class KGLPKMSAlternative implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
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


    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLPKMSAlternative)) {
            return false;
        }
        KGLPKMSAlternative other = (KGLPKMSAlternative) object;
        return this._id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLPKMSAlternative[ paID=" + _id + " ]";
    }
    
}
