/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import org.inek.dataportal.enums.CalcInfoType;

/**
 *
 * @author muellermi
 */
public class CalcHospitalInfoId implements Serializable {

    private int _id;
    private CalcInfoType _type;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this._id;
        if (_id != -1) return hash;
        hash = 37 * hash + this._type.hashCode();
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
        final CalcHospitalInfoId other = (CalcHospitalInfoId) obj;
        
        if (this._id != -1 && this._id == other._id) return true;
        
        if (this._id != other._id) {
            return false;
        }
        return this._type.equals(other._type);
    }
    
}
