/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.drg;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;
import org.inek.dataportal.common.utils.IgnoreOnCompare;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLNormalStationServiceDocumentation", schema = "calc")
public class KGLNormalStationServiceDocumentation implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nssID", updatable = false, nullable = false)
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

    //<editor-fold defaultstate="collapsed" desc="Property _used">
    @Column(name = "nssUsed")
    @Documentation(name = "Wert", rank = 2)
    private boolean _used;

    public boolean isUsed() {
        return _used;
    }

    public void setUsed(boolean used) {
        this._used = used;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "nssBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "nssBaseInformationId")
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

    // <editor-fold defaultstate="collapsed" desc="Property ContentText">
    @OneToOne
    @PrimaryKeyJoinColumn(name = "nssContentTextID")
    @IgnoreOnCompare
    private DrgContentText _contentText;

    public DrgContentText getContentText() {
        return _contentText;
    }

    public void setContentText(DrgContentText contentText) {
        _contentText = contentText;
        if (contentText != null) {
            _contentTextId = contentText.getId();
        }
    }

    @Documentation(name = "Bereich", rank = 1)
    public String getContentTextText() {
        return _contentText.getText();
    }
    // </editor-fold>

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
