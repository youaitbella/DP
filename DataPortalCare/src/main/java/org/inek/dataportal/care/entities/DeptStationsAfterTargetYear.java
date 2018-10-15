package org.inek.dataportal.care.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author lautenti
 */
@Entity
@Table(name = "DeptStationsAfterTargetYear", schema = "care")
public class DeptStationsAfterTargetYear implements Serializable {

    private static final long serialVersionUID = 1L;

    public DeptStationsAfterTargetYear() {
    }

    public DeptStationsAfterTargetYear(DeptStationsAfterTargetYear afterTargetYearStation) {
        this._stationNameTargetYear = afterTargetYearStation.getStationNameTargetYear();
        this._locationCodeTargetYear = afterTargetYearStation.getLocationCodeTargetYear();
        this._stationName = afterTargetYearStation.getStationName();
        this._deptNumber = afterTargetYearStation.getDeptNumber();
        this._deptName = afterTargetYearStation.getDeptName();
        this._deptNameHospital = afterTargetYearStation.getDeptNameHospital();
        this._locationCode = afterTargetYearStation.getLocationCode();
        this._sensitiveArea = afterTargetYearStation.getSensitiveArea();
        this._comment = afterTargetYearStation.getComment();
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dsId")
    private Integer _id;


    public int getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Dept">
    @ManyToOne
    @JoinColumn(name = "dsDeptId")
    private Dept _dept;

    public Dept getDept() {
        return _dept;
    }

    public void setDept(Dept dept) {
        this._dept = dept;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "dsDeptName")
    private String _deptName = "";

    public String getDeptName() {
        return _deptName;
    }

    public void setDeptName(String deptName) {
        this._deptName = deptName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "dsDeptNameHospital")
    private String _deptNameHospital = "";

    public String getDeptNameHospital() {
        return _deptNameHospital;
    }

    public void setDeptNameHospital(String deptNameHospital) {
        this._deptNameHospital = deptNameHospital;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept SensitiveArea">
    @Column(name = "dsSensitiveArea")
    private String _sensitiveArea;

    public String getSensitiveArea() {
        return _sensitiveArea;
    }

    public void setSensitiveArea(String sensitiveArea) {
        this._sensitiveArea = sensitiveArea;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Number">
    @Column(name = "dsDeptNumber")
    private String _deptNumber;

    public String getDeptNumber() {
        return _deptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        this._deptNumber = deptNumber;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "dsStationName")
    private String _stationName = "";

    public String getStationName() {
        return _stationName;
    }

    public void setStationName(String stationName) {
        this._stationName = stationName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Location Code">
    @Column(name = "dsLocationCode")
    private String _locationCode;

    public String getLocationCode() {
        return _locationCode;
    }

    public void setLocationCode(String locationCode) {
        this._locationCode = locationCode;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "dsStationNameTargetYear")
    private String _stationNameTargetYear = "";

    public String getStationNameTargetYear() {
        return _stationNameTargetYear;
    }

    public void setStationNameTargetYear(String stationNameTargetYear) {
        this._stationNameTargetYear = stationNameTargetYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Location Code">
    @Column(name = "dsLocationCodeTargetYear")
    private String _locationCodeTargetYear;

    public String getLocationCodeTargetYear() {
        return _locationCodeTargetYear;
    }

    public void setLocationCodeTargetYear(String locationCodeTargetYear) {
        this._locationCodeTargetYear = locationCodeTargetYear;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "dsComment")
    private String _comment = "";

    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
        this._comment = comment;
    }
    //</editor-fold>


    @SuppressWarnings("CyclomaticComplexity")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeptStationsAfterTargetYear that = (DeptStationsAfterTargetYear) o;
        return Objects.equals(_id, that._id) &&
                Objects.equals(_dept, that._dept) &&
                Objects.equals(_deptName, that._deptName) &&
                Objects.equals(_deptNameHospital, that._deptNameHospital) &&
                Objects.equals(_sensitiveArea, that._sensitiveArea) &&
                Objects.equals(_deptNumber, that._deptNumber) &&
                Objects.equals(_stationName, that._stationName) &&
                Objects.equals(_locationCode, that._locationCode) &&
                Objects.equals(_stationNameTargetYear, that._stationNameTargetYear) &&
                Objects.equals(_locationCodeTargetYear, that._locationCodeTargetYear) &&
                Objects.equals(_comment, that._comment);
    }

    @Override
    public int hashCode() {

        return Objects.hash(_id, _dept, _deptName, _deptNameHospital, _sensitiveArea, _deptNumber, _stationName, _locationCode,
                _stationNameTargetYear, _locationCodeTargetYear, _comment);
    }
}
