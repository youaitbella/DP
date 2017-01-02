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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
@NamedQueries({
    @NamedQuery(name = "KGLListSheet.findAll", query = "SELECT k FROM KGLListSheet k")
    , @NamedQuery(name = "KGLListSheet.findBySID", query = "SELECT k FROM KGLListSheet k WHERE k.sID = :sID")
    , @NamedQuery(name = "KGLListSheet.findBySSheet", query = "SELECT k FROM KGLListSheet k WHERE k.sSheet = :sSheet")})
public class DRGSheetName implements Serializable {

    private static final long serialVersionUID = 1L;
//<editor-fold defaultstate="collapsed" desc="Property ID">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "sID")
    private Integer _id;
    
    public Integer getID() {
        return _id;
    }
    
    public void setID(Integer id) {
        this._id = id;
    }
//</editor-fold>
    
    
//<editor-fold defaultstate="collapsed" desc="Property Sheet">
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 100)
    @Column(name = "sSheet")
    private String _sheet = "";
    
    public String getSheet() {
        return _sheet;
    }
    
    public void setSheet(String sheet) {
        this._sheet = sheet;
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Property DrgHeaderText">
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sId", referencedColumnName = "htSheetId")
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

    public DRGSheetName(Integer sID) {
        this._id = sID;
    }

    public DRGSheetName(Integer sID, String sSheet) {
        this._id = sID;
        this._sheet = sSheet;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this._id);
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
        if (!Objects.equals(this._sheet, other._sheet)) {
            return false;
        }
        return Objects.equals(this._id, other._id);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListSheet[ sID=" + _id + " sSheet=" + _sheet + " ]";
    }
    
}
