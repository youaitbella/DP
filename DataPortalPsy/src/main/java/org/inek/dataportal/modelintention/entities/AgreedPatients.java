/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.modelintention.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.inek.dataportal.common.utils.Documentation;

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
    @Column(name = "apId")
    private Integer _id;

    @Column(name = "apModelIntentionId")
    private int _modelIntentionId;

    // <editor-fold defaultstate="collapsed" desc="patientsFrom">
    @Column(name = "apFrom")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Documentation(key = "lblFrom", omitOnEmpty = true, dateFormat = "dd.MM.yyyy")
    private Date _from;

    public Date getPatientsFrom() {
        return _from;
    }

    public void setPatientsFrom(Date from) {
        _from = from;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="patientsTo">
    @Column(name = "apTo")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Documentation(key = "lblTo", omitOnEmpty = true, dateFormat = "dd.MM.yyyy")
    private Date _to;

    public Date getPatientsTo() {
        return _to;
    }

    public void setPatientsTo(Date to) {
        _to = to;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="PatientsCount">
    @Column(name = "apPatientsCount")
    @Documentation(key = "lblPatientsCount", omitOnEmpty = true)
    private int _patientsCount = -1;
    
    public Integer getPatientsCount() {
        return _patientsCount == -1 ? null : _patientsCount;
    }

    public void setPatientsCount(Integer patientsCount) {
        _patientsCount = patientsCount == null ? -1 : patientsCount;
    }
    // </editor-fold>
  
    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    

    public int getModelIntentionId() {
        return _modelIntentionId;
    }

    public void setModelIntentionId(int miId) {
        _modelIntentionId = miId;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgreedPatients)) {
            return false;
        }
        AgreedPatients other = (AgreedPatients) object;
        return Objects.equals(_id, other._id)
                && (_id != null
                || Objects.equals(_from, other._from)
                && Objects.equals(_to, other._to));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this._id);
        if (_id == null) {
            hash = 23 * hash + Objects.hashCode(this._from);
            hash = 23 * hash + Objects.hashCode(this._to);
        }
        return hash;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }

    // </editor-fold>
}
