package org.inek.dataportal.care.entities.StructuralChanges;

import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.enums.SensitiveArea;
import org.inek.dataportal.common.utils.DateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "WardsToChange", schema = "care")
public class WardsToChange implements Serializable {

    private static final long serialVersionUID = 1L;

    public WardsToChange() {
        _validTo = DateUtils.MAX_DATE;
    }

    public WardsToChange(DeptWard ward) {
        _deptId = ward.getDept().getId();
        _wardName = ward.getWardName();
        _deptName = ward.getDeptName();
        _locationP21 = ward.getLocationCodeP21();
        _locationVz = ward.getLocationText();
        _fab = ward.getFab();
        _beds = ward.getBedCount();
        setValidFrom(ward.getValidFrom());
        setValidTo(ward.getValidTo());
        _sensitiveAreaId = SensitiveArea.fromName(ward.getDept().getSensitiveArea()).getId();
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wtcId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DeptId">
    @Column(name = "wtcDeptId")
    private Integer _deptId;

    public Integer getDeptId() {
        return _deptId;
    }

    public void setDeptId(Integer deptId) {
        _deptId = deptId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property WardName">
    @Column(name = "wtcWardName")
    private String _wardName = "";

    public String getWardName() {
        return _wardName;
    }

    public void setWardName(String wardName) {
        this._wardName = wardName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DeptName">
    @Column(name = "wtcDeptName")
    private String _deptName = "";

    public String getDeptName() {
        return _deptName;
    }

    public void setDeptName(String deptName) {
        this._deptName = deptName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LocationP21">
    @Column(name = "wtcLocationP21")
    private int _locationP21;

    public int getLocationP21() {
        return _locationP21;
    }

    public void setLocationP21(int locationP21) {
        this._locationP21 = locationP21;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property BedCount">
    @Column(name = "wtcBedCount")
    private int _beds;

    public int getBeds() {
        return _beds;
    }

    public void setBeds(int beds) {
        this._beds = beds;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property SenstiveAreaId">
    @Column(name = "wtcSensitivAreaId")
    private int _sensitiveAreaId = -1;

    public int getSensitiveAreaId() {
        return _sensitiveAreaId;
    }

    public void setSensitiveAreaId(int sensitiveAreaId) {
        this._sensitiveAreaId = sensitiveAreaId;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LocationVz">
    @Column(name = "wtcLocationVz")
    private String _locationVz = "";

    public String getLocationVz() {
        return _locationVz;
    }

    public void setLocationVz(String locationVz) {
        this._locationVz = locationVz;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Fab">
    @Column(name = "wtcFab")
    private String _fab = "";

    public String getFab() {
        return _fab;
    }

    public void setFab(String fab) {
        this._fab = fab;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ValidFrom">
    @Column(name = "wtcValidFrom")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _validFrom = DateUtils.MIN_DATE;

    public Date getValidFrom() {
        return _validFrom == DateUtils.MIN_DATE ? null : _validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this._validFrom = validFrom == null ? DateUtils.MIN_DATE : validFrom;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ValidTo ">
    @Column(name = "wtcValidTo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _validTo = DateUtils.MAX_DATE;

    public Date getValidTo() {
        return _validTo;
    }

    public void setValidTo(Date validTo) {
        this._validTo = validTo == null ? DateUtils.MAX_DATE : validTo;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property wtcComment ">
    @Column(name = "wtcComment")
    private String _comment = "";

    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
        this._comment = comment;
    }

    //</editor-fold>


    @OneToOne
    @JoinColumn(name = "wtcDeptWardId", referencedColumnName = "dwId")
    private DeptWard _deptWard;

    public DeptWard getDeptWard() {
        return _deptWard;
    }

    public void setDeptWard(DeptWard deptWard) {
        this._deptWard = deptWard;
    }

    //<editor-fold defaultstate="collapsed" desc="Property StructuralChanges">
    @OneToOne(mappedBy = "_wardsToChange")
    private StructuralChanges _structuralChanges;

    public StructuralChanges getStructuralChanges() {
        return _structuralChanges;
    }

    public void setStructuralChanges(StructuralChanges structuralChanges) {
        this._structuralChanges = structuralChanges;
    }
    //</editor-fold>

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WardsToChange that = (WardsToChange) o;
        return _deptId == that._deptId &&
                _locationP21 == that._locationP21 &&
                _beds == that._beds &&
                Objects.equals(_locationVz, that._locationVz) &&
                Objects.equals(_id, that._id) &&
                Objects.equals(_wardName, that._wardName) &&
                Objects.equals(_deptName, that._deptName) &&
                Objects.equals(_fab, that._fab) &&
                Objects.equals(_validFrom, that._validFrom) &&
                Objects.equals(_validTo, that._validTo) &&
                Objects.equals(_structuralChanges, that._structuralChanges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _deptId, _wardName, _deptName, _locationP21, _beds, _locationVz,
                _fab, _validFrom, _validTo, _structuralChanges);
    }
}
