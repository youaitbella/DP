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
import org.inek.dataportal.entities.calc.iface.IdValue;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListProvidedType", schema = "calc")
public class KGPListProvidedType implements Serializable, IdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ptID")
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _text">
    @Size(max = 200)
    @Column(name = "ptText")
    private String _text = "";

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _firstYear">
    @Column(name = "ptFirstYear")
    private int _firstYear;

    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int firstYear) {
        this._firstYear = firstYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _lastYear">
    @Column(name = "ptLastYear")
    private int _lastYear;

    public int getLastYear() {
        return _lastYear;
    }

    public void setLastYear(int lastYear) {
        this._lastYear = lastYear;
    }
    //</editor-fold>

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spProvidedTypeID")
//    private List<KGPListServiceProvision> kGPListServiceProvisionList;
    public KGPListProvidedType() {
    }

    public KGPListProvidedType(int ptID) {
        this._id = ptID;
    }

    public KGPListProvidedType(int ptID, String ptText, int ptFirstYear, int ptLastYear) {
        this._id = ptID;
        this._text = ptText;
        this._firstYear = ptFirstYear;
        this._lastYear = ptLastYear;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 97 * hash + Objects.hashCode(this._text);
        hash = 97 * hash + this._firstYear;
        hash = 97 * hash + this._lastYear;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListProvidedType)) {
            return false;
        }
        final KGPListProvidedType other = (KGPListProvidedType) obj;
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
        return "org.inek.dataportal.entities.calc.KGPListProvidedType[ ptID=" + _id + " ]";
    }
    //</editor-fold>

}
