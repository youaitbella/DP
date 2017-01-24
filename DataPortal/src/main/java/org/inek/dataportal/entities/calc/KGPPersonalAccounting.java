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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPPersonalAccounting", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPPersonalAccounting.findAll", query = "SELECT k FROM KGPPersonalAccounting k")})
public class KGPPersonalAccounting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "paID")
    private Integer paID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paCostTypeID")
    private int paCostTypeID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paStaffRecording")
    private boolean paStaffRecording;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paStaffEvaluation")
    private boolean paStaffEvaluation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paServiceEvaluation")
    private boolean paServiceEvaluation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paServiceStatistic")
    private boolean paServiceStatistic;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paExpertRating")
    private boolean paExpertRating;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paOther")
    private boolean paOther;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paAmount")
    private int paAmount;
    @JoinColumn(name = "paBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private PeppCalcBasics paBaseInformationID;

    public KGPPersonalAccounting() {
    }

    public KGPPersonalAccounting(Integer paID) {
        this.paID = paID;
    }

    public KGPPersonalAccounting(Integer paID, int paCostTypeID, boolean paStaffRecording, boolean paStaffEvaluation, boolean paServiceEvaluation, boolean paServiceStatistic, boolean paExpertRating, boolean paOther, int paAmount) {
        this.paID = paID;
        this.paCostTypeID = paCostTypeID;
        this.paStaffRecording = paStaffRecording;
        this.paStaffEvaluation = paStaffEvaluation;
        this.paServiceEvaluation = paServiceEvaluation;
        this.paServiceStatistic = paServiceStatistic;
        this.paExpertRating = paExpertRating;
        this.paOther = paOther;
        this.paAmount = paAmount;
    }

    public Integer getPaID() {
        return paID;
    }

    public void setPaID(Integer paID) {
        this.paID = paID;
    }

    public int getPaCostTypeID() {
        return paCostTypeID;
    }

    public void setPaCostTypeID(int paCostTypeID) {
        this.paCostTypeID = paCostTypeID;
    }

    public boolean getPaStaffRecording() {
        return paStaffRecording;
    }

    public void setPaStaffRecording(boolean paStaffRecording) {
        this.paStaffRecording = paStaffRecording;
    }

    public boolean getPaStaffEvaluation() {
        return paStaffEvaluation;
    }

    public void setPaStaffEvaluation(boolean paStaffEvaluation) {
        this.paStaffEvaluation = paStaffEvaluation;
    }

    public boolean getPaServiceEvaluation() {
        return paServiceEvaluation;
    }

    public void setPaServiceEvaluation(boolean paServiceEvaluation) {
        this.paServiceEvaluation = paServiceEvaluation;
    }

    public boolean getPaServiceStatistic() {
        return paServiceStatistic;
    }

    public void setPaServiceStatistic(boolean paServiceStatistic) {
        this.paServiceStatistic = paServiceStatistic;
    }

    public boolean getPaExpertRating() {
        return paExpertRating;
    }

    public void setPaExpertRating(boolean paExpertRating) {
        this.paExpertRating = paExpertRating;
    }

    public boolean getPaOther() {
        return paOther;
    }

    public void setPaOther(boolean paOther) {
        this.paOther = paOther;
    }

    public int getPaAmount() {
        return paAmount;
    }

    public void setPaAmount(int paAmount) {
        this.paAmount = paAmount;
    }

    public PeppCalcBasics getPaBaseInformationID() {
        return paBaseInformationID;
    }

    public void setPaBaseInformationID(PeppCalcBasics paBaseInformationID) {
        this.paBaseInformationID = paBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paID != null ? paID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGPPersonalAccounting)) {
            return false;
        }
        KGPPersonalAccounting other = (KGPPersonalAccounting) object;
        if ((this.paID == null && other.paID != null) || (this.paID != null && !this.paID.equals(other.paID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPPersonalAccounting[ paID=" + paID + " ]";
    }
    
}
