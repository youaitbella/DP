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
 * @author vohldo/schlappajo
 */
@Entity
@Table(name = "AgreedPatients", schema = "mvh")
public class AgreedPatients implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "apId")
    private Integer _Id;
    
    @Column(name = "apFrom")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _from;
    
    @Column(name = "apTo")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _to;
    
    @Column(name = "apPatientsCount")
    private Integer _patientsCount;


    
    
    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Integer getId() {
        return _Id;
    }

    public void setId(Integer _Id) {
        this._Id = _Id;
    }
    
    
    public Date getPatientsFrom() {
        return _from;
    }

    public void setPatientsFrom(Date from) {
        this._from = from;
    }
        
    public Date getPatientsTo() {
        return _to;
    }

    public void setPatientsTo(Date to) {
        this._to = to;
    }
    
    public Integer getPatientsCount() {
        return _patientsCount;
    }

    public void setPatientsCount(Integer patientsCount) {
        this._patientsCount = patientsCount;
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
        if (!(object instanceof AgreedPatients)) {
            return false;
        }
        AgreedPatients other = (AgreedPatients) object;
        if ((_Id == null && other.getId()!= null) || (_Id != null && !_Id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.AgreedPatients[id=" + _Id + "]";
    }

    // </editor-fold>
}
