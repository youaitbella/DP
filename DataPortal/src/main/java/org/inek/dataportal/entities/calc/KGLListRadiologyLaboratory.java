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
@Table(name = "KGLListRadiologyLaboratory", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListRadiologyLaboratory.findAll", query = "SELECT k FROM KGLListRadiologyLaboratory k")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByRlID", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k.rlID = :rlID")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByRlCostCenterID", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k.rlCostCenterID = :rlCostCenterID")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByRlCostCenterText", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k.rlCostCenterText = :rlCostCenterText")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByRlServiceDocumentation", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k.rlServiceDocumentation = :rlServiceDocumentation")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByRlDescription", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k.rlDescription = :rlDescription")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByRlServiceVolumePre", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k.rlServiceVolumePre = :rlServiceVolumePre")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByRlAmountPre", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k.rlAmountPre = :rlAmountPre")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByRlServiceVolumePost", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k.rlServiceVolumePost = :rlServiceVolumePost")
    , @NamedQuery(name = "KGLListRadiologyLaboratory.findByRlAmountPost", query = "SELECT k FROM KGLListRadiologyLaboratory k WHERE k.rlAmountPost = :rlAmountPost")})
public class KGLListRadiologyLaboratory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlID")
    private Integer rlID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlCostCenterID")
    private int rlCostCenterID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "rlCostCenterText")
    private String rlCostCenterText;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "rlServiceDocumentation")
    private String rlServiceDocumentation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "rlDescription")
    private String rlDescription;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceVolumePre")
    private BigDecimal rlServiceVolumePre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlAmountPre")
    private BigDecimal rlAmountPre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceVolumePost")
    private BigDecimal rlServiceVolumePost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlAmountPost")
    private BigDecimal rlAmountPost;
    @JoinColumn(name = "rlBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics rlBaseInformationID;

    public KGLListRadiologyLaboratory() {
    }

    public KGLListRadiologyLaboratory(Integer rlID) {
        this.rlID = rlID;
    }

    public KGLListRadiologyLaboratory(Integer rlID, int rlCostCenterID, String rlCostCenterText, String rlServiceDocumentation, String rlDescription, BigDecimal rlServiceVolumePre, BigDecimal rlAmountPre, BigDecimal rlServiceVolumePost, BigDecimal rlAmountPost) {
        this.rlID = rlID;
        this.rlCostCenterID = rlCostCenterID;
        this.rlCostCenterText = rlCostCenterText;
        this.rlServiceDocumentation = rlServiceDocumentation;
        this.rlDescription = rlDescription;
        this.rlServiceVolumePre = rlServiceVolumePre;
        this.rlAmountPre = rlAmountPre;
        this.rlServiceVolumePost = rlServiceVolumePost;
        this.rlAmountPost = rlAmountPost;
    }

    public Integer getRlID() {
        return rlID;
    }

    public void setRlID(Integer rlID) {
        this.rlID = rlID;
    }

    public int getRlCostCenterID() {
        return rlCostCenterID;
    }

    public void setRlCostCenterID(int rlCostCenterID) {
        this.rlCostCenterID = rlCostCenterID;
    }

    public String getRlCostCenterText() {
        return rlCostCenterText;
    }

    public void setRlCostCenterText(String rlCostCenterText) {
        this.rlCostCenterText = rlCostCenterText;
    }

    public String getRlServiceDocumentation() {
        return rlServiceDocumentation;
    }

    public void setRlServiceDocumentation(String rlServiceDocumentation) {
        this.rlServiceDocumentation = rlServiceDocumentation;
    }

    public String getRlDescription() {
        return rlDescription;
    }

    public void setRlDescription(String rlDescription) {
        this.rlDescription = rlDescription;
    }

    public BigDecimal getRlServiceVolumePre() {
        return rlServiceVolumePre;
    }

    public void setRlServiceVolumePre(BigDecimal rlServiceVolumePre) {
        this.rlServiceVolumePre = rlServiceVolumePre;
    }

    public BigDecimal getRlAmountPre() {
        return rlAmountPre;
    }

    public void setRlAmountPre(BigDecimal rlAmountPre) {
        this.rlAmountPre = rlAmountPre;
    }

    public BigDecimal getRlServiceVolumePost() {
        return rlServiceVolumePost;
    }

    public void setRlServiceVolumePost(BigDecimal rlServiceVolumePost) {
        this.rlServiceVolumePost = rlServiceVolumePost;
    }

    public BigDecimal getRlAmountPost() {
        return rlAmountPost;
    }

    public void setRlAmountPost(BigDecimal rlAmountPost) {
        this.rlAmountPost = rlAmountPost;
    }

    public DrgCalcBasics getRlBaseInformationID() {
        return rlBaseInformationID;
    }

    public void setRlBaseInformationID(DrgCalcBasics rlBaseInformationID) {
        this.rlBaseInformationID = rlBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rlID != null ? rlID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListRadiologyLaboratory)) {
            return false;
        }
        KGLListRadiologyLaboratory other = (KGLListRadiologyLaboratory) object;
        if ((this.rlID == null && other.rlID != null) || (this.rlID != null && !this.rlID.equals(other.rlID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListRadiologyLaboratory[ rlID=" + rlID + " ]";
    }
    
}
