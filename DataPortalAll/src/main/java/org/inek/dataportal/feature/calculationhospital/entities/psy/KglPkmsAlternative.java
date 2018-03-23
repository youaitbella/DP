/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital.entities.psy;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLPKMSAlternative", schema = "calc")
@XmlRootElement
public class KglPkmsAlternative implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paID", updatable = false, nullable = false)
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

    //<editor-fold defaultstate="collapsed" desc="Property _department">
    @Column(name = "paDepartment")
    @Documentation (name = "Fachabteilung", rank = 10)
    private String _department = "";

    @Size(max = 200)
    public String getDepartment() {
        return _department;
    }

    public void setDepartment(String department) {
        this._department = department;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _departmentKey">
    @Column(name = "paDepartmentKey")
    @Documentation (name = "FAB Schlüssel 301", rank = 20)
    private String _departmentKey = "";

    @Size(max = 4)
    public String getDepartmentKey() {
        return _departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this._departmentKey = departmentKey;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _alternative">
    @Column(name = "paAlternative")
    @Documentation (name = "Alternativverfahren", rank = 30)
    private String _alternative = "";

    @Size(max = 200)
    public String getAlternative() {
        return _alternative;
    }

    public void setAlternative(String alternative) {
        this._alternative = alternative;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "paBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "paBaseInformationId")
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    public KglPkmsAlternative() {
    }

    public KglPkmsAlternative(Integer baseInformationId) {
        _baseInformationId = baseInformationId;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 61 * hash + Objects.hashCode(this._department);
        hash = 61 * hash + Objects.hashCode(this._departmentKey);
        hash = 61 * hash + Objects.hashCode(this._alternative);
        hash = 61 * hash + this._baseInformationId;
        return hash;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
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
        final KglPkmsAlternative other = (KglPkmsAlternative) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (!Objects.equals(this._department, other._department)) {
            return false;
        }
        if (!Objects.equals(this._departmentKey, other._departmentKey)) {
            return false;
        }
        if (!Objects.equals(this._alternative, other._alternative)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLPKMSAlternative[ paID=" + _id + " ]";
    }
    //</editor-fold>

}
