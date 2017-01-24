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
@Table(name = "KGPListProvidedType", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListProvidedType.findAll", query = "SELECT k FROM KGPListProvidedType k")})
public class KGPListProvidedType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ptID")
    private Integer ptID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "ptText")
    private String ptText;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ptFirstYear")
    private int ptFirstYear;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ptLastYear")
    private int ptLastYear;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spProvidedTypeID")
    private List<KGPListServiceProvision> kGPListServiceProvisionList;

    public KGPListProvidedType() {
    }

    public KGPListProvidedType(Integer ptID) {
        this.ptID = ptID;
    }

    public KGPListProvidedType(Integer ptID, String ptText, int ptFirstYear, int ptLastYear) {
        this.ptID = ptID;
        this.ptText = ptText;
        this.ptFirstYear = ptFirstYear;
        this.ptLastYear = ptLastYear;
    }

    public Integer getPtID() {
        return ptID;
    }

    public void setPtID(Integer ptID) {
        this.ptID = ptID;
    }

    public String getPtText() {
        return ptText;
    }

    public void setPtText(String ptText) {
        this.ptText = ptText;
    }

    public int getPtFirstYear() {
        return ptFirstYear;
    }

    public void setPtFirstYear(int ptFirstYear) {
        this.ptFirstYear = ptFirstYear;
    }

    public int getPtLastYear() {
        return ptLastYear;
    }

    public void setPtLastYear(int ptLastYear) {
        this.ptLastYear = ptLastYear;
    }

    @XmlTransient
    public List<KGPListServiceProvision> getKGPListServiceProvisionList() {
        return kGPListServiceProvisionList;
    }

    public void setKGPListServiceProvisionList(List<KGPListServiceProvision> kGPListServiceProvisionList) {
        this.kGPListServiceProvisionList = kGPListServiceProvisionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ptID != null ? ptID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGPListProvidedType)) {
            return false;
        }
        KGPListProvidedType other = (KGPListProvidedType) object;
        if ((this.ptID == null && other.ptID != null) || (this.ptID != null && !this.ptID.equals(other.ptID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListProvidedType[ ptID=" + ptID + " ]";
    }
    
}
