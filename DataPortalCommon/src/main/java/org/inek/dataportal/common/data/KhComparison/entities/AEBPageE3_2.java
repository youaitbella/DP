package org.inek.dataportal.common.data.KhComparison.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "AEBPage_E3_2", schema = "psy")
public class AEBPageE3_2 implements Serializable {

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

    //<editor-fold defaultstate="collapsed" desc="Property ZE">
    @Column(name = "peZe")
    private String _ze = "";

    public String getZe() {
        return _ze;
    }

    public void setZe(String ze) {
        _ze = ze;
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

    //<editor-fold defaultstate="collapsed" desc="Property Count">
    @Column(name = "peCount")
    private int _count = 0;

    public int getCount() {
        return _count;
    }

    public void setCount(int count) {
        _count = count;
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

    //<editor-fold defaultstate="collapsed" desc="Property OPS">
    @Column(name = "peOps")
    private String _ops = "";

    public String getOps() {
        return _ops;
    }

    public void setOps(String ops) {
        _ops = ops;
    }
    //</editor-fold>

    public double getSumProceeds() {
        return _count * _renumerationValue;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this._baseInformation);
        hash = 79 * hash + Objects.hashCode(this._ze);
        hash = 79 * hash + Objects.hashCode(this._renumerationKey);
        hash = 79 * hash + this._count;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this._renumerationValue) ^ (Double.doubleToLongBits(this._renumerationValue) >>> 32));
        hash = 79 * hash + Objects.hashCode(this._ops);
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
        final AEBPageE3_2 other = (AEBPageE3_2) obj;
        if (this._count != other._count) {
            return false;
        }
        if (Double.doubleToLongBits(this._renumerationValue) != Double.doubleToLongBits(other._renumerationValue)) {
            return false;
        }
        if (!Objects.equals(this._ze, other._ze)) {
            return false;
        }
        if (!Objects.equals(this._renumerationKey, other._renumerationKey)) {
            return false;
        }
        if (!Objects.equals(this._ops, other._ops)) {
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
