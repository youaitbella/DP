package org.inek.dataportal.common.data.common;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import org.inek.dataportal.api.enums.PortalType;
import org.inek.dataportal.common.enums.Stage;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listPortalAddress")
public class PortalAddress implements Serializable {

    private static final long serialVersionUID = 1L;
    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @Column(name = "paId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property PortalType">
    @Column(name = "paPortalType")
    @Enumerated(EnumType.STRING)
    private PortalType _portalType;

    public PortalType getPortalType() {
        return _portalType;
    }

    public void setPortalType(PortalType portalType) {
        _portalType = portalType;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Stage">
    @Column(name = "paStage")
    @Enumerated(EnumType.STRING)
    private Stage _stage;

    public Stage getStage() {
        return _stage;
    }

    public void setStage(Stage stage) {
        _stage = stage;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property Url">
    @Column(name = "paUrl")
    private String _url = "";

    public String getUrl() {
        return _url;
    }

    public void setUrl(String url) {
        _url = url;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="hashCode, equals ">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this._id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PortalAddress other = (PortalAddress) obj;
        return this._id == other._id;
    }
    //</editor-fold>

}
