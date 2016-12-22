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
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "sID")
    private Integer sID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 100)
    @Column(name = "sSheet")
    private String sSheet = "";
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sId", referencedColumnName = "htSheetId")
    private List<DrgHeaderText> drgHeaderTextList;

    public KGLListSheet() {
    }

    public KGLListSheet(Integer sID) {
        this.sID = sID;
    }

    public KGLListSheet(Integer sID, String sSheet) {
        this.sID = sID;
        this.sSheet = sSheet;
    }

    public Integer getSID() {
        return sID;
    }

    public void setSID(Integer sID) {
        this.sID = sID;
    }

    public String getSSheet() {
        return sSheet;
    }

    public void setSSheet(String sSheet) {
        this.sSheet = sSheet;
    }

    @XmlTransient
    public List<DrgHeaderText> getDrgHeaderTextList() {
        return drgHeaderTextList;
    }

    public void setDrgHeaderTextList(List<DrgHeaderText> drgHeaderTextList) {
        this.drgHeaderTextList = drgHeaderTextList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sID != null ? sID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListSheet)) {
            return false;
        }
        KGLListSheet other = (KGLListSheet) object;
        if ((this.sID == null && other.sID != null) || (this.sID != null && !this.sID.equals(other.sID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListSheet[ sID=" + sID + " ]";
    }
    
}
