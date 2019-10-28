package org.inek.dataportal.common.data.KhComparison.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "AEBPage_E3_1", schema = "psy")
public class AEBPageE3_1 extends AEBPage implements Serializable {

    private static final long serialVersionUID = 1L;

    public AEBPageE3_1() {
    }

    public AEBPageE3_1(AEBPageE3_1 page) {
        this._renumeration = page.getRenumeration();
        this._renumerationKey = page.getRenumerationKey();
        this._caseCount = page.getCaseCount();
        this._renumerationValue = page.getRenumerationValue();
        this._caseCountDeductions = page.getCaseCountDeductions();
        this._dayCountSurcharges = page.getDayCountSurcharges();
        this._caseCountSurcharges = page.getCaseCountSurcharges();
        this._dayCountDeductions = page.getDayCountDeductions();
        this._surchargesPerDay = page.getSurchargesPerDay();
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "peId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BaseInformation">
    @ManyToOne
    @JoinColumn(name = "peBaseInformationId")
    private AEBBaseInformation _baseInformation;

    @JsonIgnore
    public AEBBaseInformation getBaseInformation() {
        return _baseInformation;
    }

    @JsonIgnore
    public void setBaseInformation(AEBBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }

    //Using only for JSON Export
    public int getBaseInformationId() {
        return _baseInformation.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Renumeration">
    @Column(name = "peRenumeration")
    private String _renumeration = "";

    public String getRenumeration() {
        return _renumeration;
    }

    public void setRenumeration(String renumeration) {
        _renumeration = renumeration;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property RenumerationKey">
    @Column(name = "peRenumerationKey")
    private String _renumerationKey = "";

    public String getRenumerationKey() {
        return _renumerationKey;
    }

    public void setRenumerationKey(String renumerationKey) {
        _renumerationKey = renumerationKey;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CaseCount">
    @Column(name = "peCaseCount")
    private int _caseCount;

    public int getCaseCount() {
        return _caseCount;
    }

    public void setCaseCount(int caseCount) {
        _caseCount = caseCount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property RenumerationValue">
    @Column(name = "peRenumerationValue")
    private double _renumerationValue;

    public double getRenumerationValue() {
        return _renumerationValue;
    }

    public void setRenumerationValue(double renumerationValue) {
        _renumerationValue = renumerationValue;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CaseCountDeductions">
    @Column(name = "peCaseCountDeductions")
    private int _caseCountDeductions;

    public int getCaseCountDeductions() {
        return _caseCountDeductions;
    }

    public void setCaseCountDeductions(int caseCountDeductions) {
        _caseCountDeductions = caseCountDeductions;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DayCountDeductions">
    @Column(name = "peDayCountDeductions")
    private int _dayCountDeductions;

    public int getDayCountDeductions() {
        return _dayCountDeductions;
    }

    public void setDayCountDeductions(int dayCountDeductions) {
        _dayCountDeductions = dayCountDeductions;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DeductionPerDay">
    @Column(name = "peDeductionPerDay")
    private double _deductionPerDay;

    public double getDeductionPerDay() {
        return _deductionPerDay;
    }

    public void setDeductionPerDay(double deductionPerDay) {
        _deductionPerDay = deductionPerDay;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CaseCountSurcharges">
    @Column(name = "peCaseCountSurcharges")
    private int _caseCountSurcharges;

    public int getCaseCountSurcharges() {
        return _caseCountSurcharges;
    }

    public void setCaseCountSurcharges(int caseCountSurcharges) {
        _caseCountSurcharges = caseCountSurcharges;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DayCountSurcharges">
    @Column(name = "peDayCountSurcharges")
    private int _dayCountSurcharges;

    public int getDayCountSurcharges() {
        return _dayCountSurcharges;
    }

    public void setDayCountSurcharges(int dayCountSurcharges) {
        _dayCountSurcharges = dayCountSurcharges;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SurchargesPerDay">
    @Column(name = "peSurchargesPerDay")
    private double _surchargesPerDay;

    public double getSurchargesPerDay() {
        return _surchargesPerDay;
    }

    public void setSurchargesPerDay(double surchargesPerDay) {
        _surchargesPerDay = surchargesPerDay;
    }
    //</editor-fold>

    @JsonIgnore
    public double getSumBruttoClear() {
        return _caseCount * _renumerationValue;
    }

    @JsonIgnore
    public double getSumDeduction() {
        return _dayCountDeductions * _deductionPerDay;
    }

    @JsonIgnore
    public double getSumSurcharges() {
        return _dayCountSurcharges * _surchargesPerDay;
    }

    @JsonIgnore
    public double getSumNetto() {
        return getSumSurcharges() + getSumDeduction() + getSumBruttoClear();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this._baseInformation);
        hash = 79 * hash + Objects.hashCode(this._renumeration);
        hash = 79 * hash + Objects.hashCode(this._renumerationKey);
        hash = 79 * hash + this._caseCount;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this._renumerationValue) ^ (Double.doubleToLongBits(this._renumerationValue) >>> 32));
        hash = 79 * hash + this._caseCountDeductions;
        hash = 79 * hash + this._dayCountDeductions;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this._deductionPerDay) ^ (Double.doubleToLongBits(this._deductionPerDay) >>> 32));
        hash = 79 * hash + this._caseCountSurcharges;
        hash = 79 * hash + this._dayCountSurcharges;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this._surchargesPerDay) ^ (Double.doubleToLongBits(this._surchargesPerDay) >>> 32));
        return hash;
    }

    @SuppressWarnings("CyclomaticComplexity")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AEBPageE3_1 other = (AEBPageE3_1) obj;
        if (this._caseCount != other._caseCount) {
            return false;
        }
        if (Double.doubleToLongBits(this._renumerationValue) != Double.doubleToLongBits(other._renumerationValue)) {
            return false;
        }
        if (this._caseCountDeductions != other._caseCountDeductions) {
            return false;
        }
        if (this._dayCountDeductions != other._dayCountDeductions) {
            return false;
        }
        if (Double.doubleToLongBits(this._deductionPerDay) != Double.doubleToLongBits(other._deductionPerDay)) {
            return false;
        }
        if (this._caseCountSurcharges != other._caseCountSurcharges) {
            return false;
        }
        if (this._dayCountSurcharges != other._dayCountSurcharges) {
            return false;
        }
        if (Double.doubleToLongBits(this._surchargesPerDay) != Double.doubleToLongBits(other._surchargesPerDay)) {
            return false;
        }
        if (!Objects.equals(this._renumeration, other._renumeration)) {
            return false;
        }
        if (!Objects.equals(this._renumerationKey, other._renumerationKey)) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        if (!Objects.equals(this._baseInformation, other._baseInformation)) {
            return false;
        }
        return true;
    }

}
