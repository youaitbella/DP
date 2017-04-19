/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListMedInfra", schema = "calc")
public class KGLListMedInfra implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property _id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "miID", updatable = false, nullable = false)
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

    //<editor-fold defaultstate="collapsed" desc="Property _costTypeId">
    @Column(name = "miCostTypeID")
    @Documentation (name = "Kostenartengruppe", rank = 10)
    private int _costTypeId;

    public int getCostTypeId() {
        return _costTypeId;
    }

    public void setCostTypeId(int costTypeId) {
        this._costTypeId = costTypeId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costCenter">
    @Column(name = "miCostCenterNumber")
    @Documentation (name = "Nummer der Kostenstelle:", rank = 20)
    private String _costCenterNumber = "";

    @Size(max = 100)
    public String getCostCenterNumber() {
        return _costCenterNumber;
    }

    public void setCostCenterNumber(String costCenterNumber) {
        this._costCenterNumber = costCenterNumber;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costCenterText">
    @Column(name = "miCostCenterText")
    @Documentation (name = "Name der Kostenstelle", rank = 30)
    private String _costCenterText = "";

    @Size(max = 200)
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _keyUsed">
    @Column(name = "miKeyUsed")
    @Documentation (name = "Verwendeter Schlüssel", rank = 40)
    private String _keyUsed = "";

    @Size(max = 200)
    public String getKeyUsed() {
        return _keyUsed;
    }

    public void setKeyUsed(String keyUsed) {
        this._keyUsed = keyUsed;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _amount">
    @Column(name = "miAmount")
    @Documentation (name = "Kostenvolumen:", rank = 50)
    private int _amount;

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
    @Column(name = "miBaseInformationId")
    //@Documentation (name = "_baseInformationId:", rank = 50)
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

    public KGLListMedInfra() {
    }

    public KGLListMedInfra(Integer miID) {
        this._id = miID;
    }

    public KGLListMedInfra(Integer miID, int miCostTypeID, String miCostCenter, String miCostCenterText, String miKeyUsed, int miAmount, int baseInfo) {
        this._id = miID;
        this._costTypeId = miCostTypeID;
        this._costCenterNumber = miCostCenter;
        this._costCenterText = miCostCenterText;
        this._keyUsed = miKeyUsed;
        this._amount = miAmount;
        this._baseInformationId = baseInfo;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this._id;

        if (this._id != -1) {
            return hash;
        }

        hash = 29 * hash + this._costTypeId;
        hash = 29 * hash + Objects.hashCode(this._costCenterNumber);
        hash = 29 * hash + Objects.hashCode(this._costCenterText);
        hash = 29 * hash + Objects.hashCode(this._keyUsed);
        hash = 29 * hash + this._amount;
        hash = 29 * hash + this._baseInformationId;
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
        if (this._costTypeId != other._costTypeId) {
            return false;
        }
        if (this._amount != other._amount) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (!Objects.equals(this._costCenterNumber, other._costCenterNumber)) {
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
