/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListMedInfra", schema = "calc")
public class KGLListMedInfra implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "miID")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costTypeID">
    @Column(name = "miCostTypeID")
    private int _costTypeID;

    public int getCostTypeID() {
        return _costTypeID;
    }

    public void setCostTypeID(int costTypeID) {
        this._costTypeID = costTypeID;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costCenter">
    @Column(name = "miCostCenter")
    @Size(max = 20)
    private String _costCenter = "";

    public String getCostCenter() {
        return _costCenter;
    }

    public void setCostCenter(String costCenter) {
        this._costCenter = costCenter;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costCenterText">
    @Column(name = "miCostCenterText")
    @Size(max = 100)
    private String _costCenterText = "";

    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _keyUsed">
    @Column(name = "miKeyUsed")
    @Size(max = 50)
    private String _keyUsed = "";

    public String getKeyUsed() {
        return _keyUsed;
    }

    public void setKeyUsed(String keyUsed) {
        this._keyUsed = keyUsed;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _amount">
    @Column(name = "miAmount")
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationID">
    @Column(name = "miBaseInformationID")
    private int _baseInformationID;

    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    //</editor-fold>

    public KGLListMedInfra() {
    }

    public KGLListMedInfra(Integer miID) {
        this._id = miID;
    }

    public KGLListMedInfra(Integer miID, int miCostTypeID, String miCostCenter, String miCostCenterText, String miKeyUsed, int miAmount) {
        this._id = miID;
        this._costTypeID = miCostTypeID;
        this._costCenter = miCostCenter;
        this._costCenterText = miCostCenterText;
        this._keyUsed = miKeyUsed;
        this._amount = miAmount;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this._id;

        if (this._id != -1) {
            return hash;
        }

        hash = 29 * hash + this._costTypeID;
        hash = 29 * hash + Objects.hashCode(this._costCenter);
        hash = 29 * hash + Objects.hashCode(this._costCenterText);
        hash = 29 * hash + Objects.hashCode(this._keyUsed);
        hash = 29 * hash + this._amount;
        hash = 29 * hash + this._baseInformationID;
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
        final KGLListMedInfra other = (KGLListMedInfra) obj;

        if (this._id != -1 && this._id == other._id) {
            return true;
        }

        if (this._id != other._id) {
            return false;
        }
        if (this._costTypeID != other._costTypeID) {
            return false;
        }
        if (this._amount != other._amount) {
            return false;
        }
        if (this._baseInformationID != other._baseInformationID) {
            return false;
        }
        if (!Objects.equals(this._costCenter, other._costCenter)) {
            return false;
        }
        if (!Objects.equals(this._costCenterText, other._costCenterText)) {
            return false;
        }
        return Objects.equals(this._keyUsed, other._keyUsed);
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListMedInfra[ miID=" + _id + " ]";
    }
    //</editor-fold>

}
