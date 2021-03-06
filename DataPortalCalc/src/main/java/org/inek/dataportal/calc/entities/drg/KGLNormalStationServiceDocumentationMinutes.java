/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "KGLNormalStationServiceDocumentationMinutes", schema = "calc")
public class KGLNormalStationServiceDocumentationMinutes implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;
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
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }

    @Column(name = "nssdmDepartment")
    @Documentation(name = "Fachabteilung")
    private String _department = "";

    @Size(max = 50)
    public String getDepartment() {
        return _department;
    }

    public void setDepartment(String department) {
        this._department = department;
    }

    @Column(name = "nssdmDepartmentKey")
    @Documentation(name = "FAB-Schlüssel")
    private String _departmentKey = "";

    @Size(max = 4)
    public String getDepartmentKey() {
        return _departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this._departmentKey = departmentKey;
    }

    @Column(name = "nssdmAlternative")
    @Documentation(name = "Alternativverfahren")
    private String _alternative = "";

    @Size(max = 50)
    public String getAlternative() {
        return _alternative;
    }

    public void setAlternative(String alternative) {
        this._alternative = alternative;
    }
}
