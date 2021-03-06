package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "KGLListLocation", schema = "calc")
public class KGLListLocation implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lID", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BaseInformationId">
    @Column(name = "lBaseInformationId")
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="location">
    @Column(name = "lLocation")
    @Documentation(name = "Name", rank = 10)
    private String _location = "";

    @Size(max = 300)
    public String getLocation() {
        return _location;
    }

    public void setLocation(String location) {
        this._location = location;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="locationNo">
    @Column(name = "lLocationNo")
    @Documentation(name = "Nummer", rank = 10)
    private int _locationNo;

    public int getLocationNo() {
        return _locationNo;
    }

    public void setLocationNo(int locationNo) {
        this._locationNo = locationNo;
    }
    //</editor-fold>

    public KGLListLocation() {
    }

    public KGLListLocation(Integer lID) {
        this._id = lID;
    }

    public KGLListLocation(Integer lID, int baseInformationId, String lLocation, short lLocationNo) {
        this._id = lID;
        this._baseInformationId = baseInformationId;
        this._location = lLocation;
        this._locationNo = lLocationNo;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 13 * hash + this._baseInformationId;
        hash = 13 * hash + Objects.hashCode(this._location);
        hash = 13 * hash + this._locationNo;
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
        final KGLListLocation other = (KGLListLocation) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._locationNo != other._locationNo) {
            return false;
        }
        return Objects.equals(this._location, other._location);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListLocation[ lID=" + _id + " ]";
    }
    //</editor-fold>
}
