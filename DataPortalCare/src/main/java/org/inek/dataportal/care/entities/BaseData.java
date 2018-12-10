package org.inek.dataportal.care.entities;

import org.inek.dataportal.care.enums.Shift;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author lautenti
 */
@Entity
@Table(name = "PpugBaseData", schema = "care")
@Cacheable(true)
public class BaseData implements Serializable {

    private static final long serialVersionUID = 1L;

    public BaseData() {
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pbdId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property pbdSensitiveAreaId">
    @Column(name = "pbdSensitiveAreaId")
    private int _sensitiveAreaId;

    public int getSensitiveAreaId() {
        return _sensitiveAreaId;
    }

    public void setSensitiveAreaId(int sensitiveAreaId) {
        this._sensitiveAreaId = sensitiveAreaId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Shift">
    @Column(name = "pbdShift")
    private int _shift;

    public Shift getShift() {
        return Shift.getById(_shift);
    }

    public void setShift(Shift shift) {
        this._shift = shift.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property pbdPpug">
    @Column(name = "pbdPpug")
    private double _ppug;

    public double getPpug() {
        return _ppug;
    }

    public void setPpug(double ppug) {
        this._ppug = ppug;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property pbdPart">
    @Column(name = "pbdPart")
    private double _part;

    public double getPart() {
        return _part;
    }

    public void setPart(double part) {
        this._part = part;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property pbdValidFrom">
    @Column(name = "pbdValidFrom")
    private int _validFrom;

    public int getValidFrom() {
        return _validFrom;
    }

    public void setValidFrom(int validFrom) {
        this._validFrom = validFrom;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property pbdValidTo">
    @Column(name = "pbdValidTo")
    private int _validTo;

    public int getValidTo() {
        return _validTo;
    }

    public void setValidTo(int validTo) {
        this._validTo = validTo;
    }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseData baseData = (BaseData) o;
        return _sensitiveAreaId == baseData._sensitiveAreaId &&
                _shift == baseData._shift &&
                _ppug == baseData._ppug &&
                _part == baseData._part &&
                _validFrom == baseData._validFrom &&
                _validTo == baseData._validTo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_sensitiveAreaId, _shift, _ppug, _part, _validFrom, _validTo);
    }
}
