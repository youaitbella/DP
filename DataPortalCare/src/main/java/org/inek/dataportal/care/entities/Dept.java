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
        hash = 59 * hash + Objects.hashCode(this._baseInformation);
        hash = 59 * hash + Objects.hashCode(this._deptName);
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
        if (!Objects.equals(this._deptName, other._deptName)) {
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
