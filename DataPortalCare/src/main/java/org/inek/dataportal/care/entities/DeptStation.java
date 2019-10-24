package org.inek.dataportal.care.entities;

import org.inek.dataportal.care.entities.version.MapVersion;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author lautenti
 */
@Entity
@Table(name = "DeptWards", schema = "care")
public class DeptStation implements Serializable {

    private static final long serialVersionUID = 1L;

    public DeptStation() {
    }

    public DeptStation(DeptStation other) {
        this._wardNumber = other._wardNumber;
        this._dept = other._dept;
        this._stationName = other._stationName;
        this._locationCodeP21 = other._locationCodeP21;
        this._locationCodeVz = other._locationCodeVz;
        this._deptName = other._deptName;
        this._fab = other._fab;
        this._validFrom = other._validFrom;
        this._validTo = other._validTo;
    }


    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dpId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property WardNumber">
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "dpWardId")
    private WardNumber _wardNumber = new WardNumber();

    public WardNumber getWardNumber() {
        return _wardNumber;
    }

    public void setWardNumber(WardNumber wardNumber) {
        this._wardNumber = wardNumber;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Version Id">
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "dpVersionId")
    private MapVersion _mapVersion = new MapVersion();

    public MapVersion getMapVersion() {
        return _mapVersion;
    }

    public void setMapVersion(MapVersion mapVersion) {
        this._mapVersion = mapVersion;
    }

    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BaseInformation">
    @ManyToOne
    @JoinColumn(name = "dpDeptId")
    private Dept _dept;

    public Dept getDept() {
        return _dept;
    }

    public void setDept(Dept dept) {
        this._dept = dept;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "dpWardName")
    private String _stationName = "";

    public String getStationName() {
        return _stationName;
    }

    public void setStationName(String stationName) {
        this._stationName = stationName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Location Code P21">
    @Column(name = "dpLocationP21")
    private int _locationCodeP21;

    public int getLocationCodeP21() {
        return _locationCodeP21;
    }

    public void setLocationCodeP21(int locationCodeP21) {
        this._locationCodeP21 = locationCodeP21;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Location Code VZ">
    @Column(name = "dpLocationVz")
    private int _locationCodeVz;

    public int getLocationCodeVz() {
        return _locationCodeVz;
    }

    public void setLocationCodeVz(int locationCodeVz) {
        this._locationCodeVz = locationCodeVz;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Bed Count">
    @Column(name = "dpBedCount")
    private int _bedCount;

    public int getBedCount() {
        return _bedCount;
    }

    public void setBedCount(int bedCount) {
        this._bedCount = bedCount;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "dpDeptName")
    private String _deptName = "";

    public String getDeptName() {
        return _deptName;
    }

    public void setDeptName(String deptName) {
        this._deptName = deptName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Number">
    @Column(name = "dpFab")
    private String _fab = "";

    public String getFab() {
        return _fab;
    }

    public void setFab(String fab) {
        this._fab = fab;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Valid From">
    @Column(name = "dpValidFrom")
    private Date _validFrom;

    public Date getValidFrom() {
        return _validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this._validFrom = validFrom;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Valid To">
    @Column(name = "dpValidTo")
    private Date _validTo;

    public Date getValidTo() {
        return _validTo;
    }

    public void setValidTo(Date validTo) {
        this._validTo = validTo;
    }

    //</editor-fold>

    public boolean stationIsUnlimitedValid() {
        return _validTo.getYear() >= 2050;
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeptStation that = (DeptStation) o;
        return _locationCodeP21 == that._locationCodeP21 &&
                _locationCodeVz == that._locationCodeVz &&
                _bedCount == that._bedCount &&
                Objects.equals(_id, that._id) &&
                Objects.equals(_wardNumber, that._wardNumber) &&
                Objects.equals(_mapVersion, that._mapVersion) &&
                Objects.equals(_dept, that._dept) &&
                Objects.equals(_stationName, that._stationName) &&
                Objects.equals(_deptName, that._deptName) &&
                Objects.equals(_fab, that._fab) &&
                Objects.equals(_validFrom, that._validFrom) &&
                Objects.equals(_validTo, that._validTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _wardNumber, _mapVersion, _dept, _stationName, _locationCodeP21,
                _locationCodeVz, _bedCount, _deptName, _fab, _validFrom, _validTo);
    }
}
