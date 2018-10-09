package org.inek.dataportal.care.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "DeptStationsAfterTargetYear", schema = "care")
public class DeptStationsAfterTargetYear implements Serializable {

    private static final long serialVersionUID = 1L;

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

    //<editor-fold defaultstate="collapsed" desc="BaseInformation">
    @ManyToOne
    @JoinColumn(name = "dsBaseInformationId")
    private DeptBaseInformation _baseInformation;

    public DeptBaseInformation getBaseInformation() {
        return _baseInformation;
    }

    public void setBaseInformation(DeptBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
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
    private int _deptNumber;

    public int getDeptNumber() {
        return _deptNumber;
    }

    public void setDeptNumber(int deptNumber) {
        this._deptNumber = deptNumber;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Area Id">
    @Column(name = "dsDeptAreaId")
    private int _deptArea;

    public int getDeptArea() {
        return _deptArea;
    }

    public void setDeptArea(int deptArea) {
        this._deptArea = deptArea;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "dsName")
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this._id);
        hash = 13 * hash + Objects.hashCode(this._baseInformation);
        hash = 13 * hash + Objects.hashCode(this._deptName);
        hash = 13 * hash + Objects.hashCode(this._sensitiveArea);
        hash = 13 * hash + this._deptNumber;
        hash = 13 * hash + this._deptArea;
        hash = 13 * hash + Objects.hashCode(this._stationName);
        hash = 13 * hash + Objects.hashCode(this._locationCode);
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
        final DeptStationsAfterTargetYear other = (DeptStationsAfterTargetYear) obj;
        if (this._deptNumber != other._deptNumber) {
            return false;
        }
        if (this._deptArea != other._deptArea) {
            return false;
        }
        if (!Objects.equals(this._deptName, other._deptName)) {
            return false;
        }
        if (!Objects.equals(this._sensitiveArea, other._sensitiveArea)) {
            return false;
        }
        if (!Objects.equals(this._stationName, other._stationName)) {
            return false;
        }
        if (!Objects.equals(this._locationCode, other._locationCode)) {
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
