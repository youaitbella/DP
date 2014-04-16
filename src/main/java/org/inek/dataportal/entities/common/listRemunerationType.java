
package org.inek.dataportal.entities.common;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "listRemunerationType", schema = "dbo")
public class listRemunerationType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column (name = "rtCharId")
    private String _id;
    
    @Column (name = "rtText")
    private String _text;
    
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }
    
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
        if (!(object instanceof listRemunerationType)) {
            return false;
        }
        listRemunerationType other = (listRemunerationType) object;
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
