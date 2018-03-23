/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital.entities.psy;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListServiceProvisionType", schema = "calc")
public class KGPListServiceProvisionType implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sptID", updatable = false, nullable = false)
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _text">
    @Column(name = "sptText")
    @Documentation(name = "Text", rank = 10)
    private String _text = "";

    @Size(max = 200)
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _firstYear">
    @Column(name = "sptFirstYear")
    @Documentation(name = "Erstes Jahr", rank = 20)
    private int _firstYear;

    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int firstYear) {
        this._firstYear = firstYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _lastYear">
    @Column(name = "sptLastYear")
    @Documentation(name = "Letztes Jahr", rank = 30)
    private int _lastYear;

    public int getLastYear() {
        return _lastYear;
    }

    public void setLastYear(int lastYear) {
        this._lastYear = lastYear;
    }
    //</editor-fold>        

    //<editor-fold defaultstate="collapsed" desc="Sequence">
    @Column(name = "sptSequence")
    private int _sequence;

    public int getSequence() {
        return _sequence;
    }

    public void setSequence(int sequence) {
        this._sequence = sequence;
    }
    //</editor-fold>
    

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spServiceProvisionTypeId")
//    private List<KGPListServiceProvision> kGPListServiceProvisionList;
    public KGPListServiceProvisionType() {
    }

    public KGPListServiceProvisionType(int sptID) {
        this._id = sptID;
    }

    public KGPListServiceProvisionType(int sptID, String sptText, int sptFirstYear, int sptLastYear) {
        this._id = sptID;
        this._text = sptText;
        this._firstYear = sptFirstYear;
        this._lastYear = sptLastYear;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 47 * hash + Objects.hashCode(this._text);
        hash = 47 * hash + this._firstYear;
        hash = 47 * hash + this._lastYear;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListServiceProvisionType)) {
            return false;
        }
        final KGPListServiceProvisionType other = (KGPListServiceProvisionType) obj;
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
        return "org.inek.dataportal.entities.calc.KGPListServiceProvisionType[ sptID=" + _id + " ]";
    }
    //</editor-fold>

}
