/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListServiceProvisionType", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListServiceProvisionType.findAll", query = "SELECT k FROM KGLListServiceProvisionType k")
    , @NamedQuery(name = "KGLListServiceProvisionType.findBySptID", query = "SELECT k FROM KGLListServiceProvisionType k WHERE k._id = :sptID")
    , @NamedQuery(name = "KGLListServiceProvisionType.findBySptText", query = "SELECT k FROM KGLListServiceProvisionType k WHERE k._text = :sptText")
    , @NamedQuery(name = "KGLListServiceProvisionType.findBySptFirstYear", query = "SELECT k FROM KGLListServiceProvisionType k WHERE k._firstYear = :sptFirstYear")
    , @NamedQuery(name = "KGLListServiceProvisionType.findBySptLastYear", query = "SELECT k FROM KGLListServiceProvisionType k WHERE k._lastYear = :sptLastYear")})
public class KGLListServiceProvisionType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="ID">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "sptID")
    private Integer _id;
    
    public Integer getId() {
        return _id;
    }
    
    public void setId(Integer id) {
        this._id = id;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Text">
    @Basic(optional = false)
    @NotNull
    @Size(max = 200)
    @Column(name = "sptText")
    private String _text = "";

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="FirstYear">
    @Basic(optional = false)
    @NotNull
    @Column(name = "sptFirstYear")
    private int _firstYear;

    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int firstYear) {
        this._firstYear = firstYear;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="LastYear">
    @Basic(optional = false)
    @NotNull
    @Column(name = "sptLastYear")
    private int _lastYear;

    public int getLastYear() {
        return _lastYear;
    }

    public void setLastYear(int lastYear) {
        this._lastYear = lastYear;
    }
    //</editor-fold>
    
    

    public KGLListServiceProvisionType() {
    }

    public KGLListServiceProvisionType(Integer sptID) {
        this._id = sptID;
    }

    public KGLListServiceProvisionType(Integer sptID, String sptText, int sptFirstYear, int sptLastYear) {
        this._id = sptID;
        this._text = sptText;
        this._firstYear = sptFirstYear;
        this._lastYear = sptLastYear;
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
        if (!(object instanceof KGLListServiceProvisionType)) {
            return false;
        }
        KGLListServiceProvisionType other = (KGLListServiceProvisionType) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListServiceProvisionType[ sptID=" + _id + " ]";
    }
    
}
