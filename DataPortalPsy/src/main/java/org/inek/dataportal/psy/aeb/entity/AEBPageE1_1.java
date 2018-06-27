package org.inek.dataportal.psy.aeb.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "AEBPage_E1_1", schema = "psy")
public class AEBPageE1_1 implements Serializable {

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

    //<editor-fold defaultstate="collapsed" desc="Property Pepp">
    @Column(name = "pePepp")
    private String _pepp;

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
    private int _valuationRadioDay;

    public int getValuationRadioDay() {
        return _valuationRadioDay;
    }

    public void setValuationRadioDay(int valuationRadioDay) {
        _valuationRadioDay = valuationRadioDay;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SumValuationRadio">
    @Column(name = "peSumValuationRadio")
    private int _sumValuationRadio;

    public int getSumValuationRadio() {
        return _sumValuationRadio;
    }

    public void setSumValuationRadio(int sumValuationRadio) {
        _sumValuationRadio = sumValuationRadio;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this._baseInformation);
        hash = 11 * hash + Objects.hashCode(this._pepp);
        hash = 11 * hash + Objects.hashCode(this._compensationClass);
        hash = 11 * hash + this._caseCount;
        hash = 11 * hash + this._calculationDays;
        hash = 11 * hash + this._valuationRadioDay;
        hash = 11 * hash + this._sumValuationRadio;
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
        final AEBPageE1_1 other = (AEBPageE1_1) obj;
        if (this._caseCount != other._caseCount) {
            return false;
        }
        if (this._calculationDays != other._calculationDays) {
            return false;
        }
        if (this._valuationRadioDay != other._valuationRadioDay) {
            return false;
        }
        if (this._sumValuationRadio != other._sumValuationRadio) {
            return false;
        }
        if (!Objects.equals(this._pepp, other._pepp)) {
            return false;
        }
        if (!Objects.equals(this._compensationClass, other._compensationClass)) {
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
