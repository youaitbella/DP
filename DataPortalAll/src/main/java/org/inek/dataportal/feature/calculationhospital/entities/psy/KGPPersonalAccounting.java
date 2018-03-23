/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital.entities.psy;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.inek.dataportal.common.data.common.CostType;
import org.inek.dataportal.common.utils.Documentation;
import org.inek.dataportal.common.data.iface.BaseIdValue;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPPersonalAccounting", schema = "calc")
public class KGPPersonalAccounting implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paID", updatable = false, nullable = false)
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

    //<editor-fold defaultstate="collapsed" desc="Property _costTypeId">
    @Documentation(name = "Kostenartengruppe", rank = 10)
    @Column(name = "paCostTypeID")
    private int _costTypeId;    

    public int getCostTypeId() {
        return _costType.getId();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostType">
    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn(name = "paCostTypeID")
    private CostType _costType;

    public CostType getCostType() {
        return _costType;
    }

    public void setCostType(CostType costType) {
        _costType = costType;
        _costTypeId = costType.getId();
    }

    @Documentation(name = "Kostenartengruppe", rank = 10)
    public String getCostTypeText() {
        if (_costType == null) {
            return "";
        }
        return _costType.getText();
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _staffRecording">
    @Column(name = "paStaffRecording")
    @Documentation(name = "Mitarbeiterbezogene Zeiterfassung", rank = 20)
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
    @Documentation(name = "Stellenplanauswertung", rank = 30)
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
    @Documentation(name = "Stellenplanauswertung", rank = 40)
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
    @Documentation(name = "Leistungsstatistiken", rank = 50)
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
    @Documentation(name = "Expertenschätzung", rank = 60)
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
    @Documentation(name = "Sonstige", rank = 70)
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
    @Documentation(name = "Kostenvolumen", rank = 80)
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

    public KGPPersonalAccounting(CostType costType, int prior) {
        setCostType(costType);
        priorCostAmount = prior;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 97 * hash + this._costType.getId();
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
    @SuppressWarnings("CyclomaticComplexity")
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
        if (this._costType.getId() != other._costType.getId()) {
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
