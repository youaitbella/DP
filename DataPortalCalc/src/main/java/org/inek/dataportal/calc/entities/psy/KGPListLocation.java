/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.psy;

import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListLocation", schema = "calc")
public class KGPListLocation implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property id">
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

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Entlassender Standort">
    @Column(name = "lLocation")
    @Documentation(name = "Entlassender Standort", rank = 10)
    private String _location = "";

    @Size(max = 300)
    public String getLocation() {
        return _location;
    }

    public void setLocation(String location) {
        this._location = location;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Nummer">
    @Column(name = "lLocationNo")
    @Documentation(name = "Nummer", rank = 20)
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
