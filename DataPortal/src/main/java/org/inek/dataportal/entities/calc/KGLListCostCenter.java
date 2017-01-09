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
@Table(name = "KGLListCostCenter", schema = "calc")
@XmlRootElement
public class KGLListCostCenter implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ccID")
    private int _id = -1;
    
    public int getID() {
        return _id;
    }

    public void setID(int ccID) {
        this._id = ccID;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="CostCenterID">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ccCostCenterID")
    private int _costCenterID;
    
    public int getCostCenterID() {
        return _costCenterID;
    }

    public void setCostCenterID(int costCenterID) {
        this._costCenterID = costCenterID;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="CostCenterText">
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "ccCostCenterText")
    private String _costCenterText;
    
    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Amount">
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "ccAmount")
    private double _amount;
    
    public double getAmount() {
        return _amount;
    }

    public void setAmount(double amount) {
        this._amount = amount;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="FullVigorCnt">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ccFullVigorCnt")
    private double _fullVigorCnt;
    
    public double getFullVigorCnt() {
        return _fullVigorCnt;
    }

    public void setFullVigorCnt(double fullVigorCnt) {
        this._fullVigorCnt = fullVigorCnt;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ServiceKey">
    @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "ccServiceKey")
    private String _serviceKey;

    public String getServiceKey() {
        return _serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this._serviceKey = serviceKey;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ServiceKeyDescription">
    @Basic(optional = false)
    @NotNull
    @Size(max = 2147483647)
    @Column(name = "ccServiceKeyDescription")
    private String _serviceKeyDescription;
    
    public String getServiceKeyDescription() {
        return _serviceKeyDescription;
    }

    public void setServiceKeyDescription(String serviceKeyDescription) {
        this._serviceKeyDescription = serviceKeyDescription;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ServiceSum">
    @Basic(optional = false)
    @NotNull
    @Column(name = "ccServiceSum")
    private double _serviceSum;

    public double getServiceSum() {
        return _serviceSum;
    }

    public void setServiceSum(double serviceSum) {
        this._serviceSum = serviceSum;
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="BaseInformation">
//    @JoinColumn(name = "ccBaseInformationID", referencedColumnName = "biID")
//    @ManyToOne(optional = false)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ccBaseInformationID")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    // </editor-fold>

    public KGLListCostCenter() {
    }

    public KGLListCostCenter(Integer ccID) {
        this._id = ccID;
    }

    public KGLListCostCenter(Integer id, int costCenterID, String costCenterText, double amount, double fullVigorCnt, String ccServiceKey, String ccServiceKeyDescription, double serviceSum) {
        this._id = id;
        this._costCenterID = costCenterID;
        this._costCenterText = costCenterText;
        this._amount = amount;
        this._fullVigorCnt = fullVigorCnt;
        this._serviceKey = ccServiceKey;
        this._serviceKeyDescription = ccServiceKeyDescription;
        this._serviceSum = serviceSum;
    }

    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this._id;
        
        if (this._id != -1) return hash;
        
        hash = 71 * hash + this._costCenterID;
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
        
        if (this._id != -1 && this._id == other._id) return true;
        
        if (this._id != other._id) {
            return false;
        }
        if (this._costCenterID != other._costCenterID) {
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
