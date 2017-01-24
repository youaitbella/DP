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
@Table(name = "KGPListServiceProvision", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListServiceProvision.findAll", query = "SELECT k FROM KGPListServiceProvision k")})
public class KGPListServiceProvision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "spID")
    private Integer spID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "spPartitionExternalAssignment")
    private String spPartitionExternalAssignment;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "spNote")
    private String spNote;
    @Basic(optional = false)
    @NotNull
    @Column(name = "spAmount")
    private int spAmount;
    @JoinColumn(name = "spBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private PeppCalcBasics spBaseInformationID;
    @JoinColumn(name = "spProvidedTypeID", referencedColumnName = "ptID")
    @ManyToOne(optional = false)
    private KGPListProvidedType spProvidedTypeID;
    @JoinColumn(name = "spServiceProvisionTypeID", referencedColumnName = "sptID")
    @ManyToOne(optional = false)
    private KGPListServiceProvisionType spServiceProvisionTypeID;

    public KGPListServiceProvision() {
    }

    public KGPListServiceProvision(Integer spID) {
        this.spID = spID;
    }

    public KGPListServiceProvision(Integer spID, String spPartitionExternalAssignment, String spNote, int spAmount) {
        this.spID = spID;
        this.spPartitionExternalAssignment = spPartitionExternalAssignment;
        this.spNote = spNote;
        this.spAmount = spAmount;
    }

    public Integer getSpID() {
        return spID;
    }

    public void setSpID(Integer spID) {
        this.spID = spID;
    }

    public String getSpPartitionExternalAssignment() {
        return spPartitionExternalAssignment;
    }

    public void setSpPartitionExternalAssignment(String spPartitionExternalAssignment) {
        this.spPartitionExternalAssignment = spPartitionExternalAssignment;
    }

    public String getSpNote() {
        return spNote;
    }

    public void setSpNote(String spNote) {
        this.spNote = spNote;
    }

    public int getSpAmount() {
        return spAmount;
    }

    public void setSpAmount(int spAmount) {
        this.spAmount = spAmount;
    }

    public PeppCalcBasics getSpBaseInformationID() {
        return spBaseInformationID;
    }

    public void setSpBaseInformationID(PeppCalcBasics spBaseInformationID) {
        this.spBaseInformationID = spBaseInformationID;
    }

    public KGPListProvidedType getSpProvidedTypeID() {
        return spProvidedTypeID;
    }

    public void setSpProvidedTypeID(KGPListProvidedType spProvidedTypeID) {
        this.spProvidedTypeID = spProvidedTypeID;
    }

    public KGPListServiceProvisionType getSpServiceProvisionTypeID() {
        return spServiceProvisionTypeID;
    }

    public void setSpServiceProvisionTypeID(KGPListServiceProvisionType spServiceProvisionTypeID) {
        this.spServiceProvisionTypeID = spServiceProvisionTypeID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (spID != null ? spID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGPListServiceProvision)) {
            return false;
        }
        KGPListServiceProvision other = (KGPListServiceProvision) object;
        if ((this.spID == null && other.spID != null) || (this.spID != null && !this.spID.equals(other.spID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListServiceProvision[ spID=" + spID + " ]";
    }
    
}
