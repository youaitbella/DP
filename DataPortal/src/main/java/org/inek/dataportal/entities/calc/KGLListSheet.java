/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.List;
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
public class KGLListSheet implements Serializable {

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
    
    public KGLListSheet() {
    }

    public KGLListSheet(Integer sID) {
        this._id = sID;
    }

    public KGLListSheet(Integer sID, String sSheet) {
        this._id = sID;
        this._sheet = sSheet;
    }




    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListSheet)) {
            return false;
        }
        KGLListSheet other = (KGLListSheet) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListSheet[ sID=" + _id + " ]";
    }
    
}
