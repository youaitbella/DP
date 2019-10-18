package org.inek.dataportal.common.data.KhComparison.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.common.helper.MathHelper;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "AEBPage_E1_1", schema = "psy")
public class AEBPageE1_1 extends AEBPage implements Serializable {

    private static final long serialVersionUID = 1L;

    public AEBPageE1_1() {
    }

    public AEBPageE1_1(AEBPageE1_1 page) {
        this._pepp = page.getPepp();
        this._compensationClass = page.getCompensationClass();
        this._caseCount = page.getCaseCount();
        this._calculationDays = page.getCalculationDays();
        this._valuationRadioDay = page.getValuationRadioDay();
        this._isOverlyer = page.isIsOverlyer();
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
    @JsonIgnore
    private AEBBaseInformation _baseInformation;
    @JsonIgnore
    public AEBBaseInformation getBaseInformation() {
        return _baseInformation;
    }
    @JsonIgnore
    public void setBaseInformation(AEBBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Pepp">
    @Column(name = "pePepp")
    private String _pepp = "";

    public String getPepp() {
        return _pepp;
    }

    public void setPepp(String pepp) {
        _pepp = pepp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CompensationClass">
    @Column(name = "peCompensationClass")
    private int _compensationClass;

    public int getCompensationClass() {
        return _compensationClass;
    }

    public void setCompensationClass(int compensationClass) {
        _compensationClass = compensationClass;
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

    //<editor-fold defaultstate="collapsed" desc="Property CalculationDays">
    @Column(name = "peCalculationDays")
    private int _calculationDays;

    public int getCalculationDays() {
        return _calculationDays;
    }

    public void setCalculationDays(int calculationDays) {
        _calculationDays = calculationDays;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ValuationRadioDay">
    @Column(name = "peValuationRadioDay")
    private double _valuationRadioDay;

    public double getValuationRadioDay() {
        return _valuationRadioDay;
    }

    public void setValuationRadioDay(double valuationRadioDay) {
        _valuationRadioDay = MathHelper.round(valuationRadioDay, 4);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property peIsOverlyer">
    @Column(name = "peIsOverlyer")
    private boolean _isOverlyer = false;

    public boolean isIsOverlyer() {
        return _isOverlyer;
    }

    public void setIsOverlyer(boolean isOverlyer) {
        this._isOverlyer = isOverlyer;
    }
    //</editor-fold>


    public double getSumValuationRadio() {
        return _calculationDays * _valuationRadioDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AEBPageE1_1 that = (AEBPageE1_1) o;
        return _compensationClass == that._compensationClass &&
                _caseCount == that._caseCount &&
                _calculationDays == that._calculationDays &&
                Double.compare(that._valuationRadioDay, _valuationRadioDay) == 0 &&
                _isOverlyer == that._isOverlyer &&
                Objects.equals(_id, that._id) &&
                Objects.equals(_baseInformation, that._baseInformation) &&
                Objects.equals(_pepp, that._pepp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _baseInformation, _pepp, _compensationClass, _caseCount, _calculationDays, _valuationRadioDay, _isOverlyer);
    }
}
