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
        _valuationRadioDay = valuationRadioDay;
    }
    //</editor-fold>

    public double getSumValuationRadio() {
        return _calculationDays * _valuationRadioDay;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this._baseInformation);
        hash = 67 * hash + Objects.hashCode(this._et);
        hash = 67 * hash + this._calculationDays;
        hash = 67 * hash + (int) (Double.doubleToLongBits(this._valuationRadioDay) ^ (Double.doubleToLongBits(this._valuationRadioDay) >>> 32));
        return hash;
    }

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
        final AEBPageE1_2 other = (AEBPageE1_2) obj;
        if (this._calculationDays != other._calculationDays) {
            return false;
        }
        if (Double.doubleToLongBits(this._valuationRadioDay) != Double.doubleToLongBits(other._valuationRadioDay)) {
            return false;
        }
        if (!Objects.equals(this._et, other._et)) {
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
