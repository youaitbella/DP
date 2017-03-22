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

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "KGLListContentText", schema = "calc")
public class DrgContentText implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ctId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property HeaderTextId">
    @Column(name = "ctHeaderTextId")
    private int _headerTextId;

    public int getHeaderTextId() {
        return _headerTextId;
    }

    public void setHeaderTextId(int contentHeaderId) {
        this._headerTextId = contentHeaderId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Text">
    @Column(name = "ctText")
    private String _text;

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FirstYear">
    @Column(name = "ctFirstYear")
    private int _firstYear;

    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int firstYear) {
        this._firstYear = firstYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastYear">
    @Column(name = "ctLastYear")
    private int _lastYear;

    public int getLastYear() {
        return _lastYear;
    }

    public void setLastYear(int lastYear) {
        this._lastYear = lastYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DecimalCount">
    @Column(name = "ctDecimalCnt")
    private int _decimalCount;

    public int getDecimalCount() {
        return _decimalCount;
    }

    public void setDecimalCount(int decimalCount) {
        this._decimalCount = decimalCount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Sequence">
    @Column(name = "ctseq")
    private int _sequence;

    public int getSequence() {
        return _sequence;
    }

    public void setSequence(int sequence) {
        this._sequence = sequence;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DiffAsPercent">
    @Column(name = "ctDiffAsPercent")
    private boolean _diffAsPercent;

    public boolean isDiffAsPercent() {
        return _diffAsPercent;
    }

    public void setDiffAsPercent(boolean diffAsPercent) {
        this._diffAsPercent = diffAsPercent;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InputRequired">
    @Column(name = "ctInputRequired")
    private boolean _inputRequired;

    public boolean isInputRequired() {
        return _inputRequired;
    }

    public void setInputRequired(boolean value) {
        this._inputRequired = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this._id;
        if (this._id != -1) return hash;
        
        hash = 83 * hash + this._headerTextId;
        hash = 83 * hash + Objects.hashCode(this._text);
        hash = 83 * hash + this._firstYear;
        hash = 83 * hash + this._lastYear;
        hash = 83 * hash + this._decimalCount;
        hash = 83 * hash + this._sequence;
        hash = 83 * hash + (this._diffAsPercent ? 1 : 0);
        return hash;
    }

    @Override    
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DrgContentText)) {
            return false;
        }
        final DrgContentText other = (DrgContentText) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._headerTextId != other._headerTextId) {
            return false;
        }
        if (this._firstYear != other._firstYear) {
            return false;
        }
        if (this._lastYear != other._lastYear) {
            return false;
        }
        if (this._decimalCount != other._decimalCount) {
            return false;
        }
        if (this._sequence != other._sequence) {
            return false;
        }
        if (this._diffAsPercent != other._diffAsPercent) {
            return false;
        }
        return Objects.equals(this._text, other._text);
    }

    @Override
    public String toString() {
        return _text;
    }
    // </editor-fold>
}
