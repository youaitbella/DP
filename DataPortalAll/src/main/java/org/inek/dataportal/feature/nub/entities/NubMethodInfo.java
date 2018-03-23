/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author muellermi
 */
@Entity 
public class NubMethodInfo implements Serializable {
    
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

    // <editor-fold defaultstate="collapsed" desc="Property MethodId">
    @Column(name = "ciFKCaId")
    private int _methodId;

    public int getMethodId() {
        return _methodId;
    }

    public void setMethodId(int methodId) {
        _methodId = methodId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property BaseYear">
    @Column(name = "BaseYear")
    private int _baseYear;

    public int getBaseYear() {
        return _baseYear;
    }

    public void setBaseYear(int baseYear) {
        _baseYear = baseYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Sequence">
    @Column(name = "Seq")
    private int _sequence;

    public int getSequence() {
        return _sequence;
    }

    public void setSequence(int sequence) {
        _sequence = sequence;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "Name")
    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Type">
    @Column(name = "nmnType")
    private String _type;

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Text">
    @Column(name = "nmnText")
    private String _text;

    public String getText() {
        return _text;
    }

    public void setTexte(String text ){
        _text = text;
    }
    // </editor-fold>


}
