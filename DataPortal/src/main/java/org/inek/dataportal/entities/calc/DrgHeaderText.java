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
@Table(name = "KGLListHeaderText", schema = "calc")
public class DrgHeaderText implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "htId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property SheetId">
    @Column(name = "htSheetId")
    private int _sheetId;
    public int getSheetId() {
        return _sheetId;
    }

    public void setSheetId(int sheetId) {
        _sheetId = sheetId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Type">
    @Column(name = "htType")
    private int _type;
    public int getType() {
        return _type;
    }

    public void setType(int type) {
        _type = type;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Sequence">
    @Column(name = "htSeq")
    private int _sequence;
    public int getSequence() {
        return _sequence;
    }

    public void setSequence(int sequence) {
        _sequence = sequence;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Text">
    @Column(name = "htText")
    private String _text;

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FirstYear">
    @Column(name = "htFirstYear")
    private int _firstYear;
    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int firstYear) {
        _firstYear = firstYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastYear">
    @Column(name = "htLastYear")
    private int _lastYear;
    public int getLastYear() {
        return _lastYear;
    }

    public void setLastYear(int lastYear) {
        _lastYear = lastYear;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this._id;
        
        if (this._id != -1) return hash;
        
        hash = 37 * hash + this._sheetId;
        hash = 37 * hash + this._type;
        hash = 37 * hash + this._sequence;
        hash = 37 * hash + Objects.hashCode(this._text);
        hash = 37 * hash + this._firstYear;
        hash = 37 * hash + this._lastYear;
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
        final DrgHeaderText other = (DrgHeaderText) obj;
        
        if (this._id != -1 && this._id == other._id) return true;
        
        if (this._id != other._id) {
            return false;
        }
        if (this._sheetId != other._sheetId) {
            return false;
        }
        if (this._type != other._type) {
            return false;
        }
        if (this._sequence != other._sequence) {
            return false;
        }
        if (this._firstYear != other._firstYear) {
            return false;
        }
        if (this._lastYear != other._lastYear) {
            return false;
        }
        return Objects.equals(this._text, other._text);
    }

//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 71 * hash + this._id;
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final DrgHeaderText other = (DrgHeaderText) obj;
//        return _id == other.getId();
//    }
    @Override
    public String toString() {
        return "DrgHeaderText[ id=" + _id + "; Sheet=" + _lastYear + "; Text=" + _text +" ]";
    }
    // </editor-fold>
    
}
