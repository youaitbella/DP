package org.inek.dataportal.common.data.KhComparison.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "AEBPage_B1", schema = "psy")
public class AEBPageB1 implements Serializable {

    private static final long serialVersionUID = 1L;

    public AEBPageB1() {
    }

    public AEBPageB1(AEBPageB1 page) {
        this._totalAgreementPeriod = page.getTotalAgreementPeriod();
        this._changedTotal = page.getChangedTotal();
        this._changedProceedsBudget = page.getChangedProceedsBudget();
        this._sumValuationRadioRenumeration = page.getSumValuationRadioRenumeration();
        this._sumEffectivValuationRadio = page.getSumEffectivValuationRadio();
        this._basisRenumerationValueCompensation = page.getBasisRenumerationValueCompensation();
        this._basisRenumerationValueNoCompensation = page.getBasisRenumerationValueNoCompensation();
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pbId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BaseInformation">
    @OneToOne
    @JoinColumn(name = "pbBaseInformationId")
    private AEBBaseInformation _baseInformation;

    public AEBBaseInformation getBaseInformation() {
        return _baseInformation;
    }

    public void setBaseInformation(AEBBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property TotalAgreementPeriod">
    @Column(name = "pbTotalAgreementPeriod")
    private double _totalAgreementPeriod;

    public double getTotalAgreementPeriod() {
        return _totalAgreementPeriod;
    }

    public void setTotalAgreementPeriod(double totalAgreementPeriod) {
        _totalAgreementPeriod = totalAgreementPeriod;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ChangedTotal">
    @Column(name = "pbChangedTotal")
    private double _changedTotal;

    public double getChangedTotal() {
        return _changedTotal;
    }

    public void setChangedTotal(double changedTotal) {
        _changedTotal = changedTotal;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ChangedProceedsBudget">
    @Column(name = "pbChangedProceedsBudget")
    private double _changedProceedsBudget;

    public double getChangedProceedsBudget() {
        return _changedProceedsBudget;
    }

    public void setChangedProceedsBudget(double changedProceedsBudget) {
        _changedProceedsBudget = changedProceedsBudget;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SumValuationRadioRenumeration">
    @Column(name = "pbSumValuationRadioRenumeration")
    private double _sumValuationRadioRenumeration;

    public double getSumValuationRadioRenumeration() {
        return _sumValuationRadioRenumeration;
    }

    public void setSumValuationRadioRenumeration(double sumValuationRadioRenumeration) {
        _sumValuationRadioRenumeration = sumValuationRadioRenumeration;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SumEffectivValuationRadio">
    @Column(name = "pbSumEffectivValuationRadio")
    private double _sumEffectivValuationRadio;

    public double getSumEffectivValuationRadio() {
        return _sumEffectivValuationRadio;
    }

    public void setSumEffectivValuationRadio(double sumEffectivValuationRadio) {
        _sumEffectivValuationRadio = sumEffectivValuationRadio;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property BasisRenumerationValueCompensation">
    @Column(name = "pbBasisRenumerationValueCompensation")
    private double _basisRenumerationValueCompensation;

    public double getBasisRenumerationValueCompensation() {
        return _basisRenumerationValueCompensation;
    }

    public void setBasisRenumerationValueCompensation(double basisRenumerationValueCompensation) {
        _basisRenumerationValueCompensation = basisRenumerationValueCompensation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property BasisRenumerationValueNoCompensation">
    @Column(name = "pbBasisRenumerationValueNoCompensation")
    private double _basisRenumerationValueNoCompensation;

    public double getBasisRenumerationValueNoCompensation() {
        return _basisRenumerationValueNoCompensation;
    }

    public void setBasisRenumerationValueNoCompensation(double basisRenumerationValueNoCompensation) {
        _basisRenumerationValueNoCompensation = basisRenumerationValueNoCompensation;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this._baseInformation);
        hash = 83 * hash + (int) (Double.doubleToLongBits(this._totalAgreementPeriod)
                ^ (Double.doubleToLongBits(this._totalAgreementPeriod) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this._changedTotal)
                ^ (Double.doubleToLongBits(this._changedTotal) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this._changedProceedsBudget)
                ^ (Double.doubleToLongBits(this._changedProceedsBudget) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this._sumValuationRadioRenumeration)
                ^ (Double.doubleToLongBits(this._sumValuationRadioRenumeration) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this._sumEffectivValuationRadio)
                ^ (Double.doubleToLongBits(this._sumEffectivValuationRadio) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this._basisRenumerationValueCompensation)
                ^ (Double.doubleToLongBits(this._basisRenumerationValueCompensation) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this._basisRenumerationValueNoCompensation)
                ^ (Double.doubleToLongBits(this._basisRenumerationValueNoCompensation) >>> 32));
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
        final AEBPageB1 other = (AEBPageB1) obj;
        if (Double.doubleToLongBits(this._totalAgreementPeriod)
                != Double.doubleToLongBits(other._totalAgreementPeriod)) {
            return false;
        }
        if (Double.doubleToLongBits(this._changedTotal)
                != Double.doubleToLongBits(other._changedTotal)) {
            return false;
        }
        if (Double.doubleToLongBits(this._changedProceedsBudget)
                != Double.doubleToLongBits(other._changedProceedsBudget)) {
            return false;
        }
        if (Double.doubleToLongBits(this._sumValuationRadioRenumeration)
                != Double.doubleToLongBits(other._sumValuationRadioRenumeration)) {
            return false;
        }
        if (Double.doubleToLongBits(this._sumEffectivValuationRadio)
                != Double.doubleToLongBits(other._sumEffectivValuationRadio)) {
            return false;
        }
        if (Double.doubleToLongBits(this._basisRenumerationValueCompensation)
                != Double.doubleToLongBits(other._basisRenumerationValueCompensation)) {
            return false;
        }
        if (Double.doubleToLongBits(this._basisRenumerationValueNoCompensation)
                != Double.doubleToLongBits(other._basisRenumerationValueNoCompensation)) {
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
