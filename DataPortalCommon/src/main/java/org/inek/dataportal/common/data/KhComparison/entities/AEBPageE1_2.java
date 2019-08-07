package org.inek.dataportal.common.data.KhComparison.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "AEBPage_E1_2", schema = "psy")
public class AEBPageE1_2 extends AEBPage implements Serializable {


    private static final long serialVersionUID = 1L;

    public AEBPageE1_2() {
    }

    public AEBPageE1_2(AEBPageE1_2 page) {
        this._et = page.getEt();
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
    private AEBBaseInformation _baseInformation;

    public AEBBaseInformation getBaseInformation() {
        return _baseInformation;
    }

    public void setBaseInformation(AEBBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ET">
    @Column(name = "peEt")
    private String _et = "";

    public String getEt() {
        return _et;
    }

    public void setEt(String et) {
        _et = et;
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
        _valuationRadioDay = (double) Math.round(valuationRadioDay * 10000d) / 10000d;
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
        AEBPageE1_2 that = (AEBPageE1_2) o;
        return _calculationDays == that._calculationDays &&
                Double.compare(that._valuationRadioDay, _valuationRadioDay) == 0 &&
                _isOverlyer == that._isOverlyer &&
                Objects.equals(_id, that._id) &&
                Objects.equals(_baseInformation, that._baseInformation) &&
                Objects.equals(_et, that._et);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _baseInformation, _et, _calculationDays, _valuationRadioDay, _isOverlyer);
    }
}
