/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "KGLListServiceProvision", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListServiceProvision.findAll", query = "SELECT k FROM KGLListServiceProvision k")
    , @NamedQuery(name = "KGLListServiceProvision.findBySpID", query = "SELECT k FROM KGLListServiceProvision k WHERE k.spID = :spID")
    , @NamedQuery(name = "KGLListServiceProvision.findBySpNotProvided", query = "SELECT k FROM KGLListServiceProvision k WHERE k.spNotProvided = :spNotProvided")
    , @NamedQuery(name = "KGLListServiceProvision.findBySpNoExternalAssignment", query = "SELECT k FROM KGLListServiceProvision k WHERE k.spNoExternalAssignment = :spNoExternalAssignment")
    , @NamedQuery(name = "KGLListServiceProvision.findBySpFullExternalAssignment", query = "SELECT k FROM KGLListServiceProvision k WHERE k.spFullExternalAssignment = :spFullExternalAssignment")
    , @NamedQuery(name = "KGLListServiceProvision.findBySpPartialExternalAssignment", query = "SELECT k FROM KGLListServiceProvision k WHERE k.spPartialExternalAssignment = :spPartialExternalAssignment")
    , @NamedQuery(name = "KGLListServiceProvision.findBySpPartitionExternalAssignment", query = "SELECT k FROM KGLListServiceProvision k WHERE k.spPartitionExternalAssignment = :spPartitionExternalAssignment")
    , @NamedQuery(name = "KGLListServiceProvision.findBySpNote", query = "SELECT k FROM KGLListServiceProvision k WHERE k.spNote = :spNote")
    , @NamedQuery(name = "KGLListServiceProvision.findBySpAmount", query = "SELECT k FROM KGLListServiceProvision k WHERE k.spAmount = :spAmount")})
public class KGLListServiceProvision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "spID")
    private Integer spID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "spNotProvided")
    private boolean spNotProvided;
    @Basic(optional = false)
    @NotNull
    @Column(name = "spNoExternalAssignment")
    private boolean spNoExternalAssignment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "spFullExternalAssignment")
    private boolean spFullExternalAssignment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "spPartialExternalAssignment")
    private boolean spPartialExternalAssignment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "spPartitionExternalAssignment")
    private boolean spPartitionExternalAssignment;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "spNote")
    private String spNote;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "spAmount")
    private BigDecimal spAmount;
    @JoinColumn(name = "spBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics spBaseInformationID;
    @JoinColumn(name = "spServiceProvisionTypeID", referencedColumnName = "sptID")
    @ManyToOne(optional = false)
    private KGLListServiceProvisionType spServiceProvisionTypeID;

    public KGLListServiceProvision() {
    }

    public KGLListServiceProvision(Integer spID) {
        this.spID = spID;
    }

    public KGLListServiceProvision(Integer spID, boolean spNotProvided, boolean spNoExternalAssignment, boolean spFullExternalAssignment, boolean spPartialExternalAssignment, boolean spPartitionExternalAssignment, String spNote, BigDecimal spAmount) {
        this.spID = spID;
        this.spNotProvided = spNotProvided;
        this.spNoExternalAssignment = spNoExternalAssignment;
        this.spFullExternalAssignment = spFullExternalAssignment;
        this.spPartialExternalAssignment = spPartialExternalAssignment;
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

    public boolean getSpNotProvided() {
        return spNotProvided;
    }

    public void setSpNotProvided(boolean spNotProvided) {
        this.spNotProvided = spNotProvided;
    }

    public boolean getSpNoExternalAssignment() {
        return spNoExternalAssignment;
    }

    public void setSpNoExternalAssignment(boolean spNoExternalAssignment) {
        this.spNoExternalAssignment = spNoExternalAssignment;
    }

    public boolean getSpFullExternalAssignment() {
        return spFullExternalAssignment;
    }

    public void setSpFullExternalAssignment(boolean spFullExternalAssignment) {
        this.spFullExternalAssignment = spFullExternalAssignment;
    }

    public boolean getSpPartialExternalAssignment() {
        return spPartialExternalAssignment;
    }

    public void setSpPartialExternalAssignment(boolean spPartialExternalAssignment) {
        this.spPartialExternalAssignment = spPartialExternalAssignment;
    }

    public boolean getSpPartitionExternalAssignment() {
        return spPartitionExternalAssignment;
    }

    public void setSpPartitionExternalAssignment(boolean spPartitionExternalAssignment) {
        this.spPartitionExternalAssignment = spPartitionExternalAssignment;
    }

    public String getSpNote() {
        return spNote;
    }

    public void setSpNote(String spNote) {
        this.spNote = spNote;
    }

    public BigDecimal getSpAmount() {
        return spAmount;
    }

    public void setSpAmount(BigDecimal spAmount) {
        this.spAmount = spAmount;
    }

    public DrgCalcBasics getSpBaseInformationID() {
        return spBaseInformationID;
    }

    public void setSpBaseInformationID(DrgCalcBasics spBaseInformationID) {
        this.spBaseInformationID = spBaseInformationID;
    }

    public KGLListServiceProvisionType getSpServiceProvisionTypeID() {
        return spServiceProvisionTypeID;
    }

    public void setSpServiceProvisionTypeID(KGLListServiceProvisionType spServiceProvisionTypeID) {
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
        if (!(object instanceof KGLListServiceProvision)) {
            return false;
        }
        KGLListServiceProvision other = (KGLListServiceProvision) object;
        if ((this.spID == null && other.spID != null) || (this.spID != null && !this.spID.equals(other.spID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListServiceProvision[ spID=" + spID + " ]";
    }
    
}
