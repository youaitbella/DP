/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities.modelintention;

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
@Table(name = "ModelLife", schema = "mvh")
public class ModelIntentionModelLife implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "mlId")
    private Integer _Id;

    @Column(name = "mlStartDate")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _StartDate;
    
    @Column(name = "mlMonthDuration")
    private Integer _MonthDuration;
    
  
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Integer getId() {
        return _Id;
    }

    public void setId(Integer _Id) {
        this._Id = _Id;
    }

    public Date getStartDate() {
        return _StartDate;
    }

    public void setStartDate(Date _StartDate) {
        this._StartDate = _StartDate;
    }

    public Integer getMonthDuration() {
        return _MonthDuration;
    }

    public void setMonthDuration(Integer _MonthDuration) {
        this._MonthDuration = _MonthDuration;
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
        if (!(object instanceof ModelIntentionModelLife)) {
            return false;
        }
        ModelIntentionModelLife other = (ModelIntentionModelLife) object;
        if ((_Id == null && other.getId()!= null) || (_Id != null && !_Id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.ModelLife[id=" + _Id + "]";
    }

    // </editor-fold>
}
