/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListStationDepartment", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListStationDepartment.findAll", query = "SELECT k FROM KGPListStationDepartment k")})
public class KGPListStationDepartment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "sdID")
    private Integer sdID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "seDepartmentName")
    private String seDepartmentName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "seAlternative")
    private String seAlternative;
    @JoinColumn(name = "seBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private PeppCalcBasics seBaseInformationID;

    public KGPListStationDepartment() {
    }

    public KGPListStationDepartment(Integer sdID) {
        this.sdID = sdID;
    }

    public KGPListStationDepartment(Integer sdID, String seDepartmentName, String seAlternative) {
        this.sdID = sdID;
        this.seDepartmentName = seDepartmentName;
        this.seAlternative = seAlternative;
    }

    public Integer getSdID() {
        return sdID;
    }

    public void setSdID(Integer sdID) {
        this.sdID = sdID;
    }

    public String getSeDepartmentName() {
        return seDepartmentName;
    }

    public void setSeDepartmentName(String seDepartmentName) {
        this.seDepartmentName = seDepartmentName;
    }

    public String getSeAlternative() {
        return seAlternative;
    }

    public void setSeAlternative(String seAlternative) {
        this.seAlternative = seAlternative;
    }

    public PeppCalcBasics getSeBaseInformationID() {
        return seBaseInformationID;
    }

    public void setSeBaseInformationID(PeppCalcBasics seBaseInformationID) {
        this.seBaseInformationID = seBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sdID != null ? sdID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGPListStationDepartment)) {
            return false;
        }
        KGPListStationDepartment other = (KGPListStationDepartment) object;
        if ((this.sdID == null && other.sdID != null) || (this.sdID != null && !this.sdID.equals(other.sdID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListStationDepartment[ sdID=" + sdID + " ]";
    }
    
}
