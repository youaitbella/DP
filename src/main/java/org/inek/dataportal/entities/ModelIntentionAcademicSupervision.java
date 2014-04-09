/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author schlappajo
 */
@Entity
@Table(name = "AcademicSupervision", schema = "mvh")
public class ModelIntentionAcademicSupervision implements Serializable {/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "asId")
    private Integer _Id;
    
    @Column(name = "asRemitter")
    private String _Remitter;
    
    @Column(name = "asContractor")
    private String _Contractor;
        
    @Column(name = "asFrom")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _From;
   
    @Column(name = "asTo")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _To;
    
    
    
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Integer getId() {
        return _Id;
    }

    public void setId(Integer _Id) {
        this._Id = _Id;
    }

    public String getRemitter() {
        return _Remitter;
    }

    public void setRemitter(String _Remitter) {
        this._Remitter = _Remitter;
    }

    public String getContractor() {
        return _Contractor;
    }

    public void setContractor(String _Contractor) {
        this._Contractor = _Contractor;
    }

    public Date getFrom() {
        return _From;
    }

    public void setFrom(Date _From) {
        this._From = _From;
    }

    public Date getTo() {
        return _To;
    }

    public void setTo(Date _To) {
        this._To = _To;
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
        if (!(object instanceof ModelIntentionAcademicSupervision)) {
            return false;
        }
        ModelIntentionAcademicSupervision other = (ModelIntentionAcademicSupervision) object;
        if ((_Id == null && other.getId()!= null) || (_Id != null && !_Id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.AcademicSupervision[id=" + _Id + "]";
    }

    // </editor-fold>
    
    
}
