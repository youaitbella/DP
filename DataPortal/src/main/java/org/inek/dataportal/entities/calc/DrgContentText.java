/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
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

    // <editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this._id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DrgContentText other = (DrgContentText) obj;
        return _id == other.getId();
    }

    @Override
    public String toString() {
        return "DrgContentText[ id=" + _id + " ]";
    }

    // </editor-fold>
}
