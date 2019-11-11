package org.inek.dataportal.care.entities.StructuralChanges;

import org.inek.dataportal.care.entities.DeptWard;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "StructuralChangesWards", schema = "care")
public class StructuralChangesWards implements Serializable {

    private static final long serialVersionUID = 1L;

    public StructuralChangesWards() {
    }

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scwId")
    private Integer _id;

    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc="Property StructuralChangesBaseInformation">

    @ManyToOne
    @JoinColumn(name = "scwStructuralChangesId")
    private StructuralChangesBaseInformation _structuralChangesBaseInformation;

    //</editor-fold>

    @OneToOne
    @JoinColumn(name = "scwDeptWardId", referencedColumnName = "dwId")
    private DeptWard _deptWard;

    public DeptWard getDeptWard() {
        return _deptWard;
    }

    public void setDeptWard(DeptWard deptWard) {
        this._deptWard = deptWard;
    }

    //<editor-fold defaultstate="collapsed" desc="Property WardsToChangeId">
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "scwWardsToChangeId", referencedColumnName  = "wtcId")
    private WardsToChange _wardsToChange;

    //</editor-fold>


    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public StructuralChangesBaseInformation getStructuralChangesBaseInformation() {
        return _structuralChangesBaseInformation;
    }

    public void setStructuralChangesBaseInformation(StructuralChangesBaseInformation structuralChangesBaseInformation) {
        this._structuralChangesBaseInformation = structuralChangesBaseInformation;
    }

    public WardsToChange getWardsToChange() {
        return _wardsToChange;
    }

    public void setWardsToChange(WardsToChange wardsToChange) {
        this._wardsToChange = wardsToChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructuralChangesWards that = (StructuralChangesWards) o;
        return Objects.equals(_id, that._id) &&
                Objects.equals(_wardsToChange, that._wardsToChange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _wardsToChange);
    }
}
