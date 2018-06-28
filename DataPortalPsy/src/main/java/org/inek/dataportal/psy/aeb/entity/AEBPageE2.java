package org.inek.dataportal.psy.aeb.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "AEBPage_E2", schema = "psy")
public class AEBPageE2 implements Serializable {

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
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this._baseInformation);
        hash = 89 * hash + Objects.hashCode(this._ze);
        hash = 89 * hash + this._zeCount;
        hash = 89 * hash + this._valuationRadioDay;
        hash = 89 * hash + this._sumValuationRadio;
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
        final AEBPageE2 other = (AEBPageE2) obj;
        if (this._zeCount != other._zeCount) {
            return false;
        }
        if (this._valuationRadioDay != other._valuationRadioDay) {
            return false;
        }
        if (this._sumValuationRadio != other._sumValuationRadio) {
            return false;
        }
        if (!Objects.equals(this._ze, other._ze)) {
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
