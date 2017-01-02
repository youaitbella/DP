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
@Table(name = "KGLListServiceProvisionType", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListServiceProvisionType.findAll", query = "SELECT k FROM KGLListServiceProvisionType k")
    , @NamedQuery(name = "KGLListServiceProvisionType.findBySptID", query = "SELECT k FROM KGLListServiceProvisionType k WHERE k.sptID = :sptID")
    , @NamedQuery(name = "KGLListServiceProvisionType.findBySptText", query = "SELECT k FROM KGLListServiceProvisionType k WHERE k.sptText = :sptText")
    , @NamedQuery(name = "KGLListServiceProvisionType.findBySptFirstYear", query = "SELECT k FROM KGLListServiceProvisionType k WHERE k.sptFirstYear = :sptFirstYear")
    , @NamedQuery(name = "KGLListServiceProvisionType.findBySptLastYear", query = "SELECT k FROM KGLListServiceProvisionType k WHERE k.sptLastYear = :sptLastYear")})
public class KGLListServiceProvisionType implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spServiceProvisionTypeID")
    private List<KGLListServiceProvision> kGLListServiceProvisionList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "sptID")
    private Integer sptID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "sptText")
    private String sptText;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sptFirstYear")
    private int sptFirstYear;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sptLastYear")
    private int sptLastYear;

    public KGLListServiceProvisionType() {
    }

    public KGLListServiceProvisionType(Integer sptID) {
        this.sptID = sptID;
    }

    public KGLListServiceProvisionType(Integer sptID, String sptText, int sptFirstYear, int sptLastYear) {
        this.sptID = sptID;
        this.sptText = sptText;
        this.sptFirstYear = sptFirstYear;
        this.sptLastYear = sptLastYear;
    }

    public Integer getSptID() {
        return sptID;
    }

    public void setSptID(Integer sptID) {
        this.sptID = sptID;
    }

    public String getSptText() {
        return sptText;
    }

    public void setSptText(String sptText) {
        this.sptText = sptText;
    }

    public int getSptFirstYear() {
        return sptFirstYear;
    }

    public void setSptFirstYear(int sptFirstYear) {
        this.sptFirstYear = sptFirstYear;
    }

    public int getSptLastYear() {
        return sptLastYear;
    }

    public void setSptLastYear(int sptLastYear) {
        this.sptLastYear = sptLastYear;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sptID != null ? sptID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListServiceProvisionType)) {
            return false;
        }
        KGLListServiceProvisionType other = (KGLListServiceProvisionType) object;
        if ((this.sptID == null && other.sptID != null) || (this.sptID != null && !this.sptID.equals(other.sptID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListServiceProvisionType[ sptID=" + sptID + " ]";
    }

    @XmlTransient
    public List<KGLListServiceProvision> getKGLListServiceProvisionList() {
        return kGLListServiceProvisionList;
    }

    public void setKGLListServiceProvisionList(List<KGLListServiceProvision> kGLListServiceProvisionList) {
        this.kGLListServiceProvisionList = kGLListServiceProvisionList;
    }
    
}
