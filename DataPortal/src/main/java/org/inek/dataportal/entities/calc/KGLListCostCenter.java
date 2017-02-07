/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import org.inek.dataportal.entities.calc.iface.ListCostCenter;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListCostCenter", schema = "calc")
public class KGLListCostCenter implements Serializable, ListCostCenter {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ccID")
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
    private int _costCenterId;

    @Override
    public int getCostCenterId() {
        return _costCenterId;
    }

    @Override
    public void setCostCenterId(int costCenterId) {
        this._costCenterId = costCenterId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterNumber">
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterText">
    @Size(max = 50)
    @Column(name = "ccCostCenterText")
    private String _costCenterText = "";

    @Override
    public String getCostCenterText() {
        return _costCenterText;
    }

    @Override
    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Amount">
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ccAmount")
    private int _amount;

    @Override
    public int getAmount() {
        return _amount;
    }

    @Override
    public void setAmount(int amount) {
        this._amount = amount;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="FullVigorCnt">
    @Column(name = "ccFullVigorCnt")
    private double _fullVigorCnt;

    @Override
    public double getFullVigorCnt() {
        return _fullVigorCnt;
    }

    @Override
    public void setFullVigorCnt(double fullVigorCnt) {
        this._fullVigorCnt = fullVigorCnt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceKey">
    @Size(max = 50)
    @Column(name = "ccServiceKey")
    private String _serviceKey = "";

    @Override
    public String getServiceKey() {
        return _serviceKey;
    }

    @Override
    public void setServiceKey(String serviceKey) {
        this._serviceKey = serviceKey;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceKeyDescription">
    @Column(name = "ccServiceKeyDescription")
    private String _serviceKeyDescription = "";

    @Override
    public String getServiceKeyDescription() {
        return _serviceKeyDescription;
    }

    @Override
    public void setServiceKeyDescription(String serviceKeyDescription) {
        this._serviceKeyDescription = serviceKeyDescription;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceSum">
    @Column(name = "ccServiceSum")
    private double _serviceSum;

    @Override
    public double getServiceSum() {
        return _serviceSum;
    }

    @Override
    public void setServiceSum(double serviceSum) {
        this._serviceSum = serviceSum;
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="BaseInformation">
//    @JoinColumn(name = "ccBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Column(name = "ccBaseInformationID")
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
