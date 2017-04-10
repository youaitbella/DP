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
import org.inek.dataportal.entities.calc.iface.BaseIdValue;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "KGLNormalStationServiceDocumentationMinutes", schema = "calc")
public class KGLNormalStationServiceDocumentationMinutes implements Serializable, BaseIdValue {
    
    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nssdmId", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    @Column(name = "nssdmBaseInformationId")
    private int _baseInformationId = -1;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int _baseInformationId) {
        this._baseInformationId = _baseInformationId;
    }
    
    @Column(name = "nssdmDepartment")
    @Documentation(name = "Fachabteilung")
    private String _department = "";

    @Size(max = 50)
    public String getDepartment() {
        return _department;
    }

    public void setDepartment(String _department) {
        this._department = _department;
    }
    
    @Column(name = "nssdmDepartmentKey")
    @Documentation(name = "FAB-Schlüssel")
    private String _departmentKey = "";

    @Size(max = 4)
    public String getDepartmentKey() {
        return _departmentKey;
    }

    public void setDepartmentKey(String _departmentKey) {
        this._departmentKey = _departmentKey;
    }
    
    @Column(name = "nssdmAlternative")
    @Documentation(name = "Alternativverfahren")
    private String _alternative = "";

    @Size(max = 50)
    public String getAlternative() {
        return _alternative;
    }

    public void setAlternative(String _alternative) {
        this._alternative = _alternative;
    }
}
