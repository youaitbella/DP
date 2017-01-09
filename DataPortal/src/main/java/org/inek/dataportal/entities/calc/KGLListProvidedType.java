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
@Table(name = "KGLListProvidedType", schema = "calc")
@XmlRootElement
public class KGLListProvidedType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ptID")
    private int _id = -1;
    
    public int getId() {
        return _id;
    }
    
    public void setId(int id) {
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

    public KGLListProvidedType(int ptID) {
        this._id = ptID;
    }

    public KGLListProvidedType(int ptID, String ptText, int ptFirstYear, int ptLastYear) {
        this._id = ptID;
        this._text = ptText;
        this._firstYear = ptFirstYear;
        this._lastYear = ptLastYear;
    }


    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListProvidedType)) {
            return false;
        }
        KGLListProvidedType other = (KGLListProvidedType) object;
        return this._id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListProvidedType[ ptID=" + _id + " ]";
    }
    
}
