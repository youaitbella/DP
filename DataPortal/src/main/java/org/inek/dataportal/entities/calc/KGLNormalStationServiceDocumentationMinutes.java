/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "KGLNormalStationServiceDocumentationMinutes", schema = "calc")
public class KGLNormalStationServiceDocumentationMinutes implements Serializable {
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nssdmId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    @Column(name = "nssdmBaseInformationId")
    private int _baseInformationId = -1;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int _baseInformationId) {
        this._baseInformationId = _baseInformationId;
    }
    
    @Column(name = "nssdmDepartment")
    private String _department = "";

    @Size(max = 50)
    public String getDepartment() {
        return _department;
    }

    public void setDepartment(String _department) {
        this._department = _department;
    }
    
    @Column(name = "nssdmDepartmentKey")
    private String _departmentKey = "";

    @Size(max = 50)
    public String getDepartmentKey() {
        return _departmentKey;
    }

    public void setDepartmentKey(String _departmentKey) {
        this._departmentKey = _departmentKey;
    }
    
    @Column(name = "nssdmAlternative")
    private String _alternative = "";

    @Size(max = 50)
    public String getAlternative() {
        return _alternative;
    }

    public void setAlternative(String _alternative) {
        this._alternative = _alternative;
    }
}
