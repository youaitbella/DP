
package org.inek.dataportal.psy.modelintention.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "listAdjustmentType", schema = "mvh")
public class AdjustmentType implements Serializable {
    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Column (name = "adtId")
    private Integer _id;
    
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="text">
    @Column (name = "adtText")
    private String _text;
    
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
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
        // TODO: Warning - this method won't work in the case the _id fields are not set
        if (!(object instanceof AdjustmentType)) {
            return false;
        }
        AdjustmentType other = (AdjustmentType) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }
    // </editor-fold>

}
