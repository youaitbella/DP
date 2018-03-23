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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListContentText", schema = "calc")
@XmlRootElement
public class KGPListContentText implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ctID", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _text">
    @Column(name = "ctText")
    private String _text = "";

    @Size(max = 300)
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _firstYear">
    @Column(name = "ctFirstYear")
    private int _firstYear;

    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int firstYear) {
        this._firstYear = firstYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _lastYear">
    @Column(name = "ctLastYear")
    private int _lastYear;

    public int getLastYear() {
        return _lastYear;
    }

    public void setLastYear(int lastYear) {
        this._lastYear = lastYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _decimalCnt">
    @Column(name = "ctDecimalCnt")
    private int _decimalCnt;

    public int getDecimalCnt() {
        return _decimalCnt;
    }

    public void setDecimalCnt(int decimalCnt) {
        this._decimalCnt = decimalCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _seq">
    @Column(name = "ctSeq")
    private int _seq;

    public int getSeq() {
        return _seq;
    }

    public void setSeq(int seq) {
        this._seq = seq;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _headerTextID">
//    @JoinColumn(name = "ctHeaderTextID", referencedColumnName = "htID")
//    @ManyToOne(optional = false)
    @Column(name = "ctHeaderTextID")
    private int _headerTextID;

    public int getHeaderTextID() {
        return _headerTextID;
    }

    public void setHeaderTextID(int headerTextID) {
        this._headerTextID = headerTextID;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _diffAsPercent">
    @Column(name = "ctDiffAsPercent")
    private boolean _diffAsPercent;

    public boolean isDiffAsPercent() {
        return _diffAsPercent;
    }

    public void setDiffAsPercent(boolean diffAsPercent) {
        this._diffAsPercent = diffAsPercent;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InputRequired">
    @Column(name = "ctInputRequired")
    private boolean _inputRequired;

    public boolean isInputRequired() {
        return _inputRequired;
    }

    public void setInputRequired(boolean value) {
        this._inputRequired = value;
    }
    // </editor-fold>
    
    public KGPListContentText() {
    }

    public KGPListContentText(int ctID) {
        this._id = ctID;
    }

    public KGPListContentText(int ctID, String ctText, int ctFirstYear, int ctLastYear, int ctDecimalCnt, int ctSeq) {
        this._id = ctID;
        this._text = ctText;
        this._firstYear = ctFirstYear;
        this._lastYear = ctLastYear;
        this._decimalCnt = ctDecimalCnt;
        this._seq = ctSeq;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this._id);
        if (this._id != -1) {
            return hash;
        }
        hash = 29 * hash + Objects.hashCode(this._text);
        hash = 29 * hash + this._firstYear;
        hash = 29 * hash + this._lastYear;
        hash = 29 * hash + this._decimalCnt;
        hash = 29 * hash + this._seq;
        hash = 29 * hash + this._headerTextID;
        return hash;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
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
        final KGPListContentText other = (KGPListContentText) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._firstYear != other._firstYear) {
            return false;
        }
        if (this._lastYear != other._lastYear) {
            return false;
        }
        if (this._decimalCnt != other._decimalCnt) {
            return false;
        }
        if (this._seq != other._seq) {
            return false;
        }
        if (this._headerTextID != other._headerTextID) {
            return false;
        }
        if (!Objects.equals(this._text, other._text)) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListContentText[ ctID=" + _id + " ]";
    }
    //</editor-fold>

}
