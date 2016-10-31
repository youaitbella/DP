/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author muellermi
 */
@Entity 
public class CalculationHospitalInfo implements Serializable {
    
    // <editor-fold defaultstate="collapsed" desc="Property RowNum">
    @Column(name = "rowNum")
    @Id
    private int _rowNum;

    public int getRowNum() {
        return _rowNum;
    }

    public void setRowNum(int rowNum) {
        _rowNum = rowNum;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property BaseYear">
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        _dataYear = dataYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IK">
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Type">
    private String _type;

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }
    // </editor-fold>

}
