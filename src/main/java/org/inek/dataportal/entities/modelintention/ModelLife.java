/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;
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
public class ModelLife implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger _logger = Logger.getLogger("ModelLife");
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "mlId")
    private Integer _id;
    
    @Column (name = "mlModelIntentionId")
    private int _modelIntentionId;
    
    
    // <editor-fold defaultstate="collapsed" desc="startDate">
    @Column(name = "mlStartDate")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _startDate;
    
    public Date getStartDate() {
        return _startDate;
    }

    public void setStartDate(Date startDate) {
        _startDate = startDate;
    }
    // </editor-fold>
    
    @Column(name = "mlMonthDuration")
    private Integer _monthDuration = -1;

    
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
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
        if (!(object instanceof ModelLife)) {
            return false;
        }
        ModelLife other = (ModelLife) object;
        if ((_id == null && other.getId()!= null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return this._startDate.equals(other._startDate)
                && this._monthDuration.equals(other._monthDuration);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[id=" + _id + "]";
    }

    // </editor-fold>
}
