package org.inek.dataportal.care.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.care.entities.version.MapVersion;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "Dept", schema = "care")
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    public Dept() {}
    public Dept(DeptBaseInformation baseInformation) {
        this._baseInformation = baseInformation;
    }

    public Dept(Dept dept) {
        _deptArea = dept.getDeptArea();
        _deptName = dept.getDeptName();
        _deptNumber = dept.getDeptNumber();
        _sensitiveArea = dept.getSensitiveArea();
        _required = dept.getRequired();
        _seeDeptAreaId = dept.getSeeDeptAreaId();
        _location = dept.getLocation();
        _required = dept.getRequired();

        for (DeptWard ward : dept.getDeptWards()) {
            DeptWard deptWard = new DeptWard(ward);
            addDeptWard(deptWard);
        }

        for (DeptStationsAfterTargetYear afterTargetYearStation : dept.getDeptsAftertargetYear()) {
            DeptStationsAfterTargetYear newStation = new DeptStationsAfterTargetYear(afterTargetYearStation);
            newStation.setDept(this);
            addDeptAfterTargetYear(newStation);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deId")
    private Integer _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BaseInformation">
    @ManyToOne
    @JoinColumn(name = "deBaseInformationId")
    @JsonIgnore
    private DeptBaseInformation _baseInformation;

    @JsonIgnore
    public DeptBaseInformation getBaseInformation() {
        return _baseInformation;
    }

    // for Json only - do not delete
    public int getBaseInformationId() {
        return _baseInformation.getId();
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
    private String _sensitiveArea = "";

    public String getSensitiveArea() {
        return _sensitiveArea;
    }

    public void setSensitiveArea(String sensitiveArea) {
        this._sensitiveArea = sensitiveArea;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Number">
    @Column(name = "deDeptNumber")
    private String _deptNumber = "";

    public String getDeptNumber() {
        return _deptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        this._deptNumber = deptNumber;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Number">
    @Column(name = "deLocationP21")
    private String _location = "";

    public String getLocation() {
        return _location;
    }

    public void setLocation(String location) {
        this._location = location;
    }

    @JsonIgnore
    public String getLocationForDisplay() {
        return "Standort " + _location;
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

    //<editor-fold defaultstate="collapsed" desc="Property See Dept Area Id">
    @Column(name = "deSeeDeptAreaId")
    private int _seeDeptAreaId;

    public int getSeeDeptAreaId() {
        return _seeDeptAreaId;
    }

    public void setSeeDeptAreaId(int deptArea) {
        this._seeDeptAreaId = deptArea;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Required">
    @Column(name = "deRequired")
    private boolean _required;

    public boolean getRequired() {
        return _required;
    }

    public void setRequired(boolean required) {
        this._required = required;
    }
    //</editor-fold>

    @OneToMany(mappedBy = "_dept", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dwDeptId", referencedColumnName = "deId")
    private List<DeptWard> _deptWards = new ArrayList<>();

    public List<DeptWard> getDeptWards() {
        return _deptWards;
    }

    public List<DeptWard> getInitDeptWards() {
        return _deptWards.stream().filter(DeptWard::getIsInitial).collect(Collectors.toList());
    }

    public void setDeptWards(List<DeptWard> deptWards) {
        this._deptWards = deptWards;
    }

    public void addNewInitialDeptWard(MapVersion version, Date validFrom, Date validTo) {
        addDeptWard(version, validFrom, validTo, true);
    }

    public DeptWard addNewDeptWard(MapVersion version, Date validFrom, Date validTo) {
        return addDeptWard(version, validFrom, validTo, false);
    }

    private DeptWard addDeptWard(MapVersion version, Date validFrom, Date validTo, boolean isInitial) {
        DeptWard deptWard = new DeptWard(version);
        deptWard.setDept(this);
        deptWard.setDeptName(_deptName);
        deptWard.setFab(_deptNumber);
        deptWard.setValidFrom(validFrom);
        deptWard.setValidTo(validTo);
        deptWard.setIsInitial(isInitial);
        _deptWards.add(deptWard);
        return deptWard;
    }

    public void addDeptWard(DeptWard ward) {
        ward.setDept(this);
        _deptWards.add(ward);
    }

    public void removeDeptStation(DeptWard deptWard) {
        _deptWards.remove(deptWard);
    }

    @OneToMany(mappedBy = "_dept", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dsDeptId")
    @JsonIgnore
    private List<DeptStationsAfterTargetYear> _deptsAftertargetYear = new ArrayList<>();

    @JsonIgnore
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
        hash = 37 * hash + Objects.hashCode(this._required);
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
        return true;
    }

}
