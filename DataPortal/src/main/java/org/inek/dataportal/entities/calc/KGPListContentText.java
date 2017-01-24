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
@Table(name = "KGPListContentText", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListContentText.findAll", query = "SELECT k FROM KGPListContentText k")})
public class KGPListContentText implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctID")
    private Integer ctID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "ctText")
    private String ctText;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctFirstYear")
    private int ctFirstYear;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctLastYear")
    private int ctLastYear;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctDecimalCnt")
    private int ctDecimalCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctSeq")
    private int ctSeq;
    @JoinColumn(name = "ctHeaderTextID", referencedColumnName = "htID")
    @ManyToOne(optional = false)
    private KGPListHeaderText ctHeaderTextID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dfContentTextID")
    private List<KGPListDelimitationFact> kGPListDelimitationFactList;

    public KGPListContentText() {
    }

    public KGPListContentText(Integer ctID) {
        this.ctID = ctID;
    }

    public KGPListContentText(Integer ctID, String ctText, int ctFirstYear, int ctLastYear, int ctDecimalCnt, int ctSeq) {
        this.ctID = ctID;
        this.ctText = ctText;
        this.ctFirstYear = ctFirstYear;
        this.ctLastYear = ctLastYear;
        this.ctDecimalCnt = ctDecimalCnt;
        this.ctSeq = ctSeq;
    }

    public Integer getCtID() {
        return ctID;
    }

    public void setCtID(Integer ctID) {
        this.ctID = ctID;
    }

    public String getCtText() {
        return ctText;
    }

    public void setCtText(String ctText) {
        this.ctText = ctText;
    }

    public int getCtFirstYear() {
        return ctFirstYear;
    }

    public void setCtFirstYear(int ctFirstYear) {
        this.ctFirstYear = ctFirstYear;
    }

    public int getCtLastYear() {
        return ctLastYear;
    }

    public void setCtLastYear(int ctLastYear) {
        this.ctLastYear = ctLastYear;
    }

    public int getCtDecimalCnt() {
        return ctDecimalCnt;
    }

    public void setCtDecimalCnt(int ctDecimalCnt) {
        this.ctDecimalCnt = ctDecimalCnt;
    }

    public int getCtSeq() {
        return ctSeq;
    }

    public void setCtSeq(int ctSeq) {
        this.ctSeq = ctSeq;
    }

    public KGPListHeaderText getCtHeaderTextID() {
        return ctHeaderTextID;
    }

    public void setCtHeaderTextID(KGPListHeaderText ctHeaderTextID) {
        this.ctHeaderTextID = ctHeaderTextID;
    }

    @XmlTransient
    public List<KGPListDelimitationFact> getKGPListDelimitationFactList() {
        return kGPListDelimitationFactList;
    }

    public void setKGPListDelimitationFactList(List<KGPListDelimitationFact> kGPListDelimitationFactList) {
        this.kGPListDelimitationFactList = kGPListDelimitationFactList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ctID != null ? ctID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGPListContentText)) {
            return false;
        }
        KGPListContentText other = (KGPListContentText) object;
        if ((this.ctID == null && other.ctID != null) || (this.ctID != null && !this.ctID.equals(other.ctID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListContentText[ ctID=" + ctID + " ]";
    }
    
}
