package org.inek.dataportal.common.data.KhComparison.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "AEBPage_E3_3", schema = "psy")
public class AEBPageE3_3 extends AEBPage implements Serializable {

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

    //<editor-fold defaultstate="collapsed" desc="Property Days">
    @Column(name = "peDays")
    private int _days = 0;

    public int getDays() {
        return _days;
    }

    public void setDays(int days) {
        _days = days;
    }
    //</editor-fold>

    public double getSumProceeds() {
        return _days * _renumerationValue;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this._baseInformation);
        hash = 23 * hash + Objects.hashCode(this._renumeration);
        hash = 23 * hash + Objects.hashCode(this._renumerationKey);
        hash = 23 * hash + this._caseCount;
        hash = 23 * hash + (int) (Double.doubleToLongBits(this._renumerationValue) ^ (Double.doubleToLongBits(this._renumerationValue) >>> 32));
        hash = 23 * hash + this._days;
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
        final AEBPageE3_3 other = (AEBPageE3_3) obj;
        if (this._caseCount != other._caseCount) {
            return false;
        }
        if (Double.doubleToLongBits(this._renumerationValue) != Double.doubleToLongBits(other._renumerationValue)) {
            return false;
        }
        if (this._days != other._days) {
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
