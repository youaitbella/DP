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

    public void setId(int id) {
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
    //<editor-fold defaultstate="collapsed" desc="Property Location Code">
    @Column(name = "deLocationNumber")
    private int _locationCode;

    public int getLocationCode() {
        return _locationCode;
    }

    public void setLocationCode(int locationCode) {
        this._locationCode = locationCode;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Property Dept Name">
    @Column(name = "deDeptName")
    private String _deptName = "";

    public String getDeptName() {
        return _deptName;
    }

    public void setDeptName(String _deptName) {
        this._deptName = _deptName;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this._id);
        hash = 59 * hash + Objects.hashCode(this._baseInformation);
        hash = 59 * hash + this._locationCode;
        hash = 59 * hash + Objects.hashCode(this._deptName);
        hash = 59 * hash + Objects.hashCode(this._deptStations);
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
        if (this._locationCode != other._locationCode) {
            return false;
        }
        if (!Objects.equals(this._deptName, other._deptName)) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        if (!Objects.equals(this._baseInformation, other._baseInformation)) {
            return false;
        }
        if (!Objects.equals(this._deptStations, other._deptStations)) {
            return false;
        }
        return true;
    }

}
