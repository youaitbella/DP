/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author schlappajo
 */
@Entity
@Table(name = "Quality", schema = "mvh")
public class ModelIntentionQuality implements Serializable {/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "qyId")
    private Integer _Id;
    
    @Column(name = "qyType")
    private Integer _Type;
    
    @Column(name = "qyIndicator")
    private String _Indicator;
        
    @Column(name = "qyDescription")
    private String _Description;
    
    
    
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Integer getId() {
        return _Id;
    }

    public void setId(Integer _Id) {
        this._Id = _Id;
    }

    public Integer getType() {
        return _Type;
    }

    public void setType(Integer _Type) {
        this._Type = _Type;
    }

    public String getIndicator() {
        return _Indicator;
    }

    public void setIndicator(String _Indicator) {
        this._Indicator = _Indicator;
    }

    public String getDescription() {
        return _Description;
    }

    public void setDescription(String _Description) {
        this._Description = _Description;
    }
    
    // </editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_Id != null ? _Id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModelIntentionQuality)) {
            return false;
        }
        ModelIntentionQuality other = (ModelIntentionQuality) object;
        if ((_Id == null && other.getId()!= null) || (_Id != null && !_Id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.Quality[id=" + _Id + "]";
    }

    // </editor-fold>
    
    
}
