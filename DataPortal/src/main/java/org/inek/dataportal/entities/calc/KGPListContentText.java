/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctID")
    private Integer _id = -1;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
    @Basic(optional = false)
    @NotNull
    @Size(max = 300)
    @Column(name = "ctText")
    private String _text = "";

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctFirstYear")
    private int _firstYear;

    public int getFirstYear() {
        return _firstYear;
    }

    public void setFirstYear(int firstYear) {
        this._firstYear = firstYear;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctLastYear")
    private int _lastYear;

    public int getLastYear() {
        return _lastYear;
    }

    public void setLastYear(int lastYear) {
        this._lastYear = lastYear;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctDecimalCnt")
    private int _decimalCnt;

    public int getDecimalCnt() {
        return _decimalCnt;
    }

    public void setDecimalCnt(int decimalCnt) {
        this._decimalCnt = decimalCnt;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctSeq")
    private int _seq;

    public int getSeq() {
        return _seq;
    }

    public void setSeq(int seq) {
        this._seq = seq;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
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
    
    //<editor-fold defaultstate="collapsed" desc="Property ">
    @Column(name = "dfContentTextID")
    private int _contentTextId;

    public int getContentTextId() {
        return _contentTextId;
    }

    public void setContentTextId(int contentTextId) {
        this._contentTextId = contentTextId;
    }
    //</editor-fold>

    public KGPListContentText() {
    }

    public KGPListContentText(Integer ctID) {
        this._id = ctID;
    }

    public KGPListContentText(Integer ctID, String ctText, int ctFirstYear, int ctLastYear, int ctDecimalCnt, int ctSeq) {
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
        hash = 29 * hash + this._contentTextId;
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
        if (this._contentTextId != other._contentTextId) {
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
