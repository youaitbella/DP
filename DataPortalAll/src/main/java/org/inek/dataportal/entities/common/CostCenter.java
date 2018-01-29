
package org.inek.dataportal.entities.common;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "listCostCenter", schema = "dbo")
public class CostCenter implements Serializable {
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @Column (name = "ccId")
    private Integer _id;
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CharId">
    @Column (name = "ccCharId")
    private String _charId;
    
    public String getCharId() {
        return _charId;
    }

    public void setCharId(String charId) {
        _charId = charId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Text">
    @Column (name = "ccText")
    private String _text;
    
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property IsDrg">
    @Column (name = "ccIsDrg")
    private boolean _isDrg;
    
    public boolean getIsDrg() {
        return _isDrg;
    }

    public void setIsDrg(boolean value) {
        _isDrg = value;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property IsPsy">
    @Column (name = "ccIsPsy")
    private boolean _isPsy;
    
    public boolean getIsPsy() {
        return _isPsy;
    }

    public void setIsPsy(boolean value) {
        _isPsy = value;
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
        if (!(object instanceof CostCenter)) {
            return false;
        }
        CostCenter other = (CostCenter) object;
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
