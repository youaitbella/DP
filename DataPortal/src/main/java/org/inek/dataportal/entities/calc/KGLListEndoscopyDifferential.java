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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListEndoscopyDifferential", schema = "calc")
@XmlRootElement
public class KGLListEndoscopyDifferential implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "edID")
    private Integer _id;
    
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Division">
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "edDivision")
    private String _division = "";
    
    public String getDivision() {
        return _division;
    }

    public void setDivision(String division) {
        this._division = division;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ActivityKey">
    @Basic(optional = false)
    @NotNull
    @Size(max = 20)
    @Column(name = "edActivityKey")
    private String _activityKey = "";
    
    public String getActivityKey() {
        return _activityKey;
    }

    public void setActivityKey(String activityKey) {
        this._activityKey = activityKey;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformation">
    @JoinColumn(name = "edBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics _baseInformation;
    
    public DrgCalcBasics getBaseInformationID() {
        return _baseInformation;
    }

    public void setBaseInformationID(DrgCalcBasics baseInformation) {
        this._baseInformation = baseInformation;
    }
    // </editor-fold>

    public KGLListEndoscopyDifferential() {
    }

    public KGLListEndoscopyDifferential(Integer edID) {
        this._id = edID;
    }

    public KGLListEndoscopyDifferential(Integer edID, String edDivision, String edActivityKey) {
        this._id = edID;
        this._division = edDivision;
        this._activityKey = edActivityKey;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListEndoscopyDifferential)) {
            return false;
        }
        KGLListEndoscopyDifferential other = (KGLListEndoscopyDifferential) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListEndoscopyDifferential[ edID=" + _id + " ]";
    }
    
}
