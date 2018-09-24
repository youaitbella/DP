package org.inek.dataportal.care.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "DeptStation", schema = "care")
public class DeptStation implements Serializable {

    private static final long serialVersionUID = 1L;

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
    //<editor-fold defaultstate="collapsed" desc="Property Location Code">
    @Column(name = "desBeds")
    private int _beds;

    public int getBeds() {
        return _beds;
    }

    public void setBeds(int beds) {
        this._beds = beds;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this._id);
        hash = 67 * hash + Objects.hashCode(this._dept);
        hash = 67 * hash + this._beds;
        hash = 67 * hash + Objects.hashCode(this._stationName);
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
        final DeptStation other = (DeptStation) obj;
        if (this._beds != other._beds) {
            return false;
        }
        if (!Objects.equals(this._stationName, other._stationName)) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        if (!Objects.equals(this._dept, other._dept)) {
            return false;
        }
        return true;
    }

}
