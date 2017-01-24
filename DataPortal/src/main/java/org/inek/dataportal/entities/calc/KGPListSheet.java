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
@Table(name = "KGPListSheet", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListSheet.findAll", query = "SELECT k FROM KGPListSheet k")})
public class KGPListSheet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "sID")
    private Integer sID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "sSheet")
    private String sSheet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "htSheetID")
    private List<KGPListHeaderText> kGPListHeaderTextList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doSheetID")
    private List<KGPDocuments> kGPDocumentsList;

    public KGPListSheet() {
    }

    public KGPListSheet(Integer sID) {
        this.sID = sID;
    }

    public KGPListSheet(Integer sID, String sSheet) {
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
    public List<KGPListHeaderText> getKGPListHeaderTextList() {
        return kGPListHeaderTextList;
    }

    public void setKGPListHeaderTextList(List<KGPListHeaderText> kGPListHeaderTextList) {
        this.kGPListHeaderTextList = kGPListHeaderTextList;
    }

    @XmlTransient
    public List<KGPDocuments> getKGPDocumentsList() {
        return kGPDocumentsList;
    }

    public void setKGPDocumentsList(List<KGPDocuments> kGPDocumentsList) {
        this.kGPDocumentsList = kGPDocumentsList;
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
        if (!(object instanceof KGPListSheet)) {
            return false;
        }
        KGPListSheet other = (KGPListSheet) object;
        if ((this.sID == null && other.sID != null) || (this.sID != null && !this.sID.equals(other.sID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListSheet[ sID=" + sID + " ]";
    }
    
}
