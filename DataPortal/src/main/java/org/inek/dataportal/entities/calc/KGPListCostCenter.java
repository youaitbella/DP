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
import org.inek.dataportal.entities.calc.iface.ListCostCenter;
import org.inek.dataportal.entities.calc.iface.BaseIdValue;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListCostCenter", schema = "calc")
public class KGPListCostCenter implements Serializable, ListCostCenter, BaseIdValue {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ccID", updatable = false, nullable = false)
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

    //<editor-fold defaultstate="collapsed" desc="Property _costCenterId">
    @Column(name = "ccCostCenterID")
    @Documentation(name = "Kostenstelle", rank = 10)
    private int _costCenterId;

    @Override
    public int getCostCenterId() {
        return _costCenterId;
    }

    @Override
    public void setCostCenterId(int costCenterId) {
        this._costCenterId = costCenterId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costCenterNumber">
    @Column(name = "ccCostCenterNumber")
    private int _costCenterNumber;

    @Override
    public int getCostCenterNumber() {
        return _costCenterNumber;
    }

    @Override
    public void setCostCenterNumber(int costCenterNumber) {
        this._costCenterNumber = costCenterNumber;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _costCenterText">
    @Column(name = "ccCostCenterText")
    @Documentation(name = "Name der Kostenstelle", rank = 20)
    private String _costCenterText = "";

    @Override
    @Size(max = 50)
    public String getCostCenterText() {
        return _costCenterText;
    }

    @Override
    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _amount">
    @Column(name = "ccAmount")
    @Documentation(name = "Kostenvolumen", rank = 30)
    private int _amount;

    @Override
    public int getAmount() {
        return _amount;
    }

    @Override
    public void setAmount(int amount) {
        this._amount = amount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _fullVigorCnt">
    @Column(name = "ccFullVigorCnt")
    @Documentation(name = "Anzahl zugeordenter Vollkräfte…", rank = 40)
    private double _fullVigorCnt;

    @Override
    public double getFullVigorCnt() {
        return _fullVigorCnt;
    }

    @Override
    public void setFullVigorCnt(double fullVigorCnt) {
        this._fullVigorCnt = fullVigorCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceKey">
    @Column(name = "ccServiceKey")
    @Documentation(name = "Leistungsschlüssel", rank = 50)
    private String _serviceKey = "";

    @Override
    @Size(max = 50)
    public String getServiceKey() {
        return _serviceKey;
    }

    @Override
    public void setServiceKey(String serviceKey) {
        this._serviceKey = serviceKey;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceKeyDescription">
    @Column(name = "ccServiceKeyDescription")
    @Documentation(name = "Beschreibung Leistungsschlüssel", rank = 60)
    private String _serviceKeyDescription = "";

    @Override
    public String getServiceKeyDescription() {
        return _serviceKeyDescription;
    }

    @Override
    public void setServiceKeyDescription(String serviceKeyDescription) {
        this._serviceKeyDescription = serviceKeyDescription;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _serviceSum">
    @Column(name = "ccServiceSum")
    @Documentation(name = "Summe Leistungseinheiten", rank = 70)
    private double _serviceSum;

    @Override
    public double getServiceSum() {
        return _serviceSum;
    }

    @Override
    public void setServiceSum(double serviceSum) {
        this._serviceSum = serviceSum;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property _baseInformationId">
    @Column(name = "ccBaseInformationId")
    private int _baseInformationId = -1;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    public KGPListCostCenter() {
    }

    public KGPListCostCenter(Integer ccID) {
        this._id = ccID;
    }

    public KGPListCostCenter(int baseInformationId, int costCenterId) {
        _baseInformationId = baseInformationId;
        _costCenterId = costCenterId;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 79 * hash + this._costCenterId;
        hash = 79 * hash + this._costCenterNumber;
        hash = 79 * hash + Objects.hashCode(this._costCenterText);
        hash = 79 * hash + this._amount;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this._fullVigorCnt) ^ (Double.doubleToLongBits(this._fullVigorCnt) >>> 32));
        hash = 79 * hash + Objects.hashCode(this._serviceKey);
        hash = 79 * hash + Objects.hashCode(this._serviceKeyDescription);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this._serviceSum) ^ (Double.doubleToLongBits(this._serviceSum) >>> 32));
        hash = 79 * hash + this._baseInformationId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof KGPListCostCenter)) {
            return false;
        }
        final KGPListCostCenter other = (KGPListCostCenter) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._costCenterId != other._costCenterId) {
            return false;
        }
        if (this._costCenterNumber != other._costCenterNumber) {
            return false;
        }
        if (this._amount != other._amount) {
            return false;
        }
        if (Double.doubleToLongBits(this._fullVigorCnt) != Double.doubleToLongBits(other._fullVigorCnt)) {
            return false;
        }
        if (Double.doubleToLongBits(this._serviceSum) != Double.doubleToLongBits(other._serviceSum)) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
            return false;
        }
        if (!Objects.equals(this._costCenterText, other._costCenterText)) {
            return false;
        }
        if (!Objects.equals(this._serviceKey, other._serviceKey)) {
            return false;
        }
        if (!Objects.equals(this._serviceKeyDescription, other._serviceKeyDescription)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListCostCenter[ ccID=" + _id + " ]";
    }
    //</editor-fold>

}
