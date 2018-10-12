package org.inek.dataportal.care.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "Dept", schema = "care")
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deId")
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
    @JoinColumn(name = "deBaseInformationId")
    private DeptBaseInformation _baseInformation;

    public DeptBaseInformation getBaseInformation() {
        return _baseInformation;
    }

    public void setBaseInformation(DeptBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "deDeptName")
    private String _deptName = "";

    public String getDeptName() {
        return _deptName;
    }

    public void setDeptName(String deptName) {
        this._deptName = deptName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept SensitiveArea">
    @Column(name = "deSensitiveArea")
    private String _sensitiveArea;

    public String getSensitiveArea() {
        return _sensitiveArea;
    }

    public void setSensitiveArea(String sensitiveArea) {
        this._sensitiveArea = sensitiveArea;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Number">
    @Column(name = "deDeptNumber")
    private String _deptNumber;

    public String getDeptNumber() {
        return _deptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        this._deptNumber = deptNumber;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Area Id">
    @Column(name = "deDeptAreaId")
    private int _deptArea;

    public int getDeptArea() {
        return _deptArea;
    }

    public void setDeptArea(int deptArea) {
        this._deptArea = deptArea;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Required">
    @Column(name = "deRequired")
    private Boolean _required;

    public Boolean getRequired() {
        return _required;
    }

    public void setRequired(Boolean required) {
        this._required = required;
    }
    //</editor-fold>

    @OneToMany(mappedBy = "_dept", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "desDeptId")
    private List<DeptStation> _deptStations = new ArrayList<>();

    public List<DeptStation> getDeptStations() {
        return _deptStations;
    }

    public void setDeptStations(List<DeptStation> deptStations) {
        this._deptStations = deptStations;
    }

    public void addNewDeptStation() {
        DeptStation deptStation = new DeptStation();
        deptStation.setDept(this);
        _deptStations.add(deptStation);
    }

    public void removeDeptStation(DeptStation deptStation) {
        _deptStations.remove(deptStation);
    }

    @OneToMany(mappedBy = "_dept", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dsDeptId")
    private List<DeptStationsAfterTargetYear> _deptsAftertargetYear = new ArrayList<>();

    public List<DeptStationsAfterTargetYear> getDeptsAftertargetYear() {
        return _deptsAftertargetYear;
    }

    public void setDeptsAftertargetYear(List<DeptStationsAfterTargetYear> deptsAftertargetYear) {
        this._deptsAftertargetYear = deptsAftertargetYear;
    }

    public void addNewDeptAfterTargetYear() {
        DeptStationsAfterTargetYear dept = new DeptStationsAfterTargetYear();
        dept.setDept(this);
        _deptsAftertargetYear.add(dept);
    }

    public void removeDeptAfterTargetYear(DeptStationsAfterTargetYear dept) {
        _deptsAftertargetYear.remove(dept);
    }

    public void addDeptAfterTargetYear(DeptStationsAfterTargetYear dept) {
        _deptsAftertargetYear.add(dept);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this._id);
        hash = 37 * hash + Objects.hashCode(this._baseInformation);
        hash = 37 * hash + Objects.hashCode(this._deptName);
        hash = 37 * hash + Objects.hashCode(this._sensitiveArea);
        hash = 37 * hash + Objects.hashCode(this._deptNumber);
        hash = 37 * hash + this._deptArea;
        hash = 37 * hash + Objects.hashCode(this._required);
        hash = 37 * hash + Objects.hashCode(this._deptStations);
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
        final Dept other = (Dept) obj;
        if (this._deptArea != other._deptArea) {
            return false;
        }
        if (!Objects.equals(this._deptName, other._deptName)) {
            return false;
        }
        if (!Objects.equals(this._sensitiveArea, other._sensitiveArea)) {
            return false;
        }
        if (!Objects.equals(this._deptNumber, other._deptNumber)) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        if (!Objects.equals(this._baseInformation, other._baseInformation)) {
            return false;
        }
        if (!Objects.equals(this._required, other._required)) {
            return false;
        }
        if (!Objects.equals(this._deptStations, other._deptStations)) {
            return false;
        }
        return true;
    }

}
