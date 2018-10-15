package org.inek.dataportal.care.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author lautenti
 */
@Entity
@Table(name = "DeptStation", schema = "care")
public class DeptStation implements Serializable {

    private static final long serialVersionUID = 1L;

    public DeptStation() {

    }

    public DeptStation(DeptStation station) {
        this._stationName = station.getStationName();
        this._locationCode = station.getLocationCode();
        this._deptNumber = station.getDeptNumber();
        this._deptName = station.getDeptName();
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "desId")
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
    @JoinColumn(name = "desDeptId")
    private Dept _dept;

    public Dept getDept() {
        return _dept;
    }

    public void setDept(Dept dept) {
        this._dept = dept;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "desName")
    private String _stationName = "";

    public String getStationName() {
        return _stationName;
    }

    public void setStationName(String stationName) {
        this._stationName = stationName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Location Code">
    @Column(name = "desLocationCode")
    private String _locationCode;

    public String getLocationCode() {
        return _locationCode;
    }

    public void setLocationCode(String locationCode) {
        this._locationCode = locationCode;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "desDeptName")
    private String _deptName = "";

    public String getDeptName() {
        return _deptName;
    }

    public void setDeptName(String deptName) {
        this._deptName = deptName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Number">
    @Column(name = "desDeptNumber")
    private String _deptNumber = "";

    public String getDeptNumber() {
        return _deptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        this._deptNumber = deptNumber;
    }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeptStation that = (DeptStation) o;
        return Objects.equals(_id, that._id) &&
                Objects.equals(_dept, that._dept) &&
                Objects.equals(_stationName, that._stationName) &&
                Objects.equals(_locationCode, that._locationCode) &&
                Objects.equals(_deptName, that._deptName) &&
                Objects.equals(_deptNumber, that._deptNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _dept, _stationName, _locationCode, _deptName, _deptNumber);
    }
}
