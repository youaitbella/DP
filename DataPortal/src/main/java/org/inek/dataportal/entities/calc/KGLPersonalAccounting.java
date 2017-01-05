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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLPersonalAccounting", schema = "calc")
@XmlRootElement
public class KGLPersonalAccounting implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "paID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _costTypeID">
    @Basic(optional = false)
    @NotNull
    @Column(name = "paCostTypeID")
    private int _costTypeID;

    public int getCostTypeID() {
        return _costTypeID;
    }

    public void setCostTypeID(int costTypeID) {
        this._costTypeID = costTypeID;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _staffRecording">
    @Basic(optional = false)
    @NotNull
    @Column(name = "paStaffRecording")
    private boolean _staffRecording;

    public boolean isStaffRecording() {
        return _staffRecording;
    }

    public void setStaffRecording(boolean staffRecording) {
        this._staffRecording = staffRecording;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _staffEvaluation">
    @Basic(optional = false)
    @NotNull
    @Column(name = "paStaffEvaluation")
    private boolean _staffEvaluation;

    public boolean isStaffEvaluation() {
        return _staffEvaluation;
    }

    public void setStaffEvaluation(boolean staffEvaluation) {
        this._staffEvaluation = staffEvaluation;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceEvaluation">
    @Basic(optional = false)
    @NotNull
    @Column(name = "paServiceEvaluation")
    private boolean _serviceEvaluation;

    public boolean isServiceEvaluation() {
        return _serviceEvaluation;
    }

    public void setServiceEvaluation(boolean serviceEvaluation) {
        this._serviceEvaluation = serviceEvaluation;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _serviceStatistic">
    @Basic(optional = false)
    @NotNull
    @Column(name = "paServiceStatistic")
    private boolean _serviceStatistic;

    public boolean isServiceStatistic() {
        return _serviceStatistic;
    }

    public void setServiceStatistic(boolean serviceStatistic) {
        this._serviceStatistic = serviceStatistic;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _expertRating">
    @Basic(optional = false)
    @NotNull
    @Column(name = "paExpertRating")
    private boolean _expertRating;

    public boolean isExpertRating() {
        return _expertRating;
    }

    public void setExpertRating(boolean expertRating) {
        this._expertRating = expertRating;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _other">
    @Basic(optional = false)
    @NotNull
    @Column(name = "paOther")
    private boolean _other;

    public boolean isOther() {
        return _other;
    }

    public void setOther(boolean other) {
        this._other = other;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _amount">
    @Basic(optional = false)
    @NotNull
    @Column(name = "paAmount")
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationID">
//    @JoinColumn(name = "paBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "paBaseInformationID")
    private int _baseInformationID;
    
    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    //</editor-fold>
    

    public KGLPersonalAccounting() {
    }

    public KGLPersonalAccounting(int paID) {
        this._id = paID;
    }

    public KGLPersonalAccounting(int paID, int paCostTypeID, boolean paStaffRecording, boolean paStaffEvaluation, boolean paServiceEvaluation, boolean paServiceStatistic, boolean paExpertRating, boolean paOther, int paAmount) {
        this._id = paID;
        this._costTypeID = paCostTypeID;
        this._staffRecording = paStaffRecording;
        this._staffEvaluation = paStaffEvaluation;
        this._serviceEvaluation = paServiceEvaluation;
        this._serviceStatistic = paServiceStatistic;
        this._expertRating = paExpertRating;
        this._other = paOther;
        this._amount = paAmount;
    }


    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLPersonalAccounting)) {
            return false;
        }
        KGLPersonalAccounting other = (KGLPersonalAccounting) object;
        return this._id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLPersonalAccounting[ paID=" + _id + " ]";
    }
    
}
