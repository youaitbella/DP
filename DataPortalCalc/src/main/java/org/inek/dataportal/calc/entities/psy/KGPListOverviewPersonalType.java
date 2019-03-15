/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.psy;

import org.inek.dataportal.common.data.common.CostType;
import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListOverviewPersonalType", schema = "calc")
@XmlRootElement
public class KGPListOverviewPersonalType implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="ID">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "optID", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Text">
    @Column(name = "optText")
    @Documentation(name = "Beschreibung", rank = 10)
    private String _text = "";

    @Size(max = 200)
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CostType">
    @ManyToOne
    @JoinColumn(name = "optCostTypeId")
    @Documentation(name = "Beschreibung", rank = 10)
    private CostType _costType;

    public CostType getCostType() {
        return _costType;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FirstYear">
    @Column(name = "optFirstYear")
    @Documentation(name = "Erstes Jahr", rank = 20)
    private int _firstYear;

    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int firstYear) {
        this._firstYear = firstYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LastYear">
    @Column(name = "optLastYear")
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
    @Column(name = "optSequence")
    @Documentation(name = "Sequence", rank = 30)
    private int _sequence;

    public int getSequence() {
        return _sequence;
    }

    public void setSequence(int sequence) {
        this._sequence = sequence;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 59 * hash + Objects.hashCode(this._text);
        hash = 59 * hash + this._firstYear;
        hash = 59 * hash + this._lastYear;
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
        final KGPListOverviewPersonalType other = (KGPListOverviewPersonalType) obj;
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
        if (!Objects.equals(this._text, other._text)) {
            return false;
        }
        return true;
    }
    //</editor-fold>
}
