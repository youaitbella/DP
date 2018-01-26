package org.inek.dataportal.entities.valuationratio;

import java.io.Serializable;
import javax.persistence.*;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "ValuationRatioDrgCount", schema = "vr")
public class ValuationRatioDrgCount implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vrdcId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int value) {
        _id = value;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Data Year">
    @Column(name = "vrdcDataYear")
    @Documentation(key = "lblYear")
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }
    // </editor-fold>
    
    @Column(name = "vrdcDrg")
    private String _drg;

    public String getDrg() {
        return _drg;
    }
    
    @Column(name = "vrdcCount")
    private int _count;

    public int getCount() {
        return _count;
    }
    
    @Column(name = "vrdcIk")
    private int _ik;

    public int getIk() {
        return _ik;
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
        final ValuationRatioDrgCount other = (ValuationRatioDrgCount) obj;
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
