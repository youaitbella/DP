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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "KGLNeonatData", schema = "calc")
public class DrgNeonatData implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ndId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CalcBasicsId">
    @Column(name = "ndBaseInformationID")
    private int _calcBasicsId;

    public int getCalcBasicsId() {
        return _calcBasicsId;
    }

    public void setCalcBasicsId(int calcBasicsId) {
        _calcBasicsId = calcBasicsId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ContentTextId">
    @Column(name = "ndContentTextId")
    private int _contentTextId;

    public int getContentTextId() {
        return _contentTextId;
    }

    public void setContentTextId(int contentTextId) {
        _contentTextId = contentTextId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ContentText">
    @OneToOne()
    @PrimaryKeyJoinColumn(name = "ndContentTextId")
    private DrgContentText _contentText;

    public DrgContentText getContentText() {
        return _contentText;
    }

    public void setContentText(DrgContentText contentText) {
        _contentText = contentText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Data">
    @Column(name = "ndData")
    private int _data = -1;  
    public int getData() {
        return _data;
    }

    public void setData(int data) {
        _data = data;
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
        final DrgNeonatData other = (DrgNeonatData) obj;
        return _id == other.getId();
    }

    @Override
    public String toString() {
        return "DrgNeonateData[ id=" + _id + " ]";
    }

    // </editor-fold>
}
