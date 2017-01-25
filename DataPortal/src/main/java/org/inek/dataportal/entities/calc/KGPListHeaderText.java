/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListHeaderText", schema = "calc")
@XmlRootElement
public class KGPListHeaderText implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "htID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _text">
    @Basic(optional = false)
    @NotNull
    @Size(max = 300)
    @Column(name = "htText")
    private String _text = "";

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _firstYear">
    @Basic(optional = false)
    @NotNull
    @Column(name = "htFirstYear")
    private int _firstYear;

    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int firstYear) {
        this._firstYear = firstYear;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _lastYear">
    @Basic(optional = false)
    @NotNull
    @Column(name = "htLastYear")
    private int _lastYear;

    public int getLastYear() {
        return _lastYear;
    }

    public void setLastYear(int lastYear) {
        this._lastYear = lastYear;
    }
    //</editor-fold>
    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ctHeaderTextID")
//    private List<KGPListContentText> kGPListContentTextList;
    
    //<editor-fold defaultstate="collapsed" desc="Property _sheetId">
//    @JoinColumn(name = "htSheetID", referencedColumnName = "sID")
//    @ManyToOne(optional = false)
    @Column(name = "htSheetID")
    private int _sheetId;

    public int getSheetId() {
        return _sheetId;
    }

    public void setSheetId(int sheetId) {
        this._sheetId = sheetId;
    }
    //</editor-fold>

    public KGPListHeaderText() {
    }

    public KGPListHeaderText(Integer htID) {
        this._id = htID;
    }

    public KGPListHeaderText(Integer htID, String htText, int htFirstYear, int htLastYear) {
        this._id = htID;
        this._text = htText;
        this._firstYear = htFirstYear;
        this._lastYear = htLastYear;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 23 * hash + Objects.hashCode(this._text);
        hash = 23 * hash + this._firstYear;
        hash = 23 * hash + this._lastYear;
        hash = 23 * hash + this._sheetId;
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListHeaderText)) {
            return false;
        }
        final KGPListHeaderText other = (KGPListHeaderText) obj;
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
        if (this._sheetId != other._sheetId) {
            return false;
        }
        if (!Objects.equals(this._text, other._text)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListHeaderText[ htID=" + _id + " ]";
    }
    //</editor-fold>
    
}
