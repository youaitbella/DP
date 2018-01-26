/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.autopsy;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listAutopsyServiceText", schema = "calc")
public class AutopsyServiceText implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "astId", updatable = false, nullable = false)
    private int _id;
    
    public int getId() {
        return _id;
    }
    
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property Text">
    @Column(name = "astText")
    private String _text;
    
    public String getText() {
        return _text;
    }
    
    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property ShowOnForensic">
    @Column(name = "astShowOnForensic")
    private boolean _showOnForensic;
    
    public boolean getShowOnForensic() {
        return _showOnForensic;
    }
    
    public void setShowOnForensic(boolean showOnForensic) {
        this._showOnForensic = showOnForensic;
    }
    //</editor-fold>
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) _id;
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the _showOnForensic fields are not set
        if (!(object instanceof AutopsyServiceText)) {
            return false;
        }
        AutopsyServiceText other = (AutopsyServiceText) object;
        return this._showOnForensic == other._showOnForensic;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.autopsy.AutopsyServiceText[ id=" + _showOnForensic + " ]";
    }
    
}
