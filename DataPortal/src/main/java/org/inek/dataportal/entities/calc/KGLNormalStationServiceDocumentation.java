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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLNormalStationServiceDocumentation", schema = "calc")
public class KGLNormalStationServiceDocumentation implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nssID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _used">
    @Column(name = "nssUsed")
    private boolean _used;

    public boolean isUsed() {
        return _used;
    }

    public void setUsed(boolean used) {
        this._used = used;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "nssBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "nssBaseInformationID")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _contentTextId">
//    @JoinColumn(name = "nssContentTextID", referencedColumnName = "ctID")
//    @ManyToOne(optional = false)
    @Column(name = "nssContentTextID")
    private int _contentTextId;

    public int getContentTextId() {
        return _contentTextId;
    }

    public void setContentTextId(int contentTextId) {
        this._contentTextId = contentTextId;
    }
    //</editor-fold>

    @Transient
    private String _label;

    public String getLabel() {
        return _label;
    }

    public void setLabel(String _label) {
        this._label = _label;
    }

    public KGLNormalStationServiceDocumentation() {
    }

    public KGLNormalStationServiceDocumentation(int nssID) {
        this._id = nssID;
    }

    public KGLNormalStationServiceDocumentation(int nssID, boolean nssUsed, String nssDepartment, String nssDepartmentKey, String nssAlternative) {
        this._id = nssID;
        this._used = nssUsed;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 59 * hash + (this._used ? 1 : 0);
        hash = 59 * hash + this._baseInformationId;
        hash = 59 * hash + this._contentTextId;
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
        final KGLNormalStationServiceDocumentation other = (KGLNormalStationServiceDocumentation) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._used != other._used) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (this._contentTextId != other._contentTextId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLNormalStationServiceDocumentation[ nssID=" + _id + " ]";
    }
    //</editor-fold>

}
