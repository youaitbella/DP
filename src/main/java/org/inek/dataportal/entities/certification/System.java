package org.inek.dataportal.entities.certification;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "System", schema = "crt")
public class System implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Properties">
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "syId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="RemunerationId">
    @Column(name = "syRemunerationId")
    private int _remunerationId;

    public int getRemunerationId() {
        return _remunerationId;
    }

    public void setRemunerationId(int remunerationId) {
        _remunerationId = remunerationId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="YearData">
    @Column(name = "syYearData")
    private int _yearData;

    @Min(2010) @Max(2030)
    public int getYearData() {
        return _yearData;
    }

    public void setYearData(int value) {
        _yearData = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="YearSystem">
    @Column(name = "syYearSystem")
    private int _yearSystem;

    @Min(2010) @Max(2030)
    public int getYearSystem() {
        return _yearSystem;
    }

    public void setYearSystem(int value) {
        _yearSystem = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="YearSystem">
    @Column(name = "syApproved")
    private boolean _approved;

    public boolean isApproved() {
        return _approved;
    }

    public void setApproved(boolean approved) {
        _approved = approved;
    }
    // </editor-fold>

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof System)) {
            return false;
        }
        System other = (System) object;
        return _id == other._id;
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }
    // </editor-fold>

}
