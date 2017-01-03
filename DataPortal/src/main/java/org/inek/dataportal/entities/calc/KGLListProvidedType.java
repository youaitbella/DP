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
@Table(name = "KGLListProvidedType", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListProvidedType.findAll", query = "SELECT k FROM KGLListProvidedType k")
    , @NamedQuery(name = "KGLListProvidedType.findByPtID", query = "SELECT k FROM KGLListProvidedType k WHERE k._id = :ptID")
    , @NamedQuery(name = "KGLListProvidedType.findByPtText", query = "SELECT k FROM KGLListProvidedType k WHERE k._text = :ptText")
    , @NamedQuery(name = "KGLListProvidedType.findByPtFirstYear", query = "SELECT k FROM KGLListProvidedType k WHERE k._firstYear = :ptFirstYear")
    , @NamedQuery(name = "KGLListProvidedType.findByPtLastYear", query = "SELECT k FROM KGLListProvidedType k WHERE k._lastYear = :ptLastYear")})
public class KGLListProvidedType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ptID")
    private Integer _id;
    
    public Integer getId() {
        return _id;
    }
    
    public void setId(Integer id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="text">
    @Basic(optional = false)
    @NotNull
    @Size(max = 200)
    @Column(name = "ptText")
    private String _text = "";

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="firstYear">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ptFirstYear")
    private int _firstYear;

    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int firstYear) {
        this._firstYear = firstYear;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="lastYear">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ptLastYear")
    private int _lastYear;

    public int getLastYear() {
        return _lastYear;
    }

    public void setLastYear(int lastYear) {
        this._lastYear = lastYear;
    }
    //</editor-fold>
        
    public KGLListProvidedType() {
    }

    public KGLListProvidedType(Integer ptID) {
        this._id = ptID;
    }

    public KGLListProvidedType(Integer ptID, String ptText, int ptFirstYear, int ptLastYear) {
        this._id = ptID;
        this._text = ptText;
        this._firstYear = ptFirstYear;
        this._lastYear = ptLastYear;
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
        if (!(object instanceof KGLListProvidedType)) {
            return false;
        }
        KGLListProvidedType other = (KGLListProvidedType) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListProvidedType[ ptID=" + _id + " ]";
    }
    
}
