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
@Table(name = "KGPListPsyPVTypes", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListPsyPVTypes.findAll", query = "SELECT k FROM KGPListPsyPVTypes k")})
public class KGPListPsyPVTypes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "pptID")
    private Integer pptID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "pptCharID")
    private String pptCharID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "pptText")
    private String pptText;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sscMappingPsyPV")
    private List<KGPListStationServiceCost> kGPListStationServiceCostList;

    public KGPListPsyPVTypes() {
    }

    public KGPListPsyPVTypes(Integer pptID) {
        this.pptID = pptID;
    }

    public KGPListPsyPVTypes(Integer pptID, String pptCharID, String pptText) {
        this.pptID = pptID;
        this.pptCharID = pptCharID;
        this.pptText = pptText;
    }

    public Integer getPptID() {
        return pptID;
    }

    public void setPptID(Integer pptID) {
        this.pptID = pptID;
    }

    public String getPptCharID() {
        return pptCharID;
    }

    public void setPptCharID(String pptCharID) {
        this.pptCharID = pptCharID;
    }

    public String getPptText() {
        return pptText;
    }

    public void setPptText(String pptText) {
        this.pptText = pptText;
    }

    @XmlTransient
    public List<KGPListStationServiceCost> getKGPListStationServiceCostList() {
        return kGPListStationServiceCostList;
    }

    public void setKGPListStationServiceCostList(List<KGPListStationServiceCost> kGPListStationServiceCostList) {
        this.kGPListStationServiceCostList = kGPListStationServiceCostList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pptID != null ? pptID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGPListPsyPVTypes)) {
            return false;
        }
        KGPListPsyPVTypes other = (KGPListPsyPVTypes) object;
        if ((this.pptID == null && other.pptID != null) || (this.pptID != null && !this.pptID.equals(other.pptID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListPsyPVTypes[ pptID=" + pptID + " ]";
    }
    
}
