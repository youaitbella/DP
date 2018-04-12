package org.inek.dataportal.common.data.adm;

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
    
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anId")
    private Integer _id;
    
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Active">
    @Column(name = "anActive")
    private boolean _isActive;
    

    public boolean isActive() {
        return _isActive;
    }

    public void setActive(boolean isActive) {
        _isActive = isActive;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Warning">
    @Column(name = "anWarning")
    private boolean _isWarning;

    public boolean isWarning() {
        return _isWarning;
    }

    public void setWarning(boolean isWarning) {
        _isWarning = isWarning;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property PortalType">
    @Column(name = "anPortalType")
    private String _portalType;

    public String getPortalType() {
        return _portalType;
    }

    public void setPortalType(String portalType) {
        _portalType = portalType;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property ServerName">
    @Column(name = "anServerName")
    private String _serverName;

    public String getServerName() {
        return _serverName;
    }

    public void setServerName(String serverName) {
        this._serverName = serverName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Text">
    @Column(name = "anText")
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
