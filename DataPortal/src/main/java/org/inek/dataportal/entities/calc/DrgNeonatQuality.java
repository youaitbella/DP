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
@Table(name = "KGLNeonatQuality", schema = "calc")
public class DrgNeonatQuality implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nqId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CalcBasicsId">
    @Column(name = "nqBaseInformationID")
    private int _calcBasicsId;

    public int getCalcBasicsId() {
        return _calcBasicsId;
    }

    public void setCalcBasicsId(int calcBasicsId) {
        _calcBasicsId = calcBasicsId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ContentTextId">
    @Column(name = "nqContentTextId")
    private int _contentTextId;

    public int getContentTextId() {
        return _contentTextId;
    }

    public void setContentTextId(int contentTextId) {
        _contentTextId = contentTextId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Fullfilled">
    @Column(name = "nqFullfilled")
    private int _fullfilled = -1;  
    // -1 = not fullfilled, 0 = whole year fullfilled, 1 = fullfilled since Jan., 2 = fullfilled since Feb, ...
    public int getFullfilled() {
        return _fullfilled;
    }

    public void setFullfilled(int fullfilled) {
        _fullfilled = fullfilled;
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
        final DrgNeonatQuality other = (DrgNeonatQuality) obj;
        return _id == other.getId();
    }

    @Override
    public String toString() {
        return "DrgNeonatQuality[ id=" + _id + " ]";
    }

    // </editor-fold>
}
