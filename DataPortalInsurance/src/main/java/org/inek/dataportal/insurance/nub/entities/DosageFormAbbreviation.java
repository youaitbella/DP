/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.insurance.nub.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listDosageFormAbbreviation")
public class DosageFormAbbreviation implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Property Text">
    @Id
    @Column(name = "dfaShort")
    private String _shortText = "";

    public String getShortText() {
        return _shortText;
    }

    public void setShortText(String shortText) {
        _shortText = shortText;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property DosageFormId">
    @Column(name = "dfaDosageformId")
    private int _dosageFormId = -1;

    public int getDosageFormId() {
        return _dosageFormId;
    }

    public void setDosageFormId(int value) {
        _dosageFormId = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this._shortText);
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
        final DosageFormAbbreviation other = (DosageFormAbbreviation) obj;
        if (!Objects.equals(this._shortText, other._shortText)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DosageFormAbbreviation{text=" + _shortText + '}';
    }
    // </editor-fold>

    
    
}
