package org.inek.dataportal.care.entities.StructuralChanges;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "WardsToChange", schema = "care")
public class WardsToChange implements Serializable {

    private static final long serialVersionUID = 1L;

    public WardsToChange() {
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
    private int _deptId;

    public int getDeptId() {
        return _deptId;
    }

    public void setDeptId(Integer deptId) {
        _deptId = deptId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property WardName">
    @Column(name = "wtcWardName")
    private String _wardName;

    public String getWardName() {
        return _wardName;
    }

    public void setWardName(String wardName) {
        this._wardName = wardName;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DeptName">
    @Column(name = "wtcDeptName")
    private String _deptName;

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

    //<editor-fold defaultstate="collapsed" desc="Property LocationVz">
    @Column(name = "wtcLocationVz")
    private int _locationVz;

    public int getLocationVz() {
        return _locationVz;
    }

    public void setLocationVz(int locationVz) {
        this._locationVz = locationVz;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Fab">
    @Column(name = "wtcFab")
    private String _fab;

    public String getFab() {
        return _fab;
    }

    public void setFab(String fab) {
        this._fab = fab;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ValidFrom">
    @Column(name = "wtcValidFrom")
    private Date _validFrom;

    public Date getValidFrom() {
        return _validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this._validFrom = validFrom;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ValidTo ">
    @Column(name = "wtcValidTo")
    private Date _validTo;

    public Date getValidTo() {
        return _validTo;
    }

    public void setValidTo(Date validTo) {
        this._validTo = validTo;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StructuralChangesWards">
    @OneToOne(mappedBy = "_wardsToChange")
    private StructuralChangesBaseInformation _structuralChangesBaseInformation;

    public StructuralChangesBaseInformation getStructuralChangesBaseInformation() {
        return _structuralChangesBaseInformation;
    }

    public void setStructuralChangesBaseInformation(StructuralChangesBaseInformation structuralChangesBaseInformation) {
        this._structuralChangesBaseInformation = structuralChangesBaseInformation;
    }
    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc="Property StructuralChangesWards">
    @OneToOne(mappedBy = "_wardsToChange")
    private StructuralChangesWards _structuralChangesWards;

    public StructuralChangesWards getStructuralChangesWards() {
        return _structuralChangesWards;
    }

    public void setStructuralChangesWards(StructuralChangesWards structuralChangesWards) {
        this._structuralChangesWards = structuralChangesWards;
    }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WardsToChange that = (WardsToChange) o;
        return _deptId == that._deptId &&
                _locationP21 == that._locationP21 &&
                _locationVz == that._locationVz &&
                Objects.equals(_id, that._id) &&
                Objects.equals(_wardName, that._wardName) &&
                Objects.equals(_deptName, that._deptName) &&
                Objects.equals(_fab, that._fab) &&
                Objects.equals(_validFrom, that._validFrom) &&
                Objects.equals(_validTo, that._validTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _deptId, _wardName, _deptName, _locationP21, _locationVz, _fab, _validFrom, _validTo);
    }
}
