/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.entities.calc.iface.IdValue;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListLocation", schema = "calc")
public class KGPListLocation implements Serializable, IdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lID")
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

    //<editor-fold defaultstate="collapsed" desc="BaseInformationID">
    @Column(name = "lBaseInformationID")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _location">
    @Size(max = 300)
    @Column(name = "lLocation")
    private String _location = "";

    public String getLocation() {
        return _location;
    }

    public void setLocation(String location) {
        this._location = location;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _locationNo">
    @Column(name = "lLocationNo")
    private short _locationNo;

    public short getLocationNo() {
        return _locationNo;
    }

    public void setLocationNo(short locationNo) {
        this._locationNo = locationNo;
    }
    //</editor-fold>

    public KGPListLocation() {
    }

    public KGPListLocation(Integer lID) {
        this._id = lID;
    }

    public KGPListLocation(Integer lID, String lLocation, short lLocationNo) {
        this._id = lID;
        this._location = lLocation;
        this._locationNo = lLocationNo;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 89 * hash + Objects.hashCode(this._location);
        hash = 89 * hash + this._locationNo;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListLocation)) {
            return false;
        }
        final KGPListLocation other = (KGPListLocation) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._locationNo != other._locationNo) {
            return false;
        }
        if (!Objects.equals(this._location, other._location)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListLocation[ lID=" + _id + " ]";
    }
    //</editor-fold>

}
