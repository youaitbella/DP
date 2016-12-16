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
@Table(name = "KGLListHeaderText", schema = "calc")
public class CalcBasicsHeadline implements Serializable {

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
        this._sheetId = sheetId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Hospital Name">
    @Column(name = "htText")
    private String _text;

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
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
        final StatementOfParticipance other = (StatementOfParticipance) obj;
        return _id == other.getId();
    }
    
    @Override
    public String toString() {
        return "Headline[ id=" + _id + "; Sheet=" + _sheetId + "; Text=" + _text +" ]";
    }
    
    // </editor-fold>
    
}
