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
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListCostCenter", schema = "calc")
public class KGLListCostCenter implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="id">
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterId">
    @Column(name = "ccCostCenterID")
    @Documentation(name = "Kostenstelle", rank = 10)
    private int _costCenterId;

    public int getCostCenterId() {
        return _costCenterId;
    }

    public void setCostCenterId(int costCenterId) {
        this._costCenterId = costCenterId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterNumber">
    @Column(name = "ccCostCenterNumber")
    @Documentation(name = "Nummer:", rank = 10)
    private String _costCenterNumber = "";

    @Size(max = 20)
    public String getCostCenterNumber() {
        return _costCenterNumber;
    }

    public void setCostCenterNumber(String costCenterNumber) {
        this._costCenterNumber = costCenterNumber;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterText">
    @Column(name = "ccCostCenterText")
    @Documentation(name = "Name der Kostenstelle", rank = 20)
    private String _costCenterText = "";

    @Size(max = 200, message = "Für Bezeichnung sind max. {max} Zeichen zulässig.")
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Amount">
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ccAmount")
    @Documentation(name = "Kostenvolumen", rank = 40)
    private int _amount;

    @Min(0)
    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        this._amount = amount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="FullVigorCnt">
    @Column(name = "ccFullVigorCnt")
    @Documentation(name = "Anzahl zugeordenter Vollkräfte…", rank = 60)
    private double _fullVigorCnt;

    @Min(0)
    public double getFullVigorCnt() {
        return _fullVigorCnt;
    }

    public void setFullVigorCnt(double fullVigorCnt) {
        this._fullVigorCnt = fullVigorCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceKey">
    @Column(name = "ccServiceKey")
    @Documentation(name = "Leistungsschlüssel", rank = 30)
    private String _serviceKey = "";

    @Size(max = 100, message = "Für Leistungsschlüssel sind max. {max} Zeichen zulässig.")
    public String getServiceKey() {
        return _serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this._serviceKey = serviceKey;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceKeyDescription">
    @Column(name = "ccServiceKeyDescription")
    @Documentation(name = "Beschreibung Schlüssel", rank = 50)
    private String _serviceKeyDescription = "";

    public String getServiceKeyDescription() {
        return _serviceKeyDescription;
    }

    public void setServiceKeyDescription(String serviceKeyDescription) {
        this._serviceKeyDescription = serviceKeyDescription;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceSum">
    @Column(name = "ccServiceSum")
    @Documentation(name = "Summe Leistungsschlüssel", rank = 70)
    private double _serviceSum;

    @Min(0)
    public double getServiceSum() {
        return _serviceSum;
    }

    public void setServiceSum(double serviceSum) {
        this._serviceSum = serviceSum;
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="BaseInformation">
//    @JoinColumn(name = "ccBaseInformationId", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "ccBaseInformationId")
    //  @Documentation (name = "ccBaseInformationId", rank = 80)
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    // </editor-fold>

    public KGLListCostCenter() {
    }

    public KGLListCostCenter(Integer ccID) {
        this._id = ccID;
    }

    public KGLListCostCenter(int baseInformationId, int costCenterId) {
        _baseInformationId = baseInformationId;
        _costCenterId = costCenterId;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this._id;

        if (this._id != -1) {
            return hash;
        }

        hash = 71 * hash + this._costCenterId;
        hash = 71 * hash + Objects.hashCode(this._costCenterText);
        hash = 71 * hash + (int) (Double.doubleToLongBits(this._amount) ^ (Double.doubleToLongBits(this._amount) >>> 32));
        hash = 71 * hash + (int) (Double.doubleToLongBits(this._fullVigorCnt) ^ (Double.doubleToLongBits(this._fullVigorCnt) >>> 32));
        hash = 71 * hash + Objects.hashCode(this._serviceKey);
        hash = 71 * hash + Objects.hashCode(this._serviceKeyDescription);
        hash = 71 * hash + (int) (Double.doubleToLongBits(this._serviceSum) ^ (Double.doubleToLongBits(this._serviceSum) >>> 32));
        hash = 71 * hash + this._baseInformationId;
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
        final KGLListCostCenter other = (KGLListCostCenter) obj;

        if (this._id != -1 && this._id == other._id) {
            return true;
        }

        if (this._id != other._id) {
            return false;
        }
        if (this._costCenterId != other._costCenterId) {
            return false;
        }
        if (Double.doubleToLongBits(this._amount) != Double.doubleToLongBits(other._amount)) {
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
        return "org.inek.dataportal.entities.calc.KGLListCostCenter[ ccID=" + _id + " ]";
    }
    //</editor-fold>

}
