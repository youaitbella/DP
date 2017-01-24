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
import javax.persistence.ManyToOne;
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
@Table(name = "KGPListHeaderText", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListHeaderText.findAll", query = "SELECT k FROM KGPListHeaderText k")})
public class KGPListHeaderText implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "htID")
    private Integer htID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "htText")
    private String htText;
    @Basic(optional = false)
    @NotNull
    @Column(name = "htFirstYear")
    private int htFirstYear;
    @Basic(optional = false)
    @NotNull
    @Column(name = "htLastYear")
    private int htLastYear;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ctHeaderTextID")
    private List<KGPListContentText> kGPListContentTextList;
    @JoinColumn(name = "htSheetID", referencedColumnName = "sID")
    @ManyToOne(optional = false)
    private KGPListSheet htSheetID;

    public KGPListHeaderText() {
    }

    public KGPListHeaderText(Integer htID) {
        this.htID = htID;
    }

    public KGPListHeaderText(Integer htID, String htText, int htFirstYear, int htLastYear) {
        this.htID = htID;
        this.htText = htText;
        this.htFirstYear = htFirstYear;
        this.htLastYear = htLastYear;
    }

    public Integer getHtID() {
        return htID;
    }

    public void setHtID(Integer htID) {
        this.htID = htID;
    }

    public String getHtText() {
        return htText;
    }

    public void setHtText(String htText) {
        this.htText = htText;
    }

    public int getHtFirstYear() {
        return htFirstYear;
    }

    public void setHtFirstYear(int htFirstYear) {
        this.htFirstYear = htFirstYear;
    }

    public int getHtLastYear() {
        return htLastYear;
    }

    public void setHtLastYear(int htLastYear) {
        this.htLastYear = htLastYear;
    }

    @XmlTransient
    public List<KGPListContentText> getKGPListContentTextList() {
        return kGPListContentTextList;
    }

    public void setKGPListContentTextList(List<KGPListContentText> kGPListContentTextList) {
        this.kGPListContentTextList = kGPListContentTextList;
    }

    public KGPListSheet getHtSheetID() {
        return htSheetID;
    }

    public void setHtSheetID(KGPListSheet htSheetID) {
        this.htSheetID = htSheetID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (htID != null ? htID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGPListHeaderText)) {
            return false;
        }
        KGPListHeaderText other = (KGPListHeaderText) object;
        if ((this.htID == null && other.htID != null) || (this.htID != null && !this.htID.equals(other.htID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListHeaderText[ htID=" + htID + " ]";
    }
    
}
