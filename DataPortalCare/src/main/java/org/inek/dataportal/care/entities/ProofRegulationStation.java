package org.inek.dataportal.care.entities;

import org.inek.dataportal.care.enums.SensitiveArea;
import org.inek.dataportal.care.enums.Shift;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author lautenti
 */
@Entity
@Table(name = "ProofRegulationStation", schema = "care")
@Cacheable(true)
public class ProofRegulationStation implements Serializable {

    private static final long serialVersionUID = 1L;

    public ProofRegulationStation() {
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prsId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property prsSensitiveAreaIds">
    @Column(name = "prsSensitiveAreaId")
    private int _sensitiveAreaId;

    public SensitiveArea getSensitiveArea() {
        return SensitiveArea.getById(_sensitiveAreaId);
    }

    public void setSensitiveArea(SensitiveArea sensitiveArea) {
        this._sensitiveAreaId = sensitiveArea.getId();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property prsIk">
    @Column(name = "prsIk")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property prsYear">
    @Column(name = "prsYear")
    private int _year;

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        this._year = year;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property prsFabNumber">
    @Column(name = "prsFabNumber")
    private String _fabNumber;

    public String getFabNumber() {
        return _fabNumber;
    }

    public void setFabNumber(String fabNumber) {
        this._fabNumber = fabNumber;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property prsFabName">
    @Column(name = "prsFabName")
    private String _fabName;

    public String getFabName() {
        return _fabName;
    }

    public void setFabName(String fabName) {
        this._fabName = fabName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property prsStationName">
    @Column(name = "prsStationName")
    private String _stationName;

    public String getStationName() {
        return _stationName;
    }

    public void setStationName(String stationName) {
        this._stationName = stationName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property prsLocationCode">
    @Column(name = "prsLocationCode")
    private String _locationCode;

    public String getLocationCode() {
        return _locationCode;
    }

    public void setLocationCode(String stationName) {
        this._locationCode = stationName;
    }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProofRegulationStation that = (ProofRegulationStation) o;
        return _sensitiveAreaId == that._sensitiveAreaId &&
                _ik == that._ik &&
                _year == that._year &&
                Objects.equals(_fabNumber, that._fabNumber) &&
                Objects.equals(_fabName, that._fabName) &&
                Objects.equals(_stationName, that._stationName) &&
                Objects.equals(_locationCode, that._locationCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(_sensitiveAreaId, _ik, _year, _fabNumber, _fabName, _stationName, _locationCode);
    }
}
