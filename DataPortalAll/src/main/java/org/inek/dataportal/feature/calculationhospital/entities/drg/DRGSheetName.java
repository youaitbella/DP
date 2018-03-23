/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital.entities.drg;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListSheet", schema = "calc")
@XmlRootElement
public class DRGSheetName implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property ID">
    @Id
    @Column(name = "sID", updatable = false, nullable = false)
    private int _id = -1;

    public int getID() {
        return _id;
    }

    public void setID(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Sheet">
    @Column(name = "sSheet")
    private String _sheet = "";

    @Size(max = 100)
    public String getSheet() {
        return _sheet;
    }

    public void setSheet(String sheet) {
        this._sheet = sheet;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DrgHeaderText">
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "htSheetId", referencedColumnName = "sId")
    private List<DrgHeaderText> _drgHeaderTextList;

    @XmlTransient
    public List<DrgHeaderText> getDrgHeaderTextList() {
        return _drgHeaderTextList;
    }

    public void setDrgHeaderTextList(List<DrgHeaderText> drgHeaderTextList) {
        this._drgHeaderTextList = drgHeaderTextList;
    }
    //</editor-fold>

    public DRGSheetName() {
    }

    public DRGSheetName(int sID) {
        this._id = sID;
    }

    public DRGSheetName(int sID, String sSheet) {
        this._id = sID;
        this._sheet = sSheet;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 73 * hash + Objects.hashCode(this._sheet);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DRGSheetName)) {
            return false;
        }
        final DRGSheetName other = (DRGSheetName) obj;
        if (_id != -1) {
            return _id == other._id;
        }
        if (_id != other._id) {
            return false;
        }
        return Objects.equals(this._sheet, other._sheet);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListSheet[ sID=" + _id + " sSheet=" + _sheet + " ]";
    }

}
