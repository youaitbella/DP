package org.inek.dataportal.care.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.care.entities.version.MapVersion;
import org.inek.dataportal.care.utils.CareValueChecker;
import org.inek.dataportal.common.utils.DateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author lautenti
 */
@Entity
@Table(name = "DeptWard", schema = "care")
public class DeptWard implements Serializable {

    private static final long serialVersionUID = 1L;

    public DeptWard() {
    }  // for JPA only!

    public DeptWard(MapVersion version) {
        this._mapVersion = version;
    }

    public DeptWard(DeptWard other) {
        _dept = other._dept;
        _wardName = other._wardName;
        _locationCodeP21 = other._locationCodeP21;
        _locationCodeVz = other._locationCodeVz;
        _locationText = other._locationText;
        _deptName = other._deptName;
        _fab = other._fab;
        _validFrom = other._validFrom;
        _validTo = other._validTo;
        _isInitial = other._isInitial;
        _mapVersion = other._mapVersion;
        _bedCount = other._bedCount;
        _location2017 = other._location2017;
    }


    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dwId")
    private Integer _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Version Id">
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "dwVersionId", referencedColumnName = "verId")
    @JsonIgnore
    private MapVersion _mapVersion;

    @JsonIgnore
    public MapVersion getMapVersion() {
        return _mapVersion;
    }

    // for Json only - do not delete
    public int getMapVersionId() {
        return _mapVersion.getId();
    }

    public void setMapVersion(MapVersion mapVersion) {
        this._mapVersion = mapVersion;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Dept">
    @ManyToOne
    @JoinColumn(name = "dwDeptId")
    @JsonIgnore
    private Dept _dept;

    @JsonIgnore
    public Dept getDept() {
        return _dept;
    }

    // for Json only - do not delete
    public int getDeptId() {
        return _dept.getId();
    }

    public void setDept(Dept dept) {
        this._dept = dept;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "dwWardName")
    private String _wardName = "";

    public String getWardName() {
        return _wardName;
    }

    public void setWardName(String wardName) {
        this._wardName = wardName;
    }
    //</editor-fold>

    //<editor-fold desc="Property Created At">
    @Column(name = "dwCreatedAt")
    private Date _createdAt = new Date();

    public Date getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this._createdAt = createdAt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Location Code P21">
    @Column(name = "dwLocationP21")
    private int _locationCodeP21;

    public int getLocationCodeP21() {
        return _locationCodeP21;
    }

    public void setLocationCodeP21(int locationCodeP21) {
        this._locationCodeP21 = locationCodeP21;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Location Code VZ">
    @Column(name = "dwLocationVz")
    private int _locationCodeVz;

    public int getLocationCodeVz() {
        return _locationCodeVz;
    }

    public void setLocationCodeVz(int locationCodeVz) {
        this._locationCodeVz = locationCodeVz;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Bed Count">
    @Column(name = "dwBedCount")
    private int _bedCount;

    public int getBedCount() {
        return _bedCount;
    }

    public void setBedCount(int bedCount) {
        this._bedCount = bedCount;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "dwDeptName")
    private String _deptName = "";

    public String getDeptName() {
        return _deptName;
    }

    public void setDeptName(String deptName) {
        this._deptName = deptName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Number">
    @Column(name = "dwFab")
    private String _fab = "";

    public String getFab() {
        return _fab;
    }

    public void setFab(String fab) {
        this._fab = fab;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Valid From">
    @Column(name = "dwValidFrom")
    private Date _validFrom = DateUtils.MIN_DATE;

    public Date getValidFrom() {
        return _validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this._validFrom = validFrom;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Valid To">
    @Column(name = "dwValidTo")
    private Date _validTo = DateUtils.MAX_DATE;

    public Date getValidTo() {
        return _validTo;
    }

    public void setValidTo(Date validTo) {
        this._validTo = validTo;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "dwIsInitial")
    private boolean _isInitial;

    public boolean getIsInitial() {
        return _isInitial;
    }

    public void setIsInitial(boolean isInitial) {
        this._isInitial = isInitial;
    }

    //</editor-fold>

    //<editor-fold desc="Property BaseDeptWardId">
    @JsonIgnore
    @Column(name = "dwBaseDeptWardId")
    private int baseDeptWardId;

    @JsonIgnore
    public int getBaseDeptWardId() {
        return baseDeptWardId == 0 ? _id : baseDeptWardId;
    }

    public void setBaseDeptWardId(int baseDeptWardId) {
        this.baseDeptWardId = baseDeptWardId;
    }
    //</editor-fold>

    @Column(name = "dwLocation2017")
    private String _location2017 = "";

    public String getLocation2017() {
        return _location2017;
    }

    public void setLocation2017(String location2017) {
        this._location2017 = location2017;
    }

    @Column(name = "dwLocationText")
    private String _locationText = "";

    public String getLocationText() {
        return _locationText;
    }

    public void setLocationText(String locationText) {
        this._locationText = locationText;
        this._locationCodeVz = CareValueChecker.extractFormalValidVzNumber(locationText);
    }

    @JsonIgnore
    public String getValidToDisplayText() {
        if (_validTo.equals(DateUtils.MAX_DATE)) {
            return "unbegrenzt";
        } else {
            Format formatter = new SimpleDateFormat("dd.MM.yyyy");
            return formatter.format(_validTo);
        }
    }


    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeptWard that = (DeptWard) o;
        return _locationCodeP21 == that._locationCodeP21 &&
                _locationCodeVz == that._locationCodeVz &&
                _bedCount == that._bedCount &&
                Objects.equals(_id, that._id) &&
                Objects.equals(_mapVersion, that._mapVersion) &&
                Objects.equals(_dept, that._dept) &&
                Objects.equals(_wardName, that._wardName) &&
                Objects.equals(_deptName, that._deptName) &&
                Objects.equals(_fab, that._fab) &&
                Objects.equals(_validFrom, that._validFrom) &&
                Objects.equals(_validTo, that._validTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _mapVersion, _dept, _wardName, _locationCodeP21,
                _locationCodeVz, _bedCount, _deptName, _fab, _validFrom, _validTo);
    }

    @Override
    public String toString() {
        return "DeptWard{" +
                "_id=" + _id +
                ", _mapVersion=" + _mapVersion +
                ", _dept=" + _dept +
                ", _wardName='" + _wardName + '\'' +
                ", _locationCodeP21=" + _locationCodeP21 +
                ", _locationCodeVz=" + _locationCodeVz +
                ", _bedCount=" + _bedCount +
                ", _deptName='" + _deptName + '\'' +
                ", _fab='" + _fab + '\'' +
                ", _validFrom=" + _validFrom +
                ", _validTo=" + _validTo +
                '}';
    }
}
