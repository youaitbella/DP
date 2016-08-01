
package org.inek.dataportal.entities.common;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "listRemunerationType", schema = "dbo")
public class RemunerationType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column (name = "rtCharId")
    private String _charId;
    
    @Column (name = "rtText")
    private String _text;
    
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public String getCharId() {
        return _charId;
    }

    public void setCharId(String charId) {
        _charId = charId;
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
        hash += (_charId != null ? _charId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the _charId fields are not set
        if (!(object instanceof RemunerationType)) {
            return false;
        }
        RemunerationType other = (RemunerationType) object;
        if ((this._charId == null && other._charId != null) || (this._charId != null && !this._charId.equals(other._charId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _charId + " ]";
    }
    // </editor-fold>

}
