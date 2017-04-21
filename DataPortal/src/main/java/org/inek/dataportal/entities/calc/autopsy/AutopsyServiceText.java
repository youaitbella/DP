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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "astId", updatable = false, nullable = false)
    private int id;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }
    //</editor-fold>
    
/*
    astId	int	Unchecked
astText	nvarchar(100)	Unchecked
astShowOnForensic	bit	Unchecked
    */    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AutopsyServiceText)) {
            return false;
        }
        AutopsyServiceText other = (AutopsyServiceText) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.autopsy.AutopsyServiceText[ id=" + id + " ]";
    }
    
}
