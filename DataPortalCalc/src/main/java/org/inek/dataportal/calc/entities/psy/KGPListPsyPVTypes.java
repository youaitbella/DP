/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.psy;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListPsyPVTypes", schema = "calc")
public class KGPListPsyPVTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pptID", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _charId">
    @Column(name = "pptCharID")
    private String _charId = "";

    @Size(max = 5)
    public String getCharId() {
        return _charId;
    }

    public void setCharId(String charId) {
        this._charId = charId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _text">
    @Column(name = "pptText")
    private String _text = "";

    @Size(max = 50)
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sscMappingPsyPV")
//    private List<KGPListStationServiceCost> kGPListStationServiceCostList;
    public KGPListPsyPVTypes() {
    }

    public KGPListPsyPVTypes(int pptID) {
        this._id = pptID;
    }

    public KGPListPsyPVTypes(int pptID, String pptCharID, String pptText) {
        this._id = pptID;
        this._charId = pptCharID;
        this._text = pptText;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 41 * hash + Objects.hashCode(this._charId);
        hash = 41 * hash + Objects.hashCode(this._text);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListPsyPVTypes)) {
            return false;
        }
        final KGPListPsyPVTypes other = (KGPListPsyPVTypes) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (!Objects.equals(this._charId, other._charId)) {
            return false;
        }
        if (!Objects.equals(this._text, other._text)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListPsyPVTypes[ pptID=" + _id + " ]";
    }
    //</editor-fold>

}
