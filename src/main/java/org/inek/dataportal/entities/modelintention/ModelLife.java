/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author schlappajo
 */
@Entity
@Table(name = "ModelLife", schema = "mvh")
public class ModelLife implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger _logger = Logger.getLogger("ModelLife");
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "mlId")
    private Integer _id;
    
    @Column (name = "mlModelIntentionId")
    private int _modelIntentionId;
    
    
    @Column(name = "mlStartDate")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _startDate;
    
    @Column(name = "mlMonthDuration")
    private Integer _monthDuration = -1;

    
    @Transient
    private final String _uuid= UUID.randomUUID().toString().replace("-", "");
    
  
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Date getStartDate() {
//        String msg = " <<< getStartDate: " + (_startDate == null ? "null" : new SimpleDateFormat("dd.MM.yyyy").format(_startDate));
//        _logger.log(Level.WARNING, msg);
        return _startDate;
    }

    public void setStartDate(Date startDate) {
//        String msg = " >>> setStartDate: " + (_startDate == null ? "null" : new SimpleDateFormat("dd.MM.yyyy").format(_startDate));
//        _logger.log(Level.WARNING, msg);
        _startDate = startDate;
    }

    public Integer getMonthDuration() {
        return _monthDuration == -1 ? null : _monthDuration;
    }
    public void setMonthDuration(Integer monthDuration) {
        _monthDuration = monthDuration == null ? -1 : monthDuration;
    }

    public int getModelIntentionId() {
        return _modelIntentionId;
    }

    public void setModelIntentionId(int modelIntentionId) {
        _modelIntentionId = modelIntentionId;
    }
    
    public String getUUID(){
        return _uuid;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModelLife)) {
            return false;
        }
        ModelLife other = (ModelLife) object;
        if ((_id == null && other.getId()!= null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[id=" + _id + "]";
    }

    // </editor-fold>
}
