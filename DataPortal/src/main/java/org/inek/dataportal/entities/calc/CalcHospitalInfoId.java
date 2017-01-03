/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;

/**
 *
 * @author muellermi
 */
public class CalcHospitalInfoId implements Serializable {

    private int _id;
    private int _type;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this._id;
        hash = 37 * hash + this._type;
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
        if (this._id != other._id) {
            return false;
        }
        if (this._type != other._type) {
            return false;
        }
        return true;
    }

    
}
