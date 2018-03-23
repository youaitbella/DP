package org.inek.dataportal.feature.valuationratio.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "ValuationRatioMedians", schema = "vr")
public class ValuationRatioMedian implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vrmId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int value) {
        _id = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Data Year">
    @Column(name = "vrmDataYear")
    @Documentation(key = "lblYear")
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }
    // </editor-fold>
    
    @Column(name = "vrmDrg")
    private String _drg;

    public String getDrg() {
        return _drg;
    }
    
    @Column(name = "vrmMedian")
    private int _median;

    public int getMedian() {
        return _median;
    }
    
    @Column(name = "vrmFactor")
    private float _factor;

    public float getFactor() {
        return _factor;
    }
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this._id;
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
        final ValuationRatioMedian other = (ValuationRatioMedian) obj;
        if (this._id != other._id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.InsuranceNubNotice[id=" + _id + "]";
    }
    // </editor-fold>

}
