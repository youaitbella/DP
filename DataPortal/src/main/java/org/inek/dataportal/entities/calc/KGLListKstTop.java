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
@Table(name = "KGLListKstTop", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListKstTop.findAll", query = "SELECT k FROM KGLListKstTop k")
    , @NamedQuery(name = "KGLListKstTop.findByKtID", query = "SELECT k FROM KGLListKstTop k WHERE k.ktID = :ktID")
    , @NamedQuery(name = "KGLListKstTop.findByKtCostCenterID", query = "SELECT k FROM KGLListKstTop k WHERE k.ktCostCenterID = :ktCostCenterID")
    , @NamedQuery(name = "KGLListKstTop.findByKtText", query = "SELECT k FROM KGLListKstTop k WHERE k.ktText = :ktText")
    , @NamedQuery(name = "KGLListKstTop.findByKtCaseCnt", query = "SELECT k FROM KGLListKstTop k WHERE k.ktCaseCnt = :ktCaseCnt")
    , @NamedQuery(name = "KGLListKstTop.findByKtAmount", query = "SELECT k FROM KGLListKstTop k WHERE k.ktAmount = :ktAmount")
    , @NamedQuery(name = "KGLListKstTop.findByKtDelimitationAmount", query = "SELECT k FROM KGLListKstTop k WHERE k.ktDelimitationAmount = :ktDelimitationAmount")
    , @NamedQuery(name = "KGLListKstTop.findByKtRank", query = "SELECT k FROM KGLListKstTop k WHERE k.ktRank = :ktRank")})
public class KGLListKstTop implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ktID")
    private Integer ktID;
    
    public Integer getKtID() {
        return ktID;
    }

    public void setKtID(Integer ktID) {
        this.ktID = ktID;
    }

    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ktCostCenterID")
    private int ktCostCenterID;
    
    public int getKtCostCenterID() {
        return ktCostCenterID;
    }

    public void setKtCostCenterID(int ktCostCenterID) {
        this.ktCostCenterID = ktCostCenterID;
    }

    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ktText")
    private String ktText;
    
    public String getKtText() {
        return ktText;
    }

    public void setKtText(String ktText) {
        this.ktText = ktText;
    }

    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ktCaseCnt")
    private int ktCaseCnt;
    
    public int getKtCaseCnt() {
        return ktCaseCnt;
    }

    public void setKtCaseCnt(int ktCaseCnt) {
        this.ktCaseCnt = ktCaseCnt;
    }

    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "ktAmount")
    private BigDecimal ktAmount;
    
    public BigDecimal getKtAmount() {
        return ktAmount;
    }

    public void setKtAmount(BigDecimal ktAmount) {
        this.ktAmount = ktAmount;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ktDelimitationAmount")
    private BigDecimal ktDelimitationAmount;
    
    public BigDecimal getKtDelimitationAmount() {
        return ktDelimitationAmount;
    }

    public void setKtDelimitationAmount(BigDecimal ktDelimitationAmount) {
        this.ktDelimitationAmount = ktDelimitationAmount;
    }

    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ktRank")
    private int ktRank;
    
    public int getKtRank() {
        return ktRank;
    }

    public void setKtRank(int ktRank) {
        this.ktRank = ktRank;
    }

    
    @JoinColumn(name = "ktBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics ktBaseInformationID;
    
    public DrgCalcBasics getKtBaseInformationID() {
        return ktBaseInformationID;
    }

    public void setKtBaseInformationID(DrgCalcBasics ktBaseInformationID) {
        this.ktBaseInformationID = ktBaseInformationID;
    }
    

    public KGLListKstTop() {
    }

    public KGLListKstTop(Integer ktID) {
        this.ktID = ktID;
    }

    public KGLListKstTop(Integer ktID, int ktCostCenterID, String ktText, int ktCaseCnt, BigDecimal ktAmount, BigDecimal ktDelimitationAmount, int ktRank) {
        this.ktID = ktID;
        this.ktCostCenterID = ktCostCenterID;
        this.ktText = ktText;
        this.ktCaseCnt = ktCaseCnt;
        this.ktAmount = ktAmount;
        this.ktDelimitationAmount = ktDelimitationAmount;
        this.ktRank = ktRank;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ktID != null ? ktID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListKstTop)) {
            return false;
        }
        KGLListKstTop other = (KGLListKstTop) object;
        if ((this.ktID == null && other.ktID != null) || (this.ktID != null && !this.ktID.equals(other.ktID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListKstTop[ ktID=" + ktID + " ]";
    }
    
}
