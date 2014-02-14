package org.inek.dataportal.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This entity is used to signal a cooperation request
 * It will be deleted after confirmation of the cooperation 
 * or after a defined amount of time, whichever occurs first
 * @author muellermi
 */
@Entity
@Table(name = "Announcement", schema = "adm")
public class Announcement implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anId")
    private Integer _id;
    
    @Column(name = "anActive")
    private boolean _isActive;
    
    @Column(name = "anWarning")
    private boolean _isWarning;

    @Column(name = "anText")
    private String _text;

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public boolean isActive() {
        return _isActive;
    }

    public void setActive(boolean isActive) {
        _isActive = isActive;
    }

    public boolean isWarning() {
        return _isWarning;
    }

    public void setWarning(boolean isWarning) {
        this._isWarning = isWarning;
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
        if (!(object instanceof Announcement)) {
            return false;
        }
        Announcement other = (Announcement) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.CooperationRequest[id=" + _id + "]";
    }
    // </editor-fold>

}
