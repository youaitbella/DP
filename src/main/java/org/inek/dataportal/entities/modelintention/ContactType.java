
package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ContactType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "lctId")
    private Long _id;
    
    @Column (name = "lctText")
    private String _text;
    
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        this._id = id;
    }
    
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
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
        if (!(object instanceof ContactType)) {
            return false;
        }
        ContactType other = (ContactType) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.modelintention.ContactType[ id=" + _id + " ]";
    }
    // </editor-fold>

}
