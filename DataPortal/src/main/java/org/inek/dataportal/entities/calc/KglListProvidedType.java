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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListProvidedType", schema = "calc")
@XmlRootElement
public class KglListProvidedType implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ptID", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="text">
    @Column(name = "ptText")
    private String _text = "";

    @Size(max = 200)
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="firstYear">
    @Column(name = "ptFirstYear")
    private int _firstYear;

    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int firstYear) {
        this._firstYear = firstYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="lastYear">
    @Column(name = "ptLastYear")
    private int _lastYear;

    public int getLastYear() {
        return _lastYear;
    }

    public void setLastYear(int lastYear) {
        this._lastYear = lastYear;
    }
    //</editor-fold>

    public KglListProvidedType() {
    }

    public KglListProvidedType(int ptID) {
        this._id = ptID;
    }

    public KglListProvidedType(int ptID, String ptText, int ptFirstYear, int ptLastYear) {
        this._id = ptID;
        this._text = ptText;
        this._firstYear = ptFirstYear;
        this._lastYear = ptLastYear;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this._id;

        if (this._id != -1) {
            return hash;
        }

        hash = 29 * hash + Objects.hashCode(this._text);
        hash = 29 * hash + this._firstYear;
        hash = 29 * hash + this._lastYear;
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
        final KglListProvidedType other = (KglListProvidedType) obj;

        if (this._id != -1 && this._id == other._id) {
            return true;
        }

        if (this._id != other._id) {
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

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListProvidedType[ ptID=" + _id + " ]";
    }
    //</editor-fold>

}
