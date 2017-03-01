/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.inek.dataportal.entities.calc.iface.IdValue;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPPersonalAccounting", schema = "calc")
public class KGPPersonalAccounting implements Serializable, IdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paID")
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costTypeID">
    @Column(name = "paCostTypeID")
    private int _costTypeId;

    public int getCostTypeId() {
        return _costTypeId;
    }

    public void setCostTypeId(int costTypeId) {
        this._costTypeId = costTypeId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _staffRecording">
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
    @Column(name = "paAmount")
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "paBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "paBaseInformationId")
    private int _baseInformationId = -1;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    @Transient
    private int priorCostAmount;

    public int getPriorCostAmount() {
        return priorCostAmount;
    }

    public void setPriorCostAmount(int priorCostAmount) {
        this.priorCostAmount = priorCostAmount;
    }

    public KGPPersonalAccounting() {
    }

    public KGPPersonalAccounting(int paID) {
        this._id = paID;
    }

    public KGPPersonalAccounting(int costTypeId, int prior) {
        this._costTypeId = costTypeId;
        this.priorCostAmount = prior;
    }

    public KGPPersonalAccounting(int paID, int paCostTypeID, boolean paStaffRecording, boolean paStaffEvaluation, boolean paServiceEvaluation, boolean paServiceStatistic, boolean paExpertRating, boolean paOther, int paAmount, int paBaseInfoID) {
        this._id = paID;
        this._costTypeId = paCostTypeID;
        this._staffRecording = paStaffRecording;
        this._staffEvaluation = paStaffEvaluation;
        this._serviceEvaluation = paServiceEvaluation;
        this._serviceStatistic = paServiceStatistic;
        this._expertRating = paExpertRating;
        this._other = paOther;
        this._amount = paAmount;
        this._baseInformationId = paBaseInfoID;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 97 * hash + this._costTypeId;
        hash = 97 * hash + (this._staffRecording ? 1 : 0);
        hash = 97 * hash + (this._staffEvaluation ? 1 : 0);
        hash = 97 * hash + (this._serviceEvaluation ? 1 : 0);
        hash = 97 * hash + (this._serviceStatistic ? 1 : 0);
        hash = 97 * hash + (this._expertRating ? 1 : 0);
        hash = 97 * hash + (this._other ? 1 : 0);
        hash = 97 * hash + this._amount;
        hash = 97 * hash + this._baseInformationId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPPersonalAccounting)) {
            return false;
        }
        final KGPPersonalAccounting other = (KGPPersonalAccounting) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._costTypeId != other._costTypeId) {
            return false;
        }
        if (this._staffRecording != other._staffRecording) {
            return false;
        }
        if (this._staffEvaluation != other._staffEvaluation) {
            return false;
        }
        if (this._serviceEvaluation != other._serviceEvaluation) {
            return false;
        }
        if (this._serviceStatistic != other._serviceStatistic) {
            return false;
        }
        if (this._expertRating != other._expertRating) {
            return false;
        }
        if (this._other != other._other) {
            return false;
        }
        if (this._amount != other._amount) {
            return false;
        }
        return this._baseInformationId == other._baseInformationId;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPPersonalAccounting[ paID=" + _id + " ]";
    }
    //</editor-fold>

}
