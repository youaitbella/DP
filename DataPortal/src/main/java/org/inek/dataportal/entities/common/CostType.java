
package org.inek.dataportal.entities.common;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "listCostType", schema = "dbo")
public class CostType implements Serializable {
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Column (name = "ctId")
    private Integer _id;
    
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="charId">
    @Column (name = "ctCharId")
    private String _charId;
    
    public String getCharId() {
        return _charId;
    }

    public void setCharId(String charId) {
        _charId = charId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="text">
    @Column (name = "ctText")
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
        if (!(object instanceof CostType)) {
            return false;
        }
        CostType other = (CostType) object;
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
