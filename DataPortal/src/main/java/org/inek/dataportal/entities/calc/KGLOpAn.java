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
@Table(name = "KGLOpAn", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLOpAn.findAll", query = "SELECT k FROM KGLOpAn k")
    , @NamedQuery(name = "KGLOpAn.findByOaID", query = "SELECT k FROM KGLOpAn k WHERE k.oaID = :oaID")
    , @NamedQuery(name = "KGLOpAn.findByOaHasCentralOP", query = "SELECT k FROM KGLOpAn k WHERE k.oaHasCentralOP = :oaHasCentralOP")
    , @NamedQuery(name = "KGLOpAn.findByOaCentralOPCnt", query = "SELECT k FROM KGLOpAn k WHERE k.oaCentralOPCnt = :oaCentralOPCnt")
    , @NamedQuery(name = "KGLOpAn.findByOaMedicalService", query = "SELECT k FROM KGLOpAn k WHERE k.oaMedicalService = :oaMedicalService")
    , @NamedQuery(name = "KGLOpAn.findByOaFunctionalService", query = "SELECT k FROM KGLOpAn k WHERE k.oaFunctionalService = :oaFunctionalService")
    , @NamedQuery(name = "KGLOpAn.findByOaDescription", query = "SELECT k FROM KGLOpAn k WHERE k.oaDescription = :oaDescription")
    , @NamedQuery(name = "KGLOpAn.findByOaMedicalServiceAmount", query = "SELECT k FROM KGLOpAn k WHERE k.oaMedicalServiceAmount = :oaMedicalServiceAmount")
    , @NamedQuery(name = "KGLOpAn.findByOaFunctionalServiceAmount", query = "SELECT k FROM KGLOpAn k WHERE k.oaFunctionalServiceAmount = :oaFunctionalServiceAmount")})
public class KGLOpAn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaID")
    private Integer oaID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaHasCentralOP")
    private boolean oaHasCentralOP;
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaCentralOPCnt")
    private int oaCentralOPCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaMedicalService")
    private boolean oaMedicalService;
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaFunctionalService")
    private boolean oaFunctionalService;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "oaDescription")
    private String oaDescription;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaMedicalServiceAmount")
    private BigDecimal oaMedicalServiceAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "oaFunctionalServiceAmount")
    private BigDecimal oaFunctionalServiceAmount;
    @JoinColumn(name = "oaBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics oaBaseInformationID;
    @JoinColumn(name = "oaContentTextID", referencedColumnName = "ctID")
    @ManyToOne(optional = false)
    private DrgContentText oaContentTextID;

    public KGLOpAn() {
    }

    public KGLOpAn(Integer oaID) {
        this.oaID = oaID;
    }

    public KGLOpAn(Integer oaID, boolean oaHasCentralOP, int oaCentralOPCnt, boolean oaMedicalService, boolean oaFunctionalService, String oaDescription, BigDecimal oaMedicalServiceAmount, BigDecimal oaFunctionalServiceAmount) {
        this.oaID = oaID;
        this.oaHasCentralOP = oaHasCentralOP;
        this.oaCentralOPCnt = oaCentralOPCnt;
        this.oaMedicalService = oaMedicalService;
        this.oaFunctionalService = oaFunctionalService;
        this.oaDescription = oaDescription;
        this.oaMedicalServiceAmount = oaMedicalServiceAmount;
        this.oaFunctionalServiceAmount = oaFunctionalServiceAmount;
    }

    public Integer getOaID() {
        return oaID;
    }

    public void setOaID(Integer oaID) {
        this.oaID = oaID;
    }

    public boolean getOaHasCentralOP() {
        return oaHasCentralOP;
    }

    public void setOaHasCentralOP(boolean oaHasCentralOP) {
        this.oaHasCentralOP = oaHasCentralOP;
    }

    public int getOaCentralOPCnt() {
        return oaCentralOPCnt;
    }

    public void setOaCentralOPCnt(int oaCentralOPCnt) {
        this.oaCentralOPCnt = oaCentralOPCnt;
    }

    public boolean getOaMedicalService() {
        return oaMedicalService;
    }

    public void setOaMedicalService(boolean oaMedicalService) {
        this.oaMedicalService = oaMedicalService;
    }

    public boolean getOaFunctionalService() {
        return oaFunctionalService;
    }

    public void setOaFunctionalService(boolean oaFunctionalService) {
        this.oaFunctionalService = oaFunctionalService;
    }

    public String getOaDescription() {
        return oaDescription;
    }

    public void setOaDescription(String oaDescription) {
        this.oaDescription = oaDescription;
    }

    public BigDecimal getOaMedicalServiceAmount() {
        return oaMedicalServiceAmount;
    }

    public void setOaMedicalServiceAmount(BigDecimal oaMedicalServiceAmount) {
        this.oaMedicalServiceAmount = oaMedicalServiceAmount;
    }

    public BigDecimal getOaFunctionalServiceAmount() {
        return oaFunctionalServiceAmount;
    }

    public void setOaFunctionalServiceAmount(BigDecimal oaFunctionalServiceAmount) {
        this.oaFunctionalServiceAmount = oaFunctionalServiceAmount;
    }

    public DrgCalcBasics getOaBaseInformationID() {
        return oaBaseInformationID;
    }

    public void setOaBaseInformationID(DrgCalcBasics oaBaseInformationID) {
        this.oaBaseInformationID = oaBaseInformationID;
    }

    public DrgContentText getOaContentTextID() {
        return oaContentTextID;
    }

    public void setOaContentTextID(DrgContentText oaContentTextID) {
        this.oaContentTextID = oaContentTextID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (oaID != null ? oaID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLOpAn)) {
            return false;
        }
        KGLOpAn other = (KGLOpAn) object;
        if ((this.oaID == null && other.oaID != null) || (this.oaID != null && !this.oaID.equals(other.oaID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLOpAn[ oaID=" + oaID + " ]";
    }
    
}
