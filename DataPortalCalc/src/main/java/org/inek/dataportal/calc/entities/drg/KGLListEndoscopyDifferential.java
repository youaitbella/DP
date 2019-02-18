/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.drg;

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
@Table(name = "KGLListEndoscopyDifferential", schema = "calc")
public class KGLListEndoscopyDifferential implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "edID", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Division">
    @Column(name = "edDivision")
    @Documentation(name = "Bereich", rank = 10)
    private String _division = "";

    @Size(max = 100)
    public String getDivision() {
        return _division;
    }

    public void setDivision(String division) {
        this._division = division;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ActivityKey">
    @Column(name = "edActivityKey")
    @Documentation(name = "Leistungsschlüssel", rank = 20)
    private String _activityKey = "";

    @Size(max = 20)
    public String getActivityKey() {
        return _activityKey;
    }

    public void setActivityKey(String activityKey) {
        this._activityKey = activityKey;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformation">
    @Column(name = "edBaseInformationId")
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ActivityKey">
    @Column(name = "edActivityKeyComment")
    @Documentation(name = "Leistungsschlüssel Kommentar", rank = 30)
    private String _activityKeyComment = "";

    public String getActivityKeyComment() {
        return _activityKeyComment;
    }

    public void setActivityKeyComment(String activityKeyComment) {
        this._activityKeyComment = activityKeyComment;
    }
    // </editor-fold>

    public KGLListEndoscopyDifferential() {
    }

    public KGLListEndoscopyDifferential(Integer edID) {
        this._id = edID;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this._id;

        if (this._id != -1) {
            return hash;
        }

        hash = 41 * hash + Objects.hashCode(this._division);
        hash = 41 * hash + Objects.hashCode(this._activityKey);
        hash = 41 * hash + this._baseInformationId;
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
        final KGLListEndoscopyDifferential other = (KGLListEndoscopyDifferential) obj;

        if (this._id != -1 && this._id == other._id) {
            return true;
        }

        if (this._id != other._id) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (!Objects.equals(this._division, other._division)) {
            return false;
        }
        if (!Objects.equals(this._activityKey, other._activityKey)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListEndoscopyDifferential[ edID=" + _id + " ]";
    }
    //</editor-fold>
}
