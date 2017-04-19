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
import org.inek.dataportal.entities.iface.BaseIdValue;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListSheet", schema = "calc")
public class KGPListSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sID", updatable = false, nullable = false)
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _sheet">
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

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "htSheetID")
//    private List<KGPListHeaderText> kGPListHeaderTextList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doSheetID")
//    private List<KGPDocuments> kGPDocumentsList;
    public KGPListSheet() {
    }

    public KGPListSheet(int sID) {
        this._id = sID;
    }

    public KGPListSheet(int sID, String sSheet) {
        this._id = sID;
        this._sheet = sSheet;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 37 * hash + Objects.hashCode(this._sheet);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListSheet)) {
            return false;
        }
        final KGPListSheet other = (KGPListSheet) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        return Objects.equals(this._sheet, other._sheet);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListSheet[ sID=" + _id + " ]";
    }
    //</editor-fold>

}
