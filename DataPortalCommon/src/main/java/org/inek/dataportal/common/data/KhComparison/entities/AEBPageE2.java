package org.inek.dataportal.common.data.KhComparison.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "AEBPage_E2", schema = "psy")
public class AEBPageE2 extends AEBPage implements Serializable {

    private static final long serialVersionUID = 1L;

    public AEBPageE2() {
    }

    public AEBPageE2(AEBPageE2 page) {
        this._ze = page.getZe();
        this._zeCount = page.getZeCount();
        this._valuationRadioDay = page.getValuationRadioDay();
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

    //<editor-fold defaultstate="collapsed" desc="Property Ze">
    @Column(name = "peZe")
    private String _ze = "";

    public String getZe() {
        return _ze;
    }

    public void setZe(String ze) {
        _ze = ze;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ZeCount">
    @Column(name = "peZeCount")
    private int _zeCount;

    public int getZeCount() {
        return _zeCount;
    }

    public void setZeCount(int zeCount) {
        _zeCount = zeCount;
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
        return _zeCount * _valuationRadioDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AEBPageE2 aebPageE2 = (AEBPageE2) o;
        return _zeCount == aebPageE2._zeCount &&
                Double.compare(aebPageE2._valuationRadioDay, _valuationRadioDay) == 0 &&
                _isOverlyer == aebPageE2._isOverlyer &&
                Objects.equals(_id, aebPageE2._id) &&
                Objects.equals(_baseInformation, aebPageE2._baseInformation) &&
                Objects.equals(_ze, aebPageE2._ze);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _baseInformation, _ze, _zeCount, _valuationRadioDay, _isOverlyer);
    }
}
