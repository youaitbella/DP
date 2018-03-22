/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.drg;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLNormalFeeContract", schema = "calc")
public class KGLNormalFeeContract implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nfcID", updatable = false, nullable = false)
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

    //<editor-fold defaultstate="collapsed" desc="Property _division">
    @Column(name = "nfcDivision")
    @Documentation (name = "Bereich", rank = 10)
    private String _division = "";

    @Size(max = 300)
    public String getDivision() {
        return _division;
    }

    public void setDivision(String division) {
        this._division = division;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _departmentKey">
    @Column(name = "nfcDepartmentKey")
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

    //<editor-fold defaultstate="collapsed" desc="Property _caseCnt">
    @Column(name = "nfcCaseCnt")
    @Documentation (name = "Anzahl Fälle", rank = 30)
    private int _caseCnt;

    public int getCaseCnt() {
        return _caseCnt;
    }

    public void setCaseCnt(int caseCnt) {
        this._caseCnt = caseCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _amount">
    @Column(name = "nfcAmount")
    @Documentation (name = "abgegr. Kostenvolumen", rank = 40)
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
//    @JoinColumn(name = "nfcBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "nfcBaseInformationId")
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

    public KGLNormalFeeContract() {
    }

    public KGLNormalFeeContract(int baseInformationId) {
        _baseInformationId = baseInformationId;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 31 * hash + Objects.hashCode(this._division);
        hash = 31 * hash + Objects.hashCode(this._departmentKey);
        hash = 31 * hash + this._caseCnt;
        hash = 31 * hash + this._amount;
        hash = 31 * hash + this._baseInformationId;
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
        final KGLNormalFeeContract other = (KGLNormalFeeContract) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._caseCnt != other._caseCnt) {
            return false;
        }
        if (this._amount != other._amount) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (!Objects.equals(this._division, other._division)) {
            return false;
        }
        if (!Objects.equals(this._departmentKey, other._departmentKey)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLNormalFeeContract[ nfcID=" + _id + " ]";
    }
    //</editor-fold>

}
